package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class TextureFX {
    public byte[] imageData = new byte[1024];
    public int iconIndex;
    public boolean anaglyphEnabled = false;
    public int textureId = 0;
    public int tileSize = 1;
    public int tileImage = 0;

    public TextureFX(int sprite) {
        this.iconIndex = sprite;
    }

    public void onTick() {
    }

    public void bindImage(RenderEngine textureManager) {
        if (this.tileImage == 0) {
            GL11.glBindTexture(3553, textureManager.getTexture("/terrain.png"));
        } else if (this.tileImage == 1) {
            GL11.glBindTexture(3553, textureManager.getTexture("/gui/items.png"));
        }
    }
}

