package net.minecraft.src;

import java.util.Random;
import org.lwjgl.opengl.GL11;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class RenderItem
extends Render<EntityItem> {
    private RenderBlocks renderBlocks = new RenderBlocks();
    private Random random = new Random();
    public boolean useCustomDisplayColor = true;

    public RenderItem() {
        this.shadowSize = 0.15f;
        this.shadowDarkness = 0.75f;
    }

    @Override
    public void doRender(EntityItem entityItem, double d, double d2, double d3, float f, float f2) {
        this.random.setSeed(187L);
        ItemStack itemStack = entityItem.item;
        GL11.glPushMatrix();
        float f3 = MathHelper.sin(((float)entityItem.age + f2) / 10.0f + entityItem.bobOffset) * 0.1f + 0.1f;
        float f4 = (((float)entityItem.age + f2) / 20.0f + entityItem.bobOffset) * 57.295776f;
        int n = 1;
        if (entityItem.item.stackSize > 1) {
            n = 2;
        }
        if (entityItem.item.stackSize > 5) {
            n = 3;
        }
        if (entityItem.item.stackSize > 20) {
            n = 4;
        }
        GL11.glTranslatef((float)d, (float)d2 + f3, (float)d3);
        GL11.glEnable(32826);
        if (itemStack.itemID < 256 && RenderBlocks.renderItemIn3d(Block.blocksList[itemStack.itemID].getRenderType())) {
            GL11.glRotatef(f4, 0.0f, 1.0f, 0.0f);
            this.loadTexture("/terrain.png");
            float f5 = 0.25f;
            if (!Block.blocksList[itemStack.itemID].renderAsNormalBlock() && itemStack.itemID != Block.stairSingle.blockID && Block.blocksList[itemStack.itemID].getRenderType() != 16) {
                f5 = 0.5f;
            }
            GL11.glScalef(f5, f5, f5);
            for (int i = 0; i < n; ++i) {
                GL11.glPushMatrix();
                if (i > 0) {
                    float f6 = (this.random.nextFloat() * 2.0f - 1.0f) * 0.2f / f5;
                    float f7 = (this.random.nextFloat() * 2.0f - 1.0f) * 0.2f / f5;
                    float f8 = (this.random.nextFloat() * 2.0f - 1.0f) * 0.2f / f5;
                    GL11.glTranslatef(f6, f7, f8);
                }
                this.renderBlocks.renderBlockOnInventory(Block.blocksList[itemStack.itemID], itemStack.getItemDamage(), entityItem.getEntityBrightness(f2));
                GL11.glPopMatrix();
            }
        } else {
            float f9;
            float f10;
            float f11;
            int n2;
            GL11.glScalef(0.5f, 0.5f, 0.5f);
            int n3 = itemStack.getIconIndex();
            if (itemStack.itemID < 256) {
                this.loadTexture("/terrain.png");
            } else {
                this.loadTexture("/gui/items.png");
            }
            Tessellator tessellator = Tessellator.instance;
            float f12 = (float)(n3 % 16 * 16 + 0) / 256.0f;
            float f13 = (float)(n3 % 16 * 16 + 16) / 256.0f;
            float f14 = (float)(n3 / 16 * 16 + 0) / 256.0f;
            float f15 = (float)(n3 / 16 * 16 + 16) / 256.0f;
            float f16 = 1.0f;
            float f17 = 0.5f;
            float f18 = 0.25f;
            if (this.useCustomDisplayColor) {
                n2 = Item.itemsList[itemStack.itemID].getColorFromDamage(itemStack.getItemDamage());
                f11 = (float)(n2 >> 16 & 0xFF) / 255.0f;
                f10 = (float)(n2 >> 8 & 0xFF) / 255.0f;
                f9 = (float)(n2 & 0xFF) / 255.0f;
                float f19 = entityItem.getEntityBrightness(f2);
                GL11.glColor4f(f11 * f19, f10 * f19, f9 * f19, 1.0f);
            }
            for (n2 = 0; n2 < n; ++n2) {
                GL11.glPushMatrix();
                if (n2 > 0) {
                    f11 = (this.random.nextFloat() * 2.0f - 1.0f) * 0.3f;
                    f10 = (this.random.nextFloat() * 2.0f - 1.0f) * 0.3f;
                    f9 = (this.random.nextFloat() * 2.0f - 1.0f) * 0.3f;
                    GL11.glTranslatef(f11, f10, f9);
                }
                GL11.glRotatef(180.0f - this.renderManager.playerViewY, 0.0f, 1.0f, 0.0f);
                tessellator.startDrawingQuads();
                tessellator.setNormal(0.0f, 1.0f, 0.0f);
                tessellator.addVertexWithUV(0.0f - f17, 0.0f - f18, 0.0, f12, f15);
                tessellator.addVertexWithUV(f16 - f17, 0.0f - f18, 0.0, f13, f15);
                tessellator.addVertexWithUV(f16 - f17, 1.0f - f18, 0.0, f13, f14);
                tessellator.addVertexWithUV(0.0f - f17, 1.0f - f18, 0.0, f12, f14);
                tessellator.draw();
                GL11.glPopMatrix();
            }
        }
        GL11.glDisable(32826);
        GL11.glPopMatrix();
    }

    public void drawItemIntoGui(FontRenderer textRenderer, RenderEngine textureManager, int item, int metadata, int sprite, int x, int y) {
        if (item < 256 && RenderBlocks.renderItemIn3d(Block.blocksList[item].getRenderType())) {
            int n = item;
            textureManager.bindTexture(textureManager.getTexture("/terrain.png"));
            Block block = Block.blocksList[n];
            GL11.glPushMatrix();
            GL11.glTranslatef(x - 2, y + 3, -3.0f);
            GL11.glScalef(10.0f, 10.0f, 10.0f);
            GL11.glTranslatef(1.0f, 0.5f, 1.0f);
            GL11.glScalef(1.0f, 1.0f, -1.0f);
            GL11.glRotatef(210.0f, 1.0f, 0.0f, 0.0f);
            GL11.glRotatef(45.0f, 0.0f, 1.0f, 0.0f);
            int n2 = Item.itemsList[item].getColorFromDamage(metadata);
            float f = (float)(n2 >> 16 & 0xFF) / 255.0f;
            float f2 = (float)(n2 >> 8 & 0xFF) / 255.0f;
            float f3 = (float)(n2 & 0xFF) / 255.0f;
            if (this.useCustomDisplayColor) {
                GL11.glColor4f(f, f2, f3, 1.0f);
            }
            GL11.glRotatef(-90.0f, 0.0f, 1.0f, 0.0f);
            this.renderBlocks.updateColor = this.useCustomDisplayColor;
            this.renderBlocks.renderBlockOnInventory(block, metadata, 1.0f);
            this.renderBlocks.updateColor = true;
            GL11.glPopMatrix();
        } else if (sprite >= 0) {
            GL11.glDisable(2896);
            if (item < 256) {
                textureManager.bindTexture(textureManager.getTexture("/terrain.png"));
            } else {
                textureManager.bindTexture(textureManager.getTexture("/gui/items.png"));
            }
            int n = Item.itemsList[item].getColorFromDamage(metadata);
            float f = (float)(n >> 16 & 0xFF) / 255.0f;
            float f4 = (float)(n >> 8 & 0xFF) / 255.0f;
            float f5 = (float)(n & 0xFF) / 255.0f;
            if (this.useCustomDisplayColor) {
                GL11.glColor4f(f, f4, f5, 1.0f);
            }
            this.renderTexturedQuad(x, y, sprite % 16 * 16, sprite / 16 * 16, 16, 16);
            GL11.glEnable(2896);
        }
        GL11.glEnable(2884);
    }

    public void renderItemIntoGUI(FontRenderer textRenderer, RenderEngine textureManager, ItemStack item, int x, int y) {
        if (item == null) {
            return;
        }
        this.drawItemIntoGui(textRenderer, textureManager, item.itemID, item.getItemDamage(), item.getIconIndex(), x, y);
    }

    public void renderItemOverlayIntoGUI(FontRenderer textRenderer, RenderEngine textureManager, ItemStack item, int x, int y) {
        if (item == null) {
            return;
        }
        if (item.stackSize > 1) {
            String string = "" + item.stackSize;
            GL11.glDisable(2896);
            GL11.glDisable(2929);
            textRenderer.drawStringWithShadow(string, x + 19 - 2 - textRenderer.getStringWidth(string), y + 6 + 3, 0xFFFFFF);
            GL11.glEnable(2896);
            GL11.glEnable(2929);
        }
        if (item.isItemDamaged()) {
            int n = (int)Math.round(13.0 - (double)item.getItemDamageForDisplay() * 13.0 / (double)item.getMaxDamage());
            int n2 = (int)Math.round(255.0 - (double)item.getItemDamageForDisplay() * 255.0 / (double)item.getMaxDamage());
            GL11.glDisable(2896);
            GL11.glDisable(2929);
            GL11.glDisable(3553);
            Tessellator tessellator = Tessellator.instance;
            int n3 = 255 - n2 << 16 | n2 << 8;
            int n4 = (255 - n2) / 4 << 16 | 0x3F00;
            this.renderQuad(tessellator, x + 2, y + 13, 13, 2, 0);
            this.renderQuad(tessellator, x + 2, y + 13, 12, 1, n4);
            this.renderQuad(tessellator, x + 2, y + 13, n, 1, n3);
            GL11.glEnable(3553);
            GL11.glEnable(2896);
            GL11.glEnable(2929);
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        }
    }

    private void renderQuad(Tessellator tesselator, int x, int y, int width, int height, int color) {
        tesselator.startDrawingQuads();
        tesselator.setColorOpaque_I(color);
        tesselator.addVertex(x + 0, y + 0, 0.0);
        tesselator.addVertex(x + 0, y + height, 0.0);
        tesselator.addVertex(x + width, y + height, 0.0);
        tesselator.addVertex(x + width, y + 0, 0.0);
        tesselator.draw();
    }

    public void renderTexturedQuad(int x, int y, int u, int v, int width, int height) {
        float f = 0.0f;
        float f2 = 0.00390625f;
        float f3 = 0.00390625f;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(x + 0, y + height, f, (float)(u + 0) * f2, (float)(v + height) * f3);
        tessellator.addVertexWithUV(x + width, y + height, f, (float)(u + width) * f2, (float)(v + height) * f3);
        tessellator.addVertexWithUV(x + width, y + 0, f, (float)(u + width) * f2, (float)(v + 0) * f3);
        tessellator.addVertexWithUV(x + 0, y + 0, f, (float)(u + 0) * f2, (float)(v + 0) * f3);
        tessellator.draw();
    }
}

