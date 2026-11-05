package net.minecraft.src;


public class ChunkCache
implements IBlockAccess {
    private int chunkX;
    private int chunkZ;
    private Chunk[][] chunkArray;
    private World worldObj;

    public ChunkCache(World world, int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        this.worldObj = world;
        this.chunkX = minX >> 4;
        this.chunkZ = minZ >> 4;
        int n = maxX >> 4;
        int n2 = maxZ >> 4;
        this.chunkArray = new Chunk[n - this.chunkX + 1][n2 - this.chunkZ + 1];
        for (int i = this.chunkX; i <= n; ++i) {
            for (int j = this.chunkZ; j <= n2; ++j) {
                this.chunkArray[i - this.chunkX][j - this.chunkZ] = world.getChunkFromChunkCoords(i, j);
            }
        }
    }

    public int getBlockId(int x, int y, int z) {
        if (y < 0) {
            return 0;
        }
        if (y >= 128) {
            return 0;
        }
        int n = (x >> 4) - this.chunkX;
        int n2 = (z >> 4) - this.chunkZ;
        if (n < 0 || n >= this.chunkArray.length || n2 < 0 || n2 >= this.chunkArray[n].length) {
            return 0;
        }
        Chunk chunk = this.chunkArray[n][n2];
        if (chunk == null) {
            return 0;
        }
        return chunk.getBlockID(x & 0xF, y, z & 0xF);
    }

    public TileEntity getBlockTileEntity(int x, int y, int z) {
        int n = (x >> 4) - this.chunkX;
        int n2 = (z >> 4) - this.chunkZ;
        return this.chunkArray[n][n2].getChunkBlockTileEntity(x & 0xF, y, z & 0xF);
    }

    public float getBrightness(int x, int y, int z, int blockLight) {
        int n = this.getLightValue(x, y, z);
        if (n < blockLight) {
            n = blockLight;
        }
        return this.worldObj.worldProvider.lightBrightnessTable[n];
    }

    public float getLightBrightness(int x, int y, int z) {
        return this.worldObj.worldProvider.lightBrightnessTable[this.getLightValue(x, y, z)];
    }

    public int getLightValue(int x, int y, int z) {
        return this.getLightValueExt(x, y, z, true);
    }

    public int getLightValueExt(int x, int y, int z, boolean useNeighborLight) {
        int n;
        if (x < -32000000 || z < -32000000 || x >= 32000000 || z > 32000000) {
            return 15;
        }
        if (useNeighborLight && ((n = this.getBlockId(x, y, z)) == Block.stairSingle.blockID || n == Block.tilledField.blockID || n == Block.stairCompactPlanks.blockID || n == Block.stairCompactCobblestone.blockID)) {
            int n2 = this.getLightValueExt(x, y + 1, z, false);
            int n3 = this.getLightValueExt(x + 1, y, z, false);
            int n4 = this.getLightValueExt(x - 1, y, z, false);
            int n5 = this.getLightValueExt(x, y, z + 1, false);
            int n6 = this.getLightValueExt(x, y, z - 1, false);
            if (n3 > n2) {
                n2 = n3;
            }
            if (n4 > n2) {
                n2 = n4;
            }
            if (n5 > n2) {
                n2 = n5;
            }
            if (n6 > n2) {
                n2 = n6;
            }
            return n2;
        }
        if (y < 0) {
            return 0;
        }
        if (y >= 128) {
            n = 15 - this.worldObj.skylightSubtracted;
            if (n < 0) {
                n = 0;
            }
            return n;
        }
        n = (x >> 4) - this.chunkX;
        int n7 = (z >> 4) - this.chunkZ;
        return this.chunkArray[n][n7].getBlockLightValue(x & 0xF, y, z & 0xF, this.worldObj.skylightSubtracted);
    }

    public int getBlockMetadata(int x, int y, int z) {
        if (y < 0) {
            return 0;
        }
        if (y >= 128) {
            return 0;
        }
        int n = (x >> 4) - this.chunkX;
        int n2 = (z >> 4) - this.chunkZ;
        return this.chunkArray[n][n2].getBlockMetadata(x & 0xF, y, z & 0xF);
    }

    public Material getBlockMaterial(int x, int y, int z) {
        int n = this.getBlockId(x, y, z);
        if (n == 0) {
            return Material.air;
        }
        return Block.blocksList[n].blockMaterial;
    }

    public WorldChunkManager getWorldChunkManager() {
        return this.worldObj.getWorldChunkManager();
    }

    public boolean isBlockOpaqueCube(int x, int y, int z) {
        Block block = Block.blocksList[this.getBlockId(x, y, z)];
        if (block == null) {
            return false;
        }
        return block.isOpaqueCube();
    }

    public boolean isBlockNormalCube(int x, int y, int z) {
        Block block = Block.blocksList[this.getBlockId(x, y, z)];
        if (block == null) {
            return false;
        }
        return block.blockMaterial.getIsSolid() && block.renderAsNormalBlock();
    }
}

