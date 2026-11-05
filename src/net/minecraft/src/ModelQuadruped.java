package net.minecraft.src;


public class ModelQuadruped
extends ModelBase {
    public ModelRenderer head = new ModelRenderer(0, 0);
    public ModelRenderer body;
    public ModelRenderer leg1;
    public ModelRenderer leg2;
    public ModelRenderer leg3;
    public ModelRenderer leg4;

    public ModelQuadruped(int pivotPoint, float reduction) {
        this.head.addBox(-4.0f, -4.0f, -8.0f, 8, 8, 8, reduction);
        this.head.setRotationPoint(0.0f, 18 - pivotPoint, -6.0f);
        this.body = new ModelRenderer(28, 8);
        this.body.addBox(-5.0f, -10.0f, -7.0f, 10, 16, 8, reduction);
        this.body.setRotationPoint(0.0f, 17 - pivotPoint, 2.0f);
        this.leg1 = new ModelRenderer(0, 16);
        this.leg1.addBox(-2.0f, 0.0f, -2.0f, 4, pivotPoint, 4, reduction);
        this.leg1.setRotationPoint(-3.0f, 24 - pivotPoint, 7.0f);
        this.leg2 = new ModelRenderer(0, 16);
        this.leg2.addBox(-2.0f, 0.0f, -2.0f, 4, pivotPoint, 4, reduction);
        this.leg2.setRotationPoint(3.0f, 24 - pivotPoint, 7.0f);
        this.leg3 = new ModelRenderer(0, 16);
        this.leg3.addBox(-2.0f, 0.0f, -2.0f, 4, pivotPoint, 4, reduction);
        this.leg3.setRotationPoint(-3.0f, 24 - pivotPoint, -5.0f);
        this.leg4 = new ModelRenderer(0, 16);
        this.leg4.addBox(-2.0f, 0.0f, -2.0f, 4, pivotPoint, 4, reduction);
        this.leg4.setRotationPoint(3.0f, 24 - pivotPoint, -5.0f);
    }

    public void render(float walkAnimationProgress, float walkAnimationSpeed, float bob, float yaw, float pitch, float scale) {
        this.setRotationAngles(walkAnimationProgress, walkAnimationSpeed, bob, yaw, pitch, scale);
        this.head.render(scale);
        this.body.render(scale);
        this.leg1.render(scale);
        this.leg2.render(scale);
        this.leg3.render(scale);
        this.leg4.render(scale);
    }

    public void setRotationAngles(float walkAnimationProgress, float walkAnimationSpeed, float bob, float yaw, float pitch, float scale) {
        this.head.rotateAngleX = pitch / 57.295776f;
        this.head.rotateAngleY = yaw / 57.295776f;
        this.body.rotateAngleX = 1.5707964f;
        this.leg1.rotateAngleX = MathHelper.cos(walkAnimationProgress * 0.6662f) * 1.4f * walkAnimationSpeed;
        this.leg2.rotateAngleX = MathHelper.cos(walkAnimationProgress * 0.6662f + (float)Math.PI) * 1.4f * walkAnimationSpeed;
        this.leg3.rotateAngleX = MathHelper.cos(walkAnimationProgress * 0.6662f + (float)Math.PI) * 1.4f * walkAnimationSpeed;
        this.leg4.rotateAngleX = MathHelper.cos(walkAnimationProgress * 0.6662f) * 1.4f * walkAnimationSpeed;
    }
}

