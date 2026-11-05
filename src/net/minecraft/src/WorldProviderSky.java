package net.minecraft.src;

public class WorldProviderSky extends WorldProvider {
    @Override
    public void registerWorldChunkManager() {
        this.worldChunkMgr = new WorldChunkManagerHell(BiomeGenBase.sky, 0.5, 0.0);
        this.worldType = 1;
    }

    @Override
    public IChunkProvider getChunkProvider() {
        return new ChunkProviderSky(this.worldObj, this.worldObj.getRandomSeed());
    }

    @Override
    public float calculateCelestialAngle(long time, float tickDelta) {
        return 0.0F;
    }

    @Override
    public float[] calcSunriseSunsetColors(float timeOfDay, float tickDelta) {
        return null;
    }

    @Override
    public Vec3D getFogColor(float timeOfDay, float tickDelta) {
        int i = 8421536;
        float f = MathHelper.cos(timeOfDay * (float) Math.PI * 2.0F) * 2.0F + 0.5F;
        if (f < 0.0F) {
            f = 0.0F;
        }

        if (f > 1.0F) {
            f = 1.0F;
        }

        float f1 = (i >> 16 & 0xFF) / 255.0F;
        float f2 = (i >> 8 & 0xFF) / 255.0F;
        float f3 = (i & 0xFF) / 255.0F;
        f1 *= f * 0.94F + 0.06F;
        f2 *= f * 0.94F + 0.06F;
        f3 *= f * 0.91F + 0.09F;
        return Vec3D.createVector(f1, f2, f3);
    }

    @Override
    public boolean hasGround() {
        return false;
    }

    @Override
    public float getCloudHeight() {
        return 8.0F;
    }

    public boolean canCoordinateBeSpawn(int x, int z) {
        int n = this.worldObj.getFirstUncoveredBlock(x, z);
        if (n == 0) {
            return false;
        }
        return Block.blocksList[n].blockMaterial.getIsSolid();
    }
}
