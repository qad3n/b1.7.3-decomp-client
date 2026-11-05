package net.minecraft.src;


/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public abstract class TileEntitySpecialRenderer<T extends TileEntity> {
    protected TileEntityRenderer tileEntityRenderer;

    public abstract void renderTileEntityAt(T var1, double var2, double var4, double var6, float var8);

    protected void bindTextureByName(String path) {
        RenderEngine renderEngine = this.tileEntityRenderer.renderEngine;
        renderEngine.bindTexture(renderEngine.getTexture(path));
    }

    public void setTileEntityRenderer(TileEntityRenderer dispatcher) {
        this.tileEntityRenderer = dispatcher;
    }

    public void setWorld(World world) {
    }

    public FontRenderer getFontRenderer() {
        return this.tileEntityRenderer.getFontRenderer();
    }
}

