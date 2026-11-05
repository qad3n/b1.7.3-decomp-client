package net.minecraft.src;

public class EntitySnowShovelFX extends EntityFX {
    float initialSize;

    public EntitySnowShovelFX(World var1, double var2, double var4, double var6, double var8, double var10, double var12) {
        this(var1, var2, var4, var6, var8, var10, var12, 1.0F);
    }

    public EntitySnowShovelFX(World world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, float scaleFactor) {
        super(world, x, y, z, velocityX, velocityY, velocityZ);
        this.motionX *= 0.1F;
        this.motionY *= 0.1F;
        this.motionZ *= 0.1F;
        this.motionX += velocityX;
        this.motionY += velocityY;
        this.motionZ += velocityZ;
        this.particleRed = this.particleGreen = this.particleBlue = 1.0F - (float)(Math.random() * 0.3F);
        this.particleScale *= 0.75F;
        this.particleScale *= scaleFactor;
        this.initialSize = this.particleScale;
        this.particleMaxAge = (int)(8.0 / (Math.random() * 0.8 + 0.2));
        this.particleMaxAge = (int)(this.particleMaxAge * scaleFactor);
        this.noClip = false;
    }

    @Override
    public void renderParticle(Tessellator tesselator, float tickDelta, float dx, float dy, float dz, float forwards, float sideways) {
        float f = (this.particleAge + tickDelta) / this.particleMaxAge * 32.0F;
        if (f < 0.0F) {
            f = 0.0F;
        }

        if (f > 1.0F) {
            f = 1.0F;
        }

        this.particleScale = this.initialSize * f;
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

        this.particleTextureIndex = 7 - this.particleAge * 8 / this.particleMaxAge;
        this.motionY -= 0.03;
        this.moveEntity(this.motionX, this.motionY, this.motionZ);
        this.motionX *= 0.99F;
        this.motionY *= 0.99F;
        this.motionZ *= 0.99F;
        if (this.onGround) {
            this.motionX *= 0.7F;
            this.motionZ *= 0.7F;
        }
    }
}
