package net.minecraft.src;

import java.io.File;
import java.util.Random;
import paulscode.sound.SoundSystem;
import paulscode.sound.SoundSystemConfig;
import paulscode.sound.codecs.CodecJOrbis;
import paulscode.sound.codecs.CodecWav;
import paulscode.sound.libraries.LibraryLWJGLOpenAL;

public class SoundManager {
    private static SoundSystem sndSystem;
    private SoundPool soundPoolSounds = new SoundPool();
    private SoundPool soundPoolStreaming = new SoundPool();
    private SoundPool soundPoolMusic = new SoundPool();
    private int nextSoundEventIndex = 0;
    private GameSettings options;
    private static boolean loaded;
    private Random rand = new Random();
    private int ticksBeforeMusic = this.rand.nextInt(12000);

    public void loadSoundSettings(GameSettings options) {
        this.soundPoolStreaming.trimTrailingDigits = false;
        this.options = options;
        if (!(loaded || options != null && options.soundVolume == 0.0f && options.musicVolume == 0.0f)) {
            this.tryToSetLibraryAndCodecs();
        }
    }

    private void tryToSetLibraryAndCodecs() {
        try {
            float f = this.options.soundVolume;
            float f2 = this.options.musicVolume;
            this.options.soundVolume = 0.0f;
            this.options.musicVolume = 0.0f;
            this.options.saveOptions();
            SoundSystemConfig.addLibrary(LibraryLWJGLOpenAL.class);
            SoundSystemConfig.setCodec("ogg", CodecJOrbis.class);
            SoundSystemConfig.setCodec("mus", CodecMus.class);
            SoundSystemConfig.setCodec("wav", CodecWav.class);
            sndSystem = new SoundSystem();
            this.options.soundVolume = f;
            this.options.musicVolume = f2;
            this.options.saveOptions();
        }
        catch (Throwable throwable) {
            throwable.printStackTrace();
            System.err.println("error linking with the LibraryJavaSound plug-in");
        }
        loaded = true;
    }

    public void onSoundOptionsChanged() {
        if (!(loaded || this.options.soundVolume == 0.0f && this.options.musicVolume == 0.0f)) {
            this.tryToSetLibraryAndCodecs();
        }
        if (loaded) {
            if (this.options.musicVolume == 0.0f) {
                sndSystem.stop("BgMusic");
            } else {
                sndSystem.setVolume("BgMusic", this.options.musicVolume);
            }
        }
    }

    public void closeMinecraft() {
        if (loaded) {
            sndSystem.cleanup();
        }
    }

    public void addSound(String path, File dir) {
        this.soundPoolSounds.addSound(path, dir);
    }

    public void addStreaming(String path, File fil) {
        this.soundPoolStreaming.addSound(path, fil);
    }

    public void addMusic(String path, File file) {
        this.soundPoolMusic.addSound(path, file);
    }

    public void playRandomMusicIfReady() {
        if (!loaded || this.options.musicVolume == 0.0f) {
            return;
        }
        if (!sndSystem.playing("BgMusic") && !sndSystem.playing("streaming")) {
            if (this.ticksBeforeMusic > 0) {
                --this.ticksBeforeMusic;
                return;
            }
            SoundPoolEntry soundPoolEntry = this.soundPoolMusic.getRandomSound();
            if (soundPoolEntry != null) {
                this.ticksBeforeMusic = this.rand.nextInt(12000) + 12000;
                sndSystem.backgroundMusic("BgMusic", soundPoolEntry.soundUrl, soundPoolEntry.soundName, false);
                sndSystem.setVolume("BgMusic", this.options.musicVolume);
                sndSystem.play("BgMusic");
            }
        }
    }

    public void update(EntityLiving listener, float tickDelta) {
        if (!loaded || this.options.soundVolume == 0.0f) {
            return;
        }
        if (listener == null) {
            return;
        }
        float f = listener.prevRotationYaw + (listener.rotationYaw - listener.prevRotationYaw) * tickDelta;
        double d = listener.prevPosX + (listener.posX - listener.prevPosX) * (double)tickDelta;
        double d2 = listener.prevPosY + (listener.posY - listener.prevPosY) * (double)tickDelta;
        double d3 = listener.prevPosZ + (listener.posZ - listener.prevPosZ) * (double)tickDelta;
        float f2 = MathHelper.cos(-f * ((float)Math.PI / 180) - (float)Math.PI);
        float f3 = MathHelper.sin(-f * ((float)Math.PI / 180) - (float)Math.PI);
        float f4 = -f3;
        float f5 = 0.0f;
        float f6 = -f2;
        float f7 = 0.0f;
        float f8 = 1.0f;
        float f9 = 0.0f;
        sndSystem.setListenerPosition((float)d, (float)d2, (float)d3);
        sndSystem.setListenerOrientation(f4, f5, f6, f7, f8, f9);
    }

    public void playStreaming(String name, float x, float y, float z, float volume, float pitch) {
        if (!loaded || this.options.soundVolume == 0.0f) {
            return;
        }
        String string = "streaming";
        if (sndSystem.playing("streaming")) {
            sndSystem.stop("streaming");
        }
        if (name == null) {
            return;
        }
        SoundPoolEntry soundPoolEntry = this.soundPoolStreaming.getRandomSoundFromSoundPool(name);
        if (soundPoolEntry != null && volume > 0.0f) {
            if (sndSystem.playing("BgMusic")) {
                sndSystem.stop("BgMusic");
            }
            float f = 16.0f;
            sndSystem.newStreamingSource(true, string, soundPoolEntry.soundUrl, soundPoolEntry.soundName, false, x, y, z, 2, f * 4.0f);
            sndSystem.setVolume(string, 0.5f * this.options.soundVolume);
            sndSystem.play(string);
        }
    }

    public void playSound(String id, float x, float y, float z, float volume, float pitch) {
        if (!loaded || this.options.soundVolume == 0.0f) {
            return;
        }
        SoundPoolEntry soundPoolEntry = this.soundPoolSounds.getRandomSoundFromSoundPool(id);
        if (soundPoolEntry != null && volume > 0.0f) {
            this.nextSoundEventIndex = (this.nextSoundEventIndex + 1) % 256;
            String string = "sound_" + this.nextSoundEventIndex;
            float f = 16.0f;
            if (volume > 1.0f) {
                f *= volume;
            }
            sndSystem.newSource(volume > 1.0f, string, soundPoolEntry.soundUrl, soundPoolEntry.soundName, false, x, y, z, 2, f);
            sndSystem.setPitch(string, pitch);
            if (volume > 1.0f) {
                volume = 1.0f;
            }
            sndSystem.setVolume(string, volume * this.options.soundVolume);
            sndSystem.play(string);
        }
    }

    public void playSoundFX(final String id, float volume, final float pitch) {
        if (!SoundManager.loaded || this.options.soundVolume == 0.0f) {
            return;
        }
        final SoundPoolEntry randomSoundFromSoundPool = this.soundPoolSounds.getRandomSoundFromSoundPool(id);
        if (randomSoundFromSoundPool != null) {
            this.nextSoundEventIndex = (this.nextSoundEventIndex + 1) % 256;
            final String string = "sound_" + this.nextSoundEventIndex;
            SoundManager.sndSystem.newSource(false, string, randomSoundFromSoundPool.soundUrl, randomSoundFromSoundPool.soundName, false, 0.0f, 0.0f, 0.0f, 0, 0.0f);
            if (volume > 1.0f) {
                volume = 1.0f;
            }
            volume *= 0.25f;
            SoundManager.sndSystem.setPitch(string, pitch);
            SoundManager.sndSystem.setVolume(string, volume * this.options.soundVolume);
            SoundManager.sndSystem.play(string);
        }
    }

    static {
        loaded = false;
    }
}

