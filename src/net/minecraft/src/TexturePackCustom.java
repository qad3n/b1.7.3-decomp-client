package net.minecraft.src;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

public class TexturePackCustom
extends TexturePackBase {
    private ZipFile texturePackZipFile;
    private int texturePackName = -1;
    private BufferedImage texturePackThumbnail;
    private File texturePackFile;

    public TexturePackCustom(File file) throws IOException {
        this.texturePackFileName = file.getName();
        this.texturePackFile = file;
    }

    private String truncateString(String line) {
        if (line != null && line.length() > 34) {
            line = line.substring(0, 34);
        }
        return line;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void load(Minecraft minecraft) throws IOException {
        ZipFile zipFile = null;
        InputStream inputStream = null;
        try {
            zipFile = new ZipFile(this.texturePackFile);
            try {
                inputStream = zipFile.getInputStream(zipFile.getEntry("pack.txt"));
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                this.firstDescriptionLine = this.truncateString(bufferedReader.readLine());
                this.secondDescriptionLine = this.truncateString(bufferedReader.readLine());
                bufferedReader.close();
                inputStream.close();
            }
            catch (Exception exception) {
                // empty catch block
            }
            try {
                inputStream = zipFile.getInputStream(zipFile.getEntry("pack.png"));
                this.texturePackThumbnail = ImageIO.read(inputStream);
                inputStream.close();
            }
            catch (Exception exception) {
                // empty catch block
            }
            zipFile.close();
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        finally {
            try {
                inputStream.close();
            }
            catch (Exception exception) {}
            try {
                zipFile.close();
            }
            catch (Exception exception) {}
        }
    }

    public void unload(Minecraft minecraft) {
        if (this.texturePackThumbnail != null) {
            minecraft.renderEngine.deleteTexture(this.texturePackName);
        }
        this.closeTexturePackFile();
    }

    public void bindThumbnailTexture(Minecraft minecraft) {
        if (this.texturePackThumbnail != null && this.texturePackName < 0) {
            this.texturePackName = minecraft.renderEngine.allocateAndSetupTexture(this.texturePackThumbnail);
        }
        if (this.texturePackThumbnail != null) {
            minecraft.renderEngine.bindTexture(this.texturePackName);
        } else {
            GL11.glBindTexture(3553, minecraft.renderEngine.getTexture("/gui/unknown_pack.png"));
        }
    }

    public void open() {
        try {
            this.texturePackZipFile = new ZipFile(this.texturePackFile);
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    public void closeTexturePackFile() {
        try {
            this.texturePackZipFile.close();
        }
        catch (Exception exception) {
            // empty catch block
        }
        this.texturePackZipFile = null;
    }

    public InputStream getResourceAsStream(String path) {
        try {
            ZipEntry zipEntry = this.texturePackZipFile.getEntry(path.substring(1));
            if (zipEntry != null) {
                return this.texturePackZipFile.getInputStream(zipEntry);
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
        return TexturePackBase.class.getResourceAsStream(path);
    }
}

