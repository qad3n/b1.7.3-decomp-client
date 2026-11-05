package net.minecraft.src;

import net.minecraft.client.Minecraft;

public class PlayerControllerTest
extends PlayerController {
    public PlayerControllerTest(Minecraft minecraft) {
        super(minecraft);
        this.creative = true;
    }

    public void adjustPlayer(EntityPlayer player) {
        for (int i = 0; i < 9; ++i) {
            if (player.inventory.mainInventory[i] == null) {
                this.mc.thePlayer.inventory.mainInventory[i] = new ItemStack(Session.registeredBlocksList.get(i));
                continue;
            }
            this.mc.thePlayer.inventory.mainInventory[i].stackSize = 1;
        }
    }

    public boolean shouldDrawHUD() {
        return false;
    }

    public void initWorld(World world) {
        super.initWorld(world);
    }

    public void updateController() {
    }
}

