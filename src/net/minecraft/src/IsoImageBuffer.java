package net.minecraft.src;

import java.awt.image.BufferedImage;

public class IsoImageBuffer {
    public BufferedImage image;
    public World worldObj;
    public int chunkX;
    public int chunkZ;
    public boolean rendered = false;
    public boolean empty = false;
    public int lastVisible = 0;
    public boolean toBeRendered = false;

    public IsoImageBuffer(World world, int chunkX, int chunkZ) {
        this.worldObj = world;
        this.init(chunkX, chunkZ);
    }

    public void init(int chunkX, int chunkZ) {
        this.rendered = false;
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        this.lastVisible = 0;
        this.toBeRendered = false;
    }

    public void init(World world, int chunkX, int chunkZ) {
        this.worldObj = world;
        this.init(chunkX, chunkZ);
    }
}

