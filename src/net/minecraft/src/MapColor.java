package net.minecraft.src;

public class MapColor {
    public static final MapColor[] mapColorArray = new MapColor[16];
    public static final MapColor airColor = new MapColor(0, 0);
    public static final MapColor grassColor = new MapColor(1, 8368696);
    public static final MapColor sandColor = new MapColor(2, 16247203);
    public static final MapColor clothColor = new MapColor(3, 0xA7A7A7);
    public static final MapColor tntColor = new MapColor(4, 0xFF0000);
    public static final MapColor iceColor = new MapColor(5, 0xA0A0FF);
    public static final MapColor ironColor = new MapColor(6, 0xA7A7A7);
    public static final MapColor foliageColor = new MapColor(7, 31744);
    public static final MapColor snowColor = new MapColor(8, 0xFFFFFF);
    public static final MapColor clayColor = new MapColor(9, 10791096);
    public static final MapColor dirtColor = new MapColor(10, 12020271);
    public static final MapColor stoneColor = new MapColor(11, 0x707070);
    public static final MapColor waterColor = new MapColor(12, 0x4040FF);
    public static final MapColor woodColor = new MapColor(13, 6837042);
    public final int colorValue;
    public final int colorIndex;

    private MapColor(int id, int color) {
        this.colorIndex = id;
        this.colorValue = color;
        MapColor.mapColorArray[id] = this;
    }
}

