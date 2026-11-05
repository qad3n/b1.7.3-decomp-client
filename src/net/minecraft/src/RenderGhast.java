package net.minecraft.src;

import org.lwjgl.opengl.GL11;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class RenderGhast
extends RenderLiving<EntityGhast> {
    public RenderGhast() {
        super(new ModelGhast(), 0.5f);
    }

    @Override
    protected void preRenderCallback(EntityGhast entityGhast, float f) {
        EntityGhast entityGhast2 = entityGhast;
        float f2 = ((float)entityGhast2.prevAttackCounter + (float)(entityGhast2.attackCounter - entityGhast2.prevAttackCounter) * f) / 20.0f;
        if (f2 < 0.0f) {
            f2 = 0.0f;
        }
        f2 = 1.0f / (f2 * f2 * f2 * f2 * f2 * 2.0f + 1.0f);
        float f3 = (8.0f + f2) / 2.0f;
        float f4 = (8.0f + 1.0f / f2) / 2.0f;
        GL11.glScalef(f4, f3, f4);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    }
}

