package net.minecraft.src;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class GuiEditSign
extends GuiScreen {
    protected String screenTitle = "Edit sign message:";
    private TileEntitySign entitySign;
    private int updateCounter;
    private int editLine = 0;
    private static final String allowedCharacters = ChatAllowedCharacters.allowedCharacters;

    public GuiEditSign(TileEntitySign sign) {
        this.entitySign = sign;
    }

    public void initGui() {
        this.controlList.clear();
        Keyboard.enableRepeatEvents(true);
        this.controlList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 120, "Done"));
    }

    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
        if (this.mc.theWorld.multiplayerWorld) {
            this.mc.getSendQueue().addToSendQueue(new Packet130UpdateSign(this.entitySign.xCoord, this.entitySign.yCoord, this.entitySign.zCoord, this.entitySign.signText));
        }
    }

    public void updateScreen() {
        ++this.updateCounter;
    }

    protected void actionPerformed(GuiButton button) {
        if (!button.enabled) {
            return;
        }
        if (button.id == 0) {
            this.entitySign.onInventoryChanged();
            this.mc.displayGuiScreen(null);
        }
    }

    protected void keyTyped(char chr, int key) {
        if (key == 200) {
            this.editLine = this.editLine - 1 & 3;
        }
        if (key == 208 || key == 28) {
            this.editLine = this.editLine + 1 & 3;
        }
        if (key == 14 && this.entitySign.signText[this.editLine].length() > 0) {
            this.entitySign.signText[this.editLine] = this.entitySign.signText[this.editLine].substring(0, this.entitySign.signText[this.editLine].length() - 1);
        }
        if (allowedCharacters.indexOf(chr) >= 0 && this.entitySign.signText[this.editLine].length() < 15) {
            this.entitySign.signText[this.editLine] += chr;
        }
    }

    public void drawScreen(int mouseX, int mouseY, float tickDelta) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRenderer, this.screenTitle, this.width / 2, 40, 0xFFFFFF);
        GL11.glPushMatrix();
        GL11.glTranslatef(this.width / 2, 0.0f, 50.0f);
        float f = 93.75f;
        GL11.glScalef(-f, -f, -f);
        GL11.glRotatef(180.0f, 0.0f, 1.0f, 0.0f);
        Block block = this.entitySign.getBlockType();
        if (block == Block.signPost) {
            float f2 = (float)(this.entitySign.getBlockMetadata() * 360) / 16.0f;
            GL11.glRotatef(f2, 0.0f, 1.0f, 0.0f);
            GL11.glTranslatef(0.0f, -1.0625f, 0.0f);
        } else {
            int n = this.entitySign.getBlockMetadata();
            float f3 = 0.0f;
            if (n == 2) {
                f3 = 180.0f;
            }
            if (n == 4) {
                f3 = 90.0f;
            }
            if (n == 5) {
                f3 = -90.0f;
            }
            GL11.glRotatef(f3, 0.0f, 1.0f, 0.0f);
            GL11.glTranslatef(0.0f, -1.0625f, 0.0f);
        }
        if (this.updateCounter / 6 % 2 == 0) {
            this.entitySign.lineBeingEdited = this.editLine;
        }
        TileEntityRenderer.instance.renderTileEntityAt(this.entitySign, -0.5, -0.75, -0.5, 0.0f);
        this.entitySign.lineBeingEdited = -1;
        GL11.glPopMatrix();
        super.drawScreen(mouseX, mouseY, tickDelta);
    }
}

