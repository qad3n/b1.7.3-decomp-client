package net.minecraft.src;

import java.awt.Component;
import java.nio.IntBuffer;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Cursor;
import org.lwjgl.input.Mouse;

public class MouseHelper {
    private Component component;
    private Cursor cursor;
    public int deltaX;
    public int deltaY;
    private int field_1115_e = 10;

    public MouseHelper(Component component) {
        this.component = component;
        IntBuffer intBuffer = GLAllocation.createDirectIntBuffer(1);
        intBuffer.put(0);
        intBuffer.flip();
        IntBuffer intBuffer2 = GLAllocation.createDirectIntBuffer(1024);
        try {
            this.cursor = new Cursor(32, 32, 16, 16, 1, intBuffer2, intBuffer);
        }
        catch (LWJGLException lWJGLException) {
            lWJGLException.printStackTrace();
        }
    }

    public void grabMouseCursor() {
        Mouse.setGrabbed(true);
        this.deltaX = 0;
        this.deltaY = 0;
    }

    public void ungrabMouseCursor() {
        Mouse.setCursorPosition(this.component.getWidth() / 2, this.component.getHeight() / 2);
        Mouse.setGrabbed(false);
    }

    public void mouseXYChange() {
        this.deltaX = Mouse.getDX();
        this.deltaY = Mouse.getDY();
    }
}

