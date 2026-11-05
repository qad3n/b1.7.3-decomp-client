package net.minecraft.src;

import net.minecraft.client.Minecraft;

public class EntityPlayerSP
extends EntityPlayer {
    public MovementInput movementInput;
    protected Minecraft mc;
    private MouseFilter field_21903_bJ = new MouseFilter();
    private MouseFilter field_21904_bK = new MouseFilter();
    private MouseFilter field_21902_bL = new MouseFilter();

    public EntityPlayerSP(Minecraft minecraft, World world, Session session, int dimension) {
        super(world);
        this.mc = minecraft;
        this.dimension = dimension;
        if (session != null && session.username != null && session.username.length() > 0) {
            this.skinUrl = "http://s3.amazonaws.com/MinecraftSkins/" + session.username + ".png";
        }
        this.username = session.username;
    }

    public void moveEntity(double dx, double dy, double dz) {
        super.moveEntity(dx, dy, dz);
    }

    public void updatePlayerActionState() {
        super.updatePlayerActionState();
        this.moveStrafing = this.movementInput.moveStrafe;
        this.moveForward = this.movementInput.moveForward;
        this.isJumping = this.movementInput.jump;
    }

    public void onLivingUpdate() {
        if (!this.mc.statFileWriter.hasAchievementUnlocked(AchievementList.openInventory)) {
            this.mc.guiAchievement.queueAchievementInformation(AchievementList.openInventory);
        }
        this.prevTimeInPortal = this.timeInPortal;
        if (this.inPortal) {
            if (!this.worldObj.multiplayerWorld && this.ridingEntity != null) {
                this.mountEntity(null);
            }
            if (this.mc.currentScreen != null) {
                this.mc.displayGuiScreen(null);
            }
            if (this.timeInPortal == 0.0f) {
                this.mc.sndManager.playSoundFX("portal.trigger", 1.0f, this.rand.nextFloat() * 0.4f + 0.8f);
            }
            this.timeInPortal += 0.0125f;
            if (this.timeInPortal >= 1.0f) {
                this.timeInPortal = 1.0f;
                if (!this.worldObj.multiplayerWorld) {
                    this.timeUntilPortal = 10;
                    this.mc.sndManager.playSoundFX("portal.travel", 1.0f, this.rand.nextFloat() * 0.4f + 0.8f);
                    this.mc.usePortal();
                }
            }
            this.inPortal = false;
        } else {
            if (this.timeInPortal > 0.0f) {
                this.timeInPortal -= 0.05f;
            }
            if (this.timeInPortal < 0.0f) {
                this.timeInPortal = 0.0f;
            }
        }
        if (this.timeUntilPortal > 0) {
            --this.timeUntilPortal;
        }
        this.movementInput.updatePlayerMoveState(this);
        if (this.movementInput.sneak && this.ySize < 0.2f) {
            this.ySize = 0.2f;
        }
        this.pushOutOfBlocks(this.posX - (double)this.width * 0.35, this.boundingBox.minY + 0.5, this.posZ + (double)this.width * 0.35);
        this.pushOutOfBlocks(this.posX - (double)this.width * 0.35, this.boundingBox.minY + 0.5, this.posZ - (double)this.width * 0.35);
        this.pushOutOfBlocks(this.posX + (double)this.width * 0.35, this.boundingBox.minY + 0.5, this.posZ - (double)this.width * 0.35);
        this.pushOutOfBlocks(this.posX + (double)this.width * 0.35, this.boundingBox.minY + 0.5, this.posZ + (double)this.width * 0.35);
        super.onLivingUpdate();
    }

    public void resetPlayerKeyState() {
        this.movementInput.resetKeyState();
    }

    public void handleKeyPress(int key, boolean state) {
        this.movementInput.checkKeyForMovementInput(key, state);
    }

    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        nbt.setInteger("Score", this.score);
    }

    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);
        this.score = nbt.getInteger("Score");
    }

    public void closeScreen() {
        super.closeScreen();
        this.mc.displayGuiScreen(null);
    }

    public void displayGUIEditSign(TileEntitySign sign) {
        this.mc.displayGuiScreen(new GuiEditSign(sign));
    }

    public void displayGUIChest(IInventory inventory) {
        this.mc.displayGuiScreen(new GuiChest(this.inventory, inventory));
    }

    public void displayWorkbenchGUI(int x, int y, int z) {
        this.mc.displayGuiScreen(new GuiCrafting(this.inventory, this.worldObj, x, y, z));
    }

    public void displayGUIFurnace(TileEntityFurnace furnace) {
        this.mc.displayGuiScreen(new GuiFurnace(this.inventory, furnace));
    }

    public void displayGUIDispenser(TileEntityDispenser dispenser) {
        this.mc.displayGuiScreen(new GuiDispenser(this.inventory, dispenser));
    }

    public void onItemPickup(Entity item, int count) {
        this.mc.effectRenderer.addEffect(new EntityPickupFX(this.mc.theWorld, item, this, -0.5f));
    }

    public int getPlayerArmorValue() {
        return this.inventory.getTotalArmorValue();
    }

    public void sendChatMessage(String content) {
    }

    public boolean isSneaking() {
        return this.movementInput.sneak && !this.sleeping;
    }

    public void setHealth(int health) {
        int i = this.health - health;
        if (i <= 0) {
            this.health = health;
            if (i < 0) {
                this.heartsLife = this.heartsHalvesLife / 2;
            }
        } else {
            this.prevDamageTaken = i;
            this.prevHealth = this.health;
            this.heartsLife = this.heartsHalvesLife;
            this.damageEntity(i);
            this.hurtTime = this.maxHurtTime = 10;
        }
    }

    public void respawnPlayer() {
        this.mc.respawn(false, 0);
    }

    public void addRespawnParticles() {
    }

    public void addChatMessage(String message) {
        this.mc.ingameGUI.addChatMessageTranslate(message);
    }

    public void addStat(StatBase stat, int amount) {
        if (stat == null) {
            return;
        }
        if (stat.isAchievement()) {
            Achievement achievement = (Achievement)stat;
            if (achievement.parentAchievement == null || this.mc.statFileWriter.hasAchievementUnlocked(achievement.parentAchievement)) {
                if (!this.mc.statFileWriter.hasAchievementUnlocked(achievement)) {
                    this.mc.guiAchievement.queueTakenAchievement(achievement);
                }
                this.mc.statFileWriter.readStat(stat, amount);
            }
        } else {
            this.mc.statFileWriter.readStat(stat, amount);
        }
    }

    private boolean isBlockTranslucent(int x, int y, int z) {
        return this.worldObj.isBlockNormalCube(x, y, z);
    }

    protected boolean pushOutOfBlocks(double x, double y, double z) {
        int n = MathHelper.floor_double(x);
        int n2 = MathHelper.floor_double(y);
        int n3 = MathHelper.floor_double(z);
        double d = x - (double)n;
        double d2 = z - (double)n3;
        if (this.isBlockTranslucent(n, n2, n3) || this.isBlockTranslucent(n, n2 + 1, n3)) {
            boolean bl = !this.isBlockTranslucent(n - 1, n2, n3) && !this.isBlockTranslucent(n - 1, n2 + 1, n3);
            boolean bl2 = !this.isBlockTranslucent(n + 1, n2, n3) && !this.isBlockTranslucent(n + 1, n2 + 1, n3);
            boolean bl3 = !this.isBlockTranslucent(n, n2, n3 - 1) && !this.isBlockTranslucent(n, n2 + 1, n3 - 1);
            boolean bl4 = !this.isBlockTranslucent(n, n2, n3 + 1) && !this.isBlockTranslucent(n, n2 + 1, n3 + 1);
            int n4 = -1;
            double d3 = 9999.0;
            if (bl && d < d3) {
                d3 = d;
                n4 = 0;
            }
            if (bl2 && 1.0 - d < d3) {
                d3 = 1.0 - d;
                n4 = 1;
            }
            if (bl3 && d2 < d3) {
                d3 = d2;
                n4 = 4;
            }
            if (bl4 && 1.0 - d2 < d3) {
                d3 = 1.0 - d2;
                n4 = 5;
            }
            float f = 0.1f;
            if (n4 == 0) {
                this.motionX = -f;
            }
            if (n4 == 1) {
                this.motionX = f;
            }
            if (n4 == 4) {
                this.motionZ = -f;
            }
            if (n4 == 5) {
                this.motionZ = f;
            }
        }
        return false;
    }
}

