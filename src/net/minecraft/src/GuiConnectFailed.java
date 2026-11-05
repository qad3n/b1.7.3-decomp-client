package net.minecraft.src;

public class GuiConnectFailed extends GuiScreen {
    private String errorMessage;
    private String errorDetail;

    public GuiConnectFailed(String title, String reason, Object... reasonArgs) {
        StringTranslate stringtranslate = StringTranslate.getInstance();
        this.errorMessage = stringtranslate.translateKey(title);
        if (reasonArgs != null) {
            this.errorDetail = stringtranslate.translateKeyFormat(reason, reasonArgs);
        } else {
            this.errorDetail = stringtranslate.translateKey(reason);
        }
    }

    @Override
    public void updateScreen() {
    }

    @Override
    protected void keyTyped(char chr, int key) {
    }

    @Override
    public void initGui() {
        StringTranslate stringtranslate = StringTranslate.getInstance();
        this.controlList.clear();
        this.controlList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 120 + 12, stringtranslate.translateKey("gui.toMenu")));
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == 0) {
            this.mc.displayGuiScreen(new GuiMainMenu());
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float tickDelta) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRenderer, this.errorMessage, this.width / 2, this.height / 2 - 50, 16777215);
        this.drawCenteredString(this.fontRenderer, this.errorDetail, this.width / 2, this.height / 2 - 10, 16777215);
        super.drawScreen(mouseX, mouseY, tickDelta);
    }
}
