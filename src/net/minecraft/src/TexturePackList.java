package net.minecraft.src;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.client.Minecraft;

public class TexturePackList {
    private List<TexturePackBase> availableTexturePacks = new ArrayList<>();
    private TexturePackBase defaultTexturePack = new TexturePackDefault();
    public TexturePackBase selectedTexturePack;
    private Map<String, TexturePackBase> availablePacksByKey = new HashMap<>();
    private Minecraft mc;
    private File texturePackDir;
    private String currentTexturePack;

    public TexturePackList(Minecraft minecraft, File gameDir) {
        this.mc = minecraft;
        this.texturePackDir = new File(gameDir, "texturepacks");
        if (!this.texturePackDir.exists()) {
            this.texturePackDir.mkdirs();
        }

        this.currentTexturePack = minecraft.gameSettings.skin;
        this.updateAvaliableTexturePacks();
        this.selectedTexturePack.open();
    }

    public boolean setTexturePack(TexturePackBase pack) {
        if (pack == this.selectedTexturePack) {
            return false;
        }

        this.selectedTexturePack.closeTexturePackFile();
        this.currentTexturePack = pack.texturePackFileName;
        this.selectedTexturePack = pack;
        this.mc.gameSettings.skin = this.currentTexturePack;
        this.mc.gameSettings.saveOptions();
        this.selectedTexturePack.open();
        return true;
    }

    public void updateAvaliableTexturePacks() {
        List arraylist = new ArrayList();
        this.selectedTexturePack = null;
        arraylist.add(this.defaultTexturePack);
        if (this.texturePackDir.exists() && this.texturePackDir.isDirectory()) {
            File[] afile = this.texturePackDir.listFiles();

            for (File file1 : afile) {
                if (file1.isFile() && file1.getName().toLowerCase().endsWith(".zip")) {
                    String s = file1.getName() + ":" + file1.length() + ":" + file1.lastModified();

                    try {
                        if (!this.availablePacksByKey.containsKey(s)) {
                            TexturePackBase texturepackcustom = new TexturePackCustom(file1);
                            texturepackcustom.key = s;
                            this.availablePacksByKey.put(s, texturepackcustom);
                            texturepackcustom.load(this.mc);
                        }

                        TexturePackBase texturepackbase1 = this.availablePacksByKey.get(s);
                        if (texturepackbase1.texturePackFileName.equals(this.currentTexturePack)) {
                            this.selectedTexturePack = texturepackbase1;
                        }

                        arraylist.add(texturepackbase1);
                    } catch (IOException ioexception) {
                        ioexception.printStackTrace();
                    }
                }
            }
        }

        if (this.selectedTexturePack == null) {
            this.selectedTexturePack = this.defaultTexturePack;
        }

        this.availableTexturePacks.removeAll(arraylist);

        for (TexturePackBase texturepackbase : this.availableTexturePacks) {
            texturepackbase.unload(this.mc);
            this.availablePacksByKey.remove(texturepackbase.key);
        }

        this.availableTexturePacks = arraylist;
    }

    public List<TexturePackBase> availableTexturePacks() {
        return new ArrayList<>(this.availableTexturePacks);
    }
}
