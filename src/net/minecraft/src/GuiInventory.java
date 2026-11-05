package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class GuiInventory
extends GuiContainer {
    private float xSize_lo;
    private float ySize_lo;

    public GuiInventory(EntityPlayer player) {
        super(player.inventorySlots);
        this.passEvents = true;
        player.addStat(AchievementList.openInventory, 1);
    }

    public void initGui() {
        this.controlList.clear();
    }

    protected void drawGuiContainerForegroundLayer() {
        this.fontRenderer.drawString("Crafting", 86, 16, 0x404040);
    }

    public void drawScreen(int mouseX, int mouseY, float tickDelta) {
        super.drawScreen(mouseX, mouseY, tickDelta);
        this.xSize_lo = mouseX;
        this.ySize_lo = mouseY;
    }

    protected void drawGuiContainerBackgroundLayer(float tickDelta) {
        int n = this.mc.renderEngine.getTexture("/gui/inventory.png");
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.renderEngine.bindTexture(n);
        int n2 = (this.width - this.xSize) / 2;
        int n3 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(n2, n3, 0, 0, this.xSize, this.ySize);
        GL11.glEnable(32826);
        GL11.glEnable(2903);
        GL11.glPushMatrix();
        GL11.glTranslatef(n2 + 51, n3 + 75, 50.0f);
        float f = 30.0f;
        GL11.glScalef(-f, f, f);
        GL11.glRotatef(180.0f, 0.0f, 0.0f, 1.0f);
        float f2 = this.mc.thePlayer.renderYawOffset;
        float f3 = this.mc.thePlayer.rotationYaw;
        float f4 = this.mc.thePlayer.rotationPitch;
        float f5 = (float)(n2 + 51) - this.xSize_lo;
        float f6 = (float)(n3 + 75 - 50) - this.ySize_lo;
        GL11.glRotatef(135.0f, 0.0f, 1.0f, 0.0f);
        RenderHelper.enableStandardItemLighting();
        GL11.glRotatef(-135.0f, 0.0f, 1.0f, 0.0f);
        GL11.glRotatef(-((float)Math.atan(f6 / 40.0f)) * 20.0f, 1.0f, 0.0f, 0.0f);
        this.mc.thePlayer.renderYawOffset = (float)Math.atan(f5 / 40.0f) * 20.0f;
        this.mc.thePlayer.rotationYaw = (float)Math.atan(f5 / 40.0f) * 40.0f;
        this.mc.thePlayer.rotationPitch = -((float)Math.atan(f6 / 40.0f)) * 20.0f;
        this.mc.thePlayer.entityBrightness = 1.0f;
        GL11.glTranslatef(0.0f, this.mc.thePlayer.yOffset, 0.0f);
        RenderManager.instance.playerViewY = 180.0f;
        RenderManager.instance.renderEntityWithPosYaw(this.mc.thePlayer, 0.0, 0.0, 0.0, 0.0f, 1.0f);
        this.mc.thePlayer.entityBrightness = 0.0f;
        this.mc.thePlayer.renderYawOffset = f2;
        this.mc.thePlayer.rotationYaw = f3;
        this.mc.thePlayer.rotationPitch = f4;
        GL11.glPopMatrix();
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(32826);
    }

    protected void actionPerformed(GuiButton button) {
        if (button.id == 0) {
            this.mc.displayGuiScreen(new GuiAchievements(this.mc.statFileWriter));
        }
        if (button.id == 1) {
            this.mc.displayGuiScreen(new GuiStats(this, this.mc.statFileWriter));
        }
    }
}

