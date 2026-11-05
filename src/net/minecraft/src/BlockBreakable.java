package net.minecraft.src;


public class BlockBreakable
extends Block {
    private boolean localFlag;

    protected BlockBreakable(int id, int sprite, Material material, boolean culling) {
        super(id, sprite, material);
        this.localFlag = culling;
    }

    public boolean isOpaqueCube() {
        return false;
    }

    public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int face) {
        int n = world.getBlockId(x, y, z);
        if (!this.localFlag && n == this.blockID) {
            return false;
        }
        return super.shouldSideBeRendered(world, x, y, z, face);
    }
}

