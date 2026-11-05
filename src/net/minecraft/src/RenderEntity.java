package net.minecraft.src;

import org.lwjgl.opengl.GL11;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class RenderEntity
extends Render<Entity> {
    @Override
    public void doRender(Entity entity, double dx, double dy, double dz, float yaw, float tickDelta) {
        GL11.glPushMatrix();
        RenderEntity.renderOffsetAABB(entity.boundingBox, dx - entity.lastTickPosX, dy - entity.lastTickPosY, dz - entity.lastTickPosZ);
        GL11.glPopMatrix();
    }
}

