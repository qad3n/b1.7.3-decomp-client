package net.minecraft.src;

public class EntityRainFX extends EntityFX {
    public EntityRainFX(World world, double x, double y, double z) {
        super(world, x, y, z, 0.0, 0.0, 0.0);
        this.motionX *= 0.3F;
        this.motionY = (float)Math.random() * 0.2F + 0.1F;
        this.motionZ *= 0.3F;
        this.particleRed = 1.0F;
        this.particleGreen = 1.0F;
        this.particleBlue = 1.0F;
        this.particleTextureIndex = 19 + this.rand.nextInt(4);
        this.setSize(0.01F, 0.01F);
        this.particleGravity = 0.06F;
        this.particleMaxAge = (int)(8.0 / (Math.random() * 0.8 + 0.2));
    }

    @Override
    public void renderParticle(Tessellator tesselator, float tickDelta, float dx, float dy, float dz, float forwards, float sideways) {
        super.renderParticle(tesselator, tickDelta, dx, dy, dz, forwards, sideways);
    }

    @Override
    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.motionY = this.motionY - this.particleGravity;
        this.moveEntity(this.motionX, this.motionY, this.motionZ);
        this.motionX *= 0.98F;
        this.motionY *= 0.98F;
        this.motionZ *= 0.98F;
        if (this.particleMaxAge-- <= 0) {
            this.setEntityDead();
        }

        if (this.onGround) {
            if (Math.random() < 0.5) {
                this.setEntityDead();
            }

            this.motionX *= 0.7F;
            this.motionZ *= 0.7F;
        }

        Material material = this.worldObj
            .getBlockMaterial(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ));
        if (material.getIsLiquid() || material.isSolid()) {
            double d0 = MathHelper.floor_double(this.posY) + 1
                - BlockFluid.getPercentAir(
                    this.worldObj.getBlockMetadata(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ))
                );
            if (this.posY < d0) {
                this.setEntityDead();
            }
        }
    }
}
