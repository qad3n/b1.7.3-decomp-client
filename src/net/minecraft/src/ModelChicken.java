package net.minecraft.src;


public class ModelChicken
extends ModelBase {
    public ModelRenderer head;
    public ModelRenderer body;
    public ModelRenderer rightLeg;
    public ModelRenderer leftLeg;
    public ModelRenderer rightWing;
    public ModelRenderer leftWing;
    public ModelRenderer bill;
    public ModelRenderer chin;

    public ModelChicken() {
        int n = 16;
        this.head = new ModelRenderer(0, 0);
        this.head.addBox(-2.0f, -6.0f, -2.0f, 4, 6, 3, 0.0f);
        this.head.setRotationPoint(0.0f, -1 + n, -4.0f);
        this.bill = new ModelRenderer(14, 0);
        this.bill.addBox(-2.0f, -4.0f, -4.0f, 4, 2, 2, 0.0f);
        this.bill.setRotationPoint(0.0f, -1 + n, -4.0f);
        this.chin = new ModelRenderer(14, 4);
        this.chin.addBox(-1.0f, -2.0f, -3.0f, 2, 2, 2, 0.0f);
        this.chin.setRotationPoint(0.0f, -1 + n, -4.0f);
        this.body = new ModelRenderer(0, 9);
        this.body.addBox(-3.0f, -4.0f, -3.0f, 6, 8, 6, 0.0f);
        this.body.setRotationPoint(0.0f, 0 + n, 0.0f);
        this.rightLeg = new ModelRenderer(26, 0);
        this.rightLeg.addBox(-1.0f, 0.0f, -3.0f, 3, 5, 3);
        this.rightLeg.setRotationPoint(-2.0f, 3 + n, 1.0f);
        this.leftLeg = new ModelRenderer(26, 0);
        this.leftLeg.addBox(-1.0f, 0.0f, -3.0f, 3, 5, 3);
        this.leftLeg.setRotationPoint(1.0f, 3 + n, 1.0f);
        this.rightWing = new ModelRenderer(24, 13);
        this.rightWing.addBox(0.0f, 0.0f, -3.0f, 1, 4, 6);
        this.rightWing.setRotationPoint(-4.0f, -3 + n, 0.0f);
        this.leftWing = new ModelRenderer(24, 13);
        this.leftWing.addBox(-1.0f, 0.0f, -3.0f, 1, 4, 6);
        this.leftWing.setRotationPoint(4.0f, -3 + n, 0.0f);
    }

    public void render(float walkAnimationProgress, float walkAnimationSpeed, float bob, float yaw, float pitch, float scale) {
        this.setRotationAngles(walkAnimationProgress, walkAnimationSpeed, bob, yaw, pitch, scale);
        this.head.render(scale);
        this.bill.render(scale);
        this.chin.render(scale);
        this.body.render(scale);
        this.rightLeg.render(scale);
        this.leftLeg.render(scale);
        this.rightWing.render(scale);
        this.leftWing.render(scale);
    }

    public void setRotationAngles(float walkAnimationProgress, float walkAnimationSpeed, float bob, float yaw, float pitch, float scale) {
        this.head.rotateAngleX = -(pitch / 57.295776f);
        this.head.rotateAngleY = yaw / 57.295776f;
        this.bill.rotateAngleX = this.head.rotateAngleX;
        this.bill.rotateAngleY = this.head.rotateAngleY;
        this.chin.rotateAngleX = this.head.rotateAngleX;
        this.chin.rotateAngleY = this.head.rotateAngleY;
        this.body.rotateAngleX = 1.5707964f;
        this.rightLeg.rotateAngleX = MathHelper.cos(walkAnimationProgress * 0.6662f) * 1.4f * walkAnimationSpeed;
        this.leftLeg.rotateAngleX = MathHelper.cos(walkAnimationProgress * 0.6662f + (float)Math.PI) * 1.4f * walkAnimationSpeed;
        this.rightWing.rotateAngleZ = bob;
        this.leftWing.rotateAngleZ = -bob;
    }
}

