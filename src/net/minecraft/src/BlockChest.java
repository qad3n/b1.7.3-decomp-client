package net.minecraft.src;

import java.util.Random;

public class BlockChest
extends BlockContainer {
    private Random random = new Random();

    protected BlockChest(int id) {
        super(id, Material.wood);
        this.blockIndexInTexture = 26;
    }

    public int getBlockTexture(IBlockAccess world, int x, int y, int z, int face) {
        if (face == 1) {
            return this.blockIndexInTexture - 1;
        }
        if (face == 0) {
            return this.blockIndexInTexture - 1;
        }
        int n = world.getBlockId(x, y, z - 1);
        int n2 = world.getBlockId(x, y, z + 1);
        int n3 = world.getBlockId(x - 1, y, z);
        int n4 = world.getBlockId(x + 1, y, z);
        if (n == this.blockID || n2 == this.blockID) {
            if (face == 2 || face == 3) {
                return this.blockIndexInTexture;
            }
            int n5 = 0;
            if (n == this.blockID) {
                n5 = -1;
            }
            int n6 = world.getBlockId(x - 1, y, n == this.blockID ? z - 1 : z + 1);
            int n7 = world.getBlockId(x + 1, y, n == this.blockID ? z - 1 : z + 1);
            if (face == 4) {
                n5 = -1 - n5;
            }
            int n8 = 5;
            if ((Block.opaqueCubeLookup[n3] || Block.opaqueCubeLookup[n6]) && !Block.opaqueCubeLookup[n4] && !Block.opaqueCubeLookup[n7]) {
                n8 = 5;
            }
            if ((Block.opaqueCubeLookup[n4] || Block.opaqueCubeLookup[n7]) && !Block.opaqueCubeLookup[n3] && !Block.opaqueCubeLookup[n6]) {
                n8 = 4;
            }
            return (face == n8 ? this.blockIndexInTexture + 16 : this.blockIndexInTexture + 32) + n5;
        }
        if (n3 == this.blockID || n4 == this.blockID) {
            if (face == 4 || face == 5) {
                return this.blockIndexInTexture;
            }
            int n9 = 0;
            if (n3 == this.blockID) {
                n9 = -1;
            }
            int n10 = world.getBlockId(n3 == this.blockID ? x - 1 : x + 1, y, z - 1);
            int n11 = world.getBlockId(n3 == this.blockID ? x - 1 : x + 1, y, z + 1);
            if (face == 3) {
                n9 = -1 - n9;
            }
            int n12 = 3;
            if ((Block.opaqueCubeLookup[n] || Block.opaqueCubeLookup[n10]) && !Block.opaqueCubeLookup[n2] && !Block.opaqueCubeLookup[n11]) {
                n12 = 3;
            }
            if ((Block.opaqueCubeLookup[n2] || Block.opaqueCubeLookup[n11]) && !Block.opaqueCubeLookup[n] && !Block.opaqueCubeLookup[n10]) {
                n12 = 2;
            }
            return (face == n12 ? this.blockIndexInTexture + 16 : this.blockIndexInTexture + 32) + n9;
        }
        int n13 = 3;
        if (Block.opaqueCubeLookup[n] && !Block.opaqueCubeLookup[n2]) {
            n13 = 3;
        }
        if (Block.opaqueCubeLookup[n2] && !Block.opaqueCubeLookup[n]) {
            n13 = 2;
        }
        if (Block.opaqueCubeLookup[n3] && !Block.opaqueCubeLookup[n4]) {
            n13 = 5;
        }
        if (Block.opaqueCubeLookup[n4] && !Block.opaqueCubeLookup[n3]) {
            n13 = 4;
        }
        return face == n13 ? this.blockIndexInTexture + 1 : this.blockIndexInTexture;
    }

    public int getBlockTextureFromSide(int face) {
        if (face == 1) {
            return this.blockIndexInTexture - 1;
        }
        if (face == 0) {
            return this.blockIndexInTexture - 1;
        }
        if (face == 3) {
            return this.blockIndexInTexture + 1;
        }
        return this.blockIndexInTexture;
    }

    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        int n = 0;
        if (world.getBlockId(x - 1, y, z) == this.blockID) {
            ++n;
        }
        if (world.getBlockId(x + 1, y, z) == this.blockID) {
            ++n;
        }
        if (world.getBlockId(x, y, z - 1) == this.blockID) {
            ++n;
        }
        if (world.getBlockId(x, y, z + 1) == this.blockID) {
            ++n;
        }
        if (n > 1) {
            return false;
        }
        if (this.isThereANeighborChest(world, x - 1, y, z)) {
            return false;
        }
        if (this.isThereANeighborChest(world, x + 1, y, z)) {
            return false;
        }
        if (this.isThereANeighborChest(world, x, y, z - 1)) {
            return false;
        }
        return !this.isThereANeighborChest(world, x, y, z + 1);
    }

    private boolean isThereANeighborChest(World world, int x, int y, int z) {
        if (world.getBlockId(x, y, z) != this.blockID) {
            return false;
        }
        if (world.getBlockId(x - 1, y, z) == this.blockID) {
            return true;
        }
        if (world.getBlockId(x + 1, y, z) == this.blockID) {
            return true;
        }
        if (world.getBlockId(x, y, z - 1) == this.blockID) {
            return true;
        }
        return world.getBlockId(x, y, z + 1) == this.blockID;
    }

    public void onBlockRemoval(World world, int x, int y, int z) {
        TileEntityChest tileEntityChest = (TileEntityChest)world.getBlockTileEntity(x, y, z);
        for (int i = 0; i < tileEntityChest.getSizeInventory(); ++i) {
            ItemStack itemStack = tileEntityChest.getStackInSlot(i);
            if (itemStack == null) continue;
            float f = this.random.nextFloat() * 0.8f + 0.1f;
            float f2 = this.random.nextFloat() * 0.8f + 0.1f;
            float f3 = this.random.nextFloat() * 0.8f + 0.1f;
            while (itemStack.stackSize > 0) {
                int n = this.random.nextInt(21) + 10;
                if (n > itemStack.stackSize) {
                    n = itemStack.stackSize;
                }
                itemStack.stackSize -= n;
                EntityItem entityItem = new EntityItem(world, (float)x + f, (float)y + f2, (float)z + f3, new ItemStack(itemStack.itemID, n, itemStack.getItemDamage()));
                float f4 = 0.05f;
                entityItem.motionX = (float)this.random.nextGaussian() * f4;
                entityItem.motionY = (float)this.random.nextGaussian() * f4 + 0.2f;
                entityItem.motionZ = (float)this.random.nextGaussian() * f4;
                world.entityJoinedWorld(entityItem);
            }
        }
        super.onBlockRemoval(world, x, y, z);
    }

    public boolean blockActivated(World world, int x, int y, int z, EntityPlayer player) {
        IInventory iInventory = (TileEntityChest)world.getBlockTileEntity(x, y, z);
        if (world.isBlockNormalCube(x, y + 1, z)) {
            return true;
        }
        if (world.getBlockId(x - 1, y, z) == this.blockID && world.isBlockNormalCube(x - 1, y + 1, z)) {
            return true;
        }
        if (world.getBlockId(x + 1, y, z) == this.blockID && world.isBlockNormalCube(x + 1, y + 1, z)) {
            return true;
        }
        if (world.getBlockId(x, y, z - 1) == this.blockID && world.isBlockNormalCube(x, y + 1, z - 1)) {
            return true;
        }
        if (world.getBlockId(x, y, z + 1) == this.blockID && world.isBlockNormalCube(x, y + 1, z + 1)) {
            return true;
        }
        if (world.getBlockId(x - 1, y, z) == this.blockID) {
            iInventory = new InventoryLargeChest("Large chest", (TileEntityChest)world.getBlockTileEntity(x - 1, y, z), iInventory);
        }
        if (world.getBlockId(x + 1, y, z) == this.blockID) {
            iInventory = new InventoryLargeChest("Large chest", iInventory, (TileEntityChest)world.getBlockTileEntity(x + 1, y, z));
        }
        if (world.getBlockId(x, y, z - 1) == this.blockID) {
            iInventory = new InventoryLargeChest("Large chest", (TileEntityChest)world.getBlockTileEntity(x, y, z - 1), iInventory);
        }
        if (world.getBlockId(x, y, z + 1) == this.blockID) {
            iInventory = new InventoryLargeChest("Large chest", iInventory, (TileEntityChest)world.getBlockTileEntity(x, y, z + 1));
        }
        if (world.multiplayerWorld) {
            return true;
        }
        player.displayGUIChest(iInventory);
        return true;
    }

    protected TileEntity getBlockEntity() {
        return new TileEntityChest();
    }
}

