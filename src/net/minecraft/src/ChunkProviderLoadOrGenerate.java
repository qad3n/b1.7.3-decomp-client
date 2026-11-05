package net.minecraft.src;

import java.io.IOException;

public class ChunkProviderLoadOrGenerate implements IChunkProvider {
    private Chunk blankChunk;
    private IChunkProvider chunkProvider;
    private IChunkLoader chunkLoader;
    private Chunk[] chunks;
    private World worldObj;
    int lastQueriedChunkXPos;
    int lastQueriedChunkZPos;
    private Chunk lastQueriedChunk;
    private int curChunkX;
    private int curChunkY;

    public void setCurrentChunkOver(int chunkX, int chunkZ) {
        this.curChunkX = chunkX;
        this.curChunkY = chunkZ;
    }

    public boolean canChunkExist(int chunkX, int chunkZ) {
        byte b0 = 15;
        return chunkX >= this.curChunkX - b0 && chunkZ >= this.curChunkY - b0 && chunkX <= this.curChunkX + b0 && chunkZ <= this.curChunkY + b0;
    }

    @Override
    public boolean chunkExists(int chunkX, int chunkZ) {
        if (!this.canChunkExist(chunkX, chunkZ)) {
            return false;
        }

        if (chunkX == this.lastQueriedChunkXPos && chunkZ == this.lastQueriedChunkZPos && this.lastQueriedChunk != null) {
            return true;
        }

        int i = chunkX & 31;
        int j = chunkZ & 31;
        int k = i + j * 32;
        return this.chunks[k] != null && (this.chunks[k] == this.blankChunk || this.chunks[k].isAtLocation(chunkX, chunkZ));
    }

    @Override
    public Chunk prepareChunk(int chunkX, int chunkZ) {
        return this.provideChunk(chunkX, chunkZ);
    }

    @Override
    public Chunk provideChunk(int chunkX, int chunkZ) {
        if (chunkX == this.lastQueriedChunkXPos && chunkZ == this.lastQueriedChunkZPos && this.lastQueriedChunk != null) {
            return this.lastQueriedChunk;
        }

        if (!this.worldObj.findingSpawnPoint && !this.canChunkExist(chunkX, chunkZ)) {
            return this.blankChunk;
        }

        int i = chunkX & 31;
        int j = chunkZ & 31;
        int k = i + j * 32;
        if (!this.chunkExists(chunkX, chunkZ)) {
            if (this.chunks[k] != null) {
                this.chunks[k].onChunkUnload();
                this.saveChunk(this.chunks[k]);
                this.saveExtraChunkData(this.chunks[k]);
            }

            Chunk chunk = this.loadChunkFromStorage(chunkX, chunkZ);
            if (chunk == null) {
                if (this.chunkProvider == null) {
                    chunk = this.blankChunk;
                } else {
                    chunk = this.chunkProvider.provideChunk(chunkX, chunkZ);
                    chunk.fill();
                }
            }

            this.chunks[k] = chunk;
            chunk.populateBlockLight();
            if (this.chunks[k] != null) {
                this.chunks[k].onChunkLoad();
            }

            if (!this.chunks[k].isTerrainPopulated
                && this.chunkExists(chunkX + 1, chunkZ + 1)
                && this.chunkExists(chunkX, chunkZ + 1)
                && this.chunkExists(chunkX + 1, chunkZ)) {
                this.populate(this, chunkX, chunkZ);
            }

            if (this.chunkExists(chunkX - 1, chunkZ)
                && !this.provideChunk(chunkX - 1, chunkZ).isTerrainPopulated
                && this.chunkExists(chunkX - 1, chunkZ + 1)
                && this.chunkExists(chunkX, chunkZ + 1)
                && this.chunkExists(chunkX - 1, chunkZ)) {
                this.populate(this, chunkX - 1, chunkZ);
            }

            if (this.chunkExists(chunkX, chunkZ - 1)
                && !this.provideChunk(chunkX, chunkZ - 1).isTerrainPopulated
                && this.chunkExists(chunkX + 1, chunkZ - 1)
                && this.chunkExists(chunkX, chunkZ - 1)
                && this.chunkExists(chunkX + 1, chunkZ)) {
                this.populate(this, chunkX, chunkZ - 1);
            }

            if (this.chunkExists(chunkX - 1, chunkZ - 1)
                && !this.provideChunk(chunkX - 1, chunkZ - 1).isTerrainPopulated
                && this.chunkExists(chunkX - 1, chunkZ - 1)
                && this.chunkExists(chunkX, chunkZ - 1)
                && this.chunkExists(chunkX - 1, chunkZ)) {
                this.populate(this, chunkX - 1, chunkZ - 1);
            }
        }

        this.lastQueriedChunkXPos = chunkX;
        this.lastQueriedChunkZPos = chunkZ;
        this.lastQueriedChunk = this.chunks[k];
        return this.chunks[k];
    }

    private Chunk loadChunkFromStorage(int chunkX, int chunkZ) {
        if (this.chunkLoader == null) {
            return this.blankChunk;
        }

        try {
            Chunk chunk = this.chunkLoader.loadChunk(this.worldObj, chunkX, chunkZ);
            if (chunk != null) {
                chunk.lastSaveTime = this.worldObj.getWorldTime();
            }

            return chunk;
        } catch (Exception exception) {
            exception.printStackTrace();
            return this.blankChunk;
        }
    }

    private void saveExtraChunkData(Chunk chunk) {
        if (this.chunkLoader == null) {
            return;
        }
        try {
            this.chunkLoader.saveExtraChunkData(this.worldObj, chunk);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void saveChunk(Chunk chunk) {
        if (this.chunkLoader == null) {
            return;
        }
        try {
            chunk.lastSaveTime = this.worldObj.getWorldTime();
            this.chunkLoader.saveChunk(this.worldObj, chunk);
        } catch (IOException iOException) {
            iOException.printStackTrace();
        }
    }

    @Override
    public void populate(IChunkProvider source, int chunkX, int chunkZ) {
        Chunk chunk = this.provideChunk(chunkX, chunkZ);
        if (!chunk.isTerrainPopulated) {
            chunk.isTerrainPopulated = true;
            if (this.chunkProvider != null) {
                this.chunkProvider.populate(source, chunkX, chunkZ);
                chunk.setChunkModified();
            }
        }
    }

    @Override
    public boolean saveChunks(boolean saveEntities, IProgressUpdate listener) {
        int i = 0;
        int j = 0;
        if (listener != null) {
            for (int k = 0; k < this.chunks.length; k++) {
                if (this.chunks[k] != null && this.chunks[k].needsSaving(saveEntities)) {
                    j++;
                }
            }
        }

        int i1 = 0;

        for (int l = 0; l < this.chunks.length; l++) {
            if (this.chunks[l] != null) {
                if (saveEntities && !this.chunks[l].neverSave) {
                    this.saveExtraChunkData(this.chunks[l]);
                }

                if (this.chunks[l].needsSaving(saveEntities)) {
                    this.saveChunk(this.chunks[l]);
                    this.chunks[l].isModified = false;
                    if (++i == 2 && !saveEntities) {
                        return false;
                    }

                    if (listener != null) {
                        if (++i1 % 10 == 0) {
                            listener.setLoadingProgress(i1 * 100 / j);
                        }
                    }
                }
            }
        }

        if (saveEntities) {
            if (this.chunkLoader == null) {
                return true;
            }

            this.chunkLoader.saveExtraData();
        }

        return true;
    }

    @Override
    public boolean unload100OldestChunks() {
        if (this.chunkLoader != null) {
            this.chunkLoader.tick();
        }

        return this.chunkProvider.unload100OldestChunks();
    }

    @Override
    public boolean canSave() {
        return true;
    }

    @Override
    public String makeString() {
        return "ChunkCache: " + this.chunks.length;
    }
}
