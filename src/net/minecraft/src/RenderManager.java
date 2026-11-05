package net.minecraft.src;

import java.util.HashMap;
import java.util.Map;
import org.lwjgl.opengl.GL11;

public class RenderManager {
    private Map<Class<? extends Entity>, Render<? extends Entity>> entityRenderMap = new HashMap<>();
    public static RenderManager instance = new RenderManager();
    private FontRenderer fontRenderer;
    public static double renderPosX;
    public static double renderPosY;
    public static double renderPosZ;
    public RenderEngine renderEngine;
    public ItemRenderer itemRenderer;
    public World worldObj;
    public EntityLiving livingPlayer;
    public float playerViewY;
    public float playerViewX;
    public GameSettings options;
    public double cameraX;
    public double cameraY;
    public double cameraZ;

    private RenderManager() {
        this.entityRenderMap.put(EntitySpider.class, new RenderSpider());
        this.entityRenderMap.put(EntityPig.class, new RenderPig(new ModelPig(), new ModelPig(0.5F), 0.7F));
        this.entityRenderMap.put(EntitySheep.class, new RenderSheep(new ModelSheep2(), new ModelSheep1(), 0.7F));
        this.entityRenderMap.put(EntityCow.class, new RenderCow(new ModelCow(), 0.7F));
        this.entityRenderMap.put(EntityWolf.class, new RenderWolf(new ModelWolf(), 0.5F));
        this.entityRenderMap.put(EntityChicken.class, new RenderChicken(new ModelChicken(), 0.3F));
        this.entityRenderMap.put(EntityCreeper.class, new RenderCreeper());
        this.entityRenderMap.put(EntitySkeleton.class, new RenderBiped(new ModelSkeleton(), 0.5F));
        this.entityRenderMap.put(EntityZombie.class, new RenderBiped(new ModelZombie(), 0.5F));
        this.entityRenderMap.put(EntitySlime.class, new RenderSlime(new ModelSlime(16), new ModelSlime(0), 0.25F));
        this.entityRenderMap.put(EntityPlayer.class, new RenderPlayer());
        this.entityRenderMap.put(EntityGiantZombie.class, new RenderGiantZombie(new ModelZombie(), 0.5F, 6.0F));
        this.entityRenderMap.put(EntityGhast.class, new RenderGhast());
        this.entityRenderMap.put(EntitySquid.class, new RenderSquid(new ModelSquid(), 0.7F));
        this.entityRenderMap.put(EntityLiving.class, new RenderLiving(new ModelBiped(), 0.5F));
        this.entityRenderMap.put(Entity.class, new RenderEntity());
        this.entityRenderMap.put(EntityPainting.class, new RenderPainting());
        this.entityRenderMap.put(EntityArrow.class, new RenderArrow());
        this.entityRenderMap.put(EntitySnowball.class, new RenderSnowball(Item.snowball.getIconFromDamage(0)));
        this.entityRenderMap.put(EntityEgg.class, new RenderSnowball(Item.egg.getIconFromDamage(0)));
        this.entityRenderMap.put(EntityFireball.class, new RenderFireball());
        this.entityRenderMap.put(EntityItem.class, new RenderItem());
        this.entityRenderMap.put(EntityTNTPrimed.class, new RenderTNTPrimed());
        this.entityRenderMap.put(EntityFallingSand.class, new RenderFallingSand());
        this.entityRenderMap.put(EntityMinecart.class, new RenderMinecart());
        this.entityRenderMap.put(EntityBoat.class, new RenderBoat());
        this.entityRenderMap.put(EntityFish.class, new RenderFish());
        this.entityRenderMap.put(EntityLightningBolt.class, new RenderLightningBolt());

        for (Render render : this.entityRenderMap.values()) {
            render.setRenderManager(this);
        }
    }

    public <T extends Entity> Render<T> getEntityClassRenderObject(Class<? extends Entity> type) {
        Render render = this.entityRenderMap.get(type);
        if (render == null && type != Entity.class) {
            render = this.getEntityClassRenderObject((Class<? extends Entity>)type.getSuperclass());
            this.entityRenderMap.put(type, render);
        }

        return render;
    }

    public <T extends Entity> Render<T> getEntityRenderObject(Entity entity) {
        return this.getEntityClassRenderObject((Class<? extends Entity>)entity.getClass());
    }

    public void cacheActiveRenderInfo(
        World world, RenderEngine textureManager, FontRenderer textRenderer, EntityLiving camera, GameSettings options, float tickDelta
    ) {
        this.worldObj = world;
        this.renderEngine = textureManager;
        this.options = options;
        this.livingPlayer = camera;
        this.fontRenderer = textRenderer;
        if (camera.isPlayerSleeping()) {
            int i = world.getBlockId(MathHelper.floor_double(camera.posX), MathHelper.floor_double(camera.posY), MathHelper.floor_double(camera.posZ));
            if (i == Block.blockBed.blockID) {
                int j = world.getBlockMetadata(MathHelper.floor_double(camera.posX), MathHelper.floor_double(camera.posY), MathHelper.floor_double(camera.posZ));
                int k = j & 3;
                this.playerViewY = k * 90 + 180;
                this.playerViewX = 0.0F;
            }
        } else {
            this.playerViewY = camera.prevRotationYaw + (camera.rotationYaw - camera.prevRotationYaw) * tickDelta;
            this.playerViewX = camera.prevRotationPitch + (camera.rotationPitch - camera.prevRotationPitch) * tickDelta;
        }

        this.cameraX = camera.lastTickPosX + (camera.posX - camera.lastTickPosX) * tickDelta;
        this.cameraY = camera.lastTickPosY + (camera.posY - camera.lastTickPosY) * tickDelta;
        this.cameraZ = camera.lastTickPosZ + (camera.posZ - camera.lastTickPosZ) * tickDelta;
    }

    public void renderEntity(Entity entity, float tickDelta) {
        double d0 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * tickDelta;
        double d1 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * tickDelta;
        double d2 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * tickDelta;
        float f = entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * tickDelta;
        float f1 = entity.getEntityBrightness(tickDelta);
        GL11.glColor3f(f1, f1, f1);
        this.renderEntityWithPosYaw(entity, d0 - renderPosX, d1 - renderPosY, d2 - renderPosZ, f, tickDelta);
    }

    public void renderEntityWithPosYaw(Entity entity, double dx, double dy, double dz, float yaw, float tickDelta) {
        Render render = this.getEntityRenderObject(entity);
        if (render != null) {
            render.doRender(entity, dx, dy, dz, yaw, tickDelta);
            render.doRenderShadowAndFire(entity, dx, dy, dz, yaw, tickDelta);
        }
    }

    public void setWorld(World world) {
        this.worldObj = world;
    }

    public double squaredDistanceToCamera(double x, double y, double z) {
        double d0 = x - this.cameraX;
        double d1 = y - this.cameraY;
        double d2 = z - this.cameraZ;
        return d0 * d0 + d1 * d1 + d2 * d2;
    }

    public FontRenderer getFontRenderer() {
        return this.fontRenderer;
    }
}
