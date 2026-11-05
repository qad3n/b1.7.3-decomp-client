package net.minecraft.src;

import java.util.Random;
import org.lwjgl.input.Keyboard;

public class GuiCreateWorld
extends GuiScreen {
    private GuiScreen parent;
    private GuiTextField textboxWorldName;
    private GuiTextField textboxSeed;
    private String folderName;
    private boolean createClicked;

    public GuiCreateWorld(GuiScreen parent) {
        this.parent = parent;
    }

    public void updateScreen() {
        this.textboxWorldName.updateCursorCounter();
        this.textboxSeed.updateCursorCounter();
    }

    public void initGui() {
        StringTranslate stringTranslate = StringTranslate.getInstance();
        Keyboard.enableRepeatEvents(true);
        this.controlList.clear();
        this.controlList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 96 + 12, stringTranslate.translateKey("selectWorld.create")));
        this.controlList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 120 + 12, stringTranslate.translateKey("gui.cancel")));
        this.textboxWorldName = new GuiTextField(this, this.fontRenderer, this.width / 2 - 100, 60, 200, 20, stringTranslate.translateKey("selectWorld.newWorld"));
        this.textboxWorldName.isFocused = true;
        this.textboxWorldName.setMaxStringLength(32);
        this.textboxSeed = new GuiTextField(this, this.fontRenderer, this.width / 2 - 100, 116, 200, 20, "");
        this.getSaveDirectoryName();
    }

    private void getSaveDirectoryName() {
        this.folderName = this.textboxWorldName.getText().trim();
        for (char c : ChatAllowedCharacters.allowedCharactersArray) {
            this.folderName = this.folderName.replace(c, '_');
        }
        if (MathHelper.stringNullOrLengthZero(this.folderName)) {
            this.folderName = "World";
        }
        this.folderName = GuiCreateWorld.generateUnusedFolderName(this.mc.getSaveLoader(), this.folderName);
    }

    public static String generateUnusedFolderName(ISaveFormat storageSource, String worldName) {
        while (storageSource.getData(worldName) != null) {
            worldName = worldName + "-";
        }
        return worldName;
    }

    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    protected void actionPerformed(GuiButton button) {
        if (!button.enabled) {
            return;
        }
        if (button.id == 1) {
            this.mc.displayGuiScreen(this.parent);
        } else if (button.id == 0) {
            this.mc.displayGuiScreen(null);
            if (this.createClicked) {
                return;
            }
            this.createClicked = true;
            long l = new Random().nextLong();
            String string = this.textboxSeed.getText();
            if (!MathHelper.stringNullOrLengthZero(string)) {
                try {
                    long l2 = Long.parseLong(string);
                    if (l2 != 0L) {
                        l = l2;
                    }
                }
                catch (NumberFormatException numberFormatException) {
                    l = string.hashCode();
                }
            }
            this.mc.playerController = new PlayerControllerSP(this.mc);
            this.mc.startWorld(this.folderName, this.textboxWorldName.getText(), l);
            this.mc.displayGuiScreen(null);
        }
    }

    protected void keyTyped(char chr, int key) {
        if (this.textboxWorldName.isFocused) {
            this.textboxWorldName.textboxKeyTyped(chr, key);
        } else {
            this.textboxSeed.textboxKeyTyped(chr, key);
        }
        if (chr == '\r') {
            this.actionPerformed((GuiButton)this.controlList.get(0));
        }
        ((GuiButton)this.controlList.get((int)0)).enabled = this.textboxWorldName.getText().length() > 0;
        this.getSaveDirectoryName();
    }

    protected void mouseClicked(int mouseX, int mouseY, int button) {
        super.mouseClicked(mouseX, mouseY, button);
        this.textboxWorldName.mouseClicked(mouseX, mouseY, button);
        this.textboxSeed.mouseClicked(mouseX, mouseY, button);
    }

    public void drawScreen(int mouseX, int mouseY, float tickDelta) {
        StringTranslate stringTranslate = StringTranslate.getInstance();
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRenderer, stringTranslate.translateKey("selectWorld.create"), this.width / 2, this.height / 4 - 60 + 20, 0xFFFFFF);
        this.drawString(this.fontRenderer, stringTranslate.translateKey("selectWorld.enterName"), this.width / 2 - 100, 47, 0xA0A0A0);
        this.drawString(this.fontRenderer, stringTranslate.translateKey("selectWorld.resultFolder") + " " + this.folderName, this.width / 2 - 100, 85, 0xA0A0A0);
        this.drawString(this.fontRenderer, stringTranslate.translateKey("selectWorld.enterSeed"), this.width / 2 - 100, 104, 0xA0A0A0);
        this.drawString(this.fontRenderer, stringTranslate.translateKey("selectWorld.seedInfo"), this.width / 2 - 100, 140, 0xA0A0A0);
        this.textboxWorldName.drawTextBox();
        this.textboxSeed.drawTextBox();
        super.drawScreen(mouseX, mouseY, tickDelta);
    }

    public void selectNextField() {
        if (this.textboxWorldName.isFocused) {
            this.textboxWorldName.setFocused(false);
            this.textboxSeed.setFocused(true);
        } else {
            this.textboxWorldName.setFocused(true);
            this.textboxSeed.setFocused(false);
        }
    }
}

