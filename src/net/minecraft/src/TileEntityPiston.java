package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;

public class TileEntityPiston
extends TileEntity {
    private int storedBlockID;
    private int storedMetadata;
    private int facing;
    private boolean extending;
    private boolean source;
    private float progress;
    private float lastProgress;
    private static List<Entity> movedEntities = new ArrayList<Entity>();

    public TileEntityPiston() {
    }

    public TileEntityPiston(int movedBlock, int movedMetadata, int facing, boolean extending, boolean source) {
        this.storedBlockID = movedBlock;
        this.storedMetadata = movedMetadata;
        this.facing = facing;
        this.extending = extending;
        this.source = source;
    }

    public int getStoredBlockID() {
        return this.storedBlockID;
    }

    public int getBlockMetadata() {
        return this.storedMetadata;
    }

    public boolean isExtending() {
        return this.extending;
    }

    public int getFacing() {
        return this.facing;
    }

    public boolean isSource() {
        return this.source;
    }

    public float getProgress(float tickDelta) {
        if (tickDelta > 1.0f) {
            tickDelta = 1.0f;
        }
        return this.lastProgress + (this.progress - this.lastProgress) * tickDelta;
    }

    public float getRenderOffsetX(float tickDelta) {
        if (this.extending) {
            return (this.getProgress(tickDelta) - 1.0f) * (float)PistonBlockTextures.X_OFFSET[this.facing];
        }
        return (1.0f - this.getProgress(tickDelta)) * (float)PistonBlockTextures.X_OFFSET[this.facing];
    }

    public float getRenderOffsetY(float tickDelta) {
        if (this.extending) {
            return (this.getProgress(tickDelta) - 1.0f) * (float)PistonBlockTextures.Y_OFFSET[this.facing];
        }
        return (1.0f - this.getProgress(tickDelta)) * (float)PistonBlockTextures.Y_OFFSET[this.facing];
    }

    public float getRenderOffsetZ(float tickDelta) {
        if (this.extending) {
            return (this.getProgress(tickDelta) - 1.0f) * (float)PistonBlockTextures.Z_OFFSET[this.facing];
        }
        return (1.0f - this.getProgress(tickDelta)) * (float)PistonBlockTextures.Z_OFFSET[this.facing];
    }

    private void moveEntities(float progress, float amount) {
        if (!this.extending) {
            progress--;
        } else {
            progress = 1.0F - progress;
        }
        AxisAlignedBB axisalignedbb = Block.pistonMoving.getCollisionShape(this.worldObj, this.xCoord, this.yCoord, this.zCoord, this.storedBlockID, progress, this.facing);
        if (axisalignedbb != null) {
            List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(null, axisalignedbb);
            if (!list.isEmpty()) {
                movedEntities.addAll(list);
                for (Entity entity : movedEntities) {
                    entity.moveEntity(amount * PistonBlockTextures.X_OFFSET[this.facing], amount * PistonBlockTextures.Y_OFFSET[this.facing], amount * PistonBlockTextures.Z_OFFSET[this.facing]);
                }
                movedEntities.clear();
            }
        }
    }

    public void finish() {
        if (this.lastProgress < 1.0F) {
            this.lastProgress = this.progress = 1.0F;
            this.worldObj.removeBlockTileEntity(this.xCoord, this.yCoord, this.zCoord);
            this.markRemoved();
            if (this.worldObj.getBlockId(this.xCoord, this.yCoord, this.zCoord) == Block.pistonMoving.blockID) {
                this.worldObj.setBlockAndMetadataWithNotify(this.xCoord, this.yCoord, this.zCoord, this.storedBlockID, this.storedMetadata);
            }
        }
    }

    public void updateEntity() {
        this.lastProgress = this.progress;
        if (this.lastProgress >= 1.0f) {
            this.moveEntities(1.0f, 0.25f);
            this.worldObj.removeBlockTileEntity(this.xCoord, this.yCoord, this.zCoord);
            this.markRemoved();
            if (this.worldObj.getBlockId(this.xCoord, this.yCoord, this.zCoord) == Block.pistonMoving.blockID) {
                this.worldObj.setBlockAndMetadataWithNotify(this.xCoord, this.yCoord, this.zCoord, this.storedBlockID, this.storedMetadata);
            }
            return;
        }
        this.progress += 0.5f;
        if (this.progress >= 1.0f) {
            this.progress = 1.0f;
        }
        if (this.extending) {
            this.moveEntities(this.progress, this.progress - this.lastProgress + 0.0625f);
        }
    }

    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.storedBlockID = nbt.getInteger("blockId");
        this.storedMetadata = nbt.getInteger("blockData");
        this.facing = nbt.getInteger("facing");
        this.lastProgress = this.progress = nbt.getFloat("progress");
        this.extending = nbt.getBoolean("extending");
    }

    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setInteger("blockId", this.storedBlockID);
        nbt.setInteger("blockData", this.storedMetadata);
        nbt.setInteger("facing", this.facing);
        nbt.setFloat("progress", this.lastProgress);
        nbt.setBoolean("extending", this.extending);
    }
}

