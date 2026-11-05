package net.minecraft.src;

import java.util.Random;

public abstract class BlockFluid
extends Block {
    protected BlockFluid(int n, Material material) {
        super(n, (material == Material.lava ? 14 : 12) * 16 + 13, material);
        float f = 0.0f;
        float f2 = 0.0f;
        this.setBlockBounds(0.0f + f2, 0.0f + f, 0.0f + f2, 1.0f + f2, 1.0f + f, 1.0f + f2);
        this.setTickOnLoad(true);
    }

    public int colorMultiplier(IBlockAccess world, int x, int y, int z) {
        return 0xFFFFFF;
    }

    public static float getPercentAir(int state) {
        if (state >= 8) {
            state = 0;
        }
        float f = (float)(state + 1) / 9.0f;
        return f;
    }

    public int getBlockTextureFromSide(int face) {
        if (face == 0 || face == 1) {
            return this.blockIndexInTexture;
        }
        return this.blockIndexInTexture + 1;
    }

    protected int getFlowDecay(World world, int x, int y, int z) {
        if (world.getBlockMaterial(x, y, z) != this.blockMaterial) {
            return -1;
        }
        return world.getBlockMetadata(x, y, z);
    }

    protected int getEffectiveFlowDecay(IBlockAccess world, int x, int y, int z) {
        if (world.getBlockMaterial(x, y, z) != this.blockMaterial) {
            return -1;
        }
        int n = world.getBlockMetadata(x, y, z);
        if (n >= 8) {
            n = 0;
        }
        return n;
    }

    public boolean renderAsNormalBlock() {
        return false;
    }

    public boolean isOpaqueCube() {
        return false;
    }

    public boolean canCollideCheck(int metadata, boolean allowLiquids) {
        return allowLiquids && metadata == 0;
    }

    public boolean getIsBlockSolid(IBlockAccess world, int x, int y, int z, int face) {
        Material material = world.getBlockMaterial(x, y, z);
        if (material == this.blockMaterial) {
            return false;
        }
        if (material == Material.ice) {
            return false;
        }
        if (face == 1) {
            return true;
        }
        return super.getIsBlockSolid(world, x, y, z, face);
    }

    public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int face) {
        Material material = world.getBlockMaterial(x, y, z);
        if (material == this.blockMaterial) {
            return false;
        }
        if (material == Material.ice) {
            return false;
        }
        if (face == 1) {
            return true;
        }
        return super.shouldSideBeRendered(world, x, y, z, face);
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        return null;
    }

    public int getRenderType() {
        return 4;
    }

    public int idDropped(int metadata, Random random) {
        return 0;
    }

    public int quantityDropped(Random random) {
        return 0;
    }

    private Vec3D getFlowVector(IBlockAccess world, int x, int y, int z) {
        int n;
        Vec3D vec3D = Vec3D.createVector(0.0, 0.0, 0.0);
        int n2 = this.getEffectiveFlowDecay(world, x, y, z);
        for (n = 0; n < 4; ++n) {
            int n3;
            int n4;
            int n5 = x;
            int n6 = y;
            int n7 = z;
            if (n == 0) {
                --n5;
            }
            if (n == 1) {
                --n7;
            }
            if (n == 2) {
                ++n5;
            }
            if (n == 3) {
                ++n7;
            }
            if ((n4 = this.getEffectiveFlowDecay(world, n5, n6, n7)) < 0) {
                if (world.getBlockMaterial(n5, n6, n7).getIsSolid() || (n4 = this.getEffectiveFlowDecay(world, n5, n6 - 1, n7)) < 0) continue;
                n3 = n4 - (n2 - 8);
                vec3D = vec3D.addVector((n5 - x) * n3, (n6 - y) * n3, (n7 - z) * n3);
                continue;
            }
            if (n4 < 0) continue;
            n3 = n4 - n2;
            vec3D = vec3D.addVector((n5 - x) * n3, (n6 - y) * n3, (n7 - z) * n3);
        }
        if (world.getBlockMetadata(x, y, z) >= 8) {
            n = 0;
            if (n != 0 || this.getIsBlockSolid(world, x, y, z - 1, 2)) {
                n = 1;
            }
            if (n != 0 || this.getIsBlockSolid(world, x, y, z + 1, 3)) {
                n = 1;
            }
            if (n != 0 || this.getIsBlockSolid(world, x - 1, y, z, 4)) {
                n = 1;
            }
            if (n != 0 || this.getIsBlockSolid(world, x + 1, y, z, 5)) {
                n = 1;
            }
            if (n != 0 || this.getIsBlockSolid(world, x, y + 1, z - 1, 2)) {
                n = 1;
            }
            if (n != 0 || this.getIsBlockSolid(world, x, y + 1, z + 1, 3)) {
                n = 1;
            }
            if (n != 0 || this.getIsBlockSolid(world, x - 1, y + 1, z, 4)) {
                n = 1;
            }
            if (n != 0 || this.getIsBlockSolid(world, x + 1, y + 1, z, 5)) {
                n = 1;
            }
            if (n != 0) {
                vec3D = vec3D.normalize().addVector(0.0, -6.0, 0.0);
            }
        }
        vec3D = vec3D.normalize();
        return vec3D;
    }

    public void velocityToAddToEntity(World world, int x, int y, int z, Entity entity, Vec3D velocity) {
        Vec3D vec3D = this.getFlowVector(world, x, y, z);
        velocity.xCoord += vec3D.xCoord;
        velocity.yCoord += vec3D.yCoord;
        velocity.zCoord += vec3D.zCoord;
    }

    public int tickRate() {
        if (this.blockMaterial == Material.water) {
            return 5;
        }
        if (this.blockMaterial == Material.lava) {
            return 30;
        }
        return 0;
    }

    @Override
public float getBlockBrightness(IBlockAccess world, int x, int y, int z) {
    float f = world.getLightBrightness(x, y, z);
    float f1 = world.getLightBrightness(x, y + 1, z);
    return f > f1 ? f : f1;
}

    public void updateTick(World world, int x, int y, int z, Random random) {
        super.updateTick(world, x, y, z, random);
    }

    public int getRenderBlockPass() {
        return this.blockMaterial == Material.water ? 1 : 0;
    }

    @Override
public void randomDisplayTick(World world, int x, int y, int z, Random random) {
    if (this.blockMaterial == Material.water && random.nextInt(64) == 0) {
        int i = world.getBlockMetadata(x, y, z);
        if (i > 0 && i < 8) {
            world.playSoundEffect(x + 0.5F, y + 0.5F, z + 0.5F, "liquid.water", random.nextFloat() * 0.25F + 0.75F, random.nextFloat() * 1.0F + 0.5F);
        }
    }
    if (this.blockMaterial == Material.lava && world.getBlockMaterial(x, y + 1, z) == Material.air && !world.isBlockOpaqueCube(x, y + 1, z) && random.nextInt(100) == 0) {
        double d2 = x + random.nextFloat();
        double d0 = y + this.maxY;
        double d1 = z + random.nextFloat();
        world.spawnParticle("lava", d2, d0, d1, 0.0, 0.0, 0.0);
    }
}

    public static double getFlowAngle(IBlockAccess world, int x, int y, int z, Material material) {
        Vec3D vec3D = null;
        if (material == Material.water) {
            vec3D = ((BlockFluid)Block.waterMoving).getFlowVector(world, x, y, z);
        }
        if (material == Material.lava) {
            vec3D = ((BlockFluid)Block.lavaMoving).getFlowVector(world, x, y, z);
        }
        if (vec3D.xCoord == 0.0 && vec3D.zCoord == 0.0) {
            return -1000.0;
        }
        return Math.atan2(vec3D.zCoord, vec3D.xCoord) - 1.5707963267948966;
    }

    public void onBlockAdded(World world, int x, int y, int z) {
        this.checkForHarden(world, x, y, z);
    }

    public void onNeighborBlockChange(World world, int x, int y, int z, int neighborBlock) {
        this.checkForHarden(world, x, y, z);
    }

    private void checkForHarden(World world, int x, int y, int z) {
        if (world.getBlockId(x, y, z) != this.blockID) {
            return;
        }
        if (this.blockMaterial == Material.lava) {
            boolean bl = false;
            if (bl || world.getBlockMaterial(x, y, z - 1) == Material.water) {
                bl = true;
            }
            if (bl || world.getBlockMaterial(x, y, z + 1) == Material.water) {
                bl = true;
            }
            if (bl || world.getBlockMaterial(x - 1, y, z) == Material.water) {
                bl = true;
            }
            if (bl || world.getBlockMaterial(x + 1, y, z) == Material.water) {
                bl = true;
            }
            if (bl || world.getBlockMaterial(x, y + 1, z) == Material.water) {
                bl = true;
            }
            if (bl) {
                int n = world.getBlockMetadata(x, y, z);
                if (n == 0) {
                    world.setBlockWithNotify(x, y, z, Block.obsidian.blockID);
                } else if (n <= 4) {
                    world.setBlockWithNotify(x, y, z, Block.cobblestone.blockID);
                }
                this.triggerLavaMixEffects(world, x, y, z);
            }
        }
    }

    protected void triggerLavaMixEffects(World world, int x, int y, int z) {
        world.playSoundEffect((float)x + 0.5f, (float)y + 0.5f, (float)z + 0.5f, "random.fizz", 0.5f, 2.6f + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8f);
        for (int i = 0; i < 8; ++i) {
            world.spawnParticle("largesmoke", (double)x + Math.random(), (double)y + 1.2, (double)z + Math.random(), 0.0, 0.0, 0.0);
        }
    }
}

