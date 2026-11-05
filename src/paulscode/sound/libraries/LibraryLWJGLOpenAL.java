package paulscode.sound.libraries;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import javax.sound.sampled.AudioFormat;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import paulscode.sound.Channel;
import paulscode.sound.FilenameURL;
import paulscode.sound.ICodec;
import paulscode.sound.Library;
import paulscode.sound.ListenerData;
import paulscode.sound.SoundBuffer;
import paulscode.sound.SoundSystemConfig;
import paulscode.sound.SoundSystemException;
import paulscode.sound.Source;

public class LibraryLWJGLOpenAL extends Library {
    private static final boolean GET = false;
    private static final boolean SET = true;
    private static final boolean XXX = false;
    private FloatBuffer listenerPositionAL = null;
    private FloatBuffer listenerOrientation = null;
    private FloatBuffer listenerVelocity = null;
    private HashMap<String, IntBuffer> ALBufferMap = null;
    private static boolean alPitchSupported = true;

    public LibraryLWJGLOpenAL() throws SoundSystemException {
        this.ALBufferMap = new HashMap<>();
    }

    @Override
    public void init() throws SoundSystemException {
        boolean flag = false;

        try {
            AL.create();
            flag = this.checkALError();
        } catch (LWJGLException lwjglexception) {
            this.errorMessage("Unable to initialize OpenAL.  Probable cause: OpenAL not supported.");
            this.printStackTrace(lwjglexception);
            throw new SoundSystemException(lwjglexception.getMessage(), 6);
        }

        if (flag) {
            this.importantMessage("OpenAL did not initialize properly!");
        } else {
            this.message("OpenAL initialized.");
        }

        this.listenerPositionAL = BufferUtils.createFloatBuffer(3)
            .put(new float[]{this.listener.position.x, this.listener.position.y, this.listener.position.z});
        this.listenerOrientation = BufferUtils.createFloatBuffer(6)
            .put(
                new float[]{this.listener.lookAt.x, this.listener.lookAt.y, this.listener.lookAt.z, this.listener.up.x, this.listener.up.y, this.listener.up.z}
            );
        this.listenerVelocity = BufferUtils.createFloatBuffer(3).put(new float[]{0.0F, 0.0F, 0.0F});
        ((Buffer)this.listenerPositionAL).flip();
        ((Buffer)this.listenerOrientation).flip();
        ((Buffer)this.listenerVelocity).flip();
        AL10.alListener(4100, this.listenerPositionAL);
        flag = this.checkALError() || flag;
        AL10.alListener(4111, this.listenerOrientation);
        flag = this.checkALError() || flag;
        AL10.alListener(4102, this.listenerVelocity);
        flag = this.checkALError() || flag;
        if (flag) {
            this.importantMessage("OpenAL did not initialize properly!");
            throw new SoundSystemException("Problem encountered while loading OpenAL or creating the listener.  Probably cause:  OpenAL not supported", 6);
        }

        super.init();
        ChannelLWJGLOpenAL channellwjglopenal = (ChannelLWJGLOpenAL)this.normalChannels.get(1);

        try {
            AL10.alSourcef(channellwjglopenal.ALSource.get(0), 4099, 1.0F);
            if (this.checkALError()) {
                alPitchSupported(true, false);
                throw new SoundSystemException("OpenAL: AL_PITCH not supported.", 13);
            }

            alPitchSupported(true, true);
        } catch (Exception exception) {
            alPitchSupported(true, false);
            throw new SoundSystemException("OpenAL: AL_PITCH not supported.", 13);
        }
    }

    public static boolean libraryCompatible() {
        if (AL.isCreated()) {
            return true;
        }

        try {
            AL.create();
        } catch (Exception exception1) {
            return false;
        }

        try {
            AL.destroy();
        } catch (Exception exception) {
        }

        return true;
    }

    @Override
    protected Channel createChannel(int var1) {
        IntBuffer intbuffer = BufferUtils.createIntBuffer(1);

        try {
            AL10.alGenSources(intbuffer);
        } catch (Exception exception) {
            AL10.alGetError();
            return null;
        }

        return AL10.alGetError() != 0 ? null : new ChannelLWJGLOpenAL(var1, intbuffer);
    }

    @Override
    public void cleanup() {
        super.cleanup();

        Set<String> set = this.bufferMap.keySet();
        Iterator<String> iterator = set.iterator();
        while (iterator.hasNext()) {
            String s = iterator.next();
            IntBuffer intbuffer = this.ALBufferMap.get(s);
            if (intbuffer != null) {
                AL10.alDeleteBuffers(intbuffer);
                this.checkALError();
                intbuffer.clear();
            }
        }

        this.bufferMap.clear();
        AL.destroy();
        this.bufferMap = null;
        this.listenerPositionAL = null;
        this.listenerOrientation = null;
        this.listenerVelocity = null;
    }

    @Override
    public boolean loadSound(FilenameURL var1) {
        if (this.bufferMap == null) {
            this.bufferMap = new HashMap<>();
            this.importantMessage("Buffer Map was null in method 'loadSound'");
        }

        if (this.ALBufferMap == null) {
            this.ALBufferMap = new HashMap<>();
            this.importantMessage("Open AL Buffer Map was null in method'loadSound'");
        }

        if (this.errorCheck(var1 == null, "Filename/URL not specified in method 'loadSound'")) {
            return false;
        }

        if (this.bufferMap.get(var1.getFilename()) != null) {
            return true;
        }

        ICodec icodec = SoundSystemConfig.getCodec(var1.getFilename());
        if (this.errorCheck(icodec == null, "No codec found for file '" + var1.getFilename() + "' in method 'loadSound'")) {
            return false;
        }

        icodec.initialize(var1.getURL());
        SoundBuffer soundbuffer = icodec.readAll();
        icodec.cleanup();
        Object object = null;
        if (this.errorCheck(soundbuffer == null, "Sound buffer null in method 'loadSound'")) {
            return false;
        }

        this.bufferMap.put(var1.getFilename(), soundbuffer);
        AudioFormat audioformat = soundbuffer.audioFormat;
        short short1 = 0;
        if (audioformat.getChannels() == 1) {
            if (audioformat.getSampleSizeInBits() == 8) {
                short1 = 4352;
            } else {
                if (audioformat.getSampleSizeInBits() != 16) {
                    this.errorMessage("Illegal sample size in method 'loadSound'");
                    return false;
                }

                short1 = 4353;
            }
        } else {
            if (audioformat.getChannels() != 2) {
                this.errorMessage("File neither mono nor stereo in method 'loadSound'");
                return false;
            }

            if (audioformat.getSampleSizeInBits() == 8) {
                short1 = 4354;
            } else {
                if (audioformat.getSampleSizeInBits() != 16) {
                    this.errorMessage("Illegal sample size in method 'loadSound'");
                    return false;
                }

                short1 = 4355;
            }
        }

        IntBuffer intbuffer = BufferUtils.createIntBuffer(1);
        AL10.alGenBuffers(intbuffer);
        if (this.errorCheck(AL10.alGetError() != 0, "alGenBuffers error when loading " + var1.getFilename())) {
            return false;
        }

        ByteBuffer bytebuffer = BufferUtils.createByteBuffer(soundbuffer.audioData.length);
        ((Buffer)bytebuffer).clear();
        bytebuffer.put(soundbuffer.audioData);
        ((Buffer)bytebuffer).flip();
        AL10.alBufferData(intbuffer.get(0), short1, bytebuffer, (int)audioformat.getSampleRate());
        if (this.errorCheck(AL10.alGetError() != 0, "alBufferData error when loading " + var1.getFilename())
            && this.errorCheck(intbuffer == null, "Sound buffer was not created for " + var1.getFilename())) {
            return false;
        }

        this.ALBufferMap.put(var1.getFilename(), intbuffer);
        return true;
    }

    @Override
    public void unloadSound(String var1) {
        this.ALBufferMap.remove(var1);
        super.unloadSound(var1);
    }

    @Override
    public void setMasterVolume(float var1) {
        super.setMasterVolume(var1);
        AL10.alListenerf(4106, var1);
        this.checkALError();
    }

    @Override
    public void newSource(boolean var1, boolean var2, boolean var3, String var4, FilenameURL var5, float var6, float var7, float var8, int var9, float var10) {
        IntBuffer intbuffer = null;
        if (!var2) {
            intbuffer = this.ALBufferMap.get(var5.getFilename());
            if (intbuffer == null && !this.loadSound(var5)) {
                this.errorMessage("Source '" + var4 + "' was not created " + "because an error occurred while loading " + var5.getFilename());
                return;
            }

            intbuffer = this.ALBufferMap.get(var5.getFilename());
            if (intbuffer == null) {
                this.errorMessage("Source '" + var4 + "' was not created " + "because a sound buffer was not found for " + var5.getFilename());
                return;
            }
        }

        SoundBuffer soundbuffer = null;
        if (!var2) {
            soundbuffer = this.bufferMap.get(var5.getFilename());
            if (soundbuffer == null && !this.loadSound(var5)) {
                this.errorMessage("Source '" + var4 + "' was not created " + "because an error occurred while loading " + var5.getFilename());
                return;
            }

            soundbuffer = this.bufferMap.get(var5.getFilename());
            if (soundbuffer == null) {
                this.errorMessage("Source '" + var4 + "' was not created " + "because audio data was not found for " + var5.getFilename());
                return;
            }
        }

        this.sourceMap
            .put(
                var4,
                new SourceLWJGLOpenAL(this.listenerPositionAL, intbuffer, var1, var2, var3, var4, var5, soundbuffer, var6, var7, var8, var9, var10, false)
            );
    }

    @Override
    public void rawDataStream(AudioFormat var1, boolean var2, String var3, float var4, float var5, float var6, int var7, float var8) {
        this.sourceMap.put(var3, new SourceLWJGLOpenAL(this.listenerPositionAL, var1, var2, var3, var4, var5, var6, var7, var8));
    }

    @Override
    public void quickPlay(
        boolean var1, boolean var2, boolean var3, String var4, FilenameURL var5, float var6, float var7, float var8, int var9, float var10, boolean var11
    ) {
        IntBuffer intbuffer = null;
        if (!var2) {
            intbuffer = this.ALBufferMap.get(var5.getFilename());
            if (intbuffer == null) {
                this.loadSound(var5);
            }

            intbuffer = this.ALBufferMap.get(var5.getFilename());
            if (intbuffer == null) {
                this.errorMessage("Sound buffer was not created for " + var5.getFilename());
                return;
            }
        }

        SoundBuffer soundbuffer = null;
        if (!var2) {
            soundbuffer = this.bufferMap.get(var5.getFilename());
            if (soundbuffer == null && !this.loadSound(var5)) {
                this.errorMessage("Source '" + var4 + "' was not created " + "because an error occurred while loading " + var5.getFilename());
                return;
            }

            soundbuffer = this.bufferMap.get(var5.getFilename());
            if (soundbuffer == null) {
                this.errorMessage("Source '" + var4 + "' was not created " + "because audio data was not found for " + var5.getFilename());
                return;
            }
        }

        SourceLWJGLOpenAL sourcelwjglopenal = new SourceLWJGLOpenAL(
            this.listenerPositionAL, intbuffer, var1, var2, var3, var4, var5, soundbuffer, var6, var7, var8, var9, var10, false
        );
        this.sourceMap.put(var4, sourcelwjglopenal);
        this.play(sourcelwjglopenal);
        if (var11) {
            sourcelwjglopenal.setTemporary(true);
        }
    }

    @Override
    public void copySources(HashMap<String, Source> var1) {
        if (var1 != null) {
            Set set = var1.keySet();
            Iterator iterator = set.iterator();
            if (this.bufferMap == null) {
                this.bufferMap = new HashMap<>();
                this.importantMessage("Buffer Map was null in method 'copySources'");
            }

            if (this.ALBufferMap == null) {
                this.ALBufferMap = new HashMap<>();
                this.importantMessage("Open AL Buffer Map was null in method'copySources'");
            }

            this.sourceMap.clear();

            while (iterator.hasNext()) {
                String s = (String)iterator.next();
                Source source = (Source)var1.get(s);
                if (source != null) {
                    SoundBuffer soundbuffer = null;
                    if (!source.toStream) {
                        this.loadSound(source.filenameURL);
                        soundbuffer = this.bufferMap.get(source.filenameURL.getFilename());
                    }

                    if (source.toStream || soundbuffer != null) {
                        this.sourceMap
                            .put(s, new SourceLWJGLOpenAL(this.listenerPositionAL, this.ALBufferMap.get(source.filenameURL.getFilename()), source, soundbuffer));
                    }
                }
            }
        }
    }

    @Override
    public void setListenerPosition(float var1, float var2, float var3) {
        super.setListenerPosition(var1, var2, var3);
        this.listenerPositionAL.put(0, var1);
        this.listenerPositionAL.put(1, var2);
        this.listenerPositionAL.put(2, var3);
        AL10.alListener(4100, this.listenerPositionAL);
        this.checkALError();
    }

    @Override
    public void setListenerAngle(float var1) {
        super.setListenerAngle(var1);
        this.listenerOrientation.put(0, this.listener.lookAt.x);
        this.listenerOrientation.put(2, this.listener.lookAt.z);
        AL10.alListener(4111, this.listenerOrientation);
        this.checkALError();
    }

    @Override
    public void setListenerOrientation(float var1, float var2, float var3, float var4, float var5, float var6) {
        super.setListenerOrientation(var1, var2, var3, var4, var5, var6);
        this.listenerOrientation.put(0, var1);
        this.listenerOrientation.put(1, var2);
        this.listenerOrientation.put(2, var3);
        this.listenerOrientation.put(3, var4);
        this.listenerOrientation.put(4, var5);
        this.listenerOrientation.put(5, var6);
        AL10.alListener(4111, this.listenerOrientation);
        this.checkALError();
    }

    @Override
    public void setListenerData(ListenerData var1) {
        super.setListenerData(var1);
        this.listenerPositionAL.put(0, var1.position.x);
        this.listenerPositionAL.put(1, var1.position.y);
        this.listenerPositionAL.put(2, var1.position.z);
        AL10.alListener(4100, this.listenerPositionAL);
        this.listenerOrientation.put(0, var1.lookAt.x);
        this.listenerOrientation.put(1, var1.lookAt.y);
        this.listenerOrientation.put(2, var1.lookAt.z);
        this.listenerOrientation.put(3, var1.up.x);
        this.listenerOrientation.put(4, var1.up.y);
        this.listenerOrientation.put(5, var1.up.z);
        AL10.alListener(4111, this.listenerOrientation);
        this.checkALError();
    }

    private boolean checkALError() {
        switch (AL10.alGetError()) {
            case 0:
                return false;
            case 40961:
                this.errorMessage("Invalid name parameter.");
                return true;
            case 40962:
                this.errorMessage("Invalid parameter.");
                return true;
            case 40963:
                this.errorMessage("Invalid enumerated parameter value.");
                return true;
            case 40964:
                this.errorMessage("Illegal call.");
                return true;
            case 40965:
                this.errorMessage("Unable to allocate memory.");
                return true;
            default:
                this.errorMessage("An unrecognized error occurred.");
                return true;
        }
    }

    public static boolean alPitchSupported() {
        return alPitchSupported(false, false);
    }

    private static synchronized boolean alPitchSupported(boolean var0, boolean var1) {
        if (var0 == true) {
            alPitchSupported = var1;
        }

        return alPitchSupported;
    }

    public static String getTitle() {
        return "LWJGL OpenAL";
    }

    public static String getDescription() {
        return "The LWJGL binding of OpenAL.  For more information, see http://www.lwjgl.org";
    }

    @Override
    public String getClassName() {
        return "LibraryLWJGLOpenAL";
    }
}
