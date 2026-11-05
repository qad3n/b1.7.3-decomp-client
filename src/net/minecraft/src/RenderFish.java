package net.minecraft.src;

import org.lwjgl.opengl.GL11;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class RenderFish
extends Render<EntityFish> {
    @Override
    public void doRender(EntityFish entityFish, double d, double d2, double d3, float f, float f2) {
        GL11.glPushMatrix();
        GL11.glTranslatef((float)d, (float)d2, (float)d3);
        GL11.glEnable(32826);
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        int n = 1;
        int n2 = 2;
        this.loadTexture("/particles.png");
        Tessellator tessellator = Tessellator.instance;
        float f3 = (float)(n * 8 + 0) / 128.0f;
        float f4 = (float)(n * 8 + 8) / 128.0f;
        float f5 = (float)(n2 * 8 + 0) / 128.0f;
        float f6 = (float)(n2 * 8 + 8) / 128.0f;
        float f7 = 1.0f;
        float f8 = 0.5f;
        float f9 = 0.5f;
        GL11.glRotatef(180.0f - this.renderManager.playerViewY, 0.0f, 1.0f, 0.0f);
        GL11.glRotatef(-this.renderManager.playerViewX, 1.0f, 0.0f, 0.0f);
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0f, 1.0f, 0.0f);
        tessellator.addVertexWithUV(0.0f - f8, 0.0f - f9, 0.0, f3, f6);
        tessellator.addVertexWithUV(f7 - f8, 0.0f - f9, 0.0, f4, f6);
        tessellator.addVertexWithUV(f7 - f8, 1.0f - f9, 0.0, f4, f5);
        tessellator.addVertexWithUV(0.0f - f8, 1.0f - f9, 0.0, f3, f5);
        tessellator.draw();
        GL11.glDisable(32826);
        GL11.glPopMatrix();
        if (entityFish.angler != null) {
            float f10 = (entityFish.angler.prevRotationYaw + (entityFish.angler.rotationYaw - entityFish.angler.prevRotationYaw) * f2) * (float)Math.PI / 180.0f;
            double d4 = MathHelper.sin(f10);
            double d5 = MathHelper.cos(f10);
            float f11 = entityFish.angler.getSwingProgress(f2);
            float f12 = MathHelper.sin(MathHelper.sqrt_float(f11) * (float)Math.PI);
            Vec3D vec3D = Vec3D.createVector(-0.5, 0.03, 0.8);
            vec3D.rotateAroundX(-(entityFish.angler.prevRotationPitch + (entityFish.angler.rotationPitch - entityFish.angler.prevRotationPitch) * f2) * (float)Math.PI / 180.0f);
            vec3D.rotateAroundY(-(entityFish.angler.prevRotationYaw + (entityFish.angler.rotationYaw - entityFish.angler.prevRotationYaw) * f2) * (float)Math.PI / 180.0f);
            vec3D.rotateAroundY(f12 * 0.5f);
            vec3D.rotateAroundX(-f12 * 0.7f);
            double d6 = entityFish.angler.prevPosX + (entityFish.angler.posX - entityFish.angler.prevPosX) * (double)f2 + vec3D.xCoord;
            double d7 = entityFish.angler.prevPosY + (entityFish.angler.posY - entityFish.angler.prevPosY) * (double)f2 + vec3D.yCoord;
            double d8 = entityFish.angler.prevPosZ + (entityFish.angler.posZ - entityFish.angler.prevPosZ) * (double)f2 + vec3D.zCoord;
            if (this.renderManager.options.thirdPersonView) {
                f10 = (entityFish.angler.prevRenderYawOffset + (entityFish.angler.renderYawOffset - entityFish.angler.prevRenderYawOffset) * f2) * (float)Math.PI / 180.0f;
                d4 = MathHelper.sin(f10);
                d5 = MathHelper.cos(f10);
                d6 = entityFish.angler.prevPosX + (entityFish.angler.posX - entityFish.angler.prevPosX) * (double)f2 - d5 * 0.35 - d4 * 0.85;
                d7 = entityFish.angler.prevPosY + (entityFish.angler.posY - entityFish.angler.prevPosY) * (double)f2 - 0.45;
                d8 = entityFish.angler.prevPosZ + (entityFish.angler.posZ - entityFish.angler.prevPosZ) * (double)f2 - d4 * 0.35 + d5 * 0.85;
            }
            double d9 = entityFish.prevPosX + (entityFish.posX - entityFish.prevPosX) * (double)f2;
            double d10 = entityFish.prevPosY + (entityFish.posY - entityFish.prevPosY) * (double)f2 + 0.25;
            double d11 = entityFish.prevPosZ + (entityFish.posZ - entityFish.prevPosZ) * (double)f2;
            double d12 = (float)(d6 - d9);
            double d13 = (float)(d7 - d10);
            double d14 = (float)(d8 - d11);
            GL11.glDisable(3553);
            GL11.glDisable(2896);
            tessellator.startDrawing(3);
            tessellator.setColorOpaque_I(0);
            int n3 = 16;
            for (int i = 0; i <= n3; ++i) {
                float f13 = (float)i / (float)n3;
                tessellator.addVertex(d + d12 * (double)f13, d2 + d13 * (double)(f13 * f13 + f13) * 0.5 + 0.25, d3 + d14 * (double)f13);
            }
            tessellator.draw();
            GL11.glEnable(2896);
            GL11.glEnable(3553);
        }
    }
}

