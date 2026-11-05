package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.lwjgl.opengl.GL11;

public class EffectRenderer {
    protected World worldObj;
    private List<EntityFX>[] fxLayers = new List[4];
    private RenderEngine renderer;
    private Random rand = new Random();

    public EffectRenderer(World world, RenderEngine textureManager) {
        if (world != null) {
            this.worldObj = world;
        }
        this.renderer = textureManager;
        for (int i = 0; i < 4; ++i) {
            this.fxLayers[i] = new ArrayList<EntityFX>();
        }
    }

    public void addEffect(EntityFX particle) {
        int n = particle.getFXLayer();
        if (this.fxLayers[n].size() >= 4000) {
            this.fxLayers[n].remove(0);
        }
        this.fxLayers[n].add(particle);
    }

    public void updateEffects() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < this.fxLayers[i].size(); j++) {
                EntityFX entityfx = this.fxLayers[i].get(j);
                entityfx.onUpdate();
                if (entityfx.isDead) {
                    this.fxLayers[i].remove(j--);
                }
            }
        }
    }

    public void renderParticles(Entity camera, float tickDelta) {
        float f = MathHelper.cos(camera.rotationYaw * (float)Math.PI / 180.0f);
        float f2 = MathHelper.sin(camera.rotationYaw * (float)Math.PI / 180.0f);
        float f3 = -f2 * MathHelper.sin(camera.rotationPitch * (float)Math.PI / 180.0f);
        float f4 = f * MathHelper.sin(camera.rotationPitch * (float)Math.PI / 180.0f);
        float f5 = MathHelper.cos(camera.rotationPitch * (float)Math.PI / 180.0f);
        EntityFX.interpPosX = camera.lastTickPosX + (camera.posX - camera.lastTickPosX) * (double)tickDelta;
        EntityFX.interpPosY = camera.lastTickPosY + (camera.posY - camera.lastTickPosY) * (double)tickDelta;
        EntityFX.interpPosZ = camera.lastTickPosZ + (camera.posZ - camera.lastTickPosZ) * (double)tickDelta;
        for (int i = 0; i < 3; ++i) {
            if (this.fxLayers[i].size() == 0) continue;
            int n = 0;
            if (i == 0) {
                n = this.renderer.getTexture("/particles.png");
            }
            if (i == 1) {
                n = this.renderer.getTexture("/terrain.png");
            }
            if (i == 2) {
                n = this.renderer.getTexture("/gui/items.png");
            }
            GL11.glBindTexture(3553, n);
            Tessellator tessellator = Tessellator.instance;
            tessellator.startDrawingQuads();
            for (int j = 0; j < this.fxLayers[i].size(); ++j) {
                EntityFX entityFX = this.fxLayers[i].get(j);
                entityFX.renderParticle(tessellator, tickDelta, f, f5, f2, f3, f4);
            }
            tessellator.draw();
        }
    }

    public void renderLit(Entity camera, float tickDelta) {
        int n = 3;
        if (this.fxLayers[n].size() == 0) {
            return;
        }
        Tessellator tessellator = Tessellator.instance;
        for (int i = 0; i < this.fxLayers[n].size(); ++i) {
            EntityFX entityFX = this.fxLayers[n].get(i);
            entityFX.renderParticle(tessellator, tickDelta, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
        }
    }

    public void clearEffects(World world) {
        this.worldObj = world;
        for (int i = 0; i < 4; ++i) {
            this.fxLayers[i].clear();
        }
    }

    public void addBlockDestroyEffects(int x, int y, int z, int block, int metadata) {
        if (block == 0) {
            return;
        }
        Block block2 = Block.blocksList[block];
        int n = 4;
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                for (int k = 0; k < n; ++k) {
                    double d = (double)x + ((double)i + 0.5) / (double)n;
                    double d2 = (double)y + ((double)j + 0.5) / (double)n;
                    double d3 = (double)z + ((double)k + 0.5) / (double)n;
                    int n2 = this.rand.nextInt(6);
                    this.addEffect(new EntityDiggingFX(this.worldObj, d, d2, d3, d - (double)x - 0.5, d2 - (double)y - 0.5, d3 - (double)z - 0.5, block2, n2, metadata).init(x, y, z));
                }
            }
        }
    }

    public void addBlockHitEffects(int x, int y, int z, int face) {
        int n = this.worldObj.getBlockId(x, y, z);
        if (n == 0) {
            return;
        }
        Block block = Block.blocksList[n];
        float f = 0.1f;
        double d = (double)x + this.rand.nextDouble() * (block.maxX - block.minX - (double)(f * 2.0f)) + (double)f + block.minX;
        double d2 = (double)y + this.rand.nextDouble() * (block.maxY - block.minY - (double)(f * 2.0f)) + (double)f + block.minY;
        double d3 = (double)z + this.rand.nextDouble() * (block.maxZ - block.minZ - (double)(f * 2.0f)) + (double)f + block.minZ;
        if (face == 0) {
            d2 = (double)y + block.minY - (double)f;
        }
        if (face == 1) {
            d2 = (double)y + block.maxY + (double)f;
        }
        if (face == 2) {
            d3 = (double)z + block.minZ - (double)f;
        }
        if (face == 3) {
            d3 = (double)z + block.maxZ + (double)f;
        }
        if (face == 4) {
            d = (double)x + block.minX - (double)f;
        }
        if (face == 5) {
            d = (double)x + block.maxX + (double)f;
        }
        this.addEffect(new EntityDiggingFX(this.worldObj, d, d2, d3, 0.0, 0.0, 0.0, block, face, this.worldObj.getBlockMetadata(x, y, z)).init(x, y, z).multiplyVelocity(0.2f).multiplySize(0.6f));
    }

    public String getStatistics() {
        return "" + (this.fxLayers[0].size() + this.fxLayers[1].size() + this.fxLayers[2].size());
    }
}

