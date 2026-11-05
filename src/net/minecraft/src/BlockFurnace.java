package net.minecraft.src;

import java.util.Random;

public class BlockFurnace
extends BlockContainer {
    private Random furnaceRand = new Random();
    private final boolean isActive;
    private static boolean keepFurnaceInventory = false;

    protected BlockFurnace(int id, boolean lit) {
        super(id, Material.rock);
        this.isActive = lit;
        this.blockIndexInTexture = 45;
    }

    public int idDropped(int metadata, Random random) {
        return Block.stoneOvenIdle.blockID;
    }

    public void onBlockAdded(World world, int x, int y, int z) {
        super.onBlockAdded(world, x, y, z);
        this.setDefaultDirection(world, x, y, z);
    }

    private void setDefaultDirection(World world, int x, int y, int z) {
        if (world.multiplayerWorld) {
            return;
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
        world.setBlockMetadataWithNotify(x, y, z, n5);
    }

    public int getBlockTexture(IBlockAccess world, int x, int y, int z, int face) {
        if (face == 1) {
            return this.blockIndexInTexture + 17;
        }
        if (face == 0) {
            return this.blockIndexInTexture + 17;
        }
        int n = world.getBlockMetadata(x, y, z);
        if (face != n) {
            return this.blockIndexInTexture;
        }
        if (this.isActive) {
            return this.blockIndexInTexture + 16;
        }
        return this.blockIndexInTexture - 1;
    }

    public void randomDisplayTick(World world, int x, int y, int z, Random random) {
        if (!this.isActive) {
            return;
        }
        int n = world.getBlockMetadata(x, y, z);
        float f = (float)x + 0.5f;
        float f2 = (float)y + 0.0f + random.nextFloat() * 6.0f / 16.0f;
        float f3 = (float)z + 0.5f;
        float f4 = 0.52f;
        float f5 = random.nextFloat() * 0.6f - 0.3f;
        if (n == 4) {
            world.spawnParticle("smoke", f - f4, f2, f3 + f5, 0.0, 0.0, 0.0);
            world.spawnParticle("flame", f - f4, f2, f3 + f5, 0.0, 0.0, 0.0);
        } else if (n == 5) {
            world.spawnParticle("smoke", f + f4, f2, f3 + f5, 0.0, 0.0, 0.0);
            world.spawnParticle("flame", f + f4, f2, f3 + f5, 0.0, 0.0, 0.0);
        } else if (n == 2) {
            world.spawnParticle("smoke", f + f5, f2, f3 - f4, 0.0, 0.0, 0.0);
            world.spawnParticle("flame", f + f5, f2, f3 - f4, 0.0, 0.0, 0.0);
        } else if (n == 3) {
            world.spawnParticle("smoke", f + f5, f2, f3 + f4, 0.0, 0.0, 0.0);
            world.spawnParticle("flame", f + f5, f2, f3 + f4, 0.0, 0.0, 0.0);
        }
    }

    public int getBlockTextureFromSide(int face) {
        if (face == 1) {
            return this.blockIndexInTexture + 17;
        }
        if (face == 0) {
            return this.blockIndexInTexture + 17;
        }
        if (face == 3) {
            return this.blockIndexInTexture - 1;
        }
        return this.blockIndexInTexture;
    }

    public boolean blockActivated(World world, int x, int y, int z, EntityPlayer player) {
        if (world.multiplayerWorld) {
            return true;
        }
        TileEntityFurnace tileEntityFurnace = (TileEntityFurnace)world.getBlockTileEntity(x, y, z);
        player.displayGUIFurnace(tileEntityFurnace);
        return true;
    }

    public static void updateFurnaceBlockState(boolean lit, World world, int x, int y, int z) {
        int n = world.getBlockMetadata(x, y, z);
        TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
        keepFurnaceInventory = true;
        if (lit) {
            world.setBlockWithNotify(x, y, z, Block.stoneOvenActive.blockID);
        } else {
            world.setBlockWithNotify(x, y, z, Block.stoneOvenIdle.blockID);
        }
        keepFurnaceInventory = false;
        world.setBlockMetadataWithNotify(x, y, z, n);
        tileEntity.cancelRemoval();
        world.setBlockTileEntity(x, y, z, tileEntity);
    }

    protected TileEntity getBlockEntity() {
        return new TileEntityFurnace();
    }

    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLiving entity) {
        int n = MathHelper.floor_double((double)(entity.rotationYaw * 4.0f / 360.0f) + 0.5) & 3;
        if (n == 0) {
            world.setBlockMetadataWithNotify(x, y, z, 2);
        }
        if (n == 1) {
            world.setBlockMetadataWithNotify(x, y, z, 5);
        }
        if (n == 2) {
            world.setBlockMetadataWithNotify(x, y, z, 3);
        }
        if (n == 3) {
            world.setBlockMetadataWithNotify(x, y, z, 4);
        }
    }

    public void onBlockRemoval(World world, int x, int y, int z) {
        if (!keepFurnaceInventory) {
            TileEntityFurnace tileEntityFurnace = (TileEntityFurnace)world.getBlockTileEntity(x, y, z);
            for (int i = 0; i < tileEntityFurnace.getSizeInventory(); ++i) {
                ItemStack itemStack = tileEntityFurnace.getStackInSlot(i);
                if (itemStack == null) continue;
                float f = this.furnaceRand.nextFloat() * 0.8f + 0.1f;
                float f2 = this.furnaceRand.nextFloat() * 0.8f + 0.1f;
                float f3 = this.furnaceRand.nextFloat() * 0.8f + 0.1f;
                while (itemStack.stackSize > 0) {
                    int n = this.furnaceRand.nextInt(21) + 10;
                    if (n > itemStack.stackSize) {
                        n = itemStack.stackSize;
                    }
                    itemStack.stackSize -= n;
                    EntityItem entityItem = new EntityItem(world, (float)x + f, (float)y + f2, (float)z + f3, new ItemStack(itemStack.itemID, n, itemStack.getItemDamage()));
                    float f4 = 0.05f;
                    entityItem.motionX = (float)this.furnaceRand.nextGaussian() * f4;
                    entityItem.motionY = (float)this.furnaceRand.nextGaussian() * f4 + 0.2f;
                    entityItem.motionZ = (float)this.furnaceRand.nextGaussian() * f4;
                    world.entityJoinedWorld(entityItem);
                }
            }
        }
        super.onBlockRemoval(world, x, y, z);
    }
}

