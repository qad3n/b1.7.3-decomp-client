package net.minecraft.src;


public class ItemCoal
extends Item {
    public ItemCoal(int n) {
        super(n);
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
    }

    public String getItemNameIS(ItemStack stack) {
        if (stack.getItemDamage() == 1) {
            return "item.charcoal";
        }
        return "item.coal";
    }
}

