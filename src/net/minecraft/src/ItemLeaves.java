package net.minecraft.src;


public class ItemLeaves
extends ItemBlock {
    public ItemLeaves(int n) {
        super(n);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    public int getPlacedBlockMetadata(int metadata) {
        return metadata | 8;
    }

    public int getIconFromDamage(int metadata) {
        return Block.leaves.getBlockTextureFromSideAndMetadata(0, metadata);
    }

    public int getColorFromDamage(int metadata) {
        if ((metadata & 1) == 1) {
            return ColorizerFoliage.getFoliageColorPine();
        }
        if ((metadata & 2) == 2) {
            return ColorizerFoliage.getFoliageColorBirch();
        }
        return ColorizerFoliage.getDefaultColor();
    }
}

