package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class GuiChest
extends GuiContainer {
    private IInventory upperChestInventory;
    private IInventory lowerChestInventory;
    private int inventoryRows = 0;

    public GuiChest(IInventory playerInventory, IInventory inventory) {
        super(new ContainerChest(playerInventory, inventory));
        this.upperChestInventory = playerInventory;
        this.lowerChestInventory = inventory;
        this.passEvents = false;
        int n = 222;
        int n2 = n - 108;
        this.inventoryRows = inventory.getSizeInventory() / 9;
        this.ySize = n2 + this.inventoryRows * 18;
    }

    protected void drawGuiContainerForegroundLayer() {
        this.fontRenderer.drawString(this.lowerChestInventory.getInvName(), 8, 6, 0x404040);
        this.fontRenderer.drawString(this.upperChestInventory.getInvName(), 8, this.ySize - 96 + 2, 0x404040);
    }

    protected void drawGuiContainerBackgroundLayer(float tickDelta) {
        int n = this.mc.renderEngine.getTexture("/gui/container.png");
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.renderEngine.bindTexture(n);
        int n2 = (this.width - this.xSize) / 2;
        int n3 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(n2, n3, 0, 0, this.xSize, this.inventoryRows * 18 + 17);
        this.drawTexturedModalRect(n2, n3 + this.inventoryRows * 18 + 17, 0, 126, this.xSize, 96);
    }
}

