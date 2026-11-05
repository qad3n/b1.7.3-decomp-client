package net.minecraft.src;

import org.lwjgl.input.Keyboard;

public class GuiRenameWorld
extends GuiScreen {
    private GuiScreen parent;
    private GuiTextField searchField;
    private final String worldName;

    public GuiRenameWorld(GuiScreen parent, String worldName) {
        this.parent = parent;
        this.worldName = worldName;
    }

    public void updateScreen() {
        this.searchField.updateCursorCounter();
    }

    public void initGui() {
        StringTranslate stringTranslate = StringTranslate.getInstance();
        Keyboard.enableRepeatEvents(true);
        this.controlList.clear();
        this.controlList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 96 + 12, stringTranslate.translateKey("selectWorld.renameButton")));
        this.controlList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 120 + 12, stringTranslate.translateKey("gui.cancel")));
        ISaveFormat iSaveFormat = this.mc.getSaveLoader();
        WorldInfo worldInfo = iSaveFormat.getData(this.worldName);
        String string = worldInfo.getWorldName();
        this.searchField = new GuiTextField(this, this.fontRenderer, this.width / 2 - 100, 60, 200, 20, string);
        this.searchField.isFocused = true;
        this.searchField.setMaxStringLength(32);
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
            ISaveFormat iSaveFormat = this.mc.getSaveLoader();
            iSaveFormat.rename(this.worldName, this.searchField.getText().trim());
            this.mc.displayGuiScreen(this.parent);
        }
    }

    @Override
protected void keyTyped(char chr, int key) {
    this.searchField.textboxKeyTyped(chr, key);
    this.controlList.get(0).enabled = this.searchField.getText().trim().length() > 0;
    if (chr == '\r') {
        this.actionPerformed(this.controlList.get(0));
    }
}

    protected void mouseClicked(int mouseX, int mouseY, int button) {
        super.mouseClicked(mouseX, mouseY, button);
        this.searchField.mouseClicked(mouseX, mouseY, button);
    }

    public void drawScreen(int mouseX, int mouseY, float tickDelta) {
        StringTranslate stringTranslate = StringTranslate.getInstance();
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRenderer, stringTranslate.translateKey("selectWorld.renameTitle"), this.width / 2, this.height / 4 - 60 + 20, 0xFFFFFF);
        this.drawString(this.fontRenderer, stringTranslate.translateKey("selectWorld.enterName"), this.width / 2 - 100, 47, 0xA0A0A0);
        this.searchField.drawTextBox();
        super.drawScreen(mouseX, mouseY, tickDelta);
    }
}

