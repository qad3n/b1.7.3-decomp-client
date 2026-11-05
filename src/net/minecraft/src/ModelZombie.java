package net.minecraft.src;


public class ModelZombie
extends ModelBiped {
    public void setRotationAngles(float walkAnimationProgress, float walkAnimationSpeed, float bob, float yaw, float pitch, float scale) {
        super.setRotationAngles(walkAnimationProgress, walkAnimationSpeed, bob, yaw, pitch, scale);
        float f = MathHelper.sin(this.onGround * (float)Math.PI);
        float f2 = MathHelper.sin((1.0f - (1.0f - this.onGround) * (1.0f - this.onGround)) * (float)Math.PI);
        this.bipedRightArm.rotateAngleZ = 0.0f;
        this.bipedLeftArm.rotateAngleZ = 0.0f;
        this.bipedRightArm.rotateAngleY = -(0.1f - f * 0.6f);
        this.bipedLeftArm.rotateAngleY = 0.1f - f * 0.6f;
        this.bipedRightArm.rotateAngleX = -1.5707964f;
        this.bipedLeftArm.rotateAngleX = -1.5707964f;
        this.bipedRightArm.rotateAngleX -= f * 1.2f - f2 * 0.4f;
        this.bipedLeftArm.rotateAngleX -= f * 1.2f - f2 * 0.4f;
        this.bipedRightArm.rotateAngleZ += MathHelper.cos(bob * 0.09f) * 0.05f + 0.05f;
        this.bipedLeftArm.rotateAngleZ -= MathHelper.cos(bob * 0.09f) * 0.05f + 0.05f;
        this.bipedRightArm.rotateAngleX += MathHelper.sin(bob * 0.067f) * 0.05f;
        this.bipedLeftArm.rotateAngleX -= MathHelper.sin(bob * 0.067f) * 0.05f;
    }
}

