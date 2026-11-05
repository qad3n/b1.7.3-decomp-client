package net.minecraft.src;


public class ModelCreeper
extends ModelBase {
    public ModelRenderer head;
    public ModelRenderer hat;
    public ModelRenderer body;
    public ModelRenderer leg1;
    public ModelRenderer leg2;
    public ModelRenderer leg3;
    public ModelRenderer leg4;

    public ModelCreeper() {
        this(0.0f);
    }

    public ModelCreeper(float reduction) {
        int n = 4;
        this.head = new ModelRenderer(0, 0);
        this.head.addBox(-4.0f, -8.0f, -4.0f, 8, 8, 8, reduction);
        this.head.setRotationPoint(0.0f, n, 0.0f);
        this.hat = new ModelRenderer(32, 0);
        this.hat.addBox(-4.0f, -8.0f, -4.0f, 8, 8, 8, reduction + 0.5f);
        this.hat.setRotationPoint(0.0f, n, 0.0f);
        this.body = new ModelRenderer(16, 16);
        this.body.addBox(-4.0f, 0.0f, -2.0f, 8, 12, 4, reduction);
        this.body.setRotationPoint(0.0f, n, 0.0f);
        this.leg1 = new ModelRenderer(0, 16);
        this.leg1.addBox(-2.0f, 0.0f, -2.0f, 4, 6, 4, reduction);
        this.leg1.setRotationPoint(-2.0f, 12 + n, 4.0f);
        this.leg2 = new ModelRenderer(0, 16);
        this.leg2.addBox(-2.0f, 0.0f, -2.0f, 4, 6, 4, reduction);
        this.leg2.setRotationPoint(2.0f, 12 + n, 4.0f);
        this.leg3 = new ModelRenderer(0, 16);
        this.leg3.addBox(-2.0f, 0.0f, -2.0f, 4, 6, 4, reduction);
        this.leg3.setRotationPoint(-2.0f, 12 + n, -4.0f);
        this.leg4 = new ModelRenderer(0, 16);
        this.leg4.addBox(-2.0f, 0.0f, -2.0f, 4, 6, 4, reduction);
        this.leg4.setRotationPoint(2.0f, 12 + n, -4.0f);
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
        this.head.rotateAngleY = yaw / 57.295776f;
        this.head.rotateAngleX = pitch / 57.295776f;
        this.leg1.rotateAngleX = MathHelper.cos(walkAnimationProgress * 0.6662f) * 1.4f * walkAnimationSpeed;
        this.leg2.rotateAngleX = MathHelper.cos(walkAnimationProgress * 0.6662f + (float)Math.PI) * 1.4f * walkAnimationSpeed;
        this.leg3.rotateAngleX = MathHelper.cos(walkAnimationProgress * 0.6662f + (float)Math.PI) * 1.4f * walkAnimationSpeed;
        this.leg4.rotateAngleX = MathHelper.cos(walkAnimationProgress * 0.6662f) * 1.4f * walkAnimationSpeed;
    }
}

