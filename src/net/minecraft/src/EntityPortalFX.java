package net.minecraft.src;

public class EntityPortalFX extends EntityFX {
    private float initialSize;
    private double initialX;
    private double initialY;
    private double initialZ;

    public EntityPortalFX(World var1, double var2, double var4, double var6, double var8, double var10, double var12) {
        super(var1, var2, var4, var6, var8, var10, var12);
        this.motionX = var8;
        this.motionY = var10;
        this.motionZ = var12;
        this.initialX = this.posX = var2;
        this.initialY = this.posY = var4;
        this.initialZ = this.posZ = var6;
        float f = this.rand.nextFloat() * 0.6F + 0.4F;
        this.initialSize = this.particleScale = this.rand.nextFloat() * 0.2F + 0.5F;
        this.particleRed = this.particleGreen = this.particleBlue = 1.0F * f;
        this.particleGreen *= 0.3F;
        this.particleRed *= 0.9F;
        this.particleMaxAge = (int)(Math.random() * 10.0) + 40;
        this.noClip = true;
        this.particleTextureIndex = (int)(Math.random() * 8.0);
    }

    @Override
    public void renderParticle(Tessellator tesselator, float tickDelta, float dx, float dy, float dz, float forwards, float sideways) {
        float f = (this.particleAge + tickDelta) / this.particleMaxAge;
        f = 1.0F - f;
        f *= f;
        f = 1.0F - f;
        this.particleScale = this.initialSize * f;
        super.renderParticle(tesselator, tickDelta, dx, dy, dz, forwards, sideways);
    }

    @Override
    public float getEntityBrightness(float tickDelta) {
        float f = super.getEntityBrightness(tickDelta);
        float f1 = (float)this.particleAge / this.particleMaxAge;
        f1 *= f1;
        f1 *= f1;
        return f * (1.0F - f1) + f1;
    }

    @Override
    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        float f = (float)this.particleAge / this.particleMaxAge;
        float f1 = f;
        f = -f + f * f * 2.0F;
        f = 1.0F - f;
        this.posX = this.initialX + this.motionX * f;
        this.posY = this.initialY + this.motionY * f + (1.0F - f1);
        this.posZ = this.initialZ + this.motionZ * f;
        if (this.particleAge++ >= this.particleMaxAge) {
            this.setEntityDead();
        }
    }
}
