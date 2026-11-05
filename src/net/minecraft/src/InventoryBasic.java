package net.minecraft.src;

import java.util.List;

public class InventoryBasic
implements IInventory {
    private String inventoryTitle;
    private int slotsCount;
    private ItemStack[] inventoryContents;
    private List<IInvBasic> listeners;

    public InventoryBasic(String name, int size) {
        this.inventoryTitle = name;
        this.slotsCount = size;
        this.inventoryContents = new ItemStack[size];
    }

    public ItemStack getStackInSlot(int slot) {
        return this.inventoryContents[slot];
    }

    public ItemStack decrStackSize(int slot, int amount) {
        if (this.inventoryContents[slot] != null) {
            if (this.inventoryContents[slot].stackSize <= amount) {
                ItemStack itemStack = this.inventoryContents[slot];
                this.inventoryContents[slot] = null;
                this.onInventoryChanged();
                return itemStack;
            }
            ItemStack itemStack = this.inventoryContents[slot].splitStack(amount);
            if (this.inventoryContents[slot].stackSize == 0) {
                this.inventoryContents[slot] = null;
            }
            this.onInventoryChanged();
            return itemStack;
        }
        return null;
    }

    public void setInventorySlotContents(int slot, ItemStack item) {
        this.inventoryContents[slot] = item;
        if (item != null && item.stackSize > this.getInventoryStackLimit()) {
            item.stackSize = this.getInventoryStackLimit();
        }
        this.onInventoryChanged();
    }

    public int getSizeInventory() {
        return this.slotsCount;
    }

    public String getInvName() {
        return this.inventoryTitle;
    }

    public int getInventoryStackLimit() {
        return 64;
    }

    public void onInventoryChanged() {
        if (this.listeners != null) {
            for (int i = 0; i < this.listeners.size(); ++i) {
                this.listeners.get(i).onInventoryChanged(this);
            }
        }
    }

    public boolean canInteractWith(EntityPlayer player) {
        return true;
    }
}

