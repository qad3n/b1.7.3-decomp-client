package net.minecraft.src;

public class EntitySmokeFX extends EntityFX {
    float initialSize;

    public EntitySmokeFX(World world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
        this(world, x, y, z, velocityX, velocityY, velocityZ, 1.0F);
    }

    public EntitySmokeFX(World world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, float scale) {
        super(world, x, y, z, 0.0, 0.0, 0.0);
        this.motionX *= 0.1F;
        this.motionY *= 0.1F;
        this.motionZ *= 0.1F;
        this.motionX += velocityX;
        this.motionY += velocityY;
        this.motionZ += velocityZ;
        this.particleRed = this.particleGreen = this.particleBlue = (float)(Math.random() * 0.3F);
        this.particleScale *= 0.75F;
        this.particleScale *= scale;
        this.initialSize = this.particleScale;
        this.particleMaxAge = (int)(8.0 / (Math.random() * 0.8 + 0.2));
        this.particleMaxAge = (int)(this.particleMaxAge * scale);
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
        this.motionY += 0.004;
        this.moveEntity(this.motionX, this.motionY, this.motionZ);
        if (this.posY == this.prevPosY) {
            this.motionX *= 1.1;
            this.motionZ *= 1.1;
        }

        this.motionX *= 0.96F;
        this.motionY *= 0.96F;
        this.motionZ *= 0.96F;
        if (this.onGround) {
            this.motionX *= 0.7F;
            this.motionZ *= 0.7F;
        }
    }
}
