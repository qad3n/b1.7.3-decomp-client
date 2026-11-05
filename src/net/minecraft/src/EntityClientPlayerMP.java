// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.src;

import net.minecraft.client.Minecraft;

public class EntityClientPlayerMP extends EntityPlayerSP
{
    public NetClientHandler sendQueue;
    private int ticksSinceSendInventory;
    private boolean healthInitialized;
    private double oldPosX;
    private double sentMinY;
    private double oldPosY;
    private double oldPosZ;
    private float oldRotationYaw;
    private float oldRotationPitch;
    private boolean sentOnGround;
    private boolean wasSneaking;
    private int ticksSinceSendOnGround;
    
    public EntityClientPlayerMP(final Minecraft minecraft, final World world, final Session session, final NetClientHandler networkHandler) {
        super(minecraft, world, session, 0);
        this.ticksSinceSendInventory = 0;
        this.healthInitialized = false;
        this.sentOnGround = false;
        this.wasSneaking = false;
        this.ticksSinceSendOnGround = 0;
        this.sendQueue = networkHandler;
    }
    
    @Override
    public boolean attackEntityFrom(final Entity source, final int amount) {
        return false;
    }
    
    @Override
    public void heal(final int amount) {
    }
    
    @Override
    public void onUpdate() {
        if (!this.worldObj.blockExists(MathHelper.floor_double(this.posX), 64, MathHelper.floor_double(this.posZ))) {
            return;
        }
        super.onUpdate();
        this.sendMovement();
    }
    
    public void sendMovement() {
        if (this.ticksSinceSendInventory++ == 20) {
            this.sendInventoryChanged();
            this.ticksSinceSendInventory = 0;
        }
        final boolean sneaking = this.isSneaking();
        if (sneaking != this.wasSneaking) {
            if (sneaking) {
                this.sendQueue.addToSendQueue(new Packet19EntityAction(this, 1));
            }
            else {
                this.sendQueue.addToSendQueue(new Packet19EntityAction(this, 2));
            }
            this.wasSneaking = sneaking;
        }
        final double n = this.posX - this.oldPosX;
        final double n2 = this.boundingBox.minY - this.sentMinY;
        final double n3 = this.posY - this.oldPosY;
        final double n4 = this.posZ - this.oldPosZ;
        final double n5 = this.rotationYaw - this.oldRotationYaw;
        final double n6 = this.rotationPitch - this.oldRotationPitch;
        boolean b = n2 != 0.0 || n3 != 0.0 || n != 0.0 || n4 != 0.0;
        final boolean b2 = n5 != 0.0 || n6 != 0.0;
        if (this.ridingEntity != null) {
            if (b2) {
                this.sendQueue.addToSendQueue(new Packet10Flying.Packet11PlayerPosition(this.motionX, -999.0, -999.0, this.motionZ, this.onGround));
            }
            else {
                this.sendQueue.addToSendQueue(new Packet10Flying.Packet13PlayerLookMove(this.motionX, -999.0, -999.0, this.motionZ, this.rotationYaw, this.rotationPitch, this.onGround));
            }
            b = false;
        }
        else if (b && b2) {
            this.sendQueue.addToSendQueue(new Packet10Flying.Packet13PlayerLookMove(this.posX, this.boundingBox.minY, this.posY, this.posZ, this.rotationYaw, this.rotationPitch, this.onGround));
            this.ticksSinceSendOnGround = 0;
        }
        else if (b) {
            this.sendQueue.addToSendQueue(new Packet10Flying.Packet11PlayerPosition(this.posX, this.boundingBox.minY, this.posY, this.posZ, this.onGround));
            this.ticksSinceSendOnGround = 0;
        }
        else if (b2) {
            this.sendQueue.addToSendQueue(new Packet10Flying.Packet12PlayerLook(this.rotationYaw, this.rotationPitch, this.onGround));
            this.ticksSinceSendOnGround = 0;
        }
        else {
            this.sendQueue.addToSendQueue(new Packet10Flying(this.onGround));
            if (this.sentOnGround != this.onGround || this.ticksSinceSendOnGround > 200) {
                this.ticksSinceSendOnGround = 0;
            }
            else {
                ++this.ticksSinceSendOnGround;
            }
        }
        this.sentOnGround = this.onGround;
        if (b) {
            this.oldPosX = this.posX;
            this.sentMinY = this.boundingBox.minY;
            this.oldPosY = this.posY;
            this.oldPosZ = this.posZ;
        }
        if (b2) {
            this.oldRotationYaw = this.rotationYaw;
            this.oldRotationPitch = this.rotationPitch;
        }
    }
    
    @Override
    public void dropCurrentItem() {
        this.sendQueue.addToSendQueue(new Packet14BlockDig(4, 0, 0, 0, 0));
    }
    
    private void sendInventoryChanged() {
    }
    
    @Override
    protected void joinEntityItemWithWorld(final EntityItem item) {
    }
    
    @Override
    public void sendChatMessage(final String content) {
        this.sendQueue.addToSendQueue(new Packet3Chat(content));
    }
    
    @Override
    public void swingItem() {
        super.swingItem();
        this.sendQueue.addToSendQueue(new Packet18Animation(this, 1));
    }
    
    @Override
    public void respawnPlayer() {
        this.sendInventoryChanged();
        this.sendQueue.addToSendQueue(new Packet9Respawn((byte)this.dimension));
    }
    
    @Override
    protected void damageEntity(final int amount) {
        this.health -= amount;
    }
    
    @Override
    public void closeScreen() {
        this.sendQueue.addToSendQueue(new Packet101CloseWindow(this.craftingInventory.windowId));
        this.inventory.setItemStack(null);
        super.closeScreen();
    }
    
    @Override
    public void setHealth(final int health) {
        if (this.healthInitialized) {
            super.setHealth(health);
        }
        else {
            this.health = health;
            this.healthInitialized = true;
        }
    }
    
    @Override
    public void addStat(final StatBase stat, final int amount) {
        if (stat == null) {
            return;
        }
        if (stat.local) {
            super.addStat(stat, amount);
        }
    }
    
    public void incrementRemoteStat(final StatBase stat, final int amount) {
        if (stat == null) {
            return;
        }
        if (!stat.local) {
            super.addStat(stat, amount);
        }
    }
}
