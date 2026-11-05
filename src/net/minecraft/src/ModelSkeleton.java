package net.minecraft.src;


public class ModelSkeleton
extends ModelZombie {
    public ModelSkeleton() {
        float f = 0.0f;
        this.bipedRightArm = new ModelRenderer(40, 16);
        this.bipedRightArm.addBox(-1.0f, -2.0f, -1.0f, 2, 12, 2, f);
        this.bipedRightArm.setRotationPoint(-5.0f, 2.0f, 0.0f);
        this.bipedLeftArm = new ModelRenderer(40, 16);
        this.bipedLeftArm.mirror = true;
        this.bipedLeftArm.addBox(-1.0f, -2.0f, -1.0f, 2, 12, 2, f);
        this.bipedLeftArm.setRotationPoint(5.0f, 2.0f, 0.0f);
        this.bipedRightLeg = new ModelRenderer(0, 16);
        this.bipedRightLeg.addBox(-1.0f, 0.0f, -1.0f, 2, 12, 2, f);
        this.bipedRightLeg.setRotationPoint(-2.0f, 12.0f, 0.0f);
        this.bipedLeftLeg = new ModelRenderer(0, 16);
        this.bipedLeftLeg.mirror = true;
        this.bipedLeftLeg.addBox(-1.0f, 0.0f, -1.0f, 2, 12, 2, f);
        this.bipedLeftLeg.setRotationPoint(2.0f, 12.0f, 0.0f);
    }
}

