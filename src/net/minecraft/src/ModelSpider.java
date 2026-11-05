package net.minecraft.src;


public class ModelSpider
extends ModelBase {
    public ModelRenderer spiderHead;
    public ModelRenderer spiderNeck;
    public ModelRenderer spiderBody;
    public ModelRenderer spiderLeg1;
    public ModelRenderer spiderLeg2;
    public ModelRenderer spiderLeg3;
    public ModelRenderer spiderLeg4;
    public ModelRenderer spiderLeg5;
    public ModelRenderer spiderLeg6;
    public ModelRenderer spiderLeg7;
    public ModelRenderer spiderLeg8;

    public ModelSpider() {
        float f = 0.0f;
        int n = 15;
        this.spiderHead = new ModelRenderer(32, 4);
        this.spiderHead.addBox(-4.0f, -4.0f, -8.0f, 8, 8, 8, f);
        this.spiderHead.setRotationPoint(0.0f, 0 + n, -3.0f);
        this.spiderNeck = new ModelRenderer(0, 0);
        this.spiderNeck.addBox(-3.0f, -3.0f, -3.0f, 6, 6, 6, f);
        this.spiderNeck.setRotationPoint(0.0f, n, 0.0f);
        this.spiderBody = new ModelRenderer(0, 12);
        this.spiderBody.addBox(-5.0f, -4.0f, -6.0f, 10, 8, 12, f);
        this.spiderBody.setRotationPoint(0.0f, 0 + n, 9.0f);
        this.spiderLeg1 = new ModelRenderer(18, 0);
        this.spiderLeg1.addBox(-15.0f, -1.0f, -1.0f, 16, 2, 2, f);
        this.spiderLeg1.setRotationPoint(-4.0f, 0 + n, 2.0f);
        this.spiderLeg2 = new ModelRenderer(18, 0);
        this.spiderLeg2.addBox(-1.0f, -1.0f, -1.0f, 16, 2, 2, f);
        this.spiderLeg2.setRotationPoint(4.0f, 0 + n, 2.0f);
        this.spiderLeg3 = new ModelRenderer(18, 0);
        this.spiderLeg3.addBox(-15.0f, -1.0f, -1.0f, 16, 2, 2, f);
        this.spiderLeg3.setRotationPoint(-4.0f, 0 + n, 1.0f);
        this.spiderLeg4 = new ModelRenderer(18, 0);
        this.spiderLeg4.addBox(-1.0f, -1.0f, -1.0f, 16, 2, 2, f);
        this.spiderLeg4.setRotationPoint(4.0f, 0 + n, 1.0f);
        this.spiderLeg5 = new ModelRenderer(18, 0);
        this.spiderLeg5.addBox(-15.0f, -1.0f, -1.0f, 16, 2, 2, f);
        this.spiderLeg5.setRotationPoint(-4.0f, 0 + n, 0.0f);
        this.spiderLeg6 = new ModelRenderer(18, 0);
        this.spiderLeg6.addBox(-1.0f, -1.0f, -1.0f, 16, 2, 2, f);
        this.spiderLeg6.setRotationPoint(4.0f, 0 + n, 0.0f);
        this.spiderLeg7 = new ModelRenderer(18, 0);
        this.spiderLeg7.addBox(-15.0f, -1.0f, -1.0f, 16, 2, 2, f);
        this.spiderLeg7.setRotationPoint(-4.0f, 0 + n, -1.0f);
        this.spiderLeg8 = new ModelRenderer(18, 0);
        this.spiderLeg8.addBox(-1.0f, -1.0f, -1.0f, 16, 2, 2, f);
        this.spiderLeg8.setRotationPoint(4.0f, 0 + n, -1.0f);
    }

    public void render(float walkAnimationProgress, float walkAnimationSpeed, float bob, float yaw, float pitch, float scale) {
        this.setRotationAngles(walkAnimationProgress, walkAnimationSpeed, bob, yaw, pitch, scale);
        this.spiderHead.render(scale);
        this.spiderNeck.render(scale);
        this.spiderBody.render(scale);
        this.spiderLeg1.render(scale);
        this.spiderLeg2.render(scale);
        this.spiderLeg3.render(scale);
        this.spiderLeg4.render(scale);
        this.spiderLeg5.render(scale);
        this.spiderLeg6.render(scale);
        this.spiderLeg7.render(scale);
        this.spiderLeg8.render(scale);
    }

    public void setRotationAngles(float walkAnimationProgress, float walkAnimationSpeed, float bob, float yaw, float pitch, float scale) {
        this.spiderHead.rotateAngleY = yaw / 57.295776f;
        this.spiderHead.rotateAngleX = pitch / 57.295776f;
        float f = 0.7853982f;
        this.spiderLeg1.rotateAngleZ = -f;
        this.spiderLeg2.rotateAngleZ = f;
        this.spiderLeg3.rotateAngleZ = -f * 0.74f;
        this.spiderLeg4.rotateAngleZ = f * 0.74f;
        this.spiderLeg5.rotateAngleZ = -f * 0.74f;
        this.spiderLeg6.rotateAngleZ = f * 0.74f;
        this.spiderLeg7.rotateAngleZ = -f;
        this.spiderLeg8.rotateAngleZ = f;
        float f2 = -0.0f;
        float f3 = 0.3926991f;
        this.spiderLeg1.rotateAngleY = f3 * 2.0f + f2;
        this.spiderLeg2.rotateAngleY = -f3 * 2.0f - f2;
        this.spiderLeg3.rotateAngleY = f3 * 1.0f + f2;
        this.spiderLeg4.rotateAngleY = -f3 * 1.0f - f2;
        this.spiderLeg5.rotateAngleY = -f3 * 1.0f + f2;
        this.spiderLeg6.rotateAngleY = f3 * 1.0f - f2;
        this.spiderLeg7.rotateAngleY = -f3 * 2.0f + f2;
        this.spiderLeg8.rotateAngleY = f3 * 2.0f - f2;
        float f4 = -(MathHelper.cos(walkAnimationProgress * 0.6662f * 2.0f + 0.0f) * 0.4f) * walkAnimationSpeed;
        float f5 = -(MathHelper.cos(walkAnimationProgress * 0.6662f * 2.0f + (float)Math.PI) * 0.4f) * walkAnimationSpeed;
        float f6 = -(MathHelper.cos(walkAnimationProgress * 0.6662f * 2.0f + 1.5707964f) * 0.4f) * walkAnimationSpeed;
        float f7 = -(MathHelper.cos(walkAnimationProgress * 0.6662f * 2.0f + 4.712389f) * 0.4f) * walkAnimationSpeed;
        float f8 = Math.abs(MathHelper.sin(walkAnimationProgress * 0.6662f + 0.0f) * 0.4f) * walkAnimationSpeed;
        float f9 = Math.abs(MathHelper.sin(walkAnimationProgress * 0.6662f + (float)Math.PI) * 0.4f) * walkAnimationSpeed;
        float f10 = Math.abs(MathHelper.sin(walkAnimationProgress * 0.6662f + 1.5707964f) * 0.4f) * walkAnimationSpeed;
        float f11 = Math.abs(MathHelper.sin(walkAnimationProgress * 0.6662f + 4.712389f) * 0.4f) * walkAnimationSpeed;
        this.spiderLeg1.rotateAngleY += f4;
        this.spiderLeg2.rotateAngleY += -f4;
        this.spiderLeg3.rotateAngleY += f5;
        this.spiderLeg4.rotateAngleY += -f5;
        this.spiderLeg5.rotateAngleY += f6;
        this.spiderLeg6.rotateAngleY += -f6;
        this.spiderLeg7.rotateAngleY += f7;
        this.spiderLeg8.rotateAngleY += -f7;
        this.spiderLeg1.rotateAngleZ += f8;
        this.spiderLeg2.rotateAngleZ += -f8;
        this.spiderLeg3.rotateAngleZ += f9;
        this.spiderLeg4.rotateAngleZ += -f9;
        this.spiderLeg5.rotateAngleZ += f10;
        this.spiderLeg6.rotateAngleZ += -f10;
        this.spiderLeg7.rotateAngleZ += f11;
        this.spiderLeg8.rotateAngleZ += -f11;
    }
}

