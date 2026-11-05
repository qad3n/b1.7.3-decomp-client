package net.minecraft.src;


public class ItemSlab
extends ItemBlock {
    public ItemSlab(int n) {
        super(n);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    public int getIconFromDamage(int metadata) {
        return Block.stairSingle.getBlockTextureFromSideAndMetadata(2, metadata);
    }

    public int getPlacedBlockMetadata(int metadata) {
        return metadata;
    }

    public String getItemNameIS(ItemStack stack) {
        return super.getItemName() + "." + BlockStep.VARIANTS[stack.getItemDamage()];
    }
}

