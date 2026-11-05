package net.minecraft.src;


public class PositionTextureVertex {
    public Vec3D vector3D;
    public float texturePositionX;
    public float texturePositionY;

    public PositionTextureVertex(float x, float y, float z, float u, float v) {
        this(Vec3D.createVectorHelper(x, y, z), u, v);
    }

    public PositionTextureVertex setTexturePosition(float u, float v) {
        return new PositionTextureVertex(this, u, v);
    }

    public PositionTextureVertex(PositionTextureVertex pos, float u, float v) {
        this.vector3D = pos.vector3D;
        this.texturePositionX = u;
        this.texturePositionY = v;
    }

    public PositionTextureVertex(Vec3D pos, float u, float v) {
        this.vector3D = pos;
        this.texturePositionX = u;
        this.texturePositionY = v;
    }
}

