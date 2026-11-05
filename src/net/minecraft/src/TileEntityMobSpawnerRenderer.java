package net.minecraft.src;

import java.util.HashMap;
import java.util.Map;
import org.lwjgl.opengl.GL11;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class TileEntityMobSpawnerRenderer
extends TileEntitySpecialRenderer<TileEntityMobSpawner> {
    private Map<String, Entity> entityHashMap = new HashMap<String, Entity>();

    @Override
    public void renderTileEntityAt(TileEntityMobSpawner x, double d, double d2, double d3, float f) {
        GL11.glPushMatrix();
        GL11.glTranslatef((float)d + 0.5f, (float)d2, (float)d3 + 0.5f);
        Entity entity = this.entityHashMap.get(x.getMobID());
        if (entity == null) {
            entity = EntityList.createEntityInWorld(x.getMobID(), null);
            this.entityHashMap.put(x.getMobID(), entity);
        }
        if (entity != null) {
            entity.setWorld(x.worldObj);
            float f2 = 0.4375f;
            GL11.glTranslatef(0.0f, 0.4f, 0.0f);
            GL11.glRotatef((float)(x.yaw2 + (x.yaw - x.yaw2) * (double)f) * 10.0f, 0.0f, 1.0f, 0.0f);
            GL11.glRotatef(-30.0f, 1.0f, 0.0f, 0.0f);
            GL11.glTranslatef(0.0f, -0.4f, 0.0f);
            GL11.glScalef(f2, f2, f2);
            entity.setLocationAndAngles(d, d2, d3, 0.0f, 0.0f);
            RenderManager.instance.renderEntityWithPosYaw(entity, 0.0, 0.0, 0.0, 0.0f, f);
        }
        GL11.glPopMatrix();
    }
}

