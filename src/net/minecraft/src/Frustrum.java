package net.minecraft.src;


public class Frustrum
implements ICamera {
    private ClippingHelper clippingHelper = ClippingHelperImpl.getInstance();
    private double xPosition;
    private double yPosition;
    private double zPosition;

    public void setPosition(double offsetX, double offsetY, double offsetZ) {
        this.xPosition = offsetX;
        this.yPosition = offsetY;
        this.zPosition = offsetZ;
    }

    public boolean isBoxInFrustum(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        return this.clippingHelper.isBoxInFrustum(minX - this.xPosition, minY - this.yPosition, minZ - this.zPosition, maxX - this.xPosition, maxY - this.yPosition, maxZ - this.zPosition);
    }

    public boolean isBoundingBoxInFrustum(AxisAlignedBB shape) {
        return this.isBoxInFrustum(shape.minX, shape.minY, shape.minZ, shape.maxX, shape.maxY, shape.maxZ);
    }
}

