package paulscode.sound;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Locale;

public class SoundSystemConfig {
    public static final Object THREAD_SYNC = new Object();
    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_STREAMING = 1;
    public static final int ATTENUATION_NONE = 0;
    public static final int ATTENUATION_ROLLOFF = 1;
    public static final int ATTENUATION_LINEAR = 2;
    public static final String EXTENSION_MIDI = ".*[mM][iI][dD][iI]?$";
    public static final String PREFIX_URL = "^[hH][tT][tT][pP]://.*";
    private static SoundSystemLogger logger = null;
    private static LinkedList<Class<?>> libraries;
    private static LinkedList<SoundSystemConfig.Codec> codecs = null;
    private static int numberNormalChannels = 28;
    private static int numberStreamingChannels = 4;
    private static float masterGain = 1.0F;
    private static int defaultAttenuationModel = 1;
    private static float defaultRolloffFactor = 0.03F;
    private static float defaultFadeDistance = 1000.0F;
    private static String soundFilesPackage = "Sounds/";
    private static int streamingBufferSize = 131072;
    private static int numberStreamingBuffers = 3;
    private static int maxFileSize = 268435456;
    private static int fileChunkSize = 1048576;
    private static boolean midiCodec = false;

    public static void addLibrary(Class<?> var0) throws SoundSystemException {
        if (var0 == null) {
            throw new SoundSystemException("Parameter null in method 'addLibrary'", 2);
        }

        if (!Library.class.isAssignableFrom(var0)) {
            throw new SoundSystemException("The specified class does not extend class 'Library' in method 'addLibrary'");
        }

        if (libraries == null) {
            libraries = new LinkedList<>();
        }

        if (!libraries.contains(var0)) {
            libraries.add(var0);
        }
    }

    public static void removeLibrary(Class<?> var0) {
        if (libraries != null && var0 != null) {
            libraries.remove(var0);
        }
    }

    public static LinkedList<Class<?>> getLibraries() {
        return libraries;
    }

    public static boolean libraryCompatible(Class<?> var0) {
        if (var0 == null) {
            errorMessage("Parameter 'libraryClass' null in method'librayCompatible'");
            return false;
        } else if (!Library.class.isAssignableFrom(var0)) {
            errorMessage("The specified class does not extend class 'Library' in method 'libraryCompatible'");
            return false;
        } else {
            Object object = runMethod(var0, "libraryCompatible", new Class[0], new Object[0]);
            if (object == null) {
                errorMessage("Method 'Library.libraryCompatible' returned 'null' in method 'libraryCompatible'");
                return false;
            } else {
                return (Boolean)object;
            }
        }
    }

    public static String getLibraryTitle(Class<?> var0) {
        if (var0 == null) {
            errorMessage("Parameter 'libraryClass' null in method'getLibrayTitle'");
            return null;
        } else if (!Library.class.isAssignableFrom(var0)) {
            errorMessage("The specified class does not extend class 'Library' in method 'getLibraryTitle'");
            return null;
        } else {
            Object object = runMethod(var0, "getTitle", new Class[0], new Object[0]);
            if (object == null) {
                errorMessage("Method 'Library.getTitle' returned 'null' in method 'getLibraryTitle'");
                return null;
            } else {
                return (String)object;
            }
        }
    }

    public static String getLibraryDescription(Class<?> var0) {
        if (var0 == null) {
            errorMessage("Parameter 'libraryClass' null in method'getLibrayDescription'");
            return null;
        } else if (!Library.class.isAssignableFrom(var0)) {
            errorMessage("The specified class does not extend class 'Library' in method 'getLibraryDescription'");
            return null;
        } else {
            Object object = runMethod(var0, "getDescription", new Class[0], new Object[0]);
            if (object == null) {
                errorMessage("Method 'Library.getDescription' returned 'null' in method 'getLibraryDescription'");
                return null;
            } else {
                return (String)object;
            }
        }
    }

    public static void setLogger(SoundSystemLogger var0) {
        logger = var0;
    }

    public static SoundSystemLogger getLogger() {
        return logger;
    }

    public static synchronized void setNumberNormalChannels(int var0) {
        numberNormalChannels = var0;
    }

    public static synchronized int getNumberNormalChannels() {
        return numberNormalChannels;
    }

    public static synchronized void setNumberStreamingChannels(int var0) {
        numberStreamingChannels = var0;
    }

    public static synchronized int getNumberStreamingChannels() {
        return numberStreamingChannels;
    }

    public static synchronized void setMasterGain(float var0) {
        masterGain = var0;
    }

    public static synchronized float getMasterGain() {
        return masterGain;
    }

    public static synchronized void setDefaultAttenuation(int var0) {
        defaultAttenuationModel = var0;
    }

    public static synchronized int getDefaultAttenuation() {
        return defaultAttenuationModel;
    }

    public static synchronized void setDefaultRolloff(float var0) {
        defaultRolloffFactor = var0;
    }

    public static synchronized float getDefaultRolloff() {
        return defaultRolloffFactor;
    }

    public static synchronized void setDefaultFadeDistance(float var0) {
        defaultFadeDistance = var0;
    }

    public static synchronized float getDefaultFadeDistance() {
        return defaultFadeDistance;
    }

    public static synchronized void setSoundFilesPackage(String var0) {
        soundFilesPackage = var0;
    }

    public static synchronized String getSoundFilesPackage() {
        return soundFilesPackage;
    }

    public static synchronized void setStreamingBufferSize(int var0) {
        streamingBufferSize = var0;
    }

    public static synchronized int getStreamingBufferSize() {
        return streamingBufferSize;
    }

    public static synchronized void setNumberStreamingBuffers(int var0) {
        numberStreamingBuffers = var0;
    }

    public static synchronized int getNumberStreamingBuffers() {
        return numberStreamingBuffers;
    }

    public static synchronized void setMaxFileSize(int var0) {
        maxFileSize = var0;
    }

    public static synchronized int getMaxFileSize() {
        return maxFileSize;
    }

    public static synchronized void setFileChunkSize(int var0) {
        fileChunkSize = var0;
    }

    public static synchronized int getFileChunkSize() {
        return fileChunkSize;
    }

    public static synchronized void setCodec(String var0, Class<?> var1) throws SoundSystemException {
        if (var0 == null) {
            throw new SoundSystemException("Parameter 'extension' null in method 'setCodec'.", 2);
        }

        if (var1 == null) {
            throw new SoundSystemException("Parameter 'iCodecClass' null in method 'setCodec'.", 2);
        }

        if (!ICodec.class.isAssignableFrom(var1)) {
            throw new SoundSystemException("The specified class does not implement interface 'ICodec' in method 'setCodec'", 3);
        }

        if (codecs == null) {
            codecs = new LinkedList<>();
        }

        ListIterator listiterator = codecs.listIterator();

        while (listiterator.hasNext()) {
            SoundSystemConfig.Codec soundsystemconfig$codec = (SoundSystemConfig.Codec)listiterator.next();
            if (var0.matches(soundsystemconfig$codec.extensionRegX)) {
                listiterator.remove();
            }
        }

        codecs.add(new SoundSystemConfig.Codec(var0, var1));
        if (var0.matches(".*[mM][iI][dD][iI]?$")) {
            midiCodec = true;
        }
    }

    public static synchronized ICodec getCodec(String var0) {
        if (codecs == null) {
            return null;
        }

        ListIterator<SoundSystemConfig.Codec> iterator = codecs.listIterator();
        while (iterator.hasNext()) {
            SoundSystemConfig.Codec soundsystemconfig$codec = iterator.next();
            if (var0.matches(soundsystemconfig$codec.extensionRegX)) {
                return soundsystemconfig$codec.getInstance();
            }
        }

        return null;
    }

    public static boolean midiCodec() {
        return midiCodec;
    }

    private static void errorMessage(String var0) {
        if (logger != null) {
            logger.errorMessage("SoundSystemConfig", var0, 0);
        }
    }

    private static Object runMethod(Class<?> var0, String var1, Class<?>[] var2, Object[] var3) {
        Method method = null;

        try {
            method = var0.getMethod(var1, var2);
        } catch (NoSuchMethodException nosuchmethodexception) {
            errorMessage("NoSuchMethodException thrown when attempting to call method '" + var1 + "' in " + "method 'runMethod'");
            return null;
        } catch (SecurityException securityexception) {
            errorMessage("Access denied when attempting to call method '" + var1 + "' in method 'runMethod'");
            return null;
        } catch (NullPointerException nullpointerexception1) {
            errorMessage("NullPointerException thrown when attempting to call method '" + var1 + "' in " + "method 'runMethod'");
            return null;
        }

        if (method == null) {
            errorMessage("Method '" + var1 + "' not found for the class " + "specified in method 'runMethod'");
            return null;
        }

        Object object = null;

        try {
            return method.invoke(null, var3);
        } catch (IllegalAccessException illegalaccessexception) {
            errorMessage("IllegalAccessException thrown when attempting to invoke method '" + var1 + "' in " + "method 'runMethod'");
            return null;
        } catch (IllegalArgumentException illegalargumentexception) {
            errorMessage("IllegalArgumentException thrown when attempting to invoke method '" + var1 + "' in " + "method 'runMethod'");
            return null;
        } catch (InvocationTargetException invocationtargetexception) {
            errorMessage("InvocationTargetException thrown while attempting to invoke method 'Library.getTitle' in method 'getLibraryTitle'");
            return null;
        } catch (NullPointerException nullpointerexception) {
            errorMessage("NullPointerException thrown when attempting to invoke method '" + var1 + "' in " + "method 'runMethod'");
            return null;
        } catch (ExceptionInInitializerError exceptionininitializererror) {
            errorMessage("ExceptionInInitializerError thrown when attempting to invoke method '" + var1 + "' in " + "method 'runMethod'");
            return null;
        }
    }

    static class Codec {
        public String extensionRegX = "";
        public Class<?> iCodecClass;

        public Codec(String var1, Class<?> var2) {
            if (var1 != null && var1.length() > 0) {
                this.extensionRegX = ".*";

                for (int i = 0; i < var1.length(); i++) {
                    String s = var1.substring(i, i + 1);
                    this.extensionRegX = this.extensionRegX + "[" + s.toLowerCase(Locale.ENGLISH) + s.toUpperCase(Locale.ENGLISH) + "]";
                }

                this.extensionRegX = this.extensionRegX + "$";
            }

            this.iCodecClass = var2;
        }

        public ICodec getInstance() {
            if (this.iCodecClass == null) {
                return null;
            }

            Object object = null;

            try {
                object = this.iCodecClass.newInstance();
            } catch (InstantiationException instantiationexception) {
                this.instantiationErrorMessage();
                return null;
            } catch (IllegalAccessException illegalaccessexception) {
                this.instantiationErrorMessage();
                return null;
            } catch (ExceptionInInitializerError exceptionininitializererror) {
                this.instantiationErrorMessage();
                return null;
            } catch (SecurityException securityexception) {
                this.instantiationErrorMessage();
                return null;
            }

            if (object == null) {
                this.instantiationErrorMessage();
                return null;
            } else {
                return (ICodec)object;
            }
        }

        private void instantiationErrorMessage() {
            SoundSystemConfig.errorMessage(
                "Unrecognized ICodec implementation in method 'getInstance'.  Ensure that the implementing class has one public, parameterless constructor."
            );
        }
    }
}
