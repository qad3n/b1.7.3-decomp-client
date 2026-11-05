package net.minecraft.src;

import org.lwjgl.opengl.GL11;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class RenderMinecart
extends Render<EntityMinecart> {
    protected ModelBase modelMinecart;

    public RenderMinecart() {
        this.shadowSize = 0.5f;
        this.modelMinecart = new ModelMinecart();
    }

    @Override
    public void doRender(EntityMinecart entityMinecart, double d, double d2, double d3, float f, float f2) {
        GL11.glPushMatrix();
        double d4 = entityMinecart.lastTickPosX + (entityMinecart.posX - entityMinecart.lastTickPosX) * (double)f2;
        double d5 = entityMinecart.lastTickPosY + (entityMinecart.posY - entityMinecart.lastTickPosY) * (double)f2;
        double d6 = entityMinecart.lastTickPosZ + (entityMinecart.posZ - entityMinecart.lastTickPosZ) * (double)f2;
        double d7 = 0.3f;
        Vec3D vec3D = entityMinecart.snapPositionToRail(d4, d5, d6);
        float f3 = entityMinecart.prevRotationPitch + (entityMinecart.rotationPitch - entityMinecart.prevRotationPitch) * f2;
        if (vec3D != null) {
            Vec3D vec3D2 = entityMinecart.snapPositionToRailWithOffset(d4, d5, d6, d7);
            Vec3D vec3D3 = entityMinecart.snapPositionToRailWithOffset(d4, d5, d6, -d7);
            if (vec3D2 == null) {
                vec3D2 = vec3D;
            }
            if (vec3D3 == null) {
                vec3D3 = vec3D;
            }
            d += vec3D.xCoord - d4;
            d2 += (vec3D2.yCoord + vec3D3.yCoord) / 2.0 - d5;
            d3 += vec3D.zCoord - d6;
            Vec3D vec3D4 = vec3D3.addVector(-vec3D2.xCoord, -vec3D2.yCoord, -vec3D2.zCoord);
            if (vec3D4.lengthVector() != 0.0) {
                vec3D4 = vec3D4.normalize();
                f = (float)(Math.atan2(vec3D4.zCoord, vec3D4.xCoord) * 180.0 / Math.PI);
                f3 = (float)(Math.atan(vec3D4.yCoord) * 73.0);
            }
        }
        GL11.glTranslatef((float)d, (float)d2, (float)d3);
        GL11.glRotatef(180.0f - f, 0.0f, 1.0f, 0.0f);
        GL11.glRotatef(-f3, 0.0f, 0.0f, 1.0f);
        float f4 = (float)entityMinecart.minecartTimeSinceHit - f2;
        float f5 = (float)entityMinecart.minecartCurrentDamage - f2;
        if (f5 < 0.0f) {
            f5 = 0.0f;
        }
        if (f4 > 0.0f) {
            GL11.glRotatef(MathHelper.sin(f4) * f4 * f5 / 10.0f * (float)entityMinecart.minecartRockDirection, 1.0f, 0.0f, 0.0f);
        }
        if (entityMinecart.minecartType != 0) {
            this.loadTexture("/terrain.png");
            float f6 = 0.75f;
            GL11.glScalef(f6, f6, f6);
            GL11.glTranslatef(0.0f, 0.3125f, 0.0f);
            GL11.glRotatef(90.0f, 0.0f, 1.0f, 0.0f);
            if (entityMinecart.minecartType == 1) {
                new RenderBlocks().renderBlockOnInventory(Block.chest, 0, entityMinecart.getEntityBrightness(f2));
            } else if (entityMinecart.minecartType == 2) {
                new RenderBlocks().renderBlockOnInventory(Block.stoneOvenIdle, 0, entityMinecart.getEntityBrightness(f2));
            }
            GL11.glRotatef(-90.0f, 0.0f, 1.0f, 0.0f);
            GL11.glTranslatef(0.0f, -0.3125f, 0.0f);
            GL11.glScalef(1.0f / f6, 1.0f / f6, 1.0f / f6);
        }
        this.loadTexture("/item/cart.png");
        GL11.glScalef(-1.0f, -1.0f, 1.0f);
        this.modelMinecart.render(0.0f, 0.0f, -0.1f, 0.0f, 0.0f, 0.0625f);
        GL11.glPopMatrix();
    }
}

