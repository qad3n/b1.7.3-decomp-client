package net.minecraft.src;

public class TextureWaterFX extends TextureFX {
    protected float[] current = new float[256];
    protected float[] next = new float[256];
    protected float[] heat = new float[256];
    protected float[] heatDelta = new float[256];
    private int tickCounter = 0;

    public TextureWaterFX() {
        super(Block.waterMoving.blockIndexInTexture);
    }

    @Override
    public void onTick() {
        this.tickCounter++;

        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                float f = 0.0F;

                for (int k = i - 1; k <= i + 1; k++) {
                    int l = k & 15;
                    int i1 = j & 15;
                    f += this.current[l + i1 * 16];
                }

                this.next[i + j * 16] = f / 3.3F + this.heat[i + j * 16] * 0.8F;
            }
        }

        for (int k2 = 0; k2 < 16; k2++) {
            for (int l2 = 0; l2 < 16; l2++) {
                this.heat[k2 + l2 * 16] = this.heat[k2 + l2 * 16] + this.heatDelta[k2 + l2 * 16] * 0.05F;
                if (this.heat[k2 + l2 * 16] < 0.0F) {
                    this.heat[k2 + l2 * 16] = 0.0F;
                }

                this.heatDelta[k2 + l2 * 16] = this.heatDelta[k2 + l2 * 16] - 0.1F;
                if (Math.random() < 0.05) {
                    this.heatDelta[k2 + l2 * 16] = 0.5F;
                }
            }
        }

        float[] afloat = this.next;
        this.next = this.current;
        this.current = afloat;

        for (int i3 = 0; i3 < 256; i3++) {
            float f1 = this.current[i3];
            if (f1 > 1.0F) {
                f1 = 1.0F;
            }

            if (f1 < 0.0F) {
                f1 = 0.0F;
            }

            float f2 = f1 * f1;
            int j3 = (int)(32.0F + f2 * 32.0F);
            int k3 = (int)(50.0F + f2 * 64.0F);
            int j1 = 255;
            int k1 = (int)(146.0F + f2 * 50.0F);
            if (this.anaglyphEnabled) {
                int l1 = (j3 * 30 + k3 * 59 + j1 * 11) / 100;
                int i2 = (j3 * 30 + k3 * 70) / 100;
                int j2 = (j3 * 30 + j1 * 70) / 100;
                j3 = l1;
                k3 = i2;
                j1 = j2;
            }

            this.imageData[i3 * 4 + 0] = (byte)j3;
            this.imageData[i3 * 4 + 1] = (byte)k3;
            this.imageData[i3 * 4 + 2] = (byte)j1;
            this.imageData[i3 * 4 + 3] = (byte)k1;
        }
    }
}
