package net.minecraft.src;

import java.nio.Buffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.ARBOcclusionQuery;
import org.lwjgl.opengl.GL11;

public class RenderGlobal implements IWorldAccess {
    public List<TileEntity> tileEntities = new ArrayList<>();
    private World worldObj;
    private RenderEngine renderEngine;
    private List<WorldRenderer> worldRenderersToUpdate = new ArrayList<>();
    private WorldRenderer[] sortedWorldRenderers;
    private WorldRenderer[] worldRenderers;
    private int renderChunksWide;
    private int renderChunksTall;
    private int renderChunksDeep;
    private int glRenderListBase;
    private Minecraft mc;
    private RenderBlocks globalRenderBlocks;
    private IntBuffer glOcclusionQueryBase;
    private boolean occlusionEnabled = false;
    private int cloudOffsetX = 0;
    private int starGLCallList;
    private int glSkyList;
    private int glSkyList2;
    private int minBlockX;
    private int minBlockY;
    private int minBlockZ;
    private int maxBlockX;
    private int maxBlockY;
    private int maxBlockZ;
    private int renderDistance = -1;
    private int renderEntitiesStartupCounter = 2;
    private int countEntitiesTotal;
    private int countEntitiesRendered;
    private int countEntitiesHidden;
    int[] dummyBuf50k = new int[50000];
    IntBuffer occlusionResult = GLAllocation.createDirectIntBuffer(64);
    private int renderersLoaded;
    private int renderersBeingClipped;
    private int renderersBeingOccluded;
    private int renderersBeingRendered;
    private int renderersSkippingRenderPass;
    private int worldRenderersCheckIndex;
    private List<WorldRenderer> glRenderLists = new ArrayList<>();
    private RenderList[] allRenderLists = new RenderList[]{new RenderList(), new RenderList(), new RenderList(), new RenderList()};
    int dummyInt0 = 0;
    int glDummyList = GLAllocation.generateDisplayLists(1);
    double prevSortX = -9999.0;
    double prevSortY = -9999.0;
    double prevSortZ = -9999.0;
    public float damagePartialTime;
    int frustrumCheckOffset = 0;

    public RenderGlobal(Minecraft minecraft, RenderEngine textureManager) {
        int n;
        int n2;
        this.mc = minecraft;
        this.renderEngine = textureManager;
        int n3 = 64;
        this.glRenderListBase = GLAllocation.generateDisplayLists(n3 * n3 * n3 * 3);
        this.occlusionEnabled = minecraft.getOpenGlCapsChecker().checkARBOcclusion();
        if (this.occlusionEnabled) {
            this.occlusionResult.clear();
            this.glOcclusionQueryBase = GLAllocation.createDirectIntBuffer(n3 * n3 * n3);
            this.glOcclusionQueryBase.clear();
            this.glOcclusionQueryBase.position(0);
            this.glOcclusionQueryBase.limit(n3 * n3 * n3);
            ARBOcclusionQuery.glGenQueriesARB(this.glOcclusionQueryBase);
        }
        this.starGLCallList = GLAllocation.generateDisplayLists(3);
        GL11.glPushMatrix();
        GL11.glNewList(this.starGLCallList, 4864);
        this.renderStars();
        GL11.glEndList();
        GL11.glPopMatrix();
        Tessellator tessellator = Tessellator.instance;
        this.glSkyList = this.starGLCallList + 1;
        GL11.glNewList(this.glSkyList, 4864);
        int n4 = 64;
        int n5 = 256 / n4 + 2;
        float f = 16.0f;
        for (n2 = -n4 * n5; n2 <= n4 * n5; n2 += n4) {
            for (n = -n4 * n5; n <= n4 * n5; n += n4) {
                tessellator.startDrawingQuads();
                tessellator.addVertex(n2 + 0, f, n + 0);
                tessellator.addVertex(n2 + n4, f, n + 0);
                tessellator.addVertex(n2 + n4, f, n + n4);
                tessellator.addVertex(n2 + 0, f, n + n4);
                tessellator.draw();
            }
        }
        GL11.glEndList();
        this.glSkyList2 = this.starGLCallList + 2;
        GL11.glNewList(this.glSkyList2, 4864);
        f = -16.0f;
        tessellator.startDrawingQuads();
        for (n2 = -n4 * n5; n2 <= n4 * n5; n2 += n4) {
            for (n = -n4 * n5; n <= n4 * n5; n += n4) {
                tessellator.addVertex(n2 + n4, f, n + 0);
                tessellator.addVertex(n2 + 0, f, n + 0);
                tessellator.addVertex(n2 + 0, f, n + n4);
                tessellator.addVertex(n2 + n4, f, n + n4);
            }
        }
        tessellator.draw();
        GL11.glEndList();
    }

    private void renderStars() {
        Random random = new Random(10842L);
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();

        for (int i = 0; i < 1500; i++) {
            double d0 = random.nextFloat() * 2.0F - 1.0F;
            double d1 = random.nextFloat() * 2.0F - 1.0F;
            double d2 = random.nextFloat() * 2.0F - 1.0F;
            double d3 = 0.25F + random.nextFloat() * 0.25F;
            double d4 = d0 * d0 + d1 * d1 + d2 * d2;
            if (d4 < 1.0 && d4 > 0.01) {
                d4 = 1.0 / Math.sqrt(d4);
                d0 *= d4;
                d1 *= d4;
                d2 *= d4;
                double d5 = d0 * 100.0;
                double d6 = d1 * 100.0;
                double d7 = d2 * 100.0;
                double d8 = Math.atan2(d0, d2);
                double d9 = Math.sin(d8);
                double d10 = Math.cos(d8);
                double d11 = Math.atan2(Math.sqrt(d0 * d0 + d2 * d2), d1);
                double d12 = Math.sin(d11);
                double d13 = Math.cos(d11);
                double d14 = random.nextDouble() * Math.PI * 2.0;
                double d15 = Math.sin(d14);
                double d16 = Math.cos(d14);

                for (int j = 0; j < 4; j++) {
                    double d17 = 0.0;
                    double d18 = ((j & 2) - 1) * d3;
                    double d19 = ((j + 1 & 2) - 1) * d3;
                    double d20 = d17;
                    double d21 = d18 * d16 - d19 * d15;
                    double d22 = d19 * d16 + d18 * d15;
                    double d23 = d22;
                    double d24 = d21 * d12 + d20 * d13;
                    double d25 = d20 * d12 - d21 * d13;
                    double d26 = d25 * d9 - d23 * d10;
                    double d27 = d24;
                    double d28 = d23 * d9 + d25 * d10;
                    tessellator.addVertex(d5 + d26, d6 + d27, d7 + d28);
                }
            }
        }

        tessellator.draw();
    }

    public void changeWorld(World world) {
        if (this.worldObj != null) {
            this.worldObj.removeWorldAccess(this);
        }

        this.prevSortX = -9999.0;
        this.prevSortY = -9999.0;
        this.prevSortZ = -9999.0;
        RenderManager.instance.setWorld(world);
        this.worldObj = world;
        this.globalRenderBlocks = new RenderBlocks(world);
        if (world != null) {
            world.addWorldAccess(this);
            this.loadRenderers();
        }
    }

    public void loadRenderers() {
        Block.leaves.setGraphicsLevel(this.mc.gameSettings.fancyGraphics);
        this.renderDistance = this.mc.gameSettings.renderDistance;
        if (this.worldRenderers != null) {
            for (int i = 0; i < this.worldRenderers.length; i++) {
                this.worldRenderers[i].delete();
            }
        }

        int j1 = 64 << 3 - this.renderDistance;
        if (j1 > 400) {
            j1 = 400;
        }

        this.renderChunksWide = j1 / 16 + 1;
        this.renderChunksTall = 8;
        this.renderChunksDeep = j1 / 16 + 1;
        this.worldRenderers = new WorldRenderer[this.renderChunksWide * this.renderChunksTall * this.renderChunksDeep];
        this.sortedWorldRenderers = new WorldRenderer[this.renderChunksWide * this.renderChunksTall * this.renderChunksDeep];
        int b0 = 0;
        int j = 0;
        this.minBlockX = 0;
        this.minBlockY = 0;
        this.minBlockZ = 0;
        this.maxBlockX = this.renderChunksWide;
        this.maxBlockY = this.renderChunksTall;
        this.maxBlockZ = this.renderChunksDeep;

        for (int k = 0; k < this.worldRenderersToUpdate.size(); k++) {
            this.worldRenderersToUpdate.get(k).needsUpdate = false;
        }

        this.worldRenderersToUpdate.clear();
        this.tileEntities.clear();

        for (int k1 = 0; k1 < this.renderChunksWide; k1++) {
            for (int l = 0; l < this.renderChunksTall; l++) {
                for (int i1 = 0; i1 < this.renderChunksDeep; i1++) {
                    this.worldRenderers[(i1 * this.renderChunksTall + l) * this.renderChunksWide + k1] = new WorldRenderer(
                        this.worldObj, this.tileEntities, k1 * 16, l * 16, i1 * 16, 16, this.glRenderListBase + b0
                    );
                    if (this.occlusionEnabled) {
                        this.worldRenderers[(i1 * this.renderChunksTall + l) * this.renderChunksWide + k1].glOcclusionQuery = this.glOcclusionQueryBase.get(j);
                    }

                    this.worldRenderers[(i1 * this.renderChunksTall + l) * this.renderChunksWide + k1].isWaitingOnOcclusionQuery = false;
                    this.worldRenderers[(i1 * this.renderChunksTall + l) * this.renderChunksWide + k1].isVisible = true;
                    this.worldRenderers[(i1 * this.renderChunksTall + l) * this.renderChunksWide + k1].isInFrustum = true;
                    this.worldRenderers[(i1 * this.renderChunksTall + l) * this.renderChunksWide + k1].chunkIndex = j++;
                    this.worldRenderers[(i1 * this.renderChunksTall + l) * this.renderChunksWide + k1].markDirty();
                    this.sortedWorldRenderers[(i1 * this.renderChunksTall + l) * this.renderChunksWide + k1] = this.worldRenderers[(
                                i1 * this.renderChunksTall + l
                            )
                            * this.renderChunksWide
                        + k1];
                    this.worldRenderersToUpdate.add(this.worldRenderers[(i1 * this.renderChunksTall + l) * this.renderChunksWide + k1]);
                    b0 += 3;
                }
            }
        }

        if (this.worldObj != null) {
            EntityLiving entityliving = this.mc.renderViewEntity;
            if (entityliving != null) {
                this.markRenderersForNewPosition(
                    MathHelper.floor_double(entityliving.posX), MathHelper.floor_double(entityliving.posY), MathHelper.floor_double(entityliving.posZ)
                );
                Arrays.sort(this.sortedWorldRenderers, new EntitySorter(entityliving));
            }
        }

        this.renderEntitiesStartupCounter = 2;
    }

    public void renderEntities(Vec3D cameraPos, ICamera culler, float tickDelta) {
        if (this.renderEntitiesStartupCounter > 0) {
            this.renderEntitiesStartupCounter--;
        } else {
            TileEntityRenderer.instance.cacheActiveRenderInfo(this.worldObj, this.renderEngine, this.mc.fontRenderer, this.mc.renderViewEntity, tickDelta);
            RenderManager.instance
                .cacheActiveRenderInfo(this.worldObj, this.renderEngine, this.mc.fontRenderer, this.mc.renderViewEntity, this.mc.gameSettings, tickDelta);
            this.countEntitiesTotal = 0;
            this.countEntitiesRendered = 0;
            this.countEntitiesHidden = 0;
            EntityLiving entityliving = this.mc.renderViewEntity;
            RenderManager.renderPosX = entityliving.lastTickPosX + (entityliving.posX - entityliving.lastTickPosX) * tickDelta;
            RenderManager.renderPosY = entityliving.lastTickPosY + (entityliving.posY - entityliving.lastTickPosY) * tickDelta;
            RenderManager.renderPosZ = entityliving.lastTickPosZ + (entityliving.posZ - entityliving.lastTickPosZ) * tickDelta;
            TileEntityRenderer.staticPlayerX = entityliving.lastTickPosX + (entityliving.posX - entityliving.lastTickPosX) * tickDelta;
            TileEntityRenderer.staticPlayerY = entityliving.lastTickPosY + (entityliving.posY - entityliving.lastTickPosY) * tickDelta;
            TileEntityRenderer.staticPlayerZ = entityliving.lastTickPosZ + (entityliving.posZ - entityliving.lastTickPosZ) * tickDelta;
            List list = this.worldObj.getLoadedEntityList();
            this.countEntitiesTotal = list.size();

            for (int i = 0; i < this.worldObj.weatherEffects.size(); i++) {
                Entity entity = this.worldObj.weatherEffects.get(i);
                this.countEntitiesRendered++;
                if (entity.isInRangeToRenderVec3D(cameraPos)) {
                    RenderManager.instance.renderEntity(entity, tickDelta);
                }
            }

            for (int k = 0; k < list.size(); k++) {
                Entity entity1 = (Entity)list.get(k);
                if (entity1.isInRangeToRenderVec3D(cameraPos)
                    && (entity1.ignoreFrustumCheck || culler.isBoundingBoxInFrustum(entity1.boundingBox))
                    && (entity1 != this.mc.renderViewEntity || this.mc.gameSettings.thirdPersonView || this.mc.renderViewEntity.isPlayerSleeping())) {
                    int j = MathHelper.floor_double(entity1.posY);
                    if (j < 0) {
                        j = 0;
                    }

                    if (j >= 128) {
                        j = 127;
                    }

                    if (this.worldObj.blockExists(MathHelper.floor_double(entity1.posX), j, MathHelper.floor_double(entity1.posZ))) {
                        this.countEntitiesRendered++;
                        RenderManager.instance.renderEntity(entity1, tickDelta);
                    }
                }
            }

            for (int l = 0; l < this.tileEntities.size(); l++) {
                TileEntityRenderer.instance.renderTileEntity(this.tileEntities.get(l), tickDelta);
            }
        }
    }

    public String getDebugInfoRenders() {
        return "C: "
            + this.renderersBeingRendered
            + "/"
            + this.renderersLoaded
            + ". F: "
            + this.renderersBeingClipped
            + ", O: "
            + this.renderersBeingOccluded
            + ", E: "
            + this.renderersSkippingRenderPass;
    }

    public String getDebugInfoEntities() {
        return "E: "
            + this.countEntitiesRendered
            + "/"
            + this.countEntitiesTotal
            + ". B: "
            + this.countEntitiesHidden
            + ", I: "
            + (this.countEntitiesTotal - this.countEntitiesHidden - this.countEntitiesRendered);
    }

    private void markRenderersForNewPosition(int cameraX, int cameraY, int cameraZ) {
        cameraX -= 8;
        cameraY -= 8;
        cameraZ -= 8;
        this.minBlockX = Integer.MAX_VALUE;
        this.minBlockY = Integer.MAX_VALUE;
        this.minBlockZ = Integer.MAX_VALUE;
        this.maxBlockX = Integer.MIN_VALUE;
        this.maxBlockY = Integer.MIN_VALUE;
        this.maxBlockZ = Integer.MIN_VALUE;
        int i = this.renderChunksWide * 16;
        int j = i / 2;

        for (int k = 0; k < this.renderChunksWide; k++) {
            int l = k * 16;
            int i1 = l + j - cameraX;
            if (i1 < 0) {
                i1 -= i - 1;
            }

            i1 /= i;
            l -= i1 * i;
            if (l < this.minBlockX) {
                this.minBlockX = l;
            }

            if (l > this.maxBlockX) {
                this.maxBlockX = l;
            }

            for (int j1 = 0; j1 < this.renderChunksDeep; j1++) {
                int k1 = j1 * 16;
                int l1 = k1 + j - cameraZ;
                if (l1 < 0) {
                    l1 -= i - 1;
                }

                l1 /= i;
                k1 -= l1 * i;
                if (k1 < this.minBlockZ) {
                    this.minBlockZ = k1;
                }

                if (k1 > this.maxBlockZ) {
                    this.maxBlockZ = k1;
                }

                for (int i2 = 0; i2 < this.renderChunksTall; i2++) {
                    int j2 = i2 * 16;
                    if (j2 < this.minBlockY) {
                        this.minBlockY = j2;
                    }

                    if (j2 > this.maxBlockY) {
                        this.maxBlockY = j2;
                    }

                    WorldRenderer worldrenderer = this.worldRenderers[(j1 * this.renderChunksTall + i2) * this.renderChunksWide + k];
                    boolean flag = worldrenderer.needsUpdate;
                    worldrenderer.setPosition(l, j2, k1);
                    if (!flag && worldrenderer.needsUpdate) {
                        this.worldRenderersToUpdate.add(worldrenderer);
                    }
                }
            }
        }
    }

    public int sortAndRender(EntityLiving camera, int layer, double tickDelta) {
        for (int i = 0; i < 10; i++) {
            this.worldRenderersCheckIndex = (this.worldRenderersCheckIndex + 1) % this.worldRenderers.length;
            WorldRenderer worldrenderer = this.worldRenderers[this.worldRenderersCheckIndex];
            if (worldrenderer.needsUpdate && !this.worldRenderersToUpdate.contains(worldrenderer)) {
                this.worldRenderersToUpdate.add(worldrenderer);
            }
        }

        if (this.mc.gameSettings.renderDistance != this.renderDistance) {
            this.loadRenderers();
        }

        if (layer == 0) {
            this.renderersLoaded = 0;
            this.renderersBeingClipped = 0;
            this.renderersBeingOccluded = 0;
            this.renderersBeingRendered = 0;
            this.renderersSkippingRenderPass = 0;
        }

        double d5 = camera.lastTickPosX + (camera.posX - camera.lastTickPosX) * tickDelta;
        double d0 = camera.lastTickPosY + (camera.posY - camera.lastTickPosY) * tickDelta;
        double d1 = camera.lastTickPosZ + (camera.posZ - camera.lastTickPosZ) * tickDelta;
        double d2 = camera.posX - this.prevSortX;
        double d3 = camera.posY - this.prevSortY;
        double d4 = camera.posZ - this.prevSortZ;
        if (d2 * d2 + d3 * d3 + d4 * d4 > 16.0) {
            this.prevSortX = camera.posX;
            this.prevSortY = camera.posY;
            this.prevSortZ = camera.posZ;
            this.markRenderersForNewPosition(MathHelper.floor_double(camera.posX), MathHelper.floor_double(camera.posY), MathHelper.floor_double(camera.posZ));
            Arrays.sort(this.sortedWorldRenderers, new EntitySorter(camera));
        }

        RenderHelper.disableStandardItemLighting();
        int j = 0;
        if (this.occlusionEnabled && this.mc.gameSettings.advancedOpengl && !this.mc.gameSettings.anaglyph && layer == 0) {
            int k = 0;
            int l = 16;
            this.checkOcclusionQueryResult(k, l);

            for (int i1 = k; i1 < l; i1++) {
                this.sortedWorldRenderers[i1].isVisible = true;
            }

            j += this.renderSortedRenderers(k, l, layer, tickDelta);

            do {
                k = l;
                l *= 2;
                if (l > this.sortedWorldRenderers.length) {
                    l = this.sortedWorldRenderers.length;
                }

                GL11.glDisable(3553);
                GL11.glDisable(2896);
                GL11.glDisable(3008);
                GL11.glDisable(2912);
                GL11.glColorMask(false, false, false, false);
                GL11.glDepthMask(false);
                this.checkOcclusionQueryResult(k, l);
                GL11.glPushMatrix();
                float f9 = 0.0F;
                float f = 0.0F;
                float f1 = 0.0F;

                for (int j1 = k; j1 < l; j1++) {
                    if (this.sortedWorldRenderers[j1].skipAllRenderPasses()) {
                        this.sortedWorldRenderers[j1].isInFrustum = false;
                    } else {
                        if (!this.sortedWorldRenderers[j1].isInFrustum) {
                            this.sortedWorldRenderers[j1].isVisible = true;
                        }

                        if (this.sortedWorldRenderers[j1].isInFrustum && !this.sortedWorldRenderers[j1].isWaitingOnOcclusionQuery) {
                            float f2 = MathHelper.sqrt_float(this.sortedWorldRenderers[j1].distanceToEntitySquared(camera));
                            int k1 = (int)(1.0F + f2 / 128.0F);
                            if (this.cloudOffsetX % k1 == j1 % k1) {
                                WorldRenderer worldrenderer1 = this.sortedWorldRenderers[j1];
                                float f3 = (float)(worldrenderer1.posXMinus - d5);
                                float f4 = (float)(worldrenderer1.posYMinus - d0);
                                float f5 = (float)(worldrenderer1.posZMinus - d1);
                                float f6 = f3 - f9;
                                float f7 = f4 - f;
                                float f8 = f5 - f1;
                                if (f6 != 0.0F || f7 != 0.0F || f8 != 0.0F) {
                                    GL11.glTranslatef(f6, f7, f8);
                                    f9 += f6;
                                    f += f7;
                                    f1 += f8;
                                }

                                ARBOcclusionQuery.glBeginQueryARB(35092, this.sortedWorldRenderers[j1].glOcclusionQuery);
                                this.sortedWorldRenderers[j1].callOcclusionQueryList();
                                ARBOcclusionQuery.glEndQueryARB(35092);
                                this.sortedWorldRenderers[j1].isWaitingOnOcclusionQuery = true;
                            }
                        }
                    }
                }

                GL11.glPopMatrix();
                if (this.mc.gameSettings.anaglyph) {
                    if (EntityRenderer.anaglyphField == 0) {
                        GL11.glColorMask(false, true, true, true);
                    } else {
                        GL11.glColorMask(true, false, false, true);
                    }
                } else {
                    GL11.glColorMask(true, true, true, true);
                }

                GL11.glDepthMask(true);
                GL11.glEnable(3553);
                GL11.glEnable(3008);
                GL11.glEnable(2912);
                j += this.renderSortedRenderers(k, l, layer, tickDelta);
            } while (l < this.sortedWorldRenderers.length);
        } else {
            j += this.renderSortedRenderers(0, this.sortedWorldRenderers.length, layer, tickDelta);
        }

        return j;
    }

    private void checkOcclusionQueryResult(int from, int to) {
        for (int i = from; i < to; i++) {
            if (this.sortedWorldRenderers[i].isWaitingOnOcclusionQuery) {
                ((Buffer)this.occlusionResult).clear();
                ARBOcclusionQuery.glGetQueryObjectuARB(this.sortedWorldRenderers[i].glOcclusionQuery, 34919, this.occlusionResult);
                if (this.occlusionResult.get(0) != 0) {
                    this.sortedWorldRenderers[i].isWaitingOnOcclusionQuery = false;
                    ((Buffer)this.occlusionResult).clear();
                    ARBOcclusionQuery.glGetQueryObjectuARB(this.sortedWorldRenderers[i].glOcclusionQuery, 34918, this.occlusionResult);
                    this.sortedWorldRenderers[i].isVisible = this.occlusionResult.get(0) != 0;
                }
            }
        }
    }

    private int renderSortedRenderers(int from, int to, int layer, double tickDelta) {
        this.glRenderLists.clear();
        int i = 0;

        for (int j = from; j < to; j++) {
            if (layer == 0) {
                this.renderersLoaded++;
                if (this.sortedWorldRenderers[j].skipRenderPass[layer]) {
                    this.renderersSkippingRenderPass++;
                } else if (!this.sortedWorldRenderers[j].isInFrustum) {
                    this.renderersBeingClipped++;
                } else if (this.occlusionEnabled && !this.sortedWorldRenderers[j].isVisible) {
                    this.renderersBeingOccluded++;
                } else {
                    this.renderersBeingRendered++;
                }
            }

            if (!this.sortedWorldRenderers[j].skipRenderPass[layer]
                && this.sortedWorldRenderers[j].isInFrustum
                && (!this.occlusionEnabled || this.sortedWorldRenderers[j].isVisible)) {
                int k = this.sortedWorldRenderers[j].getGLCallListForPass(layer);
                if (k >= 0) {
                    this.glRenderLists.add(this.sortedWorldRenderers[j]);
                    i++;
                }
            }
        }

        EntityLiving entityliving = this.mc.renderViewEntity;
        double d2 = entityliving.lastTickPosX + (entityliving.posX - entityliving.lastTickPosX) * tickDelta;
        double d0 = entityliving.lastTickPosY + (entityliving.posY - entityliving.lastTickPosY) * tickDelta;
        double d1 = entityliving.lastTickPosZ + (entityliving.posZ - entityliving.lastTickPosZ) * tickDelta;
        int l = 0;

        for (int i1 = 0; i1 < this.allRenderLists.length; i1++) {
            this.allRenderLists[i1].clear();
        }

        for (int l1 = 0; l1 < this.glRenderLists.size(); l1++) {
            WorldRenderer worldrenderer = this.glRenderLists.get(l1);
            int j1 = -1;

            for (int k1 = 0; k1 < l; k1++) {
                if (this.allRenderLists[k1].isAt(worldrenderer.posXMinus, worldrenderer.posYMinus, worldrenderer.posZMinus)) {
                    j1 = k1;
                }
            }

            if (j1 < 0) {
                j1 = l++;
                this.allRenderLists[j1].init(worldrenderer.posXMinus, worldrenderer.posYMinus, worldrenderer.posZMinus, d2, d0, d1);
            }

            this.allRenderLists[j1].addGlList(worldrenderer.getGLCallListForPass(layer));
        }

        this.renderAllRenderLists(layer, tickDelta);
        return i;
    }

    public void renderAllRenderLists(int layer, double tickDelta) {
        for (int i = 0; i < this.allRenderLists.length; i++) {
            this.allRenderLists[i].render();
        }
    }

    public void updateClouds() {
        this.cloudOffsetX++;
    }

    public void renderSky(float tickDelta) {
        float f;
        float f2;
        float f3;
        float f4;
        if (this.mc.theWorld.worldProvider.isNether) {
            return;
        }
        GL11.glDisable(3553);
        Vec3D vec3D = this.worldObj.getSkyColor(this.mc.renderViewEntity, tickDelta);
        float f5 = (float) vec3D.xCoord;
        float f6 = (float) vec3D.yCoord;
        float f7 = (float) vec3D.zCoord;
        if (this.mc.gameSettings.anaglyph) {
            float f8 = (f5 * 30.0f + f6 * 59.0f + f7 * 11.0f) / 100.0f;
            float f9 = (f5 * 30.0f + f6 * 70.0f) / 100.0f;
            f4 = (f5 * 30.0f + f7 * 70.0f) / 100.0f;
            f5 = f8;
            f6 = f9;
            f7 = f4;
        }
        GL11.glColor3f(f5, f6, f7);
        Tessellator tessellator = Tessellator.instance;
        GL11.glDepthMask(false);
        GL11.glEnable(2912);
        GL11.glColor3f(f5, f6, f7);
        GL11.glCallList(this.glSkyList);
        GL11.glDisable(2912);
        GL11.glDisable(3008);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        RenderHelper.disableStandardItemLighting();
        float[] fArray = this.worldObj.worldProvider.calcSunriseSunsetColors(this.worldObj.getCelestialAngle(tickDelta), tickDelta);
        if (fArray != null) {
            float f10;
            GL11.glDisable(3553);
            GL11.glShadeModel(7425);
            GL11.glPushMatrix();
            GL11.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
            f4 = this.worldObj.getCelestialAngle(tickDelta);
            GL11.glRotatef(f4 > 0.5f ? 180.0f : 0.0f, 0.0f, 0.0f, 1.0f);
            f3 = fArray[0];
            f2 = fArray[1];
            f = fArray[2];
            if (this.mc.gameSettings.anaglyph) {
                float f11 = (f3 * 30.0f + f2 * 59.0f + f * 11.0f) / 100.0f;
                float f12 = (f3 * 30.0f + f2 * 70.0f) / 100.0f;
                f10 = (f3 * 30.0f + f * 70.0f) / 100.0f;
                f3 = f11;
                f2 = f12;
                f = f10;
            }
            tessellator.startDrawing(6);
            tessellator.setColorRGBA_F(f3, f2, f, fArray[3]);
            tessellator.addVertex(0.0, 100.0, 0.0);
            int n = 16;
            tessellator.setColorRGBA_F(fArray[0], fArray[1], fArray[2], 0.0f);
            for (int i = 0; i <= n; ++i) {
                f10 = (float) i * (float) Math.PI * 2.0f / (float) n;
                float f13 = MathHelper.sin(f10);
                float f14 = MathHelper.cos(f10);
                tessellator.addVertex(f13 * 120.0f, f14 * 120.0f, -f14 * 40.0f * fArray[3]);
            }
            tessellator.draw();
            GL11.glPopMatrix();
            GL11.glShadeModel(7424);
        }
        GL11.glEnable(3553);
        GL11.glBlendFunc(770, 1);
        GL11.glPushMatrix();
        float f15 = 1.0f - this.worldObj.getRain(tickDelta);
        f4 = 0.0f;
        f3 = 0.0f;
        f2 = 0.0f;
        GL11.glColor4f(1.0f, 1.0f, 1.0f, f15);
        GL11.glTranslatef(f4, f3, f2);
        GL11.glRotatef(0.0f, 0.0f, 0.0f, 1.0f);
        GL11.glRotatef(this.worldObj.getCelestialAngle(tickDelta) * 360.0f, 1.0f, 0.0f, 0.0f);
        f = 30.0f;
        GL11.glBindTexture(3553, this.renderEngine.getTexture("/terrain/sun.png"));
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(-f, 100.0, -f, 0.0, 0.0);
        tessellator.addVertexWithUV(f, 100.0, -f, 1.0, 0.0);
        tessellator.addVertexWithUV(f, 100.0, f, 1.0, 1.0);
        tessellator.addVertexWithUV(-f, 100.0, f, 0.0, 1.0);
        tessellator.draw();
        f = 20.0f;
        GL11.glBindTexture(3553, this.renderEngine.getTexture("/terrain/moon.png"));
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(-f, -100.0, f, 1.0, 1.0);
        tessellator.addVertexWithUV(f, -100.0, f, 0.0, 1.0);
        tessellator.addVertexWithUV(f, -100.0, -f, 0.0, 0.0);
        tessellator.addVertexWithUV(-f, -100.0, -f, 1.0, 0.0);
        tessellator.draw();
        GL11.glDisable(3553);
        float f16 = this.worldObj.getStarBrightness(tickDelta) * f15;
        if (f16 > 0.0f) {
            GL11.glColor4f(f16, f16, f16, f16);
            GL11.glCallList(this.starGLCallList);
        }
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glDisable(3042);
        GL11.glEnable(3008);
        GL11.glEnable(2912);
        GL11.glPopMatrix();
        if (this.worldObj.worldProvider.hasGround()) {
            GL11.glColor3f(f5 * 0.2f + 0.04f, f6 * 0.2f + 0.04f, f7 * 0.6f + 0.1f);
        } else {
            GL11.glColor3f(f5, f6, f7);
        }
        GL11.glDisable(3553);
        GL11.glCallList(this.glSkyList2);
        GL11.glEnable(3553);
        GL11.glDepthMask(true);
    }

    public void renderClouds(float tickDelta) {
        if (!this.mc.theWorld.worldProvider.isNether) {
            if (this.mc.gameSettings.fancyGraphics) {
                this.renderCloudsFancy(tickDelta);
            } else {
                GL11.glDisable(2884);
                float f = (float)(this.mc.renderViewEntity.lastTickPosY + (this.mc.renderViewEntity.posY - this.mc.renderViewEntity.lastTickPosY) * tickDelta);
                byte b0 = 32;
                int i = 256 / b0;
                Tessellator tessellator = Tessellator.instance;
                GL11.glBindTexture(3553, this.renderEngine.getTexture("/environment/clouds.png"));
                GL11.glEnable(3042);
                GL11.glBlendFunc(770, 771);
                Vec3D vec3d = this.worldObj.getCloudColor(tickDelta);
                float f1 = (float)vec3d.xCoord;
                float f2 = (float)vec3d.yCoord;
                float f3 = (float)vec3d.zCoord;
                if (this.mc.gameSettings.anaglyph) {
                    float f4 = (f1 * 30.0F + f2 * 59.0F + f3 * 11.0F) / 100.0F;
                    float f5 = (f1 * 30.0F + f2 * 70.0F) / 100.0F;
                    float f6 = (f1 * 30.0F + f3 * 70.0F) / 100.0F;
                    f1 = f4;
                    f2 = f5;
                    f3 = f6;
                }

                float f10 = 4.8828125E-4F;
                double d1 = this.mc.renderViewEntity.prevPosX
                    + (this.mc.renderViewEntity.posX - this.mc.renderViewEntity.prevPosX) * tickDelta
                    + (this.cloudOffsetX + tickDelta) * 0.03F;
                double d0 = this.mc.renderViewEntity.prevPosZ + (this.mc.renderViewEntity.posZ - this.mc.renderViewEntity.prevPosZ) * tickDelta;
                int j = MathHelper.floor_double(d1 / 2048.0);
                int k = MathHelper.floor_double(d0 / 2048.0);
                d1 -= j * 2048;
                d0 -= k * 2048;
                float f7 = this.worldObj.worldProvider.getCloudHeight() - f + 0.33F;
                float f8 = (float)(d1 * f10);
                float f9 = (float)(d0 * f10);
                tessellator.startDrawingQuads();
                tessellator.setColorRGBA_F(f1, f2, f3, 0.8F);

                for (int l = -b0 * i; l < b0 * i; l += b0) {
                    for (int i1 = -b0 * i; i1 < b0 * i; i1 += b0) {
                        tessellator.addVertexWithUV(l + 0, f7, i1 + b0, (l + 0) * f10 + f8, (i1 + b0) * f10 + f9);
                        tessellator.addVertexWithUV(l + b0, f7, i1 + b0, (l + b0) * f10 + f8, (i1 + b0) * f10 + f9);
                        tessellator.addVertexWithUV(l + b0, f7, i1 + 0, (l + b0) * f10 + f8, (i1 + 0) * f10 + f9);
                        tessellator.addVertexWithUV(l + 0, f7, i1 + 0, (l + 0) * f10 + f8, (i1 + 0) * f10 + f9);
                    }
                }

                tessellator.draw();
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                GL11.glDisable(3042);
                GL11.glEnable(2884);
            }
        }
    }

    public boolean hasThiccFog(double x, double y, double z, float tickDelta) {
        return false;
    }

    public void renderCloudsFancy(float tickDelta) {
        GL11.glDisable(2884);
        float f = (float)(this.mc.renderViewEntity.lastTickPosY + (this.mc.renderViewEntity.posY - this.mc.renderViewEntity.lastTickPosY) * tickDelta);
        Tessellator tessellator = Tessellator.instance;
        float f1 = 12.0F;
        float f2 = 4.0F;
        double d0 = (
                this.mc.renderViewEntity.prevPosX
                    + (this.mc.renderViewEntity.posX - this.mc.renderViewEntity.prevPosX) * tickDelta
                    + (this.cloudOffsetX + tickDelta) * 0.03F
            )
            / f1;
        double d1 = (this.mc.renderViewEntity.prevPosZ + (this.mc.renderViewEntity.posZ - this.mc.renderViewEntity.prevPosZ) * tickDelta) / f1 + 0.33F;
        float f3 = this.worldObj.worldProvider.getCloudHeight() - f + 0.33F;
        int i = MathHelper.floor_double(d0 / 2048.0);
        int j = MathHelper.floor_double(d1 / 2048.0);
        d0 -= i * 2048;
        d1 -= j * 2048;
        GL11.glBindTexture(3553, this.renderEngine.getTexture("/environment/clouds.png"));
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        Vec3D vec3d = this.worldObj.getCloudColor(tickDelta);
        float f4 = (float)vec3d.xCoord;
        float f5 = (float)vec3d.yCoord;
        float f6 = (float)vec3d.zCoord;
        if (this.mc.gameSettings.anaglyph) {
            float f7 = (f4 * 30.0F + f5 * 59.0F + f6 * 11.0F) / 100.0F;
            float f8 = (f4 * 30.0F + f5 * 70.0F) / 100.0F;
            float f9 = (f4 * 30.0F + f6 * 70.0F) / 100.0F;
            f4 = f7;
            f5 = f8;
            f6 = f9;
        }

        float f17 = (float)(d0 * 0.0);
        float f18 = (float)(d1 * 0.0);
        float f19 = 0.00390625F;
        f17 = MathHelper.floor_double(d0) * f19;
        f18 = MathHelper.floor_double(d1) * f19;
        float f10 = (float)(d0 - MathHelper.floor_double(d0));
        float f11 = (float)(d1 - MathHelper.floor_double(d1));
        byte b0 = 8;
        byte b1 = 3;
        float f12 = 9.765625E-4F;
        GL11.glScalef(f1, 1.0F, f1);

        for (int k = 0; k < 2; k++) {
            if (k == 0) {
                GL11.glColorMask(false, false, false, false);
            } else if (this.mc.gameSettings.anaglyph) {
                if (EntityRenderer.anaglyphField == 0) {
                    GL11.glColorMask(false, true, true, true);
                } else {
                    GL11.glColorMask(true, false, false, true);
                }
            } else {
                GL11.glColorMask(true, true, true, true);
            }

            for (int l = -b1 + 1; l <= b1; l++) {
                for (int i1 = -b1 + 1; i1 <= b1; i1++) {
                    tessellator.startDrawingQuads();
                    float f13 = l * b0;
                    float f14 = i1 * b0;
                    float f15 = f13 - f10;
                    float f16 = f14 - f11;
                    if (f3 > -f2 - 1.0F) {
                        tessellator.setColorRGBA_F(f4 * 0.7F, f5 * 0.7F, f6 * 0.7F, 0.8F);
                        tessellator.setNormal(0.0F, -1.0F, 0.0F);
                        tessellator.addVertexWithUV(f15 + 0.0F, f3 + 0.0F, f16 + b0, (f13 + 0.0F) * f19 + f17, (f14 + b0) * f19 + f18);
                        tessellator.addVertexWithUV(f15 + b0, f3 + 0.0F, f16 + b0, (f13 + b0) * f19 + f17, (f14 + b0) * f19 + f18);
                        tessellator.addVertexWithUV(f15 + b0, f3 + 0.0F, f16 + 0.0F, (f13 + b0) * f19 + f17, (f14 + 0.0F) * f19 + f18);
                        tessellator.addVertexWithUV(f15 + 0.0F, f3 + 0.0F, f16 + 0.0F, (f13 + 0.0F) * f19 + f17, (f14 + 0.0F) * f19 + f18);
                    }

                    if (f3 <= f2 + 1.0F) {
                        tessellator.setColorRGBA_F(f4, f5, f6, 0.8F);
                        tessellator.setNormal(0.0F, 1.0F, 0.0F);
                        tessellator.addVertexWithUV(f15 + 0.0F, f3 + f2 - f12, f16 + b0, (f13 + 0.0F) * f19 + f17, (f14 + b0) * f19 + f18);
                        tessellator.addVertexWithUV(f15 + b0, f3 + f2 - f12, f16 + b0, (f13 + b0) * f19 + f17, (f14 + b0) * f19 + f18);
                        tessellator.addVertexWithUV(f15 + b0, f3 + f2 - f12, f16 + 0.0F, (f13 + b0) * f19 + f17, (f14 + 0.0F) * f19 + f18);
                        tessellator.addVertexWithUV(f15 + 0.0F, f3 + f2 - f12, f16 + 0.0F, (f13 + 0.0F) * f19 + f17, (f14 + 0.0F) * f19 + f18);
                    }

                    tessellator.setColorRGBA_F(f4 * 0.9F, f5 * 0.9F, f6 * 0.9F, 0.8F);
                    if (l > -1) {
                        tessellator.setNormal(-1.0F, 0.0F, 0.0F);

                        for (int j1 = 0; j1 < b0; j1++) {
                            tessellator.addVertexWithUV(f15 + j1 + 0.0F, f3 + 0.0F, f16 + b0, (f13 + j1 + 0.5F) * f19 + f17, (f14 + b0) * f19 + f18);
                            tessellator.addVertexWithUV(f15 + j1 + 0.0F, f3 + f2, f16 + b0, (f13 + j1 + 0.5F) * f19 + f17, (f14 + b0) * f19 + f18);
                            tessellator.addVertexWithUV(f15 + j1 + 0.0F, f3 + f2, f16 + 0.0F, (f13 + j1 + 0.5F) * f19 + f17, (f14 + 0.0F) * f19 + f18);
                            tessellator.addVertexWithUV(f15 + j1 + 0.0F, f3 + 0.0F, f16 + 0.0F, (f13 + j1 + 0.5F) * f19 + f17, (f14 + 0.0F) * f19 + f18);
                        }
                    }

                    if (l <= 1) {
                        tessellator.setNormal(1.0F, 0.0F, 0.0F);

                        for (int k1 = 0; k1 < b0; k1++) {
                            tessellator.addVertexWithUV(f15 + k1 + 1.0F - f12, f3 + 0.0F, f16 + b0, (f13 + k1 + 0.5F) * f19 + f17, (f14 + b0) * f19 + f18);
                            tessellator.addVertexWithUV(f15 + k1 + 1.0F - f12, f3 + f2, f16 + b0, (f13 + k1 + 0.5F) * f19 + f17, (f14 + b0) * f19 + f18);
                            tessellator.addVertexWithUV(f15 + k1 + 1.0F - f12, f3 + f2, f16 + 0.0F, (f13 + k1 + 0.5F) * f19 + f17, (f14 + 0.0F) * f19 + f18);
                            tessellator.addVertexWithUV(f15 + k1 + 1.0F - f12, f3 + 0.0F, f16 + 0.0F, (f13 + k1 + 0.5F) * f19 + f17, (f14 + 0.0F) * f19 + f18);
                        }
                    }

                    tessellator.setColorRGBA_F(f4 * 0.8F, f5 * 0.8F, f6 * 0.8F, 0.8F);
                    if (i1 > -1) {
                        tessellator.setNormal(0.0F, 0.0F, -1.0F);

                        for (int l1 = 0; l1 < b0; l1++) {
                            tessellator.addVertexWithUV(f15 + 0.0F, f3 + f2, f16 + l1 + 0.0F, (f13 + 0.0F) * f19 + f17, (f14 + l1 + 0.5F) * f19 + f18);
                            tessellator.addVertexWithUV(f15 + b0, f3 + f2, f16 + l1 + 0.0F, (f13 + b0) * f19 + f17, (f14 + l1 + 0.5F) * f19 + f18);
                            tessellator.addVertexWithUV(f15 + b0, f3 + 0.0F, f16 + l1 + 0.0F, (f13 + b0) * f19 + f17, (f14 + l1 + 0.5F) * f19 + f18);
                            tessellator.addVertexWithUV(f15 + 0.0F, f3 + 0.0F, f16 + l1 + 0.0F, (f13 + 0.0F) * f19 + f17, (f14 + l1 + 0.5F) * f19 + f18);
                        }
                    }

                    if (i1 <= 1) {
                        tessellator.setNormal(0.0F, 0.0F, 1.0F);

                        for (int i2 = 0; i2 < b0; i2++) {
                            tessellator.addVertexWithUV(f15 + 0.0F, f3 + f2, f16 + i2 + 1.0F - f12, (f13 + 0.0F) * f19 + f17, (f14 + i2 + 0.5F) * f19 + f18);
                            tessellator.addVertexWithUV(f15 + b0, f3 + f2, f16 + i2 + 1.0F - f12, (f13 + b0) * f19 + f17, (f14 + i2 + 0.5F) * f19 + f18);
                            tessellator.addVertexWithUV(f15 + b0, f3 + 0.0F, f16 + i2 + 1.0F - f12, (f13 + b0) * f19 + f17, (f14 + i2 + 0.5F) * f19 + f18);
                            tessellator.addVertexWithUV(f15 + 0.0F, f3 + 0.0F, f16 + i2 + 1.0F - f12, (f13 + 0.0F) * f19 + f17, (f14 + i2 + 0.5F) * f19 + f18);
                        }
                    }

                    tessellator.draw();
                }
            }
        }

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(3042);
        GL11.glEnable(2884);
    }

    public boolean updateRenderers(EntityLiving camera, boolean force) {
        boolean flag = false;
        if (flag) {
            Collections.sort(this.worldRenderersToUpdate, new RenderSorter(camera));
            int k1 = this.worldRenderersToUpdate.size() - 1;
            int l1 = this.worldRenderersToUpdate.size();

            for (int i2 = 0; i2 < l1; i2++) {
                WorldRenderer worldrenderer2 = this.worldRenderersToUpdate.get(k1 - i2);
                if (!force) {
                    if (worldrenderer2.distanceToEntitySquared(camera) > 256.0F) {
                        if (worldrenderer2.isInFrustum) {
                            if (i2 >= 3) {
                                return false;
                            }
                        } else if (i2 >= 1) {
                            return false;
                        }
                    }
                } else if (!worldrenderer2.isInFrustum) {
                    continue;
                }

                worldrenderer2.updateRenderer();
                this.worldRenderersToUpdate.remove(worldrenderer2);
                worldrenderer2.needsUpdate = false;
            }

            return this.worldRenderersToUpdate.size() == 0;
        } else {
            byte b0 = 2;
            RenderSorter rendersorter = new RenderSorter(camera);
            WorldRenderer[] aworldrenderer = new WorldRenderer[b0];
            ArrayList arraylist = null;
            int i = this.worldRenderersToUpdate.size();
            int j = 0;

            for (int k = 0; k < i; k++) {
                WorldRenderer worldrenderer = this.worldRenderersToUpdate.get(k);
                if (!force) {
                    if (worldrenderer.distanceToEntitySquared(camera) > 256.0F) {
                        int l = 0;

                        while (l < b0 && (aworldrenderer[l] == null || rendersorter.compare(aworldrenderer[l], worldrenderer) <= 0)) {
                            l++;
                        }

                        if (--l <= 0) {
                            continue;
                        }

                        int i1 = l;

                        while (--i1 != 0) {
                            aworldrenderer[i1 - 1] = aworldrenderer[i1];
                        }

                        aworldrenderer[l] = worldrenderer;
                        continue;
                    }
                } else if (!worldrenderer.isInFrustum) {
                    continue;
                }

                if (arraylist == null) {
                    arraylist = new ArrayList();
                }

                j++;
                arraylist.add(worldrenderer);
                this.worldRenderersToUpdate.set(k, null);
            }

            if (arraylist != null) {
                if (arraylist.size() > 1) {
                    Collections.sort(arraylist, rendersorter);
                }

                for (int j2 = arraylist.size() - 1; j2 >= 0; j2--) {
                    WorldRenderer worldrenderer3 = (WorldRenderer)arraylist.get(j2);
                    worldrenderer3.updateRenderer();
                    worldrenderer3.needsUpdate = false;
                }
            }

            int k2 = 0;

            for (int l2 = b0 - 1; l2 >= 0; l2--) {
                WorldRenderer worldrenderer4 = aworldrenderer[l2];
                if (worldrenderer4 != null) {
                    if (!worldrenderer4.isInFrustum && l2 != b0 - 1) {
                        aworldrenderer[l2] = null;
                        aworldrenderer[0] = null;
                        break;
                    }

                    aworldrenderer[l2].updateRenderer();
                    aworldrenderer[l2].needsUpdate = false;
                    k2++;
                }
            }

            int i3 = 0;
            int j3 = 0;

            for (int k3 = this.worldRenderersToUpdate.size(); i3 != k3; i3++) {
                WorldRenderer worldrenderer1 = this.worldRenderersToUpdate.get(i3);
                if (worldrenderer1 != null) {
                    boolean flag1 = false;

                    for (int j1 = 0; j1 < b0 && !flag1; j1++) {
                        if (worldrenderer1 == aworldrenderer[j1]) {
                            flag1 = true;
                        }
                    }

                    if (!flag1) {
                        if (j3 != i3) {
                            this.worldRenderersToUpdate.set(j3, worldrenderer1);
                        }

                        j3++;
                    }
                }
            }

            while (--i3 >= j3) {
                this.worldRenderersToUpdate.remove(i3);
            }

            return i == j + k2;
        }
    }

    public void drawBlockBreaking(EntityPlayer camera, MovingObjectPosition hit, int var3, ItemStack itemInHand, float tickDelta) {
        Tessellator tessellator = Tessellator.instance;
        GL11.glEnable(3042);
        GL11.glEnable(3008);
        GL11.glBlendFunc(770, 1);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, (MathHelper.sin((float)System.currentTimeMillis() / 100.0F) * 0.2F + 0.4F) * 0.5F);
        if (var3 == 0) {
            if (this.damagePartialTime > 0.0F) {
                GL11.glBlendFunc(774, 768);
                int i = this.renderEngine.getTexture("/terrain.png");
                GL11.glBindTexture(3553, i);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.5F);
                GL11.glPushMatrix();
                int j = this.worldObj.getBlockId(hit.blockX, hit.blockY, hit.blockZ);
                Block block = j > 0 ? Block.blocksList[j] : null;
                GL11.glDisable(3008);
                GL11.glPolygonOffset(-3.0F, -3.0F);
                GL11.glEnable(32823);
                double d0 = camera.lastTickPosX + (camera.posX - camera.lastTickPosX) * tickDelta;
                double d1 = camera.lastTickPosY + (camera.posY - camera.lastTickPosY) * tickDelta;
                double d2 = camera.lastTickPosZ + (camera.posZ - camera.lastTickPosZ) * tickDelta;
                if (block == null) {
                    block = Block.stone;
                }

                GL11.glEnable(3008);
                tessellator.startDrawingQuads();
                tessellator.setTranslationD(-d0, -d1, -d2);
                tessellator.disableColor();
                this.globalRenderBlocks.renderBlockUsingTexture(block, hit.blockX, hit.blockY, hit.blockZ, 240 + (int)(this.damagePartialTime * 10.0F));
                tessellator.draw();
                tessellator.setTranslationD(0.0, 0.0, 0.0);
                GL11.glDisable(3008);
                GL11.glPolygonOffset(0.0F, 0.0F);
                GL11.glDisable(32823);
                GL11.glEnable(3008);
                GL11.glDepthMask(true);
                GL11.glPopMatrix();
            }
        } else if (itemInHand != null) {
            GL11.glBlendFunc(770, 771);
            float f = MathHelper.sin((float)System.currentTimeMillis() / 100.0F) * 0.2F + 0.8F;
            GL11.glColor4f(f, f, f, MathHelper.sin((float)System.currentTimeMillis() / 200.0F) * 0.2F + 0.5F);
            int l = this.renderEngine.getTexture("/terrain.png");
            GL11.glBindTexture(3553, l);
            int i1 = hit.blockX;
            int j1 = hit.blockY;
            int k = hit.blockZ;
            if (hit.sideHit == 0) {
                j1--;
            }

            if (hit.sideHit == 1) {
                j1++;
            }

            if (hit.sideHit == 2) {
                k--;
            }

            if (hit.sideHit == 3) {
                k++;
            }

            if (hit.sideHit == 4) {
                i1--;
            }

            if (hit.sideHit == 5) {
                i1++;
            }
        }

        GL11.glDisable(3042);
        GL11.glDisable(3008);
    }

    public void drawSelectionBox(EntityPlayer camera, MovingObjectPosition hit, int var3, ItemStack itemInHand, float tickDelta) {
        if (var3 == 0 && hit.typeOfHit == MovingObjectPosition.EnumMovingObjectType.TILE) {
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.4F);
            GL11.glLineWidth(2.0F);
            GL11.glDisable(3553);
            GL11.glDepthMask(false);
            float f = 0.002F;
            int i = this.worldObj.getBlockId(hit.blockX, hit.blockY, hit.blockZ);
            if (i > 0) {
                Block.blocksList[i].setBlockBoundsBasedOnState(this.worldObj, hit.blockX, hit.blockY, hit.blockZ);
                double d0 = camera.lastTickPosX + (camera.posX - camera.lastTickPosX) * tickDelta;
                double d1 = camera.lastTickPosY + (camera.posY - camera.lastTickPosY) * tickDelta;
                double d2 = camera.lastTickPosZ + (camera.posZ - camera.lastTickPosZ) * tickDelta;
                this.drawOutlinedBoundingBox(
                    Block.blocksList[i]
                        .getSelectedBoundingBoxFromPool(this.worldObj, hit.blockX, hit.blockY, hit.blockZ)
                        .expand(f, f, f)
                        .getOffsetBoundingBox(-d0, -d1, -d2)
                );
            }

            GL11.glDepthMask(true);
            GL11.glEnable(3553);
            GL11.glDisable(3042);
        }
    }

    private void drawOutlinedBoundingBox(AxisAlignedBB shape) {
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawing(3);
        tessellator.addVertex(shape.minX, shape.minY, shape.minZ);
        tessellator.addVertex(shape.maxX, shape.minY, shape.minZ);
        tessellator.addVertex(shape.maxX, shape.minY, shape.maxZ);
        tessellator.addVertex(shape.minX, shape.minY, shape.maxZ);
        tessellator.addVertex(shape.minX, shape.minY, shape.minZ);
        tessellator.draw();
        tessellator.startDrawing(3);
        tessellator.addVertex(shape.minX, shape.maxY, shape.minZ);
        tessellator.addVertex(shape.maxX, shape.maxY, shape.minZ);
        tessellator.addVertex(shape.maxX, shape.maxY, shape.maxZ);
        tessellator.addVertex(shape.minX, shape.maxY, shape.maxZ);
        tessellator.addVertex(shape.minX, shape.maxY, shape.minZ);
        tessellator.draw();
        tessellator.startDrawing(1);
        tessellator.addVertex(shape.minX, shape.minY, shape.minZ);
        tessellator.addVertex(shape.minX, shape.maxY, shape.minZ);
        tessellator.addVertex(shape.maxX, shape.minY, shape.minZ);
        tessellator.addVertex(shape.maxX, shape.maxY, shape.minZ);
        tessellator.addVertex(shape.maxX, shape.minY, shape.maxZ);
        tessellator.addVertex(shape.maxX, shape.maxY, shape.maxZ);
        tessellator.addVertex(shape.minX, shape.minY, shape.maxZ);
        tessellator.addVertex(shape.minX, shape.maxY, shape.maxZ);
        tessellator.draw();
    }

    public void markDirty(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        int i = MathHelper.bucketInt(minX, 16);
        int j = MathHelper.bucketInt(minY, 16);
        int k = MathHelper.bucketInt(minZ, 16);
        int l = MathHelper.bucketInt(maxX, 16);
        int i1 = MathHelper.bucketInt(maxY, 16);
        int j1 = MathHelper.bucketInt(maxZ, 16);

        for (int k1 = i; k1 <= l; k1++) {
            int l1 = k1 % this.renderChunksWide;
            if (l1 < 0) {
                l1 += this.renderChunksWide;
            }

            for (int i2 = j; i2 <= i1; i2++) {
                int j2 = i2 % this.renderChunksTall;
                if (j2 < 0) {
                    j2 += this.renderChunksTall;
                }

                for (int k2 = k; k2 <= j1; k2++) {
                    int l2 = k2 % this.renderChunksDeep;
                    if (l2 < 0) {
                        l2 += this.renderChunksDeep;
                    }

                    int i3 = (l2 * this.renderChunksTall + j2) * this.renderChunksWide + l1;
                    WorldRenderer worldrenderer = this.worldRenderers[i3];
                    if (!worldrenderer.needsUpdate) {
                        this.worldRenderersToUpdate.add(worldrenderer);
                        worldrenderer.markDirty();
                    }
                }
            }
        }
    }

    @Override
    public void markBlockAndNeighborsNeedsUpdate(int x, int y, int z) {
        this.markDirty(x - 1, y - 1, z - 1, x + 1, y + 1, z + 1);
    }

    @Override
    public void markBlockRangeNeedsUpdate(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        this.markDirty(minX - 1, minY - 1, minZ - 1, maxX + 1, maxY + 1, maxZ + 1);
    }

    public void clipRenderersByFrustrum(ICamera culler, float tickDelta) {
        for (int i = 0; i < this.worldRenderers.length; i++) {
            if (!this.worldRenderers[i].skipAllRenderPasses() && (!this.worldRenderers[i].isInFrustum || (i + this.frustrumCheckOffset & 15) == 0)) {
                this.worldRenderers[i].updateInFrustrum(culler);
            }
        }

        this.frustrumCheckOffset++;
    }

    @Override
    public void playRecord(String record, int x, int y, int z) {
        if (record != null) {
            this.mc.ingameGUI.setRecordPlayingMessage("C418 - " + record);
        }

        this.mc.sndManager.playStreaming(record, x, y, z, 1.0F, 1.0F);
    }

    @Override
    public void playSound(String name, double x, double y, double z, float volume, float pitch) {
        float f = 16.0F;
        if (volume > 1.0F) {
            f *= volume;
        }

        if (this.mc.renderViewEntity.getDistanceSq(x, y, z) < f * f) {
            this.mc.sndManager.playSound(name, (float)x, (float)y, (float)z, volume, pitch);
        }
    }

    public void spawnParticle(String type, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
        if (this.mc == null || this.mc.renderViewEntity == null || this.mc.effectRenderer == null) {
            return;
        }
        double d = this.mc.renderViewEntity.posX - x;
        double d2 = this.mc.renderViewEntity.posY - y;
        double d3 = this.mc.renderViewEntity.posZ - z;
        double d4 = 16.0;
        if (d * d + d2 * d2 + d3 * d3 > d4 * d4) {
            return;
        }
        if (type.equals("bubble")) {
            this.mc.effectRenderer.addEffect(new EntityBubbleFX(this.worldObj, x, y, z, velocityX, velocityY, velocityZ));
        } else if (type.equals("smoke")) {
            this.mc.effectRenderer.addEffect(new EntitySmokeFX(this.worldObj, x, y, z, velocityX, velocityY, velocityZ));
        } else if (type.equals("note")) {
            this.mc.effectRenderer.addEffect(new EntityNoteFX(this.worldObj, x, y, z, velocityX, velocityY, velocityZ));
        } else if (type.equals("portal")) {
            this.mc.effectRenderer.addEffect(new EntityPortalFX(this.worldObj, x, y, z, velocityX, velocityY, velocityZ));
        } else if (type.equals("explode")) {
            this.mc.effectRenderer.addEffect(new EntityExplodeFX(this.worldObj, x, y, z, velocityX, velocityY, velocityZ));
        } else if (type.equals("flame")) {
            this.mc.effectRenderer.addEffect(new EntityFlameFX(this.worldObj, x, y, z, velocityX, velocityY, velocityZ));
        } else if (type.equals("lava")) {
            this.mc.effectRenderer.addEffect(new EntityLavaFX(this.worldObj, x, y, z));
        } else if (type.equals("footstep")) {
            this.mc.effectRenderer.addEffect(new EntityFootStepFX(this.renderEngine, this.worldObj, x, y, z));
        } else if (type.equals("splash")) {
            this.mc.effectRenderer.addEffect(new EntitySplashFX(this.worldObj, x, y, z, velocityX, velocityY, velocityZ));
        } else if (type.equals("largesmoke")) {
            this.mc.effectRenderer.addEffect(new EntitySmokeFX(this.worldObj, x, y, z, velocityX, velocityY, velocityZ, 2.5f));
        } else if (type.equals("reddust")) {
            this.mc.effectRenderer.addEffect(new EntityReddustFX(this.worldObj, x, y, z, (float) velocityX, (float) velocityY, (float) velocityZ));
        } else if (type.equals("snowballpoof")) {
            this.mc.effectRenderer.addEffect(new EntitySlimeFX(this.worldObj, x, y, z, Item.snowball));
        } else if (type.equals("snowshovel")) {
            this.mc.effectRenderer.addEffect(new EntitySnowShovelFX(this.worldObj, x, y, z, velocityX, velocityY, velocityZ));
        } else if (type.equals("slime")) {
            this.mc.effectRenderer.addEffect(new EntitySlimeFX(this.worldObj, x, y, z, Item.slimeBall));
        } else if (type.equals("heart")) {
            this.mc.effectRenderer.addEffect(new EntityHeartFX(this.worldObj, x, y, z, velocityX, velocityY, velocityZ));
        }
    }

    @Override
    public void obtainEntitySkin(Entity entity) {
        entity.updateCloak();
        if (entity.skinUrl != null) {
            this.renderEngine.obtainImageData(entity.skinUrl, new ImageBufferDownload());
        }

        if (entity.cloakUrl != null) {
            this.renderEngine.obtainImageData(entity.cloakUrl, new ImageBufferDownload());
        }
    }

    @Override
    public void releaseEntitySkin(Entity entity) {
        if (entity.skinUrl != null) {
            this.renderEngine.releaseImageData(entity.skinUrl);
        }

        if (entity.cloakUrl != null) {
            this.renderEngine.releaseImageData(entity.cloakUrl);
        }
    }

    @Override
    public void updateAllRenderers() {
        for (int i = 0; i < this.worldRenderers.length; i++) {
            if (this.worldRenderers[i].isChunkLit && !this.worldRenderers[i].needsUpdate) {
                this.worldRenderersToUpdate.add(this.worldRenderers[i]);
                this.worldRenderers[i].markDirty();
            }
        }
    }

    @Override
    public void doNothingWithTileEntity(int x, int y, int z, TileEntity blockEntity) {
    }

    public void releaseGlLists() {
        GLAllocation.releaseList(this.glRenderListBase);
    }

    public void doEvent(EntityPlayer source, int type, int x, int y, int z, int data) {
        Random random = this.worldObj.rand;
        switch(type) {
            case 1001:
                {
                    this.worldObj.playSoundEffect(x, y, z, "random.click", 1.0f, 1.2f);
                    break;
                }
            case 1000:
                {
                    this.worldObj.playSoundEffect(x, y, z, "random.click", 1.0f, 1.0f);
                    break;
                }
            case 1002:
                {
                    this.worldObj.playSoundEffect(x, y, z, "random.bow", 1.0f, 1.2f);
                    break;
                }
            case 2000:
                {
                    int n = data % 3 - 1;
                    int n2 = data / 3 % 3 - 1;
                    double d = (double) x + (double) n * 0.6 + 0.5;
                    double d2 = (double) y + 0.5;
                    double d3 = (double) z + (double) n2 * 0.6 + 0.5;
                    for (int i = 0; i < 10; ++i) {
                        double d4 = random.nextDouble() * 0.2 + 0.01;
                        double d5 = d + (double) n * 0.01 + (random.nextDouble() - 0.5) * (double) n2 * 0.5;
                        double d6 = d2 + (random.nextDouble() - 0.5) * 0.5;
                        double d7 = d3 + (double) n2 * 0.01 + (random.nextDouble() - 0.5) * (double) n * 0.5;
                        double d8 = (double) n * d4 + random.nextGaussian() * 0.01;
                        double d9 = -0.03 + random.nextGaussian() * 0.01;
                        double d10 = (double) n2 * d4 + random.nextGaussian() * 0.01;
                        this.spawnParticle("smoke", d5, d6, d7, d8, d9, d10);
                    }
                    break;
                }
            case 2001:
                {
                    int n = data & 0xFF;
                    if (n > 0) {
                        Block block = Block.blocksList[n];
                        this.mc.sndManager.playSound(block.stepSound.stepSoundDir(), (float) x + 0.5f, (float) y + 0.5f, (float) z + 0.5f, (block.stepSound.getVolume() + 1.0f) / 2.0f, block.stepSound.getPitch() * 0.8f);
                    }
                    this.mc.effectRenderer.addBlockDestroyEffects(x, y, z, data & 0xFF, data >> 8 & 0xFF);
                    break;
                }
            case 1003:
                {
                    if (Math.random() < 0.5) {
                        this.worldObj.playSoundEffect((double) x + 0.5, (double) y + 0.5, (double) z + 0.5, "random.door_open", 1.0f, this.worldObj.rand.nextFloat() * 0.1f + 0.9f);
                        break;
                    }
                    this.worldObj.playSoundEffect((double) x + 0.5, (double) y + 0.5, (double) z + 0.5, "random.door_close", 1.0f, this.worldObj.rand.nextFloat() * 0.1f + 0.9f);
                    break;
                }
            case 1004:
                {
                    this.worldObj.playSoundEffect((float) x + 0.5f, (float) y + 0.5f, (float) z + 0.5f, "random.fizz", 0.5f, 2.6f + (random.nextFloat() - random.nextFloat()) * 0.8f);
                    break;
                }
            case 1005:
                {
                    if (Item.itemsList[data] instanceof ItemRecord) {
                        this.worldObj.playRecord(((ItemRecord) Item.itemsList[data]).recordName, x, y, z);
                        break;
                    }
                    this.worldObj.playRecord(null, x, y, z);
                }
            }
    }
}
