package net.minecraft.src;

import org.lwjgl.opengl.GL11;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class RenderFireball
extends Render<EntityFireball> {
    @Override
    public void doRender(EntityFireball entityFireball, double d, double d2, double d3, float f, float f2) {
        GL11.glPushMatrix();
        GL11.glTranslatef((float)d, (float)d2, (float)d3);
        GL11.glEnable(32826);
        float f3 = 2.0f;
        GL11.glScalef(f3 / 1.0f, f3 / 1.0f, f3 / 1.0f);
        int n = Item.snowball.getIconFromDamage(0);
        this.loadTexture("/gui/items.png");
        Tessellator tessellator = Tessellator.instance;
        float f4 = (float)(n % 16 * 16 + 0) / 256.0f;
        float f5 = (float)(n % 16 * 16 + 16) / 256.0f;
        float f6 = (float)(n / 16 * 16 + 0) / 256.0f;
        float f7 = (float)(n / 16 * 16 + 16) / 256.0f;
        float f8 = 1.0f;
        float f9 = 0.5f;
        float f10 = 0.25f;
        GL11.glRotatef(180.0f - this.renderManager.playerViewY, 0.0f, 1.0f, 0.0f);
        GL11.glRotatef(-this.renderManager.playerViewX, 1.0f, 0.0f, 0.0f);
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0f, 1.0f, 0.0f);
        tessellator.addVertexWithUV(0.0f - f9, 0.0f - f10, 0.0, f4, f7);
        tessellator.addVertexWithUV(f8 - f9, 0.0f - f10, 0.0, f5, f7);
        tessellator.addVertexWithUV(f8 - f9, 1.0f - f10, 0.0, f5, f6);
        tessellator.addVertexWithUV(0.0f - f9, 1.0f - f10, 0.0, f4, f6);
        tessellator.draw();
        GL11.glDisable(32826);
        GL11.glPopMatrix();
    }
}

