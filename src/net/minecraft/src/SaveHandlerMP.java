package net.minecraft.src;

import java.io.File;
import java.util.List;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class SaveHandlerMP
implements ISaveHandler {
    @Override
    public WorldInfo loadWorldInfo() {
        return null;
    }

    @Override
    public void checkSessionLock() {
    }

    @Override
    public IChunkLoader getChunkLoader(WorldProvider dimension) {
        return null;
    }

    @Override
    public void saveWorldInfoAndPlayer(WorldInfo data, List<EntityPlayer> players) {
    }

    @Override
    public void saveWorldInfo(WorldInfo data) {
    }

    @Override
    public File getDataFile(String name) {
        return null;
    }
}

