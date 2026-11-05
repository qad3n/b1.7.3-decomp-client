package net.minecraft.src;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class GuiScreen
extends Gui {
    protected Minecraft mc;
    public int width;
    public int height;
    protected List<GuiButton> controlList = new ArrayList<GuiButton>();
    public boolean passEvents = false;
    protected FontRenderer fontRenderer;
    public GuiParticle particlesGui;
    private GuiButton selectedButton = null;

    public void drawScreen(int mouseX, int mouseY, float tickDelta) {
        for (int i = 0; i < this.controlList.size(); ++i) {
            GuiButton guiButton = this.controlList.get(i);
            guiButton.drawButton(this.mc, mouseX, mouseY);
        }
    }

    protected void keyTyped(char chr, int key) {
        if (key == 1) {
            this.mc.displayGuiScreen(null);
            this.mc.setIngameFocus();
        }
    }

    public static String getClipboardString() {
        try {
            Transferable transferable = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
            if (transferable != null && transferable.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                String string = (String)transferable.getTransferData(DataFlavor.stringFlavor);
                return string;
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
        return null;
    }

    protected void mouseClicked(int mouseX, int mouseY, int button) {
        if (button == 0) {
            for (int i = 0; i < this.controlList.size(); i++) {
                GuiButton guibutton = this.controlList.get(i);
                if (guibutton.mousePressed(this.mc, mouseX, mouseY)) {
                    this.selectedButton = guibutton;
                    this.mc.sndManager.playSoundFX("random.click", 1.0F, 1.0F);
                    this.actionPerformed(guibutton);
                }
            }
        }
    }

    protected void mouseMovedOrUp(int mouseX, int mouseY, int button) {
        if (this.selectedButton != null && button == 0) {
            this.selectedButton.mouseReleased(mouseX, mouseY);
            this.selectedButton = null;
        }
    }

    protected void actionPerformed(GuiButton button) {
    }

    public void setWorldAndResolution(Minecraft minecraft, int width, int height) {
        this.particlesGui = new GuiParticle(minecraft);
        this.mc = minecraft;
        this.fontRenderer = minecraft.fontRenderer;
        this.width = width;
        this.height = height;
        this.controlList.clear();
        this.initGui();
    }

    public void initGui() {
    }

    public void handleInput() {
        while (Mouse.next()) {
            this.handleMouseInput();
        }
        while (Keyboard.next()) {
            this.handleKeyboardInput();
        }
    }

    public void handleMouseInput() {
        if (Mouse.getEventButtonState()) {
            int n = Mouse.getEventX() * this.width / this.mc.displayWidth;
            int n2 = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
            this.mouseClicked(n, n2, Mouse.getEventButton());
        } else {
            int n = Mouse.getEventX() * this.width / this.mc.displayWidth;
            int n3 = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
            this.mouseMovedOrUp(n, n3, Mouse.getEventButton());
        }
    }

    public void handleKeyboardInput() {
        if (Keyboard.getEventKeyState()) {
            if (Keyboard.getEventKey() == 87) {
                this.mc.toggleFullscreen();
                return;
            }
            this.keyTyped(Keyboard.getEventCharacter(), Keyboard.getEventKey());
        }
    }

    public void updateScreen() {
    }

    public void onGuiClosed() {
    }

    public void drawDefaultBackground() {
        this.drawWorldBackground(0);
    }

    public void drawWorldBackground(int offset) {
        if (this.mc.theWorld != null) {
            this.drawGradientRect(0, 0, this.width, this.height, -1072689136, -804253680);
        } else {
            this.drawBackground(offset);
        }
    }

    public void drawBackground(int offset) {
        GL11.glDisable(2896);
        GL11.glDisable(2912);
        Tessellator tessellator = Tessellator.instance;
        GL11.glBindTexture(3553, this.mc.renderEngine.getTexture("/gui/background.png"));
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        float f = 32.0f;
        tessellator.startDrawingQuads();
        tessellator.setColorOpaque_I(0x404040);
        tessellator.addVertexWithUV(0.0, this.height, 0.0, 0.0, (float)this.height / f + (float)offset);
        tessellator.addVertexWithUV(this.width, this.height, 0.0, (float)this.width / f, (float)this.height / f + (float)offset);
        tessellator.addVertexWithUV(this.width, 0.0, 0.0, (float)this.width / f, 0 + offset);
        tessellator.addVertexWithUV(0.0, 0.0, 0.0, 0.0, 0 + offset);
        tessellator.draw();
    }

    public boolean doesGuiPauseGame() {
        return true;
    }

    public void deleteWorld(boolean result, int id) {
    }

    public void selectNextField() {
    }
}

