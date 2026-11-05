package net.minecraft.src;

import java.net.ConnectException;
import java.net.UnknownHostException;
import net.minecraft.client.Minecraft;

public class GuiConnecting
extends GuiScreen {
    private NetClientHandler clientHandler;
    private boolean cancelled = false;

    public GuiConnecting(final Minecraft minecraft, final String address, final int port) {
        System.out.println("Connecting to " + address + ", " + port);
        minecraft.changeWorld1(null);
        new Thread(){

            public void run() {
                try {
                    GuiConnecting.this.clientHandler = new NetClientHandler(minecraft, address, port);
                    if (GuiConnecting.this.cancelled) {
                        return;
                    }
                    GuiConnecting.this.clientHandler.addToSendQueue(new Packet2Handshake(minecraft.session.username));
                }
                catch (UnknownHostException unknownHostException) {
                    if (GuiConnecting.this.cancelled) {
                        return;
                    }
                    minecraft.displayGuiScreen(new GuiConnectFailed("connect.failed", "disconnect.genericReason", "Unknown host '" + address + "'"));
                }
                catch (ConnectException connectException) {
                    if (GuiConnecting.this.cancelled) {
                        return;
                    }
                    minecraft.displayGuiScreen(new GuiConnectFailed("connect.failed", "disconnect.genericReason", connectException.getMessage()));
                }
                catch (Exception exception) {
                    if (GuiConnecting.this.cancelled) {
                        return;
                    }
                    exception.printStackTrace();
                    minecraft.displayGuiScreen(new GuiConnectFailed("connect.failed", "disconnect.genericReason", exception.toString()));
                }
            }
        }.start();
    }

    public void updateScreen() {
        if (this.clientHandler != null) {
            this.clientHandler.processReadPackets();
        }
    }

    protected void keyTyped(char chr, int key) {
    }

    public void initGui() {
        StringTranslate stringTranslate = StringTranslate.getInstance();
        this.controlList.clear();
        this.controlList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 120 + 12, stringTranslate.translateKey("gui.cancel")));
    }

    protected void actionPerformed(GuiButton button) {
        if (button.id == 0) {
            this.cancelled = true;
            if (this.clientHandler != null) {
                this.clientHandler.disconnect();
            }
            this.mc.displayGuiScreen(new GuiMainMenu());
        }
    }

    public void drawScreen(int mouseX, int mouseY, float tickDelta) {
        this.drawDefaultBackground();
        StringTranslate stringTranslate = StringTranslate.getInstance();
        if (this.clientHandler == null) {
            this.drawCenteredString(this.fontRenderer, stringTranslate.translateKey("connect.connecting"), this.width / 2, this.height / 2 - 50, 0xFFFFFF);
            this.drawCenteredString(this.fontRenderer, "", this.width / 2, this.height / 2 - 10, 0xFFFFFF);
        } else {
            this.drawCenteredString(this.fontRenderer, stringTranslate.translateKey("connect.authorizing"), this.width / 2, this.height / 2 - 50, 0xFFFFFF);
            this.drawCenteredString(this.fontRenderer, this.clientHandler.message, this.width / 2, this.height / 2 - 10, 0xFFFFFF);
        }
        super.drawScreen(mouseX, mouseY, tickDelta);
    }
}

