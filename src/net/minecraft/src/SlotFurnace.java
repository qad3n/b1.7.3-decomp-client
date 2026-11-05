package net.minecraft.src;


public class SlotFurnace
extends Slot {
    private EntityPlayer thePlayer;

    public SlotFurnace(EntityPlayer player, IInventory resultInventory, int slot, int x, int y) {
        super(resultInventory, slot, x, y);
        this.thePlayer = player;
    }

    public boolean isItemValid(ItemStack item) {
        return false;
    }

    public void onPickupFromSlot(ItemStack item) {
        item.onCrafting(this.thePlayer.worldObj, this.thePlayer);
        if (item.itemID == Item.ingotIron.shiftedIndex) {
            this.thePlayer.addStat(AchievementList.acquireIron, 1);
        }
        if (item.itemID == Item.fishCooked.shiftedIndex) {
            this.thePlayer.addStat(AchievementList.cookFish, 1);
        }
        super.onPickupFromSlot(item);
    }
}

