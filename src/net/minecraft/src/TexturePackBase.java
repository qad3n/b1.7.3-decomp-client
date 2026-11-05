package net.minecraft.src;

import java.io.IOException;
import java.io.InputStream;
import net.minecraft.client.Minecraft;

public abstract class TexturePackBase {
    public String texturePackFileName;
    public String firstDescriptionLine;
    public String secondDescriptionLine;
    public String key;

    public void open() {
    }

    public void closeTexturePackFile() {
    }

    public void load(Minecraft minecraft) throws IOException {
    }

    public void unload(Minecraft minecraft) {
    }

    public void bindThumbnailTexture(Minecraft minecraft) {
    }

    public InputStream getResourceAsStream(String path) {
        return TexturePackBase.class.getResourceAsStream(path);
    }
}

