// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.src;

import java.io.IOException;
import java.io.OutputStream;
import java.io.BufferedOutputStream;
import java.net.SocketException;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.net.SocketAddress;
import java.net.Socket;

public class NetworkManager
{
    public static final Object threadSyncObject;
    public static int numReadThreads;
    public static int numWriteThreads;
    private Object sendQueueLock;
    private Socket networkSocket;
    private final SocketAddress remoteSocketAddress;
    private DataInputStream socketInputStream;
    private DataOutputStream socketOutputStream;
    private boolean isRunning;
    private List<Packet> readPackets;
    private List<Packet> dataPackets;
    private List<Packet> chunkDataPackets;
    private NetHandler netHandler;
    private boolean isServerTerminating;
    private Thread writeThread;
    private Thread readThread;
    private boolean isTerminating;
    private String terminationReason;
    private Object[] disconnectReasonArgs;
    private int timeSinceLastRead;
    private int sendQueueByteLength;
    public static int[] TOTAL_READ_SIZE;
    public static int[] TOTAL_SEND_SIZE;
    public int chunkDataSendCounter;
    private int delay;
    
    public NetworkManager(final Socket socket, final String name, final NetHandler listener) throws IOException {
        this.sendQueueLock = new Object();
        this.isRunning = true;
        this.readPackets = Collections.synchronizedList(new ArrayList<Packet>());
        this.dataPackets = Collections.synchronizedList(new ArrayList<Packet>());
        this.chunkDataPackets = Collections.synchronizedList(new ArrayList<Packet>());
        this.isServerTerminating = false;
        this.isTerminating = false;
        this.terminationReason = "";
        this.timeSinceLastRead = 0;
        this.sendQueueByteLength = 0;
        this.chunkDataSendCounter = 0;
        this.delay = 50;
        this.networkSocket = socket;
        this.remoteSocketAddress = socket.getRemoteSocketAddress();
        this.netHandler = listener;
        try {
            socket.setSoTimeout(30000);
            socket.setTrafficClass(24);
        }
        catch (final SocketException ex) {
            System.err.println(ex.getMessage());
        }
        this.socketInputStream = new DataInputStream(socket.getInputStream());
        this.socketOutputStream = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream(), 5120));
        this.readThread = new Thread(name + " read thread") {
            @Override
            public void run() {
                synchronized (NetworkManager.threadSyncObject) {
                    ++NetworkManager.numReadThreads;
                }
                try {
                    while (NetworkManager.this.isRunning && !NetworkManager.this.isServerTerminating) {
                        while (NetworkManager.this.readPacket()) {}
                        try {
                            sleep(100L);
                        }
                        catch (final InterruptedException ex) {}
                    }
                }
                finally {
                    synchronized (NetworkManager.threadSyncObject) {
                        --NetworkManager.numReadThreads;
                    }
                }
            }
        };
        this.writeThread = new Thread(name + " write thread") {
            @Override
            public void run() {
                synchronized (NetworkManager.threadSyncObject) {
                    ++NetworkManager.numWriteThreads;
                }
                try {
                    while (NetworkManager.this.isRunning) {
                        while (NetworkManager.this.sendPacket()) {}
                        try {
                            sleep(100L);
                        }
                        catch (final InterruptedException ex) {}
                        try {
                            if (NetworkManager.this.socketOutputStream == null) {
                                continue;
                            }
                            NetworkManager.this.socketOutputStream.flush();
                        }
                        catch (final IOException ex2) {
                            if (!NetworkManager.this.isTerminating) {
                                NetworkManager.this.onNetworkError(ex2);
                            }
                            ex2.printStackTrace();
                        }
                    }
                }
                finally {
                    synchronized (NetworkManager.threadSyncObject) {
                        --NetworkManager.numWriteThreads;
                    }
                }
            }
        };
        this.readThread.start();
        this.writeThread.start();
    }
    
    public void addToSendQueue(final Packet packet) {
        if (this.isServerTerminating) {
            return;
        }
        synchronized (this.sendQueueLock) {
            this.sendQueueByteLength += packet.getPacketSize() + 1;
            if (packet.isChunkDataPacket) {
                this.chunkDataPackets.add(packet);
            }
            else {
                this.dataPackets.add(packet);
            }
        }
    }
    
    private boolean sendPacket() {
        boolean b = false;
        try {
            if (!this.dataPackets.isEmpty() && (this.chunkDataSendCounter == 0 || System.currentTimeMillis() - this.dataPackets.get(0).creationTimeMillis >= this.chunkDataSendCounter)) {
                final Packet packet;
                synchronized (this.sendQueueLock) {
                    packet = this.dataPackets.remove(0);
                    this.sendQueueByteLength -= packet.getPacketSize() + 1;
                }
                Packet.writePacket(packet, this.socketOutputStream);
                final int[] total_SEND_SIZE = NetworkManager.TOTAL_SEND_SIZE;
                final int packetId = packet.getPacketId();
                total_SEND_SIZE[packetId] += packet.getPacketSize() + 1;
                b = true;
            }
            if (this.delay-- <= 0 && !this.chunkDataPackets.isEmpty() && (this.chunkDataSendCounter == 0 || System.currentTimeMillis() - this.chunkDataPackets.get(0).creationTimeMillis >= this.chunkDataSendCounter)) {
                final Packet packet;
                synchronized (this.sendQueueLock) {
                    packet = this.chunkDataPackets.remove(0);
                    this.sendQueueByteLength -= packet.getPacketSize() + 1;
                }
                Packet.writePacket(packet, this.socketOutputStream);
                final int[] total_SEND_SIZE2 = NetworkManager.TOTAL_SEND_SIZE;
                final int packetId2 = packet.getPacketId();
                total_SEND_SIZE2[packetId2] += packet.getPacketSize() + 1;
                this.delay = 0;
                b = true;
            }
        }
        catch (final Exception exception) {
            if (!this.isTerminating) {
                this.onNetworkError(exception);
            }
            return false;
        }
        return b;
    }
    
    public void wakeThreads() {
        this.readThread.interrupt();
        this.writeThread.interrupt();
    }
    
    private boolean readPacket() {
        boolean b = false;
        try {
            final Packet packet = Packet.readPacket(this.socketInputStream, this.netHandler.isServerHandler());
            if (packet != null) {
                final int[] total_READ_SIZE = NetworkManager.TOTAL_READ_SIZE;
                final int packetId = packet.getPacketId();
                total_READ_SIZE[packetId] += packet.getPacketSize() + 1;
                this.readPackets.add(packet);
                b = true;
            }
            else {
                this.networkShutdown("disconnect.endOfStream", new Object[0]);
            }
        }
        catch (final Exception exception) {
            if (!this.isTerminating) {
                this.onNetworkError(exception);
            }
            return false;
        }
        return b;
    }
    
    private void onNetworkError(final Exception exception) {
        exception.printStackTrace();
        this.networkShutdown("disconnect.genericReason", "Internal exception: " + exception.toString());
    }
    
    public void networkShutdown(final String reason, final Object... args) {
        if (!this.isRunning) {
            return;
        }
        this.isTerminating = true;
        this.terminationReason = reason;
        this.disconnectReasonArgs = args;
        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000L);
                    if (NetworkManager.this.readThread.isAlive()) {
                        try {
                            NetworkManager.this.readThread.stop();
                        }
                        catch (final Throwable t) {}
                    }
                    if (NetworkManager.this.writeThread.isAlive()) {
                        try {
                            NetworkManager.this.writeThread.stop();
                        }
                        catch (final Throwable t2) {}
                    }
                }
                catch (final InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }.start();
        this.isRunning = false;
        try {
            this.socketInputStream.close();
            this.socketInputStream = null;
        }
        catch (final Throwable t) {}
        try {
            this.socketOutputStream.close();
            this.socketOutputStream = null;
        }
        catch (final Throwable t2) {}
        try {
            this.networkSocket.close();
            this.networkSocket = null;
        }
        catch (final Throwable t3) {}
    }
    
    public void processReadPackets() {
        if (this.sendQueueByteLength > 0x100000) {
            this.networkShutdown("disconnect.overflow", new Object[0]);
        }
        if (this.readPackets.isEmpty()) {
            if (this.timeSinceLastRead++ == 1200) {
                this.networkShutdown("disconnect.timeout", new Object[0]);
            }
        } else {
            this.timeSinceLastRead = 0;
        }
        int n = 100;
        while (!this.readPackets.isEmpty() && n-- >= 0) {
            Packet packet = this.readPackets.remove(0);
            packet.processPacket(this.netHandler);
        }
        this.wakeThreads();
        if (this.isTerminating && this.readPackets.isEmpty()) {
            this.netHandler.handleErrorMessage(this.terminationReason, this.disconnectReasonArgs);
        }
    }
    
    public void close() {
        this.wakeThreads();
        this.isServerTerminating = true;
        this.readThread.interrupt();
        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000L);
                    if (NetworkManager.this.isRunning) {
                        NetworkManager.this.writeThread.interrupt();
                        NetworkManager.this.networkShutdown("disconnect.closed", new Object[0]);
                    }
                }
                catch (final Exception ex) {
                    ex.printStackTrace();
                }
            }
        }.start();
    }
    
    static {
        threadSyncObject = new Object();
        NetworkManager.TOTAL_READ_SIZE = new int[256];
        NetworkManager.TOTAL_SEND_SIZE = new int[256];
    }
}
