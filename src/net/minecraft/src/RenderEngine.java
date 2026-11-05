package net.minecraft.src;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import org.lwjgl.opengl.GL11;

public class RenderEngine {
    public static boolean useMipmaps = false;
    private HashMap<String, Integer> textureMap = new HashMap<>();
    private HashMap<String, int[]> colors = new HashMap<>();
    private HashMap<Integer, BufferedImage> textureNameToImageMap = new HashMap<>();
    private IntBuffer singleIntBuffer = GLAllocation.createDirectIntBuffer(1);
    private ByteBuffer imageData = GLAllocation.createDirectByteBuffer(1048576);
    private List<TextureFX> textureList = new ArrayList<>();
    private Map<String, ThreadDownloadImageData> urlToImageDataMap = new HashMap<>();
    private GameSettings options;
    private boolean clampTexture = false;
    private boolean blurTexture = false;
    private TexturePackList texturePack;
    private BufferedImage missingTextureImage = new BufferedImage(64, 64, 2);

    public RenderEngine(TexturePackList texturePacks, GameSettings options) {
        this.texturePack = texturePacks;
        this.options = options;
        Graphics graphics = this.missingTextureImage.getGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, 64, 64);
        graphics.setColor(Color.BLACK);
        graphics.drawString("missingtex", 1, 10);
        graphics.dispose();
    }

    public int[] getColors(String path) {
        TexturePackBase texturepackbase = this.texturePack.selectedTexturePack;
        int[] aint = this.colors.get(path);
        if (aint != null) {
            return aint;
        }

        try {
            int[] i = null;
            if (path.startsWith("##")) {
                i = this.readColors(this.unwrapImageByColumns(this.readTextureImage(texturepackbase.getResourceAsStream(path.substring(2)))));
            } else if (path.startsWith("%clamp%")) {
                this.clampTexture = true;
                i = this.readColors(this.readTextureImage(texturepackbase.getResourceAsStream(path.substring(7))));
                this.clampTexture = false;
            } else if (path.startsWith("%blur%")) {
                this.blurTexture = true;
                i = this.readColors(this.readTextureImage(texturepackbase.getResourceAsStream(path.substring(6))));
                this.blurTexture = false;
            } else {
                InputStream inputstream = texturepackbase.getResourceAsStream(path);
                if (inputstream == null) {
                    i = this.readColors(this.missingTextureImage);
                } else {
                    i = this.readColors(this.readTextureImage(inputstream));
                }
            }

            this.colors.put(path, (int[])i);
            return (int[])i;
        } catch (IOException ioexception) {
            ioexception.printStackTrace();
            int[] aint1 = this.readColors(this.missingTextureImage);
            this.colors.put(path, aint1);
            return aint1;
        }
    }

    private int[] readColors(BufferedImage image) {
        int i = image.getWidth();
        int j = image.getHeight();
        int[] aint = new int[i * j];
        image.getRGB(0, 0, i, j, aint, 0, i);
        return aint;
    }

    private int[] readColors(BufferedImage image, int[] colors) {
        int i = image.getWidth();
        int j = image.getHeight();
        image.getRGB(0, 0, i, j, colors, 0, i);
        return colors;
    }

    public int getTexture(String path) {
        TexturePackBase texturepackbase = this.texturePack.selectedTexturePack;
        Integer integer = this.textureMap.get(path);
        if (integer != null) {
            return integer;
        }

        try {
            ((Buffer)this.singleIntBuffer).clear();
            GLAllocation.generateTextureNames(this.singleIntBuffer);
            int j = this.singleIntBuffer.get(0);
            if (path.startsWith("##")) {
                this.setupTexture(this.unwrapImageByColumns(this.readTextureImage(texturepackbase.getResourceAsStream(path.substring(2)))), j);
            } else if (path.startsWith("%clamp%")) {
                this.clampTexture = true;
                this.setupTexture(this.readTextureImage(texturepackbase.getResourceAsStream(path.substring(7))), j);
                this.clampTexture = false;
            } else if (path.startsWith("%blur%")) {
                this.blurTexture = true;
                this.setupTexture(this.readTextureImage(texturepackbase.getResourceAsStream(path.substring(6))), j);
                this.blurTexture = false;
            } else {
                InputStream inputstream = texturepackbase.getResourceAsStream(path);
                if (inputstream == null) {
                    this.setupTexture(this.missingTextureImage, j);
                } else {
                    this.setupTexture(this.readTextureImage(inputstream), j);
                }
            }

            this.textureMap.put(path, j);
            return j;
        } catch (IOException ioexception) {
            ioexception.printStackTrace();
            GLAllocation.generateTextureNames(this.singleIntBuffer);
            int i = this.singleIntBuffer.get(0);
            this.setupTexture(this.missingTextureImage, i);
            this.textureMap.put(path, i);
            return i;
        }
    }

    private BufferedImage unwrapImageByColumns(BufferedImage image) {
        int i = image.getWidth() / 16;
        BufferedImage bufferedimage = new BufferedImage(16, image.getHeight() * i, 2);
        Graphics graphics = bufferedimage.getGraphics();

        for (int j = 0; j < i; j++) {
            graphics.drawImage(image, -j * 16, j * image.getHeight(), null);
        }

        graphics.dispose();
        return bufferedimage;
    }

    public int allocateAndSetupTexture(BufferedImage image) {
        ((Buffer)this.singleIntBuffer).clear();
        GLAllocation.generateTextureNames(this.singleIntBuffer);
        int i = this.singleIntBuffer.get(0);
        this.setupTexture(image, i);
        this.textureNameToImageMap.put(i, image);
        return i;
    }

    public void setupTexture(BufferedImage image, int id) {
        GL11.glBindTexture(3553, id);
        if (useMipmaps) {
            GL11.glTexParameteri(3553, 10241, 9986);
            GL11.glTexParameteri(3553, 10240, 9728);
        } else {
            GL11.glTexParameteri(3553, 10241, 9728);
            GL11.glTexParameteri(3553, 10240, 9728);
        }

        if (this.blurTexture) {
            GL11.glTexParameteri(3553, 10241, 9729);
            GL11.glTexParameteri(3553, 10240, 9729);
        }

        if (this.clampTexture) {
            GL11.glTexParameteri(3553, 10242, 10496);
            GL11.glTexParameteri(3553, 10243, 10496);
        } else {
            GL11.glTexParameteri(3553, 10242, 10497);
            GL11.glTexParameteri(3553, 10243, 10497);
        }

        int i = image.getWidth();
        int j = image.getHeight();
        int[] aint = new int[i * j];
        byte[] abyte = new byte[i * j * 4];
        image.getRGB(0, 0, i, j, aint, 0, i);

        for (int k = 0; k < aint.length; k++) {
            int l = aint[k] >> 24 & 0xFF;
            int i1 = aint[k] >> 16 & 0xFF;
            int j1 = aint[k] >> 8 & 0xFF;
            int k1 = aint[k] & 0xFF;
            if (this.options != null && this.options.anaglyph) {
                int l1 = (i1 * 30 + j1 * 59 + k1 * 11) / 100;
                int i2 = (i1 * 30 + j1 * 70) / 100;
                int j2 = (i1 * 30 + k1 * 70) / 100;
                i1 = l1;
                j1 = i2;
                k1 = j2;
            }

            abyte[k * 4 + 0] = (byte)i1;
            abyte[k * 4 + 1] = (byte)j1;
            abyte[k * 4 + 2] = (byte)k1;
            abyte[k * 4 + 3] = (byte)l;
        }

        ((Buffer)this.imageData).clear();
        this.imageData.put(abyte);
        ((Buffer)this.imageData).position(0).limit(abyte.length);
        GL11.glTexImage2D(3553, 0, 6408, i, j, 0, 6408, 5121, this.imageData);
        if (useMipmaps) {
            for (int j3 = 1; j3 <= 4; j3++) {
                int k3 = i >> j3 - 1;
                int l3 = i >> j3;
                int i4 = j >> j3;

                for (int j4 = 0; j4 < l3; j4++) {
                    for (int k4 = 0; k4 < i4; k4++) {
                        int l4 = this.imageData.getInt((j4 * 2 + 0 + (k4 * 2 + 0) * k3) * 4);
                        int i5 = this.imageData.getInt((j4 * 2 + 1 + (k4 * 2 + 0) * k3) * 4);
                        int k2 = this.imageData.getInt((j4 * 2 + 1 + (k4 * 2 + 1) * k3) * 4);
                        int l2 = this.imageData.getInt((j4 * 2 + 0 + (k4 * 2 + 1) * k3) * 4);
                        int i3 = this.weightedAverageColor(this.weightedAverageColor(l4, i5), this.weightedAverageColor(k2, l2));
                        this.imageData.putInt((j4 + k4 * l3) * 4, i3);
                    }
                }

                GL11.glTexImage2D(3553, j3, 6408, l3, i4, 0, 6408, 5121, this.imageData);
            }
        }
    }

    public void bind(int[] colors, int width, int height, int id) {
        GL11.glBindTexture(3553, id);
        if (useMipmaps) {
            GL11.glTexParameteri(3553, 10241, 9986);
            GL11.glTexParameteri(3553, 10240, 9728);
        } else {
            GL11.glTexParameteri(3553, 10241, 9728);
            GL11.glTexParameteri(3553, 10240, 9728);
        }

        if (this.blurTexture) {
            GL11.glTexParameteri(3553, 10241, 9729);
            GL11.glTexParameteri(3553, 10240, 9729);
        }

        if (this.clampTexture) {
            GL11.glTexParameteri(3553, 10242, 10496);
            GL11.glTexParameteri(3553, 10243, 10496);
        } else {
            GL11.glTexParameteri(3553, 10242, 10497);
            GL11.glTexParameteri(3553, 10243, 10497);
        }

        byte[] abyte = new byte[width * height * 4];

        for (int i = 0; i < colors.length; i++) {
            int j = colors[i] >> 24 & 0xFF;
            int k = colors[i] >> 16 & 0xFF;
            int l = colors[i] >> 8 & 0xFF;
            int i1 = colors[i] & 0xFF;
            if (this.options != null && this.options.anaglyph) {
                int j1 = (k * 30 + l * 59 + i1 * 11) / 100;
                int k1 = (k * 30 + l * 70) / 100;
                int l1 = (k * 30 + i1 * 70) / 100;
                k = j1;
                l = k1;
                i1 = l1;
            }

            abyte[i * 4 + 0] = (byte)k;
            abyte[i * 4 + 1] = (byte)l;
            abyte[i * 4 + 2] = (byte)i1;
            abyte[i * 4 + 3] = (byte)j;
        }

        ((Buffer)this.imageData).clear();
        this.imageData.put(abyte);
        ((Buffer)this.imageData).position(0).limit(abyte.length);
        GL11.glTexSubImage2D(3553, 0, 0, 0, width, height, 6408, 5121, this.imageData);
    }

    public void deleteTexture(int id) {
        this.textureNameToImageMap.remove(id);
        ((Buffer)this.singleIntBuffer).clear();
        this.singleIntBuffer.put(id);
        ((Buffer)this.singleIntBuffer).flip();
        GL11.glDeleteTextures(this.singleIntBuffer);
    }

    public int getTextureForDownloadableImage(String url, String backup) {
        ThreadDownloadImageData threaddownloadimagedata = this.urlToImageDataMap.get(url);
        if (threaddownloadimagedata != null && threaddownloadimagedata.image != null && !threaddownloadimagedata.textureSetupComplete) {
            if (threaddownloadimagedata.textureName < 0) {
                threaddownloadimagedata.textureName = this.allocateAndSetupTexture(threaddownloadimagedata.image);
            } else {
                this.setupTexture(threaddownloadimagedata.image, threaddownloadimagedata.textureName);
            }

            threaddownloadimagedata.textureSetupComplete = true;
        }

        if (threaddownloadimagedata != null && threaddownloadimagedata.textureName >= 0) {
            return threaddownloadimagedata.textureName;
        } else {
            return backup == null ? -1 : this.getTexture(backup);
        }
    }

    public ThreadDownloadImageData obtainImageData(String url, ImageBuffer processor) {
        ThreadDownloadImageData threaddownloadimagedata = this.urlToImageDataMap.get(url);
        if (threaddownloadimagedata == null) {
            this.urlToImageDataMap.put(url, new ThreadDownloadImageData(url, processor));
        } else {
            threaddownloadimagedata.referenceCount++;
        }

        return threaddownloadimagedata;
    }

    public void releaseImageData(String url) {
        ThreadDownloadImageData threaddownloadimagedata = this.urlToImageDataMap.get(url);
        if (threaddownloadimagedata != null) {
            threaddownloadimagedata.referenceCount--;
            if (threaddownloadimagedata.referenceCount == 0) {
                if (threaddownloadimagedata.textureName >= 0) {
                    this.deleteTexture(threaddownloadimagedata.textureName);
                }

                this.urlToImageDataMap.remove(url);
            }
        }
    }

    public void registerTextureFX(TextureFX texture) {
        this.textureList.add(texture);
        texture.onTick();
    }

    public void updateDynamicTextures() {
        for (int i = 0; i < this.textureList.size(); i++) {
            TextureFX texturefx = this.textureList.get(i);
            texturefx.anaglyphEnabled = this.options.anaglyph;
            texturefx.onTick();
            ((Buffer)this.imageData).clear();
            this.imageData.put(texturefx.imageData);
            ((Buffer)this.imageData).position(0).limit(texturefx.imageData.length);
            texturefx.bindImage(this);

            for (int j = 0; j < texturefx.tileSize; j++) {
                for (int k = 0; k < texturefx.tileSize; k++) {
                    GL11.glTexSubImage2D(
                        3553, 0, texturefx.iconIndex % 16 * 16 + j * 16, texturefx.iconIndex / 16 * 16 + k * 16, 16, 16, 6408, 5121, this.imageData
                    );
                    if (useMipmaps) {
                        for (int l = 1; l <= 4; l++) {
                            int i1 = 16 >> l - 1;
                            int j1 = 16 >> l;

                            for (int k1 = 0; k1 < j1; k1++) {
                                for (int l1 = 0; l1 < j1; l1++) {
                                    int i2 = this.imageData.getInt((k1 * 2 + 0 + (l1 * 2 + 0) * i1) * 4);
                                    int j2 = this.imageData.getInt((k1 * 2 + 1 + (l1 * 2 + 0) * i1) * 4);
                                    int k2 = this.imageData.getInt((k1 * 2 + 1 + (l1 * 2 + 1) * i1) * 4);
                                    int l2 = this.imageData.getInt((k1 * 2 + 0 + (l1 * 2 + 1) * i1) * 4);
                                    int i3 = this.averageColor(this.averageColor(i2, j2), this.averageColor(k2, l2));
                                    this.imageData.putInt((k1 + l1 * j1) * 4, i3);
                                }
                            }

                            GL11.glTexSubImage2D(3553, l, texturefx.iconIndex % 16 * j1, texturefx.iconIndex / 16 * j1, j1, j1, 6408, 5121, this.imageData);
                        }
                    }
                }
            }
        }

        for (int j3 = 0; j3 < this.textureList.size(); j3++) {
            TextureFX texturefx1 = this.textureList.get(j3);
            if (texturefx1.textureId > 0) {
                ((Buffer)this.imageData).clear();
                this.imageData.put(texturefx1.imageData);
                ((Buffer)this.imageData).position(0).limit(texturefx1.imageData.length);
                GL11.glBindTexture(3553, texturefx1.textureId);
                GL11.glTexSubImage2D(3553, 0, 0, 0, 16, 16, 6408, 5121, this.imageData);
                if (useMipmaps) {
                    for (int k3 = 1; k3 <= 4; k3++) {
                        int l3 = 16 >> k3 - 1;
                        int i4 = 16 >> k3;

                        for (int j4 = 0; j4 < i4; j4++) {
                            for (int k4 = 0; k4 < i4; k4++) {
                                int l4 = this.imageData.getInt((j4 * 2 + 0 + (k4 * 2 + 0) * l3) * 4);
                                int i5 = this.imageData.getInt((j4 * 2 + 1 + (k4 * 2 + 0) * l3) * 4);
                                int j5 = this.imageData.getInt((j4 * 2 + 1 + (k4 * 2 + 1) * l3) * 4);
                                int k5 = this.imageData.getInt((j4 * 2 + 0 + (k4 * 2 + 1) * l3) * 4);
                                int l5 = this.averageColor(this.averageColor(l4, i5), this.averageColor(j5, k5));
                                this.imageData.putInt((j4 + k4 * i4) * 4, l5);
                            }
                        }

                        GL11.glTexSubImage2D(3553, k3, 0, 0, i4, i4, 6408, 5121, this.imageData);
                    }
                }
            }
        }
    }

    private int averageColor(int color1, int color2) {
        int i = (color1 & 0xFF000000) >> 24 & 0xFF;
        int j = (color2 & 0xFF000000) >> 24 & 0xFF;
        return (i + j >> 1 << 24) + ((color1 & 16711422) + (color2 & 16711422) >> 1);
    }

    private int weightedAverageColor(int color1, int color2) {
        int i = (color1 & 0xFF000000) >> 24 & 0xFF;
        int j = (color2 & 0xFF000000) >> 24 & 0xFF;
        short short1 = 255;
        if (i + j == 0) {
            i = 1;
            j = 1;
            short1 = 0;
        }

        int k = (color1 >> 16 & 0xFF) * i;
        int l = (color1 >> 8 & 0xFF) * i;
        int i1 = (color1 & 0xFF) * i;
        int j1 = (color2 >> 16 & 0xFF) * j;
        int k1 = (color2 >> 8 & 0xFF) * j;
        int l1 = (color2 & 0xFF) * j;
        int i2 = (k + j1) / (i + j);
        int j2 = (l + k1) / (i + j);
        int k2 = (i1 + l1) / (i + j);
        return short1 << 24 | i2 << 16 | j2 << 8 | k2;
    }

    public void refreshTextures() {
        TexturePackBase texturepackbase = this.texturePack.selectedTexturePack;

        for (int i : this.textureNameToImageMap.keySet()) {
            BufferedImage bufferedimage = this.textureNameToImageMap.get(i);
            this.setupTexture(bufferedimage, i);
        }

        for (ThreadDownloadImageData threaddownloadimagedata : this.urlToImageDataMap.values()) {
            threaddownloadimagedata.textureSetupComplete = false;
        }

        for (String s : this.textureMap.keySet()) {
            try {
                BufferedImage bufferedimage1;
                if (s.startsWith("##")) {
                    bufferedimage1 = this.unwrapImageByColumns(this.readTextureImage(texturepackbase.getResourceAsStream(s.substring(2))));
                } else if (s.startsWith("%clamp%")) {
                    this.clampTexture = true;
                    bufferedimage1 = this.readTextureImage(texturepackbase.getResourceAsStream(s.substring(7)));
                } else if (s.startsWith("%blur%")) {
                    this.blurTexture = true;
                    bufferedimage1 = this.readTextureImage(texturepackbase.getResourceAsStream(s.substring(6)));
                } else {
                    bufferedimage1 = this.readTextureImage(texturepackbase.getResourceAsStream(s));
                }

                int j = this.textureMap.get(s);
                this.setupTexture(bufferedimage1, j);
                this.blurTexture = false;
                this.clampTexture = false;
            } catch (IOException ioexception1) {
                ioexception1.printStackTrace();
            }
        }

        for (String s1 : this.colors.keySet()) {
            try {
                BufferedImage bufferedimage2;
                if (s1.startsWith("##")) {
                    bufferedimage2 = this.unwrapImageByColumns(this.readTextureImage(texturepackbase.getResourceAsStream(s1.substring(2))));
                } else if (s1.startsWith("%clamp%")) {
                    this.clampTexture = true;
                    bufferedimage2 = this.readTextureImage(texturepackbase.getResourceAsStream(s1.substring(7)));
                } else if (s1.startsWith("%blur%")) {
                    this.blurTexture = true;
                    bufferedimage2 = this.readTextureImage(texturepackbase.getResourceAsStream(s1.substring(6)));
                } else {
                    bufferedimage2 = this.readTextureImage(texturepackbase.getResourceAsStream(s1));
                }

                this.readColors(bufferedimage2, this.colors.get(s1));
                this.blurTexture = false;
                this.clampTexture = false;
            } catch (IOException ioexception) {
                ioexception.printStackTrace();
            }
        }
    }

    private BufferedImage readTextureImage(InputStream is) throws IOException {
        BufferedImage bufferedimage = ImageIO.read(is);
        is.close();
        return bufferedimage;
    }

    public void bindTexture(int id) {
        if (id >= 0) {
            GL11.glBindTexture(3553, id);
        }
    }
}
