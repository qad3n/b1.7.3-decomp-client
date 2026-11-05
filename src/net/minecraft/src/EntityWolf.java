package net.minecraft.src;

import java.util.List;

public class EntityWolf
extends EntityAnimal {
    private boolean looksWithInterest = false;
    private float headRollAngle;
    private float lastHeadRollAngle;
    private boolean isWolfShaking;
    private boolean shaking;
    private float timeWolfIsShaking;
    private float prevTimeWolfIsShaking;

    public EntityWolf(World world) {
        super(world);
        this.texture = "/mob/wolf.png";
        this.setSize(0.8f, 0.8f);
        this.moveSpeed = 1.1f;
        this.health = 8;
    }

    protected void entityInit() {
        super.entityInit();
        this.dataWatcher.addObject(16, (byte)0);
        this.dataWatcher.addObject(17, "");
        this.dataWatcher.addObject(18, new Integer(this.health));
    }

    protected boolean canTriggerWalking() {
        return false;
    }

    public String getEntityTexture() {
        if (this.isWolfTamed()) {
            return "/mob/wolf_tame.png";
        }
        if (this.isWolfAngry()) {
            return "/mob/wolf_angry.png";
        }
        return super.getEntityTexture();
    }

    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        nbt.setBoolean("Angry", this.isWolfAngry());
        nbt.setBoolean("Sitting", this.isWolfSitting());
        if (this.getWolfOwner() == null) {
            nbt.setString("Owner", "");
        } else {
            nbt.setString("Owner", this.getWolfOwner());
        }
    }

    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);
        this.setWolfAngry(nbt.getBoolean("Angry"));
        this.setWolfSitting(nbt.getBoolean("Sitting"));
        String string = nbt.getString("Owner");
        if (string.length() > 0) {
            this.setWolfOwner(string);
            this.setWolfTamed(true);
        }
    }

    protected boolean canDespawn() {
        return !this.isWolfTamed();
    }

    protected String getLivingSound() {
        if (this.isWolfAngry()) {
            return "mob.wolf.growl";
        }
        if (this.rand.nextInt(3) == 0) {
            if (this.isWolfTamed() && this.dataWatcher.getWatchableObjectInt(18) < 10) {
                return "mob.wolf.whine";
            }
            return "mob.wolf.panting";
        }
        return "mob.wolf.bark";
    }

    protected String getHurtSound() {
        return "mob.wolf.hurt";
    }

    protected String getDeathSound() {
        return "mob.wolf.death";
    }

    protected float getSoundVolume() {
        return 0.4f;
    }

    protected int getDropItemId() {
        return -1;
    }

    @Override
protected void updatePlayerActionState() {
    super.updatePlayerActionState();
    if (!this.hasAttacked && !this.hasPath() && this.isWolfTamed() && this.ridingEntity == null) {
        EntityPlayer entityplayer = this.worldObj.getPlayerEntityByName(this.getWolfOwner());
        if (entityplayer != null) {
            float f = entityplayer.getDistanceToEntity(this);
            if (f > 5.0F) {
                this.getPathOrWalkableBlock(entityplayer, f);
            }
        } else if (!this.isInWater()) {
            this.setWolfSitting(true);
        }
    } else if (this.playerToAttack == null && !this.hasPath() && !this.isWolfTamed() && this.worldObj.rand.nextInt(100) == 0) {
        List list = this.worldObj.getEntitiesWithinAABB(EntitySheep.class, AxisAlignedBB.getBoundingBoxFromPool(this.posX, this.posY, this.posZ, this.posX + 1.0, this.posY + 1.0, this.posZ + 1.0).expand(16.0, 4.0, 16.0));
        if (!list.isEmpty()) {
            this.setTarget((Entity) list.get(this.worldObj.rand.nextInt(list.size())));
        }
    }
    if (this.isInWater()) {
        this.setWolfSitting(false);
    }
    if (!this.worldObj.multiplayerWorld) {
        this.dataWatcher.updateObject(18, this.health);
    }
}

    @Override
public void onLivingUpdate() {
    super.onLivingUpdate();
    this.looksWithInterest = false;
    if (this.hasCurrentTarget() && !this.hasPath() && !this.isWolfAngry()) {
        Entity entity = this.getCurrentTarget();
        if (entity instanceof EntityPlayer) {
            EntityPlayer entityplayer = (EntityPlayer) entity;
            ItemStack itemstack = entityplayer.inventory.getCurrentItem();
            if (itemstack != null) {
                if (!this.isWolfTamed() && itemstack.itemID == Item.bone.shiftedIndex) {
                    this.looksWithInterest = true;
                } else if (this.isWolfTamed() && Item.itemsList[itemstack.itemID] instanceof ItemFood) {
                    this.looksWithInterest = ((ItemFood) Item.itemsList[itemstack.itemID]).getIsWolfsFavoriteMeat();
                }
            }
        }
    }
    if (!this.isMultiplayerEntity && this.isWolfShaking && !this.shaking && !this.hasPath() && this.onGround) {
        this.shaking = true;
        this.timeWolfIsShaking = 0.0F;
        this.prevTimeWolfIsShaking = 0.0F;
        this.worldObj.doEntityEvent(this, (byte) 8);
    }
}

    @Override
public void onUpdate() {
    super.onUpdate();
    this.lastHeadRollAngle = this.headRollAngle;
    if (this.looksWithInterest) {
        this.headRollAngle = this.headRollAngle + (1.0F - this.headRollAngle) * 0.4F;
    } else {
        this.headRollAngle = this.headRollAngle + (0.0F - this.headRollAngle) * 0.4F;
    }
    if (this.looksWithInterest) {
        this.numTicksToChaseTarget = 10;
    }
    if (this.isWet()) {
        this.isWolfShaking = true;
        this.shaking = false;
        this.timeWolfIsShaking = 0.0F;
        this.prevTimeWolfIsShaking = 0.0F;
    } else if ((this.isWolfShaking || this.shaking) && this.shaking) {
        if (this.timeWolfIsShaking == 0.0F) {
            this.worldObj.playSoundAtEntity(this, "mob.wolf.shake", this.getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
        }
        this.prevTimeWolfIsShaking = this.timeWolfIsShaking;
        this.timeWolfIsShaking += 0.05F;
        if (this.prevTimeWolfIsShaking >= 2.0F) {
            this.isWolfShaking = false;
            this.shaking = false;
            this.prevTimeWolfIsShaking = 0.0F;
            this.timeWolfIsShaking = 0.0F;
        }
        if (this.timeWolfIsShaking > 0.4F) {
            float f = (float) this.boundingBox.minY;
            int i = (int) (MathHelper.sin((this.timeWolfIsShaking - 0.4F) * (float) Math.PI) * 7.0F);
            for (int j = 0; j < i; j++) {
                float f1 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width * 0.5F;
                float f2 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width * 0.5F;
                this.worldObj.spawnParticle("splash", this.posX + f1, f + 0.8F, this.posZ + f2, this.motionX, this.motionY, this.motionZ);
            }
        }
    }
}

    public boolean getWolfShaking() {
        return this.isWolfShaking;
    }

    public float getShadingWhileShaking(float tickDelta) {
        return 0.75f + (this.prevTimeWolfIsShaking + (this.timeWolfIsShaking - this.prevTimeWolfIsShaking) * tickDelta) / 2.0f * 0.25f;
    }

    public float getShakeAngle(float tickdelta, float offset) {
        float f = (this.prevTimeWolfIsShaking + (this.timeWolfIsShaking - this.prevTimeWolfIsShaking) * tickdelta + offset) / 1.8f;
        if (f < 0.0f) {
            f = 0.0f;
        } else if (f > 1.0f) {
            f = 1.0f;
        }
        return MathHelper.sin(f * (float)Math.PI) * MathHelper.sin(f * (float)Math.PI * 11.0f) * 0.15f * (float)Math.PI;
    }

    public float getInterestedAngle(float tickDelta) {
        return (this.lastHeadRollAngle + (this.headRollAngle - this.lastHeadRollAngle) * tickDelta) * 0.15f * (float)Math.PI;
    }

    public float getEyeHeight() {
        return this.height * 0.8f;
    }

    protected int verticleLookSpeed() {
        if (this.isWolfSitting()) {
            return 20;
        }
        return super.verticleLookSpeed();
    }

    private void getPathOrWalkableBlock(Entity owner, float distance) {
        PathEntity pathEntity = this.worldObj.getPathToEntity(this, owner, 16.0f);
        if (pathEntity == null && distance > 12.0f) {
            int n = MathHelper.floor_double(owner.posX) - 2;
            int n2 = MathHelper.floor_double(owner.posZ) - 2;
            int n3 = MathHelper.floor_double(owner.boundingBox.minY);
            for (int i = 0; i <= 4; ++i) {
                for (int j = 0; j <= 4; ++j) {
                    if (i >= 1 && j >= 1 && i <= 3 && j <= 3 || !this.worldObj.isBlockNormalCube(n + i, n3 - 1, n2 + j) || this.worldObj.isBlockNormalCube(n + i, n3, n2 + j) || this.worldObj.isBlockNormalCube(n + i, n3 + 1, n2 + j)) continue;
                    this.setLocationAndAngles((float)(n + i) + 0.5f, n3, (float)(n2 + j) + 0.5f, this.rotationYaw, this.rotationPitch);
                    return;
                }
            }
        } else {
            this.setPathToEntity(pathEntity);
        }
    }

    protected boolean isMovementCeased() {
        return this.isWolfSitting() || this.shaking;
    }

    public boolean attackEntityFrom(Entity source, int amount) {
        this.setWolfSitting(false);
        if (source != null && !(source instanceof EntityPlayer) && !(source instanceof EntityArrow)) {
            amount = (amount + 1) / 2;
        }
        if (super.attackEntityFrom(source, amount)) {
            if (!this.isWolfTamed() && !this.isWolfAngry()) {
                if (source instanceof EntityPlayer) {
                    this.setWolfAngry(true);
                    this.playerToAttack = source;
                }
                if (source instanceof EntityArrow && ((EntityArrow)source).owner != null) {
                    source = ((EntityArrow)source).owner;
                }
                if (source instanceof EntityLiving) {
                    List<EntityWolf> list = this.worldObj.getEntitiesWithinAABB(EntityWolf.class, AxisAlignedBB.getBoundingBoxFromPool(this.posX, this.posY, this.posZ, this.posX + 1.0, this.posY + 1.0, this.posZ + 1.0).expand(16.0, 4.0, 16.0));
                    for (Entity entity : list) {
                        EntityWolf entityWolf = (EntityWolf)entity;
                        if (entityWolf.isWolfTamed() || entityWolf.playerToAttack != null) continue;
                        entityWolf.playerToAttack = source;
                        if (!(source instanceof EntityPlayer)) continue;
                        entityWolf.setWolfAngry(true);
                    }
                }
            } else if (source != this && source != null) {
                if (this.isWolfTamed() && source instanceof EntityPlayer && ((EntityPlayer)source).username.equalsIgnoreCase(this.getWolfOwner())) {
                    return true;
                }
                this.playerToAttack = source;
            }
            return true;
        }
        return false;
    }

    protected Entity findPlayerToAttack() {
        if (this.isWolfAngry()) {
            return this.worldObj.getClosestPlayerToEntity(this, 16.0);
        }
        return null;
    }

    protected void attackEntity(Entity target, float distance) {
        if (distance > 2.0f && distance < 6.0f && this.rand.nextInt(10) == 0) {
            if (this.onGround) {
                double d = target.posX - this.posX;
                double d2 = target.posZ - this.posZ;
                float f = MathHelper.sqrt_double(d * d + d2 * d2);
                this.motionX = d / (double)f * 0.5 * (double)0.8f + this.motionX * (double)0.2f;
                this.motionZ = d2 / (double)f * 0.5 * (double)0.8f + this.motionZ * (double)0.2f;
                this.motionY = 0.4f;
            }
        } else if ((double)distance < 1.5 && target.boundingBox.maxY > this.boundingBox.minY && target.boundingBox.minY < this.boundingBox.maxY) {
            this.attackTime = 20;
            int n = 2;
            if (this.isWolfTamed()) {
                n = 4;
            }
            target.attackEntityFrom(this, n);
        }
    }

    @Override
public boolean interact(EntityPlayer player) {
    ItemStack itemstack = player.inventory.getCurrentItem();
    if (!this.isWolfTamed()) {
        if (itemstack != null && itemstack.itemID == Item.bone.shiftedIndex && !this.isWolfAngry()) {
            itemstack.stackSize--;
            if (itemstack.stackSize <= 0) {
                player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
            }
            if (!this.worldObj.multiplayerWorld) {
                if (this.rand.nextInt(3) == 0) {
                    this.setWolfTamed(true);
                    this.setPathToEntity(null);
                    this.setWolfSitting(true);
                    this.health = 20;
                    this.setWolfOwner(player.username);
                    this.showHeartsOrSmokeFX(true);
                    this.worldObj.doEntityEvent(this, (byte) 7);
                } else {
                    this.showHeartsOrSmokeFX(false);
                    this.worldObj.doEntityEvent(this, (byte) 6);
                }
            }
            return true;
        }
    } else {
        if (itemstack != null && Item.itemsList[itemstack.itemID] instanceof ItemFood) {
            ItemFood itemfood = (ItemFood) Item.itemsList[itemstack.itemID];
            if (itemfood.getIsWolfsFavoriteMeat() && this.dataWatcher.getWatchableObjectInt(18) < 20) {
                itemstack.stackSize--;
                if (itemstack.stackSize <= 0) {
                    player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
                }
                this.heal(((ItemFood) Item.porkRaw).getHealAmount());
                return true;
            }
        }
        if (player.username.equalsIgnoreCase(this.getWolfOwner())) {
            if (!this.worldObj.multiplayerWorld) {
                this.setWolfSitting(!this.isWolfSitting());
                this.isJumping = false;
                this.setPathToEntity(null);
            }
            return true;
        }
    }
    return false;
}

    void showHeartsOrSmokeFX(boolean breeding) {
        String string = "heart";
        if (!breeding) {
            string = "smoke";
        }
        for (int i = 0; i < 7; ++i) {
            double d = this.rand.nextGaussian() * 0.02;
            double d2 = this.rand.nextGaussian() * 0.02;
            double d3 = this.rand.nextGaussian() * 0.02;
            this.worldObj.spawnParticle(string, this.posX + (double)(this.rand.nextFloat() * this.width * 2.0f) - (double)this.width, this.posY + 0.5 + (double)(this.rand.nextFloat() * this.height), this.posZ + (double)(this.rand.nextFloat() * this.width * 2.0f) - (double)this.width, d, d2, d3);
        }
    }

    public void handleHealthUpdate(byte event) {
        if (event == 7) {
            this.showHeartsOrSmokeFX(true);
        } else if (event == 6) {
            this.showHeartsOrSmokeFX(false);
        } else if (event == 8) {
            this.shaking = true;
            this.timeWolfIsShaking = 0.0f;
            this.prevTimeWolfIsShaking = 0.0f;
        } else {
            super.handleHealthUpdate(event);
        }
    }

    public float setTailRotation() {
        if (this.isWolfAngry()) {
            return 1.5393804f;
        }
        if (this.isWolfTamed()) {
            return (0.55f - (float)(20 - this.dataWatcher.getWatchableObjectInt(18)) * 0.02f) * (float)Math.PI;
        }
        return 0.62831855f;
    }

    public int getMaxSpawnedInChunk() {
        return 8;
    }

    public String getWolfOwner() {
        return this.dataWatcher.getWatchableObjectString(17);
    }

    public void setWolfOwner(String name) {
        this.dataWatcher.updateObject(17, name);
    }

    public boolean isWolfSitting() {
        return (this.dataWatcher.getWatchableObjectByte(16) & 1) != 0;
    }

    public void setWolfSitting(boolean sitting) {
        byte by = this.dataWatcher.getWatchableObjectByte(16);
        if (sitting) {
            this.dataWatcher.updateObject(16, (byte)(by | 1));
        } else {
            this.dataWatcher.updateObject(16, (byte)(by & 0xFFFFFFFE));
        }
    }

    public boolean isWolfAngry() {
        return (this.dataWatcher.getWatchableObjectByte(16) & 2) != 0;
    }

    public void setWolfAngry(boolean angry) {
        byte by = this.dataWatcher.getWatchableObjectByte(16);
        if (angry) {
            this.dataWatcher.updateObject(16, (byte)(by | 2));
        } else {
            this.dataWatcher.updateObject(16, (byte)(by & 0xFFFFFFFD));
        }
    }

    public boolean isWolfTamed() {
        return (this.dataWatcher.getWatchableObjectByte(16) & 4) != 0;
    }

    public void setWolfTamed(boolean tame) {
        byte by = this.dataWatcher.getWatchableObjectByte(16);
        if (tame) {
            this.dataWatcher.updateObject(16, (byte)(by | 4));
        } else {
            this.dataWatcher.updateObject(16, (byte)(by & 0xFFFFFFFB));
        }
    }
}

