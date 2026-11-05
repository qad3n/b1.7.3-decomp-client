package net.minecraft.src;

import net.minecraft.client.Minecraft;

public class PlayerControllerMP
extends PlayerController {
    private int currentBlockX = -1;
    private int currentBlockY = -1;
    private int currentblockZ = -1;
    private float curBlockDamageMP = 0.0f;
    private float prevBlockDamageMP = 0.0f;
    private float miningTicks = 0.0f;
    private int blockHitDelay = 0;
    private boolean isHittingBlock = false;
    private NetClientHandler netClientHandler;
    private int currentPlayerItem = 0;

    public PlayerControllerMP(Minecraft minecraft, NetClientHandler networkHandler) {
        super(minecraft);
        this.netClientHandler = networkHandler;
    }

    public void flipPlayer(EntityPlayer player) {
        player.rotationYaw = -180.0f;
    }

    public boolean sendBlockRemoved(int x, int y, int z, int face) {
        int n = this.mc.theWorld.getBlockId(x, y, z);
        boolean bl = super.sendBlockRemoved(x, y, z, face);
        ItemStack itemStack = this.mc.thePlayer.getCurrentEquippedItem();
        if (itemStack != null) {
            itemStack.onDestroyBlock(n, x, y, z, this.mc.thePlayer);
            if (itemStack.stackSize == 0) {
                itemStack.onRemoved(this.mc.thePlayer);
                this.mc.thePlayer.destroyCurrentEquippedItem();
            }
        }
        return bl;
    }

    public void clickBlock(int x, int y, int z, int face) {
        if (!this.isHittingBlock || x != this.currentBlockX || y != this.currentBlockY || z != this.currentblockZ) {
            this.netClientHandler.addToSendQueue(new Packet14BlockDig(0, x, y, z, face));
            int n = this.mc.theWorld.getBlockId(x, y, z);
            if (n > 0 && this.curBlockDamageMP == 0.0f) {
                Block.blocksList[n].onBlockClicked(this.mc.theWorld, x, y, z, this.mc.thePlayer);
            }
            if (n > 0 && Block.blocksList[n].blockStrength(this.mc.thePlayer) >= 1.0f) {
                this.sendBlockRemoved(x, y, z, face);
            } else {
                this.isHittingBlock = true;
                this.currentBlockX = x;
                this.currentBlockY = y;
                this.currentblockZ = z;
                this.curBlockDamageMP = 0.0f;
                this.prevBlockDamageMP = 0.0f;
                this.miningTicks = 0.0f;
            }
        }
    }

    public void resetBlockRemoving() {
        this.curBlockDamageMP = 0.0f;
        this.isHittingBlock = false;
    }

    public void sendBlockRemoving(int x, int y, int z, int face) {
        if (!this.isHittingBlock) {
            return;
        }
        this.syncCurrentPlayItem();
        if (this.blockHitDelay > 0) {
            --this.blockHitDelay;
            return;
        }
        if (x == this.currentBlockX && y == this.currentBlockY && z == this.currentblockZ) {
            int n = this.mc.theWorld.getBlockId(x, y, z);
            if (n == 0) {
                this.isHittingBlock = false;
                return;
            }
            Block block = Block.blocksList[n];
            this.curBlockDamageMP += block.blockStrength(this.mc.thePlayer);
            if (this.miningTicks % 4.0f == 0.0f && block != null) {
                this.mc.sndManager.playSound(block.stepSound.getStepping(), (float)x + 0.5f, (float)y + 0.5f, (float)z + 0.5f, (block.stepSound.getVolume() + 1.0f) / 8.0f, block.stepSound.getPitch() * 0.5f);
            }
            this.miningTicks += 1.0f;
            if (this.curBlockDamageMP >= 1.0f) {
                this.isHittingBlock = false;
                this.netClientHandler.addToSendQueue(new Packet14BlockDig(2, x, y, z, face));
                this.sendBlockRemoved(x, y, z, face);
                this.curBlockDamageMP = 0.0f;
                this.prevBlockDamageMP = 0.0f;
                this.miningTicks = 0.0f;
                this.blockHitDelay = 5;
            }
        } else {
            this.clickBlock(x, y, z, face);
        }
    }

    @Override
public void setPartialTime(float tickDelta) {
    if (this.curBlockDamageMP <= 0.0F) {
        this.mc.ingameGUI.damageGuiPartialTime = 0.0F;
        this.mc.renderGlobal.damagePartialTime = 0.0F;
    } else {
        float f = this.prevBlockDamageMP + (this.curBlockDamageMP - this.prevBlockDamageMP) * tickDelta;
        this.mc.ingameGUI.damageGuiPartialTime = f;
        this.mc.renderGlobal.damagePartialTime = f;
    }
}

    public float getBlockReachDistance() {
        return 4.0f;
    }

    public void initWorld(World world) {
        super.initWorld(world);
    }

    public void updateController() {
        this.syncCurrentPlayItem();
        this.prevBlockDamageMP = this.curBlockDamageMP;
        this.mc.sndManager.playRandomMusicIfReady();
    }

    private void syncCurrentPlayItem() {
        int n = this.mc.thePlayer.inventory.currentItem;
        if (n != this.currentPlayerItem) {
            this.currentPlayerItem = n;
            this.netClientHandler.addToSendQueue(new Packet16BlockItemSwitch(this.currentPlayerItem));
        }
    }

    public boolean sendPlaceBlock(EntityPlayer player, World world, ItemStack itemInHand, int x, int y, int z, int face) {
        this.syncCurrentPlayItem();
        this.netClientHandler.addToSendQueue(new Packet15Place(x, y, z, face, player.inventory.getCurrentItem()));
        boolean bl = super.sendPlaceBlock(player, world, itemInHand, x, y, z, face);
        return bl;
    }

    public boolean sendUseItem(EntityPlayer player, World world, ItemStack itemInHand) {
        this.syncCurrentPlayItem();
        this.netClientHandler.addToSendQueue(new Packet15Place(-1, -1, -1, 255, player.inventory.getCurrentItem()));
        boolean bl = super.sendUseItem(player, world, itemInHand);
        return bl;
    }

    public EntityPlayer createPlayer(World world) {
        return new EntityClientPlayerMP(this.mc, world, this.mc.session, this.netClientHandler);
    }

    public void attackEntity(EntityPlayer player, Entity target) {
        this.syncCurrentPlayItem();
        this.netClientHandler.addToSendQueue(new Packet7UseEntity(player.entityId, target.entityId, 1));
        player.attackTargetEntityWithCurrentItem(target);
    }

    public void interactWithEntity(EntityPlayer player, Entity target) {
        this.syncCurrentPlayItem();
        this.netClientHandler.addToSendQueue(new Packet7UseEntity(player.entityId, target.entityId, 0));
        player.useCurrentItemOnEntity(target);
    }

    public ItemStack clickSlot(int menuId, int slot, int button, boolean quickMove, EntityPlayer player) {
        short s = player.craftingInventory.nextInteractionId(player.inventory);
        ItemStack itemStack = super.clickSlot(menuId, slot, button, quickMove, player);
        this.netClientHandler.addToSendQueue(new Packet102WindowClick(menuId, slot, button, quickMove, itemStack, s));
        return itemStack;
    }

    public void closeMenu(int menuId, EntityPlayer player) {
        if (menuId == -9999) {
            return;
        }
    }
}

