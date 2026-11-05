package net.minecraft.src;

import org.lwjgl.opengl.GL11;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class RenderFallingSand
extends Render<EntityFallingSand> {
    private RenderBlocks blockRenderer = new RenderBlocks();

    public RenderFallingSand() {
        this.shadowSize = 0.5f;
    }

    @Override
    public void doRender(EntityFallingSand entityFallingSand, double d, double d2, double d3, float f, float f2) {
        GL11.glPushMatrix();
        GL11.glTranslatef((float)d, (float)d2, (float)d3);
        this.loadTexture("/terrain.png");
        Block block = Block.blocksList[entityFallingSand.blockID];
        World world = entityFallingSand.getWorld();
        GL11.glDisable(2896);
        this.blockRenderer.renderBlockFallingSand(block, world, MathHelper.floor_double(entityFallingSand.posX), MathHelper.floor_double(entityFallingSand.posY), MathHelper.floor_double(entityFallingSand.posZ));
        GL11.glEnable(2896);
        GL11.glPopMatrix();
    }
}

