package net.minecraft.src;


public class GuiTextField
extends Gui {
    private final FontRenderer fontRenderer;
    private final int xPos;
    private final int yPos;
    private final int width;
    private final int height;
    private String text;
    private int maxStringLength;
    private int cursorCounter;
    public boolean isFocused = false;
    public boolean isEnabled = true;
    private GuiScreen parentGuiScreen;

    public GuiTextField(GuiScreen screen, FontRenderer textRenderer, int x, int y, int width, int height, String message) {
        this.parentGuiScreen = screen;
        this.fontRenderer = textRenderer;
        this.xPos = x;
        this.yPos = y;
        this.width = width;
        this.height = height;
        this.setText(message);
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    public void updateCursorCounter() {
        ++this.cursorCounter;
    }

    public void textboxKeyTyped(final char chr, final int key) {
        if (!this.isEnabled || !this.isFocused) {
            return;
        }
        if (chr == '\t') {
            this.parentGuiScreen.selectNextField();
        }
        if (chr == '\u0016') {
            String clipboardString = GuiScreen.getClipboardString();
            if (clipboardString == null) {
                clipboardString = "";
            }
            int length = 32 - this.text.length();
            if (length > clipboardString.length()) {
                length = clipboardString.length();
            }
            if (length > 0) {
                this.text += clipboardString.substring(0, length);
            }
        }
        if (key == 14 && this.text.length() > 0) {
            this.text = this.text.substring(0, this.text.length() - 1);
        }
        if (ChatAllowedCharacters.allowedCharacters.indexOf(chr) >= 0 && (this.text.length() < this.maxStringLength || this.maxStringLength == 0)) {
            this.text += chr;
        }
    }

    public void mouseClicked(int mouseX, int mouseY, int button) {
        boolean bl = this.isEnabled && mouseX >= this.xPos && mouseX < this.xPos + this.width && mouseY >= this.yPos && mouseY < this.yPos + this.height;
        this.setFocused(bl);
    }

    public void setFocused(boolean focused) {
        if (focused && !this.isFocused) {
            this.cursorCounter = 0;
        }
        this.isFocused = focused;
    }

    public void drawTextBox() {
        this.drawRect(this.xPos - 1, this.yPos - 1, this.xPos + this.width + 1, this.yPos + this.height + 1, -6250336);
        this.drawRect(this.xPos, this.yPos, this.xPos + this.width, this.yPos + this.height, -16777216);
        if (this.isEnabled) {
            boolean bl = this.isFocused && this.cursorCounter / 6 % 2 == 0;
            this.drawString(this.fontRenderer, this.text + (bl ? "_" : ""), this.xPos + 4, this.yPos + (this.height - 8) / 2, 0xE0E0E0);
        } else {
            this.drawString(this.fontRenderer, this.text, this.xPos + 4, this.yPos + (this.height - 8) / 2, 0x707070);
        }
    }

    public void setMaxStringLength(int maximumLength) {
        this.maxStringLength = maximumLength;
    }
}

