package net.minecraft.src;


public class ModelCow
extends ModelQuadruped {
    ModelRenderer udders;
    ModelRenderer horn1;
    ModelRenderer horn2;

    public ModelCow() {
        super(12, 0.0f);
        this.head = new ModelRenderer(0, 0);
        this.head.addBox(-4.0f, -4.0f, -6.0f, 8, 8, 6, 0.0f);
        this.head.setRotationPoint(0.0f, 4.0f, -8.0f);
        this.horn1 = new ModelRenderer(22, 0);
        this.horn1.addBox(-4.0f, -5.0f, -4.0f, 1, 3, 1, 0.0f);
        this.horn1.setRotationPoint(0.0f, 3.0f, -7.0f);
        this.horn2 = new ModelRenderer(22, 0);
        this.horn2.addBox(3.0f, -5.0f, -4.0f, 1, 3, 1, 0.0f);
        this.horn2.setRotationPoint(0.0f, 3.0f, -7.0f);
        this.udders = new ModelRenderer(52, 0);
        this.udders.addBox(-2.0f, -3.0f, 0.0f, 4, 6, 2, 0.0f);
        this.udders.setRotationPoint(0.0f, 14.0f, 6.0f);
        this.udders.rotateAngleX = 1.5707964f;
        this.body = new ModelRenderer(18, 4);
        this.body.addBox(-6.0f, -10.0f, -7.0f, 12, 18, 10, 0.0f);
        this.body.setRotationPoint(0.0f, 5.0f, 2.0f);
        this.leg1.rotationPointX -= 1.0f;
        this.leg2.rotationPointX += 1.0f;
        this.leg1.rotationPointZ += 0.0f;
        this.leg2.rotationPointZ += 0.0f;
        this.leg3.rotationPointX -= 1.0f;
        this.leg4.rotationPointX += 1.0f;
        this.leg3.rotationPointZ -= 1.0f;
        this.leg4.rotationPointZ -= 1.0f;
    }

    public void render(float walkAnimationProgress, float walkAnimationSpeed, float bob, float yaw, float pitch, float scale) {
        super.render(walkAnimationProgress, walkAnimationSpeed, bob, yaw, pitch, scale);
        this.horn1.render(scale);
        this.horn2.render(scale);
        this.udders.render(scale);
    }

    public void setRotationAngles(float walkAnimationProgress, float walkAnimationSpeed, float bob, float yaw, float pitch, float scale) {
        super.setRotationAngles(walkAnimationProgress, walkAnimationSpeed, bob, yaw, pitch, scale);
        this.horn1.rotateAngleY = this.head.rotateAngleY;
        this.horn1.rotateAngleX = this.head.rotateAngleX;
        this.horn2.rotateAngleY = this.head.rotateAngleY;
        this.horn2.rotateAngleX = this.head.rotateAngleX;
    }
}

