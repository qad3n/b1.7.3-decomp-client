package net.minecraft.src;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class ImageBufferDownload implements ImageBuffer {
    private int[] imageData;
    private int imageWidth;
    private int imageHeight;

    @Override
    public BufferedImage parseUserSkin(BufferedImage image) {
        if (image == null) {
            return null;
        }

        this.imageWidth = 64;
        this.imageHeight = 32;
        BufferedImage bufferedimage = new BufferedImage(this.imageWidth, this.imageHeight, 2);
        Graphics graphics = bufferedimage.getGraphics();
        graphics.drawImage(image, 0, 0, null);
        graphics.dispose();
        this.imageData = ((DataBufferInt)bufferedimage.getRaster().getDataBuffer()).getData();
        this.setOpaque(0, 0, 32, 16);
        this.setTransparent(32, 0, 64, 32);
        this.setOpaque(0, 16, 64, 32);
        boolean flag = false;

        for (int i = 32; i < 64; i++) {
            for (int j = 0; j < 16; j++) {
                int k = this.imageData[i + j * 64];
                if ((k >> 24 & 0xFF) < 128) {
                    flag = true;
                }
            }
        }

        if (!flag) {
            for (int l = 32; l < 64; l++) {
                for (int i1 = 0; i1 < 16; i1++) {
                    int j1 = this.imageData[l + i1 * 64];
                    if ((j1 >> 24 & 0xFF) < 128) {
                        flag = true;
                    }
                }
            }
        }

        return bufferedimage;
    }

    private void setTransparent(int u1, int v1, int u2, int v2) {
        if (!this.hasTransperancy(u1, v1, u2, v2)) {
            for (int i = u1; i < u2; i++) {
                for (int j = v1; j < v2; j++) {
                    this.imageData[i + j * this.imageWidth] = this.imageData[i + j * this.imageWidth] & 16777215;
                }
            }
        }
    }

    private void setOpaque(int u1, int v1, int u2, int v2) {
        for (int i = u1; i < u2; i++) {
            for (int j = v1; j < v2; j++) {
                this.imageData[i + j * this.imageWidth] = this.imageData[i + j * this.imageWidth] | 0xFF000000;
            }
        }
    }

    private boolean hasTransperancy(int u1, int v1, int u2, int v2) {
        for (int i = u1; i < u2; i++) {
            for (int j = v1; j < v2; j++) {
                int k = this.imageData[i + j * this.imageWidth];
                if ((k >> 24 & 0xFF) < 128) {
                    return true;
                }
            }
        }

        return false;
    }
}
