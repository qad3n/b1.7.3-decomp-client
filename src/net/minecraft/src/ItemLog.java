package net.minecraft.src;


public class ItemLog
extends ItemBlock {
    public ItemLog(int n) {
        super(n);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    public int getIconFromDamage(int metadata) {
        return Block.wood.getBlockTextureFromSideAndMetadata(2, metadata);
    }

    public int getPlacedBlockMetadata(int metadata) {
        return metadata;
    }
}

