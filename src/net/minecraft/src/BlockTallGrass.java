package net.minecraft.src;

import java.util.Random;

public class BlockTallGrass
extends BlockFlower {
    protected BlockTallGrass(int n, int n2) {
        super(n, n2);
        float f = 0.4f;
        this.setBlockBounds(0.5f - f, 0.0f, 0.5f - f, 0.5f + f, 0.8f, 0.5f + f);
    }

    public int getBlockTextureFromSideAndMetadata(int face, int metadata) {
        if (metadata == 1) {
            return this.blockIndexInTexture;
        }
        if (metadata == 2) {
            return this.blockIndexInTexture + 16 + 1;
        }
        if (metadata == 0) {
            return this.blockIndexInTexture + 16;
        }
        return this.blockIndexInTexture;
    }

    public int colorMultiplier(IBlockAccess world, int x, int y, int z) {
        int n = world.getBlockMetadata(x, y, z);
        if (n == 0) {
            return 0xFFFFFF;
        }
        long l = x * 3129871 + z * 6129781 + y;
        l = l * l * 42317861L + l * 11L;
        x = (int)((long)x + (l >> 14 & 0x1FL));
        y = (int)((long)y + (l >> 19 & 0x1FL));
        z = (int)((long)z + (l >> 24 & 0x1FL));
        world.getWorldChunkManager().getBiomes(x, z, 1, 1);
        double d = world.getWorldChunkManager().temperature[0];
        double d2 = world.getWorldChunkManager().humidity[0];
        return ColorizerGrass.getGrassColor(d, d2);
    }

    public int idDropped(int metadata, Random random) {
        if (random.nextInt(8) == 0) {
            return Item.seeds.shiftedIndex;
        }
        return -1;
    }
}

