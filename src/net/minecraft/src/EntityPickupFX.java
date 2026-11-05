package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class EntityPickupFX extends EntityFX {
    private Entity entity;
    private Entity collector;
    private int age = 0;
    private int lifetime = 0;
    private float offsetY;

    public EntityPickupFX(World world, Entity entity, Entity collector, float offsetY) {
        super(world, entity.posX, entity.posY, entity.posZ, entity.motionX, entity.motionY, entity.motionZ);
        this.entity = entity;
        this.collector = collector;
        this.lifetime = 3;
        this.offsetY = offsetY;
    }

    @Override
    public void renderParticle(Tessellator tesselator, float tickDelta, float dx, float dy, float dz, float forwards, float sideways) {
        float f = (this.age + tickDelta) / this.lifetime;
        f *= f;
        double d0 = this.entity.posX;
        double d1 = this.entity.posY;
        double d2 = this.entity.posZ;
        double d3 = this.collector.lastTickPosX + (this.collector.posX - this.collector.lastTickPosX) * tickDelta;
        double d4 = this.collector.lastTickPosY + (this.collector.posY - this.collector.lastTickPosY) * tickDelta + this.offsetY;
        double d5 = this.collector.lastTickPosZ + (this.collector.posZ - this.collector.lastTickPosZ) * tickDelta;
        double d6 = d0 + (d3 - d0) * f;
        double d7 = d1 + (d4 - d1) * f;
        double d8 = d2 + (d5 - d2) * f;
        int i = MathHelper.floor_double(d6);
        int j = MathHelper.floor_double(d7 + this.yOffset / 2.0F);
        int k = MathHelper.floor_double(d8);
        float f1 = this.worldObj.getLightBrightness(i, j, k);
        d6 -= interpPosX;
        d7 -= interpPosY;
        d8 -= interpPosZ;
        GL11.glColor4f(f1, f1, f1, 1.0F);
        RenderManager.instance.renderEntityWithPosYaw(this.entity, (float)d6, (float)d7, (float)d8, this.entity.rotationYaw, tickDelta);
    }

    @Override
    public void onUpdate() {
        this.age++;
        if (this.age == this.lifetime) {
            this.setEntityDead();
        }
    }

    @Override
    public int getFXLayer() {
        return 3;
    }
}
