package net.minecraft.src;


public class GuiUnused
extends GuiScreen {
    private String message1;
    private String message2;

    public void initGui() {
    }

    public void drawScreen(int mouseX, int mouseY, float tickDelta) {
        this.drawGradientRect(0, 0, this.width, this.height, -12574688, -11530224);
        this.drawCenteredString(this.fontRenderer, this.message1, this.width / 2, 90, 0xFFFFFF);
        this.drawCenteredString(this.fontRenderer, this.message2, this.width / 2, 110, 0xFFFFFF);
        super.drawScreen(mouseX, mouseY, tickDelta);
    }

    protected void keyTyped(char chr, int key) {
    }
}

