package net.minecraft.src;


public abstract class ModelBase {
    public float onGround;
    public boolean isRiding = false;

    public void render(float walkAnimationProgress, float walkAnimationSpeed, float bob, float yaw, float pitch, float scale) {
    }

    public void setRotationAngles(float walkAnimationProgress, float walkAnimationSpeed, float bob, float yaw, float pitch, float scale) {
    }

    public void setLivingAnimations(EntityLiving mob, float walkAnimationProgress, float walkAnimationSpeed, float tickDelta) {
    }
}

