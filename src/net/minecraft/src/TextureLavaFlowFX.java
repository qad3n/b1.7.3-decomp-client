package net.minecraft.src;

public class TextureLavaFlowFX extends TextureFX {
    protected float[] current = new float[256];
    protected float[] next = new float[256];
    protected float[] heat = new float[256];
    protected float[] heatDelta = new float[256];
    int ticks = 0;

    public TextureLavaFlowFX() {
        super(Block.lavaMoving.blockIndexInTexture + 1);
        this.tileSize = 2;
    }

    @Override
    public void onTick() {
        this.ticks++;

        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                float f = 0.0F;
                int k = (int)(MathHelper.sin(j * (float) Math.PI * 2.0F / 16.0F) * 1.2F);
                int l = (int)(MathHelper.sin(i * (float) Math.PI * 2.0F / 16.0F) * 1.2F);

                for (int i1 = i - 1; i1 <= i + 1; i1++) {
                    for (int j1 = j - 1; j1 <= j + 1; j1++) {
                        int k1 = i1 + k & 15;
                        int l1 = j1 + l & 15;
                        f += this.current[k1 + l1 * 16];
                    }
                }

                this.next[i + j * 16] = f / 10.0F
                    + (
                            this.heat[(i + 0 & 15) + (j + 0 & 15) * 16]
                                + this.heat[(i + 1 & 15) + (j + 0 & 15) * 16]
                                + this.heat[(i + 1 & 15) + (j + 1 & 15) * 16]
                                + this.heat[(i + 0 & 15) + (j + 1 & 15) * 16]
                        )
                        / 4.0F
                        * 0.8F;
                this.heat[i + j * 16] = this.heat[i + j * 16] + this.heatDelta[i + j * 16] * 0.01F;
                if (this.heat[i + j * 16] < 0.0F) {
                    this.heat[i + j * 16] = 0.0F;
                }

                this.heatDelta[i + j * 16] = this.heatDelta[i + j * 16] - 0.06F;
                if (Math.random() < 0.005) {
                    this.heatDelta[i + j * 16] = 1.5F;
                }
            }
        }

        float[] afloat = this.next;
        this.next = this.current;
        this.current = afloat;

        for (int j2 = 0; j2 < 256; j2++) {
            float f1 = this.current[j2 - this.ticks / 3 * 16 & 0xFF] * 2.0F;
            if (f1 > 1.0F) {
                f1 = 1.0F;
            }

            if (f1 < 0.0F) {
                f1 = 0.0F;
            }

            float f2 = f1;
            int k2 = (int)(f2 * 100.0F + 155.0F);
            int l2 = (int)(f2 * f2 * 255.0F);
            int i3 = (int)(f2 * f2 * f2 * f2 * 128.0F);
            if (this.anaglyphEnabled) {
                int j3 = (k2 * 30 + l2 * 59 + i3 * 11) / 100;
                int k3 = (k2 * 30 + l2 * 70) / 100;
                int i2 = (k2 * 30 + i3 * 70) / 100;
                k2 = j3;
                l2 = k3;
                i3 = i2;
            }

            this.imageData[j2 * 4 + 0] = (byte)k2;
            this.imageData[j2 * 4 + 1] = (byte)l2;
            this.imageData[j2 * 4 + 2] = (byte)i3;
            this.imageData[j2 * 4 + 3] = -1;
        }
    }
}
