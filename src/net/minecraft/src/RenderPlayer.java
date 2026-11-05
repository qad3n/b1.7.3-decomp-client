package net.minecraft.src;

import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

public class RenderPlayer extends RenderLiving<EntityPlayer> {
    private ModelBiped modelBipedMain = (ModelBiped)this.mainModel;
    private ModelBiped modelArmorChestplate = new ModelBiped(1.0F);
    private ModelBiped modelArmor = new ModelBiped(0.5F);
    private static final String[] armorFilenamePrefix = new String[]{"cloth", "chain", "iron", "diamond", "gold"};

    public RenderPlayer() {
        super(new ModelBiped(0.0F), 0.5F);
    }

    protected boolean shouldRenderPass(EntityPlayer var1, int var2, float var3) {
        ItemStack itemstack = var1.inventory.armorItemInSlot(3 - var2);
        if (itemstack != null) {
            Item item = itemstack.getItem();
            if (item instanceof ItemArmor) {
                ItemArmor itemarmor = (ItemArmor)item;
                this.loadTexture("/armor/" + armorFilenamePrefix[itemarmor.renderIndex] + "_" + (var2 == 2 ? 2 : 1) + ".png");
                ModelBiped modelbiped = var2 == 2 ? this.modelArmor : this.modelArmorChestplate;
                modelbiped.bipedHead.showModel = var2 == 0;
                modelbiped.bipedHeadwear.showModel = var2 == 0;
                modelbiped.bipedBody.showModel = var2 == 1 || var2 == 2;
                modelbiped.bipedRightArm.showModel = var2 == 1;
                modelbiped.bipedLeftArm.showModel = var2 == 1;
                modelbiped.bipedRightLeg.showModel = var2 == 2 || var2 == 3;
                modelbiped.bipedLeftLeg.showModel = var2 == 2 || var2 == 3;
                this.setRenderPassModel(modelbiped);
                return true;
            }
        }

        return false;
    }

    public void doRender(EntityPlayer var1, double var2, double var4, double var6, float var8, float var9) {
        ItemStack itemstack = var1.inventory.getCurrentItem();
        this.modelArmorChestplate.itemInRightHand = this.modelArmor.itemInRightHand = this.modelBipedMain.itemInRightHand = itemstack != null;
        this.modelArmorChestplate.isSneak = this.modelArmor.isSneak = this.modelBipedMain.isSneak = var1.isSneaking();
        double d0 = var4 - var1.yOffset;
        if (var1.isSneaking() && !(var1 instanceof EntityPlayerSP)) {
            d0 -= 0.125;
        }

        super.doRender(var1, var2, d0, var6, var8, var9);
        this.modelArmorChestplate.isSneak = this.modelArmor.isSneak = this.modelBipedMain.isSneak = false;
        this.modelArmorChestplate.itemInRightHand = this.modelArmor.itemInRightHand = this.modelBipedMain.itemInRightHand = false;
    }

    protected void passSpecialRender(EntityPlayer var1, double var2, double var4, double var6) {
        if (Minecraft.isGuiEnabled() && var1 != this.renderManager.livingPlayer) {
            float f = 1.6F;
            float f1 = 0.016666668F * f;
            float f2 = var1.getDistanceToEntity(this.renderManager.livingPlayer);
            float f3 = var1.isSneaking() ? 32.0F : 64.0F;
            if (f2 < f3) {
                String s = var1.username;
                if (!var1.isSneaking()) {
                    if (var1.isPlayerSleeping()) {
                        this.renderLivingLabel(var1, s, var2, var4 - 1.5, var6, 64);
                    } else {
                        this.renderLivingLabel(var1, s, var2, var4, var6, 64);
                    }
                } else {
                    FontRenderer fontrenderer = this.getFontRendererFromRenderManager();
                    GL11.glPushMatrix();
                    GL11.glTranslatef((float)var2 + 0.0F, (float)var4 + 2.3F, (float)var6);
                    GL11.glNormal3f(0.0F, 1.0F, 0.0F);
                    GL11.glRotatef(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
                    GL11.glRotatef(this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
                    GL11.glScalef(-f1, -f1, f1);
                    GL11.glDisable(2896);
                    GL11.glTranslatef(0.0F, 0.25F / f1, 0.0F);
                    GL11.glDepthMask(false);
                    GL11.glEnable(3042);
                    GL11.glBlendFunc(770, 771);
                    Tessellator tessellator = Tessellator.instance;
                    GL11.glDisable(3553);
                    tessellator.startDrawingQuads();
                    int i = fontrenderer.getStringWidth(s) / 2;
                    tessellator.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.25F);
                    tessellator.addVertex(-i - 1, -1.0, 0.0);
                    tessellator.addVertex(-i - 1, 8.0, 0.0);
                    tessellator.addVertex(i + 1, 8.0, 0.0);
                    tessellator.addVertex(i + 1, -1.0, 0.0);
                    tessellator.draw();
                    GL11.glEnable(3553);
                    GL11.glDepthMask(true);
                    fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, 0, 553648127);
                    GL11.glEnable(2896);
                    GL11.glDisable(3042);
                    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                    GL11.glPopMatrix();
                }
            }
        }
    }

    protected void renderEquippedItems(EntityPlayer var1, float var2) {
        ItemStack itemstack = var1.inventory.armorItemInSlot(3);
        if (itemstack != null && itemstack.getItem().shiftedIndex < 256) {
            GL11.glPushMatrix();
            this.modelBipedMain.bipedHead.postRender(0.0625F);
            if (RenderBlocks.renderItemIn3d(Block.blocksList[itemstack.itemID].getRenderType())) {
                float f = 0.625F;
                GL11.glTranslatef(0.0F, -0.25F, 0.0F);
                GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
                GL11.glScalef(f, -f, f);
            }

            this.renderManager.itemRenderer.renderItem(var1, itemstack);
            GL11.glPopMatrix();
        }

        if (var1.username.equals("deadmau5") && this.loadDownloadableImageTexture(var1.skinUrl, null)) {
            for (int i = 0; i < 2; i++) {
                float f1 = var1.prevRotationYaw
                    + (var1.rotationYaw - var1.prevRotationYaw) * var2
                    - (var1.prevRenderYawOffset + (var1.renderYawOffset - var1.prevRenderYawOffset) * var2);
                float f2 = var1.prevRotationPitch + (var1.rotationPitch - var1.prevRotationPitch) * var2;
                GL11.glPushMatrix();
                GL11.glRotatef(f1, 0.0F, 1.0F, 0.0F);
                GL11.glRotatef(f2, 1.0F, 0.0F, 0.0F);
                GL11.glTranslatef(0.375F * (i * 2 - 1), 0.0F, 0.0F);
                GL11.glTranslatef(0.0F, -0.375F, 0.0F);
                GL11.glRotatef(-f2, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(-f1, 0.0F, 1.0F, 0.0F);
                float f3 = 1.3333334F;
                GL11.glScalef(f3, f3, f3);
                this.modelBipedMain.renderEars(0.0625F);
                GL11.glPopMatrix();
            }
        }

        if (this.loadDownloadableImageTexture(var1.playerCloakUrl, null)) {
            GL11.glPushMatrix();
            GL11.glTranslatef(0.0F, 0.0F, 0.125F);
            double d3 = var1.lastCapeX + (var1.capeX - var1.lastCapeX) * var2 - (var1.prevPosX + (var1.posX - var1.prevPosX) * var2);
            double d4 = var1.lastCapeY + (var1.capeY - var1.lastCapeY) * var2 - (var1.prevPosY + (var1.posY - var1.prevPosY) * var2);
            double d0 = var1.lastCapeZ + (var1.capeZ - var1.lastCapeZ) * var2 - (var1.prevPosZ + (var1.posZ - var1.prevPosZ) * var2);
            float f4 = var1.prevRenderYawOffset + (var1.renderYawOffset - var1.prevRenderYawOffset) * var2;
            double d1 = MathHelper.sin(f4 * (float) Math.PI / 180.0F);
            double d2 = -MathHelper.cos(f4 * (float) Math.PI / 180.0F);
            float f5 = (float)d4 * 10.0F;
            if (f5 < -6.0F) {
                f5 = -6.0F;
            }

            if (f5 > 32.0F) {
                f5 = 32.0F;
            }

            float f6 = (float)(d3 * d1 + d0 * d2) * 100.0F;
            float f7 = (float)(d3 * d2 - d0 * d1) * 100.0F;
            if (f6 < 0.0F) {
                f6 = 0.0F;
            }

            float f8 = var1.lastBob + (var1.bob - var1.lastBob) * var2;
            f5 += MathHelper.sin((var1.prevDistanceWalkedModified + (var1.distanceWalkedModified - var1.prevDistanceWalkedModified) * var2) * 6.0F)
                * 32.0F
                * f8;
            if (var1.isSneaking()) {
                f5 += 25.0F;
            }

            GL11.glRotatef(6.0F + f6 / 2.0F + f5, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(f7 / 2.0F, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(-f7 / 2.0F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
            this.modelBipedMain.renderCloak(0.0625F);
            GL11.glPopMatrix();
        }

        ItemStack itemstack1 = var1.inventory.getCurrentItem();
        if (itemstack1 != null) {
            GL11.glPushMatrix();
            this.modelBipedMain.bipedRightArm.postRender(0.0625F);
            GL11.glTranslatef(-0.0625F, 0.4375F, 0.0625F);
            if (var1.fishEntity != null) {
                itemstack1 = new ItemStack(Item.stick);
            }

            if (itemstack1.itemID < 256 && RenderBlocks.renderItemIn3d(Block.blocksList[itemstack1.itemID].getRenderType())) {
                float f11 = 0.5F;
                GL11.glTranslatef(0.0F, 0.1875F, -0.3125F);
                f11 *= 0.75F;
                GL11.glRotatef(20.0F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
                GL11.glScalef(f11, -f11, f11);
            } else if (Item.itemsList[itemstack1.itemID].isFull3D()) {
                float f9 = 0.625F;
                if (Item.itemsList[itemstack1.itemID].shouldRotateAroundWhenRendering()) {
                    GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
                    GL11.glTranslatef(0.0F, -0.125F, 0.0F);
                }

                GL11.glTranslatef(0.0F, 0.1875F, 0.0F);
                GL11.glScalef(f9, -f9, f9);
                GL11.glRotatef(-100.0F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
            } else {
                float f10 = 0.375F;
                GL11.glTranslatef(0.25F, 0.1875F, -0.1875F);
                GL11.glScalef(f10, f10, f10);
                GL11.glRotatef(60.0F, 0.0F, 0.0F, 1.0F);
                GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(20.0F, 0.0F, 0.0F, 1.0F);
            }

            this.renderManager.itemRenderer.renderItem(var1, itemstack1);
            GL11.glPopMatrix();
        }
    }

    protected void preRenderCallback(EntityPlayer var1, float var2) {
        float f = 0.9375F;
        GL11.glScalef(f, f, f);
    }

    public void drawFirstPersonHand() {
        this.modelBipedMain.onGround = 0.0F;
        this.modelBipedMain.setRotationAngles(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
        this.modelBipedMain.bipedRightArm.render(0.0625F);
    }

    protected void applyTranslation(EntityPlayer var1, double var2, double var4, double var6) {
        if (var1.isEntityAlive() && var1.isPlayerSleeping()) {
            super.applyTranslation(var1, var2 + var1.sleepingCameraOffsetX, var4 + var1.sleepingCameraOffsetY, var6 + var1.sleepingCameraOffsetZ);
        } else {
            super.applyTranslation(var1, var2, var4, var6);
        }
    }

    protected void rotateCorpse(EntityPlayer var1, float var2, float var3, float var4) {
        if (var1.isEntityAlive() && var1.isPlayerSleeping()) {
            GL11.glRotatef(var1.getBedOrientationInDegrees(), 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(this.getDeathMaxRotation(var1), 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(270.0F, 0.0F, 1.0F, 0.0F);
        } else {
            super.rotateCorpse(var1, var2, var3, var4);
        }
    }
}
