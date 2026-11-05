package net.minecraft.src;


public class Slot {
    private final int slotIndex;
    private final IInventory inventory;
    public int slotNumber;
    public int xDisplayPosition;
    public int yDisplayPosition;

    public Slot(IInventory inventory, int id, int x, int y) {
        this.inventory = inventory;
        this.slotIndex = id;
        this.xDisplayPosition = x;
        this.yDisplayPosition = y;
    }

    public void onPickupFromSlot(ItemStack item) {
        this.onSlotChanged();
    }

    public boolean isItemValid(ItemStack item) {
        return true;
    }

    public ItemStack getStack() {
        return this.inventory.getStackInSlot(this.slotIndex);
    }

    public boolean getHasStack() {
        return this.getStack() != null;
    }

    public void putStack(ItemStack item) {
        this.inventory.setInventorySlotContents(this.slotIndex, item);
        this.onSlotChanged();
    }

    public void onSlotChanged() {
        this.inventory.onInventoryChanged();
    }

    public int getSlotStackLimit() {
        return this.inventory.getInventoryStackLimit();
    }

    public int getBackgroundIconIndex() {
        return -1;
    }

    public ItemStack decrStackSize(int amount) {
        return this.inventory.decrStackSize(this.slotIndex, amount);
    }
}

