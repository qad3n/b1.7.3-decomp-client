package net.minecraft.src;


public class GuiControls
extends GuiScreen {
    private GuiScreen parentScreen;
    protected String screenTitle = "Controls";
    private GameSettings options;
    private int buttonId = -1;

    public GuiControls(GuiScreen parent, GameSettings options) {
        this.parentScreen = parent;
        this.options = options;
    }

    private int getControlsListX() {
        return this.width / 2 - 155;
    }

    public void initGui() {
        StringTranslate stringTranslate = StringTranslate.getInstance();
        int n = this.getControlsListX();
        for (int i = 0; i < this.options.keyBindings.length; ++i) {
            this.controlList.add(new GuiSmallButton(i, n + i % 2 * 160, this.height / 6 + 24 * (i >> 1), 70, 20, this.options.getOptionDisplayString(i)));
        }
        this.controlList.add(new GuiButton(200, this.width / 2 - 100, this.height / 6 + 168, stringTranslate.translateKey("gui.done")));
        this.screenTitle = stringTranslate.translateKey("controls.title");
    }

    protected void actionPerformed(GuiButton button) {
        for (int i = 0; i < this.options.keyBindings.length; ++i) {
            ((GuiButton)this.controlList.get((int)i)).displayString = this.options.getOptionDisplayString(i);
        }
        if (button.id == 200) {
            this.mc.displayGuiScreen(this.parentScreen);
        } else {
            this.buttonId = button.id;
            button.displayString = "> " + this.options.getOptionDisplayString(button.id) + " <";
        }
    }

    protected void keyTyped(char chr, int key) {
        if (this.buttonId >= 0) {
            this.options.setKeyBinding(this.buttonId, key);
            ((GuiButton)this.controlList.get((int)this.buttonId)).displayString = this.options.getOptionDisplayString(this.buttonId);
            this.buttonId = -1;
        } else {
            super.keyTyped(chr, key);
        }
    }

    public void drawScreen(int mouseX, int mouseY, float tickDelta) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRenderer, this.screenTitle, this.width / 2, 20, 0xFFFFFF);
        int n = this.getControlsListX();
        for (int i = 0; i < this.options.keyBindings.length; ++i) {
            this.drawString(this.fontRenderer, this.options.getKeyBindingDescription(i), n + i % 2 * 160 + 70 + 6, this.height / 6 + 24 * (i >> 1) + 7, -1);
        }
        super.drawScreen(mouseX, mouseY, tickDelta);
    }
}

