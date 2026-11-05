package net.minecraft.src;

import java.nio.FloatBuffer;
import org.lwjgl.opengl.GL11;

public class RenderHelper {
    private static FloatBuffer BUFFER = GLAllocation.createDirectFloatBuffer(16);

    public static void disableStandardItemLighting() {
        GL11.glDisable(2896);
        GL11.glDisable(16384);
        GL11.glDisable(16385);
        GL11.glDisable(2903);
    }

    public static void enableStandardItemLighting() {
        GL11.glEnable(2896);
        GL11.glEnable(16384);
        GL11.glEnable(16385);
        GL11.glEnable(2903);
        GL11.glColorMaterial(1032, 5634);
        float f = 0.4f;
        float f2 = 0.6f;
        float f3 = 0.0f;
        Vec3D vec3D = Vec3D.createVector(0.2f, 1.0, -0.7f).normalize();
        GL11.glLight(16384, 4611, RenderHelper.getBuffer(vec3D.xCoord, vec3D.yCoord, vec3D.zCoord, 0.0));
        GL11.glLight(16384, 4609, RenderHelper.getBuffer(f2, f2, f2, 1.0f));
        GL11.glLight(16384, 4608, RenderHelper.getBuffer(0.0f, 0.0f, 0.0f, 1.0f));
        GL11.glLight(16384, 4610, RenderHelper.getBuffer(f3, f3, f3, 1.0f));
        vec3D = Vec3D.createVector(-0.2f, 1.0, 0.7f).normalize();
        GL11.glLight(16385, 4611, RenderHelper.getBuffer(vec3D.xCoord, vec3D.yCoord, vec3D.zCoord, 0.0));
        GL11.glLight(16385, 4609, RenderHelper.getBuffer(f2, f2, f2, 1.0f));
        GL11.glLight(16385, 4608, RenderHelper.getBuffer(0.0f, 0.0f, 0.0f, 1.0f));
        GL11.glLight(16385, 4610, RenderHelper.getBuffer(f3, f3, f3, 1.0f));
        GL11.glShadeModel(7424);
        GL11.glLightModel(2899, RenderHelper.getBuffer(f, f, f, 1.0f));
    }

    private static FloatBuffer getBuffer(double r, double g, double b, double a) {
        return RenderHelper.getBuffer((float)r, (float)g, (float)b, (float)a);
    }

    private static FloatBuffer getBuffer(float r, float g, float b, float a) {
        BUFFER.clear();
        BUFFER.put(r).put(g).put(b).put(a);
        BUFFER.flip();
        return BUFFER;
    }
}

