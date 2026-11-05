package net.minecraft.src;

import org.lwjgl.opengl.GL11;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class RenderSquid
extends RenderLiving<EntitySquid> {
    public RenderSquid(ModelBase modelBase, float f) {
        super(modelBase, f);
    }

    @Override
    public void doRender(EntitySquid entitySquid, double d, double d2, double d3, float f, float f2) {
        super.doRender(entitySquid, d, d2, d3, f, f2);
    }

    @Override
    protected void rotateCorpse(EntitySquid entitySquid, float f, float f2, float f3) {
        float f4 = entitySquid.lastBodyPitch + (entitySquid.bodyPitch - entitySquid.lastBodyPitch) * f3;
        float f5 = entitySquid.lastBodyRoll + (entitySquid.bodyRoll - entitySquid.lastBodyRoll) * f3;
        GL11.glTranslatef(0.0f, 0.5f, 0.0f);
        GL11.glRotatef(180.0f - f2, 0.0f, 1.0f, 0.0f);
        GL11.glRotatef(f4, 1.0f, 0.0f, 0.0f);
        GL11.glRotatef(f5, 0.0f, 1.0f, 0.0f);
        GL11.glTranslatef(0.0f, -1.2f, 0.0f);
    }

    @Override
    protected void preRenderCallback(EntitySquid entitySquid, float f) {
    }

    @Override
    protected float getBob(EntitySquid entitySquid, float f) {
        float f2 = entitySquid.lastTentacleRotation + (entitySquid.tentacleRotation - entitySquid.lastTentacleRotation) * f;
        return f2;
    }
}

