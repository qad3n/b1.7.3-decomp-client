package net.minecraft.src;

import java.nio.IntBuffer;
import org.lwjgl.opengl.GL11;

public class RenderList {
    private int x;
    private int y;
    private int z;
    private float offsetX;
    private float offsetY;
    private float offsetZ;
    private IntBuffer glListBuffer = GLAllocation.createDirectIntBuffer(65536);
    private boolean initialized = false;
    private boolean rendered = false;

    public void init(int x, int y, int z, double offsetX, double offsetY, double offsetZ) {
        this.initialized = true;
        this.glListBuffer.clear();
        this.x = x;
        this.y = y;
        this.z = z;
        this.offsetX = (float)offsetX;
        this.offsetY = (float)offsetY;
        this.offsetZ = (float)offsetZ;
    }

    public boolean isAt(int x, int y, int z) {
        if (!this.initialized) {
            return false;
        }
        return x == this.x && y == this.y && z == this.z;
    }

    public void addGlList(int glList) {
        this.glListBuffer.put(glList);
        if (this.glListBuffer.remaining() == 0) {
            this.render();
        }
    }

    public void render() {
        if (!this.initialized) {
            return;
        }
        if (!this.rendered) {
            this.glListBuffer.flip();
            this.rendered = true;
        }
        if (this.glListBuffer.remaining() > 0) {
            GL11.glPushMatrix();
            GL11.glTranslatef((float)this.x - this.offsetX, (float)this.y - this.offsetY, (float)this.z - this.offsetZ);
            GL11.glCallLists(this.glListBuffer);
            GL11.glPopMatrix();
        }
    }

    public void clear() {
        this.initialized = false;
        this.rendered = false;
    }
}

