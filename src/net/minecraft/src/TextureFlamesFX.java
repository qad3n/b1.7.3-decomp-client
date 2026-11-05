package net.minecraft.src;

public class TextureFlamesFX extends TextureFX {
    protected float[] current = new float[320];
    protected float[] next = new float[320];

    public TextureFlamesFX(int var1) {
        super(Block.fire.blockIndexInTexture + var1 * 16);
    }

    @Override
    public void onTick() {
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 20; j++) {
                int k = 18;
                float f = this.current[i + (j + 1) % 20 * 16] * k;

                for (int l = i - 1; l <= i + 1; l++) {
                    for (int i1 = j; i1 <= j + 1; i1++) {
                        int j1 = l;
                        int k1 = i1;
                        if (j1 >= 0 && k1 >= 0 && j1 < 16 && k1 < 20) {
                            f += this.current[j1 + k1 * 16];
                        }

                        k++;
                    }
                }

                this.next[i + j * 16] = f / (k * 1.06F);
                if (j >= 19) {
                    this.next[i + j * 16] = (float)(Math.random() * Math.random() * Math.random() * 4.0 + Math.random() * 0.1F + 0.2F);
                }
            }
        }

        float[] afloat = this.next;
        this.next = this.current;
        this.current = afloat;

        for (int k2 = 0; k2 < 256; k2++) {
            float f1 = this.current[k2] * 1.8F;
            if (f1 > 1.0F) {
                f1 = 1.0F;
            }

            if (f1 < 0.0F) {
                f1 = 0.0F;
            }

            float f2 = f1;
            int l2 = (int)(f2 * 155.0F + 100.0F);
            int i3 = (int)(f2 * f2 * 255.0F);
            int j3 = (int)(f2 * f2 * f2 * f2 * f2 * f2 * f2 * f2 * f2 * f2 * 255.0F);
            short short1 = 255;
            if (f2 < 0.5F) {
                short1 = 0;
            }

            f2 = (f2 - 0.5F) * 2.0F;
            if (this.anaglyphEnabled) {
                int l1 = (l2 * 30 + i3 * 59 + j3 * 11) / 100;
                int i2 = (l2 * 30 + i3 * 70) / 100;
                int j2 = (l2 * 30 + j3 * 70) / 100;
                l2 = l1;
                i3 = i2;
                j3 = j2;
            }

            this.imageData[k2 * 4 + 0] = (byte)l2;
            this.imageData[k2 * 4 + 1] = (byte)i3;
            this.imageData[k2 * 4 + 2] = (byte)j3;
            this.imageData[k2 * 4 + 3] = (byte)short1;
        }
    }
}
