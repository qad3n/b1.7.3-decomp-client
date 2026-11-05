package net.minecraft.src;


public class EntityOtherPlayerMP
extends EntityPlayer {
    private int lerpSteps;
    private double lerpX;
    private double lerpY;
    private double lerpZ;
    private double lerpYaw;
    private double lerpPitch;
    float field_20924_a = 0.0f;

    public EntityOtherPlayerMP(World world, String name) {
        super(world);
        this.username = name;
        this.yOffset = 0.0f;
        this.stepHeight = 0.0f;
        if (name != null && name.length() > 0) {
            this.skinUrl = "http://s3.amazonaws.com/MinecraftSkins/" + name + ".png";
        }
        this.noClip = true;
        this.sleepingCameraOffsetY = 0.25f;
        this.renderDistanceWeight = 10.0;
    }

    protected void resetHeight() {
        this.yOffset = 0.0f;
    }

    public boolean attackEntityFrom(Entity source, int amount) {
        return true;
    }

    public void setPositionAndRotation2(double x, double y, double z, float yaw, float pitch, int steps) {
        this.lerpX = x;
        this.lerpY = y;
        this.lerpZ = z;
        this.lerpYaw = yaw;
        this.lerpPitch = pitch;
        this.lerpSteps = steps;
    }

    public void onUpdate() {
        this.sleepingCameraOffsetY = 0.0f;
        super.onUpdate();
        this.lastWalkAnimationSpeed = this.walkAnimationSpeed;
        double d = this.posX - this.prevPosX;
        double d2 = this.posZ - this.prevPosZ;
        float f = MathHelper.sqrt_double(d * d + d2 * d2) * 4.0f;
        if (f > 1.0f) {
            f = 1.0f;
        }
        this.walkAnimationSpeed += (f - this.walkAnimationSpeed) * 0.4f;
        this.walkAnimationProgress += this.walkAnimationSpeed;
    }

    public float getShadowSize() {
        return 0.0f;
    }

    public void onLivingUpdate() {
        super.updatePlayerActionState();
        if (this.lerpSteps > 0) {
            double d;
            double d2 = this.posX + (this.lerpX - this.posX) / (double)this.lerpSteps;
            double d3 = this.posY + (this.lerpY - this.posY) / (double)this.lerpSteps;
            double d4 = this.posZ + (this.lerpZ - this.posZ) / (double)this.lerpSteps;
            for (d = this.lerpYaw - (double)this.rotationYaw; d < -180.0; d += 360.0) {
            }
            while (d >= 180.0) {
                d -= 360.0;
            }
            this.rotationYaw = (float)((double)this.rotationYaw + d / (double)this.lerpSteps);
            this.rotationPitch = (float)((double)this.rotationPitch + (this.lerpPitch - (double)this.rotationPitch) / (double)this.lerpSteps);
            --this.lerpSteps;
            this.setPosition(d2, d3, d4);
            this.setRotation(this.rotationYaw, this.rotationPitch);
        }
        this.lastBob = this.bob;
        float f = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
        float f2 = (float)Math.atan(-this.motionY * (double)0.2f) * 15.0f;
        if (f > 0.1f) {
            f = 0.1f;
        }
        if (!this.onGround || this.health <= 0) {
            f = 0.0f;
        }
        if (this.onGround || this.health <= 0) {
            f2 = 0.0f;
        }
        this.bob += (f - this.bob) * 0.4f;
        this.tilt += (f2 - this.tilt) * 0.8f;
    }

    public void outfitWithItem(int slot, int item, int metadata) {
        ItemStack itemStack = null;
        if (item >= 0) {
            itemStack = new ItemStack(item, 1, metadata);
        }
        if (slot == 0) {
            this.inventory.mainInventory[this.inventory.currentItem] = itemStack;
        } else {
            this.inventory.armorInventory[slot - 1] = itemStack;
        }
    }

    public void addRespawnParticles() {
    }
}

