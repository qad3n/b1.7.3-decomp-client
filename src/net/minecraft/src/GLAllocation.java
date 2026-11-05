package net.minecraft.src;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import org.lwjgl.opengl.GL11;

public class GLAllocation {
    private static List<Integer> displayLists = new ArrayList<Integer>();
    private static List<Integer> textureNames = new ArrayList<Integer>();

    public static synchronized int generateDisplayLists(int s) {
        int n = GL11.glGenLists(s);
        displayLists.add(n);
        displayLists.add(s);
        return n;
    }

    public static synchronized void generateTextureNames(IntBuffer lists) {
        GL11.glGenTextures(lists);
        for (int i = lists.position(); i < lists.limit(); ++i) {
            textureNames.add(lists.get(i));
        }
    }

    public static synchronized void releaseList(int list) {
        int n = displayLists.indexOf(list);
        GL11.glDeleteLists(displayLists.get(n), displayLists.get(n + 1));
        displayLists.remove(n);
        displayLists.remove(n);
    }

    public static synchronized void deleteTexturesAndDisplayLists() {
        for (int i = 0; i < displayLists.size(); i += 2) {
            GL11.glDeleteLists(displayLists.get(i), displayLists.get(i + 1));
        }
        IntBuffer intBuffer = GLAllocation.createDirectIntBuffer(textureNames.size());
        intBuffer.flip();
        GL11.glDeleteTextures(intBuffer);
        for (int i = 0; i < textureNames.size(); ++i) {
            intBuffer.put(textureNames.get(i));
        }
        intBuffer.flip();
        GL11.glDeleteTextures(intBuffer);
        displayLists.clear();
        textureNames.clear();
    }

    public static synchronized ByteBuffer createDirectByteBuffer(int capacity) {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(capacity).order(ByteOrder.nativeOrder());
        return byteBuffer;
    }

    public static IntBuffer createDirectIntBuffer(int capacity) {
        return GLAllocation.createDirectByteBuffer(capacity << 2).asIntBuffer();
    }

    public static FloatBuffer createDirectFloatBuffer(int capacity) {
        return GLAllocation.createDirectByteBuffer(capacity << 2).asFloatBuffer();
    }
}

