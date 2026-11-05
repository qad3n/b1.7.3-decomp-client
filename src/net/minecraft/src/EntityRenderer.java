package net.minecraft.src;

import java.nio.FloatBuffer;
import java.util.List;
import java.util.Random;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.glu.GLU;
import java.nio.Buffer;

public class EntityRenderer {
    public static boolean anaglyphEnabled = false;
    public static int anaglyphField;
    private Minecraft mc;
    private float farPlaneDistance = 0.0f;
    public ItemRenderer itemRenderer;
    private int rendererUpdateCount;
    private Entity pointedEntity = null;
    private MouseFilter mouseFilterXAxis = new MouseFilter();
    private MouseFilter mouseFilterYAxis = new MouseFilter();
    private MouseFilter mouseFilterDummy1 = new MouseFilter();
    private MouseFilter mouseFilterDummy2 = new MouseFilter();
    private MouseFilter mouseFilterDummy3 = new MouseFilter();
    private MouseFilter mouseFilterDummy4 = new MouseFilter();
    private float oldCamPos = 4.0f;
    private float newCamPos = 4.0f;
    private float oldCamPitch = 0.0f;
    private float newCamPitch = 0.0f;
    private float oldCamYaw = 0.0f;
    private float newCamYaw = 0.0f;
    private float oldFov = 0.0f;
    private float newFov = 0.0f;
    private float oldCamRoll = 0.0f;
    private float newCamRoll = 0.0f;
    private boolean cloudFog = false;
    private double cameraZoom = 1.0;
    private double cameraYaw = 0.0;
    private double cameraPitch = 0.0;
    private long prevFrameTime = System.currentTimeMillis();
    private long lastWorldRenderTime = 0L;
    private Random random = new Random();
    private int rainSoundCounter = 0;
    volatile int field_1394_b = 0;
    volatile int field_1393_c = 0;
    FloatBuffer fogColorBuffer = GLAllocation.createDirectFloatBuffer(16);
    float fogColorRed;
    float fogColorGreen;
    float fogColorBlue;
    private float fogColor2;
    private float fogColor1;

    public EntityRenderer(Minecraft minecraft) {
        this.mc = minecraft;
        this.itemRenderer = new ItemRenderer(minecraft);
    }

    public void updateRenderer() {
        this.fogColor2 = this.fogColor1;
        this.newCamPos = this.oldCamPos;
        this.newCamPitch = this.oldCamPitch;
        this.newCamYaw = this.oldCamYaw;
        this.newFov = this.oldFov;
        this.newCamRoll = this.oldCamRoll;
        if (this.mc.renderViewEntity == null) {
            this.mc.renderViewEntity = this.mc.thePlayer;
        }
        float f = this.mc.theWorld.getLightBrightness(MathHelper.floor_double(this.mc.renderViewEntity.posX), MathHelper.floor_double(this.mc.renderViewEntity.posY), MathHelper.floor_double(this.mc.renderViewEntity.posZ));
        float f2 = (float)(3 - this.mc.gameSettings.renderDistance) / 3.0f;
        float f3 = f * (1.0f - f2) + f2;
        this.fogColor1 += (f3 - this.fogColor1) * 0.1f;
        ++this.rendererUpdateCount;
        this.itemRenderer.updateEquippedItem();
        this.addRainParticles();
    }

    public void getMouseOver(float tickDelta) {
        if (this.mc.renderViewEntity == null) {
            return;
        }
        if (this.mc.theWorld == null) {
            return;
        }
        double d = this.mc.playerController.getBlockReachDistance();
        this.mc.objectMouseOver = this.mc.renderViewEntity.rayTrace(d, tickDelta);
        double d2 = d;
        Vec3D vec3D = this.mc.renderViewEntity.getPosition(tickDelta);
        if (this.mc.objectMouseOver != null) {
            d2 = this.mc.objectMouseOver.hitVec.distanceTo(vec3D);
        }
        if (this.mc.playerController instanceof PlayerControllerTest) {
            d = 32.0;
            d2 = 32.0;
        } else {
            if (d2 > 3.0) {
                d2 = 3.0;
            }
            d = d2;
        }
        Vec3D vec3D2 = this.mc.renderViewEntity.getLook(tickDelta);
        Vec3D vec3D3 = vec3D.addVector(vec3D2.xCoord * d, vec3D2.yCoord * d, vec3D2.zCoord * d);
        this.pointedEntity = null;
        float f = 1.0f;
        List<Entity> list = this.mc.theWorld.getEntitiesWithinAABBExcludingEntity(this.mc.renderViewEntity, this.mc.renderViewEntity.boundingBox.addCoord(vec3D2.xCoord * d, vec3D2.yCoord * d, vec3D2.zCoord * d).expand(f, f, f));
        double d3 = 0.0;
        for (int i = 0; i < list.size(); ++i) {
            double d4;
            Entity entity = list.get(i);
            if (!entity.canBeCollidedWith()) continue;
            float f2 = entity.getCollisionBorderSize();
            AxisAlignedBB axisAlignedBB = entity.boundingBox.expand(f2, f2, f2);
            MovingObjectPosition movingObjectPosition = axisAlignedBB.clip(vec3D, vec3D3);
            if (axisAlignedBB.isVecInside(vec3D)) {
                if (!(0.0 < d3) && d3 != 0.0) continue;
                this.pointedEntity = entity;
                d3 = 0.0;
                continue;
            }
            if (movingObjectPosition == null || !((d4 = vec3D.distanceTo(movingObjectPosition.hitVec)) < d3) && d3 != 0.0) continue;
            this.pointedEntity = entity;
            d3 = d4;
        }
        if (this.pointedEntity != null && !(this.mc.playerController instanceof PlayerControllerTest)) {
            this.mc.objectMouseOver = new MovingObjectPosition(this.pointedEntity);
        }
    }

    private float getFOVModifier(float tickDelta) {
        EntityLiving entityLiving = this.mc.renderViewEntity;
        float f = 70.0f;
        if (entityLiving.isInsideOfMaterial(Material.water)) {
            f = 60.0f;
        }
        if (entityLiving.health <= 0) {
            float f2 = (float)entityLiving.deathTime + tickDelta;
            f /= (1.0f - 500.0f / (f2 + 500.0f)) * 2.0f + 1.0f;
        }
        return f + this.newFov + (this.oldFov - this.newFov) * tickDelta;
    }

    private void hurtCameraEffect(float tickDelta) {
        float f;
        EntityLiving entityLiving = this.mc.renderViewEntity;
        float f2 = (float)entityLiving.hurtTime - tickDelta;
        if (entityLiving.health <= 0) {
            f = (float)entityLiving.deathTime + tickDelta;
            GL11.glRotatef(40.0f - 8000.0f / (f + 200.0f), 0.0f, 0.0f, 1.0f);
        }
        if (f2 < 0.0f) {
            return;
        }
        f2 /= (float)entityLiving.maxHurtTime;
        f2 = MathHelper.sin(f2 * f2 * f2 * f2 * (float)Math.PI);
        f = entityLiving.attackedAtYaw;
        GL11.glRotatef(-f, 0.0f, 1.0f, 0.0f);
        GL11.glRotatef(-f2 * 14.0f, 0.0f, 0.0f, 1.0f);
        GL11.glRotatef(f, 0.0f, 1.0f, 0.0f);
    }

    private void setupViewBobbing(float tickDelta) {
        if (!(this.mc.renderViewEntity instanceof EntityPlayer)) {
            return;
        }
        EntityPlayer entityPlayer = (EntityPlayer)this.mc.renderViewEntity;
        float f = entityPlayer.distanceWalkedModified - entityPlayer.prevDistanceWalkedModified;
        float f2 = -(entityPlayer.distanceWalkedModified + f * tickDelta);
        float f3 = entityPlayer.lastBob + (entityPlayer.bob - entityPlayer.lastBob) * tickDelta;
        float f4 = entityPlayer.cameraPitch + (entityPlayer.tilt - entityPlayer.cameraPitch) * tickDelta;
        GL11.glTranslatef(MathHelper.sin(f2 * (float)Math.PI) * f3 * 0.5f, -Math.abs(MathHelper.cos(f2 * (float)Math.PI) * f3), 0.0f);
        GL11.glRotatef(MathHelper.sin(f2 * (float)Math.PI) * f3 * 3.0f, 0.0f, 0.0f, 1.0f);
        GL11.glRotatef(Math.abs(MathHelper.cos(f2 * (float)Math.PI - 0.2f) * f3) * 5.0f, 1.0f, 0.0f, 0.0f);
        GL11.glRotatef(f4, 1.0f, 0.0f, 0.0f);
    }

    private void orientCamera(float tickDelta) {
        EntityLiving entityliving = this.mc.renderViewEntity;
        float f = entityliving.yOffset - 1.62F;
        double d0 = entityliving.prevPosX + (entityliving.posX - entityliving.prevPosX) * tickDelta;
        double d1 = entityliving.prevPosY + (entityliving.posY - entityliving.prevPosY) * tickDelta - f;
        double d2 = entityliving.prevPosZ + (entityliving.posZ - entityliving.prevPosZ) * tickDelta;
        GL11.glRotatef(this.newCamRoll + (this.oldCamRoll - this.newCamRoll) * tickDelta, 0.0F, 0.0F, 1.0F);
        if (entityliving.isPlayerSleeping()) {
            f = (float) (f + 1.0);
            GL11.glTranslatef(0.0F, 0.3F, 0.0F);
            if (!this.mc.gameSettings.debugCamera) {
                int i = this.mc.theWorld.getBlockId(MathHelper.floor_double(entityliving.posX), MathHelper.floor_double(entityliving.posY), MathHelper.floor_double(entityliving.posZ));
                if (i == Block.blockBed.blockID) {
                    int j = this.mc.theWorld.getBlockMetadata(MathHelper.floor_double(entityliving.posX), MathHelper.floor_double(entityliving.posY), MathHelper.floor_double(entityliving.posZ));
                    int k = j & 3;
                    GL11.glRotatef(k * 90, 0.0F, 1.0F, 0.0F);
                }
                GL11.glRotatef(entityliving.prevRotationYaw + (entityliving.rotationYaw - entityliving.prevRotationYaw) * tickDelta + 180.0F, 0.0F, -1.0F, 0.0F);
                GL11.glRotatef(entityliving.prevRotationPitch + (entityliving.rotationPitch - entityliving.prevRotationPitch) * tickDelta, -1.0F, 0.0F, 0.0F);
            }
        } else if (this.mc.gameSettings.thirdPersonView) {
            double d7 = this.newCamPos + (this.oldCamPos - this.newCamPos) * tickDelta;
            if (this.mc.gameSettings.debugCamera) {
                float f5 = this.newCamPitch + (this.oldCamPitch - this.newCamPitch) * tickDelta;
                float f1 = this.newCamYaw + (this.oldCamYaw - this.newCamYaw) * tickDelta;
                GL11.glTranslatef(0.0F, 0.0F, (float) (-d7));
                GL11.glRotatef(f1, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(f5, 0.0F, 1.0F, 0.0F);
            } else {
                float f6 = entityliving.rotationYaw;
                float f7 = entityliving.rotationPitch;
                double d3 = -MathHelper.sin(f6 / 180.0F * (float) Math.PI) * MathHelper.cos(f7 / 180.0F * (float) Math.PI) * d7;
                double d4 = MathHelper.cos(f6 / 180.0F * (float) Math.PI) * MathHelper.cos(f7 / 180.0F * (float) Math.PI) * d7;
                double d5 = -MathHelper.sin(f7 / 180.0F * (float) Math.PI) * d7;
                for (int l = 0; l < 8; l++) {
                    float f2 = (l & 1) * 2 - 1;
                    float f3 = (l >> 1 & 1) * 2 - 1;
                    float f4 = (l >> 2 & 1) * 2 - 1;
                    f2 *= 0.1F;
                    f3 *= 0.1F;
                    f4 *= 0.1F;
                    MovingObjectPosition movingobjectposition = this.mc.theWorld.rayTraceBlocks(Vec3D.createVector(d0 + f2, d1 + f3, d2 + f4), Vec3D.createVector(d0 - d3 + f2 + f4, d1 - d5 + f3, d2 - d4 + f4));
                    if (movingobjectposition != null) {
                        double d6 = movingobjectposition.hitVec.distanceTo(Vec3D.createVector(d0, d1, d2));
                        if (d6 < d7) {
                            d7 = d6;
                        }
                    }
                }
                GL11.glRotatef(entityliving.rotationPitch - f7, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(entityliving.rotationYaw - f6, 0.0F, 1.0F, 0.0F);
                GL11.glTranslatef(0.0F, 0.0F, (float) (-d7));
                GL11.glRotatef(f6 - entityliving.rotationYaw, 0.0F, 1.0F, 0.0F);
                GL11.glRotatef(f7 - entityliving.rotationPitch, 1.0F, 0.0F, 0.0F);
            }
        } else {
            GL11.glTranslatef(0.0F, 0.0F, -0.1F);
        }
        if (!this.mc.gameSettings.debugCamera) {
            GL11.glRotatef(entityliving.prevRotationPitch + (entityliving.rotationPitch - entityliving.prevRotationPitch) * tickDelta, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(entityliving.prevRotationYaw + (entityliving.rotationYaw - entityliving.prevRotationYaw) * tickDelta + 180.0F, 0.0F, 1.0F, 0.0F);
        }
        GL11.glTranslatef(0.0F, f, 0.0F);
        d0 = entityliving.prevPosX + (entityliving.posX - entityliving.prevPosX) * tickDelta;
        d1 = entityliving.prevPosY + (entityliving.posY - entityliving.prevPosY) * tickDelta - f;
        d2 = entityliving.prevPosZ + (entityliving.posZ - entityliving.prevPosZ) * tickDelta;
        this.cloudFog = this.mc.renderGlobal.hasThiccFog(d0, d1, d2, tickDelta);
    }

    private void setupCameraTransform(float tickDelta, int anaglyphRenderPass) {
        this.farPlaneDistance = 256 >> this.mc.gameSettings.renderDistance;
        GL11.glMatrixMode(5889);
        GL11.glLoadIdentity();
        float f = 0.07F;
        if (this.mc.gameSettings.anaglyph) {
            GL11.glTranslatef(-(anaglyphRenderPass * 2 - 1) * f, 0.0F, 0.0F);
        }
        if (this.cameraZoom != 1.0) {
            GL11.glTranslatef((float) this.cameraYaw, (float) (-this.cameraPitch), 0.0F);
            GL11.glScaled(this.cameraZoom, this.cameraZoom, 1.0);
            GLU.gluPerspective(this.getFOVModifier(tickDelta), (float) this.mc.displayWidth / this.mc.displayHeight, 0.05F, this.farPlaneDistance * 2.0F);
        } else {
            GLU.gluPerspective(this.getFOVModifier(tickDelta), (float) this.mc.displayWidth / this.mc.displayHeight, 0.05F, this.farPlaneDistance * 2.0F);
        }
        GL11.glMatrixMode(5888);
        GL11.glLoadIdentity();
        if (this.mc.gameSettings.anaglyph) {
            GL11.glTranslatef((anaglyphRenderPass * 2 - 1) * 0.1F, 0.0F, 0.0F);
        }
        this.hurtCameraEffect(tickDelta);
        if (this.mc.gameSettings.viewBobbing) {
            this.setupViewBobbing(tickDelta);
        }
        float f1 = this.mc.thePlayer.prevTimeInPortal + (this.mc.thePlayer.timeInPortal - this.mc.thePlayer.prevTimeInPortal) * tickDelta;
        if (f1 > 0.0F) {
            float f2 = 5.0F / (f1 * f1 + 5.0F) - f1 * 0.04F;
            f2 *= f2;
            GL11.glRotatef((this.rendererUpdateCount + tickDelta) * 20.0F, 0.0F, 1.0F, 1.0F);
            GL11.glScalef(1.0F / f2, 1.0F, 1.0F);
            GL11.glRotatef(-(this.rendererUpdateCount + tickDelta) * 20.0F, 0.0F, 1.0F, 1.0F);
        }
        this.orientCamera(tickDelta);
    }

    private void renderItemInHand(float tickDelta, int anaglyphRenderPass) {
        GL11.glLoadIdentity();
        if (this.mc.gameSettings.anaglyph) {
            GL11.glTranslatef((float)(anaglyphRenderPass * 2 - 1) * 0.1f, 0.0f, 0.0f);
        }
        GL11.glPushMatrix();
        this.hurtCameraEffect(tickDelta);
        if (this.mc.gameSettings.viewBobbing) {
            this.setupViewBobbing(tickDelta);
        }
        if (!(this.mc.gameSettings.thirdPersonView || this.mc.renderViewEntity.isPlayerSleeping() || this.mc.gameSettings.hideGUI)) {
            this.itemRenderer.renderItemInFirstPerson(tickDelta);
        }
        GL11.glPopMatrix();
        if (!this.mc.gameSettings.thirdPersonView && !this.mc.renderViewEntity.isPlayerSleeping()) {
            this.itemRenderer.renderOverlays(tickDelta);
            this.hurtCameraEffect(tickDelta);
        }
        if (this.mc.gameSettings.viewBobbing) {
            this.setupViewBobbing(tickDelta);
        }
    }

    public void updateCameraAndRender(final float tickDelta) {
        if (!Display.isActive()) {
            if (System.currentTimeMillis() - this.prevFrameTime > 500L) {
                this.mc.displayInGameMenu();
            }
        } else {
            this.prevFrameTime = System.currentTimeMillis();
        }
        if (this.mc.inGameHasFocus) {
            this.mc.mouseHelper.mouseXYChange();
            final float n = this.mc.gameSettings.mouseSensitivity * 0.6f + 0.2f;
            final float n2 = n * n * n * 8.0f;
            float smooth = this.mc.mouseHelper.deltaX * n2;
            float smooth2 = this.mc.mouseHelper.deltaY * n2;
            int n3 = 1;
            if (this.mc.gameSettings.invertMouse) {
                n3 = -1;
            }
            if (this.mc.gameSettings.smoothCamera) {
                smooth = this.mouseFilterXAxis.smooth(smooth, 0.05f * n2);
                smooth2 = this.mouseFilterYAxis.smooth(smooth2, 0.05f * n2);
            }
            this.mc.thePlayer.updateLocalPlayerCamera(smooth, smooth2 * n3);
        }
        if (this.mc.skipRenderWorld) {
            return;
        }
        EntityRenderer.anaglyphEnabled = this.mc.gameSettings.anaglyph;
        final ScaledResolution scaledResolution = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
        final int scaledWidth = scaledResolution.getScaledWidth();
        final int scaledHeight = scaledResolution.getScaledHeight();
        final int n4 = Mouse.getX() * scaledWidth / this.mc.displayWidth;
        final int n5 = scaledHeight - Mouse.getY() * scaledHeight / this.mc.displayHeight - 1;
        int n6 = 200;
        if (this.mc.gameSettings.limitFramerate == 1) {
            n6 = 120;
        }
        if (this.mc.gameSettings.limitFramerate == 2) {
            n6 = 40;
        }
        if (this.mc.theWorld != null) {
            if (this.mc.gameSettings.limitFramerate == 0) {
                this.renderWorld(tickDelta, 0L);
            } else {
                this.renderWorld(tickDelta, this.lastWorldRenderTime + 1000000000 / n6);
            }
            if (this.mc.gameSettings.limitFramerate == 2) {
                final long millis = (this.lastWorldRenderTime + 1000000000 / n6 - System.nanoTime()) / 1000000L;
                if (millis > 0L && millis < 500L) {
                    try {
                        Thread.sleep(millis);
                    } catch (final InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }
            this.lastWorldRenderTime = System.nanoTime();
            if (!this.mc.gameSettings.hideGUI || this.mc.currentScreen != null) {
                this.mc.ingameGUI.renderGameOverlay(tickDelta, this.mc.currentScreen != null, n4, n5);
            }
        } else {
            GL11.glViewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
            GL11.glMatrixMode(5889);
            GL11.glLoadIdentity();
            GL11.glMatrixMode(5888);
            GL11.glLoadIdentity();
            this.setupGuiState();
            if (this.mc.gameSettings.limitFramerate == 2) {
                long millis2 = (this.lastWorldRenderTime + 1000000000 / n6 - System.nanoTime()) / 1000000L;
                if (millis2 < 0L) {
                    millis2 += 10L;
                }
                if (millis2 > 0L && millis2 < 500L) {
                    try {
                        Thread.sleep(millis2);
                    } catch (final InterruptedException ex2) {
                        ex2.printStackTrace();
                    }
                }
            }
            this.lastWorldRenderTime = System.nanoTime();
        }
        if (this.mc.currentScreen != null) {
            GL11.glClear(256);
            this.mc.currentScreen.drawScreen(n4, n5, tickDelta);
            if (this.mc.currentScreen != null && this.mc.currentScreen.particlesGui != null) {
                this.mc.currentScreen.particlesGui.render(tickDelta);
            }
        }
    }

    public void renderWorld(float tickDelta, long renderTimeLimit) {
        int n;
        GL11.glEnable(2884);
        GL11.glEnable(2929);
        if (this.mc.renderViewEntity == null) {
            this.mc.renderViewEntity = this.mc.thePlayer;
        }
        this.getMouseOver(tickDelta);
        EntityLiving entityLiving = this.mc.renderViewEntity;
        RenderGlobal renderGlobal = this.mc.renderGlobal;
        EffectRenderer effectRenderer = this.mc.effectRenderer;
        double d = entityLiving.lastTickPosX + (entityLiving.posX - entityLiving.lastTickPosX) * (double)tickDelta;
        double d2 = entityLiving.lastTickPosY + (entityLiving.posY - entityLiving.lastTickPosY) * (double)tickDelta;
        double d3 = entityLiving.lastTickPosZ + (entityLiving.posZ - entityLiving.lastTickPosZ) * (double)tickDelta;
        IChunkProvider iChunkProvider = this.mc.theWorld.getIChunkProvider();
        if (iChunkProvider instanceof ChunkProviderLoadOrGenerate) {
            ChunkProviderLoadOrGenerate chunkProviderLoadOrGenerate = (ChunkProviderLoadOrGenerate)iChunkProvider;
            int n2 = MathHelper.floor_float((int)d) >> 4;
            n = MathHelper.floor_float((int)d3) >> 4;
            chunkProviderLoadOrGenerate.setCurrentChunkOver(n2, n);
        }
        for (int i = 0; i < 2; ++i) {
            if (this.mc.gameSettings.anaglyph) {
                anaglyphField = i;
                if (anaglyphField == 0) {
                    GL11.glColorMask(false, true, true, false);
                } else {
                    GL11.glColorMask(true, false, false, false);
                }
            }
            GL11.glViewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
            this.updateFogColor(tickDelta);
            GL11.glClear(16640);
            GL11.glEnable(2884);
            this.setupCameraTransform(tickDelta, i);
            ClippingHelperImpl.getInstance();
            if (this.mc.gameSettings.renderDistance < 2) {
                this.setupFog(-1, tickDelta);
                renderGlobal.renderSky(tickDelta);
            }
            GL11.glEnable(2912);
            this.setupFog(1, tickDelta);
            if (this.mc.gameSettings.ambientOcclusion) {
                GL11.glShadeModel(7425);
            }
            Frustrum frustrum = new Frustrum();
            frustrum.setPosition(d, d2, d3);
            this.mc.renderGlobal.clipRenderersByFrustrum(frustrum, tickDelta);
            if (i == 0) {
                long l;
                while (!this.mc.renderGlobal.updateRenderers(entityLiving, false) && renderTimeLimit != 0L && (l = renderTimeLimit - System.nanoTime()) >= 0L && l <= 1000000000L) {
                }
            }
            this.setupFog(0, tickDelta);
            GL11.glEnable(2912);
            GL11.glBindTexture(3553, this.mc.renderEngine.getTexture("/terrain.png"));
            RenderHelper.disableStandardItemLighting();
            renderGlobal.sortAndRender(entityLiving, 0, tickDelta);
            GL11.glShadeModel(7424);
            RenderHelper.enableStandardItemLighting();
            renderGlobal.renderEntities(entityLiving.getPosition(tickDelta), frustrum, tickDelta);
            effectRenderer.renderLit(entityLiving, tickDelta);
            RenderHelper.disableStandardItemLighting();
            this.setupFog(0, tickDelta);
            effectRenderer.renderParticles(entityLiving, tickDelta);
            if (this.mc.objectMouseOver != null && entityLiving.isInsideOfMaterial(Material.water) && entityLiving instanceof EntityPlayer) {
                EntityPlayer entityPlayer = (EntityPlayer)entityLiving;
                GL11.glDisable(3008);
                renderGlobal.drawBlockBreaking(entityPlayer, this.mc.objectMouseOver, 0, entityPlayer.inventory.getCurrentItem(), tickDelta);
                renderGlobal.drawSelectionBox(entityPlayer, this.mc.objectMouseOver, 0, entityPlayer.inventory.getCurrentItem(), tickDelta);
                GL11.glEnable(3008);
            }
            GL11.glBlendFunc(770, 771);
            this.setupFog(0, tickDelta);
            GL11.glEnable(3042);
            GL11.glDisable(2884);
            GL11.glBindTexture(3553, this.mc.renderEngine.getTexture("/terrain.png"));
            if (this.mc.gameSettings.fancyGraphics) {
                if (this.mc.gameSettings.ambientOcclusion) {
                    GL11.glShadeModel(7425);
                }
                GL11.glColorMask(false, false, false, false);
                n = renderGlobal.sortAndRender(entityLiving, 1, tickDelta);
                if (this.mc.gameSettings.anaglyph) {
                    if (anaglyphField == 0) {
                        GL11.glColorMask(false, true, true, true);
                    } else {
                        GL11.glColorMask(true, false, false, true);
                    }
                } else {
                    GL11.glColorMask(true, true, true, true);
                }
                if (n > 0) {
                    renderGlobal.renderAllRenderLists(1, tickDelta);
                }
                GL11.glShadeModel(7424);
            } else {
                renderGlobal.sortAndRender(entityLiving, 1, tickDelta);
            }
            GL11.glDepthMask(true);
            GL11.glEnable(2884);
            GL11.glDisable(3042);
            if (this.cameraZoom == 1.0 && entityLiving instanceof EntityPlayer && this.mc.objectMouseOver != null && !entityLiving.isInsideOfMaterial(Material.water)) {
                EntityPlayer entityPlayer = (EntityPlayer)entityLiving;
                GL11.glDisable(3008);
                renderGlobal.drawBlockBreaking(entityPlayer, this.mc.objectMouseOver, 0, entityPlayer.inventory.getCurrentItem(), tickDelta);
                renderGlobal.drawSelectionBox(entityPlayer, this.mc.objectMouseOver, 0, entityPlayer.inventory.getCurrentItem(), tickDelta);
                GL11.glEnable(3008);
            }
            this.renderRainSnow(tickDelta);
            GL11.glDisable(2912);
            if (this.pointedEntity != null) {
                // empty if block
            }
            this.setupFog(0, tickDelta);
            GL11.glEnable(2912);
            renderGlobal.renderClouds(tickDelta);
            GL11.glDisable(2912);
            this.setupFog(1, tickDelta);
            if (this.cameraZoom == 1.0) {
                GL11.glClear(256);
                this.renderItemInHand(tickDelta, i);
            }
            if (this.mc.gameSettings.anaglyph) continue;
            return;
        }
        GL11.glColorMask(true, true, true, false);
    }

    private void addRainParticles() {
        float f = this.mc.theWorld.getRain(1.0f);
        if (!this.mc.gameSettings.fancyGraphics) {
            f /= 2.0f;
        }
        if (f == 0.0f) {
            return;
        }
        this.random.setSeed((long)this.rendererUpdateCount * 312987231L);
        EntityLiving entityLiving = this.mc.renderViewEntity;
        World world = this.mc.theWorld;
        int n = MathHelper.floor_double(entityLiving.posX);
        int n2 = MathHelper.floor_double(entityLiving.posY);
        int n3 = MathHelper.floor_double(entityLiving.posZ);
        int n4 = 10;
        double d = 0.0;
        double d2 = 0.0;
        double d3 = 0.0;
        int n5 = 0;
        for (int i = 0; i < (int)(100.0f * f * f); ++i) {
            int n6 = n + this.random.nextInt(n4) - this.random.nextInt(n4);
            int n7 = n3 + this.random.nextInt(n4) - this.random.nextInt(n4);
            int n8 = world.findTopSolidBlock(n6, n7);
            int n9 = world.getBlockId(n6, n8 - 1, n7);
            if (n8 > n2 + n4 || n8 < n2 - n4 || !world.getWorldChunkManager().getBiomeGenAt(n6, n7).canSpawnLightningBolt()) continue;
            float f2 = this.random.nextFloat();
            float f3 = this.random.nextFloat();
            if (n9 <= 0) continue;
            if (Block.blocksList[n9].blockMaterial == Material.lava) {
                this.mc.effectRenderer.addEffect(new EntitySmokeFX(world, (float)n6 + f2, (double)((float)n8 + 0.1f) - Block.blocksList[n9].minY, (float)n7 + f3, 0.0, 0.0, 0.0));
                continue;
            }
            if (this.random.nextInt(++n5) == 0) {
                d = (float)n6 + f2;
                d2 = (double)((float)n8 + 0.1f) - Block.blocksList[n9].minY;
                d3 = (float)n7 + f3;
            }
            this.mc.effectRenderer.addEffect(new EntityRainFX(world, (float)n6 + f2, (double)((float)n8 + 0.1f) - Block.blocksList[n9].minY, (float)n7 + f3));
        }
        if (n5 > 0 && this.random.nextInt(3) < this.rainSoundCounter++) {
            this.rainSoundCounter = 0;
            if (d2 > entityLiving.posY + 1.0 && world.findTopSolidBlock(MathHelper.floor_double(entityLiving.posX), MathHelper.floor_double(entityLiving.posZ)) > MathHelper.floor_double(entityLiving.posY)) {
                this.mc.theWorld.playSoundEffect(d, d2, d3, "ambient.weather.rain", 0.1f, 0.5f);
            } else {
                this.mc.theWorld.playSoundEffect(d, d2, d3, "ambient.weather.rain", 0.2f, 1.0f);
            }
        }
    }

    protected void renderRainSnow(float tickDelta) {
        float f;
        int n;
        int n2;
        int n3;
        BiomeGenBase biomeGenBase;
        int n4;
        int n5;
        float f2 = this.mc.theWorld.getRain(tickDelta);
        if (f2 <= 0.0f) {
            return;
        }
        EntityLiving entityLiving = this.mc.renderViewEntity;
        World world = this.mc.theWorld;
        int n6 = MathHelper.floor_double(entityLiving.posX);
        int n7 = MathHelper.floor_double(entityLiving.posY);
        int n8 = MathHelper.floor_double(entityLiving.posZ);
        Tessellator tessellator = Tessellator.instance;
        GL11.glDisable(2884);
        GL11.glNormal3f(0.0f, 1.0f, 0.0f);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glAlphaFunc(516, 0.01f);
        GL11.glBindTexture(3553, this.mc.renderEngine.getTexture("/environment/snow.png"));
        double d = entityLiving.lastTickPosX + (entityLiving.posX - entityLiving.lastTickPosX) * (double)tickDelta;
        double d2 = entityLiving.lastTickPosY + (entityLiving.posY - entityLiving.lastTickPosY) * (double)tickDelta;
        double d3 = entityLiving.lastTickPosZ + (entityLiving.posZ - entityLiving.lastTickPosZ) * (double)tickDelta;
        int n9 = MathHelper.floor_double(d2);
        int n10 = 5;
        if (this.mc.gameSettings.fancyGraphics) {
            n10 = 10;
        }
        BiomeGenBase[] biomeGenBaseArray = world.getWorldChunkManager().getBiomes(n6 - n10, n8 - n10, n10 * 2 + 1, n10 * 2 + 1);
        int n11 = 0;
        for (n5 = n6 - n10; n5 <= n6 + n10; ++n5) {
            for (n4 = n8 - n10; n4 <= n8 + n10; ++n4) {
                if (!(biomeGenBase = biomeGenBaseArray[n11++]).getEnableSnow()) continue;
                n3 = world.findTopSolidBlock(n5, n4);
                if (n3 < 0) {
                    n3 = 0;
                }
                if ((n2 = n3) < n9) {
                    n2 = n9;
                }
                n = n7 - n10;
                int n12 = n7 + n10;
                if (n < n3) {
                    n = n3;
                }
                if (n12 < n3) {
                    n12 = n3;
                }
                f = 1.0f;
                if (n == n12) continue;
                this.random.setSeed(n5 * n5 * 3121 + n5 * 45238971 + n4 * n4 * 418711 + n4 * 13761);
                float f3 = (float)this.rendererUpdateCount + tickDelta;
                float f4 = ((float)(this.rendererUpdateCount & 0x1FF) + tickDelta) / 512.0f;
                float f5 = this.random.nextFloat() + f3 * 0.01f * (float)this.random.nextGaussian();
                float f6 = this.random.nextFloat() + f3 * (float)this.random.nextGaussian() * 0.001f;
                double d4 = (double)((float)n5 + 0.5f) - entityLiving.posX;
                double d5 = (double)((float)n4 + 0.5f) - entityLiving.posZ;
                float f7 = MathHelper.sqrt_double(d4 * d4 + d5 * d5) / (float)n10;
                tessellator.startDrawingQuads();
                float f8 = world.getLightBrightness(n5, n2, n4);
                GL11.glColor4f(f8, f8, f8, ((1.0f - f7 * f7) * 0.3f + 0.5f) * f2);
                tessellator.setTranslationD(-d * 1.0, -d2 * 1.0, -d3 * 1.0);
                tessellator.addVertexWithUV(n5 + 0, n, (double)n4 + 0.5, 0.0f * f + f5, (float)n * f / 4.0f + f4 * f + f6);
                tessellator.addVertexWithUV(n5 + 1, n, (double)n4 + 0.5, 1.0f * f + f5, (float)n * f / 4.0f + f4 * f + f6);
                tessellator.addVertexWithUV(n5 + 1, n12, (double)n4 + 0.5, 1.0f * f + f5, (float)n12 * f / 4.0f + f4 * f + f6);
                tessellator.addVertexWithUV(n5 + 0, n12, (double)n4 + 0.5, 0.0f * f + f5, (float)n12 * f / 4.0f + f4 * f + f6);
                tessellator.addVertexWithUV((double)n5 + 0.5, n, n4 + 0, 0.0f * f + f5, (float)n * f / 4.0f + f4 * f + f6);
                tessellator.addVertexWithUV((double)n5 + 0.5, n, n4 + 1, 1.0f * f + f5, (float)n * f / 4.0f + f4 * f + f6);
                tessellator.addVertexWithUV((double)n5 + 0.5, n12, n4 + 1, 1.0f * f + f5, (float)n12 * f / 4.0f + f4 * f + f6);
                tessellator.addVertexWithUV((double)n5 + 0.5, n12, n4 + 0, 0.0f * f + f5, (float)n12 * f / 4.0f + f4 * f + f6);
                tessellator.setTranslationD(0.0, 0.0, 0.0);
                tessellator.draw();
            }
        }
        GL11.glBindTexture(3553, this.mc.renderEngine.getTexture("/environment/rain.png"));
        if (this.mc.gameSettings.fancyGraphics) {
            n10 = 10;
        }
        n11 = 0;
        for (n5 = n6 - n10; n5 <= n6 + n10; ++n5) {
            for (n4 = n8 - n10; n4 <= n8 + n10; ++n4) {
                if (!(biomeGenBase = biomeGenBaseArray[n11++]).canSpawnLightningBolt()) continue;
                n3 = world.findTopSolidBlock(n5, n4);
                n2 = n7 - n10;
                n = n7 + n10;
                if (n2 < n3) {
                    n2 = n3;
                }
                if (n < n3) {
                    n = n3;
                }
                float f9 = 1.0f;
                if (n2 == n) continue;
                this.random.setSeed(n5 * n5 * 3121 + n5 * 45238971 + n4 * n4 * 418711 + n4 * 13761);
                f = ((float)(this.rendererUpdateCount + n5 * n5 * 3121 + n5 * 45238971 + n4 * n4 * 418711 + n4 * 13761 & 0x1F) + tickDelta) / 32.0f * (3.0f + this.random.nextFloat());
                double d6 = (double)((float)n5 + 0.5f) - entityLiving.posX;
                double d7 = (double)((float)n4 + 0.5f) - entityLiving.posZ;
                float f10 = MathHelper.sqrt_double(d6 * d6 + d7 * d7) / (float)n10;
                tessellator.startDrawingQuads();
                float f11 = world.getLightBrightness(n5, 128, n4) * 0.85f + 0.15f;
                GL11.glColor4f(f11, f11, f11, ((1.0f - f10 * f10) * 0.5f + 0.5f) * f2);
                tessellator.setTranslationD(-d * 1.0, -d2 * 1.0, -d3 * 1.0);
                tessellator.addVertexWithUV(n5 + 0, n2, (double)n4 + 0.5, 0.0f * f9, (float)n2 * f9 / 4.0f + f * f9);
                tessellator.addVertexWithUV(n5 + 1, n2, (double)n4 + 0.5, 1.0f * f9, (float)n2 * f9 / 4.0f + f * f9);
                tessellator.addVertexWithUV(n5 + 1, n, (double)n4 + 0.5, 1.0f * f9, (float)n * f9 / 4.0f + f * f9);
                tessellator.addVertexWithUV(n5 + 0, n, (double)n4 + 0.5, 0.0f * f9, (float)n * f9 / 4.0f + f * f9);
                tessellator.addVertexWithUV((double)n5 + 0.5, n2, n4 + 0, 0.0f * f9, (float)n2 * f9 / 4.0f + f * f9);
                tessellator.addVertexWithUV((double)n5 + 0.5, n2, n4 + 1, 1.0f * f9, (float)n2 * f9 / 4.0f + f * f9);
                tessellator.addVertexWithUV((double)n5 + 0.5, n, n4 + 1, 1.0f * f9, (float)n * f9 / 4.0f + f * f9);
                tessellator.addVertexWithUV((double)n5 + 0.5, n, n4 + 0, 0.0f * f9, (float)n * f9 / 4.0f + f * f9);
                tessellator.setTranslationD(0.0, 0.0, 0.0);
                tessellator.draw();
            }
        }
        GL11.glEnable(2884);
        GL11.glDisable(3042);
        GL11.glAlphaFunc(516, 0.1f);
    }

    public void setupGuiState() {
        ScaledResolution scaledResolution = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
        GL11.glClear(256);
        GL11.glMatrixMode(5889);
        GL11.glLoadIdentity();
        GL11.glOrtho(0.0, scaledResolution.scaledWidthD, scaledResolution.scaledHeightD, 0.0, 1000.0, 3000.0);
        GL11.glMatrixMode(5888);
        GL11.glLoadIdentity();
        GL11.glTranslatef(0.0f, 0.0f, -2000.0f);
    }

    private void updateFogColor(float tickDelta) {
        float f;
        float f2;
        World world = this.mc.theWorld;
        EntityLiving entityLiving = this.mc.renderViewEntity;
        float f3 = 1.0f / (float)(4 - this.mc.gameSettings.renderDistance);
        f3 = 1.0f - (float)Math.pow(f3, 0.25);
        Vec3D vec3D = world.getSkyColor(this.mc.renderViewEntity, tickDelta);
        float f4 = (float)vec3D.xCoord;
        float f5 = (float)vec3D.yCoord;
        float f6 = (float)vec3D.zCoord;
        Vec3D vec3D2 = world.getFogColor(tickDelta);
        this.fogColorRed = (float)vec3D2.xCoord;
        this.fogColorGreen = (float)vec3D2.yCoord;
        this.fogColorBlue = (float)vec3D2.zCoord;
        this.fogColorRed += (f4 - this.fogColorRed) * f3;
        this.fogColorGreen += (f5 - this.fogColorGreen) * f3;
        this.fogColorBlue += (f6 - this.fogColorBlue) * f3;
        float f7 = world.getRain(tickDelta);
        if (f7 > 0.0f) {
            f2 = 1.0f - f7 * 0.5f;
            f = 1.0f - f7 * 0.4f;
            this.fogColorRed *= f2;
            this.fogColorGreen *= f2;
            this.fogColorBlue *= f;
        }
        if ((f2 = world.getThunder(tickDelta)) > 0.0f) {
            f = 1.0f - f2 * 0.5f;
            this.fogColorRed *= f;
            this.fogColorGreen *= f;
            this.fogColorBlue *= f;
        }
        if (this.cloudFog) {
            Vec3D vec3D3 = world.getCloudColor(tickDelta);
            this.fogColorRed = (float)vec3D3.xCoord;
            this.fogColorGreen = (float)vec3D3.yCoord;
            this.fogColorBlue = (float)vec3D3.zCoord;
        } else if (entityLiving.isInsideOfMaterial(Material.water)) {
            this.fogColorRed = 0.02f;
            this.fogColorGreen = 0.02f;
            this.fogColorBlue = 0.2f;
        } else if (entityLiving.isInsideOfMaterial(Material.lava)) {
            this.fogColorRed = 0.6f;
            this.fogColorGreen = 0.1f;
            this.fogColorBlue = 0.0f;
        }
        float f8 = this.fogColor2 + (this.fogColor1 - this.fogColor2) * tickDelta;
        this.fogColorRed *= f8;
        this.fogColorGreen *= f8;
        this.fogColorBlue *= f8;
        if (this.mc.gameSettings.anaglyph) {
            float f9 = (this.fogColorRed * 30.0f + this.fogColorGreen * 59.0f + this.fogColorBlue * 11.0f) / 100.0f;
            float f10 = (this.fogColorRed * 30.0f + this.fogColorGreen * 70.0f) / 100.0f;
            float f11 = (this.fogColorRed * 30.0f + this.fogColorBlue * 70.0f) / 100.0f;
            this.fogColorRed = f9;
            this.fogColorGreen = f10;
            this.fogColorBlue = f11;
        }
        GL11.glClearColor(this.fogColorRed, this.fogColorGreen, this.fogColorBlue, 0.0f);
    }

    private void setupFog(int mode, float tickDelta) {
        EntityLiving entityLiving = this.mc.renderViewEntity;
        GL11.glFog(2918, this.updateColorBuffer(this.fogColorRed, this.fogColorGreen, this.fogColorBlue, 1.0f));
        GL11.glNormal3f(0.0f, -1.0f, 0.0f);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        if (this.cloudFog) {
            GL11.glFogi(2917, 2048);
            GL11.glFogf(2914, 0.1f);
            float f = 1.0f;
            float f2 = 1.0f;
            float f3 = 1.0f;
            if (this.mc.gameSettings.anaglyph) {
                float f4 = (f * 30.0f + f2 * 59.0f + f3 * 11.0f) / 100.0f;
                float f5 = (f * 30.0f + f2 * 70.0f) / 100.0f;
                float f6 = (f * 30.0f + f3 * 70.0f) / 100.0f;
                f = f4;
                f2 = f5;
                f3 = f6;
            }
        } else if (entityLiving.isInsideOfMaterial(Material.water)) {
            GL11.glFogi(2917, 2048);
            GL11.glFogf(2914, 0.1f);
            float f = 0.4f;
            float f7 = 0.4f;
            float f8 = 0.9f;
            if (this.mc.gameSettings.anaglyph) {
                float f9 = (f * 30.0f + f7 * 59.0f + f8 * 11.0f) / 100.0f;
                float f10 = (f * 30.0f + f7 * 70.0f) / 100.0f;
                float f11 = (f * 30.0f + f8 * 70.0f) / 100.0f;
                f = f9;
                f7 = f10;
                f8 = f11;
            }
        } else if (entityLiving.isInsideOfMaterial(Material.lava)) {
            GL11.glFogi(2917, 2048);
            GL11.glFogf(2914, 2.0f);
            float f = 0.4f;
            float f12 = 0.3f;
            float f13 = 0.3f;
            if (this.mc.gameSettings.anaglyph) {
                float f14 = (f * 30.0f + f12 * 59.0f + f13 * 11.0f) / 100.0f;
                float f15 = (f * 30.0f + f12 * 70.0f) / 100.0f;
                float f16 = (f * 30.0f + f13 * 70.0f) / 100.0f;
                f = f14;
                f12 = f15;
                f13 = f16;
            }
        } else {
            GL11.glFogi(2917, 9729);
            GL11.glFogf(2915, this.farPlaneDistance * 0.25f);
            GL11.glFogf(2916, this.farPlaneDistance);
            if (mode < 0) {
                GL11.glFogf(2915, 0.0f);
                GL11.glFogf(2916, this.farPlaneDistance * 0.8f);
            }
            if (GLContext.getCapabilities().GL_NV_fog_distance) {
                GL11.glFogi(34138, 34139);
            }
            if (this.mc.theWorld.worldProvider.isNether) {
                GL11.glFogf(2915, 0.0f);
            }
        }
        GL11.glEnable(2903);
        GL11.glColorMaterial(1028, 4608);
    }

    private FloatBuffer updateColorBuffer(float f1, float f2, float f3, float f4) {
        this.fogColorBuffer.clear();
        this.fogColorBuffer.put(f1).put(f2).put(f3).put(f4);
        this.fogColorBuffer.flip();
        return this.fogColorBuffer;
    }
}

