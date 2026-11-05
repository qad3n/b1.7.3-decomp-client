package net.minecraft.src;

public abstract class WorldProvider {
    public World worldObj;
    public WorldChunkManager worldChunkMgr;
    public boolean isNether = false;
    public boolean isHellWorld = false;
    public boolean hasNoSky = false;
    public float[] lightBrightnessTable = new float[16];
    public int worldType = 0;
    private float[] colorsSunriseSunset = new float[4];

    public final void registerWorld(World world) {
        this.worldObj = world;
        this.registerWorldChunkManager();
        this.generateLightBrightnessTable();
    }

    protected void generateLightBrightnessTable() {
        float f = 0.05F;

        for (int i = 0; i <= 15; i++) {
            float f1 = 1.0F - i / 15.0F;
            this.lightBrightnessTable[i] = (1.0F - f1) / (f1 * 3.0F + 1.0F) * (1.0F - f) + f;
        }
    }

    protected void registerWorldChunkManager() {
        this.worldChunkMgr = new WorldChunkManager(this.worldObj);
    }

    public IChunkProvider getChunkProvider() {
        return new ChunkProviderGenerate(this.worldObj, this.worldObj.getRandomSeed());
    }

    public boolean canCoordinateBeSpawn(int x, int z) {
        int i = this.worldObj.getFirstUncoveredBlock(x, z);
        return i == Block.sand.blockID;
    }

    public float calculateCelestialAngle(long time, float tickDelta) {
        int n = (int) (time % 24000L);
        float f = ((float) n + tickDelta) / 24000.0f - 0.25f;
        if (f < 0.0f) {
            f += 1.0f;
        }
        if (f > 1.0f) {
            f -= 1.0f;
        }
        float f2 = f;
        f = 1.0f - (float) ((Math.cos((double) f * Math.PI) + 1.0) / 2.0);
        f = f2 + (f - f2) / 3.0f;
        return f;
    }

    public float[] calcSunriseSunsetColors(float timeOfDay, float tickDelta) {
        float f = 0.4F;
        float f1 = MathHelper.cos(timeOfDay * (float) Math.PI * 2.0F) - 0.0F;
        float f2 = -0.0F;
        if (f1 >= f2 - f && f1 <= f2 + f) {
            float f3 = (f1 - f2) / f * 0.5F + 0.5F;
            float f4 = 1.0F - (1.0F - MathHelper.sin(f3 * (float) Math.PI)) * 0.99F;
            f4 *= f4;
            this.colorsSunriseSunset[0] = f3 * 0.3F + 0.7F;
            this.colorsSunriseSunset[1] = f3 * f3 * 0.7F + 0.2F;
            this.colorsSunriseSunset[2] = f3 * f3 * 0.0F + 0.2F;
            this.colorsSunriseSunset[3] = f4;
            return this.colorsSunriseSunset;
        } else {
            return null;
        }
    }

    public Vec3D getFogColor(float timeOfDay, float tickDelta) {
        float f = MathHelper.cos(timeOfDay * (float) Math.PI * 2.0F) * 2.0F + 0.5F;
        if (f < 0.0F) {
            f = 0.0F;
        }

        if (f > 1.0F) {
            f = 1.0F;
        }

        float f1 = 0.7529412F;
        float f2 = 0.84705883F;
        float f3 = 1.0F;
        f1 *= f * 0.94F + 0.06F;
        f2 *= f * 0.94F + 0.06F;
        f3 *= f * 0.91F + 0.09F;
        return Vec3D.createVector(f1, f2, f3);
    }

    public boolean canRespawnHere() {
        return true;
    }

    public static WorldProvider getProviderForDimension(int id) {
        if (id == -1) {
            return new WorldProviderHell();
        }
        if (id == 0) {
            return new WorldProviderSurface();
        }
        if (id == 1) {
            return new WorldProviderSky();
        }
        return null;
    }

    public float getCloudHeight() {
        return 108.0F;
    }

    public boolean hasGround() {
        return true;
    }
}
