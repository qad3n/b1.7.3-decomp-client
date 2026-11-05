package net.minecraft.src;

import org.lwjgl.input.Keyboard;

public class GuiChat extends GuiScreen {
    protected String message = "";
    private int updateCounter = 0;
    private static final String VALID_CHARACTERS = ChatAllowedCharacters.allowedCharacters;

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    public void updateScreen() {
        this.updateCounter++;
    }

    @Override
    protected void keyTyped(char chr, int key) {
        if (key == 1) {
            this.mc.displayGuiScreen(null);
        } else if (key == 28) {
            String s = this.message.trim();
            if (s.length() > 0) {
                String s1 = this.message.trim();
                if (!this.mc.lineIsCommand(s1)) {
                    this.mc.thePlayer.sendChatMessage(s1);
                }
            }

            this.mc.displayGuiScreen(null);
        } else {
            if (key == 14 && this.message.length() > 0) {
                this.message = this.message.substring(0, this.message.length() - 1);
            }

            if (VALID_CHARACTERS.indexOf(chr) >= 0 && this.message.length() < 100) {
                this.message = this.message + chr;
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float tickDelta) {
        this.drawRect(2, this.height - 14, this.width - 2, this.height - 2, Integer.MIN_VALUE);
        this.drawString(this.fontRenderer, "> " + this.message + (this.updateCounter / 6 % 2 == 0 ? "_" : ""), 4, this.height - 12, 14737632);
        super.drawScreen(mouseX, mouseY, tickDelta);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int button) {
        if (button == 0) {
            if (this.mc.ingameGUI.selectedName != null) {
                if (this.message.length() > 0 && !this.message.endsWith(" ")) {
                    this.message = this.message + " ";
                }

                this.message = this.message + this.mc.ingameGUI.selectedName;
                byte b0 = 100;
                if (this.message.length() > b0) {
                    this.message = this.message.substring(0, b0);
                }
            } else {
                super.mouseClicked(mouseX, mouseY, button);
            }
        }
    }
}
