package net.minecraft.src;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class WorldClient extends World {
    private LinkedList<WorldClient.WorldBlockPositionType> blockResets = new LinkedList<>();
    private NetClientHandler sendQueue;
    private ChunkProviderClient chunkCache;
    private MCHash<Entity> entitiesByNetworkId = new MCHash<>();
    private Set<Entity> forcedEntities = new HashSet<>();
    private Set<Entity> pendingEntities = new HashSet<>();

    public WorldClient(NetClientHandler networkHandler, long seed, int dimension) {
        super(new SaveHandlerMP(), "MpServer", WorldProvider.getProviderForDimension(dimension), seed);
        this.sendQueue = networkHandler;
        this.setSpawnPoint(new ChunkCoordinates(8, 64, 8));
        this.savedDataStorage = networkHandler.savedDataStorage;
    }

    @Override
    public void tick() {
        this.setWorldTime(this.getWorldTime() + 1L);
        int i = this.calculateSkylightSubtracted(1.0F);
        if (i != this.skylightSubtracted) {
            this.skylightSubtracted = i;

            for (int j = 0; j < this.worldAccesses.size(); j++) {
                this.worldAccesses.get(j).updateAllRenderers();
            }
        }

        for (int k = 0; k < 10 && !this.pendingEntities.isEmpty(); k++) {
            Entity entity = this.pendingEntities.iterator().next();
            if (!this.loadedEntityList.contains(entity)) {
                this.entityJoinedWorld(entity);
            }
        }

        this.sendQueue.processReadPackets();

        for (int l = 0; l < this.blockResets.size(); l++) {
            WorldClient.WorldBlockPositionType worldclient$worldblockpositiontype = this.blockResets.get(l);
            if (--worldclient$worldblockpositiontype.delay == 0) {
                super.setBlockAndMetadata(
                    worldclient$worldblockpositiontype.x,
                    worldclient$worldblockpositiontype.y,
                    worldclient$worldblockpositiontype.z,
                    worldclient$worldblockpositiontype.block,
                    worldclient$worldblockpositiontype.metadata
                );
                super.markBlockNeedsUpdate(worldclient$worldblockpositiontype.x, worldclient$worldblockpositiontype.y, worldclient$worldblockpositiontype.z);
                this.blockResets.remove(l--);
            }
        }
    }

    public void clearBlockResets(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        for (int i = 0; i < this.blockResets.size(); i++) {
            WorldClient.WorldBlockPositionType worldclient$worldblockpositiontype = this.blockResets.get(i);
            if (worldclient$worldblockpositiontype.x >= minX
                && worldclient$worldblockpositiontype.y >= minY
                && worldclient$worldblockpositiontype.z >= minZ
                && worldclient$worldblockpositiontype.x <= maxX
                && worldclient$worldblockpositiontype.y <= maxY
                && worldclient$worldblockpositiontype.z <= maxZ) {
                this.blockResets.remove(i--);
            }
        }
    }

    @Override
    protected IChunkProvider getChunkProvider() {
        this.chunkCache = new ChunkProviderClient(this);
        return this.chunkCache;
    }

    @Override
    public void setSpawnLocation() {
        this.setSpawnPoint(new ChunkCoordinates(8, 64, 8));
    }

    @Override
    protected void updateBlocksAndPlayCaveSounds() {
    }

    @Override
    public void scheduleBlockUpdate(int x, int y, int z, int block, int delay) {
    }

    @Override
    public boolean TickUpdates(boolean flush) {
        return false;
    }

    public void doPreChunk(int chunkX, int chunkZ, boolean load) {
        if (load) {
            this.chunkCache.prepareChunk(chunkX, chunkZ);
        } else {
            this.chunkCache.unloadChunk(chunkX, chunkZ);
        }

        if (!load) {
            this.markBlocksDirty(chunkX * 16, 0, chunkZ * 16, chunkX * 16 + 15, 128, chunkZ * 16 + 15);
        }
    }

    @Override
    public boolean entityJoinedWorld(Entity entity) {
        boolean flag = super.entityJoinedWorld(entity);
        this.forcedEntities.add(entity);
        if (!flag) {
            this.pendingEntities.add(entity);
        }

        return flag;
    }

    @Override
    public void setEntityDead(Entity entity) {
        super.setEntityDead(entity);
        this.forcedEntities.remove(entity);
    }

    @Override
    protected void obtainEntitySkin(Entity entity) {
        super.obtainEntitySkin(entity);
        if (this.pendingEntities.contains(entity)) {
            this.pendingEntities.remove(entity);
        }
    }

    @Override
    protected void releaseEntitySkin(Entity entity) {
        super.releaseEntitySkin(entity);
        if (this.forcedEntities.contains(entity)) {
            this.pendingEntities.add(entity);
        }
    }

    public void forceEntity(int networkId, Entity entity) {
        Entity entityx = this.getEntity(networkId);
        if (entityx != null) {
            this.setEntityDead(entityx);
        }

        this.forcedEntities.add(entity);
        entity.entityId = networkId;
        if (!this.entityJoinedWorld(entity)) {
            this.pendingEntities.add(entity);
        }

        this.entitiesByNetworkId.addKey(networkId, entity);
    }

    public Entity getEntity(int networkId) {
        return this.entitiesByNetworkId.lookup(networkId);
    }

    public Entity removeEntityFromWorld(int networkId) {
        Entity entity = this.entitiesByNetworkId.removeObject(networkId);
        if (entity != null) {
            this.forcedEntities.remove(entity);
            this.setEntityDead(entity);
        }

        return entity;
    }

    @Override
    public boolean setBlockMetadata(int x, int y, int z, int metadata) {
        int i = this.getBlockId(x, y, z);
        int j = this.getBlockMetadata(x, y, z);
        if (super.setBlockMetadata(x, y, z, metadata)) {
            this.blockResets.add(new WorldClient.WorldBlockPositionType(x, y, z, i, j));
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean setBlockAndMetadata(int x, int y, int z, int block, int metadata) {
        int i = this.getBlockId(x, y, z);
        int j = this.getBlockMetadata(x, y, z);
        if (super.setBlockAndMetadata(x, y, z, block, metadata)) {
            this.blockResets.add(new WorldClient.WorldBlockPositionType(x, y, z, i, j));
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean setBlock(int x, int y, int z, int block) {
        int i = this.getBlockId(x, y, z);
        int j = this.getBlockMetadata(x, y, z);
        if (super.setBlock(x, y, z, block)) {
            this.blockResets.add(new WorldClient.WorldBlockPositionType(x, y, z, i, j));
            return true;
        } else {
            return false;
        }
    }

    public boolean setBlockWithMetadataFromPacket(int x, int y, int z, int block, int metadata) {
        this.clearBlockResets(x, y, z, x, y, z);
        if (super.setBlockAndMetadata(x, y, z, block, metadata)) {
            this.notifyBlockChange(x, y, z, block);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void sendQuittingDisconnectingPacket() {
        this.sendQueue.sendPacketAndDisconnect(new Packet255KickDisconnect("Quitting"));
    }

    @Override
    protected void updateWeather() {
        if (!this.worldProvider.hasNoSky) {
            if (this.thunderCooldown > 0) {
                this.thunderCooldown--;
            }

            this.prevRainingStrength = this.rainingStrength;
            if (this.worldInfo.getRaining()) {
                this.rainingStrength = (float)(this.rainingStrength + 0.01);
            } else {
                this.rainingStrength = (float)(this.rainingStrength - 0.01);
            }

            if (this.rainingStrength < 0.0F) {
                this.rainingStrength = 0.0F;
            }

            if (this.rainingStrength > 1.0F) {
                this.rainingStrength = 1.0F;
            }

            this.prevThunderingStrength = this.thunderingStrength;
            if (this.worldInfo.getThundering()) {
                this.thunderingStrength = (float)(this.thunderingStrength + 0.01);
            } else {
                this.thunderingStrength = (float)(this.thunderingStrength - 0.01);
            }

            if (this.thunderingStrength < 0.0F) {
                this.thunderingStrength = 0.0F;
            }

            if (this.thunderingStrength > 1.0F) {
                this.thunderingStrength = 1.0F;
            }
        }
    }

    class WorldBlockPositionType {
        int x;
        int y;
        int z;
        int delay;
        int block;
        int metadata;

        public WorldBlockPositionType(int x, int y, int z, int block, int metadata) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.delay = 80;
            this.block = block;
            this.metadata = metadata;
        }
    }
}
