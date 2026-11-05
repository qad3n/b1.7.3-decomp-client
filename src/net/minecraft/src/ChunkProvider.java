// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.src;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ChunkProvider implements IChunkProvider
{
    private Set<Integer> droppedChunksSet;
    private Chunk empty;
    private IChunkProvider chunkProvider;
    private IChunkLoader chunkLoader;
    private Map<Integer, Chunk> chunkMap;
    private List<Chunk> chunkList;
    private World world;
    
    public ChunkProvider(final World world, final IChunkLoader storage, final IChunkProvider generator) {
        this.droppedChunksSet = new HashSet<Integer>();
        this.chunkMap = new HashMap<Integer, Chunk>();
        this.chunkList = new ArrayList<Chunk>();
        this.empty = new EmptyChunk(world, new byte[32768], 0, 0);
        this.world = world;
        this.chunkLoader = storage;
        this.chunkProvider = generator;
    }
    
    public boolean chunkExists(final int chunkX, final int chunkZ) {
        return this.chunkMap.containsKey(ChunkCoordIntPair.chunkXZ2Int(chunkX, chunkZ));
    }
    
    public Chunk prepareChunk(final int chunkX, final int chunkZ) {
        final int chunkXZ2Int = ChunkCoordIntPair.chunkXZ2Int(chunkX, chunkZ);
        this.droppedChunksSet.remove(chunkXZ2Int);
        Chunk chunk = this.chunkMap.get(chunkXZ2Int);
        if (chunk == null) {
            chunk = this.loadChunkFromFile(chunkX, chunkZ);
            if (chunk == null) {
                if (this.chunkProvider == null) {
                    chunk = this.empty;
                }
                else {
                    chunk = this.chunkProvider.provideChunk(chunkX, chunkZ);
                }
            }
            this.chunkMap.put(chunkXZ2Int, chunk);
            this.chunkList.add(chunk);
            if (chunk != null) {
                chunk.populateBlockLight();
                chunk.onChunkLoad();
            }
            if (!chunk.isTerrainPopulated && this.chunkExists(chunkX + 1, chunkZ + 1) && this.chunkExists(chunkX, chunkZ + 1) && this.chunkExists(chunkX + 1, chunkZ)) {
                this.populate(this, chunkX, chunkZ);
            }
            if (this.chunkExists(chunkX - 1, chunkZ) && !this.provideChunk(chunkX - 1, chunkZ).isTerrainPopulated && this.chunkExists(chunkX - 1, chunkZ + 1) && this.chunkExists(chunkX, chunkZ + 1) && this.chunkExists(chunkX - 1, chunkZ)) {
                this.populate(this, chunkX - 1, chunkZ);
            }
            if (this.chunkExists(chunkX, chunkZ - 1) && !this.provideChunk(chunkX, chunkZ - 1).isTerrainPopulated && this.chunkExists(chunkX + 1, chunkZ - 1) && this.chunkExists(chunkX, chunkZ - 1) && this.chunkExists(chunkX + 1, chunkZ)) {
                this.populate(this, chunkX, chunkZ - 1);
            }
            if (this.chunkExists(chunkX - 1, chunkZ - 1) && !this.provideChunk(chunkX - 1, chunkZ - 1).isTerrainPopulated && this.chunkExists(chunkX - 1, chunkZ - 1) && this.chunkExists(chunkX, chunkZ - 1) && this.chunkExists(chunkX - 1, chunkZ)) {
                this.populate(this, chunkX - 1, chunkZ - 1);
            }
        }
        return chunk;
    }
    
    public Chunk provideChunk(final int chunkX, final int chunkZ) {
        final Chunk chunk = this.chunkMap.get(ChunkCoordIntPair.chunkXZ2Int(chunkX, chunkZ));
        if (chunk == null) {
            return this.prepareChunk(chunkX, chunkZ);
        }
        return chunk;
    }
    
    private Chunk loadChunkFromFile(final int chunkX, final int chunkZ) {
        if (this.chunkLoader == null) {
            return null;
        }
        try {
            final Chunk loadChunk = this.chunkLoader.loadChunk(this.world, chunkX, chunkZ);
            if (loadChunk != null) {
                loadChunk.lastSaveTime = this.world.getWorldTime();
            }
            return loadChunk;
        }
        catch (final Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    private void saveEntities(final Chunk chunk) {
        if (this.chunkLoader == null) {
            return;
        }
        try {
            this.chunkLoader.saveExtraChunkData(this.world, chunk);
        }
        catch (final Exception ex) {
            ex.printStackTrace();
        }
    }
    
    private void saveChunk(final Chunk chunk) {
        if (this.chunkLoader == null) {
            return;
        }
        try {
            chunk.lastSaveTime = this.world.getWorldTime();
            this.chunkLoader.saveChunk(this.world, chunk);
        }
        catch (final IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public void populate(final IChunkProvider source, final int chunkX, final int chunkZ) {
        final Chunk provideChunk = this.provideChunk(chunkX, chunkZ);
        if (!provideChunk.isTerrainPopulated) {
            provideChunk.isTerrainPopulated = true;
            if (this.chunkProvider != null) {
                this.chunkProvider.populate(source, chunkX, chunkZ);
                provideChunk.setChunkModified();
            }
        }
    }
    
    public boolean saveChunks(final boolean saveEntities, final IProgressUpdate listener) {
        int n = 0;
        for (int i = 0; i < this.chunkList.size(); ++i) {
            final Chunk chunk = this.chunkList.get(i);
            if (saveEntities && !chunk.neverSave) {
                this.saveEntities(chunk);
            }
            if (chunk.needsSaving(saveEntities)) {
                this.saveChunk(chunk);
                chunk.isModified = false;
                if (++n == 24 && !saveEntities) {
                    return false;
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
    
    public boolean unload100OldestChunks() {
        for (int i = 0; i < 100; ++i) {
            if (!this.droppedChunksSet.isEmpty()) {
                final Integer n = this.droppedChunksSet.iterator().next();
                final Chunk chunk = this.chunkMap.get(n);
                chunk.onChunkUnload();
                this.saveChunk(chunk);
                this.saveEntities(chunk);
                this.droppedChunksSet.remove(n);
                this.chunkMap.remove(n);
                this.chunkList.remove(chunk);
            }
        }
        if (this.chunkLoader != null) {
            this.chunkLoader.tick();
        }
        return this.chunkProvider.unload100OldestChunks();
    }
    
    public boolean canSave() {
        return true;
    }
    
    public String makeString() {
        return "ServerChunkCache: " + this.chunkMap.size() + " Drop: " + this.droppedChunksSet.size();
    }
}
