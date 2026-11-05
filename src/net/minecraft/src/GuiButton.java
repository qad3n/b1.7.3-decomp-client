package net.minecraft.src;

import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

public class GuiButton
extends Gui {
    protected int width = 200;
    protected int height = 20;
    public int xPosition;
    public int yPosition;
    public String displayString;
    public int id;
    public boolean enabled = true;
    public boolean enabled2 = true;

    public GuiButton(int id, int x, int y, String message) {
        this(id, x, y, 200, 20, message);
    }

    public GuiButton(int id, int x, int y, int width, int height, String message) {
        this.id = id;
        this.xPosition = x;
        this.yPosition = y;
        this.width = width;
        this.height = height;
        this.displayString = message;
    }

    protected int getHoverState(boolean hovered) {
        int n = 1;
        if (!this.enabled) {
            n = 0;
        } else if (hovered) {
            n = 2;
        }
        return n;
    }

    public void drawButton(Minecraft minecraft, int mouseX, int mouseY) {
        if (!this.enabled2) {
            return;
        }
        FontRenderer fontRenderer = minecraft.fontRenderer;
        GL11.glBindTexture(3553, minecraft.renderEngine.getTexture("/gui/gui.png"));
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        boolean bl = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
        int n = this.getHoverState(bl);
        this.drawTexturedModalRect(this.xPosition, this.yPosition, 0, 46 + n * 20, this.width / 2, this.height);
        this.drawTexturedModalRect(this.xPosition + this.width / 2, this.yPosition, 200 - this.width / 2, 46 + n * 20, this.width / 2, this.height);
        this.mouseDragged(minecraft, mouseX, mouseY);
        if (!this.enabled) {
            this.drawCenteredString(fontRenderer, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, -6250336);
        } else if (bl) {
            this.drawCenteredString(fontRenderer, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, 0xFFFFA0);
        } else {
            this.drawCenteredString(fontRenderer, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, 0xE0E0E0);
        }
    }

    protected void mouseDragged(Minecraft minecraft, int mouseX, int mouseY) {
    }

    public void mouseReleased(int mouseX, int mouseY) {
    }

    public boolean mousePressed(Minecraft minecraft, int mouseX, int mouseY) {
        return this.enabled && mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
    }
}

