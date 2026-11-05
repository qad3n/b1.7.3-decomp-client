package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class GuiGameOver
extends GuiScreen {
    public void initGui() {
        this.controlList.clear();
        this.controlList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 72, "Respawn"));
        this.controlList.add(new GuiButton(2, this.width / 2 - 100, this.height / 4 + 96, "Title menu"));
        if (this.mc.session == null) {
            ((GuiButton)this.controlList.get((int)1)).enabled = false;
        }
    }

    protected void keyTyped(char chr, int key) {
    }

    protected void actionPerformed(GuiButton button) {
        if (button.id == 0) {
            // empty if block
        }
        if (button.id == 1) {
            this.mc.thePlayer.respawnPlayer();
            this.mc.displayGuiScreen(null);
        }
        if (button.id == 2) {
            this.mc.changeWorld1(null);
            this.mc.displayGuiScreen(new GuiMainMenu());
        }
    }

    public void drawScreen(int mouseX, int mouseY, float tickDelta) {
        this.drawGradientRect(0, 0, this.width, this.height, 0x60500000, -1602211792);
        GL11.glPushMatrix();
        GL11.glScalef(2.0f, 2.0f, 2.0f);
        this.drawCenteredString(this.fontRenderer, "Game over!", this.width / 2 / 2, 30, 0xFFFFFF);
        GL11.glPopMatrix();
        this.drawCenteredString(this.fontRenderer, "Score: &e" + this.mc.thePlayer.getScore(), this.width / 2, 100, 0xFFFFFF);
        super.drawScreen(mouseX, mouseY, tickDelta);
    }

    public boolean doesGuiPauseGame() {
        return false;
    }
}

