package net.minecraft.src;


public class ContainerFurnace
extends Container {
    private TileEntityFurnace furnace;
    private int cookTime = 0;
    private int burnTime = 0;
    private int itemBurnTime = 0;

    public ContainerFurnace(InventoryPlayer playerInventory, TileEntityFurnace furnace) {
        int n;
        this.furnace = furnace;
        this.addSlot(new Slot(furnace, 0, 56, 17));
        this.addSlot(new Slot(furnace, 1, 56, 53));
        this.addSlot(new SlotFurnace(playerInventory.player, furnace, 2, 116, 35));
        for (n = 0; n < 3; ++n) {
            for (int i = 0; i < 9; ++i) {
                this.addSlot(new Slot(playerInventory, i + n * 9 + 9, 8 + i * 18, 84 + n * 18));
            }
        }
        for (n = 0; n < 9; ++n) {
            this.addSlot(new Slot(playerInventory, n, 8 + n * 18, 142));
        }
    }

    @Override
public void updateCraftingResults() {
    super.updateCraftingResults();
    for (int i = 0; i < this.listeners.size(); i++) {
        ICrafting icrafting = this.listeners.get(i);
        if (this.cookTime != this.furnace.furnaceCookTime) {
            icrafting.onDataChanged(this, 0, this.furnace.furnaceCookTime);
        }
        if (this.burnTime != this.furnace.furnaceBurnTime) {
            icrafting.onDataChanged(this, 1, this.furnace.furnaceBurnTime);
        }
        if (this.itemBurnTime != this.furnace.currentItemBurnTime) {
            icrafting.onDataChanged(this, 2, this.furnace.currentItemBurnTime);
        }
    }
    this.cookTime = this.furnace.furnaceCookTime;
    this.burnTime = this.furnace.furnaceBurnTime;
    this.itemBurnTime = this.furnace.currentItemBurnTime;
}

    public void setData(int id, int value) {
        if (id == 0) {
            this.furnace.furnaceCookTime = value;
        }
        if (id == 1) {
            this.furnace.furnaceBurnTime = value;
        }
        if (id == 2) {
            this.furnace.currentItemBurnTime = value;
        }
    }

    public boolean isUsableByPlayer(EntityPlayer player) {
        return this.furnace.canInteractWith(player);
    }

    public ItemStack getStackInSlot(int slot) {
        ItemStack itemStack = null;
        Slot slot2 = (Slot)this.slots.get(slot);
        if (slot2 != null && slot2.getHasStack()) {
            ItemStack itemStack2 = slot2.getStack();
            itemStack = itemStack2.copy();
            if (slot == 2) {
                this.moveItem(itemStack2, 3, 39, true);
            } else if (slot >= 3 && slot < 30) {
                this.moveItem(itemStack2, 30, 39, false);
            } else if (slot >= 30 && slot < 39) {
                this.moveItem(itemStack2, 3, 30, false);
            } else {
                this.moveItem(itemStack2, 3, 39, false);
            }
            if (itemStack2.stackSize == 0) {
                slot2.putStack(null);
            } else {
                slot2.onSlotChanged();
            }
            if (itemStack2.stackSize != itemStack.stackSize) {
                slot2.onPickupFromSlot(itemStack2);
            } else {
                return null;
            }
        }
        return itemStack;
    }
}

