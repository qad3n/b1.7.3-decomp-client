package net.minecraft.src;

import java.util.Random;

public class BlockBed
extends Block {
    public static final int[][] headBlockToFootBlockMap = new int[][]{{0, 1}, {-1, 0}, {0, -1}, {1, 0}};

    public BlockBed(int id) {
        super(id, 134, Material.cloth);
        this.setBounds();
    }

    public boolean blockActivated(World world, int x, int y, int z, EntityPlayer player) {
        EntityPlayer.EnumStatus enumStatus;
        if (world.multiplayerWorld) {
            return true;
        }
        int n = world.getBlockMetadata(x, y, z);
        if (!BlockBed.isBlockFootOfBed(n)) {
            int n2 = BlockBed.getDirectionFromMetadata(n);
            x += headBlockToFootBlockMap[n2][0];
            z += headBlockToFootBlockMap[n2][1];
            if (world.getBlockId(x, y, z) != this.blockID) {
                return true;
            }
            n = world.getBlockMetadata(x, y, z);
        }
        if (!world.worldProvider.canRespawnHere()) {
            double d = (double)x + 0.5;
            double d2 = (double)y + 0.5;
            double d3 = (double)z + 0.5;
            world.setBlockWithNotify(x, y, z, 0);
            int n3 = BlockBed.getDirectionFromMetadata(n);
            x += headBlockToFootBlockMap[n3][0];
            z += headBlockToFootBlockMap[n3][1];
            if (world.getBlockId(x, y, z) == this.blockID) {
                world.setBlockWithNotify(x, y, z, 0);
                d = (d + (double)x + 0.5) / 2.0;
                d2 = (d2 + (double)y + 0.5) / 2.0;
                d3 = (d3 + (double)z + 0.5) / 2.0;
            }
            world.newExplosion(null, (float)x + 0.5f, (float)y + 0.5f, (float)z + 0.5f, 5.0f, true);
            return true;
        }
        if (BlockBed.isBedOccupied(n)) {
            EntityPlayer entityPlayer = null;
            for (EntityPlayer entityPlayer2 : world.playerEntities) {
                if (!entityPlayer2.isPlayerSleeping()) continue;
                ChunkCoordinates chunkCoordinates = entityPlayer2.bedChunkCoordinates;
                if (chunkCoordinates.x != x || chunkCoordinates.y != y || chunkCoordinates.z != z) continue;
                entityPlayer = entityPlayer2;
            }
            if (entityPlayer == null) {
                BlockBed.setBedOccupied(world, x, y, z, false);
            } else {
                player.addChatMessage("tile.bed.occupied");
                return true;
            }
        }
        enumStatus = player.sleepInBedAt(x, y, z);
        if (enumStatus == EntityPlayer.EnumStatus.OK) {
            BlockBed.setBedOccupied(world, x, y, z, true);
            return true;
        }
        if (enumStatus == EntityPlayer.EnumStatus.NOT_POSSIBLE_NOW) {
            player.addChatMessage("tile.bed.noSleep");
        }
        return true;
    }

    public int getBlockTextureFromSideAndMetadata(int face, int metadata) {
        if (face == 0) {
            return Block.planks.blockIndexInTexture;
        }
        int n = BlockBed.getDirectionFromMetadata(metadata);
        int n2 = ModelBed.bedDirection[n][face];
        if (BlockBed.isBlockFootOfBed(metadata)) {
            if (n2 == 2) {
                return this.blockIndexInTexture + 2 + 16;
            }
            if (n2 == 5 || n2 == 4) {
                return this.blockIndexInTexture + 1 + 16;
            }
            return this.blockIndexInTexture + 1;
        }
        if (n2 == 3) {
            return this.blockIndexInTexture - 1 + 16;
        }
        if (n2 == 5 || n2 == 4) {
            return this.blockIndexInTexture + 16;
        }
        return this.blockIndexInTexture;
    }

    public int getRenderType() {
        return 14;
    }

    public boolean renderAsNormalBlock() {
        return false;
    }

    public boolean isOpaqueCube() {
        return false;
    }

    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
        this.setBounds();
    }

    public void onNeighborBlockChange(World world, int x, int y, int z, int neighborBlock) {
        int n = world.getBlockMetadata(x, y, z);
        int n2 = BlockBed.getDirectionFromMetadata(n);
        if (BlockBed.isBlockFootOfBed(n)) {
            if (world.getBlockId(x - headBlockToFootBlockMap[n2][0], y, z - headBlockToFootBlockMap[n2][1]) != this.blockID) {
                world.setBlockWithNotify(x, y, z, 0);
            }
        } else if (world.getBlockId(x + headBlockToFootBlockMap[n2][0], y, z + headBlockToFootBlockMap[n2][1]) != this.blockID) {
            world.setBlockWithNotify(x, y, z, 0);
            if (!world.multiplayerWorld) {
                this.dropBlockAsItem(world, x, y, z, n);
            }
        }
    }

    public int idDropped(int metadata, Random random) {
        if (BlockBed.isBlockFootOfBed(metadata)) {
            return 0;
        }
        return Item.bed.shiftedIndex;
    }

    private void setBounds() {
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.5625f, 1.0f);
    }

    public static int getDirectionFromMetadata(int metadata) {
        return metadata & 3;
    }

    public static boolean isBlockFootOfBed(int metadata) {
        return (metadata & 8) != 0;
    }

    public static boolean isBedOccupied(int metadata) {
        return (metadata & 4) != 0;
    }

    public static void setBedOccupied(World world, int x, int y, int z, boolean occupied) {
        int i = world.getBlockMetadata(x, y, z);
        if (occupied) {
            i |= 4;
        } else {
            i &= -5;
        }
        world.setBlockMetadataWithNotify(x, y, z, i);
    }

    public static ChunkCoordinates getNearestEmptyChunkCoordinates(World world, int x, int y, int z, int skip) {
        int n = world.getBlockMetadata(x, y, z);
        int n2 = BlockBed.getDirectionFromMetadata(n);
        for (int i = 0; i <= 1; ++i) {
            int n3 = x - headBlockToFootBlockMap[n2][0] * i - 1;
            int n4 = z - headBlockToFootBlockMap[n2][1] * i - 1;
            int n5 = n3 + 2;
            int n6 = n4 + 2;
            for (int j = n3; j <= n5; ++j) {
                for (int k = n4; k <= n6; ++k) {
                    if (!world.isBlockNormalCube(j, y - 1, k) || !world.isAirBlock(j, y, k) || !world.isAirBlock(j, y + 1, k)) continue;
                    if (skip > 0) {
                        --skip;
                        continue;
                    }
                    return new ChunkCoordinates(j, y, k);
                }
            }
        }
        return null;
    }

    public void dropBlockAsItemWithChance(World world, int x, int y, int z, int metadata, float luck) {
        if (!BlockBed.isBlockFootOfBed(metadata)) {
            super.dropBlockAsItemWithChance(world, x, y, z, metadata, luck);
        }
    }

    public int getMobilityFlag() {
        return 1;
    }
}

