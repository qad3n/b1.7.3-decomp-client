package net.minecraft.src;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChunkProviderClient
implements IChunkProvider {
    private Chunk blankChunk;
    private Map<ChunkCoordIntPair, Chunk> chunkMapping = new HashMap<ChunkCoordIntPair, Chunk>();
    private List<Chunk> chunks = new ArrayList<Chunk>();
    private World worldObj;

    public ChunkProviderClient(World world) {
        this.blankChunk = new EmptyChunk(world, new byte[32768], 0, 0);
        this.worldObj = world;
    }

    public boolean chunkExists(int chunkX, int chunkZ) {
        if (this != null) {
            return true;
        }
        ChunkCoordIntPair chunkCoordIntPair = new ChunkCoordIntPair(chunkX, chunkZ);
        return this.chunkMapping.containsKey(chunkCoordIntPair);
    }

    public void unloadChunk(int chunkX, int chunkZ) {
        Chunk chunk = this.provideChunk(chunkX, chunkZ);
        if (!chunk.isEmpty()) {
            chunk.onChunkUnload();
        }
        this.chunkMapping.remove(new ChunkCoordIntPair(chunkX, chunkZ));
        this.chunks.remove(chunk);
    }

    public Chunk prepareChunk(int chunkX, int chunkZ) {
        ChunkCoordIntPair chunkCoordIntPair = new ChunkCoordIntPair(chunkX, chunkZ);
        byte[] byArray = new byte[32768];
        Chunk chunk = new Chunk(this.worldObj, byArray, chunkX, chunkZ);
        Arrays.fill(chunk.skylightMap.data, (byte)-1);
        this.chunkMapping.put(chunkCoordIntPair, chunk);
        chunk.isChunkLoaded = true;
        return chunk;
    }

    public Chunk provideChunk(int chunkX, int chunkZ) {
        ChunkCoordIntPair chunkCoordIntPair = new ChunkCoordIntPair(chunkX, chunkZ);
        Chunk chunk = this.chunkMapping.get(chunkCoordIntPair);
        if (chunk == null) {
            return this.blankChunk;
        }
        return chunk;
    }

    public boolean saveChunks(boolean saveEntities, IProgressUpdate listener) {
        return true;
    }

    public boolean unload100OldestChunks() {
        return false;
    }

    public boolean canSave() {
        return false;
    }

    public void populate(IChunkProvider source, int chunkX, int chunkZ) {
    }

    public String makeString() {
        return "MultiplayerChunkCache: " + this.chunkMapping.size();
    }
}

