package net.minecraft.src;


public class EntitySquid
extends EntityWaterMob {
    public float bodyPitch = 0.0f;
    public float lastBodyPitch = 0.0f;
    public float bodyRoll = 0.0f;
    public float lastBodyRoll = 0.0f;
    public float tentacleMovement = 0.0f;
    public float lastTentacleMovement = 0.0f;
    public float tentacleRotation = 0.0f;
    public float lastTentacleRotation = 0.0f;
    private float randomMotionSpeed = 0.0f;
    private float tentacleSpeed = 0.0f;
    private float rotationSpeed = 0.0f;
    private float randomMotionVecX = 0.0f;
    private float randomMotionVecY = 0.0f;
    private float randomMotionVecZ = 0.0f;

    public EntitySquid(World world) {
        super(world);
        this.texture = "/mob/squid.png";
        this.setSize(0.95f, 0.95f);
        this.tentacleSpeed = 1.0f / (this.rand.nextFloat() + 1.0f) * 0.2f;
    }

    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
    }

    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);
    }

    protected String getLivingSound() {
        return null;
    }

    protected String getHurtSound() {
        return null;
    }

    protected String getDeathSound() {
        return null;
    }

    protected float getSoundVolume() {
        return 0.4f;
    }

    protected int getDropItemId() {
        return 0;
    }

    protected void dropFewItems() {
        int n = this.rand.nextInt(3) + 1;
        for (int i = 0; i < n; ++i) {
            this.entityDropItem(new ItemStack(Item.dyePowder, 1, 0), 0.0f);
        }
    }

    public boolean interact(EntityPlayer player) {
        return false;
    }

    public boolean isInWater() {
        return this.worldObj.handleMaterialAcceleration(this.boundingBox.expand(0.0, -0.6f, 0.0), Material.water, this);
    }

    public void onLivingUpdate() {
        super.onLivingUpdate();
        this.lastBodyPitch = this.bodyPitch;
        this.lastBodyRoll = this.bodyRoll;
        this.lastTentacleMovement = this.tentacleMovement;
        this.lastTentacleRotation = this.tentacleRotation;
        this.tentacleMovement += this.tentacleSpeed;
        if (this.tentacleMovement > (float)Math.PI * 2) {
            this.tentacleMovement -= (float)Math.PI * 2;
            if (this.rand.nextInt(10) == 0) {
                this.tentacleSpeed = 1.0f / (this.rand.nextFloat() + 1.0f) * 0.2f;
            }
        }
        if (this.isInWater()) {
            float f;
            if (this.tentacleMovement < (float)Math.PI) {
                f = this.tentacleMovement / (float)Math.PI;
                this.tentacleRotation = MathHelper.sin(f * f * (float)Math.PI) * (float)Math.PI * 0.25f;
                if ((double)f > 0.75) {
                    this.randomMotionSpeed = 1.0f;
                    this.rotationSpeed = 1.0f;
                } else {
                    this.rotationSpeed *= 0.8f;
                }
            } else {
                this.tentacleRotation = 0.0f;
                this.randomMotionSpeed *= 0.9f;
                this.rotationSpeed *= 0.99f;
            }
            if (!this.isMultiplayerEntity) {
                this.motionX = this.randomMotionVecX * this.randomMotionSpeed;
                this.motionY = this.randomMotionVecY * this.randomMotionSpeed;
                this.motionZ = this.randomMotionVecZ * this.randomMotionSpeed;
            }
            f = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
            this.renderYawOffset += (-((float)Math.atan2(this.motionX, this.motionZ)) * 180.0f / (float)Math.PI - this.renderYawOffset) * 0.1f;
            this.rotationYaw = this.renderYawOffset;
            this.bodyRoll += (float)Math.PI * this.rotationSpeed * 1.5f;
            this.bodyPitch += (-((float)Math.atan2(f, this.motionY)) * 180.0f / (float)Math.PI - this.bodyPitch) * 0.1f;
        } else {
            this.tentacleRotation = MathHelper.abs(MathHelper.sin(this.tentacleMovement)) * (float)Math.PI * 0.25f;
            if (!this.isMultiplayerEntity) {
                this.motionX = 0.0;
                this.motionY -= 0.08;
                this.motionY *= (double)0.98f;
                this.motionZ = 0.0;
            }
            this.bodyPitch = (float)((double)this.bodyPitch + (double)(-90.0f - this.bodyPitch) * 0.02);
        }
    }

    public void moveEntityWithHeading(float sideways, float forwards) {
        this.moveEntity(this.motionX, this.motionY, this.motionZ);
    }

    protected void updatePlayerActionState() {
        if (this.rand.nextInt(50) == 0 || !this.inWater || this.randomMotionVecX == 0.0f && this.randomMotionVecY == 0.0f && this.randomMotionVecZ == 0.0f) {
            float f = this.rand.nextFloat() * (float)Math.PI * 2.0f;
            this.randomMotionVecX = MathHelper.cos(f) * 0.2f;
            this.randomMotionVecY = -0.1f + this.rand.nextFloat() * 0.2f;
            this.randomMotionVecZ = MathHelper.sin(f) * 0.2f;
        }
        this.checkDespawn();
    }
}

