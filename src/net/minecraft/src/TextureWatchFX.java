package net.minecraft.src;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;

public class TextureWatchFX
extends TextureFX {
    private Minecraft mc;
    private int[] watchIconImageData = new int[256];
    private int[] dialImageData = new int[256];
    private double angle;
    private double angleDelta;

    public TextureWatchFX(Minecraft minecraft) {
        super(Item.pocketSundial.getIconFromDamage(0));
        this.mc = minecraft;
        this.tileImage = 1;
        try {
            BufferedImage bufferedImage = ImageIO.read(Minecraft.class.getResource("/gui/items.png"));
            int n = this.iconIndex % 16 * 16;
            int n2 = this.iconIndex / 16 * 16;
            bufferedImage.getRGB(n, n2, 16, 16, this.watchIconImageData, 0, 16);
            bufferedImage = ImageIO.read(Minecraft.class.getResource("/misc/dial.png"));
            bufferedImage.getRGB(0, 0, 16, 16, this.dialImageData, 0, 16);
        }
        catch (IOException iOException) {
            iOException.printStackTrace();
        }
    }

    public void onTick() {
        double d;
        double d2 = 0.0;
        if (this.mc.theWorld != null && this.mc.thePlayer != null) {
            float f = this.mc.theWorld.getCelestialAngle(1.0f);
            d2 = -f * (float)Math.PI * 2.0f;
            if (this.mc.theWorld.worldProvider.isNether) {
                d2 = Math.random() * 3.1415927410125732 * 2.0;
            }
        }
        for (d = d2 - this.angle; d < -Math.PI; d += Math.PI * 2) {
        }
        while (d >= Math.PI) {
            d -= Math.PI * 2;
        }
        if (d < -1.0) {
            d = -1.0;
        }
        if (d > 1.0) {
            d = 1.0;
        }
        this.angleDelta += d * 0.1;
        this.angleDelta *= 0.8;
        this.angle += this.angleDelta;
        double d3 = Math.sin(this.angle);
        double d4 = Math.cos(this.angle);
        for (int i = 0; i < 256; ++i) {
            int n = this.watchIconImageData[i] >> 24 & 0xFF;
            int n2 = this.watchIconImageData[i] >> 16 & 0xFF;
            int n3 = this.watchIconImageData[i] >> 8 & 0xFF;
            int n4 = this.watchIconImageData[i] >> 0 & 0xFF;
            if (n2 == n4 && n3 == 0 && n4 > 0) {
                double d5 = -((double)(i % 16) / 15.0 - 0.5);
                double d6 = (double)(i / 16) / 15.0 - 0.5;
                int n5 = n2;
                int n6 = (int)((d5 * d4 + d6 * d3 + 0.5) * 16.0);
                int n7 = (int)((d6 * d4 - d5 * d3 + 0.5) * 16.0);
                int n8 = (n6 & 0xF) + (n7 & 0xF) * 16;
                n = this.dialImageData[n8] >> 24 & 0xFF;
                n2 = (this.dialImageData[n8] >> 16 & 0xFF) * n5 / 255;
                n3 = (this.dialImageData[n8] >> 8 & 0xFF) * n5 / 255;
                n4 = (this.dialImageData[n8] >> 0 & 0xFF) * n5 / 255;
            }
            if (this.anaglyphEnabled) {
                int n9 = (n2 * 30 + n3 * 59 + n4 * 11) / 100;
                int n10 = (n2 * 30 + n3 * 70) / 100;
                int n11 = (n2 * 30 + n4 * 70) / 100;
                n2 = n9;
                n3 = n10;
                n4 = n11;
            }
            this.imageData[i * 4 + 0] = (byte)n2;
            this.imageData[i * 4 + 1] = (byte)n3;
            this.imageData[i * 4 + 2] = (byte)n4;
            this.imageData[i * 4 + 3] = (byte)n;
        }
    }
}

