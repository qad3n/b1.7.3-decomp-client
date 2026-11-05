package net.minecraft.src;

public class SlotCrafting extends Slot {
    private final IInventory craftMatrix;
    private EntityPlayer thePlayer;

    public SlotCrafting(EntityPlayer player, IInventory craftingInventory, IInventory resultInventory, int slot, int x, int y) {
        super(resultInventory, slot, x, y);
        this.thePlayer = player;
        this.craftMatrix = craftingInventory;
    }

    @Override
    public boolean isItemValid(ItemStack item) {
        return false;
    }

    @Override
    public void onPickupFromSlot(ItemStack item) {
        item.onCrafting(this.thePlayer.worldObj, this.thePlayer);
        if (item.itemID == Block.workbench.blockID) {
            this.thePlayer.addStat(AchievementList.buildWorkBench, 1);
        } else if (item.itemID == Item.pickaxeWood.shiftedIndex) {
            this.thePlayer.addStat(AchievementList.buildPickaxe, 1);
        } else if (item.itemID == Block.stoneOvenIdle.blockID) {
            this.thePlayer.addStat(AchievementList.buildFurnace, 1);
        } else if (item.itemID == Item.hoeWood.shiftedIndex) {
            this.thePlayer.addStat(AchievementList.buildHoe, 1);
        } else if (item.itemID == Item.bread.shiftedIndex) {
            this.thePlayer.addStat(AchievementList.makeBread, 1);
        } else if (item.itemID == Item.cake.shiftedIndex) {
            this.thePlayer.addStat(AchievementList.bakeCake, 1);
        } else if (item.itemID == Item.pickaxeStone.shiftedIndex) {
            this.thePlayer.addStat(AchievementList.buildBetterPickaxe, 1);
        } else if (item.itemID == Item.swordWood.shiftedIndex) {
            this.thePlayer.addStat(AchievementList.buildSword, 1);
        }

        for (int i = 0; i < this.craftMatrix.getSizeInventory(); i++) {
            ItemStack itemstack = this.craftMatrix.getStackInSlot(i);
            if (itemstack != null) {
                this.craftMatrix.decrStackSize(i, 1);
                if (itemstack.getItem().hasContainerItem()) {
                    this.craftMatrix.setInventorySlotContents(i, new ItemStack(itemstack.getItem().getContainerItem()));
                }
            }
        }
    }
}
