package net.minecraft.src;


public class ModelSquid
extends ModelBase {
    ModelRenderer squidBody;
    ModelRenderer[] squidTentacles = new ModelRenderer[8];

    public ModelSquid() {
        int n = -16;
        this.squidBody = new ModelRenderer(0, 0);
        this.squidBody.addBox(-6.0f, -8.0f, -6.0f, 12, 16, 12);
        this.squidBody.rotationPointY += (float)(24 + n);
        for (int i = 0; i < this.squidTentacles.length; ++i) {
            this.squidTentacles[i] = new ModelRenderer(48, 0);
            double d = (double)i * Math.PI * 2.0 / (double)this.squidTentacles.length;
            float f = (float)Math.cos(d) * 5.0f;
            float f2 = (float)Math.sin(d) * 5.0f;
            this.squidTentacles[i].addBox(-1.0f, 0.0f, -1.0f, 2, 18, 2);
            this.squidTentacles[i].rotationPointX = f;
            this.squidTentacles[i].rotationPointZ = f2;
            this.squidTentacles[i].rotationPointY = 31 + n;
            d = (double)i * Math.PI * -2.0 / (double)this.squidTentacles.length + 1.5707963267948966;
            this.squidTentacles[i].rotateAngleY = (float)d;
        }
    }

    public void setRotationAngles(float walkAnimationProgress, float walkAnimationSpeed, float bob, float yaw, float pitch, float scale) {
        for (int i = 0; i < this.squidTentacles.length; ++i) {
            this.squidTentacles[i].rotateAngleX = bob;
        }
    }

    public void render(float walkAnimationProgress, float walkAnimationSpeed, float bob, float yaw, float pitch, float scale) {
        this.setRotationAngles(walkAnimationProgress, walkAnimationSpeed, bob, yaw, pitch, scale);
        this.squidBody.render(scale);
        for (int i = 0; i < this.squidTentacles.length; ++i) {
            this.squidTentacles[i].render(scale);
        }
    }
}

