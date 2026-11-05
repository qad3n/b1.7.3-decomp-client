package net.minecraft.src;

import org.lwjgl.opengl.GL11;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class RenderSheep
extends RenderLiving<EntitySheep> {
    public RenderSheep(ModelBase model, ModelBase furModel, float shadowSize) {
        super(model, shadowSize);
        this.setRenderPassModel(furModel);
    }

    @Override
    protected boolean shouldRenderPass(EntitySheep entitySheep, int n, float f) {
        if (n == 0 && !entitySheep.getSheared()) {
            this.loadTexture("/mob/sheep_fur.png");
            float f2 = entitySheep.getEntityBrightness(f);
            int n2 = entitySheep.getFleeceColor();
            GL11.glColor3f(f2 * EntitySheep.fleeceColorTable[n2][0], f2 * EntitySheep.fleeceColorTable[n2][1], f2 * EntitySheep.fleeceColorTable[n2][2]);
            return true;
        }
        return false;
    }
}

