package net.minecraft.src;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class ChunkCoordinates
implements Comparable<ChunkCoordinates> {
    public int x;
    public int y;
    public int z;

    public ChunkCoordinates() {
    }

    public ChunkCoordinates(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public ChunkCoordinates(ChunkCoordinates pos) {
        this.x = pos.x;
        this.y = pos.y;
        this.z = pos.z;
    }

    public boolean equals(Object object) {
        if (!(object instanceof ChunkCoordinates)) {
            return false;
        }
        ChunkCoordinates chunkCoordinates = (ChunkCoordinates)object;
        return this.x == chunkCoordinates.x && this.y == chunkCoordinates.y && this.z == chunkCoordinates.z;
    }

    public int hashCode() {
        return this.x + this.z << 8 + this.y << 16;
    }

    @Override
    public int compareTo(ChunkCoordinates chunkCoordinates) {
        if (this.y == chunkCoordinates.y) {
            if (this.z == chunkCoordinates.z) {
                return this.x - chunkCoordinates.x;
            }
            return this.z - chunkCoordinates.z;
        }
        return this.y - chunkCoordinates.y;
    }

    public double getSqDistanceTo(int x, int y, int z) {
        int n = this.x - x;
        int n2 = this.y - y;
        int n3 = this.z - z;
        return Math.sqrt(n * n + n2 * n2 + n3 * n3);
    }
}

