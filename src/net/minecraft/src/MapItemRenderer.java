package net.minecraft.src;

import java.awt.image.BufferedImage;
import org.lwjgl.opengl.GL11;

public class MapItemRenderer {
    private int[] colors = new int[16384];
    private int texture;
    private GameSettings options;
    private FontRenderer textRenderer;

    public MapItemRenderer(FontRenderer textRenderer, GameSettings options, RenderEngine textureManager) {
        this.options = options;
        this.textRenderer = textRenderer;
        this.texture = textureManager.allocateAndSetupTexture(new BufferedImage(128, 128, 2));
        for (int i = 0; i < 16384; ++i) {
            this.colors[i] = 0;
        }
    }

    public void render(EntityPlayer player, RenderEngine textureManager, MapData mapData) {
        byte by;
        int n;
        for (n = 0; n < 16384; ++n) {
            by = mapData.colors[n];
            if (by / 4 == 0) {
                this.colors[n] = (n + n / 128 & 1) * 8 + 16 << 24;
                continue;
            }
            int n2 = MapColor.mapColorArray[by / 4].colorValue;
            int n3 = by & 3;
            int n4 = 220;
            if (n3 == 2) {
                n4 = 255;
            }
            if (n3 == 0) {
                n4 = 180;
            }
            int n5 = (n2 >> 16 & 0xFF) * n4 / 255;
            int n6 = (n2 >> 8 & 0xFF) * n4 / 255;
            int n7 = (n2 & 0xFF) * n4 / 255;
            if (this.options.anaglyph) {
                int n8 = (n5 * 30 + n6 * 59 + n7 * 11) / 100;
                int n9 = (n5 * 30 + n6 * 70) / 100;
                int n10 = (n5 * 30 + n7 * 70) / 100;
                n5 = n8;
                n6 = n9;
                n7 = n10;
            }
            this.colors[n] = 0xFF000000 | n5 << 16 | n6 << 8 | n7;
        }
        textureManager.bind(this.colors, 128, 128, this.texture);
        n = 0;
        by = 0;
        Tessellator tessellator = Tessellator.instance;
        float f = 0.0f;
        GL11.glBindTexture(3553, this.texture);
        GL11.glEnable(3042);
        GL11.glDisable(3008);
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV((float)(n + 0) + f, (float)(by + 128) - f, -0.01f, 0.0, 1.0);
        tessellator.addVertexWithUV((float)(n + 128) - f, (float)(by + 128) - f, -0.01f, 1.0, 1.0);
        tessellator.addVertexWithUV((float)(n + 128) - f, (float)(by + 0) + f, -0.01f, 1.0, 0.0);
        tessellator.addVertexWithUV((float)(n + 0) + f, (float)(by + 0) + f, -0.01f, 0.0, 0.0);
        tessellator.draw();
        GL11.glEnable(3008);
        GL11.glDisable(3042);
        textureManager.bindTexture(textureManager.getTexture("/misc/mapicons.png"));
        for (MapData.MapCoord mapCoord : mapData.decorations) {
            GL11.glPushMatrix();
            GL11.glTranslatef((float)n + (float)mapCoord.x / 2.0f + 64.0f, (float)by + (float)mapCoord.z / 2.0f + 64.0f, -0.02f);
            GL11.glRotatef((float)(mapCoord.rotation * 360) / 16.0f, 0.0f, 0.0f, 1.0f);
            GL11.glScalef(4.0f, 4.0f, 3.0f);
            GL11.glTranslatef(-0.125f, 0.125f, 0.0f);
            float f2 = (float)(mapCoord.type % 4 + 0) / 4.0f;
            float f3 = (float)(mapCoord.type / 4 + 0) / 4.0f;
            float f4 = (float)(mapCoord.type % 4 + 1) / 4.0f;
            float f5 = (float)(mapCoord.type / 4 + 1) / 4.0f;
            tessellator.startDrawingQuads();
            tessellator.addVertexWithUV(-1.0, 1.0, 0.0, f2, f3);
            tessellator.addVertexWithUV(1.0, 1.0, 0.0, f4, f3);
            tessellator.addVertexWithUV(1.0, -1.0, 0.0, f4, f5);
            tessellator.addVertexWithUV(-1.0, -1.0, 0.0, f2, f5);
            tessellator.draw();
            GL11.glPopMatrix();
        }
        GL11.glPushMatrix();
        GL11.glTranslatef(0.0f, 0.0f, -0.04f);
        GL11.glScalef(1.0f, 1.0f, 1.0f);
        this.textRenderer.drawString(mapData.id, n, by, -16777216);
        GL11.glPopMatrix();
    }
}

