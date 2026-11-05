package net.minecraft.src;

import java.util.HashMap;
import java.util.Map;
import org.lwjgl.opengl.GL11;

public class TileEntityRenderer {
    private Map<Class<? extends TileEntity>, TileEntitySpecialRenderer<? extends TileEntity>> specialRendererMap = new HashMap<>();
    public static TileEntityRenderer instance = new TileEntityRenderer();
    private FontRenderer fontRenderer;
    public static double staticPlayerX;
    public static double staticPlayerY;
    public static double staticPlayerZ;
    public RenderEngine renderEngine;
    public World worldObj;
    public EntityLiving entityLivingPlayer;
    public float playerYaw;
    public float playerPitch;
    public double playerX;
    public double playerY;
    public double playerZ;

    private TileEntityRenderer() {
        this.specialRendererMap.put(TileEntitySign.class, new TileEntitySignRenderer());
        this.specialRendererMap.put(TileEntityMobSpawner.class, new TileEntityMobSpawnerRenderer());
        this.specialRendererMap.put(TileEntityPiston.class, new TileEntityRendererPiston());

        for (TileEntitySpecialRenderer tileentityspecialrenderer : this.specialRendererMap.values()) {
            tileentityspecialrenderer.setTileEntityRenderer(this);
        }
    }

    public <T extends TileEntity> TileEntitySpecialRenderer<T> getSpecialRendererForClass(Class<? extends TileEntity> type) {
        TileEntitySpecialRenderer tileentityspecialrenderer = this.specialRendererMap.get(type);
        if (tileentityspecialrenderer == null && type != TileEntity.class) {
            tileentityspecialrenderer = this.getSpecialRendererForClass((Class<? extends TileEntity>)type.getSuperclass());
            this.specialRendererMap.put(type, tileentityspecialrenderer);
        }

        return tileentityspecialrenderer;
    }

    public boolean hasSpecialRenderer(TileEntity blockEntity) {
        return this.getSpecialRendererForEntity(blockEntity) != null;
    }

    public <T extends TileEntity> TileEntitySpecialRenderer<T> getSpecialRendererForEntity(TileEntity blockEntity) {
        return blockEntity == null ? null : this.<T>getSpecialRendererForClass((Class<? extends TileEntity>)blockEntity.getClass());
    }

    public void cacheActiveRenderInfo(World world, RenderEngine textureManager, FontRenderer textRenderer, EntityLiving camera, float tickDelta) {
        if (this.worldObj != world) {
            this.setWorld(world);
        }

        this.renderEngine = textureManager;
        this.entityLivingPlayer = camera;
        this.fontRenderer = textRenderer;
        this.playerYaw = camera.prevRotationYaw + (camera.rotationYaw - camera.prevRotationYaw) * tickDelta;
        this.playerPitch = camera.prevRotationPitch + (camera.rotationPitch - camera.prevRotationPitch) * tickDelta;
        this.playerX = camera.lastTickPosX + (camera.posX - camera.lastTickPosX) * tickDelta;
        this.playerY = camera.lastTickPosY + (camera.posY - camera.lastTickPosY) * tickDelta;
        this.playerZ = camera.lastTickPosZ + (camera.posZ - camera.lastTickPosZ) * tickDelta;
    }

    public void renderTileEntity(TileEntity blockEntity, float tickDelta) {
        if (blockEntity.getDistanceFrom(this.playerX, this.playerY, this.playerZ) < 4096.0) {
            float f = this.worldObj.getLightBrightness(blockEntity.xCoord, blockEntity.yCoord, blockEntity.zCoord);
            GL11.glColor3f(f, f, f);
            this.renderTileEntityAt(
                blockEntity, blockEntity.xCoord - staticPlayerX, blockEntity.yCoord - staticPlayerY, blockEntity.zCoord - staticPlayerZ, tickDelta
            );
        }
    }

    public void renderTileEntityAt(TileEntity blockEntity, double dx, double dy, double dz, float tickDelta) {
        TileEntitySpecialRenderer tileentityspecialrenderer = this.getSpecialRendererForEntity(blockEntity);
        if (tileentityspecialrenderer != null) {
            tileentityspecialrenderer.renderTileEntityAt(blockEntity, dx, dy, dz, tickDelta);
        }
    }

    public void setWorld(World world) {
        this.worldObj = world;

        for (TileEntitySpecialRenderer tileentityspecialrenderer : this.specialRendererMap.values()) {
            if (tileentityspecialrenderer != null) {
                tileentityspecialrenderer.setWorld(world);
            }
        }
    }

    public FontRenderer getFontRenderer() {
        return this.fontRenderer;
    }
}
