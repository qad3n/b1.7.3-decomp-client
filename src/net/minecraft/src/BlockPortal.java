package net.minecraft.src;

import java.util.Random;

public class BlockPortal
extends BlockBreakable {
    public BlockPortal(int id, int sprite) {
        super(id, sprite, Material.portal, false);
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        return null;
    }

    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
        if (world.getBlockId(x - 1, y, z) == this.blockID || world.getBlockId(x + 1, y, z) == this.blockID) {
            float f = 0.5f;
            float f2 = 0.125f;
            this.setBlockBounds(0.5f - f, 0.0f, 0.5f - f2, 0.5f + f, 1.0f, 0.5f + f2);
        } else {
            float f = 0.125f;
            float f3 = 0.5f;
            this.setBlockBounds(0.5f - f, 0.0f, 0.5f - f3, 0.5f + f, 1.0f, 0.5f + f3);
        }
    }

    public boolean isOpaqueCube() {
        return false;
    }

    public boolean renderAsNormalBlock() {
        return false;
    }

    public boolean tryToCreatePortal(World world, int x, int y, int z) {
        int n;
        int n2;
        int n3 = 0;
        int n4 = 0;
        if (world.getBlockId(x - 1, y, z) == Block.obsidian.blockID || world.getBlockId(x + 1, y, z) == Block.obsidian.blockID) {
            n3 = 1;
        }
        if (world.getBlockId(x, y, z - 1) == Block.obsidian.blockID || world.getBlockId(x, y, z + 1) == Block.obsidian.blockID) {
            n4 = 1;
        }
        if (n3 == n4) {
            return false;
        }
        if (world.getBlockId(x - n3, y, z - n4) == 0) {
            x -= n3;
            z -= n4;
        }
        for (n2 = -1; n2 <= 2; ++n2) {
            for (n = -1; n <= 3; ++n) {
                boolean bl;
                boolean bl2 = bl = n2 == -1 || n2 == 2 || n == -1 || n == 3;
                if (!(n2 != -1 && n2 != 2 || n != -1 && n != 3)) continue;
                int n5 = world.getBlockId(x + n3 * n2, y + n, z + n4 * n2);
                if (!(bl ? n5 != Block.obsidian.blockID : n5 != 0 && n5 != Block.fire.blockID)) continue;
                return false;
            }
        }
        world.editingBlocks = true;
        for (n2 = 0; n2 < 2; ++n2) {
            for (n = 0; n < 3; ++n) {
                world.setBlockWithNotify(x + n3 * n2, y + n, z + n4 * n2, Block.portal.blockID);
            }
        }
        world.editingBlocks = false;
        return true;
    }

    public void onNeighborBlockChange(World world, int x, int y, int z, int neighborBlock) {
        boolean bl;
        int n;
        int n2 = 0;
        int n3 = 1;
        if (world.getBlockId(x - 1, y, z) == this.blockID || world.getBlockId(x + 1, y, z) == this.blockID) {
            n2 = 1;
            n3 = 0;
        }
        int n4 = y;
        while (world.getBlockId(x, n4 - 1, z) == this.blockID) {
            --n4;
        }
        if (world.getBlockId(x, n4 - 1, z) != Block.obsidian.blockID) {
            world.setBlockWithNotify(x, y, z, 0);
            return;
        }
        for (n = 1; n < 4 && world.getBlockId(x, n4 + n, z) == this.blockID; ++n) {
        }
        if (n != 3 || world.getBlockId(x, n4 + n, z) != Block.obsidian.blockID) {
            world.setBlockWithNotify(x, y, z, 0);
            return;
        }
        boolean bl2 = world.getBlockId(x - 1, y, z) == this.blockID || world.getBlockId(x + 1, y, z) == this.blockID;
        boolean bl3 = bl = world.getBlockId(x, y, z - 1) == this.blockID || world.getBlockId(x, y, z + 1) == this.blockID;
        if (bl2 && bl) {
            world.setBlockWithNotify(x, y, z, 0);
            return;
        }
        if (!(world.getBlockId(x + n2, y, z + n3) == Block.obsidian.blockID && world.getBlockId(x - n2, y, z - n3) == this.blockID || world.getBlockId(x - n2, y, z - n3) == Block.obsidian.blockID && world.getBlockId(x + n2, y, z + n3) == this.blockID)) {
            world.setBlockWithNotify(x, y, z, 0);
            return;
        }
    }

    public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int face) {
        boolean bl;
        if (world.getBlockId(x, y, z) == this.blockID) {
            return false;
        }
        boolean bl2 = world.getBlockId(x - 1, y, z) == this.blockID && world.getBlockId(x - 2, y, z) != this.blockID;
        boolean bl3 = world.getBlockId(x + 1, y, z) == this.blockID && world.getBlockId(x + 2, y, z) != this.blockID;
        boolean bl4 = world.getBlockId(x, y, z - 1) == this.blockID && world.getBlockId(x, y, z - 2) != this.blockID;
        boolean bl5 = world.getBlockId(x, y, z + 1) == this.blockID && world.getBlockId(x, y, z + 2) != this.blockID;
        boolean bl6 = bl2 || bl3;
        boolean bl7 = bl = bl4 || bl5;
        if (bl6 && face == 4) {
            return true;
        }
        if (bl6 && face == 5) {
            return true;
        }
        if (bl && face == 2) {
            return true;
        }
        return bl && face == 3;
    }

    public int quantityDropped(Random random) {
        return 0;
    }

    public int getRenderBlockPass() {
        return 1;
    }

    public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
        if (entity.ridingEntity == null && entity.riddenByEntity == null) {
            entity.setInPortal();
        }
    }

    public void randomDisplayTick(World world, int x, int y, int z, Random random) {
        if (random.nextInt(100) == 0) {
            world.playSoundEffect((double)x + 0.5, (double)y + 0.5, (double)z + 0.5, "portal.portal", 1.0f, random.nextFloat() * 0.4f + 0.8f);
        }
        for (int i = 0; i < 4; ++i) {
            double d = (float)x + random.nextFloat();
            double d2 = (float)y + random.nextFloat();
            double d3 = (float)z + random.nextFloat();
            double d4 = 0.0;
            double d5 = 0.0;
            double d6 = 0.0;
            int n = random.nextInt(2) * 2 - 1;
            d4 = ((double)random.nextFloat() - 0.5) * 0.5;
            d5 = ((double)random.nextFloat() - 0.5) * 0.5;
            d6 = ((double)random.nextFloat() - 0.5) * 0.5;
            if (world.getBlockId(x - 1, y, z) == this.blockID || world.getBlockId(x + 1, y, z) == this.blockID) {
                d3 = (double)z + 0.5 + 0.25 * (double)n;
                d6 = random.nextFloat() * 2.0f * (float)n;
            } else {
                d = (double)x + 0.5 + 0.25 * (double)n;
                d4 = random.nextFloat() * 2.0f * (float)n;
            }
            world.spawnParticle("portal", d, d2, d3, d4, d5, d6);
        }
    }
}

