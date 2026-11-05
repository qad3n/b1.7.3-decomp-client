package net.minecraft.src;

import java.util.Random;

public class TexturePortalFX extends TextureFX {
    private int portalTickCounter = 0;
    private byte[][] portalTextureData = new byte[32][1024];

    public TexturePortalFX() {
        super(Block.portal.blockIndexInTexture);
        Random random = new Random(100L);

        for (int i = 0; i < 32; i++) {
            for (int j = 0; j < 16; j++) {
                for (int k = 0; k < 16; k++) {
                    float f = 0.0F;

                    for (int l = 0; l < 2; l++) {
                        float f1 = l * 8;
                        float f2 = l * 8;
                        float f3 = (j - f1) / 16.0F * 2.0F;
                        float f4 = (k - f2) / 16.0F * 2.0F;
                        if (f3 < -1.0F) {
                            f3 += 2.0F;
                        }

                        if (f3 >= 1.0F) {
                            f3 -= 2.0F;
                        }

                        if (f4 < -1.0F) {
                            f4 += 2.0F;
                        }

                        if (f4 >= 1.0F) {
                            f4 -= 2.0F;
                        }

                        float f5 = f3 * f3 + f4 * f4;
                        float f6 = (float)Math.atan2(f4, f3) + (i / 32.0F * (float) Math.PI * 2.0F - f5 * 10.0F + l * 2) * (l * 2 - 1);
                        f6 = (MathHelper.sin(f6) + 1.0F) / 2.0F;
                        f6 /= f5 + 1.0F;
                        f += f6 * 0.5F;
                    }

                    f += random.nextFloat() * 0.1F;
                    int i1 = (int)(f * 100.0F + 155.0F);
                    int j1 = (int)(f * f * 200.0F + 55.0F);
                    int k1 = (int)(f * f * f * f * 255.0F);
                    int l1 = (int)(f * 100.0F + 155.0F);
                    int i2 = k * 16 + j;
                    this.portalTextureData[i][i2 * 4 + 0] = (byte)j1;
                    this.portalTextureData[i][i2 * 4 + 1] = (byte)k1;
                    this.portalTextureData[i][i2 * 4 + 2] = (byte)i1;
                    this.portalTextureData[i][i2 * 4 + 3] = (byte)l1;
                }
            }
        }
    }

    @Override
    public void onTick() {
        this.portalTickCounter++;
        byte[] abyte = this.portalTextureData[this.portalTickCounter & 31];

        for (int i = 0; i < 256; i++) {
            int j = abyte[i * 4 + 0] & 255;
            int k = abyte[i * 4 + 1] & 255;
            int l = abyte[i * 4 + 2] & 255;
            int i1 = abyte[i * 4 + 3] & 255;
            if (this.anaglyphEnabled) {
                int j1 = (j * 30 + k * 59 + l * 11) / 100;
                int k1 = (j * 30 + k * 70) / 100;
                int l1 = (j * 30 + l * 70) / 100;
                j = j1;
                k = k1;
                l = l1;
            }

            this.imageData[i * 4 + 0] = (byte)j;
            this.imageData[i * 4 + 1] = (byte)k;
            this.imageData[i * 4 + 2] = (byte)l;
            this.imageData[i * 4 + 3] = (byte)i1;
        }
    }
}
