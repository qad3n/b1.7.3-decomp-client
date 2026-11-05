package net.minecraft.src;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import javax.imageio.ImageIO;

public class TerrainTextureManager {
    private float[] colors = new float[768];
    private int[] pixels = new int[5120];
    private int[] height = new int[5120];
    private int[] waterHeight = new int[5120];
    private int[] waterBrightness = new int[5120];
    private int[] depth = new int[34];
    private int[] sprites = new int[768];

    public TerrainTextureManager() {
        try {
            BufferedImage bufferedimage = ImageIO.read(TerrainTextureManager.class.getResource("/terrain.png"));
            int[] aint = new int[65536];
            bufferedimage.getRGB(0, 0, 256, 256, aint, 0, 256);

            for (int i = 0; i < 256; i++) {
                int j = 0;
                int k = 0;
                int l = 0;
                int i1 = i % 16 * 16;
                int j1 = i / 16 * 16;
                int k1 = 0;

                for (int l1 = 0; l1 < 16; l1++) {
                    for (int i2 = 0; i2 < 16; i2++) {
                        int j2 = aint[i2 + i1 + (l1 + j1) * 256];
                        int k2 = j2 >> 24 & 0xFF;
                        if (k2 > 128) {
                            j += j2 >> 16 & 0xFF;
                            k += j2 >> 8 & 0xFF;
                            l += j2 & 0xFF;
                            k1++;
                        }
                    }

                    if (k1 == 0) {
                        k1++;
                    }

                    this.colors[i * 3 + 0] = j / k1;
                    this.colors[i * 3 + 1] = k / k1;
                    this.colors[i * 3 + 2] = l / k1;
                }
            }
        } catch (IOException ioexception) {
            ioexception.printStackTrace();
        }

        for (int l2 = 0; l2 < 256; l2++) {
            if (Block.blocksList[l2] != null) {
                this.sprites[l2 * 3 + 0] = Block.blocksList[l2].getBlockTextureFromSide(1);
                this.sprites[l2 * 3 + 1] = Block.blocksList[l2].getBlockTextureFromSide(2);
                this.sprites[l2 * 3 + 2] = Block.blocksList[l2].getBlockTextureFromSide(3);
            }
        }
    }

    public void render(IsoImageBuffer chunk) {
        World world = chunk.worldObj;
        if (world == null) {
            chunk.empty = true;
            chunk.rendered = true;
        } else {
            int i = chunk.chunkX * 16;
            int j = chunk.chunkZ * 16;
            int k = i + 16;
            int l = j + 16;
            Chunk chunkx = world.getChunkFromChunkCoords(chunk.chunkX, chunk.chunkZ);
            if (chunkx.isEmpty()) {
                chunk.empty = true;
                chunk.rendered = true;
            } else {
                chunk.empty = false;
                Arrays.fill(this.height, 0);
                Arrays.fill(this.waterHeight, 0);
                Arrays.fill(this.depth, 160);

                for (int i1 = l - 1; i1 >= j; i1--) {
                    for (int j1 = k - 1; j1 >= i; j1--) {
                        int k1 = j1 - i;
                        int l1 = i1 - j;
                        int i2 = k1 + l1;
                        boolean flag = true;

                        for (int j2 = 0; j2 < 128; j2++) {
                            int k2 = l1 - k1 - j2 + 160 - 16;
                            if (k2 < this.depth[i2] || k2 < this.depth[i2 + 1]) {
                                Block block = Block.blocksList[world.getBlockId(j1, j2, i1)];
                                if (block == null) {
                                    flag = false;
                                } else if (block.blockMaterial == Material.water) {
                                    int l2 = world.getBlockId(j1, j2 + 1, i1);
                                    if (l2 == 0 || Block.blocksList[l2].blockMaterial != Material.water) {
                                        float f = j2 / 127.0F * 0.6F + 0.4F;
                                        float f1 = world.getLightBrightness(j1, j2 + 1, i1) * f;
                                        if (k2 >= 0 && k2 < 160) {
                                            int i3 = i2 + k2 * 32;
                                            if (i2 >= 0 && i2 <= 32 && this.waterHeight[i3] <= j2) {
                                                this.waterHeight[i3] = j2;
                                                this.waterBrightness[i3] = (int)(f1 * 127.0F);
                                            }

                                            if (i2 >= -1 && i2 <= 31 && this.waterHeight[i3 + 1] <= j2) {
                                                this.waterHeight[i3 + 1] = j2;
                                                this.waterBrightness[i3 + 1] = (int)(f1 * 127.0F);
                                            }

                                            flag = false;
                                        }
                                    }
                                } else {
                                    if (flag) {
                                        if (k2 < this.depth[i2]) {
                                            this.depth[i2] = k2;
                                        }

                                        if (k2 < this.depth[i2 + 1]) {
                                            this.depth[i2 + 1] = k2;
                                        }
                                    }

                                    float f4 = j2 / 127.0F * 0.6F + 0.4F;
                                    if (k2 >= 0 && k2 < 160) {
                                        int k3 = i2 + k2 * 32;
                                        int i4 = this.sprites[block.blockID * 3 + 0];
                                        float f5 = (world.getLightBrightness(j1, j2 + 1, i1) * 0.8F + 0.2F) * f4;
                                        int j3 = i4;
                                        if (i2 >= 0) {
                                            float f2 = f5;
                                            if (this.height[k3] <= j2) {
                                                this.height[k3] = j2;
                                                this.pixels[k3] = 0xFF000000
                                                    | (int)(this.colors[j3 * 3 + 0] * f2) << 16
                                                    | (int)(this.colors[j3 * 3 + 1] * f2) << 8
                                                    | (int)(this.colors[j3 * 3 + 2] * f2);
                                            }
                                        }

                                        if (i2 < 31) {
                                            float f7 = f5 * 0.9F;
                                            if (this.height[k3 + 1] <= j2) {
                                                this.height[k3 + 1] = j2;
                                                this.pixels[k3 + 1] = 0xFF000000
                                                    | (int)(this.colors[j3 * 3 + 0] * f7) << 16
                                                    | (int)(this.colors[j3 * 3 + 1] * f7) << 8
                                                    | (int)(this.colors[j3 * 3 + 2] * f7);
                                            }
                                        }
                                    }

                                    if (k2 >= -1 && k2 < 159) {
                                        int l3 = i2 + (k2 + 1) * 32;
                                        int j4 = this.sprites[block.blockID * 3 + 1];
                                        float f6 = world.getLightBrightness(j1 - 1, j2, i1) * 0.8F + 0.2F;
                                        int k4 = this.sprites[block.blockID * 3 + 2];
                                        float f8 = world.getLightBrightness(j1, j2, i1 + 1) * 0.8F + 0.2F;
                                        if (i2 >= 0) {
                                            float f3 = f6 * f4 * 0.6F;
                                            if (this.height[l3] <= j2 - 1) {
                                                this.height[l3] = j2 - 1;
                                                this.pixels[l3] = 0xFF000000
                                                    | (int)(this.colors[j4 * 3 + 0] * f3) << 16
                                                    | (int)(this.colors[j4 * 3 + 1] * f3) << 8
                                                    | (int)(this.colors[j4 * 3 + 2] * f3);
                                            }
                                        }

                                        if (i2 < 31) {
                                            float f9 = f8 * 0.9F * f4 * 0.4F;
                                            if (this.height[l3 + 1] <= j2 - 1) {
                                                this.height[l3 + 1] = j2 - 1;
                                                this.pixels[l3 + 1] = 0xFF000000
                                                    | (int)(this.colors[k4 * 3 + 0] * f9) << 16
                                                    | (int)(this.colors[k4 * 3 + 1] * f9) << 8
                                                    | (int)(this.colors[k4 * 3 + 2] * f9);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                this.postProcess();
                if (chunk.image == null) {
                    chunk.image = new BufferedImage(32, 160, 2);
                }

                chunk.image.setRGB(0, 0, 32, 160, this.pixels, 0, 32);
                chunk.rendered = true;
            }
        }
    }

    private void postProcess() {
        for (int i = 0; i < 32; i++) {
            for (int j = 0; j < 160; j++) {
                int k = i + j * 32;
                if (this.height[k] == 0) {
                    this.pixels[k] = 0;
                }

                if (this.waterHeight[k] > this.height[k]) {
                    int l = this.pixels[k] >> 24 & 0xFF;
                    this.pixels[k] = ((this.pixels[k] & 16711422) >> 1) + this.waterBrightness[k];
                    if (l < 128) {
                        this.pixels[k] = Integer.MIN_VALUE + this.waterBrightness[k] * 2;
                    } else {
                        this.pixels[k] = this.pixels[k] | 0xFF000000;
                    }
                }
            }
        }
    }
}
