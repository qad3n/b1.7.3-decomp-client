package net.minecraft.src;

import java.util.List;

public class EntityEgg extends Entity {
    private int blockX = -1;
    private int blockY = -1;
    private int blockZ = -1;
    private int block = 0;
    private boolean inGround = false;
    public int shake = 0;
    private EntityLiving thrower;
    private int inBlockTicks;
    private int inAirTicks = 0;

    public EntityEgg(World var1) {
        super(var1);
        this.setSize(0.25F, 0.25F);
    }

    @Override
    protected void entityInit() {
    }

    @Override
    public boolean isInRangeToRenderDist(double squaredDistanceToCamera) {
        double d0 = this.boundingBox.getAverageEdgeLength() * 4.0;
        d0 *= 64.0;
        return squaredDistanceToCamera < d0 * d0;
    }

    public EntityEgg(World world, EntityLiving thrower) {
        super(world);
        this.thrower = thrower;
        this.setSize(0.25f, 0.25f);
        this.setLocationAndAngles(thrower.posX, thrower.posY + (double) thrower.getEyeHeight(), thrower.posZ, thrower.rotationYaw, thrower.rotationPitch);
        this.posX -= (double) (MathHelper.cos(this.rotationYaw / 180.0f * (float) Math.PI) * 0.16f);
        this.posY -= (double) 0.1f;
        this.posZ -= (double) (MathHelper.sin(this.rotationYaw / 180.0f * (float) Math.PI) * 0.16f);
        this.setPosition(this.posX, this.posY, this.posZ);
        this.yOffset = 0.0f;
        float f = 0.4f;
        this.motionX = -MathHelper.sin(this.rotationYaw / 180.0f * (float) Math.PI) * MathHelper.cos(this.rotationPitch / 180.0f * (float) Math.PI) * f;
        this.motionZ = MathHelper.cos(this.rotationYaw / 180.0f * (float) Math.PI) * MathHelper.cos(this.rotationPitch / 180.0f * (float) Math.PI) * f;
        this.motionY = -MathHelper.sin(this.rotationPitch / 180.0f * (float) Math.PI) * f;
        this.setEggHeading(this.motionX, this.motionY, this.motionZ, 1.5f, 1.0f);
    }

    public EntityEgg(World world, double x, double y, double z) {
        super(world);
        this.inBlockTicks = 0;
        this.setSize(0.25F, 0.25F);
        this.setPosition(x, y, z);
        this.yOffset = 0.0F;
    }

    public void setEggHeading(double velocityX, double velocityY, double velocityZ, float power, float variance) {
        float f = MathHelper.sqrt_double(velocityX * velocityX + velocityY * velocityY + velocityZ * velocityZ);
        velocityX /= f;
        velocityY /= f;
        velocityZ /= f;
        velocityX += this.rand.nextGaussian() * 0.0075F * variance;
        velocityY += this.rand.nextGaussian() * 0.0075F * variance;
        velocityZ += this.rand.nextGaussian() * 0.0075F * variance;
        velocityX *= power;
        velocityY *= power;
        velocityZ *= power;
        this.motionX = velocityX;
        this.motionY = velocityY;
        this.motionZ = velocityZ;
        float f1 = MathHelper.sqrt_double(velocityX * velocityX + velocityZ * velocityZ);
        this.prevRotationYaw = this.rotationYaw = (float)(Math.atan2(velocityX, velocityZ) * 180.0 / (float) Math.PI);
        this.prevRotationPitch = this.rotationPitch = (float)(Math.atan2(velocityY, f1) * 180.0 / (float) Math.PI);
        this.inBlockTicks = 0;
    }

    @Override
    public void setVelocity(double velocityX, double velocityY, double velocityZ) {
        this.motionX = velocityX;
        this.motionY = velocityY;
        this.motionZ = velocityZ;
        if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
            float f = MathHelper.sqrt_double(velocityX * velocityX + velocityZ * velocityZ);
            this.prevRotationYaw = this.rotationYaw = (float)(Math.atan2(velocityX, velocityZ) * 180.0 / (float) Math.PI);
            this.prevRotationPitch = this.rotationPitch = (float)(Math.atan2(velocityY, f) * 180.0 / (float) Math.PI);
        }
    }

    @Override
    public void onUpdate() {
        this.lastTickPosX = this.posX;
        this.lastTickPosY = this.posY;
        this.lastTickPosZ = this.posZ;
        super.onUpdate();
        if (this.shake > 0) {
            this.shake--;
        }

        if (this.inGround) {
            int i = this.worldObj.getBlockId(this.blockX, this.blockY, this.blockZ);
            if (i == this.block) {
                this.inBlockTicks++;
                if (this.inBlockTicks == 1200) {
                    this.setEntityDead();
                }

                return;
            }

            this.inGround = false;
            this.motionX = this.motionX * (this.rand.nextFloat() * 0.2F);
            this.motionY = this.motionY * (this.rand.nextFloat() * 0.2F);
            this.motionZ = this.motionZ * (this.rand.nextFloat() * 0.2F);
            this.inBlockTicks = 0;
            this.inAirTicks = 0;
        } else {
            this.inAirTicks++;
        }

        Vec3D vec3d1 = Vec3D.createVector(this.posX, this.posY, this.posZ);
        Vec3D vec3d = Vec3D.createVector(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
        MovingObjectPosition movingobjectposition = this.worldObj.rayTraceBlocks(vec3d1, vec3d);
        vec3d1 = Vec3D.createVector(this.posX, this.posY, this.posZ);
        vec3d = Vec3D.createVector(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
        if (movingobjectposition != null) {
            vec3d = Vec3D.createVector(movingobjectposition.hitVec.xCoord, movingobjectposition.hitVec.yCoord, movingobjectposition.hitVec.zCoord);
        }

        if (!this.worldObj.multiplayerWorld) {
            Entity entity = null;
            List list = this.worldObj
                .getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0, 1.0, 1.0));
            double d0 = 0.0;

            for (int k = 0; k < list.size(); k++) {
                Entity entity1 = (Entity)list.get(k);
                if (entity1.canBeCollidedWith() && (entity1 != this.thrower || this.inAirTicks >= 5)) {
                    float f = 0.3F;
                    AxisAlignedBB axisalignedbb = entity1.boundingBox.expand(f, f, f);
                    MovingObjectPosition movingobjectposition1 = axisalignedbb.clip(vec3d1, vec3d);
                    if (movingobjectposition1 != null) {
                        double d1 = vec3d1.distanceTo(movingobjectposition1.hitVec);
                        if (d1 < d0 || d0 == 0.0) {
                            entity = entity1;
                            d0 = d1;
                        }
                    }
                }
            }

            if (entity != null) {
                movingobjectposition = new MovingObjectPosition(entity);
            }
        }

        if (movingobjectposition != null) {
            if (movingobjectposition.entityHit != null && movingobjectposition.entityHit.attackEntityFrom(this.thrower, 0)) {
            }

            if (!this.worldObj.multiplayerWorld && this.rand.nextInt(8) == 0) {
                byte b0 = 1;
                if (this.rand.nextInt(32) == 0) {
                    b0 = 4;
                }

                for (int i1 = 0; i1 < b0; i1++) {
                    EntityChicken entitychicken = new EntityChicken(this.worldObj);
                    entitychicken.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
                    this.worldObj.entityJoinedWorld(entitychicken);
                }
            }

            for (int l = 0; l < 8; l++) {
                this.worldObj.spawnParticle("snowballpoof", this.posX, this.posY, this.posZ, 0.0, 0.0, 0.0);
            }

            this.setEntityDead();
        }

        this.posX = this.posX + this.motionX;
        this.posY = this.posY + this.motionY;
        this.posZ = this.posZ + this.motionZ;
        float f1 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
        this.rotationYaw = (float)(Math.atan2(this.motionX, this.motionZ) * 180.0 / (float) Math.PI);
        this.rotationPitch = (float)(Math.atan2(this.motionY, f1) * 180.0 / (float) Math.PI);

        while (this.rotationPitch - this.prevRotationPitch < -180.0F) {
            this.prevRotationPitch -= 360.0F;
        }

        while (this.rotationPitch - this.prevRotationPitch >= 180.0F) {
            this.prevRotationPitch += 360.0F;
        }

        while (this.rotationYaw - this.prevRotationYaw < -180.0F) {
            this.prevRotationYaw -= 360.0F;
        }

        while (this.rotationYaw - this.prevRotationYaw >= 180.0F) {
            this.prevRotationYaw += 360.0F;
        }

        this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
        this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;
        float f2 = 0.99F;
        float f3 = 0.03F;
        if (this.isInWater()) {
            for (int j = 0; j < 4; j++) {
                float f4 = 0.25F;
                this.worldObj
                    .spawnParticle(
                        "bubble",
                        this.posX - this.motionX * f4,
                        this.posY - this.motionY * f4,
                        this.posZ - this.motionZ * f4,
                        this.motionX,
                        this.motionY,
                        this.motionZ
                    );
            }

            f2 = 0.8F;
        }

        this.motionX *= f2;
        this.motionY *= f2;
        this.motionZ *= f2;
        this.motionY -= f3;
        this.setPosition(this.posX, this.posY, this.posZ);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        nbt.setShort("xTile", (short)this.blockX);
        nbt.setShort("yTile", (short)this.blockY);
        nbt.setShort("zTile", (short)this.blockZ);
        nbt.setByte("inTile", (byte)this.block);
        nbt.setByte("shake", (byte)this.shake);
        nbt.setByte("inGround", (byte)(this.inGround ? 1 : 0));
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        this.blockX = nbt.getShort("xTile");
        this.blockY = nbt.getShort("yTile");
        this.blockZ = nbt.getShort("zTile");
        this.block = nbt.getByte("inTile") & 255;
        this.shake = nbt.getByte("shake") & 255;
        this.inGround = nbt.getByte("inGround") == 1;
    }

    @Override
    public void onCollideWithPlayer(EntityPlayer player) {
        if (this.inGround && this.thrower == player && this.shake <= 0 && player.inventory.addItemStackToInventory(new ItemStack(Item.arrow, 1))) {
            this.worldObj.playSoundAtEntity(this, "random.pop", 0.2F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
            player.onItemPickup(this, 1);
            this.setEntityDead();
        }
    }

    @Override
    public float getShadowSize() {
        return 0.0F;
    }
}
