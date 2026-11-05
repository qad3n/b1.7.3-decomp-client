package net.minecraft.src;


public class GuiSmallButton
extends GuiButton {
    private final GameSettings.EnumOptions enumOptions;

    public GuiSmallButton(int n, int n2, int n3, String string) {
        this(n, n2, n3, null, string);
    }

    public GuiSmallButton(int n, int n2, int n3, int n4, int n5, String string) {
        super(n, n2, n3, n4, n5, string);
        this.enumOptions = null;
    }

    public GuiSmallButton(int id, int x, int y, GameSettings.EnumOptions option, String message) {
        super(id, x, y, 150, 20, message);
        this.enumOptions = option;
    }

    public GameSettings.EnumOptions returnEnumOptions() {
        return this.enumOptions;
    }
}

