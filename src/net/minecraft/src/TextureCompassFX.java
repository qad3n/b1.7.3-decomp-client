package net.minecraft.src;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;

public class TextureCompassFX
extends TextureFX {
    private Minecraft mc;
    private int[] compassIconImageData = new int[256];
    private double angle;
    private double angleDelta;

    public TextureCompassFX(Minecraft minecraft) {
        super(Item.compass.getIconFromDamage(0));
        this.mc = minecraft;
        this.tileImage = 1;
        try {
            BufferedImage bufferedImage = ImageIO.read(Minecraft.class.getResource("/gui/items.png"));
            int n = this.iconIndex % 16 * 16;
            int n2 = this.iconIndex / 16 * 16;
            bufferedImage.getRGB(n, n2, 16, 16, this.compassIconImageData, 0, 16);
        }
        catch (IOException iOException) {
            iOException.printStackTrace();
        }
    }

    public void onTick() {
        int n;
        int n2;
        int n3;
        int n4;
        int n5;
        int n6;
        int n7;
        int n8;
        int n9;
        int n10;
        int n11;
        double d;
        for (int i = 0; i < 256; ++i) {
            int n12 = this.compassIconImageData[i] >> 24 & 0xFF;
            int n13 = this.compassIconImageData[i] >> 16 & 0xFF;
            int n14 = this.compassIconImageData[i] >> 8 & 0xFF;
            int n15 = this.compassIconImageData[i] >> 0 & 0xFF;
            if (this.anaglyphEnabled) {
                int n16 = (n13 * 30 + n14 * 59 + n15 * 11) / 100;
                int n17 = (n13 * 30 + n14 * 70) / 100;
                int n18 = (n13 * 30 + n15 * 70) / 100;
                n13 = n16;
                n14 = n17;
                n15 = n18;
            }
            this.imageData[i * 4 + 0] = (byte)n13;
            this.imageData[i * 4 + 1] = (byte)n14;
            this.imageData[i * 4 + 2] = (byte)n15;
            this.imageData[i * 4 + 3] = (byte)n12;
        }
        double d2 = 0.0;
        if (this.mc.theWorld != null && this.mc.thePlayer != null) {
            ChunkCoordinates chunkCoordinates = this.mc.theWorld.getSpawnPoint();
            double d3 = (double)chunkCoordinates.x - this.mc.thePlayer.posX;
            double d4 = (double)chunkCoordinates.z - this.mc.thePlayer.posZ;
            d2 = (double)(this.mc.thePlayer.rotationYaw - 90.0f) * Math.PI / 180.0 - Math.atan2(d4, d3);
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
        double d5 = Math.sin(this.angle);
        double d6 = Math.cos(this.angle);
        for (n11 = -4; n11 <= 4; ++n11) {
            n10 = (int)(8.5 + d6 * (double)n11 * 0.3);
            n9 = (int)(7.5 - d5 * (double)n11 * 0.3 * 0.5);
            n8 = n9 * 16 + n10;
            n7 = 100;
            n6 = 100;
            n5 = 100;
            n4 = 255;
            if (this.anaglyphEnabled) {
                n3 = (n7 * 30 + n6 * 59 + n5 * 11) / 100;
                n2 = (n7 * 30 + n6 * 70) / 100;
                n = (n7 * 30 + n5 * 70) / 100;
                n7 = n3;
                n6 = n2;
                n5 = n;
            }
            this.imageData[n8 * 4 + 0] = (byte)n7;
            this.imageData[n8 * 4 + 1] = (byte)n6;
            this.imageData[n8 * 4 + 2] = (byte)n5;
            this.imageData[n8 * 4 + 3] = (byte)n4;
        }
        for (n11 = -8; n11 <= 16; ++n11) {
            n10 = (int)(8.5 + d5 * (double)n11 * 0.3);
            n9 = (int)(7.5 + d6 * (double)n11 * 0.3 * 0.5);
            n8 = n9 * 16 + n10;
            n7 = n11 >= 0 ? 255 : 100;
            n6 = n11 >= 0 ? 20 : 100;
            n5 = n11 >= 0 ? 20 : 100;
            n4 = 255;
            if (this.anaglyphEnabled) {
                n3 = (n7 * 30 + n6 * 59 + n5 * 11) / 100;
                n2 = (n7 * 30 + n6 * 70) / 100;
                n = (n7 * 30 + n5 * 70) / 100;
                n7 = n3;
                n6 = n2;
                n5 = n;
            }
            this.imageData[n8 * 4 + 0] = (byte)n7;
            this.imageData[n8 * 4 + 1] = (byte)n6;
            this.imageData[n8 * 4 + 2] = (byte)n5;
            this.imageData[n8 * 4 + 3] = (byte)n4;
        }
    }
}

