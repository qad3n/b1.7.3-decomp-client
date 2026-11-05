package net.minecraft.src;


public class EntityReddustFX
extends EntityFX {
    float initialSize;

    public EntityReddustFX(World world, double x, double y, double z, float r, float g, float b) {
        this(world, x, y, z, 1.0f, r, g, b);
    }

    public EntityReddustFX(World world, double x, double y, double z, float scale, float r, float g, float b) {
        super(world, x, y, z, 0.0, 0.0, 0.0);
        this.motionX *= (double)0.1f;
        this.motionY *= (double)0.1f;
        this.motionZ *= (double)0.1f;
        if (r == 0.0f) {
            r = 1.0f;
        }
        float f = (float)Math.random() * 0.4f + 0.6f;
        this.particleRed = ((float)(Math.random() * (double)0.2f) + 0.8f) * r * f;
        this.particleGreen = ((float)(Math.random() * (double)0.2f) + 0.8f) * g * f;
        this.particleBlue = ((float)(Math.random() * (double)0.2f) + 0.8f) * b * f;
        this.particleScale *= 0.75f;
        this.particleScale *= scale;
        this.initialSize = this.particleScale;
        this.particleMaxAge = (int)(8.0 / (Math.random() * 0.8 + 0.2));
        this.particleMaxAge = (int)((float)this.particleMaxAge * scale);
        this.noClip = false;
    }

    public void renderParticle(Tessellator tesselator, float tickDelta, float dx, float dy, float dz, float forwards, float sideways) {
        float f = ((float)this.particleAge + tickDelta) / (float)this.particleMaxAge * 32.0f;
        if (f < 0.0f) {
            f = 0.0f;
        }
        if (f > 1.0f) {
            f = 1.0f;
        }
        this.particleScale = this.initialSize * f;
        super.renderParticle(tesselator, tickDelta, dx, dy, dz, forwards, sideways);
    }

    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        if (this.particleAge++ >= this.particleMaxAge) {
            this.setEntityDead();
        }
        this.particleTextureIndex = 7 - this.particleAge * 8 / this.particleMaxAge;
        this.moveEntity(this.motionX, this.motionY, this.motionZ);
        if (this.posY == this.prevPosY) {
            this.motionX *= 1.1;
            this.motionZ *= 1.1;
        }
        this.motionX *= (double)0.96f;
        this.motionY *= (double)0.96f;
        this.motionZ *= (double)0.96f;
        if (this.onGround) {
            this.motionX *= (double)0.7f;
            this.motionZ *= (double)0.7f;
        }
    }
}

