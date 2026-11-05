package net.minecraft.src;

import org.lwjgl.input.Keyboard;

public class GuiMultiplayer
extends GuiScreen {
    private GuiScreen parentScreen;
    private GuiTextField serverField;

    public GuiMultiplayer(GuiScreen parent) {
        this.parentScreen = parent;
    }

    public void updateScreen() {
        this.serverField.updateCursorCounter();
    }

    public void initGui() {
        StringTranslate stringTranslate = StringTranslate.getInstance();
        Keyboard.enableRepeatEvents(true);
        this.controlList.clear();
        this.controlList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 96 + 12, stringTranslate.translateKey("multiplayer.connect")));
        this.controlList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 120 + 12, stringTranslate.translateKey("gui.cancel")));
        String string = this.mc.gameSettings.lastServer.replaceAll("_", ":");
        ((GuiButton)this.controlList.get((int)0)).enabled = string.length() > 0;
        this.serverField = new GuiTextField(this, this.fontRenderer, this.width / 2 - 100, this.height / 4 - 10 + 50 + 18, 200, 20, string);
        this.serverField.isFocused = true;
        this.serverField.setMaxStringLength(128);
    }

    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    protected void actionPerformed(GuiButton button) {
        if (!button.enabled) {
            return;
        }
        if (button.id == 1) {
            this.mc.displayGuiScreen(this.parentScreen);
        } else if (button.id == 0) {
            int n;
            String string = this.serverField.getText().trim();
            this.mc.gameSettings.lastServer = string.replaceAll(":", "_");
            this.mc.gameSettings.saveOptions();
            String[] stringArray = string.split(":");
            if (string.startsWith("[") && (n = string.indexOf("]")) > 0) {
                String string2 = string.substring(1, n);
                String string3 = string.substring(n + 1).trim();
                if (string3.startsWith(":") && string3.length() > 0) {
                    string3 = string3.substring(1);
                    stringArray = new String[]{string2, string3};
                } else {
                    stringArray = new String[]{string2};
                }
            }
            if (stringArray.length > 2) {
                stringArray = new String[]{string};
            }
            this.mc.displayGuiScreen(new GuiConnecting(this.mc, stringArray[0], stringArray.length > 1 ? this.parseIntWithDefault(stringArray[1], 25565) : 25565));
        }
    }

    private int parseIntWithDefault(String s, int defaultValue) {
        try {
            return Integer.parseInt(s.trim());
        }
        catch (Exception exception) {
            return defaultValue;
        }
    }

    protected void keyTyped(char chr, int key) {
        this.serverField.textboxKeyTyped(chr, key);
        if (chr == '\r') {
            this.actionPerformed((GuiButton)this.controlList.get(0));
        }
        ((GuiButton)this.controlList.get((int)0)).enabled = this.serverField.getText().length() > 0;
    }

    protected void mouseClicked(int mouseX, int mouseY, int button) {
        super.mouseClicked(mouseX, mouseY, button);
        this.serverField.mouseClicked(mouseX, mouseY, button);
    }

    public void drawScreen(int mouseX, int mouseY, float tickDelta) {
        StringTranslate stringTranslate = StringTranslate.getInstance();
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRenderer, stringTranslate.translateKey("multiplayer.title"), this.width / 2, this.height / 4 - 60 + 20, 0xFFFFFF);
        this.drawString(this.fontRenderer, stringTranslate.translateKey("multiplayer.info1"), this.width / 2 - 140, this.height / 4 - 60 + 60 + 0, 0xA0A0A0);
        this.drawString(this.fontRenderer, stringTranslate.translateKey("multiplayer.info2"), this.width / 2 - 140, this.height / 4 - 60 + 60 + 9, 0xA0A0A0);
        this.drawString(this.fontRenderer, stringTranslate.translateKey("multiplayer.ipinfo"), this.width / 2 - 140, this.height / 4 - 60 + 60 + 36, 0xA0A0A0);
        this.serverField.drawTextBox();
        super.drawScreen(mouseX, mouseY, tickDelta);
    }
}

