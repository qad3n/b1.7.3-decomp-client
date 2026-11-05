package net.minecraft.src;

import org.lwjgl.opengl.GL11;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class RenderSlime
extends RenderLiving<EntitySlime> {
    private ModelBase scaleAmount;

    public RenderSlime(ModelBase model, ModelBase innerModel, float shadowSize) {
        super(model, shadowSize);
        this.scaleAmount = innerModel;
    }

    @Override
    protected boolean shouldRenderPass(EntitySlime entitySlime, int n, float f) {
        if (n == 0) {
            this.setRenderPassModel(this.scaleAmount);
            GL11.glEnable(2977);
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            return true;
        }
        if (n == 1) {
            GL11.glDisable(3042);
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        }
        return false;
    }

    @Override
    protected void preRenderCallback(EntitySlime entitySlime, float f) {
        int n = entitySlime.getSlimeSize();
        float f2 = (entitySlime.lastStretch + (entitySlime.stretch - entitySlime.lastStretch) * f) / ((float)n * 0.5f + 1.0f);
        float f3 = 1.0f / (f2 + 1.0f);
        float f4 = n;
        GL11.glScalef(f3 * f4, 1.0f / f3 * f4, f3 * f4);
    }
}

