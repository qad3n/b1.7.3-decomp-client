package net.minecraft.src;

import net.minecraft.client.Minecraft;

public class PlayerController {
    protected final Minecraft mc;
    public boolean creative = false;

    public PlayerController(Minecraft minecraft) {
        this.mc = minecraft;
    }

    public void initWorld(World world) {
    }

    public void clickBlock(int x, int y, int z, int face) {
        this.mc.theWorld.onBlockHit(this.mc.thePlayer, x, y, z, face);
        this.sendBlockRemoved(x, y, z, face);
    }

    public boolean sendBlockRemoved(int x, int y, int z, int face) {
        World world = this.mc.theWorld;
        Block block = Block.blocksList[world.getBlockId(x, y, z)];
        world.doEvent(2001, x, y, z, block.blockID + world.getBlockMetadata(x, y, z) * 256);
        int n = world.getBlockMetadata(x, y, z);
        boolean bl = world.setBlockWithNotify(x, y, z, 0);
        if (block != null && bl) {
            block.onBlockDestroyedByPlayer(world, x, y, z, n);
        }
        return bl;
    }

    public void sendBlockRemoving(int x, int y, int z, int face) {
    }

    public void resetBlockRemoving() {
    }

    public void setPartialTime(float tickDelta) {
    }

    public float getBlockReachDistance() {
        return 5.0f;
    }

    public boolean sendUseItem(EntityPlayer player, World world, ItemStack itemInHand) {
        int n = itemInHand.stackSize;
        ItemStack itemStack = itemInHand.useItemRightClick(world, player);
        if (itemStack != itemInHand || itemStack != null && itemStack.stackSize != n) {
            player.inventory.mainInventory[player.inventory.currentItem] = itemStack;
            if (itemStack.stackSize == 0) {
                player.inventory.mainInventory[player.inventory.currentItem] = null;
            }
            return true;
        }
        return false;
    }

    public void flipPlayer(EntityPlayer player) {
    }

    public void updateController() {
    }

    public boolean shouldDrawHUD() {
        return true;
    }

    public void adjustPlayer(EntityPlayer player) {
    }

    public boolean sendPlaceBlock(EntityPlayer player, World world, ItemStack itemInHand, int x, int y, int z, int face) {
        int n = world.getBlockId(x, y, z);
        if (n > 0 && Block.blocksList[n].blockActivated(world, x, y, z, player)) {
            return true;
        }
        if (itemInHand == null) {
            return false;
        }
        return itemInHand.useItem(player, world, x, y, z, face);
    }

    public EntityPlayer createPlayer(World world) {
        return new EntityPlayerSP(this.mc, world, this.mc.session, world.worldProvider.worldType);
    }

    public void interactWithEntity(EntityPlayer player, Entity target) {
        player.useCurrentItemOnEntity(target);
    }

    public void attackEntity(EntityPlayer player, Entity target) {
        player.attackTargetEntityWithCurrentItem(target);
    }

    public ItemStack clickSlot(int menuId, int slot, int button, boolean quickMove, EntityPlayer player) {
        return player.craftingInventory.onClickSlot(slot, button, quickMove, player);
    }

    public void closeMenu(int menuId, EntityPlayer player) {
        player.craftingInventory.onCraftGuiClosed(player);
        player.craftingInventory = player.inventorySlots;
    }
}

