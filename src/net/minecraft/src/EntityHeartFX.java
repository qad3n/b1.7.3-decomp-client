package net.minecraft.src;


public class EntityHeartFX
extends EntityFX {
    float baseScale;

    public EntityHeartFX(World world, double d, double d2, double d3, double d4, double d5, double d6) {
        this(world, d, d2, d3, d4, d5, d6, 2.0f);
    }

    public EntityHeartFX(World world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, float scaleFactor) {
        super(world, x, y, z, 0.0, 0.0, 0.0);
        this.motionX *= (double)0.01f;
        this.motionY *= (double)0.01f;
        this.motionZ *= (double)0.01f;
        this.motionY += 0.1;
        this.particleScale *= 0.75f;
        this.particleScale *= scaleFactor;
        this.baseScale = this.particleScale;
        this.particleMaxAge = 16;
        this.noClip = false;
        this.particleTextureIndex = 80;
    }

    public void renderParticle(Tessellator tesselator, float tickDelta, float dx, float dy, float dz, float forwards, float sideways) {
        float f = ((float)this.particleAge + tickDelta) / (float)this.particleMaxAge * 32.0f;
        if (f < 0.0f) {
            f = 0.0f;
        }
        if (f > 1.0f) {
            f = 1.0f;
        }
        this.particleScale = this.baseScale * f;
        super.renderParticle(tesselator, tickDelta, dx, dy, dz, forwards, sideways);
    }

    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        if (this.particleAge++ >= this.particleMaxAge) {
            this.setEntityDead();
        }
        this.moveEntity(this.motionX, this.motionY, this.motionZ);
        if (this.posY == this.prevPosY) {
            this.motionX *= 1.1;
            this.motionZ *= 1.1;
        }
        this.motionX *= (double)0.86f;
        this.motionY *= (double)0.86f;
        this.motionZ *= (double)0.86f;
        if (this.onGround) {
            this.motionX *= (double)0.7f;
            this.motionZ *= (double)0.7f;
        }
    }
}

