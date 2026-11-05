package net.minecraft.src;


public class ModelSlime
extends ModelBase {
    ModelRenderer slimeBodies;
    ModelRenderer slimeRightEye;
    ModelRenderer slimeLeftEye;
    ModelRenderer slimeMouth;

    public ModelSlime(int v) {
        this.slimeBodies = new ModelRenderer(0, v);
        this.slimeBodies.addBox(-4.0f, 16.0f, -4.0f, 8, 8, 8);
        if (v > 0) {
            this.slimeBodies = new ModelRenderer(0, v);
            this.slimeBodies.addBox(-3.0f, 17.0f, -3.0f, 6, 6, 6);
            this.slimeRightEye = new ModelRenderer(32, 0);
            this.slimeRightEye.addBox(-3.25f, 18.0f, -3.5f, 2, 2, 2);
            this.slimeLeftEye = new ModelRenderer(32, 4);
            this.slimeLeftEye.addBox(1.25f, 18.0f, -3.5f, 2, 2, 2);
            this.slimeMouth = new ModelRenderer(32, 8);
            this.slimeMouth.addBox(0.0f, 21.0f, -3.5f, 1, 1, 1);
        }
    }

    public void setRotationAngles(float walkAnimationProgress, float walkAnimationSpeed, float bob, float yaw, float pitch, float scale) {
    }

    public void render(float walkAnimationProgress, float walkAnimationSpeed, float bob, float yaw, float pitch, float scale) {
        this.setRotationAngles(walkAnimationProgress, walkAnimationSpeed, bob, yaw, pitch, scale);
        this.slimeBodies.render(scale);
        if (this.slimeRightEye != null) {
            this.slimeRightEye.render(scale);
            this.slimeLeftEye.render(scale);
            this.slimeMouth.render(scale);
        }
    }
}

