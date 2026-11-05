package net.minecraft.src;

import java.util.HashMap;
import java.util.Map;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class TileEntity {
    private static Map<String, Class<? extends TileEntity>> nameToClassMap = new HashMap<String, Class<? extends TileEntity>>();
    private static Map<Class<? extends TileEntity>, String> classToNameMap = new HashMap<Class<? extends TileEntity>, String>();
    public World worldObj;
    public int xCoord;
    public int yCoord;
    public int zCoord;
    protected boolean removed;

    private static void addMapping(Class<? extends TileEntity> type, String id) {
        if (classToNameMap.containsKey(id)) {
            throw new IllegalArgumentException("Duplicate id: " + id);
        }
        nameToClassMap.put(id, type);
        classToNameMap.put(type, id);
    }

    public void readFromNBT(NBTTagCompound nbt) {
        this.xCoord = nbt.getInteger("x");
        this.yCoord = nbt.getInteger("y");
        this.zCoord = nbt.getInteger("z");
    }

    public void writeToNBT(NBTTagCompound nbt) {
        String string = classToNameMap.get(this.getClass());
        if (string == null) {
            throw new RuntimeException(this.getClass() + " is missing a mapping! This is a bug!");
        }
        nbt.setString("id", string);
        nbt.setInteger("x", this.xCoord);
        nbt.setInteger("y", this.yCoord);
        nbt.setInteger("z", this.zCoord);
    }

    public void updateEntity() {
    }

    public static TileEntity createAndLoadEntity(NBTTagCompound nbt) {
        TileEntity tileEntity = null;
        try {
            Class<? extends TileEntity> clazz = nameToClassMap.get(nbt.getString("id"));
            if (clazz != null) {
                tileEntity = clazz.newInstance();
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        if (tileEntity != null) {
            tileEntity.readFromNBT(nbt);
        } else {
            System.out.println("Skipping TileEntity with id " + nbt.getString("id"));
        }
        return tileEntity;
    }

    public int getBlockMetadata() {
        return this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord);
    }

    public void onInventoryChanged() {
        if (this.worldObj != null) {
            this.worldObj.notifyBlockEntityChanged(this.xCoord, this.yCoord, this.zCoord, this);
        }
    }

    public double getDistanceFrom(double x, double y, double z) {
        double d = (double)this.xCoord + 0.5 - x;
        double d2 = (double)this.yCoord + 0.5 - y;
        double d3 = (double)this.zCoord + 0.5 - z;
        return d * d + d2 * d2 + d3 * d3;
    }

    public Block getBlockType() {
        return Block.blocksList[this.worldObj.getBlockId(this.xCoord, this.yCoord, this.zCoord)];
    }

    public boolean isRemoved() {
        return this.removed;
    }

    public void markRemoved() {
        this.removed = true;
    }

    public void cancelRemoval() {
        this.removed = false;
    }

    static {
        TileEntity.addMapping(TileEntityFurnace.class, "Furnace");
        TileEntity.addMapping(TileEntityChest.class, "Chest");
        TileEntity.addMapping(BlockJukeBox.TileEntityRecordPlayer.class, "RecordPlayer");
        TileEntity.addMapping(TileEntityDispenser.class, "Trap");
        TileEntity.addMapping(TileEntitySign.class, "Sign");
        TileEntity.addMapping(TileEntityMobSpawner.class, "MobSpawner");
        TileEntity.addMapping(TileEntityNote.class, "Music");
        TileEntity.addMapping(TileEntityPiston.class, "Piston");
    }
}

