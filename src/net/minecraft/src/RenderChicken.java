package net.minecraft.src;


/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class RenderChicken
extends RenderLiving<EntityChicken> {
    public RenderChicken(ModelBase modelBase, float f) {
        super(modelBase, f);
    }

    @Override
    public void doRender(EntityChicken entityChicken, double d, double d2, double d3, float f, float f2) {
        super.doRender(entityChicken, d, d2, d3, f, f2);
    }

    @Override
    protected float getBob(EntityChicken entityChicken, float f) {
        float f2 = entityChicken.lastFlapProgress + (entityChicken.flapProgress - entityChicken.lastFlapProgress) * f;
        float f3 = entityChicken.lastFlapSpeed + (entityChicken.destPos - entityChicken.lastFlapSpeed) * f;
        return (MathHelper.sin(f2) + 1.0f) * f3;
    }
}

