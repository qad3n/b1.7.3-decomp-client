package net.minecraft.src;


public class ItemCloth
extends ItemBlock {
    public ItemCloth(int n) {
        super(n);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    public int getIconFromDamage(int metadata) {
        return Block.cloth.getBlockTextureFromSideAndMetadata(2, BlockCloth.getBlockMetadataForItemMetadata(metadata));
    }

    public int getPlacedBlockMetadata(int metadata) {
        return metadata;
    }

    public String getItemNameIS(ItemStack stack) {
        return super.getItemName() + "." + ItemDye.dyeColors[BlockCloth.getBlockMetadataForItemMetadata(stack.getItemDamage())];
    }
}

