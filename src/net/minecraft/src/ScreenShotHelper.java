package net.minecraft.src;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.imageio.ImageIO;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

public class ScreenShotHelper {
    private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss");
    private static ByteBuffer buffer;
    private static byte[] pixelData;
    private static int[] imageData;

    public static String saveScreenshot(File gameDir, int width, int height) {
        try {
            File file1 = new File(gameDir, "screenshots");
            file1.mkdir();
            if (buffer == null || buffer.capacity() < width * height) {
                buffer = BufferUtils.createByteBuffer(width * height * 3);
            }

            if (imageData == null || imageData.length < width * height * 3) {
                pixelData = new byte[width * height * 3];
                imageData = new int[width * height];
            }

            GL11.glPixelStorei(3333, 1);
            GL11.glPixelStorei(3317, 1);
            ((Buffer)buffer).clear();
            GL11.glReadPixels(0, 0, width, height, 6407, 5121, buffer);
            ((Buffer)buffer).clear();
            String s = "" + dateFormat.format(new Date());
            int i = 1;

            File file2;
            while ((file2 = new File(file1, s + (i == 1 ? "" : "_" + i) + ".png")).exists()) {
                i++;
            }

            buffer.get(pixelData);

            for (int j = 0; j < width; j++) {
                for (int k = 0; k < height; k++) {
                    int l = j + (height - k - 1) * width;
                    int i1 = pixelData[l * 3 + 0] & 255;
                    int j1 = pixelData[l * 3 + 1] & 255;
                    int k1 = pixelData[l * 3 + 2] & 255;
                    int l1 = 0xFF000000 | i1 << 16 | j1 << 8 | k1;
                    imageData[j + k * width] = l1;
                }
            }

            BufferedImage bufferedimage = new BufferedImage(width, height, 1);
            bufferedimage.setRGB(0, 0, width, height, imageData, 0, width);
            ImageIO.write(bufferedimage, "png", file2);
            return "Saved screenshot as " + file2.getName();
        } catch (Exception exception) {
            exception.printStackTrace();
            return "Failed to save: " + exception;
        }
    }
}
