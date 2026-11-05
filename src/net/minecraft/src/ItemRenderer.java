package net.minecraft.src;

import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

public class ItemRenderer {
    private Minecraft mc;
    private ItemStack itemToRender = null;
    private float equippedProgress = 0.0F;
    private float prevEquippedProgress = 0.0F;
    private RenderBlocks renderBlocksInstance = new RenderBlocks();
    private MapItemRenderer mapRenderer;
    private int selectedSlot = -1;

    public ItemRenderer(Minecraft minecraft) {
        this.mc = minecraft;
        this.mapRenderer = new MapItemRenderer(minecraft.fontRenderer, minecraft.gameSettings, minecraft.renderEngine);
    }

    public void renderItem(EntityLiving mob, ItemStack itemInHand) {
        GL11.glPushMatrix();
        if (itemInHand.itemID < 256 && RenderBlocks.renderItemIn3d(Block.blocksList[itemInHand.itemID].getRenderType())) {
            GL11.glBindTexture(3553, this.mc.renderEngine.getTexture("/terrain.png"));
            this.renderBlocksInstance.renderBlockOnInventory(Block.blocksList[itemInHand.itemID], itemInHand.getItemDamage(), mob.getEntityBrightness(1.0F));
        } else {
            if (itemInHand.itemID < 256) {
                GL11.glBindTexture(3553, this.mc.renderEngine.getTexture("/terrain.png"));
            } else {
                GL11.glBindTexture(3553, this.mc.renderEngine.getTexture("/gui/items.png"));
            }

            Tessellator tessellator = Tessellator.instance;
            int i = mob.getItemIcon(itemInHand);
            float f = (i % 16 * 16 + 0.0F) / 256.0F;
            float f1 = (i % 16 * 16 + 15.99F) / 256.0F;
            float f2 = (i / 16 * 16 + 0.0F) / 256.0F;
            float f3 = (i / 16 * 16 + 15.99F) / 256.0F;
            float f4 = 1.0F;
            float f5 = 0.0F;
            float f6 = 0.3F;
            GL11.glEnable(32826);
            GL11.glTranslatef(-f5, -f6, 0.0F);
            float f7 = 1.5F;
            GL11.glScalef(f7, f7, f7);
            GL11.glRotatef(50.0F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(335.0F, 0.0F, 0.0F, 1.0F);
            GL11.glTranslatef(-0.9375F, -0.0625F, 0.0F);
            float f8 = 0.0625F;
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 0.0F, 1.0F);
            tessellator.addVertexWithUV(0.0, 0.0, 0.0, f1, f3);
            tessellator.addVertexWithUV(f4, 0.0, 0.0, f, f3);
            tessellator.addVertexWithUV(f4, 1.0, 0.0, f, f2);
            tessellator.addVertexWithUV(0.0, 1.0, 0.0, f1, f2);
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 0.0F, -1.0F);
            tessellator.addVertexWithUV(0.0, 1.0, 0.0F - f8, f1, f2);
            tessellator.addVertexWithUV(f4, 1.0, 0.0F - f8, f, f2);
            tessellator.addVertexWithUV(f4, 0.0, 0.0F - f8, f, f3);
            tessellator.addVertexWithUV(0.0, 0.0, 0.0F - f8, f1, f3);
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(-1.0F, 0.0F, 0.0F);

            for (int j = 0; j < 16; j++) {
                float f9 = j / 16.0F;
                float f10 = f1 + (f - f1) * f9 - 0.001953125F;
                float f11 = f4 * f9;
                tessellator.addVertexWithUV(f11, 0.0, 0.0F - f8, f10, f3);
                tessellator.addVertexWithUV(f11, 0.0, 0.0, f10, f3);
                tessellator.addVertexWithUV(f11, 1.0, 0.0, f10, f2);
                tessellator.addVertexWithUV(f11, 1.0, 0.0F - f8, f10, f2);
            }

            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(1.0F, 0.0F, 0.0F);

            for (int k = 0; k < 16; k++) {
                float f12 = k / 16.0F;
                float f15 = f1 + (f - f1) * f12 - 0.001953125F;
                float f18 = f4 * f12 + 0.0625F;
                tessellator.addVertexWithUV(f18, 1.0, 0.0F - f8, f15, f2);
                tessellator.addVertexWithUV(f18, 1.0, 0.0, f15, f2);
                tessellator.addVertexWithUV(f18, 0.0, 0.0, f15, f3);
                tessellator.addVertexWithUV(f18, 0.0, 0.0F - f8, f15, f3);
            }

            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 1.0F, 0.0F);

            for (int l = 0; l < 16; l++) {
                float f13 = l / 16.0F;
                float f16 = f3 + (f2 - f3) * f13 - 0.001953125F;
                float f19 = f4 * f13 + 0.0625F;
                tessellator.addVertexWithUV(0.0, f19, 0.0, f1, f16);
                tessellator.addVertexWithUV(f4, f19, 0.0, f, f16);
                tessellator.addVertexWithUV(f4, f19, 0.0F - f8, f, f16);
                tessellator.addVertexWithUV(0.0, f19, 0.0F - f8, f1, f16);
            }

            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, -1.0F, 0.0F);

            for (int i1 = 0; i1 < 16; i1++) {
                float f14 = i1 / 16.0F;
                float f17 = f3 + (f2 - f3) * f14 - 0.001953125F;
                float f20 = f4 * f14;
                tessellator.addVertexWithUV(f4, f20, 0.0, f, f17);
                tessellator.addVertexWithUV(0.0, f20, 0.0, f1, f17);
                tessellator.addVertexWithUV(0.0, f20, 0.0F - f8, f1, f17);
                tessellator.addVertexWithUV(f4, f20, 0.0F - f8, f, f17);
            }

            tessellator.draw();
            GL11.glDisable(32826);
        }

        GL11.glPopMatrix();
    }

    public void renderItemInFirstPerson(float tickDelta) {
        float f = this.prevEquippedProgress + (this.equippedProgress - this.prevEquippedProgress) * tickDelta;
        EntityPlayerSP entityplayersp = this.mc.thePlayer;
        float f1 = entityplayersp.prevRotationPitch + (entityplayersp.rotationPitch - entityplayersp.prevRotationPitch) * tickDelta;
        GL11.glPushMatrix();
        GL11.glRotatef(f1, 1.0F, 0.0F, 0.0F);
        GL11.glRotatef(entityplayersp.prevRotationYaw + (entityplayersp.rotationYaw - entityplayersp.prevRotationYaw) * tickDelta, 0.0F, 1.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GL11.glPopMatrix();
        ItemStack itemstack = this.itemToRender;
        float f2 = this.mc
            .theWorld
            .getLightBrightness(
                MathHelper.floor_double(entityplayersp.posX), MathHelper.floor_double(entityplayersp.posY), MathHelper.floor_double(entityplayersp.posZ)
            );
        if (itemstack != null) {
            int i = Item.itemsList[itemstack.itemID].getColorFromDamage(itemstack.getItemDamage());
            float f3 = (i >> 16 & 0xFF) / 255.0F;
            float f4 = (i >> 8 & 0xFF) / 255.0F;
            float f5 = (i & 0xFF) / 255.0F;
            GL11.glColor4f(f2 * f3, f2 * f4, f2 * f5, 1.0F);
        } else {
            GL11.glColor4f(f2, f2, f2, 1.0F);
        }

        if (itemstack != null && itemstack.itemID == Item.mapItem.shiftedIndex) {
            GL11.glPushMatrix();
            float f9 = 0.8F;
            float f12 = entityplayersp.getSwingProgress(tickDelta);
            float f15 = MathHelper.sin(f12 * (float) Math.PI);
            float f18 = MathHelper.sin(MathHelper.sqrt_float(f12) * (float) Math.PI);
            GL11.glTranslatef(-f18 * 0.4F, MathHelper.sin(MathHelper.sqrt_float(f12) * (float) Math.PI * 2.0F) * 0.2F, -f15 * 0.2F);
            f12 = 1.0F - f1 / 45.0F + 0.1F;
            if (f12 < 0.0F) {
                f12 = 0.0F;
            }

            if (f12 > 1.0F) {
                f12 = 1.0F;
            }

            f12 = -MathHelper.cos(f12 * (float) Math.PI) * 0.5F + 0.5F;
            GL11.glTranslatef(0.0F, 0.0F * f9 - (1.0F - f) * 1.2F - f12 * 0.5F + 0.04F, -0.9F * f9);
            GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(f12 * -85.0F, 0.0F, 0.0F, 1.0F);
            GL11.glEnable(32826);
            GL11.glBindTexture(3553, this.mc.renderEngine.getTextureForDownloadableImage(this.mc.thePlayer.skinUrl, this.mc.thePlayer.getEntityTexture()));

            for (int j = 0; j < 2; j++) {
                int k = j * 2 - 1;
                GL11.glPushMatrix();
                GL11.glTranslatef(-0.0F, -0.6F, 1.1F * k);
                GL11.glRotatef(-45 * k, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(-90.0F, 0.0F, 0.0F, 1.0F);
                GL11.glRotatef(59.0F, 0.0F, 0.0F, 1.0F);
                GL11.glRotatef(-65 * k, 0.0F, 1.0F, 0.0F);
                Render render = RenderManager.instance.getEntityRenderObject(this.mc.thePlayer);
                RenderPlayer renderplayer = (RenderPlayer)render;
                float f6 = 1.0F;
                GL11.glScalef(f6, f6, f6);
                renderplayer.drawFirstPersonHand();
                GL11.glPopMatrix();
            }

            f15 = entityplayersp.getSwingProgress(tickDelta);
            f18 = MathHelper.sin(f15 * f15 * (float) Math.PI);
            float f19 = MathHelper.sin(MathHelper.sqrt_float(f15) * (float) Math.PI);
            GL11.glRotatef(-f18 * 20.0F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(-f19 * 20.0F, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(-f19 * 80.0F, 1.0F, 0.0F, 0.0F);
            f15 = 0.38F;
            GL11.glScalef(f15, f15, f15);
            GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
            GL11.glTranslatef(-1.0F, -1.0F, 0.0F);
            f18 = 0.015625F;
            GL11.glScalef(f18, f18, f18);
            this.mc.renderEngine.bindTexture(this.mc.renderEngine.getTexture("/misc/mapbg.png"));
            Tessellator tessellator = Tessellator.instance;
            GL11.glNormal3f(0.0F, 0.0F, -1.0F);
            tessellator.startDrawingQuads();
            byte b0 = 7;
            tessellator.addVertexWithUV(0 - b0, 128 + b0, 0.0, 0.0, 1.0);
            tessellator.addVertexWithUV(128 + b0, 128 + b0, 0.0, 1.0, 1.0);
            tessellator.addVertexWithUV(128 + b0, 0 - b0, 0.0, 1.0, 0.0);
            tessellator.addVertexWithUV(0 - b0, 0 - b0, 0.0, 0.0, 0.0);
            tessellator.draw();
            MapData mapdata = Item.mapItem.getSavedMapData(itemstack, this.mc.theWorld);
            this.mapRenderer.render(this.mc.thePlayer, this.mc.renderEngine, mapdata);
            GL11.glPopMatrix();
        } else if (itemstack != null) {
            GL11.glPushMatrix();
            float f7 = 0.8F;
            float f10 = entityplayersp.getSwingProgress(tickDelta);
            float f13 = MathHelper.sin(f10 * (float) Math.PI);
            float f16 = MathHelper.sin(MathHelper.sqrt_float(f10) * (float) Math.PI);
            GL11.glTranslatef(-f16 * 0.4F, MathHelper.sin(MathHelper.sqrt_float(f10) * (float) Math.PI * 2.0F) * 0.2F, -f13 * 0.2F);
            GL11.glTranslatef(0.7F * f7, -0.65F * f7 - (1.0F - f) * 0.6F, -0.9F * f7);
            GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
            GL11.glEnable(32826);
            f10 = entityplayersp.getSwingProgress(tickDelta);
            f13 = MathHelper.sin(f10 * f10 * (float) Math.PI);
            f16 = MathHelper.sin(MathHelper.sqrt_float(f10) * (float) Math.PI);
            GL11.glRotatef(-f13 * 20.0F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(-f16 * 20.0F, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(-f16 * 80.0F, 1.0F, 0.0F, 0.0F);
            f10 = 0.4F;
            GL11.glScalef(f10, f10, f10);
            if (itemstack.getItem().shouldRotateAroundWhenRendering()) {
                GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
            }

            this.renderItem(entityplayersp, itemstack);
            GL11.glPopMatrix();
        } else {
            GL11.glPushMatrix();
            float f8 = 0.8F;
            float f11 = entityplayersp.getSwingProgress(tickDelta);
            float f14 = MathHelper.sin(f11 * (float) Math.PI);
            float f17 = MathHelper.sin(MathHelper.sqrt_float(f11) * (float) Math.PI);
            GL11.glTranslatef(-f17 * 0.3F, MathHelper.sin(MathHelper.sqrt_float(f11) * (float) Math.PI * 2.0F) * 0.4F, -f14 * 0.4F);
            GL11.glTranslatef(0.8F * f8, -0.75F * f8 - (1.0F - f) * 0.6F, -0.9F * f8);
            GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
            GL11.glEnable(32826);
            f11 = entityplayersp.getSwingProgress(tickDelta);
            f14 = MathHelper.sin(f11 * f11 * (float) Math.PI);
            f17 = MathHelper.sin(MathHelper.sqrt_float(f11) * (float) Math.PI);
            GL11.glRotatef(f17 * 70.0F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(-f14 * 20.0F, 0.0F, 0.0F, 1.0F);
            GL11.glBindTexture(3553, this.mc.renderEngine.getTextureForDownloadableImage(this.mc.thePlayer.skinUrl, this.mc.thePlayer.getEntityTexture()));
            GL11.glTranslatef(-1.0F, 3.6F, 3.5F);
            GL11.glRotatef(120.0F, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(200.0F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(-135.0F, 0.0F, 1.0F, 0.0F);
            GL11.glScalef(1.0F, 1.0F, 1.0F);
            GL11.glTranslatef(5.6F, 0.0F, 0.0F);
            Render render1 = RenderManager.instance.getEntityRenderObject(this.mc.thePlayer);
            RenderPlayer renderplayer1 = (RenderPlayer)render1;
            f17 = 1.0F;
            GL11.glScalef(f17, f17, f17);
            renderplayer1.drawFirstPersonHand();
            GL11.glPopMatrix();
        }

        GL11.glDisable(32826);
        RenderHelper.disableStandardItemLighting();
    }

    public void renderOverlays(float tickDelta) {
        GL11.glDisable(3008);
        if (this.mc.thePlayer.isBurning()) {
            int i = this.mc.renderEngine.getTexture("/terrain.png");
            GL11.glBindTexture(3553, i);
            this.renderFireInFirstPerson(tickDelta);
        }

        if (this.mc.thePlayer.isEntityInsideOpaqueBlock()) {
            int j2 = MathHelper.floor_double(this.mc.thePlayer.posX);
            int j = MathHelper.floor_double(this.mc.thePlayer.posY);
            int k = MathHelper.floor_double(this.mc.thePlayer.posZ);
            int l = this.mc.renderEngine.getTexture("/terrain.png");
            GL11.glBindTexture(3553, l);
            int i1 = this.mc.theWorld.getBlockId(j2, j, k);
            if (this.mc.theWorld.isBlockNormalCube(j2, j, k)) {
                this.renderInsideOfBlock(tickDelta, Block.blocksList[i1].getBlockTextureFromSide(2));
            } else {
                for (int j1 = 0; j1 < 8; j1++) {
                    float f = ((j1 >> 0) % 2 - 0.5F) * this.mc.thePlayer.width * 0.9F;
                    float f1 = ((j1 >> 1) % 2 - 0.5F) * this.mc.thePlayer.height * 0.2F;
                    float f2 = ((j1 >> 2) % 2 - 0.5F) * this.mc.thePlayer.width * 0.9F;
                    int k1 = MathHelper.floor_float(j2 + f);
                    int l1 = MathHelper.floor_float(j + f1);
                    int i2 = MathHelper.floor_float(k + f2);
                    if (this.mc.theWorld.isBlockNormalCube(k1, l1, i2)) {
                        i1 = this.mc.theWorld.getBlockId(k1, l1, i2);
                    }
                }
            }

            if (Block.blocksList[i1] != null) {
                this.renderInsideOfBlock(tickDelta, Block.blocksList[i1].getBlockTextureFromSide(2));
            }
        }

        if (this.mc.thePlayer.isInsideOfMaterial(Material.water)) {
            int k2 = this.mc.renderEngine.getTexture("/misc/water.png");
            GL11.glBindTexture(3553, k2);
            this.renderWarpedTextureOverlay(tickDelta);
        }

        GL11.glEnable(3008);
    }

    private void renderInsideOfBlock(float tickDelta, int sprite) {
        Tessellator tessellator = Tessellator.instance;
        float f = this.mc.thePlayer.getEntityBrightness(tickDelta);
        f = 0.1F;
        GL11.glColor4f(f, f, f, 0.5F);
        GL11.glPushMatrix();
        float f1 = -1.0F;
        float f2 = 1.0F;
        float f3 = -1.0F;
        float f4 = 1.0F;
        float f5 = -0.5F;
        float f6 = 0.0078125F;
        float f7 = sprite % 16 / 256.0F - f6;
        float f8 = (sprite % 16 + 15.99F) / 256.0F + f6;
        float f9 = sprite / 16 / 256.0F - f6;
        float f10 = (sprite / 16 + 15.99F) / 256.0F + f6;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(f1, f3, f5, f8, f10);
        tessellator.addVertexWithUV(f2, f3, f5, f7, f10);
        tessellator.addVertexWithUV(f2, f4, f5, f7, f9);
        tessellator.addVertexWithUV(f1, f4, f5, f8, f9);
        tessellator.draw();
        GL11.glPopMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    private void renderWarpedTextureOverlay(float tickDelta) {
        Tessellator tessellator = Tessellator.instance;
        float f = this.mc.thePlayer.getEntityBrightness(tickDelta);
        GL11.glColor4f(f, f, f, 0.5F);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glPushMatrix();
        float f1 = 4.0F;
        float f2 = -1.0F;
        float f3 = 1.0F;
        float f4 = -1.0F;
        float f5 = 1.0F;
        float f6 = -0.5F;
        float f7 = -this.mc.thePlayer.rotationYaw / 64.0F;
        float f8 = this.mc.thePlayer.rotationPitch / 64.0F;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(f2, f4, f6, f1 + f7, f1 + f8);
        tessellator.addVertexWithUV(f3, f4, f6, 0.0F + f7, f1 + f8);
        tessellator.addVertexWithUV(f3, f5, f6, 0.0F + f7, 0.0F + f8);
        tessellator.addVertexWithUV(f2, f5, f6, f1 + f7, 0.0F + f8);
        tessellator.draw();
        GL11.glPopMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(3042);
    }

    private void renderFireInFirstPerson(float tickDelta) {
        Tessellator tessellator = Tessellator.instance;
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.9F);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        float f = 1.0F;

        for (int i = 0; i < 2; i++) {
            GL11.glPushMatrix();
            int j = Block.fire.blockIndexInTexture + i * 16;
            int k = (j & 15) << 4;
            int l = j & 240;
            float f1 = k / 256.0F;
            float f2 = (k + 15.99F) / 256.0F;
            float f3 = l / 256.0F;
            float f4 = (l + 15.99F) / 256.0F;
            float f5 = (0.0F - f) / 2.0F;
            float f6 = f5 + f;
            float f7 = 0.0F - f / 2.0F;
            float f8 = f7 + f;
            float f9 = -0.5F;
            GL11.glTranslatef(-(i * 2 - 1) * 0.24F, -0.3F, 0.0F);
            GL11.glRotatef((i * 2 - 1) * 10.0F, 0.0F, 1.0F, 0.0F);
            tessellator.startDrawingQuads();
            tessellator.addVertexWithUV(f5, f7, f9, f2, f4);
            tessellator.addVertexWithUV(f6, f7, f9, f1, f4);
            tessellator.addVertexWithUV(f6, f8, f9, f1, f3);
            tessellator.addVertexWithUV(f5, f8, f9, f2, f3);
            tessellator.draw();
            GL11.glPopMatrix();
        }

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(3042);
    }

    public void updateEquippedItem() {
        this.prevEquippedProgress = this.equippedProgress;
        EntityPlayerSP entityplayersp = this.mc.thePlayer;
        ItemStack itemstack = entityplayersp.inventory.getCurrentItem();
        ItemStack itemstack1 = itemstack;
        boolean flag = this.selectedSlot == entityplayersp.inventory.currentItem && itemstack1 == this.itemToRender;
        if (this.itemToRender == null && itemstack1 == null) {
            flag = true;
        }

        if (itemstack1 != null
            && this.itemToRender != null
            && itemstack1 != this.itemToRender
            && itemstack1.itemID == this.itemToRender.itemID
            && itemstack1.getItemDamage() == this.itemToRender.getItemDamage()) {
            this.itemToRender = itemstack1;
            flag = true;
        }

        float f = 0.4F;
        float f1 = flag ? 1.0F : 0.0F;
        float f2 = f1 - this.equippedProgress;
        if (f2 < -f) {
            f2 = -f;
        }

        if (f2 > f) {
            f2 = f;
        }

        this.equippedProgress += f2;
        if (this.equippedProgress < 0.1F) {
            this.itemToRender = itemstack1;
            this.selectedSlot = entityplayersp.inventory.currentItem;
        }
    }

    public void onBlockUsed() {
        this.equippedProgress = 0.0F;
    }

    public void onItemUsed() {
        this.equippedProgress = 0.0F;
    }
}
