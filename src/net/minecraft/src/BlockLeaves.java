package net.minecraft.src;

import java.util.Random;

public class BlockLeaves
extends BlockLeavesBase {
    private int baseIndexInPNG;
    int[] adjacentTreeBlocks;

    protected BlockLeaves(int id, int sprite) {
        super(id, sprite, Material.leaves, false);
        this.baseIndexInPNG = sprite;
        this.setTickOnLoad(true);
    }

    public int getRenderColor(int metadata) {
        if ((metadata & 1) == 1) {
            return ColorizerFoliage.getFoliageColorPine();
        }
        if ((metadata & 2) == 2) {
            return ColorizerFoliage.getFoliageColorBirch();
        }
        return ColorizerFoliage.getDefaultColor();
    }

    public int colorMultiplier(IBlockAccess world, int x, int y, int z) {
        int n = world.getBlockMetadata(x, y, z);
        if ((n & 1) == 1) {
            return ColorizerFoliage.getFoliageColorPine();
        }
        if ((n & 2) == 2) {
            return ColorizerFoliage.getFoliageColorBirch();
        }
        world.getWorldChunkManager().getBiomes(x, z, 1, 1);
        double d = world.getWorldChunkManager().temperature[0];
        double d2 = world.getWorldChunkManager().humidity[0];
        return ColorizerFoliage.getFoliageColor(d, d2);
    }

    @Override
public void onBlockRemoval(World world, int x, int y, int z) {
    byte b0 = 1;
    int i = b0 + 1;
    if (world.checkChunksExist(x - i, y - i, z - i, x + i, y + i, z + i)) {
        for (int j = -b0; j <= b0; j++) {
            for (int k = -b0; k <= b0; k++) {
                for (int l = -b0; l <= b0; l++) {
                    int i1 = world.getBlockId(x + j, y + k, z + l);
                    if (i1 == Block.leaves.blockID) {
                        int j1 = world.getBlockMetadata(x + j, y + k, z + l);
                        world.setBlockMetadata(x + j, y + k, z + l, j1 | 8);
                    }
                }
            }
        }
    }
}

    public void updateTick(World world, int x, int y, int z, Random random) {
        if (world.multiplayerWorld) {
            return;
        }
        int n = world.getBlockMetadata(x, y, z);
        if ((n & 8) != 0) {
            int n2;
            int n3 = 4;
            int n4 = n3 + 1;
            int n5 = 32;
            int n6 = n5 * n5;
            int n7 = n5 / 2;
            if (this.adjacentTreeBlocks == null) {
                this.adjacentTreeBlocks = new int[n5 * n5 * n5];
            }
            if (world.checkChunksExist(x - n4, y - n4, z - n4, x + n4, y + n4, z + n4)) {
                int n8;
                int n9;
                int n10;
                for (n2 = -n3; n2 <= n3; ++n2) {
                    for (n10 = -n3; n10 <= n3; ++n10) {
                        for (n9 = -n3; n9 <= n3; ++n9) {
                            n8 = world.getBlockId(x + n2, y + n10, z + n9);
                            this.adjacentTreeBlocks[(n2 + n7) * n6 + (n10 + n7) * n5 + (n9 + n7)] = n8 == Block.wood.blockID ? 0 : (n8 == Block.leaves.blockID ? -2 : -1);
                        }
                    }
                }
                for (n2 = 1; n2 <= 4; ++n2) {
                    for (n10 = -n3; n10 <= n3; ++n10) {
                        for (n9 = -n3; n9 <= n3; ++n9) {
                            for (n8 = -n3; n8 <= n3; ++n8) {
                                if (this.adjacentTreeBlocks[(n10 + n7) * n6 + (n9 + n7) * n5 + (n8 + n7)] != n2 - 1) continue;
                                if (this.adjacentTreeBlocks[(n10 + n7 - 1) * n6 + (n9 + n7) * n5 + (n8 + n7)] == -2) {
                                    this.adjacentTreeBlocks[(n10 + n7 - 1) * n6 + (n9 + n7) * n5 + (n8 + n7)] = n2;
                                }
                                if (this.adjacentTreeBlocks[(n10 + n7 + 1) * n6 + (n9 + n7) * n5 + (n8 + n7)] == -2) {
                                    this.adjacentTreeBlocks[(n10 + n7 + 1) * n6 + (n9 + n7) * n5 + (n8 + n7)] = n2;
                                }
                                if (this.adjacentTreeBlocks[(n10 + n7) * n6 + (n9 + n7 - 1) * n5 + (n8 + n7)] == -2) {
                                    this.adjacentTreeBlocks[(n10 + n7) * n6 + (n9 + n7 - 1) * n5 + (n8 + n7)] = n2;
                                }
                                if (this.adjacentTreeBlocks[(n10 + n7) * n6 + (n9 + n7 + 1) * n5 + (n8 + n7)] == -2) {
                                    this.adjacentTreeBlocks[(n10 + n7) * n6 + (n9 + n7 + 1) * n5 + (n8 + n7)] = n2;
                                }
                                if (this.adjacentTreeBlocks[(n10 + n7) * n6 + (n9 + n7) * n5 + (n8 + n7 - 1)] == -2) {
                                    this.adjacentTreeBlocks[(n10 + n7) * n6 + (n9 + n7) * n5 + (n8 + n7 - 1)] = n2;
                                }
                                if (this.adjacentTreeBlocks[(n10 + n7) * n6 + (n9 + n7) * n5 + (n8 + n7 + 1)] != -2) continue;
                                this.adjacentTreeBlocks[(n10 + n7) * n6 + (n9 + n7) * n5 + (n8 + n7 + 1)] = n2;
                            }
                        }
                    }
                }
            }
            if ((n2 = this.adjacentTreeBlocks[n7 * n6 + n7 * n5 + n7]) >= 0) {
                world.setBlockMetadata(x, y, z, n & 0xFFFFFFF7);
            } else {
                this.removeLeaves(world, x, y, z);
            }
        }
    }

    private void removeLeaves(World world, int x, int y, int z) {
        this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z));
        world.setBlockWithNotify(x, y, z, 0);
    }

    public int quantityDropped(Random random) {
        return random.nextInt(20) == 0 ? 1 : 0;
    }

    public int idDropped(int metadata, Random random) {
        return Block.sapling.blockID;
    }

    public void harvestBlock(World world, EntityPlayer player, int x, int y, int z, int metadata) {
        if (!world.multiplayerWorld && player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().itemID == Item.shears.shiftedIndex) {
            player.addStat(StatList.mineBlockStatArray[this.blockID], 1);
            this.dropBlockAsItem_do(world, x, y, z, new ItemStack(Block.leaves.blockID, 1, metadata & 3));
        } else {
            super.harvestBlock(world, player, x, y, z, metadata);
        }
    }

    protected int damageDropped(int metadata) {
        return metadata & 3;
    }

    public boolean isOpaqueCube() {
        return !this.graphicsLevel;
    }

    public int getBlockTextureFromSideAndMetadata(int face, int metadata) {
        if ((metadata & 3) == 1) {
            return this.blockIndexInTexture + 80;
        }
        return this.blockIndexInTexture;
    }

    public void setGraphicsLevel(boolean culling) {
        this.graphicsLevel = culling;
        this.blockIndexInTexture = this.baseIndexInPNG + (culling ? 0 : 1);
    }

    public void onEntityWalking(World world, int x, int y, int z, Entity entity) {
        super.onEntityWalking(world, x, y, z, entity);
    }
}

