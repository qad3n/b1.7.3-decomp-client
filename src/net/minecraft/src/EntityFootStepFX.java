package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class EntityFootStepFX extends EntityFX {
    private int age = 0;
    private int maxAge = 0;
    private RenderEngine textureManager;

    public EntityFootStepFX(RenderEngine textureManager, World world, double x, double y, double z) {
        super(world, x, y, z, 0.0, 0.0, 0.0);
        this.textureManager = textureManager;
        this.motionX = this.motionY = this.motionZ = 0.0;
        this.maxAge = 200;
    }

    @Override
    public void renderParticle(Tessellator tesselator, float tickDelta, float dx, float dy, float dz, float forwards, float sideways) {
        float f = (this.age + tickDelta) / this.maxAge;
        f *= f;
        float f1 = 2.0F - f * 2.0F;
        if (f1 > 1.0F) {
            f1 = 1.0F;
        }

        f1 *= 0.2F;
        GL11.glDisable(2896);
        float f2 = 0.125F;
        float f3 = (float)(this.posX - interpPosX);
        float f4 = (float)(this.posY - interpPosY);
        float f5 = (float)(this.posZ - interpPosZ);
        float f6 = this.worldObj.getLightBrightness(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ));
        this.textureManager.bindTexture(this.textureManager.getTexture("/misc/footprint.png"));
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        tesselator.startDrawingQuads();
        tesselator.setColorRGBA_F(f6, f6, f6, f1);
        tesselator.addVertexWithUV(f3 - f2, f4, f5 + f2, 0.0, 1.0);
        tesselator.addVertexWithUV(f3 + f2, f4, f5 + f2, 1.0, 1.0);
        tesselator.addVertexWithUV(f3 + f2, f4, f5 - f2, 1.0, 0.0);
        tesselator.addVertexWithUV(f3 - f2, f4, f5 - f2, 0.0, 0.0);
        tesselator.draw();
        GL11.glDisable(3042);
        GL11.glEnable(2896);
    }

    @Override
    public void onUpdate() {
        this.age++;
        if (this.age == this.maxAge) {
            this.setEntityDead();
        }
    }

    @Override
    public int getFXLayer() {
        return 3;
    }
}
