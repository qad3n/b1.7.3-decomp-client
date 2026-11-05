package net.minecraft.src;

import java.util.Random;

public class BlockLockedChest
extends Block {
    protected BlockLockedChest(int id) {
        super(id, Material.wood);
        this.blockIndexInTexture = 26;
    }

    public int getBlockTexture(IBlockAccess world, int x, int y, int z, int face) {
        if (face == 1) {
            return this.blockIndexInTexture - 1;
        }
        if (face == 0) {
            return this.blockIndexInTexture - 1;
        }
        int n = world.getBlockId(x, y, z - 1);
        int n2 = world.getBlockId(x, y, z + 1);
        int n3 = world.getBlockId(x - 1, y, z);
        int n4 = world.getBlockId(x + 1, y, z);
        int n5 = 3;
        if (Block.opaqueCubeLookup[n] && !Block.opaqueCubeLookup[n2]) {
            n5 = 3;
        }
        if (Block.opaqueCubeLookup[n2] && !Block.opaqueCubeLookup[n]) {
            n5 = 2;
        }
        if (Block.opaqueCubeLookup[n3] && !Block.opaqueCubeLookup[n4]) {
            n5 = 5;
        }
        if (Block.opaqueCubeLookup[n4] && !Block.opaqueCubeLookup[n3]) {
            n5 = 4;
        }
        return face == n5 ? this.blockIndexInTexture + 1 : this.blockIndexInTexture;
    }

    public int getBlockTextureFromSide(int face) {
        if (face == 1) {
            return this.blockIndexInTexture - 1;
        }
        if (face == 0) {
            return this.blockIndexInTexture - 1;
        }
        if (face == 3) {
            return this.blockIndexInTexture + 1;
        }
        return this.blockIndexInTexture;
    }

    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        return true;
    }

    public void updateTick(World world, int x, int y, int z, Random random) {
        world.setBlockWithNotify(x, y, z, 0);
    }
}

