package net.minecraft.src;

import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class TileEntityRendererPiston
extends TileEntitySpecialRenderer<TileEntityPiston> {
    private RenderBlocks blockRenderer;

    @Override
    public void renderTileEntityAt(TileEntityPiston tileEntityPiston, double d, double d2, double d3, float f) {
        Block block = Block.blocksList[tileEntityPiston.getStoredBlockID()];
        if (block != null && tileEntityPiston.getProgress(f) < 1.0f) {
            Tessellator tessellator = Tessellator.instance;
            this.bindTextureByName("/terrain.png");
            RenderHelper.disableStandardItemLighting();
            GL11.glBlendFunc(770, 771);
            GL11.glEnable(3042);
            GL11.glDisable(2884);
            if (Minecraft.isAmbientOcclusionEnabled()) {
                GL11.glShadeModel(7425);
            } else {
                GL11.glShadeModel(7424);
            }
            tessellator.startDrawingQuads();
            tessellator.setTranslationD((float)d - (float)tileEntityPiston.xCoord + tileEntityPiston.getRenderOffsetX(f), (float)d2 - (float)tileEntityPiston.yCoord + tileEntityPiston.getRenderOffsetY(f), (float)d3 - (float)tileEntityPiston.zCoord + tileEntityPiston.getRenderOffsetZ(f));
            tessellator.setColorOpaque(1, 1, 1);
            if (block == Block.pistonExtension && tileEntityPiston.getProgress(f) < 0.5f) {
                this.blockRenderer.renderPistonHead(block, tileEntityPiston.xCoord, tileEntityPiston.yCoord, tileEntityPiston.zCoord, false);
            } else if (tileEntityPiston.isSource() && !tileEntityPiston.isExtending()) {
                Block.pistonExtension.setForcedSprite(((BlockPistonBase)block).getTopSprite());
                this.blockRenderer.renderPistonHead(Block.pistonExtension, tileEntityPiston.xCoord, tileEntityPiston.yCoord, tileEntityPiston.zCoord, tileEntityPiston.getProgress(f) < 0.5f);
                Block.pistonExtension.clearForcedSprite();
                tessellator.setTranslationD((float)d - (float)tileEntityPiston.xCoord, (float)d2 - (float)tileEntityPiston.yCoord, (float)d3 - (float)tileEntityPiston.zCoord);
                this.blockRenderer.renderExtendedPistonBase(block, tileEntityPiston.xCoord, tileEntityPiston.yCoord, tileEntityPiston.zCoord);
            } else {
                this.blockRenderer.renderMovingBlock(block, tileEntityPiston.xCoord, tileEntityPiston.yCoord, tileEntityPiston.zCoord);
            }
            tessellator.setTranslationD(0.0, 0.0, 0.0);
            tessellator.draw();
            RenderHelper.enableStandardItemLighting();
        }
    }

    @Override
    public void setWorld(World world) {
        this.blockRenderer = new RenderBlocks(world);
    }
}

