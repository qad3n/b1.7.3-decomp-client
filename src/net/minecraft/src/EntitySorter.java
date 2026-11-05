package net.minecraft.src;

import java.util.Comparator;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class EntitySorter
implements Comparator<WorldRenderer> {
    private double cameraX;
    private double cameraY;
    private double cameraZ;

    public EntitySorter(Entity camera) {
        this.cameraX = -camera.posX;
        this.cameraY = -camera.posY;
        this.cameraZ = -camera.posZ;
    }

    @Override
    public int compare(WorldRenderer worldRenderer, WorldRenderer worldRenderer2) {
        double d = (double)worldRenderer.posXPlus + this.cameraX;
        double d2 = (double)worldRenderer.posYPlus + this.cameraY;
        double d3 = (double)worldRenderer.posZPlus + this.cameraZ;
        double d4 = (double)worldRenderer2.posXPlus + this.cameraX;
        double d5 = (double)worldRenderer2.posYPlus + this.cameraY;
        double d6 = (double)worldRenderer2.posZPlus + this.cameraZ;
        return (int)((d * d + d2 * d2 + d3 * d3 - (d4 * d4 + d5 * d5 + d6 * d6)) * 1024.0);
    }
}

