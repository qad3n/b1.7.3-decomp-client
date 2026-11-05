package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class GuiCrafting
extends GuiContainer {
    public GuiCrafting(InventoryPlayer playerInventory, World world, int x, int y, int z) {
        super(new ContainerWorkbench(playerInventory, world, x, y, z));
    }

    public void onGuiClosed() {
        super.onGuiClosed();
        this.inventorySlots.onCraftGuiClosed(this.mc.thePlayer);
    }

    protected void drawGuiContainerForegroundLayer() {
        this.fontRenderer.drawString("Crafting", 28, 6, 0x404040);
        this.fontRenderer.drawString("Inventory", 8, this.ySize - 96 + 2, 0x404040);
    }

    protected void drawGuiContainerBackgroundLayer(float tickDelta) {
        int n = this.mc.renderEngine.getTexture("/gui/crafting.png");
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.renderEngine.bindTexture(n);
        int n2 = (this.width - this.xSize) / 2;
        int n3 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(n2, n3, 0, 0, this.xSize, this.ySize);
    }
}

