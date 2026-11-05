package net.minecraft.src;

import java.net.URL;

public class SoundPoolEntry {
    public String soundName;
    public URL soundUrl;

    public SoundPoolEntry(String path, URL url) {
        this.soundName = path;
        this.soundUrl = url;
    }
}

