package net.minecraft.src;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public abstract class GuiContainer extends GuiScreen {
    private static RenderItem itemRenderer = new RenderItem();
    protected int xSize = 176;
    protected int ySize = 166;
    public Container inventorySlots;

    public GuiContainer(Container menu) {
        this.inventorySlots = menu;
    }

    @Override
    public void initGui() {
        super.initGui();
        this.mc.thePlayer.craftingInventory = this.inventorySlots;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float tickDelta) {
        this.drawDefaultBackground();
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawGuiContainerBackgroundLayer(tickDelta);
        GL11.glPushMatrix();
        GL11.glRotatef(120.0F, 1.0F, 0.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glTranslatef(i, j, 0.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(32826);
        Slot slot = null;

        for (int k = 0; k < this.inventorySlots.slots.size(); k++) {
            Slot slot1 = this.inventorySlots.slots.get(k);
            this.drawSlotInventory(slot1);
            if (this.getIsMouseOverSlot(slot1, mouseX, mouseY)) {
                slot = slot1;
                GL11.glDisable(2896);
                GL11.glDisable(2929);
                int l = slot1.xDisplayPosition;
                int i1 = slot1.yDisplayPosition;
                this.drawGradientRect(l, i1, l + 16, i1 + 16, -2130706433, -2130706433);
                GL11.glEnable(2896);
                GL11.glEnable(2929);
            }
        }

        InventoryPlayer inventoryplayer = this.mc.thePlayer.inventory;
        if (inventoryplayer.getItemStack() != null) {
            GL11.glTranslatef(0.0F, 0.0F, 32.0F);
            itemRenderer.renderItemIntoGUI(this.fontRenderer, this.mc.renderEngine, inventoryplayer.getItemStack(), mouseX - i - 8, mouseY - j - 8);
            itemRenderer.renderItemOverlayIntoGUI(this.fontRenderer, this.mc.renderEngine, inventoryplayer.getItemStack(), mouseX - i - 8, mouseY - j - 8);
        }

        GL11.glDisable(32826);
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(2896);
        GL11.glDisable(2929);
        this.drawGuiContainerForegroundLayer();
        if (inventoryplayer.getItemStack() == null && slot != null && slot.getHasStack()) {
            String s = ("" + StringTranslate.getInstance().translateNamedKey(slot.getStack().getItemName())).trim();
            if (s.length() > 0) {
                int k1 = mouseX - i + 12;
                int l1 = mouseY - j - 12;
                int j1 = this.fontRenderer.getStringWidth(s);
                this.drawGradientRect(k1 - 3, l1 - 3, k1 + j1 + 3, l1 + 8 + 3, -1073741824, -1073741824);
                this.fontRenderer.drawStringWithShadow(s, k1, l1, -1);
            }
        }

        GL11.glPopMatrix();
        super.drawScreen(mouseX, mouseY, tickDelta);
        GL11.glEnable(2896);
        GL11.glEnable(2929);
    }

    protected void drawGuiContainerForegroundLayer() {
    }

    protected abstract void drawGuiContainerBackgroundLayer(float tickDelta);

    private void drawSlotInventory(Slot slot) {
        int i = slot.xDisplayPosition;
        int j = slot.yDisplayPosition;
        ItemStack itemstack = slot.getStack();
        if (itemstack == null) {
            int k = slot.getBackgroundIconIndex();
            if (k >= 0) {
                GL11.glDisable(2896);
                this.mc.renderEngine.bindTexture(this.mc.renderEngine.getTexture("/gui/items.png"));
                this.drawTexturedModalRect(i, j, k % 16 * 16, k / 16 * 16, 16, 16);
                GL11.glEnable(2896);
                return;
            }
        }

        itemRenderer.renderItemIntoGUI(this.fontRenderer, this.mc.renderEngine, itemstack, i, j);
        itemRenderer.renderItemOverlayIntoGUI(this.fontRenderer, this.mc.renderEngine, itemstack, i, j);
    }

    private Slot getSlotAtPosition(int x, int y) {
        for (int i = 0; i < this.inventorySlots.slots.size(); i++) {
            Slot slot = this.inventorySlots.slots.get(i);
            if (this.getIsMouseOverSlot(slot, x, y)) {
                return slot;
            }
        }

        return null;
    }

    private boolean getIsMouseOverSlot(Slot slot, int mouseX, int mouseY) {
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        mouseX -= i;
        mouseY -= j;
        return mouseX >= slot.xDisplayPosition - 1
            && mouseX < slot.xDisplayPosition + 16 + 1
            && mouseY >= slot.yDisplayPosition - 1
            && mouseY < slot.yDisplayPosition + 16 + 1;
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int button) {
        super.mouseClicked(mouseX, mouseY, button);
        if (button == 0 || button == 1) {
            Slot slot = this.getSlotAtPosition(mouseX, mouseY);
            int i = (this.width - this.xSize) / 2;
            int j = (this.height - this.ySize) / 2;
            boolean flag = mouseX < i || mouseY < j || mouseX >= i + this.xSize || mouseY >= j + this.ySize;
            int k = -1;
            if (slot != null) {
                k = slot.slotNumber;
            }

            if (flag) {
                k = -999;
            }

            if (k != -1) {
                boolean flag1 = k != -999 && (Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54));
                this.mc.playerController.clickSlot(this.inventorySlots.windowId, k, button, flag1, this.mc.thePlayer);
            }
        }
    }

    @Override
    protected void mouseMovedOrUp(int mouseX, int mouseY, int button) {
        if (button == 0) {
        }
    }

    @Override
    protected void keyTyped(char chr, int key) {
        if (key == 1 || key == this.mc.gameSettings.keyBindInventory.keyCode) {
            this.mc.thePlayer.closeScreen();
        }
    }

    @Override
public void onGuiClosed() {
    if (this.mc.thePlayer == null) {
        return;
    }
    this.mc.playerController.closeMenu(this.inventorySlots.windowId, this.mc.thePlayer);
}

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        if (!this.mc.thePlayer.isEntityAlive() || this.mc.thePlayer.isDead) {
            this.mc.thePlayer.closeScreen();
        }
    }
}
