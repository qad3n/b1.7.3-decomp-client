package net.minecraft.src;

public class EntityLavaFX extends EntityFX {
    private float initialSize;

    public EntityLavaFX(World world, double x, double y, double z) {
        super(world, x, y, z, 0.0, 0.0, 0.0);
        this.motionX *= 0.8F;
        this.motionY *= 0.8F;
        this.motionZ *= 0.8F;
        this.motionY = this.rand.nextFloat() * 0.4F + 0.05F;
        this.particleRed = this.particleGreen = this.particleBlue = 1.0F;
        this.particleScale = this.particleScale * (this.rand.nextFloat() * 2.0F + 0.2F);
        this.initialSize = this.particleScale;
        this.particleMaxAge = (int)(16.0 / (Math.random() * 0.8 + 0.2));
        this.noClip = false;
        this.particleTextureIndex = 49;
    }

    @Override
    public float getEntityBrightness(float tickDelta) {
        return 1.0F;
    }

    @Override
    public void renderParticle(Tessellator tesselator, float tickDelta, float dx, float dy, float dz, float forwards, float sideways) {
        float f = (this.particleAge + tickDelta) / this.particleMaxAge;
        this.particleScale = this.initialSize * (1.0F - f * f);
        super.renderParticle(tesselator, tickDelta, dx, dy, dz, forwards, sideways);
    }

    @Override
    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        if (this.particleAge++ >= this.particleMaxAge) {
            this.setEntityDead();
        }

        float f = (float)this.particleAge / this.particleMaxAge;
        if (this.rand.nextFloat() > f) {
            this.worldObj.spawnParticle("smoke", this.posX, this.posY, this.posZ, this.motionX, this.motionY, this.motionZ);
        }

        this.motionY -= 0.03;
        this.moveEntity(this.motionX, this.motionY, this.motionZ);
        this.motionX *= 0.999F;
        this.motionY *= 0.999F;
        this.motionZ *= 0.999F;
        if (this.onGround) {
            this.motionX *= 0.7F;
            this.motionZ *= 0.7F;
        }
    }
}
