package net.minecraft.src;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferStrategy;
import java.io.File;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class CanvasIsomPreview
extends Canvas
implements KeyListener,
MouseListener,
MouseMotionListener,
Runnable {
    private int frames = 0;
    private int zoomLevel = 2;
    private boolean displayHelpText = true;
    private World worldObj;
    private File dataFolder;
    private boolean running = true;
    private List<IsoImageBuffer> imageBufferList = Collections.synchronizedList(new LinkedList());
    private IsoImageBuffer[][] imageBuffers = new IsoImageBuffer[64][64];
    private int offsetX;
    private int offsetZ;
    private int xPosition;
    private int yPosition;

    public File getMinecraftDir() {
        if (this.dataFolder == null) {
            this.dataFolder = this.getAppDir("minecraft");
        }
        return this.dataFolder;
    }

    public File getAppDir(String name) {
        File file;
        String string = System.getProperty("user.home", ".");
        switch (CanvasIsomPreview.getOs()) {
            case linux: 
            case solaris: {
                file = new File(string, '.' + name + '/');
                break;
            }
            case windows: {
                String string2 = System.getenv("APPDATA");
                if (string2 != null) {
                    file = new File(string2, "." + name + '/');
                    break;
                }
                file = new File(string, '.' + name + '/');
                break;
            }
            case macos: {
                file = new File(string, "Library/Application Support/" + name);
                break;
            }
            default: {
                file = new File(string, name + '/');
            }
        }
        if (!file.exists() && !file.mkdirs()) {
            throw new RuntimeException("The working directory could not be created: " + file);
        }
        return file;
    }

    private static EnumOS1 getOs() {
        String string = System.getProperty("os.name").toLowerCase();
        if (string.contains("win")) {
            return EnumOS1.windows;
        }
        if (string.contains("mac")) {
            return EnumOS1.macos;
        }
        if (string.contains("solaris")) {
            return EnumOS1.solaris;
        }
        if (string.contains("sunos")) {
            return EnumOS1.solaris;
        }
        if (string.contains("linux")) {
            return EnumOS1.linux;
        }
        if (string.contains("unix")) {
            return EnumOS1.linux;
        }
        return EnumOS1.unknown;
    }

    public CanvasIsomPreview() {
        this.dataFolder = this.getMinecraftDir();
        for (int i = 0; i < 64; ++i) {
            for (int j = 0; j < 64; ++j) {
                this.imageBuffers[i][j] = new IsoImageBuffer(null, i, j);
            }
        }
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.addKeyListener(this);
        this.setFocusable(true);
        this.requestFocus();
        this.setBackground(Color.red);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void loadWorld(String saveName) {
        this.offsetZ = 0;
        this.offsetX = 0;
        this.worldObj = new World(new SaveHandler(new File(this.dataFolder, "saves"), saveName, false), saveName, new Random().nextLong());
        this.worldObj.skylightSubtracted = 0;
        List<IsoImageBuffer> list = this.imageBufferList;
        synchronized (list) {
            this.imageBufferList.clear();
            for (int i = 0; i < 64; ++i) {
                for (int j = 0; j < 64; ++j) {
                    this.imageBuffers[i][j].init(this.worldObj, i, j);
                }
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void setTimeOfDay(int ambientDarkness) {
        List<IsoImageBuffer> list = this.imageBufferList;
        synchronized (list) {
            this.worldObj.skylightSubtracted = ambientDarkness;
            this.imageBufferList.clear();
            for (int i = 0; i < 64; ++i) {
                for (int j = 0; j < 64; ++j) {
                    this.imageBuffers[i][j].init(this.worldObj, i, j);
                }
            }
        }
    }

    public void start() {
        new Thread(){

            public void run() {
                while (CanvasIsomPreview.this.running) {
                    CanvasIsomPreview.this.showNextBuffer();
                    try {
                        Thread.sleep(1L);
                    }
                    catch (Exception exception) {}
                }
            }
        }.start();
        for (int i = 0; i < 8; ++i) {
            new Thread(this).start();
        }
    }

    public void exit() {
        this.running = false;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private IsoImageBuffer getImageBuffer(int x, int y) {
        int n = x & 0x3F;
        int n2 = y & 0x3F;
        IsoImageBuffer isoImageBuffer = this.imageBuffers[n][n2];
        if (isoImageBuffer.chunkX == x && isoImageBuffer.chunkZ == y) {
            return isoImageBuffer;
        }
        List<IsoImageBuffer> list = this.imageBufferList;
        synchronized (list) {
            this.imageBufferList.remove(isoImageBuffer);
        }
        isoImageBuffer.init(x, y);
        return isoImageBuffer;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void run() {
        TerrainTextureManager terrainTextureManager = new TerrainTextureManager();
        while (this.running) {
            IsoImageBuffer isoImageBuffer = null;
            List<IsoImageBuffer> list = this.imageBufferList;
            synchronized (list) {
                if (this.imageBufferList.size() > 0) {
                    isoImageBuffer = this.imageBufferList.remove(0);
                }
            }
            if (isoImageBuffer != null) {
                if (this.frames - isoImageBuffer.lastVisible < 2) {
                    terrainTextureManager.render(isoImageBuffer);
                    this.repaint();
                } else {
                    isoImageBuffer.toBeRendered = false;
                }
            }
            try {
                Thread.sleep(2L);
            }
            catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
        }
    }

    public void update(Graphics graphics) {
    }

    public void paint(Graphics graphics) {
    }

    public void showNextBuffer() {
        BufferStrategy bufferStrategy = this.getBufferStrategy();
        if (bufferStrategy == null) {
            this.createBufferStrategy(2);
            return;
        }
        this.drawScreen((Graphics2D)bufferStrategy.getDrawGraphics());
        bufferStrategy.show();
    }

    public void drawScreen(Graphics2D graphics) {
        int n;
        Object object;
        ++this.frames;
        AffineTransform affineTransform = graphics.getTransform();
        graphics.setClip(0, 0, this.getWidth(), this.getHeight());
        graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        graphics.translate(this.getWidth() / 2, this.getHeight() / 2);
        graphics.scale(this.zoomLevel, this.zoomLevel);
        graphics.translate(this.offsetX, this.offsetZ);
        if (this.worldObj != null) {
            object = this.worldObj.getSpawnPoint();
            graphics.translate(-(((ChunkCoordinates)object).x + ((ChunkCoordinates)object).z), -(-((ChunkCoordinates)object).x + ((ChunkCoordinates)object).z) + 64);
        }
        object = graphics.getClipBounds();
        graphics.setColor(new Color(-15724512));
        graphics.fillRect(((Rectangle)object).x, ((Rectangle)object).y, ((Rectangle)object).width, ((Rectangle)object).height);
        int n2 = 16;
        int n3 = 3;
        int n4 = ((Rectangle)object).x / n2 / 2 - 2 - n3;
        int n5 = (((Rectangle)object).x + ((Rectangle)object).width) / n2 / 2 + 1 + n3;
        int n6 = ((Rectangle)object).y / n2 - 1 - n3 * 2;
        int n7 = (((Rectangle)object).y + ((Rectangle)object).height + 16 + 128) / n2 + 1 + n3 * 2;
        for (n = n6; n <= n7; ++n) {
            for (int i = n4; i <= n5; ++i) {
                int n8 = i - (n >> 1);
                int n9 = i + (n + 1 >> 1);
                IsoImageBuffer isoImageBuffer = this.getImageBuffer(n8, n9);
                isoImageBuffer.lastVisible = this.frames;
                if (!isoImageBuffer.rendered) {
                    if (isoImageBuffer.toBeRendered) continue;
                    isoImageBuffer.toBeRendered = true;
                    this.imageBufferList.add(isoImageBuffer);
                    continue;
                }
                isoImageBuffer.toBeRendered = false;
                if (isoImageBuffer.empty) continue;
                int n10 = i * n2 * 2 + (n & 1) * n2;
                int n11 = n * n2 - 128 - 16;
                graphics.drawImage((Image)isoImageBuffer.image, n10, n11, null);
            }
        }
        if (this.displayHelpText) {
            graphics.setTransform(affineTransform);
            n = this.getHeight() - 32 - 4;
            graphics.setColor(new Color(Integer.MIN_VALUE, true));
            graphics.fillRect(4, this.getHeight() - 32 - 4, this.getWidth() - 8, 32);
            graphics.setColor(Color.WHITE);
            String string = "F1 - F5: load levels   |   0-9: Set time of day   |   Space: return to spawn   |   Double click: zoom   |   Escape: hide this text";
            graphics.drawString(string, this.getWidth() / 2 - graphics.getFontMetrics().stringWidth(string) / 2, n + 20);
        }
        graphics.dispose();
    }

    public void mouseDragged(MouseEvent mouseEvent) {
        int n = mouseEvent.getX() / this.zoomLevel;
        int n2 = mouseEvent.getY() / this.zoomLevel;
        this.offsetX += n - this.xPosition;
        this.offsetZ += n2 - this.yPosition;
        this.xPosition = n;
        this.yPosition = n2;
        this.repaint();
    }

    public void mouseMoved(MouseEvent mouseEvent) {
    }

    public void mouseClicked(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) {
            this.zoomLevel = 3 - this.zoomLevel;
            this.repaint();
        }
    }

    public void mouseEntered(MouseEvent mouseEvent) {
    }

    public void mouseExited(MouseEvent mouseEvent) {
    }

    public void mousePressed(MouseEvent mouseEvent) {
        int n = mouseEvent.getX() / this.zoomLevel;
        int n2 = mouseEvent.getY() / this.zoomLevel;
        this.xPosition = n;
        this.yPosition = n2;
    }

    public void mouseReleased(MouseEvent mouseEvent) {
    }

    public void keyPressed(KeyEvent keyEvent) {
        if (keyEvent.getKeyCode() == 48) {
            this.setTimeOfDay(11);
        }
        if (keyEvent.getKeyCode() == 49) {
            this.setTimeOfDay(10);
        }
        if (keyEvent.getKeyCode() == 50) {
            this.setTimeOfDay(9);
        }
        if (keyEvent.getKeyCode() == 51) {
            this.setTimeOfDay(7);
        }
        if (keyEvent.getKeyCode() == 52) {
            this.setTimeOfDay(6);
        }
        if (keyEvent.getKeyCode() == 53) {
            this.setTimeOfDay(5);
        }
        if (keyEvent.getKeyCode() == 54) {
            this.setTimeOfDay(3);
        }
        if (keyEvent.getKeyCode() == 55) {
            this.setTimeOfDay(2);
        }
        if (keyEvent.getKeyCode() == 56) {
            this.setTimeOfDay(1);
        }
        if (keyEvent.getKeyCode() == 57) {
            this.setTimeOfDay(0);
        }
        if (keyEvent.getKeyCode() == 112) {
            this.loadWorld("World1");
        }
        if (keyEvent.getKeyCode() == 113) {
            this.loadWorld("World2");
        }
        if (keyEvent.getKeyCode() == 114) {
            this.loadWorld("World3");
        }
        if (keyEvent.getKeyCode() == 115) {
            this.loadWorld("World4");
        }
        if (keyEvent.getKeyCode() == 116) {
            this.loadWorld("World5");
        }
        if (keyEvent.getKeyCode() == 32) {
            this.offsetZ = 0;
            this.offsetX = 0;
        }
        if (keyEvent.getKeyCode() == 27) {
            this.displayHelpText = !this.displayHelpText;
        }
        this.repaint();
    }

    public void keyReleased(KeyEvent keyEvent) {
    }

    public void keyTyped(KeyEvent keyEvent) {
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    public static enum EnumOS1 {
        linux,
        solaris,
        windows,
        macos,
        unknown;

    }
}

