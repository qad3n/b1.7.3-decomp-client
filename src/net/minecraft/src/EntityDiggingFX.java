package net.minecraft.src;

public class EntityDiggingFX extends EntityFX {
    private Block block;
    private int face = 0;

    public EntityDiggingFX(World world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, Block block, int face, int metadata) {
        super(world, x, y, z, velocityX, velocityY, velocityZ);
        this.block = block;
        this.particleTextureIndex = block.getBlockTextureFromSideAndMetadata(0, metadata);
        this.particleGravity = block.blockParticleGravity;
        this.particleRed = this.particleGreen = this.particleBlue = 0.6F;
        this.particleScale /= 2.0F;
        this.face = face;
    }

    public EntityDiggingFX init(int dx, int dy, int dz) {
        if (this.block == Block.grass) {
            return this;
        }

        int i = this.block.colorMultiplier(this.worldObj, dx, dy, dz);
        this.particleRed *= (i >> 16 & 0xFF) / 255.0F;
        this.particleGreen *= (i >> 8 & 0xFF) / 255.0F;
        this.particleBlue *= (i & 0xFF) / 255.0F;
        return this;
    }

    @Override
    public int getFXLayer() {
        return 1;
    }

    @Override
    public void renderParticle(Tessellator tesselator, float tickDelta, float dx, float dy, float dz, float forwards, float sideways) {
        float f = (this.particleTextureIndex % 16 + this.particleTextureJitterX / 4.0F) / 16.0F;
        float f1 = f + 0.015609375F;
        float f2 = (this.particleTextureIndex / 16 + this.particleTextureJitterY / 4.0F) / 16.0F;
        float f3 = f2 + 0.015609375F;
        float f4 = 0.1F * this.particleScale;
        float f5 = (float)(this.prevPosX + (this.posX - this.prevPosX) * tickDelta - interpPosX);
        float f6 = (float)(this.prevPosY + (this.posY - this.prevPosY) * tickDelta - interpPosY);
        float f7 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * tickDelta - interpPosZ);
        float f8 = this.getEntityBrightness(tickDelta);
        tesselator.setColorOpaque_F(f8 * this.particleRed, f8 * this.particleGreen, f8 * this.particleBlue);
        tesselator.addVertexWithUV(f5 - dx * f4 - forwards * f4, f6 - dy * f4, f7 - dz * f4 - sideways * f4, f, f3);
        tesselator.addVertexWithUV(f5 - dx * f4 + forwards * f4, f6 + dy * f4, f7 - dz * f4 + sideways * f4, f, f2);
        tesselator.addVertexWithUV(f5 + dx * f4 + forwards * f4, f6 + dy * f4, f7 + dz * f4 + sideways * f4, f1, f2);
        tesselator.addVertexWithUV(f5 + dx * f4 - forwards * f4, f6 - dy * f4, f7 + dz * f4 - sideways * f4, f1, f3);
    }
}
