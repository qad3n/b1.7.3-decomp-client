package net.minecraft.src;

import org.lwjgl.opengl.GL11;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class RenderGiantZombie
extends RenderLiving<EntityGiantZombie> {
    private float scale;

    public RenderGiantZombie(ModelBase model, float shadowSize, float scale) {
        super(model, shadowSize * scale);
        this.scale = scale;
    }

    @Override
    protected void preRenderCallback(EntityGiantZombie entityGiantZombie, float f) {
        GL11.glScalef(this.scale, this.scale, this.scale);
    }
}

