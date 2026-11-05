package net.minecraft.src;

import org.lwjgl.opengl.GL11;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class RenderBoat
extends Render<EntityBoat> {
    protected ModelBase modelBoat;

    public RenderBoat() {
        this.shadowSize = 0.5f;
        this.modelBoat = new ModelBoat();
    }

    @Override
    public void doRender(EntityBoat entityBoat, double d, double d2, double d3, float f, float f2) {
        GL11.glPushMatrix();
        GL11.glTranslatef((float)d, (float)d2, (float)d3);
        GL11.glRotatef(180.0f - f, 0.0f, 1.0f, 0.0f);
        float f3 = (float)entityBoat.boatTimeSinceHit - f2;
        float f4 = (float)entityBoat.boatCurrentDamage - f2;
        if (f4 < 0.0f) {
            f4 = 0.0f;
        }
        if (f3 > 0.0f) {
            GL11.glRotatef(MathHelper.sin(f3) * f3 * f4 / 10.0f * (float)entityBoat.boatRockDirection, 1.0f, 0.0f, 0.0f);
        }
        this.loadTexture("/terrain.png");
        float f5 = 0.75f;
        GL11.glScalef(f5, f5, f5);
        GL11.glScalef(1.0f / f5, 1.0f / f5, 1.0f / f5);
        this.loadTexture("/item/boat.png");
        GL11.glScalef(-1.0f, -1.0f, 1.0f);
        this.modelBoat.render(0.0f, 0.0f, -0.1f, 0.0f, 0.0f, 0.0625f);
        GL11.glPopMatrix();
    }
}

