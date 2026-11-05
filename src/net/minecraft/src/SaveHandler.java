package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class SaveHandler implements ISaveHandler {
    private static final Logger logger = Logger.getLogger("Minecraft");
    private final File saveDirectory;
    private final File playersDirectory;
    private final File dataDir;
    private final long now = System.currentTimeMillis();

    public SaveHandler(File savesDir, String name, boolean createPlayerDataDir) {
        this.saveDirectory = new File(savesDir, name);
        this.saveDirectory.mkdirs();
        this.playersDirectory = new File(this.saveDirectory, "players");
        this.dataDir = new File(this.saveDirectory, "data");
        this.dataDir.mkdirs();
        if (createPlayerDataDir) {
            this.playersDirectory.mkdirs();
        }

        this.writeSessionLock();
    }

    private void writeSessionLock() {
        try {
            File file1 = new File(this.saveDirectory, "session.lock");
            DataOutputStream dataoutputstream = new DataOutputStream(new FileOutputStream(file1));

            try {
                dataoutputstream.writeLong(this.now);
            } finally {
                dataoutputstream.close();
            }
        } catch (IOException ioexception) {
            ioexception.printStackTrace();
            throw new RuntimeException("Failed to check session lock, aborting");
        }
    }

    protected File getSaveDirectory() {
        return this.saveDirectory;
    }

    @Override
    public void checkSessionLock() {
        try {
            File file1 = new File(this.saveDirectory, "session.lock");
            DataInputStream datainputstream = new DataInputStream(new FileInputStream(file1));

            try {
                if (datainputstream.readLong() != this.now) {
                    throw new MinecraftException("The save is being accessed from another location, aborting");
                }
            } finally {
                datainputstream.close();
            }
        } catch (IOException ioexception) {
            throw new MinecraftException("Failed to check session lock, aborting");
        }
    }

    @Override
    public IChunkLoader getChunkLoader(WorldProvider dimension) {
        if (dimension instanceof WorldProviderHell) {
            File file1 = new File(this.saveDirectory, "DIM-1");
            file1.mkdirs();
            return new ChunkLoader(file1, true);
        } else {
            return new ChunkLoader(this.saveDirectory, true);
        }
    }

    @Override
    public WorldInfo loadWorldInfo() {
        File file1 = new File(this.saveDirectory, "level.dat");
        if (file1.exists()) {
            try {
                NBTTagCompound nbttagcompound2 = CompressedStreamTools.readCompressed(new FileInputStream(file1));
                NBTTagCompound nbttagcompound3 = nbttagcompound2.getCompoundTag("Data");
                return new WorldInfo(nbttagcompound3);
            } catch (Exception exception1) {
                exception1.printStackTrace();
            }
        }

        file1 = new File(this.saveDirectory, "level.dat_old");
        if (file1.exists()) {
            try {
                NBTTagCompound nbttagcompound = CompressedStreamTools.readCompressed(new FileInputStream(file1));
                NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("Data");
                return new WorldInfo(nbttagcompound1);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        return null;
    }

    @Override
    public void saveWorldInfoAndPlayer(WorldInfo data, List<EntityPlayer> players) {
        NBTTagCompound nbttagcompound = data.getNBTTagCompoundWithPlayer(players);
        NBTTagCompound nbttagcompound1 = new NBTTagCompound();
        nbttagcompound1.setTag("Data", nbttagcompound);

        try {
            File file1 = new File(this.saveDirectory, "level.dat_new");
            File file2 = new File(this.saveDirectory, "level.dat_old");
            File file3 = new File(this.saveDirectory, "level.dat");
            CompressedStreamTools.writeGzippedCompoundToOutputStream(nbttagcompound1, new FileOutputStream(file1));
            if (file2.exists()) {
                file2.delete();
            }

            file3.renameTo(file2);
            if (file3.exists()) {
                file3.delete();
            }

            file1.renameTo(file3);
            if (file1.exists()) {
                file1.delete();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void saveWorldInfo(WorldInfo data) {
        NBTTagCompound nbttagcompound = data.getNBTTagCompound();
        NBTTagCompound nbttagcompound1 = new NBTTagCompound();
        nbttagcompound1.setTag("Data", nbttagcompound);

        try {
            File file1 = new File(this.saveDirectory, "level.dat_new");
            File file2 = new File(this.saveDirectory, "level.dat_old");
            File file3 = new File(this.saveDirectory, "level.dat");
            CompressedStreamTools.writeGzippedCompoundToOutputStream(nbttagcompound1, new FileOutputStream(file1));
            if (file2.exists()) {
                file2.delete();
            }

            file3.renameTo(file2);
            if (file3.exists()) {
                file3.delete();
            }

            file1.renameTo(file3);
            if (file1.exists()) {
                file1.delete();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public File getDataFile(String name) {
        return new File(this.dataDir, name + ".dat");
    }
}
