package net.minecraft.src;

import org.lwjgl.opengl.GL11;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class RenderTNTPrimed
extends Render<EntityTNTPrimed> {
    private RenderBlocks blockRenderer = new RenderBlocks();

    public RenderTNTPrimed() {
        this.shadowSize = 0.5f;
    }

    @Override
    public void doRender(EntityTNTPrimed entityTNTPrimed, double d, double d2, double d3, float f, float f2) {
        float f3;
        GL11.glPushMatrix();
        GL11.glTranslatef((float)d, (float)d2, (float)d3);
        if ((float)entityTNTPrimed.fuse - f2 + 1.0f < 10.0f) {
            f3 = 1.0f - ((float)entityTNTPrimed.fuse - f2 + 1.0f) / 10.0f;
            if (f3 < 0.0f) {
                f3 = 0.0f;
            }
            if (f3 > 1.0f) {
                f3 = 1.0f;
            }
            f3 *= f3;
            f3 *= f3;
            float f4 = 1.0f + f3 * 0.3f;
            GL11.glScalef(f4, f4, f4);
        }
        f3 = (1.0f - ((float)entityTNTPrimed.fuse - f2 + 1.0f) / 100.0f) * 0.8f;
        this.loadTexture("/terrain.png");
        this.blockRenderer.renderBlockOnInventory(Block.tnt, 0, entityTNTPrimed.getEntityBrightness(f2));
        if (entityTNTPrimed.fuse / 5 % 2 == 0) {
            GL11.glDisable(3553);
            GL11.glDisable(2896);
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 772);
            GL11.glColor4f(1.0f, 1.0f, 1.0f, f3);
            this.blockRenderer.renderBlockOnInventory(Block.tnt, 0, 1.0f);
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            GL11.glDisable(3042);
            GL11.glEnable(2896);
            GL11.glEnable(3553);
        }
        GL11.glPopMatrix();
    }
}

