package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class GuiFurnace
extends GuiContainer {
    private TileEntityFurnace furnaceInventory;

    public GuiFurnace(InventoryPlayer playerInventory, TileEntityFurnace furnace) {
        super(new ContainerFurnace(playerInventory, furnace));
        this.furnaceInventory = furnace;
    }

    protected void drawGuiContainerForegroundLayer() {
        this.fontRenderer.drawString("Furnace", 60, 6, 0x404040);
        this.fontRenderer.drawString("Inventory", 8, this.ySize - 96 + 2, 0x404040);
    }

    protected void drawGuiContainerBackgroundLayer(float tickDelta) {
        int n;
        int n2 = this.mc.renderEngine.getTexture("/gui/furnace.png");
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.renderEngine.bindTexture(n2);
        int n3 = (this.width - this.xSize) / 2;
        int n4 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(n3, n4, 0, 0, this.xSize, this.ySize);
        if (this.furnaceInventory.isBurning()) {
            n = this.furnaceInventory.getBurnTimeRemainingScaled(12);
            this.drawTexturedModalRect(n3 + 56, n4 + 36 + 12 - n, 176, 12 - n, 14, n + 2);
        }
        n = this.furnaceInventory.getCookProgressScaled(24);
        this.drawTexturedModalRect(n3 + 79, n4 + 34, 176, 14, n + 1, 16);
    }
}

