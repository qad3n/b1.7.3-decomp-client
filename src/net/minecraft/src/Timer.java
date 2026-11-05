package net.minecraft.src;

public class Timer {
    public float ticksPerSecond;
    private double lastHRTime;
    public int elapsedTicks;
    public float renderPartialTicks;
    public float timerSpeed = 1.0f;
    public float elapsedPartialTicks = 0.0f;
    private long lastSyncSysClock;
    private long lastSyncHRClock;
    private long cumTickTime;
    private double timeSyncAdjustment = 1.0;

    public Timer(float tps) {
        this.ticksPerSecond = tps;
        this.lastSyncSysClock = System.currentTimeMillis();
        this.lastSyncHRClock = System.nanoTime() / 1000000L;
    }

    public void updateTimer() {
        long l = System.currentTimeMillis();
        long l2 = l - this.lastSyncSysClock;
        long l3 = System.nanoTime() / 1000000L;
        double d = (double)l3 / 1000.0;
        if (l2 > 1000L) {
            this.lastHRTime = d;
        } else if (l2 < 0L) {
            this.lastHRTime = d;
        } else {
            this.cumTickTime += l2;
            if (this.cumTickTime > 1000L) {
                long l4 = l3 - this.lastSyncHRClock;
                double d2 = (double)this.cumTickTime / (double)l4;
                this.timeSyncAdjustment += (d2 - this.timeSyncAdjustment) * (double)0.2f;
                this.lastSyncHRClock = l3;
                this.cumTickTime = 0L;
            }
            if (this.cumTickTime < 0L) {
                this.lastSyncHRClock = l3;
            }
        }
        this.lastSyncSysClock = l;
        double d3 = (d - this.lastHRTime) * this.timeSyncAdjustment;
        this.lastHRTime = d;
        if (d3 < 0.0) {
            d3 = 0.0;
        }
        if (d3 > 1.0) {
            d3 = 1.0;
        }
        this.elapsedPartialTicks = (float)((double)this.elapsedPartialTicks + d3 * (double)this.timerSpeed * (double)this.ticksPerSecond);
        this.elapsedTicks = (int)this.elapsedPartialTicks;
        this.elapsedPartialTicks -= (float)this.elapsedTicks;
        if (this.elapsedTicks > 10) {
            this.elapsedTicks = 10;
        }
        this.renderPartialTicks = this.elapsedPartialTicks;
    }
}

