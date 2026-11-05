package net.minecraft.src;


public class GuiVideoSettings
extends GuiScreen {
    private GuiScreen parent;
    protected String title = "Video Settings";
    private GameSettings guiGameSettings;
    private static GameSettings.EnumOptions[] VIDEO_OPTIONS = new GameSettings.EnumOptions[]{GameSettings.EnumOptions.GRAPHICS, GameSettings.EnumOptions.RENDER_DISTANCE, GameSettings.EnumOptions.AMBIENT_OCCLUSION, GameSettings.EnumOptions.FRAMERATE_LIMIT, GameSettings.EnumOptions.ANAGLYPH, GameSettings.EnumOptions.VIEW_BOBBING, GameSettings.EnumOptions.GUI_SCALE, GameSettings.EnumOptions.ADVANCED_OPENGL};

    public GuiVideoSettings(GuiScreen parent, GameSettings options) {
        this.parent = parent;
        this.guiGameSettings = options;
    }

    public void initGui() {
        StringTranslate stringTranslate = StringTranslate.getInstance();
        this.title = stringTranslate.translateKey("options.videoTitle");
        int n = 0;
        for (GameSettings.EnumOptions enumOptions : VIDEO_OPTIONS) {
            if (!enumOptions.getEnumFloat()) {
                this.controlList.add(new GuiSmallButton(enumOptions.returnEnumOrdinal(), this.width / 2 - 155 + n % 2 * 160, this.height / 6 + 24 * (n >> 1), enumOptions, this.guiGameSettings.getKeyBinding(enumOptions)));
            } else {
                this.controlList.add(new GuiSlider(enumOptions.returnEnumOrdinal(), this.width / 2 - 155 + n % 2 * 160, this.height / 6 + 24 * (n >> 1), enumOptions, this.guiGameSettings.getKeyBinding(enumOptions), this.guiGameSettings.getOptionFloatValue(enumOptions)));
            }
            ++n;
        }
        this.controlList.add(new GuiButton(200, this.width / 2 - 100, this.height / 6 + 168, stringTranslate.translateKey("gui.done")));
    }

    protected void actionPerformed(GuiButton button) {
        if (!button.enabled) {
            return;
        }
        if (button.id < 100 && button instanceof GuiSmallButton) {
            this.guiGameSettings.setOptionValue(((GuiSmallButton)button).returnEnumOptions(), 1);
            button.displayString = this.guiGameSettings.getKeyBinding(GameSettings.EnumOptions.getEnumOptions(button.id));
        }
        if (button.id == 200) {
            this.mc.gameSettings.saveOptions();
            this.mc.displayGuiScreen(this.parent);
        }
        ScaledResolution scaledResolution = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
        int n = scaledResolution.getScaledWidth();
        int n2 = scaledResolution.getScaledHeight();
        this.setWorldAndResolution(this.mc, n, n2);
    }

    public void drawScreen(int mouseX, int mouseY, float tickDelta) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRenderer, this.title, this.width / 2, 20, 0xFFFFFF);
        super.drawScreen(mouseX, mouseY, tickDelta);
    }
}

