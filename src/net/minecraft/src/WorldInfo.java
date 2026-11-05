package net.minecraft.src;

import java.util.List;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class WorldInfo {
    private long randomSeed;
    private int spawnX;
    private int spawnY;
    private int spawnZ;
    private long worldTime;
    private long lastTimePlayed;
    private long sizeOnDisk;
    private NBTTagCompound playerTag;
    private int dimension;
    private String levelName;
    private int saveVersion;
    private boolean raining;
    private int rainTime;
    private boolean thundering;
    private int thunderTime;

    public WorldInfo(NBTTagCompound nbt) {
        this.randomSeed = nbt.getLong("RandomSeed");
        this.spawnX = nbt.getInteger("SpawnX");
        this.spawnY = nbt.getInteger("SpawnY");
        this.spawnZ = nbt.getInteger("SpawnZ");
        this.worldTime = nbt.getLong("Time");
        this.lastTimePlayed = nbt.getLong("LastPlayed");
        this.sizeOnDisk = nbt.getLong("SizeOnDisk");
        this.levelName = nbt.getString("LevelName");
        this.saveVersion = nbt.getInteger("version");
        this.rainTime = nbt.getInteger("rainTime");
        this.raining = nbt.getBoolean("raining");
        this.thunderTime = nbt.getInteger("thunderTime");
        this.thundering = nbt.getBoolean("thundering");
        if (nbt.hasKey("Player")) {
            this.playerTag = nbt.getCompoundTag("Player");
            this.dimension = this.playerTag.getInteger("Dimension");
        }
    }

    public WorldInfo(long seed, String worldName) {
        this.randomSeed = seed;
        this.levelName = worldName;
    }

    public WorldInfo(WorldInfo data) {
        this.randomSeed = data.randomSeed;
        this.spawnX = data.spawnX;
        this.spawnY = data.spawnY;
        this.spawnZ = data.spawnZ;
        this.worldTime = data.worldTime;
        this.lastTimePlayed = data.lastTimePlayed;
        this.sizeOnDisk = data.sizeOnDisk;
        this.playerTag = data.playerTag;
        this.dimension = data.dimension;
        this.levelName = data.levelName;
        this.saveVersion = data.saveVersion;
        this.rainTime = data.rainTime;
        this.raining = data.raining;
        this.thunderTime = data.thunderTime;
        this.thundering = data.thundering;
    }

    public NBTTagCompound getNBTTagCompound() {
        NBTTagCompound nBTTagCompound = new NBTTagCompound();
        this.updateTagCompound(nBTTagCompound, this.playerTag);
        return nBTTagCompound;
    }

    public NBTTagCompound getNBTTagCompoundWithPlayer(List<EntityPlayer> players) {
        NBTTagCompound nBTTagCompound = new NBTTagCompound();
        EntityPlayer entityPlayer = null;
        NBTTagCompound nBTTagCompound2 = null;
        if (players.size() > 0) {
            entityPlayer = players.get(0);
        }
        if (entityPlayer != null) {
            nBTTagCompound2 = new NBTTagCompound();
            entityPlayer.writeToNBT(nBTTagCompound2);
        }
        this.updateTagCompound(nBTTagCompound, nBTTagCompound2);
        return nBTTagCompound;
    }

    private void updateTagCompound(NBTTagCompound nbt, NBTTagCompound playerData) {
        nbt.setLong("RandomSeed", this.randomSeed);
        nbt.setInteger("SpawnX", this.spawnX);
        nbt.setInteger("SpawnY", this.spawnY);
        nbt.setInteger("SpawnZ", this.spawnZ);
        nbt.setLong("Time", this.worldTime);
        nbt.setLong("SizeOnDisk", this.sizeOnDisk);
        nbt.setLong("LastPlayed", System.currentTimeMillis());
        nbt.setString("LevelName", this.levelName);
        nbt.setInteger("version", this.saveVersion);
        nbt.setInteger("rainTime", this.rainTime);
        nbt.setBoolean("raining", this.raining);
        nbt.setInteger("thunderTime", this.thunderTime);
        nbt.setBoolean("thundering", this.thundering);
        if (playerData != null) {
            nbt.setCompoundTag("Player", playerData);
        }
    }

    public long getRandomSeed() {
        return this.randomSeed;
    }

    public int getSpawnX() {
        return this.spawnX;
    }

    public int getSpawnY() {
        return this.spawnY;
    }

    public int getSpawnZ() {
        return this.spawnZ;
    }

    public long getWorldTime() {
        return this.worldTime;
    }

    public long getSizeOnDisk() {
        return this.sizeOnDisk;
    }

    public NBTTagCompound getPlayerNBTTagCompound() {
        return this.playerTag;
    }

    public int getDimension() {
        return this.dimension;
    }

    public void setSpawnX(int x) {
        this.spawnX = x;
    }

    public void setSpawnY(int y) {
        this.spawnY = y;
    }

    public void setSpawnZ(int z) {
        this.spawnZ = z;
    }

    public void setWorldTime(long time) {
        this.worldTime = time;
    }

    public void setSizeOnDisk(long size) {
        this.sizeOnDisk = size;
    }

    public void setPlayerNBTTagCompound(NBTTagCompound playerData) {
        this.playerTag = playerData;
    }

    public void setSpawn(int x, int y, int z) {
        this.spawnX = x;
        this.spawnY = y;
        this.spawnZ = z;
    }

    public String getWorldName() {
        return this.levelName;
    }

    public void setWorldName(String name) {
        this.levelName = name;
    }

    public int getSaveVersion() {
        return this.saveVersion;
    }

    public void setSaveVersion(int version) {
        this.saveVersion = version;
    }

    public long getLastTimePlayed() {
        return this.lastTimePlayed;
    }

    public boolean getThundering() {
        return this.thundering;
    }

    public void setThundering(boolean thundering) {
        this.thundering = thundering;
    }

    public int getThunderTime() {
        return this.thunderTime;
    }

    public void setThunderTime(int thunderTime) {
        this.thunderTime = thunderTime;
    }

    public boolean getRaining() {
        return this.raining;
    }

    public void setRaining(boolean raining) {
        this.raining = raining;
    }

    public int getRainTime() {
        return this.rainTime;
    }

    public void setRainTime(int rainTime) {
        this.rainTime = rainTime;
    }
}

