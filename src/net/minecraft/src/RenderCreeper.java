package net.minecraft.src;

import org.lwjgl.opengl.GL11;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class RenderCreeper
extends RenderLiving<EntityCreeper> {
    private ModelBase model = new ModelCreeper(2.0f);

    public RenderCreeper() {
        super(new ModelCreeper(), 0.5f);
    }

    @Override
    protected void preRenderCallback(EntityCreeper entityCreeper, float f) {
        EntityCreeper entityCreeper2 = entityCreeper;
        float f2 = entityCreeper2.setCreeperFlashTime(f);
        float f3 = 1.0f + MathHelper.sin(f2 * 100.0f) * f2 * 0.01f;
        if (f2 < 0.0f) {
            f2 = 0.0f;
        }
        if (f2 > 1.0f) {
            f2 = 1.0f;
        }
        f2 *= f2;
        f2 *= f2;
        float f4 = (1.0f + f2 * 0.4f) * f3;
        float f5 = (1.0f + f2 * 0.1f) / f3;
        GL11.glScalef(f4, f5, f4);
    }

    @Override
    protected int getColorMultiplier(EntityCreeper entityCreeper, float f, float f2) {
        EntityCreeper entityCreeper2 = entityCreeper;
        float f3 = entityCreeper2.setCreeperFlashTime(f2);
        if ((int)(f3 * 10.0f) % 2 == 0) {
            return 0;
        }
        int n = (int)(f3 * 0.2f * 255.0f);
        if (n < 0) {
            n = 0;
        }
        if (n > 255) {
            n = 255;
        }
        int n2 = 255;
        int n3 = 255;
        int n4 = 255;
        return n << 24 | n2 << 16 | n3 << 8 | n4;
    }

    @Override
    protected boolean shouldRenderPass(EntityCreeper entityCreeper, int n, float f) {
        if (entityCreeper.getPowered()) {
            if (n == 1) {
                float f2 = (float)entityCreeper.ticksExisted + f;
                this.loadTexture("/armor/power.png");
                GL11.glMatrixMode(5890);
                GL11.glLoadIdentity();
                float f3 = f2 * 0.01f;
                float f4 = f2 * 0.01f;
                GL11.glTranslatef(f3, f4, 0.0f);
                this.setRenderPassModel(this.model);
                GL11.glMatrixMode(5888);
                GL11.glEnable(3042);
                float f5 = 0.5f;
                GL11.glColor4f(f5, f5, f5, 1.0f);
                GL11.glDisable(2896);
                GL11.glBlendFunc(1, 1);
                return true;
            }
            if (n == 2) {
                GL11.glMatrixMode(5890);
                GL11.glLoadIdentity();
                GL11.glMatrixMode(5888);
                GL11.glEnable(2896);
                GL11.glDisable(3042);
            }
        }
        return false;
    }

    @Override
    protected boolean bindDecorationTexture(EntityCreeper entityCreeper, int n, float f) {
        return false;
    }
}

