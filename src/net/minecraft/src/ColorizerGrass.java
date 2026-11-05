package net.minecraft.src;

public class ColorizerGrass {
    private static int[] grassBuffer = new int[65536];

    public static void set(int[] colors) {
        grassBuffer = colors;
    }

    public static int getGrassColor(double temperature, double humidity) {
        humidity *= temperature;
        int i = (int)((1.0 - temperature) * 255.0);
        int j = (int)((1.0 - humidity) * 255.0);
        return grassBuffer[j << 8 | i];
    }
}
