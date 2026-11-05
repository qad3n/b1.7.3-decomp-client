package net.minecraft.src;

import java.util.Random;

public class BlockWeb
extends Block {
    public BlockWeb(int id, int sprite) {
        super(id, sprite, Material.COBWEB);
    }

    public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
        entity.isInWeb = true;
    }

    public boolean isOpaqueCube() {
        return false;
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        return null;
    }

    public int getRenderType() {
        return 1;
    }

    public boolean renderAsNormalBlock() {
        return false;
    }

    public int idDropped(int metadata, Random random) {
        return Item.silk.shiftedIndex;
    }
}

