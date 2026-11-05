package net.minecraft.src;


public class ItemFood
extends Item {
    private int healAmount;
    private boolean isWolfsFavoriteMeat;

    public ItemFood(int id, int hungerPoints, boolean canBeCooked) {
        super(id);
        this.healAmount = hungerPoints;
        this.isWolfsFavoriteMeat = canBeCooked;
        this.maxStackSize = 1;
    }

    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        --stack.stackSize;
        player.heal(this.healAmount);
        return stack;
    }

    public int getHealAmount() {
        return this.healAmount;
    }

    public boolean getIsWolfsFavoriteMeat() {
        return this.isWolfsFavoriteMeat;
    }
}

