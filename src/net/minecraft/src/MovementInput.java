package net.minecraft.src;


public class MovementInput {
    public float moveStrafe = 0.0f;
    public float moveForward = 0.0f;
    public boolean wasJumping = false;
    public boolean jump = false;
    public boolean sneak = false;

    public void updatePlayerMoveState(EntityPlayer player) {
    }

    public void resetKeyState() {
    }

    public void checkKeyForMovementInput(int key, boolean state) {
    }
}

