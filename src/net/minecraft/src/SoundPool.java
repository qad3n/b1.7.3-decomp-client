// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.src;

import java.net.MalformedURLException;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class SoundPool
{
    private Random rand;
    private Map<String, List<SoundPoolEntry>> nameToSoundPoolEntriesMapping;
    private List<SoundPoolEntry> allSoundPoolEntries;
    public int numberOfSoundPoolEntries;
    public boolean trimTrailingDigits;
    
    public SoundPool() {
        this.rand = new Random();
        this.nameToSoundPoolEntriesMapping = new HashMap<String, List<SoundPoolEntry>>();
        this.allSoundPoolEntries = new ArrayList<SoundPoolEntry>();
        this.numberOfSoundPoolEntries = 0;
        this.trimTrailingDigits = true;
    }
    
    public SoundPoolEntry addSound(String path, final File file) {
        try {
            final String path2 = path;
            path = path.substring(0, path.indexOf("."));
            if (this.trimTrailingDigits) {
                while (Character.isDigit(path.charAt(path.length() - 1))) {
                    path = path.substring(0, path.length() - 1);
                }
            }
            path = path.replaceAll("/", ".");
            if (!this.nameToSoundPoolEntriesMapping.containsKey(path)) {
                this.nameToSoundPoolEntriesMapping.put(path, new ArrayList<SoundPoolEntry>());
            }
            final SoundPoolEntry soundPoolEntry = new SoundPoolEntry(path2, file.toURI().toURL());
            this.nameToSoundPoolEntriesMapping.get(path).add(soundPoolEntry);
            this.allSoundPoolEntries.add(soundPoolEntry);
            ++this.numberOfSoundPoolEntries;
            return soundPoolEntry;
        }
        catch (final MalformedURLException cause) {
            cause.printStackTrace();
            throw new RuntimeException(cause);
        }
    }
    
    public SoundPoolEntry getRandomSoundFromSoundPool(final String path) {
        final List list = this.nameToSoundPoolEntriesMapping.get(path);
        if (list == null) {
            return null;
        }
        return (SoundPoolEntry)list.get(this.rand.nextInt(list.size()));
    }
    
    public SoundPoolEntry getRandomSound() {
        if (this.allSoundPoolEntries.size() == 0) {
            return null;
        }
        return this.allSoundPoolEntries.get(this.rand.nextInt(this.allSoundPoolEntries.size()));
    }
}
