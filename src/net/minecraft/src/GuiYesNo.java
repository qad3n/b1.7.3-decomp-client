package net.minecraft.src;


public class GuiYesNo
extends GuiScreen {
    private GuiScreen parentScreen;
    private String message1;
    private String message2;
    private String confirmText;
    private String abortText;
    private int worldNumber;

    public GuiYesNo(GuiScreen listener, String title, String description, String confirmText, String abortText, int id) {
        this.parentScreen = listener;
        this.message1 = title;
        this.message2 = description;
        this.confirmText = confirmText;
        this.abortText = abortText;
        this.worldNumber = id;
    }

    public void initGui() {
        this.controlList.add(new GuiSmallButton(0, this.width / 2 - 155 + 0, this.height / 6 + 96, this.confirmText));
        this.controlList.add(new GuiSmallButton(1, this.width / 2 - 155 + 160, this.height / 6 + 96, this.abortText));
    }

    protected void actionPerformed(GuiButton button) {
        this.parentScreen.deleteWorld(button.id == 0, this.worldNumber);
    }

    public void drawScreen(int mouseX, int mouseY, float tickDelta) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRenderer, this.message1, this.width / 2, 70, 0xFFFFFF);
        this.drawCenteredString(this.fontRenderer, this.message2, this.width / 2, 90, 0xFFFFFF);
        super.drawScreen(mouseX, mouseY, tickDelta);
    }
}

