package net.minecraft.src;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapData
extends MapDataBase {
    public int centerX;
    public int centerZ;
    public byte dimension;
    public byte scale;
    public byte[] colors = new byte[16384];
    public int ticks;
    public List<MapInfo> holders = new ArrayList<MapInfo>();
    private Map<EntityPlayer, MapInfo> holdersByPlayer = new HashMap<EntityPlayer, MapInfo>();
    public List<MapCoord> decorations = new ArrayList<MapCoord>();

    public MapData(String string) {
        super(string);
    }

    public void readFromNBT(NBTTagCompound nbt) {
        this.dimension = nbt.getByte("dimension");
        this.centerX = nbt.getInteger("xCenter");
        this.centerZ = nbt.getInteger("zCenter");
        this.scale = nbt.getByte("scale");
        if (this.scale < 0) {
            this.scale = 0;
        }
        if (this.scale > 4) {
            this.scale = (byte)4;
        }
        int n = nbt.getShort("width");
        int n2 = nbt.getShort("height");
        if (n == 128 && n2 == 128) {
            this.colors = nbt.getByteArray("colors");
        } else {
            byte[] byArray = nbt.getByteArray("colors");
            this.colors = new byte[16384];
            int n3 = (128 - n) / 2;
            int n4 = (128 - n2) / 2;
            for (int i = 0; i < n2; ++i) {
                int n5 = i + n4;
                if (n5 < 0 && n5 >= 128) continue;
                for (int j = 0; j < n; ++j) {
                    int n6 = j + n3;
                    if (n6 < 0 && n6 >= 128) continue;
                    this.colors[n6 + n5 * 128] = byArray[j + i * n];
                }
            }
        }
    }

    public void writeToNBT(NBTTagCompound nbt) {
        nbt.setByte("dimension", this.dimension);
        nbt.setInteger("xCenter", this.centerX);
        nbt.setInteger("zCenter", this.centerZ);
        nbt.setByte("scale", this.scale);
        nbt.setShort("width", (short)128);
        nbt.setShort("height", (short)128);
        nbt.setByteArray("colors", this.colors);
    }

    public void tickHolder(EntityPlayer player, ItemStack item) {
        if (!this.holdersByPlayer.containsKey(player)) {
            MapInfo mapInfo = new MapInfo(player);
            this.holdersByPlayer.put(player, mapInfo);
            this.holders.add(mapInfo);
        }
        this.decorations.clear();
        for (int i = 0; i < this.holders.size(); ++i) {
            MapInfo mapInfo = this.holders.get(i);
            if (mapInfo.entityplayerObj.isDead || !mapInfo.entityplayerObj.inventory.contains(item)) {
                this.holdersByPlayer.remove(mapInfo.entityplayerObj);
                this.holders.remove(mapInfo);
                continue;
            }
            float f = (float)(mapInfo.entityplayerObj.posX - (double)this.centerX) / (float)(1 << this.scale);
            float f2 = (float)(mapInfo.entityplayerObj.posZ - (double)this.centerZ) / (float)(1 << this.scale);
            int n = 64;
            int n2 = 64;
            if (!(f >= (float)(-n)) || !(f2 >= (float)(-n2)) || !(f <= (float)n) || !(f2 <= (float)n2)) continue;
            byte by = 0;
            byte by2 = (byte)((double)(f * 2.0f) + 0.5);
            byte by3 = (byte)((double)(f2 * 2.0f) + 0.5);
            byte by4 = (byte)((double)(player.rotationYaw * 16.0f / 360.0f) + 0.5);
            if (this.dimension < 0) {
                int n3 = this.ticks / 10;
                by4 = (byte)(n3 * n3 * 34187121 + n3 * 121 >> 15 & 0xF);
            }
            if (mapInfo.entityplayerObj.dimension != this.dimension) continue;
            this.decorations.add(new MapCoord(by, by2, by3, by4));
        }
    }

    public void markDirty(int index, int minY, int maxY) {
        super.markDirty();
        for (int i = 0; i < this.holders.size(); i++) {
            MapData.MapInfo mapdata$mapinfo = this.holders.get(i);
            if (mapdata$mapinfo.dirtyMinY[index] < 0 || mapdata$mapinfo.dirtyMinY[index] > minY) {
                mapdata$mapinfo.dirtyMinY[index] = minY;
            }
            if (mapdata$mapinfo.dirtyMaxY[index] < 0 || mapdata$mapinfo.dirtyMaxY[index] < maxY) {
                mapdata$mapinfo.dirtyMaxY[index] = maxY;
            }
        }
    }

    public void update(byte[] data) {
        if (data[0] == 0) {
            int n = data[1] & 0xFF;
            int n2 = data[2] & 0xFF;
            for (int i = 0; i < data.length - 3; ++i) {
                this.colors[(i + n2) * 128 + n] = data[i + 3];
            }
            this.markDirty();
        } else if (data[0] == 1) {
            this.decorations.clear();
            for (int i = 0; i < (data.length - 1) / 3; ++i) {
                byte by = (byte)(data[i * 3 + 1] % 16);
                byte by2 = data[i * 3 + 2];
                byte by3 = data[i * 3 + 3];
                byte by4 = (byte)(data[i * 3 + 1] / 16);
                this.decorations.add(new MapCoord(by, by2, by3, by4));
            }
        }
    }

    public class MapCoord {
        public byte type;
        public byte x;
        public byte z;
        public byte rotation;

        public MapCoord(byte type, byte x, byte z, byte rotation) {
            this.type = type;
            this.x = x;
            this.z = z;
            this.rotation = rotation;
        }
    }

    public class MapInfo {
        public final EntityPlayer entityplayerObj;
        public int[] dirtyMinY = new int[128];
        public int[] dirtyMaxY = new int[128];
        private int nextDirtyPixel = 0;
        private int cooldown = 0;

        public MapInfo(EntityPlayer player) {
            this.entityplayerObj = player;
            for (int i = 0; i < this.dirtyMinY.length; ++i) {
                this.dirtyMinY[i] = 0;
                this.dirtyMaxY[i] = 127;
            }
        }
    }
}

