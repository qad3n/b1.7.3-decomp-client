package net.minecraft.src;

import java.util.Random;

public class ModelGhast
extends ModelBase {
    ModelRenderer body;
    ModelRenderer[] tentacles = new ModelRenderer[9];

    public ModelGhast() {
        int n = -16;
        this.body = new ModelRenderer(0, 0);
        this.body.addBox(-8.0f, -8.0f, -8.0f, 16, 16, 16);
        this.body.rotationPointY += (float)(24 + n);
        Random random = new Random(1660L);
        for (int i = 0; i < this.tentacles.length; ++i) {
            this.tentacles[i] = new ModelRenderer(0, 0);
            float f = (((float)(i % 3) - (float)(i / 3 % 2) * 0.5f + 0.25f) / 2.0f * 2.0f - 1.0f) * 5.0f;
            float f2 = ((float)(i / 3) / 2.0f * 2.0f - 1.0f) * 5.0f;
            int n2 = random.nextInt(7) + 8;
            this.tentacles[i].addBox(-1.0f, 0.0f, -1.0f, 2, n2, 2);
            this.tentacles[i].rotationPointX = f;
            this.tentacles[i].rotationPointZ = f2;
            this.tentacles[i].rotationPointY = 31 + n;
        }
    }

    public void setRotationAngles(float walkAnimationProgress, float walkAnimationSpeed, float bob, float yaw, float pitch, float scale) {
        for (int i = 0; i < this.tentacles.length; ++i) {
            this.tentacles[i].rotateAngleX = 0.2f * MathHelper.sin(bob * 0.3f + (float)i) + 0.4f;
        }
    }

    public void render(float walkAnimationProgress, float walkAnimationSpeed, float bob, float yaw, float pitch, float scale) {
        this.setRotationAngles(walkAnimationProgress, walkAnimationSpeed, bob, yaw, pitch, scale);
        this.body.render(scale);
        for (int i = 0; i < this.tentacles.length; ++i) {
            this.tentacles[i].render(scale);
        }
    }
}

