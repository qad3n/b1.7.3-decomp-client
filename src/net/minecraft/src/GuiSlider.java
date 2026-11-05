package net.minecraft.src;

import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

public class GuiSlider
extends GuiButton {
    public float sliderValue = 1.0f;
    public boolean dragging = false;
    private GameSettings.EnumOptions idFloat = null;

    public GuiSlider(int id, int x, int y, GameSettings.EnumOptions option, String message, float value) {
        super(id, x, y, 150, 20, message);
        this.idFloat = option;
        this.sliderValue = value;
    }

    protected int getHoverState(boolean hovered) {
        return 0;
    }

    protected void mouseDragged(Minecraft minecraft, int mouseX, int mouseY) {
        if (!this.enabled2) {
            return;
        }
        if (this.dragging) {
            this.sliderValue = (float)(mouseX - (this.xPosition + 4)) / (float)(this.width - 8);
            if (this.sliderValue < 0.0f) {
                this.sliderValue = 0.0f;
            }
            if (this.sliderValue > 1.0f) {
                this.sliderValue = 1.0f;
            }
            minecraft.gameSettings.setOptionFloatValue(this.idFloat, this.sliderValue);
            this.displayString = minecraft.gameSettings.getKeyBinding(this.idFloat);
        }
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.drawTexturedModalRect(this.xPosition + (int)(this.sliderValue * (float)(this.width - 8)), this.yPosition, 0, 66, 4, 20);
        this.drawTexturedModalRect(this.xPosition + (int)(this.sliderValue * (float)(this.width - 8)) + 4, this.yPosition, 196, 66, 4, 20);
    }

    public boolean mousePressed(Minecraft minecraft, int mouseX, int mouseY) {
        if (super.mousePressed(minecraft, mouseX, mouseY)) {
            this.sliderValue = (float)(mouseX - (this.xPosition + 4)) / (float)(this.width - 8);
            if (this.sliderValue < 0.0f) {
                this.sliderValue = 0.0f;
            }
            if (this.sliderValue > 1.0f) {
                this.sliderValue = 1.0f;
            }
            minecraft.gameSettings.setOptionFloatValue(this.idFloat, this.sliderValue);
            this.displayString = minecraft.gameSettings.getKeyBinding(this.idFloat);
            this.dragging = true;
            return true;
        }
        return false;
    }

    public void mouseReleased(int mouseX, int mouseY) {
        this.dragging = false;
    }
}

