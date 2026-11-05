package net.minecraft.src;


public class GuiDownloadTerrain
extends GuiScreen {
    private NetClientHandler netHandler;
    private int updateCounter = 0;

    public GuiDownloadTerrain(NetClientHandler networkHandler) {
        this.netHandler = networkHandler;
    }

    protected void keyTyped(char chr, int key) {
    }

    public void initGui() {
        this.controlList.clear();
    }

    public void updateScreen() {
        ++this.updateCounter;
        if (this.updateCounter % 20 == 0) {
            this.netHandler.addToSendQueue(new Packet0KeepAlive());
        }
        if (this.netHandler != null) {
            this.netHandler.processReadPackets();
        }
    }

    protected void actionPerformed(GuiButton button) {
    }

    public void drawScreen(int mouseX, int mouseY, float tickDelta) {
        this.drawBackground(0);
        StringTranslate stringTranslate = StringTranslate.getInstance();
        this.drawCenteredString(this.fontRenderer, stringTranslate.translateKey("multiplayer.downloadingTerrain"), this.width / 2, this.height / 2 - 50, 0xFFFFFF);
        super.drawScreen(mouseX, mouseY, tickDelta);
    }
}

