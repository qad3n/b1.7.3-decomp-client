package net.minecraft.src;

import java.util.List;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public interface ISaveFormat {
    public String getName();

    public ISaveHandler getSaveLoader(String var1, boolean var2);

    public List<SaveFormatComparator> getAll();

    public void flushCache();

    public WorldInfo getData(String var1);

    public void delete(String var1);

    public void rename(String var1, String var2);

    public boolean isOldMapFormat(String var1);

    public boolean convertMapFormat(String var1, IProgressUpdate var2);
}

