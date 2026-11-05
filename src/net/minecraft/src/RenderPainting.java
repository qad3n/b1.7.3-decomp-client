package net.minecraft.src;

import java.util.Random;
import org.lwjgl.opengl.GL11;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class RenderPainting
extends Render<EntityPainting> {
    private Random rand = new Random();

    @Override
    public void doRender(EntityPainting entityPainting, double d, double d2, double d3, float f, float f2) {
        this.rand.setSeed(187L);
        GL11.glPushMatrix();
        GL11.glTranslatef((float)d, (float)d2, (float)d3);
        GL11.glRotatef(f, 0.0f, 1.0f, 0.0f);
        GL11.glEnable(32826);
        this.loadTexture("/art/kz.png");
        EntityPainting.EnumArt enumArt = entityPainting.art;
        float f3 = 0.0625f;
        GL11.glScalef(f3, f3, f3);
        this.renderPainting(entityPainting, enumArt.sizeX, enumArt.sizeY, enumArt.offsetX, enumArt.offsetY);
        GL11.glDisable(32826);
        GL11.glPopMatrix();
    }

    private void renderPainting(EntityPainting painting, int width, int height, int u, int v) {
        float f = (float)(-width) / 2.0f;
        float f2 = (float)(-height) / 2.0f;
        float f3 = -0.5f;
        float f4 = 0.5f;
        for (int i = 0; i < width / 16; ++i) {
            for (int j = 0; j < height / 16; ++j) {
                float f5 = f + (float)((i + 1) * 16);
                float f6 = f + (float)(i * 16);
                float f7 = f2 + (float)((j + 1) * 16);
                float f8 = f2 + (float)(j * 16);
                this.applyBrightness(painting, (f5 + f6) / 2.0f, (f7 + f8) / 2.0f);
                float f9 = (float)(u + width - i * 16) / 256.0f;
                float f10 = (float)(u + width - (i + 1) * 16) / 256.0f;
                float f11 = (float)(v + height - j * 16) / 256.0f;
                float f12 = (float)(v + height - (j + 1) * 16) / 256.0f;
                float f13 = 0.75f;
                float f14 = 0.8125f;
                float f15 = 0.0f;
                float f16 = 0.0625f;
                float f17 = 0.75f;
                float f18 = 0.8125f;
                float f19 = 0.001953125f;
                float f20 = 0.001953125f;
                float f21 = 0.7519531f;
                float f22 = 0.7519531f;
                float f23 = 0.0f;
                float f24 = 0.0625f;
                Tessellator tessellator = Tessellator.instance;
                tessellator.startDrawingQuads();
                tessellator.setNormal(0.0f, 0.0f, -1.0f);
                tessellator.addVertexWithUV(f5, f8, f3, f10, f11);
                tessellator.addVertexWithUV(f6, f8, f3, f9, f11);
                tessellator.addVertexWithUV(f6, f7, f3, f9, f12);
                tessellator.addVertexWithUV(f5, f7, f3, f10, f12);
                tessellator.setNormal(0.0f, 0.0f, 1.0f);
                tessellator.addVertexWithUV(f5, f7, f4, f13, f15);
                tessellator.addVertexWithUV(f6, f7, f4, f14, f15);
                tessellator.addVertexWithUV(f6, f8, f4, f14, f16);
                tessellator.addVertexWithUV(f5, f8, f4, f13, f16);
                tessellator.setNormal(0.0f, -1.0f, 0.0f);
                tessellator.addVertexWithUV(f5, f7, f3, f17, f19);
                tessellator.addVertexWithUV(f6, f7, f3, f18, f19);
                tessellator.addVertexWithUV(f6, f7, f4, f18, f20);
                tessellator.addVertexWithUV(f5, f7, f4, f17, f20);
                tessellator.setNormal(0.0f, 1.0f, 0.0f);
                tessellator.addVertexWithUV(f5, f8, f4, f17, f19);
                tessellator.addVertexWithUV(f6, f8, f4, f18, f19);
                tessellator.addVertexWithUV(f6, f8, f3, f18, f20);
                tessellator.addVertexWithUV(f5, f8, f3, f17, f20);
                tessellator.setNormal(-1.0f, 0.0f, 0.0f);
                tessellator.addVertexWithUV(f5, f7, f4, f22, f23);
                tessellator.addVertexWithUV(f5, f8, f4, f22, f24);
                tessellator.addVertexWithUV(f5, f8, f3, f21, f24);
                tessellator.addVertexWithUV(f5, f7, f3, f21, f23);
                tessellator.setNormal(1.0f, 0.0f, 0.0f);
                tessellator.addVertexWithUV(f6, f7, f3, f22, f23);
                tessellator.addVertexWithUV(f6, f8, f3, f22, f24);
                tessellator.addVertexWithUV(f6, f8, f4, f21, f24);
                tessellator.addVertexWithUV(f6, f7, f4, f21, f23);
                tessellator.draw();
            }
        }
    }

    private void applyBrightness(EntityPainting painting, float u, float v) {
        int n = MathHelper.floor_double(painting.posX);
        int n2 = MathHelper.floor_double(painting.posY + (double)(v / 16.0f));
        int n3 = MathHelper.floor_double(painting.posZ);
        if (painting.direction == 0) {
            n = MathHelper.floor_double(painting.posX + (double)(u / 16.0f));
        }
        if (painting.direction == 1) {
            n3 = MathHelper.floor_double(painting.posZ - (double)(u / 16.0f));
        }
        if (painting.direction == 2) {
            n = MathHelper.floor_double(painting.posX - (double)(u / 16.0f));
        }
        if (painting.direction == 3) {
            n3 = MathHelper.floor_double(painting.posZ + (double)(u / 16.0f));
        }
        float f = this.renderManager.worldObj.getLightBrightness(n, n2, n3);
        GL11.glColor3f(f, f, f);
    }
}

