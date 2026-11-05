package net.minecraft.src;


public class GuiOptions
extends GuiScreen {
    private GuiScreen parentScreen;
    protected String screenTitle = "Options";
    private GameSettings options;
    private static GameSettings.EnumOptions[] RENDER_OPTIONS = new GameSettings.EnumOptions[]{GameSettings.EnumOptions.MUSIC, GameSettings.EnumOptions.SOUND, GameSettings.EnumOptions.INVERT_MOUSE, GameSettings.EnumOptions.SENSITIVITY, GameSettings.EnumOptions.DIFFICULTY};

    public GuiOptions(GuiScreen parent, GameSettings options) {
        this.parentScreen = parent;
        this.options = options;
    }

    public void initGui() {
        StringTranslate stringTranslate = StringTranslate.getInstance();
        this.screenTitle = stringTranslate.translateKey("options.title");
        int n = 0;
        for (GameSettings.EnumOptions enumOptions : RENDER_OPTIONS) {
            if (!enumOptions.getEnumFloat()) {
                this.controlList.add(new GuiSmallButton(enumOptions.returnEnumOrdinal(), this.width / 2 - 155 + n % 2 * 160, this.height / 6 + 24 * (n >> 1), enumOptions, this.options.getKeyBinding(enumOptions)));
            } else {
                this.controlList.add(new GuiSlider(enumOptions.returnEnumOrdinal(), this.width / 2 - 155 + n % 2 * 160, this.height / 6 + 24 * (n >> 1), enumOptions, this.options.getKeyBinding(enumOptions), this.options.getOptionFloatValue(enumOptions)));
            }
            ++n;
        }
        this.controlList.add(new GuiButton(101, this.width / 2 - 100, this.height / 6 + 96 + 12, stringTranslate.translateKey("options.video")));
        this.controlList.add(new GuiButton(100, this.width / 2 - 100, this.height / 6 + 120 + 12, stringTranslate.translateKey("options.controls")));
        this.controlList.add(new GuiButton(200, this.width / 2 - 100, this.height / 6 + 168, stringTranslate.translateKey("gui.done")));
    }

    protected void actionPerformed(GuiButton button) {
        if (!button.enabled) {
            return;
        }
        if (button.id < 100 && button instanceof GuiSmallButton) {
            this.options.setOptionValue(((GuiSmallButton)button).returnEnumOptions(), 1);
            button.displayString = this.options.getKeyBinding(GameSettings.EnumOptions.getEnumOptions(button.id));
        }
        if (button.id == 101) {
            this.mc.gameSettings.saveOptions();
            this.mc.displayGuiScreen(new GuiVideoSettings(this, this.options));
        }
        if (button.id == 100) {
            this.mc.gameSettings.saveOptions();
            this.mc.displayGuiScreen(new GuiControls(this, this.options));
        }
        if (button.id == 200) {
            this.mc.gameSettings.saveOptions();
            this.mc.displayGuiScreen(this.parentScreen);
        }
    }

    public void drawScreen(int mouseX, int mouseY, float tickDelta) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRenderer, this.screenTitle, this.width / 2, 20, 0xFFFFFF);
        super.drawScreen(mouseX, mouseY, tickDelta);
    }
}

