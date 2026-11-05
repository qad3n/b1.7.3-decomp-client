package net.minecraft.src;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class SaveFormatComparator
implements Comparable<SaveFormatComparator> {
    private final String fileName;
    private final String displayName;
    private final long lastPlayed;
    private final long size;
    private final boolean sameVersion;

    public SaveFormatComparator(String fileName, String worldName, long lastPlayed, long size, boolean sameVersion) {
        this.fileName = fileName;
        this.displayName = worldName;
        this.lastPlayed = lastPlayed;
        this.size = size;
        this.sameVersion = sameVersion;
    }

    public String getFileName() {
        return this.fileName;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public long getSize() {
        return this.size;
    }

    public boolean isSameVersion() {
        return this.sameVersion;
    }

    public long getLastPlayed() {
        return this.lastPlayed;
    }

    @Override
    public int compareTo(SaveFormatComparator saveFormatComparator) {
        if (this.lastPlayed < saveFormatComparator.lastPlayed) {
            return 1;
        }
        if (this.lastPlayed > saveFormatComparator.lastPlayed) {
            return -1;
        }
        return this.fileName.compareTo(saveFormatComparator.fileName);
    }
}

