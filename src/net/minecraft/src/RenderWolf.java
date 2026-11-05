package net.minecraft.src;


/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class RenderWolf
extends RenderLiving<EntityWolf> {
    public RenderWolf(ModelBase modelBase, float f) {
        super(modelBase, f);
    }

    @Override
    public void doRender(EntityWolf entityWolf, double d, double d2, double d3, float f, float f2) {
        super.doRender(entityWolf, d, d2, d3, f, f2);
    }

    @Override
    protected float getBob(EntityWolf entityWolf, float f) {
        return entityWolf.setTailRotation();
    }

    @Override
    protected void preRenderCallback(EntityWolf entityWolf, float f) {
    }
}

