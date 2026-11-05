package net.minecraft.src;

import org.lwjgl.input.Keyboard;

public class GuiSleepMP
extends GuiChat {
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        StringTranslate stringTranslate = StringTranslate.getInstance();
        this.controlList.add(new GuiButton(1, this.width / 2 - 100, this.height - 40, stringTranslate.translateKey("multiplayer.stopSleeping")));
    }

    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    protected void keyTyped(char chr, int key) {
        if (key == 1) {
            this.stopSleeping();
        } else if (key == 28) {
            String string = this.message.trim();
            if (string.length() > 0) {
                this.mc.thePlayer.sendChatMessage(this.message.trim());
            }
            this.message = "";
        } else {
            super.keyTyped(chr, key);
        }
    }

    public void drawScreen(int mouseX, int mouseY, float tickDelta) {
        super.drawScreen(mouseX, mouseY, tickDelta);
    }

    protected void actionPerformed(GuiButton button) {
        if (button.id == 1) {
            this.stopSleeping();
        } else {
            super.actionPerformed(button);
        }
    }

    private void stopSleeping() {
        if (this.mc.thePlayer instanceof EntityClientPlayerMP) {
            NetClientHandler netClientHandler = ((EntityClientPlayerMP)this.mc.thePlayer).sendQueue;
            netClientHandler.addToSendQueue(new Packet19EntityAction(this.mc.thePlayer, 3));
        }
    }
}

