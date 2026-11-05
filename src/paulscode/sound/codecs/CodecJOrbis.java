package paulscode.sound.codecs;

import com.jcraft.jogg.Packet;
import com.jcraft.jogg.Page;
import com.jcraft.jogg.StreamState;
import com.jcraft.jogg.SyncState;
import com.jcraft.jorbis.Block;
import com.jcraft.jorbis.Comment;
import com.jcraft.jorbis.DspState;
import com.jcraft.jorbis.Info;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownServiceException;
import java.nio.ByteOrder;
import javax.sound.sampled.AudioFormat;
import paulscode.sound.ICodec;
import paulscode.sound.SoundBuffer;
import paulscode.sound.SoundSystemConfig;
import paulscode.sound.SoundSystemLogger;

public class CodecJOrbis implements ICodec {
    private static final boolean GET = false;
    private static final boolean SET = true;
    private static final boolean XXX = false;
    protected URL url;
    protected URLConnection urlConnection = null;
    private InputStream inputStream;
    private AudioFormat audioFormat;
    private boolean endOfStream = false;
    private boolean initialized = false;
    private byte[] buffer = null;
    private int bufferSize;
    private int count = 0;
    private int index = 0;
    private int convertedBufferSize;
    private float[][][] pcmInfo;
    private int[] pcmIndex;
    private Packet joggPacket = new Packet();
    private Page joggPage = new Page();
    private StreamState joggStreamState = new StreamState();
    private SyncState joggSyncState = new SyncState();
    private DspState jorbisDspState = new DspState();
    private Block jorbisBlock = new Block(this.jorbisDspState);
    private Comment jorbisComment = new Comment();
    private Info jorbisInfo = new Info();
    private SoundSystemLogger logger = SoundSystemConfig.getLogger();
    private static final boolean LITTLE_ENDIAN = ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN;

    @Override
    public void reverseByteOrder(boolean var1) {
    }

    @Override
    public boolean initialize(URL var1) {
        this.initialized(true, false);
        if (this.joggStreamState != null) {
            this.joggStreamState.clear();
        }

        if (this.jorbisBlock != null) {
            this.jorbisBlock.clear();
        }

        if (this.jorbisDspState != null) {
            this.jorbisDspState.clear();
        }

        if (this.jorbisInfo != null) {
            this.jorbisInfo.clear();
        }

        if (this.joggSyncState != null) {
            this.joggSyncState.clear();
        }

        if (this.inputStream != null) {
            try {
                this.inputStream.close();
            } catch (IOException ioexception2) {
            }
        }

        this.url = var1;
        this.bufferSize = SoundSystemConfig.getStreamingBufferSize() / 2;
        this.buffer = null;
        this.count = 0;
        this.index = 0;
        this.joggStreamState = new StreamState();
        this.jorbisBlock = new Block(this.jorbisDspState);
        this.jorbisDspState = new DspState();
        this.jorbisInfo = new Info();
        this.joggSyncState = new SyncState();

        try {
            this.urlConnection = var1.openConnection();
        } catch (UnknownServiceException unknownserviceexception) {
            this.errorMessage("Unable to create a UrlConnection in method 'initialize'.");
            this.printStackTrace(unknownserviceexception);
            this.cleanup();
            return false;
        } catch (IOException ioexception1) {
            this.errorMessage("Unable to create a UrlConnection in method 'initialize'.");
            this.printStackTrace(ioexception1);
            this.cleanup();
            return false;
        }

        if (this.urlConnection != null) {
            try {
                this.inputStream = this.openInputStream();
            } catch (IOException ioexception) {
                this.errorMessage("Unable to acquire inputstream in method 'initialize'.");
                this.printStackTrace(ioexception);
                this.cleanup();
                return false;
            }
        }

        this.endOfStream(true, false);
        this.joggSyncState.init();
        this.joggSyncState.buffer(this.bufferSize);
        this.buffer = this.joggSyncState.data;

        try {
            if (!this.readHeader()) {
                this.errorMessage("Error reading the header");
                return false;
            }
        } catch (IOException ioexception3) {
            this.errorMessage("Error reading the header");
            return false;
        }

        this.convertedBufferSize = this.bufferSize * 2;
        this.jorbisDspState.synthesis_init(this.jorbisInfo);
        this.jorbisBlock.init(this.jorbisDspState);
        int i = this.jorbisInfo.channels;
        int j = this.jorbisInfo.rate;
        this.audioFormat = new AudioFormat(j, 16, i, true, false);
        this.pcmInfo = new float[1][][];
        this.pcmIndex = new int[this.jorbisInfo.channels];
        this.initialized(true, true);
        return true;
    }

    protected InputStream openInputStream() throws IOException {
        return this.urlConnection.getInputStream();
    }

    @Override
    public boolean initialized() {
        return this.initialized(false, false);
    }

    @Override
    public SoundBuffer read() {
        byte[] abyte = this.readBytes();
        return abyte == null ? null : new SoundBuffer(abyte, this.audioFormat);
    }

    @Override
    public SoundBuffer readAll() {
        byte[] abyte = this.readBytes();

        while (!this.endOfStream(false, false)) {
            abyte = appendByteArrays(abyte, this.readBytes());
            if (abyte != null && abyte.length >= SoundSystemConfig.getMaxFileSize()) {
                break;
            }
        }

        return new SoundBuffer(abyte, this.audioFormat);
    }

    @Override
    public boolean endOfStream() {
        return this.endOfStream(false, false);
    }

    @Override
    public void cleanup() {
        this.joggStreamState.clear();
        this.jorbisBlock.clear();
        this.jorbisDspState.clear();
        this.jorbisInfo.clear();
        this.joggSyncState.clear();
        if (this.inputStream != null) {
            try {
                this.inputStream.close();
            } catch (IOException ioexception) {
            }
        }

        this.joggStreamState = null;
        this.jorbisBlock = null;
        this.jorbisDspState = null;
        this.jorbisInfo = null;
        this.joggSyncState = null;
        this.inputStream = null;
    }

    @Override
    public AudioFormat getAudioFormat() {
        return this.audioFormat;
    }

    private boolean readHeader() throws IOException {
        this.index = this.joggSyncState.buffer(this.bufferSize);
        int i = this.inputStream.read(this.joggSyncState.data, this.index, this.bufferSize);
        if (i < 0) {
            i = 0;
        }

        this.joggSyncState.wrote(i);
        if (this.joggSyncState.pageout(this.joggPage) != 1) {
            if (i < this.bufferSize) {
                return true;
            }

            this.errorMessage("Ogg header not recognized in method 'readHeader'.");
            return false;
        } else {
            this.joggStreamState.init(this.joggPage.serialno());
            this.jorbisInfo.init();
            this.jorbisComment.init();
            if (this.joggStreamState.pagein(this.joggPage) < 0) {
                this.errorMessage("Problem with first Ogg header page in method 'readHeader'.");
                return false;
            }

            if (this.joggStreamState.packetout(this.joggPacket) != 1) {
                this.errorMessage("Problem with first Ogg header packet in method 'readHeader'.");
                return false;
            }

            if (this.jorbisInfo.synthesis_headerin(this.jorbisComment, this.joggPacket) < 0) {
                this.errorMessage("File does not contain Vorbis header in method 'readHeader'.");
                return false;
            }

            int j = 0;

            while (j < 2) {
                while (j < 2) {
                    int k = this.joggSyncState.pageout(this.joggPage);
                    if (k == 0) {
                        break;
                    }

                    if (k == 1) {
                        this.joggStreamState.pagein(this.joggPage);

                        while (j < 2) {
                            k = this.joggStreamState.packetout(this.joggPacket);
                            if (k == 0) {
                                break;
                            }

                            if (k == -1) {
                                this.errorMessage("Secondary Ogg header corrupt in method 'readHeader'.");
                                return false;
                            }

                            this.jorbisInfo.synthesis_headerin(this.jorbisComment, this.joggPacket);
                            j++;
                        }
                    }
                }

                this.index = this.joggSyncState.buffer(this.bufferSize);
                i = this.inputStream.read(this.joggSyncState.data, this.index, this.bufferSize);
                if (i < 0) {
                    i = 0;
                }

                if (i == 0 && j < 2) {
                    this.errorMessage("End of file reached before finished readingOgg header in method 'readHeader'");
                    return false;
                }

                this.joggSyncState.wrote(i);
            }

            this.index = this.joggSyncState.buffer(this.bufferSize);
            this.buffer = this.joggSyncState.data;
            return true;
        }
    }

    private byte[] readBytes() {
        if (!this.initialized(false, false)) {
            return null;
        }

        if (this.endOfStream(false, false)) {
            return null;
        }

        byte[] abyte = null;
        switch (this.joggSyncState.pageout(this.joggPage)) {
            case -1:
            case 0:
                this.endOfStream(true, true);
                break;
            case 1:
                this.joggStreamState.pagein(this.joggPage);
                if (this.joggPage.granulepos() == 0L) {
                    this.endOfStream(true, true);
                } else {
                    label45:
                    while (true) {
                        switch (this.joggStreamState.packetout(this.joggPacket)) {
                            case -1:
                            case 0:
                                if (this.joggPage.eos() != 0) {
                                    this.endOfStream(true, true);
                                }
                                break label45;
                            case 1:
                                abyte = appendByteArrays(abyte, this.decodeCurrentPacket());
                        }
                    }
                }
        }

        if (!this.endOfStream(false, false)) {
            this.index = this.joggSyncState.buffer(this.bufferSize);
            if (this.index == -1) {
                this.endOfStream(true, true);
            } else {
                this.buffer = this.joggSyncState.data;

                try {
                    this.count = this.inputStream.read(this.buffer, this.index, this.bufferSize);
                } catch (Exception exception) {
                    this.printStackTrace(exception);
                    return abyte;
                }

                this.joggSyncState.wrote(this.count);
                if (this.count == 0) {
                    this.endOfStream(true, true);
                }
            }
        }

        return abyte;
    }

    private byte[] decodeCurrentPacket() {
        byte[] abyte = new byte[this.convertedBufferSize];
        if (this.jorbisBlock.synthesis(this.joggPacket) == 0) {
            this.jorbisDspState.synthesis_blockin(this.jorbisBlock);
        }

        int k = this.convertedBufferSize / (this.jorbisInfo.channels * 2);
        int l = 0;

        int i;
        while (l < this.convertedBufferSize && (i = this.jorbisDspState.synthesis_pcmout(this.pcmInfo, this.pcmIndex)) > 0) {
            int j;
            if (i < k) {
                j = i;
            } else {
                j = k;
            }

            for (int i1 = 0; i1 < this.jorbisInfo.channels; i1++) {
                int j1 = i1 * 2;

                for (int k1 = 0; k1 < j; k1++) {
                    int l1 = (int)(this.pcmInfo[0][i1][this.pcmIndex[i1] + k1] * 32767.0F);
                    if (l1 > 32767) {
                        l1 = 32767;
                    }

                    if (l1 < -32768) {
                        l1 = -32768;
                    }

                    if (l1 < 0) {
                        l1 |= 32768;
                    }

                    if (LITTLE_ENDIAN) {
                        abyte[l + j1] = (byte)l1;
                        abyte[l + j1 + 1] = (byte)(l1 >>> 8);
                    } else {
                        abyte[l + j1 + 1] = (byte)l1;
                        abyte[l + j1] = (byte)(l1 >>> 8);
                    }

                    j1 += 2 * this.jorbisInfo.channels;
                }
            }

            l += j * this.jorbisInfo.channels * 2;
            this.jorbisDspState.synthesis_read(j);
        }

        return trimArray(abyte, l);
    }

    private synchronized boolean initialized(boolean var1, boolean var2) {
        if (var1 == true) {
            this.initialized = var2;
        }

        return this.initialized;
    }

    private synchronized boolean endOfStream(boolean var1, boolean var2) {
        if (var1 == true) {
            this.endOfStream = var2;
        }

        return this.endOfStream;
    }

    private static byte[] trimArray(byte[] var0, int var1) {
        byte[] abyte = null;
        if (var0 != null && var0.length > var1) {
            abyte = new byte[var1];
            System.arraycopy(var0, 0, abyte, 0, var1);
        }

        return abyte;
    }

    private static byte[] appendByteArrays(byte[] var0, byte[] var1) {
        if (var0 == null && var1 == null) {
            return null;
        }

        byte[] abyte;
        if (var0 == null) {
            abyte = new byte[var1.length];
            System.arraycopy(var1, 0, abyte, 0, var1.length);
            Object object2 = null;
        } else if (var1 == null) {
            abyte = new byte[var0.length];
            System.arraycopy(var0, 0, abyte, 0, var0.length);
            Object object = null;
        } else {
            abyte = new byte[var0.length + var1.length];
            System.arraycopy(var0, 0, abyte, 0, var0.length);
            System.arraycopy(var1, 0, abyte, var0.length, var1.length);
            Object object1 = null;
            Object object3 = null;
        }

        return abyte;
    }

    private void errorMessage(String var1) {
        this.logger.errorMessage("CodecJOrbis", var1, 0);
    }

    private void printStackTrace(Exception var1) {
        this.logger.printStackTrace(var1, 1);
    }
}
