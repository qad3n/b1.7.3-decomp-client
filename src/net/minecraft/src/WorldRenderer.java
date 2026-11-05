package net.minecraft.src;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.lwjgl.opengl.GL11;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class WorldRenderer {
    public World worldObj;
    private int glRenderList = -1;
    private static Tessellator tessellator = Tessellator.instance;
    public static int chunksUpdated = 0;
    public int posX;
    public int posY;
    public int posZ;
    public int sizeWidth;
    public int sizeHeight;
    public int sizeDepth;
    public int posXMinus;
    public int posYMinus;
    public int posZMinus;
    public int posXClip;
    public int posYClip;
    public int posZClip;
    public boolean isInFrustum = false;
    public boolean[] skipRenderPass = new boolean[2];
    public int posXPlus;
    public int posYPlus;
    public int posZPlus;
    public float rendererRadius;
    public boolean needsUpdate;
    public AxisAlignedBB rendererBoundingBox;
    public int chunkIndex;
    public boolean isVisible = true;
    public boolean isWaitingOnOcclusionQuery;
    public int glOcclusionQuery;
    public boolean isChunkLit;
    private boolean isInitialized = false;
    public List<TileEntity> tileEntityRenderers = new ArrayList<TileEntity>();
    private List<TileEntity> tileEntities;

    public WorldRenderer(World world, List<TileEntity> blockEntitiesToBeRendered, int originX, int originY, int originZ, int size, int glList) {
        this.worldObj = world;
        this.tileEntities = blockEntitiesToBeRendered;
        this.sizeWidth = this.sizeHeight = this.sizeDepth = size;
        this.rendererRadius = MathHelper.sqrt_float(this.sizeWidth * this.sizeWidth + this.sizeHeight * this.sizeHeight + this.sizeDepth * this.sizeDepth) / 2.0F;
        this.glRenderList = glList;
        this.posX = -999;
        this.setPosition(originX, originY, originZ);
        this.needsUpdate = false;
    }

    public void setPosition(int x, int y, int z) {
        if (x == this.posX && y == this.posY && z == this.posZ) {
            return;
        }
        this.setDontDraw();
        this.posX = x;
        this.posY = y;
        this.posZ = z;
        this.posXPlus = x + this.sizeWidth / 2;
        this.posYPlus = y + this.sizeHeight / 2;
        this.posZPlus = z + this.sizeDepth / 2;
        this.posXClip = x & 0x3FF;
        this.posYClip = y;
        this.posZClip = z & 0x3FF;
        this.posXMinus = x - this.posXClip;
        this.posYMinus = y - this.posYClip;
        this.posZMinus = z - this.posZClip;
        float f = 6.0f;
        this.rendererBoundingBox = AxisAlignedBB.getBoundingBox((float)x - f, (float)y - f, (float)z - f, (float)(x + this.sizeWidth) + f, (float)(y + this.sizeHeight) + f, (float)(z + this.sizeDepth) + f);
        GL11.glNewList(this.glRenderList + 2, 4864);
        RenderItem.renderAABB(AxisAlignedBB.getBoundingBoxFromPool((float)this.posXClip - f, (float)this.posYClip - f, (float)this.posZClip - f, (float)(this.posXClip + this.sizeWidth) + f, (float)(this.posYClip + this.sizeHeight) + f, (float)(this.posZClip + this.sizeDepth) + f));
        GL11.glEndList();
        this.markDirty();
    }

    private void setupGLTranslation() {
        GL11.glTranslatef(this.posXClip, this.posYClip, this.posZClip);
    }

    public void updateRenderer() {
        if (!this.needsUpdate) {
            return;
        }
        ++chunksUpdated;
        int n = this.posX;
        int n2 = this.posY;
        int n3 = this.posZ;
        int n4 = this.posX + this.sizeWidth;
        int n5 = this.posY + this.sizeHeight;
        int n6 = this.posZ + this.sizeDepth;
        for (int i = 0; i < 2; ++i) {
            this.skipRenderPass[i] = true;
        }
        Chunk.isLit = false;
        Set<TileEntity> hashSet = new HashSet<TileEntity>();
        hashSet.addAll(this.tileEntityRenderers);
        this.tileEntityRenderers.clear();
        int n7 = 1;
        IBlockAccess chunkCache = new ChunkCache(this.worldObj, n - n7, n2 - n7, n3 - n7, n4 + n7, n5 + n7, n6 + n7);
        RenderBlocks renderBlocks = new RenderBlocks(chunkCache);
        for (int i = 0; i < 2; ++i) {
            boolean bl = false;
            boolean bl2 = false;
            boolean bl3 = false;
            for (int j = n2; j < n5; ++j) {
                for (int k = n3; k < n6; ++k) {
                    for (int i2 = n; i2 < n4; ++i2) {
                        Block block;
                        int n8;
                        TileEntity tileEntity;
                        int n9 = chunkCache.getBlockId(i2, j, k);
                        if (n9 <= 0) continue;
                        if (!bl3) {
                            bl3 = true;
                            GL11.glNewList(this.glRenderList + i, 4864);
                            GL11.glPushMatrix();
                            this.setupGLTranslation();
                            float f = 1.000001f;
                            GL11.glTranslatef((float)(-this.sizeDepth) / 2.0f, (float)(-this.sizeHeight) / 2.0f, (float)(-this.sizeDepth) / 2.0f);
                            GL11.glScalef(f, f, f);
                            GL11.glTranslatef((float)this.sizeDepth / 2.0f, (float)this.sizeHeight / 2.0f, (float)this.sizeDepth / 2.0f);
                            tessellator.startDrawingQuads();
                            tessellator.setTranslationD(-this.posX, -this.posY, -this.posZ);
                        }
                        if (i == 0 && Block.isBlockContainer[n9]) {
                            tileEntity = chunkCache.getBlockTileEntity(i2, j, k);
                            if (TileEntityRenderer.instance.hasSpecialRenderer(tileEntity)) {
                                this.tileEntityRenderers.add(tileEntity);
                            }
                        }
                        block = Block.blocksList[n9];
                        n8 = block.getRenderBlockPass();
                        if (n8 != i) {
                            bl = true;
                            continue;
                        }
                        if (n8 != i) continue;
                        bl2 |= renderBlocks.renderBlockByRenderType(block, i2, j, k);
                    }
                }
            }
            if (bl3) {
                tessellator.draw();
                GL11.glPopMatrix();
                GL11.glEndList();
                tessellator.setTranslationD(0.0, 0.0, 0.0);
            } else {
                bl2 = false;
            }
            if (bl2) {
                this.skipRenderPass[i] = false;
            }
            if (!bl) break;
        }
        Set<TileEntity> hashSet2 = new HashSet<TileEntity>();
        hashSet2.addAll(this.tileEntityRenderers);
        hashSet2.removeAll(hashSet);
        this.tileEntities.addAll(hashSet2);
        hashSet.removeAll(this.tileEntityRenderers);
        this.tileEntities.removeAll(hashSet);
        this.isChunkLit = Chunk.isLit;
        this.isInitialized = true;
    }

    public float distanceToEntitySquared(Entity camera) {
        float f = (float)(camera.posX - (double)this.posXPlus);
        float f2 = (float)(camera.posY - (double)this.posYPlus);
        float f3 = (float)(camera.posZ - (double)this.posZPlus);
        return f * f + f2 * f2 + f3 * f3;
    }

    public void setDontDraw() {
        for (int i = 0; i < 2; ++i) {
            this.skipRenderPass[i] = true;
        }
        this.isInFrustum = false;
        this.isInitialized = false;
    }

    public void delete() {
        this.setDontDraw();
        this.worldObj = null;
    }

    public int getGLCallListForPass(int layer) {
        if (!this.isInFrustum) {
            return -1;
        }
        if (!this.skipRenderPass[layer]) {
            return this.glRenderList + layer;
        }
        return -1;
    }

    public void updateInFrustrum(ICamera culler) {
        this.isInFrustum = culler.isBoundingBoxInFrustum(this.rendererBoundingBox);
    }

    public void callOcclusionQueryList() {
        GL11.glCallList(this.glRenderList + 2);
    }

    public boolean skipAllRenderPasses() {
        if (!this.isInitialized) {
            return false;
        }
        return this.skipRenderPass[0] && this.skipRenderPass[1];
    }

    public void markDirty() {
        this.needsUpdate = true;
    }
}

