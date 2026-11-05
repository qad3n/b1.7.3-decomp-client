package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class Gui {
    protected float zLevel = 0.0f;

    protected void drawHorizontalLine(int x1, int x2, int y, int color) {
        if (x2 < x1) {
            int n = x1;
            x1 = x2;
            x2 = n;
        }
        this.drawRect(x1, y, x2 + 1, y + 1, color);
    }

    protected void drawVerticalLine(int x, int y1, int y2, int color) {
        if (y2 < y1) {
            int n = y1;
            y1 = y2;
            y2 = n;
        }
        this.drawRect(x, y1 + 1, x + 1, y2, color);
    }

    protected void drawRect(int x1, int y1, int x2, int y2, int color) {
        int n;
        if (x1 < x2) {
            n = x1;
            x1 = x2;
            x2 = n;
        }
        if (y1 < y2) {
            n = y1;
            y1 = y2;
            y2 = n;
        }
        float f = (float)(color >> 24 & 0xFF) / 255.0f;
        float f2 = (float)(color >> 16 & 0xFF) / 255.0f;
        float f3 = (float)(color >> 8 & 0xFF) / 255.0f;
        float f4 = (float)(color & 0xFF) / 255.0f;
        Tessellator tessellator = Tessellator.instance;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glColor4f(f2, f3, f4, f);
        tessellator.startDrawingQuads();
        tessellator.addVertex(x1, y2, 0.0);
        tessellator.addVertex(x2, y2, 0.0);
        tessellator.addVertex(x2, y1, 0.0);
        tessellator.addVertex(x1, y1, 0.0);
        tessellator.draw();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
    }

    protected void drawGradientRect(int x1, int y1, int x2, int y2, int color1, int color2) {
        float f = (float)(color1 >> 24 & 0xFF) / 255.0f;
        float f2 = (float)(color1 >> 16 & 0xFF) / 255.0f;
        float f3 = (float)(color1 >> 8 & 0xFF) / 255.0f;
        float f4 = (float)(color1 & 0xFF) / 255.0f;
        float f5 = (float)(color2 >> 24 & 0xFF) / 255.0f;
        float f6 = (float)(color2 >> 16 & 0xFF) / 255.0f;
        float f7 = (float)(color2 >> 8 & 0xFF) / 255.0f;
        float f8 = (float)(color2 & 0xFF) / 255.0f;
        GL11.glDisable(3553);
        GL11.glEnable(3042);
        GL11.glDisable(3008);
        GL11.glBlendFunc(770, 771);
        GL11.glShadeModel(7425);
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA_F(f2, f3, f4, f);
        tessellator.addVertex(x2, y1, 0.0);
        tessellator.addVertex(x1, y1, 0.0);
        tessellator.setColorRGBA_F(f6, f7, f8, f5);
        tessellator.addVertex(x1, y2, 0.0);
        tessellator.addVertex(x2, y2, 0.0);
        tessellator.draw();
        GL11.glShadeModel(7424);
        GL11.glDisable(3042);
        GL11.glEnable(3008);
        GL11.glEnable(3553);
    }

    public void drawCenteredString(FontRenderer textRenderer, String text, int centerX, int y, int color) {
        textRenderer.drawStringWithShadow(text, centerX - textRenderer.getStringWidth(text) / 2, y, color);
    }

    public void drawString(FontRenderer textRenderer, String text, int x, int y, int color) {
        textRenderer.drawStringWithShadow(text, x, y, color);
    }

    public void drawTexturedModalRect(int x, int y, int u, int v, int width, int height) {
        float f = 0.00390625f;
        float f2 = 0.00390625f;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(x + 0, y + height, this.zLevel, (float)(u + 0) * f, (float)(v + height) * f2);
        tessellator.addVertexWithUV(x + width, y + height, this.zLevel, (float)(u + width) * f, (float)(v + height) * f2);
        tessellator.addVertexWithUV(x + width, y + 0, this.zLevel, (float)(u + width) * f, (float)(v + 0) * f2);
        tessellator.addVertexWithUV(x + 0, y + 0, this.zLevel, (float)(u + 0) * f, (float)(v + 0) * f2);
        tessellator.draw();
    }
}

