package net.minecraft.src;


public class MovementInputFromOptions
extends MovementInput {
    private boolean[] movementKeyStates = new boolean[10];
    private GameSettings gameSettings;

    public MovementInputFromOptions(GameSettings options) {
        this.gameSettings = options;
    }

    public void checkKeyForMovementInput(int key, boolean state) {
        int n = -1;
        if (key == this.gameSettings.keyBindForward.keyCode) {
            n = 0;
        }
        if (key == this.gameSettings.keyBindBack.keyCode) {
            n = 1;
        }
        if (key == this.gameSettings.keyBindLeft.keyCode) {
            n = 2;
        }
        if (key == this.gameSettings.keyBindRight.keyCode) {
            n = 3;
        }
        if (key == this.gameSettings.keyBindJump.keyCode) {
            n = 4;
        }
        if (key == this.gameSettings.keyBindSneak.keyCode) {
            n = 5;
        }
        if (n >= 0) {
            this.movementKeyStates[n] = state;
        }
    }

    public void resetKeyState() {
        for (int i = 0; i < 10; ++i) {
            this.movementKeyStates[i] = false;
        }
    }

    public void updatePlayerMoveState(EntityPlayer player) {
        this.moveStrafe = 0.0f;
        this.moveForward = 0.0f;
        if (this.movementKeyStates[0]) {
            this.moveForward += 1.0f;
        }
        if (this.movementKeyStates[1]) {
            this.moveForward -= 1.0f;
        }
        if (this.movementKeyStates[2]) {
            this.moveStrafe += 1.0f;
        }
        if (this.movementKeyStates[3]) {
            this.moveStrafe -= 1.0f;
        }
        this.jump = this.movementKeyStates[4];
        this.sneak = this.movementKeyStates[5];
        if (this.sneak) {
            this.moveStrafe = (float)((double)this.moveStrafe * 0.3);
            this.moveForward = (float)((double)this.moveForward * 0.3);
        }
    }
}

