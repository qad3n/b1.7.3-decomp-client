package net.minecraft.src;


public class ModelBoat
extends ModelBase {
    public ModelRenderer[] boatSides = new ModelRenderer[5];

    public ModelBoat() {
        this.boatSides[0] = new ModelRenderer(0, 8);
        this.boatSides[1] = new ModelRenderer(0, 0);
        this.boatSides[2] = new ModelRenderer(0, 0);
        this.boatSides[3] = new ModelRenderer(0, 0);
        this.boatSides[4] = new ModelRenderer(0, 0);
        int n = 24;
        int n2 = 6;
        int n3 = 20;
        int n4 = 4;
        this.boatSides[0].addBox(-n / 2, -n3 / 2 + 2, -3.0f, n, n3 - 4, 4, 0.0f);
        this.boatSides[0].setRotationPoint(0.0f, 0 + n4, 0.0f);
        this.boatSides[1].addBox(-n / 2 + 2, -n2 - 1, -1.0f, n - 4, n2, 2, 0.0f);
        this.boatSides[1].setRotationPoint(-n / 2 + 1, 0 + n4, 0.0f);
        this.boatSides[2].addBox(-n / 2 + 2, -n2 - 1, -1.0f, n - 4, n2, 2, 0.0f);
        this.boatSides[2].setRotationPoint(n / 2 - 1, 0 + n4, 0.0f);
        this.boatSides[3].addBox(-n / 2 + 2, -n2 - 1, -1.0f, n - 4, n2, 2, 0.0f);
        this.boatSides[3].setRotationPoint(0.0f, 0 + n4, -n3 / 2 + 1);
        this.boatSides[4].addBox(-n / 2 + 2, -n2 - 1, -1.0f, n - 4, n2, 2, 0.0f);
        this.boatSides[4].setRotationPoint(0.0f, 0 + n4, n3 / 2 - 1);
        this.boatSides[0].rotateAngleX = 1.5707964f;
        this.boatSides[1].rotateAngleY = 4.712389f;
        this.boatSides[2].rotateAngleY = 1.5707964f;
        this.boatSides[3].rotateAngleY = (float)Math.PI;
    }

    public void render(float walkAnimationProgress, float walkAnimationSpeed, float bob, float yaw, float pitch, float scale) {
        for (int i = 0; i < 5; ++i) {
            this.boatSides[i].render(scale);
        }
    }

    public void setRotationAngles(float walkAnimationProgress, float walkAnimationSpeed, float bob, float yaw, float pitch, float scale) {
    }
}

