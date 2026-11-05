package net.minecraft.src;

public class ModelBiped extends ModelBase {
    public ModelRenderer bipedHead;
    public ModelRenderer bipedHeadwear;
    public ModelRenderer bipedBody;
    public ModelRenderer bipedRightArm;
    public ModelRenderer bipedLeftArm;
    public ModelRenderer bipedRightLeg;
    public ModelRenderer bipedLeftLeg;
    public ModelRenderer bipedEars;
    public ModelRenderer bipedCloak;
    public boolean itemInLeftHand = false;
    public boolean itemInRightHand = false;
    public boolean isSneak = false;

    public ModelBiped() {
        this(0.0F);
    }

    public ModelBiped(float reduction) {
        this(reduction, 0.0F);
    }

    public ModelBiped(float reduction, float pivotPoint) {
        this.bipedCloak = new ModelRenderer(0, 0);
        this.bipedCloak.addBox(-5.0F, 0.0F, -1.0F, 10, 16, 1, reduction);
        this.bipedEars = new ModelRenderer(24, 0);
        this.bipedEars.addBox(-3.0F, -6.0F, -1.0F, 6, 6, 1, reduction);
        this.bipedHead = new ModelRenderer(0, 0);
        this.bipedHead.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, reduction);
        this.bipedHead.setRotationPoint(0.0F, 0.0F + pivotPoint, 0.0F);
        this.bipedHeadwear = new ModelRenderer(32, 0);
        this.bipedHeadwear.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, reduction + 0.5F);
        this.bipedHeadwear.setRotationPoint(0.0F, 0.0F + pivotPoint, 0.0F);
        this.bipedBody = new ModelRenderer(16, 16);
        this.bipedBody.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, reduction);
        this.bipedBody.setRotationPoint(0.0F, 0.0F + pivotPoint, 0.0F);
        this.bipedRightArm = new ModelRenderer(40, 16);
        this.bipedRightArm.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, reduction);
        this.bipedRightArm.setRotationPoint(-5.0F, 2.0F + pivotPoint, 0.0F);
        this.bipedLeftArm = new ModelRenderer(40, 16);
        this.bipedLeftArm.mirror = true;
        this.bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, reduction);
        this.bipedLeftArm.setRotationPoint(5.0F, 2.0F + pivotPoint, 0.0F);
        this.bipedRightLeg = new ModelRenderer(0, 16);
        this.bipedRightLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, reduction);
        this.bipedRightLeg.setRotationPoint(-2.0F, 12.0F + pivotPoint, 0.0F);
        this.bipedLeftLeg = new ModelRenderer(0, 16);
        this.bipedLeftLeg.mirror = true;
        this.bipedLeftLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, reduction);
        this.bipedLeftLeg.setRotationPoint(2.0F, 12.0F + pivotPoint, 0.0F);
    }

    @Override
    public void render(float walkAnimationProgress, float walkAnimationSpeed, float bob, float yaw, float pitch, float scale) {
        this.setRotationAngles(walkAnimationProgress, walkAnimationSpeed, bob, yaw, pitch, scale);
        this.bipedHead.render(scale);
        this.bipedBody.render(scale);
        this.bipedRightArm.render(scale);
        this.bipedLeftArm.render(scale);
        this.bipedRightLeg.render(scale);
        this.bipedLeftLeg.render(scale);
        this.bipedHeadwear.render(scale);
    }

    @Override
    public void setRotationAngles(float walkAnimationProgress, float walkAnimationSpeed, float bob, float yaw, float pitch, float scale) {
        this.bipedHead.rotateAngleY = yaw / (180.0F / (float)Math.PI);
        this.bipedHead.rotateAngleX = pitch / (180.0F / (float)Math.PI);
        this.bipedHeadwear.rotateAngleY = this.bipedHead.rotateAngleY;
        this.bipedHeadwear.rotateAngleX = this.bipedHead.rotateAngleX;
        this.bipedRightArm.rotateAngleX = MathHelper.cos(walkAnimationProgress * 0.6662F + (float) Math.PI) * 2.0F * walkAnimationSpeed * 0.5F;
        this.bipedLeftArm.rotateAngleX = MathHelper.cos(walkAnimationProgress * 0.6662F) * 2.0F * walkAnimationSpeed * 0.5F;
        this.bipedRightArm.rotateAngleZ = 0.0F;
        this.bipedLeftArm.rotateAngleZ = 0.0F;
        this.bipedRightLeg.rotateAngleX = MathHelper.cos(walkAnimationProgress * 0.6662F) * 1.4F * walkAnimationSpeed;
        this.bipedLeftLeg.rotateAngleX = MathHelper.cos(walkAnimationProgress * 0.6662F + (float) Math.PI) * 1.4F * walkAnimationSpeed;
        this.bipedRightLeg.rotateAngleY = 0.0F;
        this.bipedLeftLeg.rotateAngleY = 0.0F;
        if (this.isRiding) {
            this.bipedRightArm.rotateAngleX += (float) (-Math.PI / 5);
            this.bipedLeftArm.rotateAngleX += (float) (-Math.PI / 5);
            this.bipedRightLeg.rotateAngleX = (float) (-Math.PI * 2.0 / 5.0);
            this.bipedLeftLeg.rotateAngleX = (float) (-Math.PI * 2.0 / 5.0);
            this.bipedRightLeg.rotateAngleY = (float) (Math.PI / 10);
            this.bipedLeftLeg.rotateAngleY = (float) (-Math.PI / 10);
        }

        if (this.itemInLeftHand) {
            this.bipedLeftArm.rotateAngleX = this.bipedLeftArm.rotateAngleX * 0.5F - (float) (Math.PI / 10);
        }

        if (this.itemInRightHand) {
            this.bipedRightArm.rotateAngleX = this.bipedRightArm.rotateAngleX * 0.5F - (float) (Math.PI / 10);
        }

        this.bipedRightArm.rotateAngleY = 0.0F;
        this.bipedLeftArm.rotateAngleY = 0.0F;
        if (this.onGround > -9990.0F) {
            float f = this.onGround;
            this.bipedBody.rotateAngleY = MathHelper.sin(MathHelper.sqrt_float(f) * (float) Math.PI * 2.0F) * 0.2F;
            this.bipedRightArm.rotationPointZ = MathHelper.sin(this.bipedBody.rotateAngleY) * 5.0F;
            this.bipedRightArm.rotationPointX = -MathHelper.cos(this.bipedBody.rotateAngleY) * 5.0F;
            this.bipedLeftArm.rotationPointZ = -MathHelper.sin(this.bipedBody.rotateAngleY) * 5.0F;
            this.bipedLeftArm.rotationPointX = MathHelper.cos(this.bipedBody.rotateAngleY) * 5.0F;
            this.bipedRightArm.rotateAngleY = this.bipedRightArm.rotateAngleY + this.bipedBody.rotateAngleY;
            this.bipedLeftArm.rotateAngleY = this.bipedLeftArm.rotateAngleY + this.bipedBody.rotateAngleY;
            this.bipedLeftArm.rotateAngleX = this.bipedLeftArm.rotateAngleX + this.bipedBody.rotateAngleY;
            f = 1.0F - this.onGround;
            f *= f;
            f *= f;
            f = 1.0F - f;
            float f1 = MathHelper.sin(f * (float) Math.PI);
            float f2 = MathHelper.sin(this.onGround * (float) Math.PI) * -(this.bipedHead.rotateAngleX - 0.7F) * 0.75F;
            this.bipedRightArm.rotateAngleX = (float)(this.bipedRightArm.rotateAngleX - (f1 * 1.2 + f2));
            this.bipedRightArm.rotateAngleY = this.bipedRightArm.rotateAngleY + this.bipedBody.rotateAngleY * 2.0F;
            this.bipedRightArm.rotateAngleZ = MathHelper.sin(this.onGround * (float) Math.PI) * -0.4F;
        }

        if (this.isSneak) {
            this.bipedBody.rotateAngleX = 0.5F;
            this.bipedRightLeg.rotateAngleX -= 0.0F;
            this.bipedLeftLeg.rotateAngleX -= 0.0F;
            this.bipedRightArm.rotateAngleX += 0.4F;
            this.bipedLeftArm.rotateAngleX += 0.4F;
            this.bipedRightLeg.rotationPointZ = 4.0F;
            this.bipedLeftLeg.rotationPointZ = 4.0F;
            this.bipedRightLeg.rotationPointY = 9.0F;
            this.bipedLeftLeg.rotationPointY = 9.0F;
            this.bipedHead.rotationPointY = 1.0F;
        } else {
            this.bipedBody.rotateAngleX = 0.0F;
            this.bipedRightLeg.rotationPointZ = 0.0F;
            this.bipedLeftLeg.rotationPointZ = 0.0F;
            this.bipedRightLeg.rotationPointY = 12.0F;
            this.bipedLeftLeg.rotationPointY = 12.0F;
            this.bipedHead.rotationPointY = 0.0F;
        }

        this.bipedRightArm.rotateAngleZ = this.bipedRightArm.rotateAngleZ + (MathHelper.cos(bob * 0.09F) * 0.05F + 0.05F);
        this.bipedLeftArm.rotateAngleZ = this.bipedLeftArm.rotateAngleZ - (MathHelper.cos(bob * 0.09F) * 0.05F + 0.05F);
        this.bipedRightArm.rotateAngleX = this.bipedRightArm.rotateAngleX + MathHelper.sin(bob * 0.067F) * 0.05F;
        this.bipedLeftArm.rotateAngleX = this.bipedLeftArm.rotateAngleX - MathHelper.sin(bob * 0.067F) * 0.05F;
    }

    public void renderEars(float scale) {
        this.bipedEars.rotateAngleY = this.bipedHead.rotateAngleY;
        this.bipedEars.rotateAngleX = this.bipedHead.rotateAngleX;
        this.bipedEars.rotationPointX = 0.0F;
        this.bipedEars.rotationPointY = 0.0F;
        this.bipedEars.render(scale);
    }

    public void renderCloak(float scale) {
        this.bipedCloak.render(scale);
    }
}
