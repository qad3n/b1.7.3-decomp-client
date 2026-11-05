package net.minecraft.src;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class Container {
    public List<ItemStack> items = new ArrayList<>();
    public List<Slot> slots = new ArrayList<>();
    public int windowId = 0;
    private short nextInteractionId = 0;
    protected List<ICrafting> listeners = new ArrayList<>();
    private Set<EntityPlayer> restrictedPlayers = new HashSet<>();

    protected void addSlot(Slot slot) {
        slot.slotNumber = this.slots.size();
        this.slots.add(slot);
        this.items.add(null);
    }

    public void updateCraftingResults() {
        for (int i = 0; i < this.slots.size(); i++) {
            ItemStack itemstack = this.slots.get(i).getStack();
            ItemStack itemstack1 = this.items.get(i);
            if (!ItemStack.areItemStacksEqual(itemstack1, itemstack)) {
                itemstack1 = itemstack == null ? null : itemstack.copy();
                this.items.set(i, itemstack1);

                for (int j = 0; j < this.listeners.size(); j++) {
                    this.listeners.get(j).onSlotChanged(this, i, itemstack1);
                }
            }
        }
    }

    public Slot getSlot(int slot) {
        return this.slots.get(slot);
    }

    public ItemStack getStackInSlot(int slot) {
        Slot slot2 = this.slots.get(slot);
        if (slot2 != null) {
            return slot2.getStack();
        }
        return null;
    }

    public ItemStack onClickSlot(int slot, int button, boolean quickMove, EntityPlayer player) {
        ItemStack itemstack = null;
        if (button == 0 || button == 1) {
            InventoryPlayer inventoryplayer = player.inventory;
            if (slot == -999) {
                if (inventoryplayer.getItemStack() != null && slot == -999) {
                    if (button == 0) {
                        player.dropPlayerItem(inventoryplayer.getItemStack());
                        inventoryplayer.setItemStack(null);
                    }

                    if (button == 1) {
                        player.dropPlayerItem(inventoryplayer.getItemStack().splitStack(1));
                        if (inventoryplayer.getItemStack().stackSize == 0) {
                            inventoryplayer.setItemStack(null);
                        }
                    }
                }
            } else if (quickMove) {
                ItemStack itemstack1 = this.getStackInSlot(slot);
                if (itemstack1 != null) {
                    int i = itemstack1.stackSize;
                    itemstack = itemstack1.copy();
                    Slot slotx = this.slots.get(slot);
                    if (slotx != null && slotx.getStack() != null) {
                        int j = slotx.getStack().stackSize;
                        if (j < i) {
                            this.onClickSlot(slot, button, quickMove, player);
                        }
                    }
                }
            } else {
                Slot slot1 = this.slots.get(slot);
                if (slot1 != null) {
                    slot1.onSlotChanged();
                    ItemStack itemstack3 = slot1.getStack();
                    ItemStack itemstack4 = inventoryplayer.getItemStack();
                    if (itemstack3 != null) {
                        itemstack = itemstack3.copy();
                    }

                    if (itemstack3 == null) {
                        if (itemstack4 != null && slot1.isItemValid(itemstack4)) {
                            int k = button == 0 ? itemstack4.stackSize : 1;
                            if (k > slot1.getSlotStackLimit()) {
                                k = slot1.getSlotStackLimit();
                            }

                            slot1.putStack(itemstack4.splitStack(k));
                            if (itemstack4.stackSize == 0) {
                                inventoryplayer.setItemStack(null);
                            }
                        }
                    } else if (itemstack4 == null) {
                        int l = button == 0 ? itemstack3.stackSize : (itemstack3.stackSize + 1) / 2;
                        ItemStack itemstack2 = slot1.decrStackSize(l);
                        inventoryplayer.setItemStack(itemstack2);
                        if (itemstack3.stackSize == 0) {
                            slot1.putStack(null);
                        }

                        slot1.onPickupFromSlot(inventoryplayer.getItemStack());
                    } else if (slot1.isItemValid(itemstack4)) {
                        if (itemstack3.itemID != itemstack4.itemID || itemstack3.getHasSubtypes() && itemstack3.getItemDamage() != itemstack4.getItemDamage()) {
                            if (itemstack4.stackSize <= slot1.getSlotStackLimit()) {
                                ItemStack itemstack5 = itemstack3;
                                slot1.putStack(itemstack4);
                                inventoryplayer.setItemStack(itemstack5);
                            }
                        } else {
                            int i1 = button == 0 ? itemstack4.stackSize : 1;
                            if (i1 > slot1.getSlotStackLimit() - itemstack3.stackSize) {
                                i1 = slot1.getSlotStackLimit() - itemstack3.stackSize;
                            }

                            if (i1 > itemstack4.getMaxStackSize() - itemstack3.stackSize) {
                                i1 = itemstack4.getMaxStackSize() - itemstack3.stackSize;
                            }

                            itemstack4.splitStack(i1);
                            if (itemstack4.stackSize == 0) {
                                inventoryplayer.setItemStack(null);
                            }

                            itemstack3.stackSize += i1;
                        }
                    } else if (itemstack3.itemID == itemstack4.itemID
                        && itemstack4.getMaxStackSize() > 1
                        && (!itemstack3.getHasSubtypes() || itemstack3.getItemDamage() == itemstack4.getItemDamage())) {
                        int j1 = itemstack3.stackSize;
                        if (j1 > 0 && j1 + itemstack4.stackSize <= itemstack4.getMaxStackSize()) {
                            itemstack4.stackSize += j1;
                            itemstack3.splitStack(j1);
                            if (itemstack3.stackSize == 0) {
                                slot1.putStack(null);
                            }

                            slot1.onPickupFromSlot(inventoryplayer.getItemStack());
                        }
                    }
                }
            }
        }

        return itemstack;
    }

    public void onCraftGuiClosed(EntityPlayer player) {
        InventoryPlayer inventoryplayer = player.inventory;
        if (inventoryplayer.getItemStack() != null) {
            player.dropPlayerItem(inventoryplayer.getItemStack());
            inventoryplayer.setItemStack(null);
        }
    }

    public void onCraftMatrixChanged(IInventory inventory) {
        this.updateCraftingResults();
    }

    public void putStackInSlot(int slot, ItemStack item) {
        this.getSlot(slot).putStack(item);
    }

    public void putStacksInSlots(ItemStack[] items) {
        for (int i = 0; i < items.length; i++) {
            this.getSlot(i).putStack(items[i]);
        }
    }

    public void setData(int id, int value) {
    }

    public short nextInteractionId(InventoryPlayer inventory) {
        this.nextInteractionId++;
        return this.nextInteractionId;
    }

    public void deleteBackup(short id) {
    }

    public void rollbackToBackup(short id) {
    }

    public abstract boolean isUsableByPlayer(EntityPlayer player);

    protected void moveItem(ItemStack item, int from, int to, boolean reverse) {
        int i = from;
        if (reverse) {
            i = to - 1;
        }

        if (item.isStackable()) {
            while (item.stackSize > 0 && (!reverse && i < to || reverse && i >= from)) {
                Slot slot = this.slots.get(i);
                ItemStack itemstack = slot.getStack();
                if (itemstack != null && itemstack.itemID == item.itemID && (!item.getHasSubtypes() || item.getItemDamage() == itemstack.getItemDamage())) {
                    int j = itemstack.stackSize + item.stackSize;
                    if (j <= item.getMaxStackSize()) {
                        item.stackSize = 0;
                        itemstack.stackSize = j;
                        slot.onSlotChanged();
                    } else if (itemstack.stackSize < item.getMaxStackSize()) {
                        item.stackSize = item.stackSize - (item.getMaxStackSize() - itemstack.stackSize);
                        itemstack.stackSize = item.getMaxStackSize();
                        slot.onSlotChanged();
                    }
                }

                if (reverse) {
                    i--;
                } else {
                    i++;
                }
            }
        }

        if (item.stackSize > 0) {
            if (reverse) {
                i = to - 1;
            } else {
                i = from;
            }

            while (!reverse && i < to || reverse && i >= from) {
                Slot slot1 = this.slots.get(i);
                ItemStack itemstack1 = slot1.getStack();
                if (itemstack1 == null) {
                    slot1.putStack(item.copy());
                    slot1.onSlotChanged();
                    item.stackSize = 0;
                    break;
                }

                if (reverse) {
                    i--;
                } else {
                    i++;
                }
            }
        }
    }
}
