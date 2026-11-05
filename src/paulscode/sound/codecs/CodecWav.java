package paulscode.sound.codecs;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import paulscode.sound.ICodec;
import paulscode.sound.SoundBuffer;
import paulscode.sound.SoundSystemConfig;
import paulscode.sound.SoundSystemLogger;

public class CodecWav implements ICodec {
    private static final boolean GET = false;
    private static final boolean SET = true;
    private static final boolean XXX = false;
    private boolean endOfStream = false;
    private boolean initialized = false;
    private AudioInputStream myAudioInputStream = null;
    private SoundSystemLogger logger = SoundSystemConfig.getLogger();

    @Override
    public void reverseByteOrder(boolean var1) {
    }

    @Override
    public boolean initialize(URL var1) {
        this.initialized(true, false);
        this.cleanup();
        if (var1 == null) {
            this.errorMessage("url null in method 'initialize'");
            this.cleanup();
            return false;
        }

        try {
            this.myAudioInputStream = AudioSystem.getAudioInputStream(new BufferedInputStream(var1.openStream()));
        } catch (UnsupportedAudioFileException unsupportedaudiofileexception) {
            this.errorMessage("Unsupported audio format in method 'initialize'");
            this.printStackTrace(unsupportedaudiofileexception);
            return false;
        } catch (IOException ioexception) {
            this.errorMessage("Error setting up audio input stream in method 'initialize'");
            this.printStackTrace(ioexception);
            return false;
        }

        this.endOfStream(true, false);
        this.initialized(true, true);
        return true;
    }

    @Override
    public boolean initialized() {
        return this.initialized(false, false);
    }

    @Override
    public SoundBuffer read() {
        if (this.myAudioInputStream == null) {
            return null;
        }

        AudioFormat audioformat = this.myAudioInputStream.getFormat();
        if (audioformat == null) {
            this.errorMessage("Audio Format null in method 'read'");
            return null;
        }

        int i = 0;
        int j = 0;
        byte[] abyte = new byte[SoundSystemConfig.getStreamingBufferSize()];

        try {
            while (!this.endOfStream(false, false) && i < abyte.length) {
                if ((j = this.myAudioInputStream.read(abyte, i, abyte.length - i)) <= 0) {
                    this.endOfStream(true, true);
                    break;
                }

                i += j;
            }
        } catch (IOException ioexception) {
            this.endOfStream(true, true);
            return null;
        }

        if (i <= 0) {
            return null;
        }

        if (i < abyte.length) {
            abyte = trimArray(abyte, i);
        }

        byte[] abyte1 = convertAudioBytes(abyte, audioformat.getSampleSizeInBits() == 16);
        return new SoundBuffer(abyte1, audioformat);
    }

    @Override
    public SoundBuffer readAll() {
        if (this.myAudioInputStream == null) {
            this.errorMessage("Audio input stream null in method 'readAll'");
            return null;
        }

        AudioFormat audioformat = this.myAudioInputStream.getFormat();
        if (audioformat == null) {
            this.errorMessage("Audio Format null in method 'readAll'");
            return null;
        }

        byte[] abyte = null;
        int i = audioformat.getChannels() * (int)this.myAudioInputStream.getFrameLength() * audioformat.getSampleSizeInBits() / 8;
        if (i > 0) {
            abyte = new byte[audioformat.getChannels() * (int)this.myAudioInputStream.getFrameLength() * audioformat.getSampleSizeInBits() / 8];
            int j = 0;
            int k = 0;

            try {
                while ((j = this.myAudioInputStream.read(abyte, k, abyte.length - k)) != -1 && k < abyte.length) {
                    k += j;
                }
            } catch (IOException ioexception2) {
                this.errorMessage("Exception thrown while reading from the AudioInputStream (location #1).");
                this.printStackTrace(ioexception2);
                return null;
            }
        } else {
            int i1 = 0;
            int j1 = 0;
            int l = 0;
            byte[] abyte1 = null;
            abyte1 = new byte[SoundSystemConfig.getFileChunkSize()];

            while (!this.endOfStream(false, false) && i1 < SoundSystemConfig.getMaxFileSize()) {
                j1 = 0;
                l = 0;

                try {
                    while (j1 < abyte1.length) {
                        if ((l = this.myAudioInputStream.read(abyte1, j1, abyte1.length - j1)) <= 0) {
                            this.endOfStream(true, true);
                            break;
                        }

                        j1 += l;
                    }
                } catch (IOException ioexception1) {
                    this.errorMessage("Exception thrown while reading from the AudioInputStream (location #2).");
                    this.printStackTrace(ioexception1);
                    return null;
                }

                i1 += j1;
                abyte = appendByteArrays(abyte, abyte1, j1);
            }
        }

        byte[] abyte2 = convertAudioBytes(abyte, audioformat.getSampleSizeInBits() == 16);
        SoundBuffer soundbuffer = new SoundBuffer(abyte2, audioformat);

        try {
            this.myAudioInputStream.close();
        } catch (IOException ioexception) {
        }

        return soundbuffer;
    }

    @Override
    public boolean endOfStream() {
        return this.endOfStream(false, false);
    }

    @Override
    public void cleanup() {
        if (this.myAudioInputStream != null) {
            try {
                this.myAudioInputStream.close();
            } catch (Exception exception) {
            }
        }

        this.myAudioInputStream = null;
    }

    @Override
    public AudioFormat getAudioFormat() {
        return this.myAudioInputStream == null ? null : this.myAudioInputStream.getFormat();
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

    private static byte[] convertAudioBytes(byte[] var0, boolean var1) {
        ByteBuffer bytebuffer = ByteBuffer.allocateDirect(var0.length);
        bytebuffer.order(ByteOrder.nativeOrder());
        ByteBuffer bytebuffer1 = ByteBuffer.wrap(var0);
        bytebuffer1.order(ByteOrder.LITTLE_ENDIAN);
        if (var1) {
            ShortBuffer shortbuffer = bytebuffer.asShortBuffer();
            ShortBuffer shortbuffer1 = bytebuffer1.asShortBuffer();

            while (shortbuffer1.hasRemaining()) {
                shortbuffer.put(shortbuffer1.get());
            }
        } else {
            while (bytebuffer1.hasRemaining()) {
                bytebuffer.put(bytebuffer1.get());
            }
        }

        ((Buffer)bytebuffer).rewind();
        if (!bytebuffer.hasArray()) {
            byte[] abyte = new byte[bytebuffer.capacity()];
            bytebuffer.get(abyte);
            ((Buffer)bytebuffer).clear();
            return abyte;
        } else {
            return bytebuffer.array();
        }
    }

    private static byte[] appendByteArrays(byte[] var0, byte[] var1, int var2) {
        if (var0 == null && var1 == null) {
            return null;
        }

        byte[] abyte;
        if (var0 == null) {
            abyte = new byte[var2];
            System.arraycopy(var1, 0, abyte, 0, var2);
            Object object2 = null;
        } else if (var1 == null) {
            abyte = new byte[var0.length];
            System.arraycopy(var0, 0, abyte, 0, var0.length);
            Object object = null;
        } else {
            abyte = new byte[var0.length + var2];
            System.arraycopy(var0, 0, abyte, 0, var0.length);
            System.arraycopy(var1, 0, abyte, var0.length, var2);
            Object object1 = null;
            Object object3 = null;
        }

        return abyte;
    }

    private void errorMessage(String var1) {
        this.logger.errorMessage("CodecWav", var1, 0);
    }

    private void printStackTrace(Exception var1) {
        this.logger.printStackTrace(var1, 1);
    }
}
