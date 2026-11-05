package net.minecraft.src;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

public class GuiIngame extends Gui {
    private static RenderItem itemRenderer = new RenderItem();
    private List<ChatLine> chatMessageList = new ArrayList<>();
    private Random rand = new Random();
    private Minecraft mc;
    public String selectedName = null;
    private int updateCounter = 0;
    private String recordPlaying = "";
    private int recordPlayingUpFor = 0;
    private boolean overlayMessageTinted = false;
    public float damageGuiPartialTime;
    float prevVignetteBrightness = 1.0F;

    public GuiIngame(Minecraft minecraft) {
        this.mc = minecraft;
    }

    public void renderGameOverlay(float tickDelta, boolean screenOpen, int mouseX, int mouseY) {
        ScaledResolution scaledresolution = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
        int i = scaledresolution.getScaledWidth();
        int j = scaledresolution.getScaledHeight();
        FontRenderer fontrenderer = this.mc.fontRenderer;
        this.mc.entityRenderer.setupGuiState();
        GL11.glEnable(3042);
        if (Minecraft.isFancyGraphicsEnabled()) {
            this.renderVignette(this.mc.thePlayer.getEntityBrightness(tickDelta), i, j);
        }

        ItemStack itemstack = this.mc.thePlayer.inventory.armorItemInSlot(3);
        if (!this.mc.gameSettings.thirdPersonView && itemstack != null && itemstack.itemID == Block.pumpkin.blockID) {
            this.renderPumpkinBlur(i, j);
        }

        float f = this.mc.thePlayer.prevTimeInPortal + (this.mc.thePlayer.timeInPortal - this.mc.thePlayer.prevTimeInPortal) * tickDelta;
        if (f > 0.0F) {
            this.renderPortalOverlay(f, i, j);
        }

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glBindTexture(3553, this.mc.renderEngine.getTexture("/gui/gui.png"));
        InventoryPlayer inventoryplayer = this.mc.thePlayer.inventory;
        this.zLevel = -90.0F;
        this.drawTexturedModalRect(i / 2 - 91, j - 22, 0, 0, 182, 22);
        this.drawTexturedModalRect(i / 2 - 91 - 1 + inventoryplayer.currentItem * 20, j - 22 - 1, 0, 22, 24, 22);
        GL11.glBindTexture(3553, this.mc.renderEngine.getTexture("/gui/icons.png"));
        GL11.glEnable(3042);
        GL11.glBlendFunc(775, 769);
        this.drawTexturedModalRect(i / 2 - 7, j / 2 - 7, 0, 0, 16, 16);
        GL11.glDisable(3042);
        boolean flag = this.mc.thePlayer.heartsLife / 3 % 2 == 1;
        if (this.mc.thePlayer.heartsLife < 10) {
            flag = false;
        }

        int k = this.mc.thePlayer.health;
        int l = this.mc.thePlayer.prevHealth;
        this.rand.setSeed(this.updateCounter * 312871);
        if (this.mc.playerController.shouldDrawHUD()) {
            int i1 = this.mc.thePlayer.getPlayerArmorValue();

            for (int j1 = 0; j1 < 10; j1++) {
                int k1 = j - 32;
                if (i1 > 0) {
                    int l1 = i / 2 + 91 - j1 * 8 - 9;
                    if (j1 * 2 + 1 < i1) {
                        this.drawTexturedModalRect(l1, k1, 34, 9, 9, 9);
                    }

                    if (j1 * 2 + 1 == i1) {
                        this.drawTexturedModalRect(l1, k1, 25, 9, 9, 9);
                    }

                    if (j1 * 2 + 1 > i1) {
                        this.drawTexturedModalRect(l1, k1, 16, 9, 9, 9);
                    }
                }

                byte b1 = 0;
                if (flag) {
                    b1 = 1;
                }

                int i2 = i / 2 - 91 + j1 * 8;
                if (k <= 4) {
                    k1 += this.rand.nextInt(2);
                }

                this.drawTexturedModalRect(i2, k1, 16 + b1 * 9, 0, 9, 9);
                if (flag) {
                    if (j1 * 2 + 1 < l) {
                        this.drawTexturedModalRect(i2, k1, 70, 0, 9, 9);
                    }

                    if (j1 * 2 + 1 == l) {
                        this.drawTexturedModalRect(i2, k1, 79, 0, 9, 9);
                    }
                }

                if (j1 * 2 + 1 < k) {
                    this.drawTexturedModalRect(i2, k1, 52, 0, 9, 9);
                }

                if (j1 * 2 + 1 == k) {
                    this.drawTexturedModalRect(i2, k1, 61, 0, 9, 9);
                }
            }

            if (this.mc.thePlayer.isInsideOfMaterial(Material.water)) {
                int l3 = (int)Math.ceil((this.mc.thePlayer.air - 2) * 10.0 / 300.0);
                int k4 = (int)Math.ceil(this.mc.thePlayer.air * 10.0 / 300.0) - l3;

                for (int i6 = 0; i6 < l3 + k4; i6++) {
                    if (i6 < l3) {
                        this.drawTexturedModalRect(i / 2 - 91 + i6 * 8, j - 32 - 9, 16, 18, 9, 9);
                    } else {
                        this.drawTexturedModalRect(i / 2 - 91 + i6 * 8, j - 32 - 9, 25, 18, 9, 9);
                    }
                }
            }
        }

        GL11.glDisable(3042);
        GL11.glEnable(32826);
        GL11.glPushMatrix();
        GL11.glRotatef(120.0F, 1.0F, 0.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GL11.glPopMatrix();

        for (int i3 = 0; i3 < 9; i3++) {
            int i4 = i / 2 - 90 + i3 * 20 + 2;
            int l4 = j - 16 - 3;
            this.renderInventorySlot(i3, i4, l4, tickDelta);
        }

        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(32826);
        if (this.mc.thePlayer.getSleepingTime() > 0) {
            GL11.glDisable(2929);
            GL11.glDisable(3008);
            int j3 = this.mc.thePlayer.getSleepingTime();
            float f2 = j3 / 100.0F;
            if (f2 > 1.0F) {
                f2 = 1.0F - (j3 - 100) / 10.0F;
            }

            int i5 = (int)(220.0F * f2) << 24 | 1052704;
            this.drawRect(0, 0, i, j, i5);
            GL11.glEnable(3008);
            GL11.glEnable(2929);
        }

        if (this.mc.gameSettings.showDebugInfo) {
            GL11.glPushMatrix();
            if (Minecraft.hasPaidCheckTime > 0L) {
                GL11.glTranslatef(0.0F, 32.0F, 0.0F);
            }

            fontrenderer.drawStringWithShadow("Minecraft Beta 1.7.3 (" + this.mc.debug + ")", 2, 2, 16777215);
            fontrenderer.drawStringWithShadow(this.mc.getRenderChunkDebugInfo(), 2, 12, 16777215);
            fontrenderer.drawStringWithShadow(this.mc.getRenderEntityDebugInfo(), 2, 22, 16777215);
            fontrenderer.drawStringWithShadow(this.mc.getWorldDebugInfo(), 2, 32, 16777215);
            fontrenderer.drawStringWithShadow(this.mc.getChunkSourceDebugInfo(), 2, 42, 16777215);
            long k3 = Runtime.getRuntime().maxMemory();
            long j5 = Runtime.getRuntime().totalMemory();
            long j6 = Runtime.getRuntime().freeMemory();
            long k2 = j5 - j6;
            String s = "Used memory: " + k2 * 100L / k3 + "% (" + k2 / 1024L / 1024L + "MB) of " + k3 / 1024L / 1024L + "MB";
            this.drawString(fontrenderer, s, i - fontrenderer.getStringWidth(s) - 2, 2, 14737632);
            s = "Allocated memory: " + j5 * 100L / k3 + "% (" + j5 / 1024L / 1024L + "MB)";
            this.drawString(fontrenderer, s, i - fontrenderer.getStringWidth(s) - 2, 12, 14737632);
            this.drawString(fontrenderer, "x: " + this.mc.thePlayer.posX, 2, 64, 14737632);
            this.drawString(fontrenderer, "y: " + this.mc.thePlayer.posY, 2, 72, 14737632);
            this.drawString(fontrenderer, "z: " + this.mc.thePlayer.posZ, 2, 80, 14737632);
            this.drawString(fontrenderer, "f: " + (MathHelper.floor_double(this.mc.thePlayer.rotationYaw * 4.0F / 360.0F + 0.5) & 3), 2, 88, 14737632);
            GL11.glPopMatrix();
        }

        if (this.recordPlayingUpFor > 0) {
            float f1 = this.recordPlayingUpFor - tickDelta;
            int j4 = (int)(f1 * 256.0F / 20.0F);
            if (j4 > 255) {
                j4 = 255;
            }

            if (j4 > 0) {
                GL11.glPushMatrix();
                GL11.glTranslatef(i / 2, j - 48, 0.0F);
                GL11.glEnable(3042);
                GL11.glBlendFunc(770, 771);
                int k5 = 16777215;
                if (this.overlayMessageTinted) {
                    k5 = Color.HSBtoRGB(f1 / 50.0F, 0.7F, 0.6F) & 16777215;
                }

                fontrenderer.drawString(this.recordPlaying, -fontrenderer.getStringWidth(this.recordPlaying) / 2, -4, k5 + (j4 << 24));
                GL11.glDisable(3042);
                GL11.glPopMatrix();
            }
        }

        byte b0 = 10;
        boolean flag1 = false;
        if (this.mc.currentScreen instanceof GuiChat) {
            b0 = 20;
            flag1 = true;
        }

        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3008);
        GL11.glPushMatrix();
        GL11.glTranslatef(0.0F, j - 48, 0.0F);

        for (int l5 = 0; l5 < this.chatMessageList.size() && l5 < b0; l5++) {
            if (this.chatMessageList.get(l5).updateCounter < 200 || flag1) {
                double d0 = this.chatMessageList.get(l5).updateCounter / 200.0;
                d0 = 1.0 - d0;
                d0 *= 10.0;
                if (d0 < 0.0) {
                    d0 = 0.0;
                }

                if (d0 > 1.0) {
                    d0 = 1.0;
                }

                d0 *= d0;
                int j2 = (int)(255.0 * d0);
                if (flag1) {
                    j2 = 255;
                }

                if (j2 > 0) {
                    byte b2 = 2;
                    int l2 = -l5 * 9;
                    String s1 = this.chatMessageList.get(l5).message;
                    this.drawRect(b2, l2 - 1, b2 + 320, l2 + 8, j2 / 2 << 24);
                    GL11.glEnable(3042);
                    fontrenderer.drawStringWithShadow(s1, b2, l2, 16777215 + (j2 << 24));
                }
            }
        }

        GL11.glPopMatrix();
        GL11.glEnable(3008);
        GL11.glDisable(3042);
    }

    private void renderPumpkinBlur(int width, int height) {
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glBlendFunc(770, 771);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(3008);
        GL11.glBindTexture(3553, this.mc.renderEngine.getTexture("%blur%/misc/pumpkinblur.png"));
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(0.0, height, -90.0, 0.0, 1.0);
        tessellator.addVertexWithUV(width, height, -90.0, 1.0, 1.0);
        tessellator.addVertexWithUV(width, 0.0, -90.0, 1.0, 0.0);
        tessellator.addVertexWithUV(0.0, 0.0, -90.0, 0.0, 0.0);
        tessellator.draw();
        GL11.glDepthMask(true);
        GL11.glEnable(2929);
        GL11.glEnable(3008);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    private void renderVignette(float brightnessAtEyes, int width, int height) {
        brightnessAtEyes = 1.0F - brightnessAtEyes;
        if (brightnessAtEyes < 0.0F) {
            brightnessAtEyes = 0.0F;
        }

        if (brightnessAtEyes > 1.0F) {
            brightnessAtEyes = 1.0F;
        }

        this.prevVignetteBrightness = (float)(this.prevVignetteBrightness + (brightnessAtEyes - this.prevVignetteBrightness) * 0.01);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glBlendFunc(0, 769);
        GL11.glColor4f(this.prevVignetteBrightness, this.prevVignetteBrightness, this.prevVignetteBrightness, 1.0F);
        GL11.glBindTexture(3553, this.mc.renderEngine.getTexture("%blur%/misc/vignette.png"));
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(0.0, height, -90.0, 0.0, 1.0);
        tessellator.addVertexWithUV(width, height, -90.0, 1.0, 1.0);
        tessellator.addVertexWithUV(width, 0.0, -90.0, 1.0, 0.0);
        tessellator.addVertexWithUV(0.0, 0.0, -90.0, 0.0, 0.0);
        tessellator.draw();
        GL11.glDepthMask(true);
        GL11.glEnable(2929);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glBlendFunc(770, 771);
    }

    private void renderPortalOverlay(float portalTime, int width, int height) {
        if (portalTime < 1.0F) {
            portalTime *= portalTime;
            portalTime *= portalTime;
            portalTime = portalTime * 0.8F + 0.2F;
        }

        GL11.glDisable(3008);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glBlendFunc(770, 771);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, portalTime);
        GL11.glBindTexture(3553, this.mc.renderEngine.getTexture("/terrain.png"));
        float f = Block.portal.blockIndexInTexture % 16 / 16.0F;
        float f1 = Block.portal.blockIndexInTexture / 16 / 16.0F;
        float f2 = (Block.portal.blockIndexInTexture % 16 + 1) / 16.0F;
        float f3 = (Block.portal.blockIndexInTexture / 16 + 1) / 16.0F;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(0.0, height, -90.0, f, f3);
        tessellator.addVertexWithUV(width, height, -90.0, f2, f3);
        tessellator.addVertexWithUV(width, 0.0, -90.0, f2, f1);
        tessellator.addVertexWithUV(0.0, 0.0, -90.0, f, f1);
        tessellator.draw();
        GL11.glDepthMask(true);
        GL11.glEnable(2929);
        GL11.glEnable(3008);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    private void renderInventorySlot(int slot, int x, int z, float tickDelta) {
        ItemStack itemStack = this.mc.thePlayer.inventory.mainInventory[slot];
        if (itemStack == null) {
            return;
        }
        float f = (float) itemStack.animationsToGo - tickDelta;
        if (f > 0.0f) {
            GL11.glPushMatrix();
            float f2 = 1.0f + f / 5.0f;
            GL11.glTranslatef(x + 8, z + 12, 0.0f);
            GL11.glScalef(1.0f / f2, (f2 + 1.0f) / 2.0f, 1.0f);
            GL11.glTranslatef(-(x + 8), -(z + 12), 0.0f);
        }
        itemRenderer.renderItemIntoGUI(this.mc.fontRenderer, this.mc.renderEngine, itemStack, x, z);
        if (f > 0.0f) {
            GL11.glPopMatrix();
        }
        itemRenderer.renderItemOverlayIntoGUI(this.mc.fontRenderer, this.mc.renderEngine, itemStack, x, z);
    }

    public void updateTick() {
        if (this.recordPlayingUpFor > 0) {
            this.recordPlayingUpFor--;
        }

        this.updateCounter++;

        for (int i = 0; i < this.chatMessageList.size(); i++) {
            this.chatMessageList.get(i).updateCounter++;
        }
    }

    public void clearChatMessages() {
        this.chatMessageList.clear();
    }

    public void addChatMessage(String text) {
        while (this.mc.fontRenderer.getStringWidth(text) > 320) {
            int i = 1;

            while (i < text.length() && this.mc.fontRenderer.getStringWidth(text.substring(0, i + 1)) <= 320) {
                i++;
            }

            this.addChatMessage(text.substring(0, i));
            text = text.substring(i);
        }

        this.chatMessageList.add(0, new ChatLine(text));

        while (this.chatMessageList.size() > 50) {
            this.chatMessageList.remove(this.chatMessageList.size() - 1);
        }
    }

    public void setRecordPlayingMessage(String record) {
        this.recordPlaying = "Now playing: " + record;
        this.recordPlayingUpFor = 60;
        this.overlayMessageTinted = true;
    }

    public void addChatMessageTranslate(String s) {
        StringTranslate stringtranslate = StringTranslate.getInstance();
        String sx = stringtranslate.translateKey(s);
        this.addChatMessage(sx);
    }
}
