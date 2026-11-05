// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.src;

import java.awt.image.BufferedImage;
import org.lwjgl.opengl.GL11;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.nio.IntBuffer;
import java.nio.Buffer;

public class FontRenderer
{
    private int[] charWidth = new int[256];
    public int fontTextureName = 0;
    private int fontDisplayLists;
    private IntBuffer buffer = GLAllocation.createDirectIntBuffer(1024);
    
    public FontRenderer(GameSettings options, String fontPath, RenderEngine textureManager) {
        BufferedImage bufferedimage;
        try {
            bufferedimage = ImageIO.read(RenderEngine.class.getResourceAsStream(fontPath));
        } catch (IOException ioexception) {
            throw new RuntimeException(ioexception);
        }
        int i = bufferedimage.getWidth();
        int j = bufferedimage.getHeight();
        int[] aint = new int[i * j];
        bufferedimage.getRGB(0, 0, i, j, aint, 0, i);
        for (int k = 0; k < 256; k++) {
            int l = k % 16;
            int i1 = k / 16;
            int j1;
            for (j1 = 7; j1 >= 0; j1--) {
                int k1 = l * 8 + j1;
                boolean flag = true;
                for (int l1 = 0; l1 < 8 && flag; l1++) {
                    int i2 = (i1 * 8 + l1) * i;
                    int j2 = aint[k1 + i2] & 0xFF;
                    if (j2 > 0) {
                        flag = false;
                    }
                }
                if (!flag) {
                    break;
                }
            }
            if (k == 32) {
                j1 = 2;
            }
            this.charWidth[k] = j1 + 2;
        }
        this.fontTextureName = textureManager.allocateAndSetupTexture(bufferedimage);
        this.fontDisplayLists = GLAllocation.generateDisplayLists(288);
        Tessellator tessellator = Tessellator.instance;
        for (int l2 = 0; l2 < 256; l2++) {
            GL11.glNewList(this.fontDisplayLists + l2, 4864);
            tessellator.startDrawingQuads();
            int j3 = l2 % 16 * 8;
            int l3 = l2 / 16 * 8;
            float f = 7.99F;
            float f1 = 0.0F;
            float f2 = 0.0F;
            tessellator.addVertexWithUV(0.0, 0.0F + f, 0.0, j3 / 128.0F + f1, (l3 + f) / 128.0F + f2);
            tessellator.addVertexWithUV(0.0F + f, 0.0F + f, 0.0, (j3 + f) / 128.0F + f1, (l3 + f) / 128.0F + f2);
            tessellator.addVertexWithUV(0.0F + f, 0.0, 0.0, (j3 + f) / 128.0F + f1, l3 / 128.0F + f2);
            tessellator.addVertexWithUV(0.0, 0.0, 0.0, j3 / 128.0F + f1, l3 / 128.0F + f2);
            tessellator.draw();
            GL11.glTranslatef(this.charWidth[l2], 0.0F, 0.0F);
            GL11.glEndList();
        }
        for (int i3 = 0; i3 < 32; i3++) {
            int k3 = (i3 >> 3 & 1) * 85;
            int i4 = (i3 >> 2 & 1) * 170 + k3;
            int j4 = (i3 >> 1 & 1) * 170 + k3;
            int k4 = (i3 >> 0 & 1) * 170 + k3;
            if (i3 == 6) {
                i4 += 85;
            }
            boolean flag1 = i3 >= 16;
            if (options.anaglyph) {
                int l4 = (i4 * 30 + j4 * 59 + k4 * 11) / 100;
                int i5 = (i4 * 30 + j4 * 70) / 100;
                int k2 = (i4 * 30 + k4 * 70) / 100;
                i4 = l4;
                j4 = i5;
                k4 = k2;
            }
            if (flag1) {
                i4 /= 4;
                j4 /= 4;
                k4 /= 4;
            }
            GL11.glNewList(this.fontDisplayLists + 256 + i3, 4864);
            GL11.glColor3f(i4 / 255.0F, j4 / 255.0F, k4 / 255.0F);
            GL11.glEndList();
        }
    }
    
    public void drawStringWithShadow(final String text, final int x, final int y, final int color) {
        this.renderString(text, x + 1, y + 1, color, true);
        this.drawString(text, x, y, color);
    }
    
    public void drawString(final String text, final int x, final int y, final int color) {
        this.renderString(text, x, y, color, false);
    }
    
    public void renderString(final String text, final int x, final int y, int color, final boolean shadow) {
        if (text == null) {
            return;
        }
        if (shadow) {
            final int n = color & 0xFF000000;
            color = (color & 0xFCFCFC) >> 2;
            color += n;
        }
        GL11.glBindTexture(3553, this.fontTextureName);
        final float n2 = (color >> 16 & 0xFF) / 255.0f;
        final float n3 = (color >> 8 & 0xFF) / 255.0f;
        final float n4 = (color & 0xFF) / 255.0f;
        float n5 = (color >> 24 & 0xFF) / 255.0f;
        if (n5 == 0.0f) {
            n5 = 1.0f;
        }
        GL11.glColor4f(n2, n3, n4, n5);
        this.buffer.clear();
        GL11.glPushMatrix();
        GL11.glTranslatef((float)x, (float)y, 0.0f);
        for (int i = 0; i < text.length(); ++i) {
            while (text.length() > i + 1 && text.charAt(i) == '§') {
                int index = "0123456789abcdef".indexOf(text.toLowerCase().charAt(i + 1));
                if (index < 0 || index > 15) {
                    index = 15;
                }
                this.buffer.put(this.fontDisplayLists + 256 + index + (shadow ? 16 : 0));
                if (this.buffer.remaining() == 0) {
                    this.buffer.flip();
                    GL11.glCallLists(this.buffer);
                    this.buffer.clear();
                }
                i += 2;
            }
            if (i < text.length()) {
                final int index2 = ChatAllowedCharacters.allowedCharacters.indexOf(text.charAt(i));
                if (index2 >= 0) {
                    this.buffer.put(this.fontDisplayLists + index2 + 32);
                }
            }
            if (this.buffer.remaining() == 0) {
                this.buffer.flip();
                GL11.glCallLists(this.buffer);
                this.buffer.clear();
            }
        }
        this.buffer.flip();
        GL11.glCallLists(this.buffer);
        GL11.glPopMatrix();
    }
    
    public int getStringWidth(final String text) {
        if (text == null) {
            return 0;
        }
        int n = 0;
        for (int i = 0; i < text.length(); ++i) {
            if (text.charAt(i) == '§') {
                ++i;
            }
            else {
                final int index = ChatAllowedCharacters.allowedCharacters.indexOf(text.charAt(i));
                if (index >= 0) {
                    n += this.charWidth[index + 32];
                }
            }
        }
        return n;
    }
    
    public void splitAndDraw(final String text, final int x, int y, final int width, final int color) {
        final String[] split = text.split("\n");
        if (split.length > 1) {
            for (int i = 0; i < split.length; ++i) {
                this.splitAndDraw(split[i], x, y, width, color);
                y += this.splitAndGetHeight(split[i], width);
            }
            return;
        }
        final String[] split2 = text.split(" ");
        int j = 0;
        while (j < split2.length) {
            String s;
            for (s = split2[j++] + " "; j < split2.length && this.getStringWidth(s + split2[j]) < width; s = s + split2[j++] + " ") {}
            while (this.getStringWidth(s) > width) {
                int beginIndex;
                for (beginIndex = 0; this.getStringWidth(s.substring(0, beginIndex + 1)) <= width; ++beginIndex) {}
                if (s.substring(0, beginIndex).trim().length() > 0) {
                    this.drawString(s.substring(0, beginIndex), x, y, color);
                    y += 8;
                }
                s = s.substring(beginIndex);
            }
            if (s.trim().length() > 0) {
                this.drawString(s, x, y, color);
                y += 8;
            }
        }
    }
    
    public int splitAndGetHeight(final String text, final int width) {
        final String[] split = text.split("\n");
        if (split.length > 1) {
            int n = 0;
            for (int i = 0; i < split.length; ++i) {
                n += this.splitAndGetHeight(split[i], width);
            }
            return n;
        }
        final String[] split2 = text.split(" ");
        int j = 0;
        int n2 = 0;
        while (j < split2.length) {
            String text2;
            for (text2 = split2[j++] + " "; j < split2.length && this.getStringWidth(text2 + split2[j]) < width; text2 = text2 + split2[j++] + " ") {}
            while (this.getStringWidth(text2) > width) {
                int n3;
                for (n3 = 0; this.getStringWidth(text2.substring(0, n3 + 1)) <= width; ++n3) {}
                if (text2.substring(0, n3).trim().length() > 0) {
                    n2 += 8;
                }
                text2 = text2.substring(n3);
            }
            if (text2.trim().length() > 0) {
                n2 += 8;
            }
        }
        if (n2 < 8) {
            n2 += 8;
        }
        return n2;
    }
}
