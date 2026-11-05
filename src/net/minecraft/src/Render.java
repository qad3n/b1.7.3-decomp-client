package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public abstract class Render<T extends Entity> {
    protected RenderManager renderManager;
    private ModelBase modelBase = new ModelBiped();
    private RenderBlocks renderBlocks = new RenderBlocks();
    protected float shadowSize = 0.0F;
    protected float shadowDarkness = 1.0F;

    public abstract void doRender(T entity, double dx, double dy, double dz, float yaw, float tickDelta);

    protected void loadTexture(String path) {
        RenderEngine renderengine = this.renderManager.renderEngine;
        renderengine.bindTexture(renderengine.getTexture(path));
    }

    protected boolean loadDownloadableImageTexture(String url, String backup) {
        RenderEngine renderengine = this.renderManager.renderEngine;
        int i = renderengine.getTextureForDownloadableImage(url, backup);
        if (i >= 0) {
            renderengine.bindTexture(i);
            return true;
        } else {
            return false;
        }
    }

    private void renderEntityOnFire(Entity entity, double dx, double dy, double dz, float tickDelta) {
        GL11.glDisable(2896);
        int i = Block.fire.blockIndexInTexture;
        int j = (i & 15) << 4;
        int k = i & 240;
        float f = j / 256.0F;
        float f1 = (j + 15.99F) / 256.0F;
        float f2 = k / 256.0F;
        float f3 = (k + 15.99F) / 256.0F;
        GL11.glPushMatrix();
        GL11.glTranslatef((float)dx, (float)dy, (float)dz);
        float f4 = entity.width * 1.4F;
        GL11.glScalef(f4, f4, f4);
        this.loadTexture("/terrain.png");
        Tessellator tessellator = Tessellator.instance;
        float f5 = 0.5F;
        float f6 = 0.0F;
        float f7 = entity.height / f4;
        float f8 = (float)(entity.posY - entity.boundingBox.minY);
        GL11.glRotatef(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(0.0F, 0.0F, -0.3F + (int)f7 * 0.02F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        float f9 = 0.0F;
        int l = 0;
        tessellator.startDrawingQuads();

        while (f7 > 0.0F) {
            if (l % 2 == 0) {
                f = j / 256.0F;
                f1 = (j + 15.99F) / 256.0F;
                f2 = k / 256.0F;
                f3 = (k + 15.99F) / 256.0F;
            } else {
                f = j / 256.0F;
                f1 = (j + 15.99F) / 256.0F;
                f2 = (k + 16) / 256.0F;
                f3 = (k + 16 + 15.99F) / 256.0F;
            }

            if (l / 2 % 2 == 0) {
                float f10 = f1;
                f1 = f;
                f = f10;
            }

            tessellator.addVertexWithUV(f5 - f6, 0.0F - f8, f9, f1, f3);
            tessellator.addVertexWithUV(-f5 - f6, 0.0F - f8, f9, f, f3);
            tessellator.addVertexWithUV(-f5 - f6, 1.4F - f8, f9, f, f2);
            tessellator.addVertexWithUV(f5 - f6, 1.4F - f8, f9, f1, f2);
            f7 -= 0.45F;
            f8 -= 0.45F;
            f5 *= 0.9F;
            f9 += 0.03F;
            l++;
        }

        tessellator.draw();
        GL11.glPopMatrix();
        GL11.glEnable(2896);
    }

    private void renderShadow(Entity entity, double dx, double dy, double dz, float yaw, float tickDelta) {
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        RenderEngine renderengine = this.renderManager.renderEngine;
        renderengine.bindTexture(renderengine.getTexture("%clamp%/misc/shadow.png"));
        World world = this.getWorldFromRenderManager();
        GL11.glDepthMask(false);
        float f = this.shadowSize;
        double d0 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * tickDelta;
        double d1 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * tickDelta + entity.getShadowSize();
        double d2 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * tickDelta;
        int i = MathHelper.floor_double(d0 - f);
        int j = MathHelper.floor_double(d0 + f);
        int k = MathHelper.floor_double(d1 - f);
        int l = MathHelper.floor_double(d1);
        int i1 = MathHelper.floor_double(d2 - f);
        int j1 = MathHelper.floor_double(d2 + f);
        double d3 = dx - d0;
        double d4 = dy - d1;
        double d5 = dz - d2;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();

        for (int k1 = i; k1 <= j; k1++) {
            for (int l1 = k; l1 <= l; l1++) {
                for (int i2 = i1; i2 <= j1; i2++) {
                    int j2 = world.getBlockId(k1, l1 - 1, i2);
                    if (j2 > 0 && world.getBlockLightValue(k1, l1, i2) > 3) {
                        this.renderShadowOnBlock(
                            Block.blocksList[j2], dx, dy + entity.getShadowSize(), dz, k1, l1, i2, yaw, f, d3, d4 + entity.getShadowSize(), d5
                        );
                    }
                }
            }
        }

        tessellator.draw();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(3042);
        GL11.glDepthMask(true);
    }

    private World getWorldFromRenderManager() {
        return this.renderManager.worldObj;
    }

    private void renderShadowOnBlock(Block block, double dx, double dy, double dz, int x, int y, int z, float yaw, float shadowSize, double cx, double cy, double cz) {
        Tessellator tessellator = Tessellator.instance;
        if (!block.renderAsNormalBlock()) {
            return;
        }
        double d = ((double) yaw - (dy - ((double) y + cy)) / 2.0) * 0.5 * (double) this.getWorldFromRenderManager().getLightBrightness(x, y, z);
        if (d < 0.0) {
            return;
        }
        if (d > 1.0) {
            d = 1.0;
        }
        tessellator.setColorRGBA_F(1.0f, 1.0f, 1.0f, (float) d);
        double d2 = (double) x + block.minX + cx;
        double d3 = (double) x + block.maxX + cx;
        double d4 = (double) y + block.minY + cy + 0.015625;
        double d5 = (double) z + block.minZ + cz;
        double d6 = (double) z + block.maxZ + cz;
        float f = (float) ((dx - d2) / 2.0 / (double) shadowSize + 0.5);
        float f2 = (float) ((dx - d3) / 2.0 / (double) shadowSize + 0.5);
        float f3 = (float) ((dz - d5) / 2.0 / (double) shadowSize + 0.5);
        float f4 = (float) ((dz - d6) / 2.0 / (double) shadowSize + 0.5);
        tessellator.addVertexWithUV(d2, d4, d5, f, f3);
        tessellator.addVertexWithUV(d2, d4, d6, f, f4);
        tessellator.addVertexWithUV(d3, d4, d6, f2, f4);
        tessellator.addVertexWithUV(d3, d4, d5, f2, f3);
    }

    public static void renderOffsetAABB(AxisAlignedBB shape, double dx, double dy, double dz) {
        GL11.glDisable(3553);
        Tessellator tessellator = Tessellator.instance;
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        tessellator.startDrawingQuads();
        tessellator.setTranslationD(dx, dy, dz);
        tessellator.setNormal(0.0F, 0.0F, -1.0F);
        tessellator.addVertex(shape.minX, shape.maxY, shape.minZ);
        tessellator.addVertex(shape.maxX, shape.maxY, shape.minZ);
        tessellator.addVertex(shape.maxX, shape.minY, shape.minZ);
        tessellator.addVertex(shape.minX, shape.minY, shape.minZ);
        tessellator.setNormal(0.0F, 0.0F, 1.0F);
        tessellator.addVertex(shape.minX, shape.minY, shape.maxZ);
        tessellator.addVertex(shape.maxX, shape.minY, shape.maxZ);
        tessellator.addVertex(shape.maxX, shape.maxY, shape.maxZ);
        tessellator.addVertex(shape.minX, shape.maxY, shape.maxZ);
        tessellator.setNormal(0.0F, -1.0F, 0.0F);
        tessellator.addVertex(shape.minX, shape.minY, shape.minZ);
        tessellator.addVertex(shape.maxX, shape.minY, shape.minZ);
        tessellator.addVertex(shape.maxX, shape.minY, shape.maxZ);
        tessellator.addVertex(shape.minX, shape.minY, shape.maxZ);
        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        tessellator.addVertex(shape.minX, shape.maxY, shape.maxZ);
        tessellator.addVertex(shape.maxX, shape.maxY, shape.maxZ);
        tessellator.addVertex(shape.maxX, shape.maxY, shape.minZ);
        tessellator.addVertex(shape.minX, shape.maxY, shape.minZ);
        tessellator.setNormal(-1.0F, 0.0F, 0.0F);
        tessellator.addVertex(shape.minX, shape.minY, shape.maxZ);
        tessellator.addVertex(shape.minX, shape.maxY, shape.maxZ);
        tessellator.addVertex(shape.minX, shape.maxY, shape.minZ);
        tessellator.addVertex(shape.minX, shape.minY, shape.minZ);
        tessellator.setNormal(1.0F, 0.0F, 0.0F);
        tessellator.addVertex(shape.maxX, shape.minY, shape.minZ);
        tessellator.addVertex(shape.maxX, shape.maxY, shape.minZ);
        tessellator.addVertex(shape.maxX, shape.maxY, shape.maxZ);
        tessellator.addVertex(shape.maxX, shape.minY, shape.maxZ);
        tessellator.setTranslationD(0.0, 0.0, 0.0);
        tessellator.draw();
        GL11.glEnable(3553);
    }

    public static void renderAABB(AxisAlignedBB shape) {
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertex(shape.minX, shape.maxY, shape.minZ);
        tessellator.addVertex(shape.maxX, shape.maxY, shape.minZ);
        tessellator.addVertex(shape.maxX, shape.minY, shape.minZ);
        tessellator.addVertex(shape.minX, shape.minY, shape.minZ);
        tessellator.addVertex(shape.minX, shape.minY, shape.maxZ);
        tessellator.addVertex(shape.maxX, shape.minY, shape.maxZ);
        tessellator.addVertex(shape.maxX, shape.maxY, shape.maxZ);
        tessellator.addVertex(shape.minX, shape.maxY, shape.maxZ);
        tessellator.addVertex(shape.minX, shape.minY, shape.minZ);
        tessellator.addVertex(shape.maxX, shape.minY, shape.minZ);
        tessellator.addVertex(shape.maxX, shape.minY, shape.maxZ);
        tessellator.addVertex(shape.minX, shape.minY, shape.maxZ);
        tessellator.addVertex(shape.minX, shape.maxY, shape.maxZ);
        tessellator.addVertex(shape.maxX, shape.maxY, shape.maxZ);
        tessellator.addVertex(shape.maxX, shape.maxY, shape.minZ);
        tessellator.addVertex(shape.minX, shape.maxY, shape.minZ);
        tessellator.addVertex(shape.minX, shape.minY, shape.maxZ);
        tessellator.addVertex(shape.minX, shape.maxY, shape.maxZ);
        tessellator.addVertex(shape.minX, shape.maxY, shape.minZ);
        tessellator.addVertex(shape.minX, shape.minY, shape.minZ);
        tessellator.addVertex(shape.maxX, shape.minY, shape.minZ);
        tessellator.addVertex(shape.maxX, shape.maxY, shape.minZ);
        tessellator.addVertex(shape.maxX, shape.maxY, shape.maxZ);
        tessellator.addVertex(shape.maxX, shape.minY, shape.maxZ);
        tessellator.draw();
    }

    public void setRenderManager(RenderManager dispatcher) {
        this.renderManager = dispatcher;
    }

    public void doRenderShadowAndFire(T entity, double dx, double dy, double dz, float yaw, float tickDelta) {
        if (this.renderManager.options.fancyGraphics && this.shadowSize > 0.0F) {
            double d0 = this.renderManager.squaredDistanceToCamera(entity.posX, entity.posY, entity.posZ);
            float f = (float)((1.0 - d0 / 256.0) * this.shadowDarkness);
            if (f > 0.0F) {
                this.renderShadow(entity, dx, dy, dz, f, tickDelta);
            }
        }

        if (entity.isBurning()) {
            this.renderEntityOnFire(entity, dx, dy, dz, tickDelta);
        }
    }

    public FontRenderer getFontRendererFromRenderManager() {
        return this.renderManager.getFontRenderer();
    }
}
