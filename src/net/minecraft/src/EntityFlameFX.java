package net.minecraft.src;

public class EntityFlameFX extends EntityFX {
    private float initialSize;

    public EntityFlameFX(World var1, double var2, double var4, double var6, double var8, double var10, double var12) {
        super(var1, var2, var4, var6, var8, var10, var12);
        this.motionX = this.motionX * 0.01F + var8;
        this.motionY = this.motionY * 0.01F + var10;
        this.motionZ = this.motionZ * 0.01F + var12;
        var2 += (this.rand.nextFloat() - this.rand.nextFloat()) * 0.05F;
        var4 += (this.rand.nextFloat() - this.rand.nextFloat()) * 0.05F;
        var6 += (this.rand.nextFloat() - this.rand.nextFloat()) * 0.05F;
        this.initialSize = this.particleScale;
        this.particleRed = this.particleGreen = this.particleBlue = 1.0F;
        this.particleMaxAge = (int)(8.0 / (Math.random() * 0.8 + 0.2)) + 4;
        this.noClip = true;
        this.particleTextureIndex = 48;
    }

    @Override
    public void renderParticle(Tessellator tesselator, float tickDelta, float dx, float dy, float dz, float forwards, float sideways) {
        float f = (this.particleAge + tickDelta) / this.particleMaxAge;
        this.particleScale = this.initialSize * (1.0F - f * f * 0.5F);
        super.renderParticle(tesselator, tickDelta, dx, dy, dz, forwards, sideways);
    }

    @Override
    public float getEntityBrightness(float tickDelta) {
        float f = (this.particleAge + tickDelta) / this.particleMaxAge;
        if (f < 0.0F) {
            f = 0.0F;
        }

        if (f > 1.0F) {
            f = 1.0F;
        }

        float f1 = super.getEntityBrightness(tickDelta);
        return f1 * f + (1.0F - f);
    }

    @Override
    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        if (this.particleAge++ >= this.particleMaxAge) {
            this.setEntityDead();
        }

        this.moveEntity(this.motionX, this.motionY, this.motionZ);
        this.motionX *= 0.96F;
        this.motionY *= 0.96F;
        this.motionZ *= 0.96F;
        if (this.onGround) {
            this.motionX *= 0.7F;
            this.motionZ *= 0.7F;
        }
    }
}
