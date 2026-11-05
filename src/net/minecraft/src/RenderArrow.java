package net.minecraft.src;

import org.lwjgl.opengl.GL11;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class RenderArrow
extends Render<EntityArrow> {
    @Override
    public void doRender(EntityArrow entityArrow, double d, double d2, double d3, float f, float f2) {
        if (entityArrow.prevRotationYaw == 0.0f && entityArrow.prevRotationPitch == 0.0f) {
            return;
        }
        this.loadTexture("/item/arrows.png");
        GL11.glPushMatrix();
        GL11.glTranslatef((float)d, (float)d2, (float)d3);
        GL11.glRotatef(entityArrow.prevRotationYaw + (entityArrow.rotationYaw - entityArrow.prevRotationYaw) * f2 - 90.0f, 0.0f, 1.0f, 0.0f);
        GL11.glRotatef(entityArrow.prevRotationPitch + (entityArrow.rotationPitch - entityArrow.prevRotationPitch) * f2, 0.0f, 0.0f, 1.0f);
        Tessellator tessellator = Tessellator.instance;
        int n = 0;
        float f3 = 0.0f;
        float f4 = 0.5f;
        float f5 = (float)(0 + n * 10) / 32.0f;
        float f6 = (float)(5 + n * 10) / 32.0f;
        float f7 = 0.0f;
        float f8 = 0.15625f;
        float f9 = (float)(5 + n * 10) / 32.0f;
        float f10 = (float)(10 + n * 10) / 32.0f;
        float f11 = 0.05625f;
        GL11.glEnable(32826);
        float f12 = (float)entityArrow.arrowShake - f2;
        if (f12 > 0.0f) {
            float f13 = -MathHelper.sin(f12 * 3.0f) * f12;
            GL11.glRotatef(f13, 0.0f, 0.0f, 1.0f);
        }
        GL11.glRotatef(45.0f, 1.0f, 0.0f, 0.0f);
        GL11.glScalef(f11, f11, f11);
        GL11.glTranslatef(-4.0f, 0.0f, 0.0f);
        GL11.glNormal3f(f11, 0.0f, 0.0f);
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(-7.0, -2.0, -2.0, f7, f9);
        tessellator.addVertexWithUV(-7.0, -2.0, 2.0, f8, f9);
        tessellator.addVertexWithUV(-7.0, 2.0, 2.0, f8, f10);
        tessellator.addVertexWithUV(-7.0, 2.0, -2.0, f7, f10);
        tessellator.draw();
        GL11.glNormal3f(-f11, 0.0f, 0.0f);
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(-7.0, 2.0, -2.0, f7, f9);
        tessellator.addVertexWithUV(-7.0, 2.0, 2.0, f8, f9);
        tessellator.addVertexWithUV(-7.0, -2.0, 2.0, f8, f10);
        tessellator.addVertexWithUV(-7.0, -2.0, -2.0, f7, f10);
        tessellator.draw();
        for (int i = 0; i < 4; ++i) {
            GL11.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
            GL11.glNormal3f(0.0f, 0.0f, f11);
            tessellator.startDrawingQuads();
            tessellator.addVertexWithUV(-8.0, -2.0, 0.0, f3, f5);
            tessellator.addVertexWithUV(8.0, -2.0, 0.0, f4, f5);
            tessellator.addVertexWithUV(8.0, 2.0, 0.0, f4, f6);
            tessellator.addVertexWithUV(-8.0, 2.0, 0.0, f3, f6);
            tessellator.draw();
        }
        GL11.glDisable(32826);
        GL11.glPopMatrix();
    }
}

