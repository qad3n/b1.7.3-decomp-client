package net.minecraft.src;

public class EntityFX extends Entity {
    protected int particleTextureIndex;
    protected float particleTextureJitterX;
    protected float particleTextureJitterY;
    protected int particleAge = 0;
    protected int particleMaxAge = 0;
    protected float particleScale;
    protected float particleGravity;
    protected float particleRed;
    protected float particleGreen;
    protected float particleBlue;
    public static double interpPosX;
    public static double interpPosY;
    public static double interpPosZ;

    public EntityFX(World world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
        super(world);
        this.setSize(0.2F, 0.2F);
        this.yOffset = this.height / 2.0F;
        this.setPosition(x, y, z);
        this.particleRed = this.particleGreen = this.particleBlue = 1.0F;
        this.motionX = velocityX + (float)(Math.random() * 2.0 - 1.0) * 0.4F;
        this.motionY = velocityY + (float)(Math.random() * 2.0 - 1.0) * 0.4F;
        this.motionZ = velocityZ + (float)(Math.random() * 2.0 - 1.0) * 0.4F;
        float f = (float)(Math.random() + Math.random() + 1.0) * 0.15F;
        float f1 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
        this.motionX = this.motionX / f1 * f * 0.4F;
        this.motionY = this.motionY / f1 * f * 0.4F + 0.1F;
        this.motionZ = this.motionZ / f1 * f * 0.4F;
        this.particleTextureJitterX = this.rand.nextFloat() * 3.0F;
        this.particleTextureJitterY = this.rand.nextFloat() * 3.0F;
        this.particleScale = (this.rand.nextFloat() * 0.5F + 0.5F) * 2.0F;
        this.particleMaxAge = (int)(4.0F / (this.rand.nextFloat() * 0.9F + 0.1F));
        this.particleAge = 0;
    }

    public EntityFX multiplyVelocity(float value) {
        this.motionX *= value;
        this.motionY = (this.motionY - 0.1F) * value + 0.1F;
        this.motionZ *= value;
        return this;
    }

    public EntityFX multiplySize(float value) {
        this.setSize(0.2F * value, 0.2F * value);
        this.particleScale *= value;
        return this;
    }

    @Override
    protected boolean canTriggerWalking() {
        return false;
    }

    @Override
    protected void entityInit() {
    }

    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        if (this.particleAge++ >= this.particleMaxAge) {
            this.setEntityDead();
        }
        this.motionY -= 0.04 * (double) this.particleGravity;
        this.moveEntity(this.motionX, this.motionY, this.motionZ);
        this.motionX *= (double) 0.98f;
        this.motionY *= (double) 0.98f;
        this.motionZ *= (double) 0.98f;
        if (this.onGround) {
            this.motionX *= (double) 0.7f;
            this.motionZ *= (double) 0.7f;
        }
    }

    public void renderParticle(Tessellator tesselator, float tickDelta, float dx, float dy, float dz, float forwards, float sideways) {
        float f = this.particleTextureIndex % 16 / 16.0F;
        float f1 = f + 0.0624375F;
        float f2 = this.particleTextureIndex / 16 / 16.0F;
        float f3 = f2 + 0.0624375F;
        float f4 = 0.1F * this.particleScale;
        float f5 = (float)(this.prevPosX + (this.posX - this.prevPosX) * tickDelta - interpPosX);
        float f6 = (float)(this.prevPosY + (this.posY - this.prevPosY) * tickDelta - interpPosY);
        float f7 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * tickDelta - interpPosZ);
        float f8 = this.getEntityBrightness(tickDelta);
        tesselator.setColorOpaque_F(this.particleRed * f8, this.particleGreen * f8, this.particleBlue * f8);
        tesselator.addVertexWithUV(f5 - dx * f4 - forwards * f4, f6 - dy * f4, f7 - dz * f4 - sideways * f4, f1, f3);
        tesselator.addVertexWithUV(f5 - dx * f4 + forwards * f4, f6 + dy * f4, f7 - dz * f4 + sideways * f4, f1, f2);
        tesselator.addVertexWithUV(f5 + dx * f4 + forwards * f4, f6 + dy * f4, f7 + dz * f4 + sideways * f4, f, f2);
        tesselator.addVertexWithUV(f5 + dx * f4 - forwards * f4, f6 - dy * f4, f7 + dz * f4 - sideways * f4, f, f3);
    }

    public int getFXLayer() {
        return 0;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
    }
}
