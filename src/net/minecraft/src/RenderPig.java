package net.minecraft.src;


/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class RenderPig
extends RenderLiving<EntityPig> {
    public RenderPig(ModelBase model, ModelBase saddleModel, float shadowSize) {
        super(model, shadowSize);
        this.setRenderPassModel(saddleModel);
    }

    @Override
    protected boolean shouldRenderPass(EntityPig entityPig, int n, float f) {
        this.loadTexture("/mob/saddle.png");
        return n == 0 && entityPig.getSaddled();
    }
}

