package paulscode.sound;


public class CommandThread
extends SimpleThread {
    private SoundSystem soundSystem;
    protected String className = "CommandThread";
    protected SoundSystemLogger logger = SoundSystemConfig.getLogger();

    public CommandThread(SoundSystem soundSystem) {
        this.soundSystem = soundSystem;
    }

    protected void cleanup() {
        this.kill();
        this.logger = null;
        this.soundSystem = null;
        super.cleanup();
    }

    public void run() {
        long l = System.currentTimeMillis();
        long l2 = l;
        if (this.soundSystem == null) {
            this.errorMessage("SoundSystem was null in method run().", 0);
            this.cleanup();
            return;
        }
        this.snooze(3600000L);
        while (!this.dying()) {
            this.soundSystem.ManageSources();
            this.soundSystem.CommandQueue(null);
            l2 = System.currentTimeMillis();
            if (!this.dying() && l2 - l > 10000L) {
                l = l2;
                this.soundSystem.removeTemporarySources();
            }
            if (!this.dying()) {
                this.snooze(3600000L);
            }
        }
        this.cleanup();
    }

    protected void message(String string, int n) {
        this.logger.message(string, n);
    }

    protected void importantMessage(String string, int n) {
        this.logger.importantMessage(string, n);
    }

    protected boolean errorCheck(boolean bl, String string) {
        return this.logger.errorCheck(bl, this.className, string, 0);
    }

    protected void errorMessage(String string, int n) {
        this.logger.errorMessage(this.className, string, n);
    }
}

