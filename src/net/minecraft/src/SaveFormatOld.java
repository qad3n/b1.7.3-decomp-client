package net.minecraft.src;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class SaveFormatOld
implements ISaveFormat {
    protected final File dir;

    public SaveFormatOld(File dir) {
        if (!dir.exists()) {
            dir.mkdirs();
        }
        this.dir = dir;
    }

    @Override
    public String getName() {
        return "Old Format";
    }

    @Override
public List<SaveFormatComparator> getAll() {
    ArrayList arraylist = new ArrayList();
    for (int i = 0; i < 5; i++) {
        String s = "World" + (i + 1);
        WorldInfo worldinfo = this.getData(s);
        if (worldinfo != null) {
            arraylist.add(new SaveFormatComparator(s, "", worldinfo.getLastTimePlayed(), worldinfo.getSizeOnDisk(), false));
        }
    }
    return arraylist;
}

    @Override
    public void flushCache() {
    }

    @Override
public WorldInfo getData(String saveName) {
    File file1 = new File(this.dir, saveName);
    if (!file1.exists()) {
        return null;
    }
    File file2 = new File(file1, "level.dat");
    if (file2.exists()) {
        try {
            NBTTagCompound nbttagcompound2 = CompressedStreamTools.readCompressed(new FileInputStream(file2));
            NBTTagCompound nbttagcompound3 = nbttagcompound2.getCompoundTag("Data");
            return new WorldInfo(nbttagcompound3);
        } catch (Exception exception1) {
            exception1.printStackTrace();
        }
    }
    file2 = new File(file1, "level.dat_old");
    if (file2.exists()) {
        try {
            NBTTagCompound nbttagcompound = CompressedStreamTools.readCompressed(new FileInputStream(file2));
            NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("Data");
            return new WorldInfo(nbttagcompound1);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    return null;
}

    @Override
    public void rename(String saveName, String newName) {
        File file = new File(this.dir, saveName);
        if (!file.exists()) {
            return;
        }
        File file2 = new File(file, "level.dat");
        if (file2.exists()) {
            try {
                NBTTagCompound nBTTagCompound = CompressedStreamTools.readCompressed(new FileInputStream(file2));
                NBTTagCompound nBTTagCompound2 = nBTTagCompound.getCompoundTag("Data");
                nBTTagCompound2.setString("LevelName", newName);
                CompressedStreamTools.writeGzippedCompoundToOutputStream(nBTTagCompound, new FileOutputStream(file2));
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    @Override
    public void delete(String saveName) {
        File file = new File(this.dir, saveName);
        if (!file.exists()) {
            return;
        }
        SaveFormatOld.deleteFilesAndDirs(file.listFiles());
        file.delete();
    }

    protected static void deleteFilesAndDirs(File[] files) {
        for (int i = 0; i < files.length; ++i) {
            if (files[i].isDirectory()) {
                SaveFormatOld.deleteFilesAndDirs(files[i].listFiles());
            }
            files[i].delete();
        }
    }

    @Override
    public ISaveHandler getSaveLoader(String saveName, boolean createPlayerDataDir) {
        return new SaveHandler(this.dir, saveName, createPlayerDataDir);
    }

    @Override
    public boolean isOldMapFormat(String saveName) {
        return false;
    }

    @Override
    public boolean convertMapFormat(String saveName, IProgressUpdate listener) {
        return false;
    }
}

