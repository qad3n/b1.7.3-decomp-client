package net.minecraft.src;

import org.lwjgl.opengl.GL11;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class RenderSnowball
extends Render<Entity> {
    private int itemIconIndex;

    public RenderSnowball(int sprite) {
        this.itemIconIndex = sprite;
    }

    @Override
    public void doRender(Entity entity, double dx, double dy, double dz, float yaw, float tickDelta) {
        GL11.glPushMatrix();
        GL11.glTranslatef((float)dx, (float)dy, (float)dz);
        GL11.glEnable(32826);
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        this.loadTexture("/gui/items.png");
        Tessellator tessellator = Tessellator.instance;
        float f = (float)(this.itemIconIndex % 16 * 16 + 0) / 256.0f;
        float f2 = (float)(this.itemIconIndex % 16 * 16 + 16) / 256.0f;
        float f3 = (float)(this.itemIconIndex / 16 * 16 + 0) / 256.0f;
        float f4 = (float)(this.itemIconIndex / 16 * 16 + 16) / 256.0f;
        float f5 = 1.0f;
        float f6 = 0.5f;
        float f7 = 0.25f;
        GL11.glRotatef(180.0f - this.renderManager.playerViewY, 0.0f, 1.0f, 0.0f);
        GL11.glRotatef(-this.renderManager.playerViewX, 1.0f, 0.0f, 0.0f);
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0f, 1.0f, 0.0f);
        tessellator.addVertexWithUV(0.0f - f6, 0.0f - f7, 0.0, f, f4);
        tessellator.addVertexWithUV(f5 - f6, 0.0f - f7, 0.0, f2, f4);
        tessellator.addVertexWithUV(f5 - f6, 1.0f - f7, 0.0, f2, f3);
        tessellator.addVertexWithUV(0.0f - f6, 1.0f - f7, 0.0, f, f3);
        tessellator.draw();
        GL11.glDisable(32826);
        GL11.glPopMatrix();
    }
}

