package net.minecraft.src;

public class EntitySlimeFX extends EntityFX {
    public EntitySlimeFX(World world, double x, double y, double z, Item item) {
        super(world, x, y, z, 0.0, 0.0, 0.0);
        this.particleTextureIndex = item.getIconFromDamage(0);
        this.particleRed = this.particleGreen = this.particleBlue = 1.0F;
        this.particleGravity = Block.blockSnow.blockParticleGravity;
        this.particleScale /= 2.0F;
    }

    @Override
    public int getFXLayer() {
        return 2;
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
