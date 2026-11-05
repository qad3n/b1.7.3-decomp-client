package net.minecraft.src;


public class ItemSapling
extends ItemBlock {
    public ItemSapling(int n) {
        super(n);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    public int getPlacedBlockMetadata(int metadata) {
        return metadata;
    }

    public int getIconFromDamage(int metadata) {
        return Block.sapling.getBlockTextureFromSideAndMetadata(0, metadata);
    }
}

