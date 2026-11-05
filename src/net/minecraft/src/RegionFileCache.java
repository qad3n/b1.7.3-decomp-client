package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

public class RegionFileCache {
    private static final Map<File, Reference<RegionFile>> cache = new HashMap<>();

    private RegionFileCache() {
    }

    public static synchronized RegionFile getRegionFile(File worldDir, int chunkX, int chunkZ) {
        File file1 = new File(worldDir, "region");
        File file2 = new File(file1, "r." + (chunkX >> 5) + "." + (chunkZ >> 5) + ".mcr");
        Reference reference = cache.get(file2);
        if (reference != null) {
            RegionFile regionfile = (RegionFile)reference.get();
            if (regionfile != null) {
                return regionfile;
            }
        }

        if (!file1.exists()) {
            file1.mkdirs();
        }

        if (cache.size() >= 256) {
            flush();
        }

        RegionFile regionfile1 = new RegionFile(file2);
        cache.put(file2, new SoftReference<>(regionfile1));
        return regionfile1;
    }

    public static synchronized void flush() {
        for (Reference reference : cache.values()) {
            try {
                RegionFile regionfile = (RegionFile)reference.get();
                if (regionfile != null) {
                    regionfile.close();
                }
            } catch (IOException ioexception) {
                ioexception.printStackTrace();
            }
        }

        cache.clear();
    }

    public static int getSizeDelta(File worldDir, int chunkX, int chunkZ) {
        RegionFile regionfile = getRegionFile(worldDir, chunkX, chunkZ);
        return regionfile.resetBytesWritten();
    }

    public static DataInputStream getChunkInputStream(File worldDir, int chunkX, int chunkZ) {
        RegionFile regionfile = getRegionFile(worldDir, chunkX, chunkZ);
        return regionfile.getChunkDataInputStream(chunkX & 31, chunkZ & 31);
    }

    public static DataOutputStream getChunkOutputStream(File worldDir, int chunkX, int chunkZ) {
        RegionFile regionfile = getRegionFile(worldDir, chunkX, chunkZ);
        return regionfile.getChunkDataOutputStream(chunkX & 31, chunkZ & 31);
    }
}
