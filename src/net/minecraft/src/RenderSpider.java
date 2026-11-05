package net.minecraft.src;

import org.lwjgl.opengl.GL11;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class RenderSpider
extends RenderLiving<EntitySpider> {
    public RenderSpider() {
        super(new ModelSpider(), 1.0f);
        this.setRenderPassModel(new ModelSpider());
    }

    @Override
    protected float getDeathMaxRotation(EntitySpider entitySpider) {
        return 180.0f;
    }

    @Override
    protected boolean shouldRenderPass(EntitySpider entitySpider, int n, float f) {
        if (n != 0) {
            return false;
        }
        if (n != 0) {
            return false;
        }
        this.loadTexture("/mob/spider_eyes.png");
        float f2 = (1.0f - entitySpider.getEntityBrightness(1.0f)) * 0.5f;
        GL11.glEnable(3042);
        GL11.glDisable(3008);
        GL11.glBlendFunc(770, 771);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, f2);
        return true;
    }
}

