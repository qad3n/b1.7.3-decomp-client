// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.src;

import net.minecraft.client.Minecraft;

public class PlayerControllerSP extends PlayerController
{
    private int targetBlockX;
    private int targetBlockY;
    private int targetBlockZ;
    private float curBlockDamage;
    private float prevBlockDamage;
    private float miningTicks;
    private int blockHitWait;
    
    public PlayerControllerSP(final Minecraft minecraft) {
        super(minecraft);
        this.targetBlockX = -1;
        this.targetBlockY = -1;
        this.targetBlockZ = -1;
        this.curBlockDamage = 0.0f;
        this.prevBlockDamage = 0.0f;
        this.miningTicks = 0.0f;
        this.blockHitWait = 0;
    }
    
    @Override
    public void flipPlayer(final EntityPlayer player) {
        player.rotationYaw = -180.0f;
    }
    
    @Override
    public boolean sendBlockRemoved(final int x, final int y, final int z, final int face) {
        final int blockId = this.mc.theWorld.getBlockId(x, y, z);
        final int blockMetadata = this.mc.theWorld.getBlockMetadata(x, y, z);
        final boolean sendBlockRemoved = super.sendBlockRemoved(x, y, z, face);
        final ItemStack currentEquippedItem = this.mc.thePlayer.getCurrentEquippedItem();
        final boolean canHarvestBlock = this.mc.thePlayer.canHarvestBlock(Block.blocksList[blockId]);
        if (currentEquippedItem != null) {
            currentEquippedItem.onDestroyBlock(blockId, x, y, z, this.mc.thePlayer);
            if (currentEquippedItem.stackSize == 0) {
                currentEquippedItem.onRemoved(this.mc.thePlayer);
                this.mc.thePlayer.destroyCurrentEquippedItem();
            }
        }
        if (sendBlockRemoved && canHarvestBlock) {
            Block.blocksList[blockId].harvestBlock(this.mc.theWorld, this.mc.thePlayer, x, y, z, blockMetadata);
        }
        return sendBlockRemoved;
    }
    
    @Override
    public void clickBlock(final int x, final int y, final int z, final int face) {
        this.mc.theWorld.onBlockHit(this.mc.thePlayer, x, y, z, face);
        final int blockId = this.mc.theWorld.getBlockId(x, y, z);
        if (blockId > 0 && this.curBlockDamage == 0.0f) {
            Block.blocksList[blockId].onBlockClicked(this.mc.theWorld, x, y, z, this.mc.thePlayer);
        }
        if (blockId > 0 && Block.blocksList[blockId].blockStrength(this.mc.thePlayer) >= 1.0f) {
            this.sendBlockRemoved(x, y, z, face);
        }
    }
    
    @Override
    public void resetBlockRemoving() {
        this.curBlockDamage = 0.0f;
        this.blockHitWait = 0;
    }
    
    @Override
    public void sendBlockRemoving(final int x, final int y, final int z, final int face) {
        if (this.blockHitWait > 0) {
            --this.blockHitWait;
            return;
        }
        if (x == this.targetBlockX && y == this.targetBlockY && z == this.targetBlockZ) {
            final int blockId = this.mc.theWorld.getBlockId(x, y, z);
            if (blockId == 0) {
                return;
            }
            final Block block = Block.blocksList[blockId];
            this.curBlockDamage += block.blockStrength(this.mc.thePlayer);
            if (this.miningTicks % 4.0f == 0.0f && block != null) {
                this.mc.sndManager.playSound(block.stepSound.getStepping(), x + 0.5f, y + 0.5f, z + 0.5f, (block.stepSound.getVolume() + 1.0f) / 8.0f, block.stepSound.getPitch() * 0.5f);
            }
            ++this.miningTicks;
            if (this.curBlockDamage >= 1.0f) {
                this.sendBlockRemoved(x, y, z, face);
                this.curBlockDamage = 0.0f;
                this.prevBlockDamage = 0.0f;
                this.miningTicks = 0.0f;
                this.blockHitWait = 5;
            }
        }
        else {
            this.curBlockDamage = 0.0f;
            this.prevBlockDamage = 0.0f;
            this.miningTicks = 0.0f;
            this.targetBlockX = x;
            this.targetBlockY = y;
            this.targetBlockZ = z;
        }
    }
    
    @Override
    public void setPartialTime(final float tickDelta) {
        if (this.curBlockDamage <= 0.0f) {
            this.mc.ingameGUI.damageGuiPartialTime = 0.0f;
            this.mc.renderGlobal.damagePartialTime = 0.0f;
        }
        else {
            final float n = this.prevBlockDamage + (this.curBlockDamage - this.prevBlockDamage) * tickDelta;
            this.mc.ingameGUI.damageGuiPartialTime = n;
            this.mc.renderGlobal.damagePartialTime = n;
        }
    }
    
    @Override
    public float getBlockReachDistance() {
        return 4.0f;
    }
    
    @Override
    public void initWorld(final World world) {
        super.initWorld(world);
    }
    
    @Override
    public void updateController() {
        this.prevBlockDamage = this.curBlockDamage;
        this.mc.sndManager.playRandomMusicIfReady();
    }
}
