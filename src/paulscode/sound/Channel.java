package paulscode.sound;

import java.util.LinkedList;
import javax.sound.sampled.AudioFormat;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class Channel {
    protected Class<?> libraryType = Library.class;
    public int channelType;
    public Source attachedSource = null;
    private SoundSystemLogger logger = SoundSystemConfig.getLogger();

    public Channel(int n) {
        this.channelType = n;
    }

    public void cleanup() {
        this.logger = null;
    }

    public boolean preLoadBuffers(LinkedList<byte[]> linkedList) {
        return true;
    }

    public boolean queueBuffer(byte[] byArray) {
        return false;
    }

    public int feedRawAudioData(byte[] byArray) {
        return 1;
    }

    public int buffersProcessed() {
        return 0;
    }

    public boolean processBuffer() {
        return false;
    }

    public void setAudioFormat(AudioFormat audioFormat) {
    }

    public void flush() {
    }

    public void close() {
    }

    public void play() {
    }

    public void pause() {
    }

    public void stop() {
    }

    public void rewind() {
    }

    public boolean playing() {
        return false;
    }

    public String getClassName() {
        String string = SoundSystemConfig.getLibraryTitle(this.libraryType);
        if (string.equals("No Sound")) {
            return "Channel";
        }
        return "Channel" + string;
    }

    protected void message(String string) {
        this.logger.message(string, 0);
    }

    protected void importantMessage(String string) {
        this.logger.importantMessage(string, 0);
    }

    protected boolean errorCheck(boolean bl, String string) {
        return this.logger.errorCheck(bl, this.getClassName(), string, 0);
    }

    protected void errorMessage(String string) {
        this.logger.errorMessage(this.getClassName(), string, 0);
    }

    protected void printStackTrace(Exception exception) {
        this.logger.printStackTrace(exception, 1);
    }
}

