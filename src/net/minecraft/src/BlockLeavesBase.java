package net.minecraft.src;


public class BlockLeavesBase
extends Block {
    protected boolean graphicsLevel;

    protected BlockLeavesBase(int id, int sprite, Material material, boolean culling) {
        super(id, sprite, material);
        this.graphicsLevel = culling;
    }

    public boolean isOpaqueCube() {
        return false;
    }

    public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int face) {
        int n = world.getBlockId(x, y, z);
        if (!this.graphicsLevel && n == this.blockID) {
            return false;
        }
        return super.shouldSideBeRendered(world, x, y, z, face);
    }
}

