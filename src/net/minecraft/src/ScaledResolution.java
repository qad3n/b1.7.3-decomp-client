package net.minecraft.src;


public class ScaledResolution {
    private int scaledWidth;
    private int scaledHeight;
    public double scaledWidthD;
    public double scaledHeightD;
    public int scaleFactor;

    public ScaledResolution(GameSettings options, int width, int height) {
        this.scaledWidth = width;
        this.scaledHeight = height;
        this.scaleFactor = 1;
        int n = options.guiScale;
        if (n == 0) {
            n = 1000;
        }
        while (this.scaleFactor < n && this.scaledWidth / (this.scaleFactor + 1) >= 320 && this.scaledHeight / (this.scaleFactor + 1) >= 240) {
            ++this.scaleFactor;
        }
        this.scaledWidthD = (double)this.scaledWidth / (double)this.scaleFactor;
        this.scaledHeightD = (double)this.scaledHeight / (double)this.scaleFactor;
        this.scaledWidth = (int)Math.ceil(this.scaledWidthD);
        this.scaledHeight = (int)Math.ceil(this.scaledHeightD);
    }

    public int getScaledWidth() {
        return this.scaledWidth;
    }

    public int getScaledHeight() {
        return this.scaledHeight;
    }
}

