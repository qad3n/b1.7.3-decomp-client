package net.minecraft.src;


public class BlockTrapDoor
extends Block {
    protected BlockTrapDoor(int n, Material material) {
        super(n, material);
        this.blockIndexInTexture = 84;
        if (material == Material.iron) {
            ++this.blockIndexInTexture;
        }
        float f = 0.5f;
        float f2 = 1.0f;
        this.setBlockBounds(0.5f - f, 0.0f, 0.5f - f, 0.5f + f, f2, 0.5f + f);
    }

    public boolean isOpaqueCube() {
        return false;
    }

    public boolean renderAsNormalBlock() {
        return false;
    }

    public int getRenderType() {
        return 0;
    }

    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z) {
        this.setBlockBoundsBasedOnState(world, x, y, z);
        return super.getSelectedBoundingBoxFromPool(world, x, y, z);
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        this.setBlockBoundsBasedOnState(world, x, y, z);
        return super.getCollisionBoundingBoxFromPool(world, x, y, z);
    }

    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
        this.setBlockBoundsForBlockRender(world.getBlockMetadata(x, y, z));
    }

    public void setBlockBoundsForItemRender() {
        float f = 0.1875f;
        this.setBlockBounds(0.0f, 0.5f - f / 2.0f, 0.0f, 1.0f, 0.5f + f / 2.0f, 1.0f);
    }

    public void setBlockBoundsForBlockRender(int metadata) {
        float f = 0.1875f;
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, f, 1.0f);
        if (BlockTrapDoor.isTrapdoorOpen(metadata)) {
            if ((metadata & 3) == 0) {
                this.setBlockBounds(0.0f, 0.0f, 1.0f - f, 1.0f, 1.0f, 1.0f);
            }
            if ((metadata & 3) == 1) {
                this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, f);
            }
            if ((metadata & 3) == 2) {
                this.setBlockBounds(1.0f - f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
            }
            if ((metadata & 3) == 3) {
                this.setBlockBounds(0.0f, 0.0f, 0.0f, f, 1.0f, 1.0f);
            }
        }
    }

    public void onBlockClicked(World world, int x, int y, int z, EntityPlayer player) {
        this.blockActivated(world, x, y, z, player);
    }

    public boolean blockActivated(World world, int x, int y, int z, EntityPlayer player) {
        if (this.blockMaterial == Material.iron) {
            return true;
        }
        int n = world.getBlockMetadata(x, y, z);
        world.setBlockMetadataWithNotify(x, y, z, n ^ 4);
        world.doEvent(player, 1003, x, y, z, 0);
        return true;
    }

    public void onPoweredBlockChange(World world, int x, int y, int z, boolean open) {
        boolean bl;
        int n = world.getBlockMetadata(x, y, z);
        boolean bl2 = bl = (n & 4) > 0;
        if (bl == open) {
            return;
        }
        world.setBlockMetadataWithNotify(x, y, z, n ^ 4);
        world.doEvent(null, 1003, x, y, z, 0);
    }

    public void onNeighborBlockChange(World world, int x, int y, int z, int neighborBlock) {
        if (world.multiplayerWorld) {
            return;
        }
        int n = world.getBlockMetadata(x, y, z);
        int n2 = x;
        int n3 = z;
        if ((n & 3) == 0) {
            ++n3;
        }
        if ((n & 3) == 1) {
            --n3;
        }
        if ((n & 3) == 2) {
            ++n2;
        }
        if ((n & 3) == 3) {
            --n2;
        }
        if (!world.isBlockNormalCube(n2, y, n3)) {
            world.setBlockWithNotify(x, y, z, 0);
            this.dropBlockAsItem(world, x, y, z, n);
        }
        if (neighborBlock > 0 && Block.blocksList[neighborBlock].canProvidePower()) {
            boolean bl = world.isBlockIndirectlyGettingPowered(x, y, z);
            this.onPoweredBlockChange(world, x, y, z, bl);
        }
    }

    public MovingObjectPosition collisionRayTrace(World world, int x, int y, int z, Vec3D from, Vec3D to) {
        this.setBlockBoundsBasedOnState(world, x, y, z);
        return super.collisionRayTrace(world, x, y, z, from, to);
    }

    public void onBlockPlaced(World world, int x, int y, int z, int face) {
        int n = 0;
        if (face == 2) {
            n = 0;
        }
        if (face == 3) {
            n = 1;
        }
        if (face == 4) {
            n = 2;
        }
        if (face == 5) {
            n = 3;
        }
        world.setBlockMetadataWithNotify(x, y, z, n);
    }

    public boolean canPlaceBlockOnSide(World world, int x, int y, int z, int face) {
        if (face == 0) {
            return false;
        }
        if (face == 1) {
            return false;
        }
        if (face == 2) {
            ++z;
        }
        if (face == 3) {
            --z;
        }
        if (face == 4) {
            ++x;
        }
        if (face == 5) {
            --x;
        }
        return world.isBlockNormalCube(x, y, z);
    }

    public static boolean isTrapdoorOpen(int metadata) {
        return (metadata & 4) != 0;
    }
}

