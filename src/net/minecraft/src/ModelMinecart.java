package net.minecraft.src;


public class ModelMinecart
extends ModelBase {
    public ModelRenderer[] sideModels = new ModelRenderer[7];

    public ModelMinecart() {
        this.sideModels[0] = new ModelRenderer(0, 10);
        this.sideModels[1] = new ModelRenderer(0, 0);
        this.sideModels[2] = new ModelRenderer(0, 0);
        this.sideModels[3] = new ModelRenderer(0, 0);
        this.sideModels[4] = new ModelRenderer(0, 0);
        this.sideModels[5] = new ModelRenderer(44, 10);
        int n = 20;
        int n2 = 8;
        int n3 = 16;
        int n4 = 4;
        this.sideModels[0].addBox(-n / 2, -n3 / 2, -1.0f, n, n3, 2, 0.0f);
        this.sideModels[0].setRotationPoint(0.0f, 0 + n4, 0.0f);
        this.sideModels[5].addBox(-n / 2 + 1, -n3 / 2 + 1, -1.0f, n - 2, n3 - 2, 1, 0.0f);
        this.sideModels[5].setRotationPoint(0.0f, 0 + n4, 0.0f);
        this.sideModels[1].addBox(-n / 2 + 2, -n2 - 1, -1.0f, n - 4, n2, 2, 0.0f);
        this.sideModels[1].setRotationPoint(-n / 2 + 1, 0 + n4, 0.0f);
        this.sideModels[2].addBox(-n / 2 + 2, -n2 - 1, -1.0f, n - 4, n2, 2, 0.0f);
        this.sideModels[2].setRotationPoint(n / 2 - 1, 0 + n4, 0.0f);
        this.sideModels[3].addBox(-n / 2 + 2, -n2 - 1, -1.0f, n - 4, n2, 2, 0.0f);
        this.sideModels[3].setRotationPoint(0.0f, 0 + n4, -n3 / 2 + 1);
        this.sideModels[4].addBox(-n / 2 + 2, -n2 - 1, -1.0f, n - 4, n2, 2, 0.0f);
        this.sideModels[4].setRotationPoint(0.0f, 0 + n4, n3 / 2 - 1);
        this.sideModels[0].rotateAngleX = 1.5707964f;
        this.sideModels[1].rotateAngleY = 4.712389f;
        this.sideModels[2].rotateAngleY = 1.5707964f;
        this.sideModels[3].rotateAngleY = (float)Math.PI;
        this.sideModels[5].rotateAngleX = -1.5707964f;
    }

    public void render(float walkAnimationProgress, float walkAnimationSpeed, float bob, float yaw, float pitch, float scale) {
        this.sideModels[5].rotationPointY = 4.0f - bob;
        for (int i = 0; i < 6; ++i) {
            this.sideModels[i].render(scale);
        }
    }

    public void setRotationAngles(float walkAnimationProgress, float walkAnimationSpeed, float bob, float yaw, float pitch, float scale) {
    }
}

