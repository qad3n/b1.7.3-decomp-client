package net.minecraft.src;

import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

public class RenderBlocks {
    private IBlockAccess blockAccess;
    private int overrideBlockTexture = -1;
    private boolean flipTexture = false;
    private boolean renderAllFaces = false;
    public static boolean fancyGrass = true;
    public boolean updateColor = true;
    private int northFaceRotation = 0;
    private int southFaceRotation = 0;
    private int westFaceRotation = 0;
    private int eastFaceRotation = 0;
    private int topFaceRotation = 0;
    private int bottomFaceRotation = 0;
    private boolean enableAO;
    private float lightValueOwn;
    private float aoLightValueXNeg;
    private float aoLightValueYNeg;
    private float aoLightValueZNeg;
    private float aoLightValueXPos;
    private float aoLightValueYPos;
    private float aoLightValueZPos;
    private float bottomNorthWestVertexAmbientOcclusionLight;
    private float bottomWestEdgeAmbientOcclusionLight;
    private float bottomSouthWestVertexAmbientOcclusionLight;
    private float bottomNorthEdgeAmbientOcclusionLight;
    private float bottomSouthEdgeAmbientOcclusionLight;
    private float bottomNorthEastVertexAmbientOcclusionLight;
    private float bottomEastEdgeAmbientOcclusionLight;
    private float bottomSouthEastVertexAmbientOcclusionLight;
    private float topNorthWestVertexAmbientOcclusionLight;
    private float topWestEdgeAmbientOcclusionLight;
    private float topSouthWestVertexAmbientOcclusionLight;
    private float topNorthEdgeAmbientOcclusionLight;
    private float topNorthEastVertexAmbientOcclusionLight;
    private float topEastEdgeAmbientOcclusionLight;
    private float topSouthEdgeAmbientOcclusionLight;
    private float topSouthEastVertexAmbientOcclusionLight;
    private float northWestEdgeAmbientOcclusionLight;
    private float northEastEdgeAmbientOcclusionLight;
    private float southWestEdgeAmbientOcclusionLight;
    private float southEastEdgeAmbientOcclusionLight;
    private int ambientOcclusionLevel = 1;
    private float colorRedTopLeft;
    private float colorRedBottomLeft;
    private float colorRedBottomRight;
    private float colorRedTopRight;
    private float colorGreenTopLeft;
    private float colorGreenBottomLeft;
    private float colorGreenBottomRight;
    private float colorGreenTopRight;
    private float colorBlueTopLeft;
    private float colorBlueBottomLeft;
    private float colorBlueBottomRight;
    private float colorBlueTopRight;
    private boolean topNorthEdgeTranslucent;
    private boolean topEastEdgeTranslucent;
    private boolean topWestEdgeTranslucent;
    private boolean topSouthEdgeTranslucent;
    private boolean northWestEdgeTranslucent;
    private boolean southEastEdgeTranslucent;
    private boolean southWestEdgeTranslucent;
    private boolean northEastEdgeTranslucent;
    private boolean bottomNorthEdgeTranslucent;
    private boolean bottomEastEdgeTranslucent;
    private boolean bottomWestEdgeTranslucent;
    private boolean bottomSouthEdgeTranslucent;

    public RenderBlocks(IBlockAccess world) {
        this.blockAccess = world;
    }

    public RenderBlocks() {
    }

    public void renderBlockUsingTexture(Block block, int x, int y, int z, int sprite) {
        this.overrideBlockTexture = sprite;
        this.renderBlockByRenderType(block, x, y, z);
        this.overrideBlockTexture = -1;
    }

    public void renderMovingBlock(Block block, int x, int y, int z) {
        this.renderAllFaces = true;
        this.renderBlockByRenderType(block, x, y, z);
        this.renderAllFaces = false;
    }

    public boolean renderBlockByRenderType(Block block, int x, int y, int z) {
        int n = block.getRenderType();
        block.setBlockBoundsBasedOnState(this.blockAccess, x, y, z);
        if (n == 0) {
            return this.renderStandardBlock(block, x, y, z);
        }
        if (n == 4) {
            return this.renderBlockFluids(block, x, y, z);
        }
        if (n == 13) {
            return this.renderBlockCactus(block, x, y, z);
        }
        if (n == 1) {
            return this.renderBlockReed(block, x, y, z);
        }
        if (n == 6) {
            return this.renderBlockCrops(block, x, y, z);
        }
        if (n == 2) {
            return this.renderBlockTorch(block, x, y, z);
        }
        if (n == 3) {
            return this.renderBlockFire(block, x, y, z);
        }
        if (n == 5) {
            return this.renderBlockRedstoneWire(block, x, y, z);
        }
        if (n == 8) {
            return this.renderBlockLadder(block, x, y, z);
        }
        if (n == 7) {
            return this.renderBlockDoor(block, x, y, z);
        }
        if (n == 9) {
            return this.renderBlockMinecartTrack((BlockRail)block, x, y, z);
        }
        if (n == 10) {
            return this.renderBlockStairs(block, x, y, z);
        }
        if (n == 11) {
            return this.renderBlockFence(block, x, y, z);
        }
        if (n == 12) {
            return this.renderBlockLever(block, x, y, z);
        }
        if (n == 14) {
            return this.renderBlockBed(block, x, y, z);
        }
        if (n == 15) {
            return this.renderBlockRepeater(block, x, y, z);
        }
        if (n == 16) {
            return this.tesselatePistonBase(block, x, y, z, false);
        }
        if (n == 17) {
            return this.tesselatePistonHead(block, x, y, z, true);
        }
        return false;
    }

    private boolean renderBlockBed(Block block, int x, int y, int z) {
        Tessellator tessellator = Tessellator.instance;
        int n = this.blockAccess.getBlockMetadata(x, y, z);
        int n2 = BlockBed.getDirectionFromMetadata(n);
        boolean bl = BlockBed.isBlockFootOfBed(n);
        float f = 0.5f;
        float f2 = 1.0f;
        float f3 = 0.8f;
        float f4 = 0.6f;
        float f5 = f2;
        float f6 = f2;
        float f7 = f2;
        float f8 = f;
        float f9 = f3;
        float f10 = f4;
        float f11 = f;
        float f12 = f3;
        float f13 = f4;
        float f14 = f;
        float f15 = f3;
        float f16 = f4;
        float f17 = block.getBlockBrightness(this.blockAccess, x, y, z);
        tessellator.setColorOpaque_F(f8 * f17, f11 * f17, f14 * f17);
        int n3 = block.getBlockTexture(this.blockAccess, x, y, z, 0);
        int n4 = (n3 & 0xF) << 4;
        int n5 = n3 & 0xF0;
        double d = (float)n4 / 256.0f;
        double d2 = ((double)(n4 + 16) - 0.01) / 256.0;
        double d3 = (float)n5 / 256.0f;
        double d4 = ((double)(n5 + 16) - 0.01) / 256.0;
        double d5 = (double)x + block.minX;
        double d6 = (double)x + block.maxX;
        double d7 = (double)y + block.minY + 0.1875;
        double d8 = (double)z + block.minZ;
        double d9 = (double)z + block.maxZ;
        tessellator.addVertexWithUV(d5, d7, d9, d, d4);
        tessellator.addVertexWithUV(d5, d7, d8, d, d3);
        tessellator.addVertexWithUV(d6, d7, d8, d2, d3);
        tessellator.addVertexWithUV(d6, d7, d9, d2, d4);
        float f18 = block.getBlockBrightness(this.blockAccess, x, y + 1, z);
        tessellator.setColorOpaque_F(f5 * f18, f6 * f18, f7 * f18);
        n4 = block.getBlockTexture(this.blockAccess, x, y, z, 1);
        n5 = (n4 & 0xF) << 4;
        int n6 = n4 & 0xF0;
        double d10 = (float)n5 / 256.0f;
        double d11 = ((double)(n5 + 16) - 0.01) / 256.0;
        double d12 = (float)n6 / 256.0f;
        double d13 = ((double)(n6 + 16) - 0.01) / 256.0;
        double d14 = d10;
        double d15 = d11;
        double d16 = d12;
        double d17 = d12;
        double d18 = d10;
        double d19 = d11;
        double d20 = d13;
        double d21 = d13;
        if (n2 == 0) {
            d15 = d10;
            d16 = d13;
            d18 = d11;
            d21 = d12;
        } else if (n2 == 2) {
            d14 = d11;
            d17 = d13;
            d19 = d10;
            d20 = d12;
        } else if (n2 == 3) {
            d14 = d11;
            d17 = d13;
            d19 = d10;
            d20 = d12;
            d15 = d10;
            d16 = d13;
            d18 = d11;
            d21 = d12;
        }
        double d22 = (double)x + block.minX;
        double d23 = (double)x + block.maxX;
        double d24 = (double)y + block.maxY;
        double d25 = (double)z + block.minZ;
        double d26 = (double)z + block.maxZ;
        tessellator.addVertexWithUV(d23, d24, d26, d18, d20);
        tessellator.addVertexWithUV(d23, d24, d25, d14, d16);
        tessellator.addVertexWithUV(d22, d24, d25, d15, d17);
        tessellator.addVertexWithUV(d22, d24, d26, d19, d21);
        int n7 = ModelBed.TO_DIR[n2];
        if (bl) {
            n7 = ModelBed.TO_DIR[ModelBed.OPPOSITE[n2]];
        }
        n4 = 4;
        switch (n2) {
            case 2: {
                break;
            }
            case 0: {
                n4 = 5;
                break;
            }
            case 3: {
                n4 = 2;
                break;
            }
            case 1: {
                n4 = 3;
            }
        }
        if (n7 != 2 && (this.renderAllFaces || block.shouldSideBeRendered(this.blockAccess, x, y, z - 1, 2))) {
            float f19 = block.getBlockBrightness(this.blockAccess, x, y, z - 1);
            if (block.minZ > 0.0) {
                f19 = f17;
            }
            tessellator.setColorOpaque_F(f9 * f19, f12 * f19, f15 * f19);
            this.flipTexture = n4 == 2;
            this.renderEastFace(block, x, y, z, block.getBlockTexture(this.blockAccess, x, y, z, 2));
        }
        if (n7 != 3 && (this.renderAllFaces || block.shouldSideBeRendered(this.blockAccess, x, y, z + 1, 3))) {
            float f20 = block.getBlockBrightness(this.blockAccess, x, y, z + 1);
            if (block.maxZ < 1.0) {
                f20 = f17;
            }
            tessellator.setColorOpaque_F(f9 * f20, f12 * f20, f15 * f20);
            this.flipTexture = n4 == 3;
            this.renderWestFace(block, x, y, z, block.getBlockTexture(this.blockAccess, x, y, z, 3));
        }
        if (n7 != 4 && (this.renderAllFaces || block.shouldSideBeRendered(this.blockAccess, x - 1, y, z, 4))) {
            float f21 = block.getBlockBrightness(this.blockAccess, x - 1, y, z);
            if (block.minX > 0.0) {
                f21 = f17;
            }
            tessellator.setColorOpaque_F(f10 * f21, f13 * f21, f16 * f21);
            this.flipTexture = n4 == 4;
            this.renderNorthFace(block, x, y, z, block.getBlockTexture(this.blockAccess, x, y, z, 4));
        }
        if (n7 != 5 && (this.renderAllFaces || block.shouldSideBeRendered(this.blockAccess, x + 1, y, z, 5))) {
            float f22 = block.getBlockBrightness(this.blockAccess, x + 1, y, z);
            if (block.maxX < 1.0) {
                f22 = f17;
            }
            tessellator.setColorOpaque_F(f10 * f22, f13 * f22, f16 * f22);
            this.flipTexture = n4 == 5;
            this.renderSouthFace(block, x, y, z, block.getBlockTexture(this.blockAccess, x, y, z, 5));
        }
        this.flipTexture = false;
        return true;
    }

    public boolean renderBlockTorch(Block block, int x, int y, int z) {
        int n = this.blockAccess.getBlockMetadata(x, y, z);
        Tessellator tessellator = Tessellator.instance;
        float f = block.getBlockBrightness(this.blockAccess, x, y, z);
        if (Block.lightValue[block.blockID] > 0) {
            f = 1.0f;
        }
        tessellator.setColorOpaque_F(f, f, f);
        double d = 0.4f;
        double d2 = 0.5 - d;
        double d3 = 0.2f;
        if (n == 1) {
            this.renderTorchAtAngle(block, (double)x - d2, (double)y + d3, z, -d, 0.0);
        } else if (n == 2) {
            this.renderTorchAtAngle(block, (double)x + d2, (double)y + d3, z, d, 0.0);
        } else if (n == 3) {
            this.renderTorchAtAngle(block, x, (double)y + d3, (double)z - d2, 0.0, -d);
        } else if (n == 4) {
            this.renderTorchAtAngle(block, x, (double)y + d3, (double)z + d2, 0.0, d);
        } else {
            this.renderTorchAtAngle(block, x, y, z, 0.0, 0.0);
        }
        return true;
    }

    private boolean renderBlockRepeater(Block block, int x, int y, int z) {
        int n = this.blockAccess.getBlockMetadata(x, y, z);
        int n2 = n & 3;
        int n3 = (n & 0xC) >> 2;
        this.renderStandardBlock(block, x, y, z);
        Tessellator tessellator = Tessellator.instance;
        float f = block.getBlockBrightness(this.blockAccess, x, y, z);
        if (Block.lightValue[block.blockID] > 0) {
            f = (f + 1.0f) * 0.5f;
        }
        tessellator.setColorOpaque_F(f, f, f);
        double d = -0.1875;
        double d2 = 0.0;
        double d3 = 0.0;
        double d4 = 0.0;
        double d5 = 0.0;
        switch (n2) {
            case 0: {
                d5 = -0.3125;
                d3 = BlockRedstoneRepeater.RANDOM_DISPLAY[n3];
                break;
            }
            case 2: {
                d5 = 0.3125;
                d3 = -BlockRedstoneRepeater.RANDOM_DISPLAY[n3];
                break;
            }
            case 3: {
                d4 = -0.3125;
                d2 = BlockRedstoneRepeater.RANDOM_DISPLAY[n3];
                break;
            }
            case 1: {
                d4 = 0.3125;
                d2 = -BlockRedstoneRepeater.RANDOM_DISPLAY[n3];
            }
        }
        this.renderTorchAtAngle(block, (double)x + d2, (double)y + d, (double)z + d3, 0.0, 0.0);
        this.renderTorchAtAngle(block, (double)x + d4, (double)y + d, (double)z + d5, 0.0, 0.0);
        int n4 = block.getBlockTextureFromSide(1);
        int n5 = (n4 & 0xF) << 4;
        int n6 = n4 & 0xF0;
        double d6 = (float)n5 / 256.0f;
        double d7 = ((float)n5 + 15.99f) / 256.0f;
        double d8 = (float)n6 / 256.0f;
        double d9 = ((float)n6 + 15.99f) / 256.0f;
        float f2 = 0.125f;
        float f3 = x + 1;
        float f4 = x + 1;
        float f5 = x + 0;
        float f6 = x + 0;
        float f7 = z + 0;
        float f8 = z + 1;
        float f9 = z + 1;
        float f10 = z + 0;
        float f11 = (float)y + f2;
        if (n2 == 2) {
            f3 = f4 = (float)(x + 0);
            f5 = f6 = (float)(x + 1);
            f7 = f10 = (float)(z + 1);
            f8 = f9 = (float)(z + 0);
        } else if (n2 == 3) {
            f3 = f6 = (float)(x + 0);
            f4 = f5 = (float)(x + 1);
            f7 = f8 = (float)(z + 0);
            f9 = f10 = (float)(z + 1);
        } else if (n2 == 1) {
            f3 = f6 = (float)(x + 1);
            f4 = f5 = (float)(x + 0);
            f7 = f8 = (float)(z + 1);
            f9 = f10 = (float)(z + 0);
        }
        tessellator.addVertexWithUV(f6, f11, f10, d6, d8);
        tessellator.addVertexWithUV(f5, f11, f9, d6, d9);
        tessellator.addVertexWithUV(f4, f11, f8, d7, d9);
        tessellator.addVertexWithUV(f3, f11, f7, d7, d8);
        return true;
    }

    public void renderExtendedPistonBase(Block block, int x, int y, int z) {
        this.renderAllFaces = true;
        this.tesselatePistonBase(block, x, y, z, true);
        this.renderAllFaces = false;
    }

    private boolean tesselatePistonBase(Block block, int x, int y, int z, boolean extended) {
        int n = this.blockAccess.getBlockMetadata(x, y, z);
        boolean bl = extended || (n & 8) != 0;
        int n2 = BlockPistonBase.getFacing(n);
        if (bl) {
            switch (n2) {
                case 0: {
                    this.northFaceRotation = 3;
                    this.southFaceRotation = 3;
                    this.westFaceRotation = 3;
                    this.eastFaceRotation = 3;
                    block.setBlockBounds(0.0f, 0.25f, 0.0f, 1.0f, 1.0f, 1.0f);
                    break;
                }
                case 1: {
                    block.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.75f, 1.0f);
                    break;
                }
                case 2: {
                    this.westFaceRotation = 1;
                    this.eastFaceRotation = 2;
                    block.setBlockBounds(0.0f, 0.0f, 0.25f, 1.0f, 1.0f, 1.0f);
                    break;
                }
                case 3: {
                    this.westFaceRotation = 2;
                    this.eastFaceRotation = 1;
                    this.topFaceRotation = 3;
                    this.bottomFaceRotation = 3;
                    block.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.75f);
                    break;
                }
                case 4: {
                    this.northFaceRotation = 1;
                    this.southFaceRotation = 2;
                    this.topFaceRotation = 2;
                    this.bottomFaceRotation = 1;
                    block.setBlockBounds(0.25f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
                    break;
                }
                case 5: {
                    this.northFaceRotation = 2;
                    this.southFaceRotation = 1;
                    this.topFaceRotation = 1;
                    this.bottomFaceRotation = 2;
                    block.setBlockBounds(0.0f, 0.0f, 0.0f, 0.75f, 1.0f, 1.0f);
                }
            }
            this.renderStandardBlock(block, x, y, z);
            this.northFaceRotation = 0;
            this.southFaceRotation = 0;
            this.westFaceRotation = 0;
            this.eastFaceRotation = 0;
            this.topFaceRotation = 0;
            this.bottomFaceRotation = 0;
            block.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
        } else {
            switch (n2) {
                case 0: {
                    this.northFaceRotation = 3;
                    this.southFaceRotation = 3;
                    this.westFaceRotation = 3;
                    this.eastFaceRotation = 3;
                    break;
                }
                case 1: {
                    break;
                }
                case 2: {
                    this.westFaceRotation = 1;
                    this.eastFaceRotation = 2;
                    break;
                }
                case 3: {
                    this.westFaceRotation = 2;
                    this.eastFaceRotation = 1;
                    this.topFaceRotation = 3;
                    this.bottomFaceRotation = 3;
                    break;
                }
                case 4: {
                    this.northFaceRotation = 1;
                    this.southFaceRotation = 2;
                    this.topFaceRotation = 2;
                    this.bottomFaceRotation = 1;
                    break;
                }
                case 5: {
                    this.northFaceRotation = 2;
                    this.southFaceRotation = 1;
                    this.topFaceRotation = 1;
                    this.bottomFaceRotation = 2;
                }
            }
            this.renderStandardBlock(block, x, y, z);
            this.northFaceRotation = 0;
            this.southFaceRotation = 0;
            this.westFaceRotation = 0;
            this.eastFaceRotation = 0;
            this.topFaceRotation = 0;
            this.bottomFaceRotation = 0;
        }
        return true;
    }

    private void tesselatePistonSideX(double minX, double maxX, double minY, double maxY, double minZ, double maxZ, float color, double length) {
        int n = 108;
        if (this.overrideBlockTexture >= 0) {
            n = this.overrideBlockTexture;
        }
        int n2 = (n & 0xF) << 4;
        int n3 = n & 0xF0;
        Tessellator tessellator = Tessellator.instance;
        double d = (float)(n2 + 0) / 256.0f;
        double d2 = (float)(n3 + 0) / 256.0f;
        double d3 = ((double)n2 + length - 0.01) / 256.0;
        double d4 = ((double)((float)n3 + 4.0f) - 0.01) / 256.0;
        tessellator.setColorOpaque_F(color, color, color);
        tessellator.addVertexWithUV(minX, maxY, minZ, d3, d2);
        tessellator.addVertexWithUV(minX, minY, minZ, d, d2);
        tessellator.addVertexWithUV(maxX, minY, maxZ, d, d4);
        tessellator.addVertexWithUV(maxX, maxY, maxZ, d3, d4);
    }

    private void tesselatePistonSideY(double minX, double maxX, double minY, double maxY, double minZ, double maxZ, float color, double length) {
        int n = 108;
        if (this.overrideBlockTexture >= 0) {
            n = this.overrideBlockTexture;
        }
        int n2 = (n & 0xF) << 4;
        int n3 = n & 0xF0;
        Tessellator tessellator = Tessellator.instance;
        double d = (float)(n2 + 0) / 256.0f;
        double d2 = (float)(n3 + 0) / 256.0f;
        double d3 = ((double)n2 + length - 0.01) / 256.0;
        double d4 = ((double)((float)n3 + 4.0f) - 0.01) / 256.0;
        tessellator.setColorOpaque_F(color, color, color);
        tessellator.addVertexWithUV(minX, minY, maxZ, d3, d2);
        tessellator.addVertexWithUV(minX, minY, minZ, d, d2);
        tessellator.addVertexWithUV(maxX, maxY, minZ, d, d4);
        tessellator.addVertexWithUV(maxX, maxY, maxZ, d3, d4);
    }

    private void tesselatePistonSideZ(double minX, double maxX, double minY, double maxY, double minZ, double maxZ, float color, double length) {
        int n = 108;
        if (this.overrideBlockTexture >= 0) {
            n = this.overrideBlockTexture;
        }
        int n2 = (n & 0xF) << 4;
        int n3 = n & 0xF0;
        Tessellator tessellator = Tessellator.instance;
        double d = (float)(n2 + 0) / 256.0f;
        double d2 = (float)(n3 + 0) / 256.0f;
        double d3 = ((double)n2 + length - 0.01) / 256.0;
        double d4 = ((double)((float)n3 + 4.0f) - 0.01) / 256.0;
        tessellator.setColorOpaque_F(color, color, color);
        tessellator.addVertexWithUV(maxX, minY, minZ, d3, d2);
        tessellator.addVertexWithUV(minX, minY, minZ, d, d2);
        tessellator.addVertexWithUV(minX, maxY, maxZ, d, d4);
        tessellator.addVertexWithUV(maxX, maxY, maxZ, d3, d4);
    }

    public void renderPistonHead(Block block, int x, int y, int z, boolean isShort) {
        this.renderAllFaces = true;
        this.tesselatePistonHead(block, x, y, z, isShort);
        this.renderAllFaces = false;
    }

    private boolean tesselatePistonHead(Block block, int x, int y, int z, boolean isShort) {
        int n = this.blockAccess.getBlockMetadata(x, y, z);
        int n2 = BlockPistonExtension.getFacing(n);
        float f = block.getBlockBrightness(this.blockAccess, x, y, z);
        float f2 = isShort ? 1.0f : 0.5f;
        double d = isShort ? 16.0 : 8.0;
        switch (n2) {
            case 0: {
                this.northFaceRotation = 3;
                this.southFaceRotation = 3;
                this.westFaceRotation = 3;
                this.eastFaceRotation = 3;
                block.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.25f, 1.0f);
                this.renderStandardBlock(block, x, y, z);
                this.tesselatePistonSideX((float)x + 0.375f, (float)x + 0.625f, (float)y + 0.25f, (float)y + 0.25f + f2, (float)z + 0.625f, (float)z + 0.625f, f * 0.8f, d);
                this.tesselatePistonSideX((float)x + 0.625f, (float)x + 0.375f, (float)y + 0.25f, (float)y + 0.25f + f2, (float)z + 0.375f, (float)z + 0.375f, f * 0.8f, d);
                this.tesselatePistonSideX((float)x + 0.375f, (float)x + 0.375f, (float)y + 0.25f, (float)y + 0.25f + f2, (float)z + 0.375f, (float)z + 0.625f, f * 0.6f, d);
                this.tesselatePistonSideX((float)x + 0.625f, (float)x + 0.625f, (float)y + 0.25f, (float)y + 0.25f + f2, (float)z + 0.625f, (float)z + 0.375f, f * 0.6f, d);
                break;
            }
            case 1: {
                block.setBlockBounds(0.0f, 0.75f, 0.0f, 1.0f, 1.0f, 1.0f);
                this.renderStandardBlock(block, x, y, z);
                this.tesselatePistonSideX((float)x + 0.375f, (float)x + 0.625f, (float)y - 0.25f + 1.0f - f2, (float)y - 0.25f + 1.0f, (float)z + 0.625f, (float)z + 0.625f, f * 0.8f, d);
                this.tesselatePistonSideX((float)x + 0.625f, (float)x + 0.375f, (float)y - 0.25f + 1.0f - f2, (float)y - 0.25f + 1.0f, (float)z + 0.375f, (float)z + 0.375f, f * 0.8f, d);
                this.tesselatePistonSideX((float)x + 0.375f, (float)x + 0.375f, (float)y - 0.25f + 1.0f - f2, (float)y - 0.25f + 1.0f, (float)z + 0.375f, (float)z + 0.625f, f * 0.6f, d);
                this.tesselatePistonSideX((float)x + 0.625f, (float)x + 0.625f, (float)y - 0.25f + 1.0f - f2, (float)y - 0.25f + 1.0f, (float)z + 0.625f, (float)z + 0.375f, f * 0.6f, d);
                break;
            }
            case 2: {
                this.westFaceRotation = 1;
                this.eastFaceRotation = 2;
                block.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.25f);
                this.renderStandardBlock(block, x, y, z);
                this.tesselatePistonSideY((float)x + 0.375f, (float)x + 0.375f, (float)y + 0.625f, (float)y + 0.375f, (float)z + 0.25f, (float)z + 0.25f + f2, f * 0.6f, d);
                this.tesselatePistonSideY((float)x + 0.625f, (float)x + 0.625f, (float)y + 0.375f, (float)y + 0.625f, (float)z + 0.25f, (float)z + 0.25f + f2, f * 0.6f, d);
                this.tesselatePistonSideY((float)x + 0.375f, (float)x + 0.625f, (float)y + 0.375f, (float)y + 0.375f, (float)z + 0.25f, (float)z + 0.25f + f2, f * 0.5f, d);
                this.tesselatePistonSideY((float)x + 0.625f, (float)x + 0.375f, (float)y + 0.625f, (float)y + 0.625f, (float)z + 0.25f, (float)z + 0.25f + f2, f, d);
                break;
            }
            case 3: {
                this.westFaceRotation = 2;
                this.eastFaceRotation = 1;
                this.topFaceRotation = 3;
                this.bottomFaceRotation = 3;
                block.setBlockBounds(0.0f, 0.0f, 0.75f, 1.0f, 1.0f, 1.0f);
                this.renderStandardBlock(block, x, y, z);
                this.tesselatePistonSideY((float)x + 0.375f, (float)x + 0.375f, (float)y + 0.625f, (float)y + 0.375f, (float)z - 0.25f + 1.0f - f2, (float)z - 0.25f + 1.0f, f * 0.6f, d);
                this.tesselatePistonSideY((float)x + 0.625f, (float)x + 0.625f, (float)y + 0.375f, (float)y + 0.625f, (float)z - 0.25f + 1.0f - f2, (float)z - 0.25f + 1.0f, f * 0.6f, d);
                this.tesselatePistonSideY((float)x + 0.375f, (float)x + 0.625f, (float)y + 0.375f, (float)y + 0.375f, (float)z - 0.25f + 1.0f - f2, (float)z - 0.25f + 1.0f, f * 0.5f, d);
                this.tesselatePistonSideY((float)x + 0.625f, (float)x + 0.375f, (float)y + 0.625f, (float)y + 0.625f, (float)z - 0.25f + 1.0f - f2, (float)z - 0.25f + 1.0f, f, d);
                break;
            }
            case 4: {
                this.northFaceRotation = 1;
                this.southFaceRotation = 2;
                this.topFaceRotation = 2;
                this.bottomFaceRotation = 1;
                block.setBlockBounds(0.0f, 0.0f, 0.0f, 0.25f, 1.0f, 1.0f);
                this.renderStandardBlock(block, x, y, z);
                this.tesselatePistonSideZ((float)x + 0.25f, (float)x + 0.25f + f2, (float)y + 0.375f, (float)y + 0.375f, (float)z + 0.625f, (float)z + 0.375f, f * 0.5f, d);
                this.tesselatePistonSideZ((float)x + 0.25f, (float)x + 0.25f + f2, (float)y + 0.625f, (float)y + 0.625f, (float)z + 0.375f, (float)z + 0.625f, f, d);
                this.tesselatePistonSideZ((float)x + 0.25f, (float)x + 0.25f + f2, (float)y + 0.375f, (float)y + 0.625f, (float)z + 0.375f, (float)z + 0.375f, f * 0.6f, d);
                this.tesselatePistonSideZ((float)x + 0.25f, (float)x + 0.25f + f2, (float)y + 0.625f, (float)y + 0.375f, (float)z + 0.625f, (float)z + 0.625f, f * 0.6f, d);
                break;
            }
            case 5: {
                this.northFaceRotation = 2;
                this.southFaceRotation = 1;
                this.topFaceRotation = 1;
                this.bottomFaceRotation = 2;
                block.setBlockBounds(0.75f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
                this.renderStandardBlock(block, x, y, z);
                this.tesselatePistonSideZ((float)x - 0.25f + 1.0f - f2, (float)x - 0.25f + 1.0f, (float)y + 0.375f, (float)y + 0.375f, (float)z + 0.625f, (float)z + 0.375f, f * 0.5f, d);
                this.tesselatePistonSideZ((float)x - 0.25f + 1.0f - f2, (float)x - 0.25f + 1.0f, (float)y + 0.625f, (float)y + 0.625f, (float)z + 0.375f, (float)z + 0.625f, f, d);
                this.tesselatePistonSideZ((float)x - 0.25f + 1.0f - f2, (float)x - 0.25f + 1.0f, (float)y + 0.375f, (float)y + 0.625f, (float)z + 0.375f, (float)z + 0.375f, f * 0.6f, d);
                this.tesselatePistonSideZ((float)x - 0.25f + 1.0f - f2, (float)x - 0.25f + 1.0f, (float)y + 0.625f, (float)y + 0.375f, (float)z + 0.625f, (float)z + 0.625f, f * 0.6f, d);
            }
        }
        this.northFaceRotation = 0;
        this.southFaceRotation = 0;
        this.westFaceRotation = 0;
        this.eastFaceRotation = 0;
        this.topFaceRotation = 0;
        this.bottomFaceRotation = 0;
        block.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
        return true;
    }

    public boolean renderBlockLever(Block block, int x, int y, int z) {
        int i = this.blockAccess.getBlockMetadata(x, y, z);
        int j = i & 7;
        boolean flag = (i & 8) > 0;
        Tessellator tessellator = Tessellator.instance;
        boolean flag1 = this.overrideBlockTexture >= 0;
        if (!flag1) {
            this.overrideBlockTexture = Block.cobblestone.blockIndexInTexture;
        }
        float f = 0.25F;
        float f1 = 0.1875F;
        float f2 = 0.1875F;
        if (j == 5) {
            block.setBlockBounds(0.5F - f1, 0.0F, 0.5F - f, 0.5F + f1, f2, 0.5F + f);
        } else if (j == 6) {
            block.setBlockBounds(0.5F - f, 0.0F, 0.5F - f1, 0.5F + f, f2, 0.5F + f1);
        } else if (j == 4) {
            block.setBlockBounds(0.5F - f1, 0.5F - f, 1.0F - f2, 0.5F + f1, 0.5F + f, 1.0F);
        } else if (j == 3) {
            block.setBlockBounds(0.5F - f1, 0.5F - f, 0.0F, 0.5F + f1, 0.5F + f, f2);
        } else if (j == 2) {
            block.setBlockBounds(1.0F - f2, 0.5F - f, 0.5F - f1, 1.0F, 0.5F + f, 0.5F + f1);
        } else if (j == 1) {
            block.setBlockBounds(0.0F, 0.5F - f, 0.5F - f1, f2, 0.5F + f, 0.5F + f1);
        }
        this.renderStandardBlock(block, x, y, z);
        if (!flag1) {
            this.overrideBlockTexture = -1;
        }
        float f3 = block.getBlockBrightness(this.blockAccess, x, y, z);
        if (Block.lightValue[block.blockID] > 0) {
            f3 = 1.0F;
        }
        tessellator.setColorOpaque_F(f3, f3, f3);
        int k = block.getBlockTextureFromSide(0);
        if (this.overrideBlockTexture >= 0) {
            k = this.overrideBlockTexture;
        }
        int l = (k & 15) << 4;
        int i1 = k & 240;
        float f4 = l / 256.0F;
        float f5 = (l + 15.99F) / 256.0F;
        float f6 = i1 / 256.0F;
        float f7 = (i1 + 15.99F) / 256.0F;
        Vec3D[] avec3d = new Vec3D[8];
        float f8 = 0.0625F;
        float f9 = 0.0625F;
        float f10 = 0.625F;
        avec3d[0] = Vec3D.createVector(-f8, 0.0, -f9);
        avec3d[1] = Vec3D.createVector(f8, 0.0, -f9);
        avec3d[2] = Vec3D.createVector(f8, 0.0, f9);
        avec3d[3] = Vec3D.createVector(-f8, 0.0, f9);
        avec3d[4] = Vec3D.createVector(-f8, f10, -f9);
        avec3d[5] = Vec3D.createVector(f8, f10, -f9);
        avec3d[6] = Vec3D.createVector(f8, f10, f9);
        avec3d[7] = Vec3D.createVector(-f8, f10, f9);
        for (int j1 = 0; j1 < 8; j1++) {
            if (flag) {
                avec3d[j1].zCoord -= 0.0625;
                avec3d[j1].rotateAroundX((float) Math.PI * 2.0F / 9.0F);
            } else {
                avec3d[j1].zCoord += 0.0625;
                avec3d[j1].rotateAroundX((float) -Math.PI * 2.0F / 9.0F);
            }
            if (j == 6) {
                avec3d[j1].rotateAroundY((float) (Math.PI / 2));
            }
            if (j < 5) {
                avec3d[j1].yCoord -= 0.375;
                avec3d[j1].rotateAroundX((float) (Math.PI / 2));
                if (j == 4) {
                    avec3d[j1].rotateAroundY(0.0F);
                }
                if (j == 3) {
                    avec3d[j1].rotateAroundY((float) Math.PI);
                }
                if (j == 2) {
                    avec3d[j1].rotateAroundY((float) (Math.PI / 2));
                }
                if (j == 1) {
                    avec3d[j1].rotateAroundY((float) (-Math.PI / 2));
                }
                avec3d[j1].xCoord += x + 0.5;
                avec3d[j1].yCoord += y + 0.5F;
                avec3d[j1].zCoord += z + 0.5;
            } else {
                avec3d[j1].xCoord += x + 0.5;
                avec3d[j1].yCoord += y + 0.125F;
                avec3d[j1].zCoord += z + 0.5;
            }
        }
        Vec3D vec3d3 = null;
        Vec3D vec3d = null;
        Vec3D vec3d1 = null;
        Vec3D vec3d2 = null;
        for (int k1 = 0; k1 < 6; k1++) {
            if (k1 == 0) {
                f4 = (l + 7) / 256.0F;
                f5 = (l + 9 - 0.01F) / 256.0F;
                f6 = (i1 + 6) / 256.0F;
                f7 = (i1 + 8 - 0.01F) / 256.0F;
            } else if (k1 == 2) {
                f4 = (l + 7) / 256.0F;
                f5 = (l + 9 - 0.01F) / 256.0F;
                f6 = (i1 + 6) / 256.0F;
                f7 = (i1 + 16 - 0.01F) / 256.0F;
            }
            if (k1 == 0) {
                vec3d3 = avec3d[0];
                vec3d = avec3d[1];
                vec3d1 = avec3d[2];
                vec3d2 = avec3d[3];
            } else if (k1 == 1) {
                vec3d3 = avec3d[7];
                vec3d = avec3d[6];
                vec3d1 = avec3d[5];
                vec3d2 = avec3d[4];
            } else if (k1 == 2) {
                vec3d3 = avec3d[1];
                vec3d = avec3d[0];
                vec3d1 = avec3d[4];
                vec3d2 = avec3d[5];
            } else if (k1 == 3) {
                vec3d3 = avec3d[2];
                vec3d = avec3d[1];
                vec3d1 = avec3d[5];
                vec3d2 = avec3d[6];
            } else if (k1 == 4) {
                vec3d3 = avec3d[3];
                vec3d = avec3d[2];
                vec3d1 = avec3d[6];
                vec3d2 = avec3d[7];
            } else if (k1 == 5) {
                vec3d3 = avec3d[0];
                vec3d = avec3d[3];
                vec3d1 = avec3d[7];
                vec3d2 = avec3d[4];
            }
            tessellator.addVertexWithUV(vec3d3.xCoord, vec3d3.yCoord, vec3d3.zCoord, f4, f7);
            tessellator.addVertexWithUV(vec3d.xCoord, vec3d.yCoord, vec3d.zCoord, f5, f7);
            tessellator.addVertexWithUV(vec3d1.xCoord, vec3d1.yCoord, vec3d1.zCoord, f5, f6);
            tessellator.addVertexWithUV(vec3d2.xCoord, vec3d2.yCoord, vec3d2.zCoord, f4, f6);
        }
        return true;
    }

    public boolean renderBlockFire(Block block, int x, int y, int z) {
        Tessellator tessellator = Tessellator.instance;
        int n = block.getBlockTextureFromSide(0);
        if (this.overrideBlockTexture >= 0) {
            n = this.overrideBlockTexture;
        }
        float f = block.getBlockBrightness(this.blockAccess, x, y, z);
        tessellator.setColorOpaque_F(f, f, f);
        int n2 = (n & 0xF) << 4;
        int n3 = n & 0xF0;
        double d = (float)n2 / 256.0f;
        double d2 = ((float)n2 + 15.99f) / 256.0f;
        double d3 = (float)n3 / 256.0f;
        double d4 = ((float)n3 + 15.99f) / 256.0f;
        float f2 = 1.4f;
        if (this.blockAccess.isBlockNormalCube(x, y - 1, z) || Block.fire.canBlockCatchFire(this.blockAccess, x, y - 1, z)) {
            double d5 = (double)x + 0.5 + 0.2;
            double d6 = (double)x + 0.5 - 0.2;
            double d7 = (double)z + 0.5 + 0.2;
            double d8 = (double)z + 0.5 - 0.2;
            double d9 = (double)x + 0.5 - 0.3;
            double d10 = (double)x + 0.5 + 0.3;
            double d11 = (double)z + 0.5 - 0.3;
            double d12 = (double)z + 0.5 + 0.3;
            tessellator.addVertexWithUV(d9, (float)y + f2, z + 1, d2, d3);
            tessellator.addVertexWithUV(d5, y + 0, z + 1, d2, d4);
            tessellator.addVertexWithUV(d5, y + 0, z + 0, d, d4);
            tessellator.addVertexWithUV(d9, (float)y + f2, z + 0, d, d3);
            tessellator.addVertexWithUV(d10, (float)y + f2, z + 0, d2, d3);
            tessellator.addVertexWithUV(d6, y + 0, z + 0, d2, d4);
            tessellator.addVertexWithUV(d6, y + 0, z + 1, d, d4);
            tessellator.addVertexWithUV(d10, (float)y + f2, z + 1, d, d3);
            d = (float)n2 / 256.0f;
            d2 = ((float)n2 + 15.99f) / 256.0f;
            d3 = (float)(n3 + 16) / 256.0f;
            d4 = ((float)n3 + 15.99f + 16.0f) / 256.0f;
            tessellator.addVertexWithUV(x + 1, (float)y + f2, d12, d2, d3);
            tessellator.addVertexWithUV(x + 1, y + 0, d8, d2, d4);
            tessellator.addVertexWithUV(x + 0, y + 0, d8, d, d4);
            tessellator.addVertexWithUV(x + 0, (float)y + f2, d12, d, d3);
            tessellator.addVertexWithUV(x + 0, (float)y + f2, d11, d2, d3);
            tessellator.addVertexWithUV(x + 0, y + 0, d7, d2, d4);
            tessellator.addVertexWithUV(x + 1, y + 0, d7, d, d4);
            tessellator.addVertexWithUV(x + 1, (float)y + f2, d11, d, d3);
            d5 = (double)x + 0.5 - 0.5;
            d6 = (double)x + 0.5 + 0.5;
            d7 = (double)z + 0.5 - 0.5;
            d8 = (double)z + 0.5 + 0.5;
            d9 = (double)x + 0.5 - 0.4;
            d10 = (double)x + 0.5 + 0.4;
            d11 = (double)z + 0.5 - 0.4;
            d12 = (double)z + 0.5 + 0.4;
            tessellator.addVertexWithUV(d9, (float)y + f2, z + 0, d, d3);
            tessellator.addVertexWithUV(d5, y + 0, z + 0, d, d4);
            tessellator.addVertexWithUV(d5, y + 0, z + 1, d2, d4);
            tessellator.addVertexWithUV(d9, (float)y + f2, z + 1, d2, d3);
            tessellator.addVertexWithUV(d10, (float)y + f2, z + 1, d, d3);
            tessellator.addVertexWithUV(d6, y + 0, z + 1, d, d4);
            tessellator.addVertexWithUV(d6, y + 0, z + 0, d2, d4);
            tessellator.addVertexWithUV(d10, (float)y + f2, z + 0, d2, d3);
            d = (float)n2 / 256.0f;
            d2 = ((float)n2 + 15.99f) / 256.0f;
            d3 = (float)n3 / 256.0f;
            d4 = ((float)n3 + 15.99f) / 256.0f;
            tessellator.addVertexWithUV(x + 0, (float)y + f2, d12, d, d3);
            tessellator.addVertexWithUV(x + 0, y + 0, d8, d, d4);
            tessellator.addVertexWithUV(x + 1, y + 0, d8, d2, d4);
            tessellator.addVertexWithUV(x + 1, (float)y + f2, d12, d2, d3);
            tessellator.addVertexWithUV(x + 1, (float)y + f2, d11, d, d3);
            tessellator.addVertexWithUV(x + 1, y + 0, d7, d, d4);
            tessellator.addVertexWithUV(x + 0, y + 0, d7, d2, d4);
            tessellator.addVertexWithUV(x + 0, (float)y + f2, d11, d2, d3);
        } else {
            double d13;
            float f3 = 0.2f;
            float f4 = 0.0625f;
            if ((x + y + z & 1) == 1) {
                d = (float)n2 / 256.0f;
                d2 = ((float)n2 + 15.99f) / 256.0f;
                d3 = (float)(n3 + 16) / 256.0f;
                d4 = ((float)n3 + 15.99f + 16.0f) / 256.0f;
            }
            if ((x / 2 + y / 2 + z / 2 & 1) == 1) {
                d13 = d2;
                d2 = d;
                d = d13;
            }
            if (Block.fire.canBlockCatchFire(this.blockAccess, x - 1, y, z)) {
                tessellator.addVertexWithUV((float)x + f3, (float)y + f2 + f4, z + 1, d2, d3);
                tessellator.addVertexWithUV(x + 0, (float)(y + 0) + f4, z + 1, d2, d4);
                tessellator.addVertexWithUV(x + 0, (float)(y + 0) + f4, z + 0, d, d4);
                tessellator.addVertexWithUV((float)x + f3, (float)y + f2 + f4, z + 0, d, d3);
                tessellator.addVertexWithUV((float)x + f3, (float)y + f2 + f4, z + 0, d, d3);
                tessellator.addVertexWithUV(x + 0, (float)(y + 0) + f4, z + 0, d, d4);
                tessellator.addVertexWithUV(x + 0, (float)(y + 0) + f4, z + 1, d2, d4);
                tessellator.addVertexWithUV((float)x + f3, (float)y + f2 + f4, z + 1, d2, d3);
            }
            if (Block.fire.canBlockCatchFire(this.blockAccess, x + 1, y, z)) {
                tessellator.addVertexWithUV((float)(x + 1) - f3, (float)y + f2 + f4, z + 0, d, d3);
                tessellator.addVertexWithUV(x + 1 - 0, (float)(y + 0) + f4, z + 0, d, d4);
                tessellator.addVertexWithUV(x + 1 - 0, (float)(y + 0) + f4, z + 1, d2, d4);
                tessellator.addVertexWithUV((float)(x + 1) - f3, (float)y + f2 + f4, z + 1, d2, d3);
                tessellator.addVertexWithUV((float)(x + 1) - f3, (float)y + f2 + f4, z + 1, d2, d3);
                tessellator.addVertexWithUV(x + 1 - 0, (float)(y + 0) + f4, z + 1, d2, d4);
                tessellator.addVertexWithUV(x + 1 - 0, (float)(y + 0) + f4, z + 0, d, d4);
                tessellator.addVertexWithUV((float)(x + 1) - f3, (float)y + f2 + f4, z + 0, d, d3);
            }
            if (Block.fire.canBlockCatchFire(this.blockAccess, x, y, z - 1)) {
                tessellator.addVertexWithUV(x + 0, (float)y + f2 + f4, (float)z + f3, d2, d3);
                tessellator.addVertexWithUV(x + 0, (float)(y + 0) + f4, z + 0, d2, d4);
                tessellator.addVertexWithUV(x + 1, (float)(y + 0) + f4, z + 0, d, d4);
                tessellator.addVertexWithUV(x + 1, (float)y + f2 + f4, (float)z + f3, d, d3);
                tessellator.addVertexWithUV(x + 1, (float)y + f2 + f4, (float)z + f3, d, d3);
                tessellator.addVertexWithUV(x + 1, (float)(y + 0) + f4, z + 0, d, d4);
                tessellator.addVertexWithUV(x + 0, (float)(y + 0) + f4, z + 0, d2, d4);
                tessellator.addVertexWithUV(x + 0, (float)y + f2 + f4, (float)z + f3, d2, d3);
            }
            if (Block.fire.canBlockCatchFire(this.blockAccess, x, y, z + 1)) {
                tessellator.addVertexWithUV(x + 1, (float)y + f2 + f4, (float)(z + 1) - f3, d, d3);
                tessellator.addVertexWithUV(x + 1, (float)(y + 0) + f4, z + 1 - 0, d, d4);
                tessellator.addVertexWithUV(x + 0, (float)(y + 0) + f4, z + 1 - 0, d2, d4);
                tessellator.addVertexWithUV(x + 0, (float)y + f2 + f4, (float)(z + 1) - f3, d2, d3);
                tessellator.addVertexWithUV(x + 0, (float)y + f2 + f4, (float)(z + 1) - f3, d2, d3);
                tessellator.addVertexWithUV(x + 0, (float)(y + 0) + f4, z + 1 - 0, d2, d4);
                tessellator.addVertexWithUV(x + 1, (float)(y + 0) + f4, z + 1 - 0, d, d4);
                tessellator.addVertexWithUV(x + 1, (float)y + f2 + f4, (float)(z + 1) - f3, d, d3);
            }
            if (Block.fire.canBlockCatchFire(this.blockAccess, x, y + 1, z)) {
                d13 = (double)x + 0.5 + 0.5;
                double d14 = (double)x + 0.5 - 0.5;
                double d15 = (double)z + 0.5 + 0.5;
                double d16 = (double)z + 0.5 - 0.5;
                double d17 = (double)x + 0.5 - 0.5;
                double d18 = (double)x + 0.5 + 0.5;
                double d19 = (double)z + 0.5 - 0.5;
                double d20 = (double)z + 0.5 + 0.5;
                d = (float)n2 / 256.0f;
                d2 = ((float)n2 + 15.99f) / 256.0f;
                d3 = (float)n3 / 256.0f;
                d4 = ((float)n3 + 15.99f) / 256.0f;
                f2 = -0.2f;
                if ((x + ++y + z & 1) == 0) {
                    tessellator.addVertexWithUV(d17, (float)y + f2, z + 0, d2, d3);
                    tessellator.addVertexWithUV(d13, y + 0, z + 0, d2, d4);
                    tessellator.addVertexWithUV(d13, y + 0, z + 1, d, d4);
                    tessellator.addVertexWithUV(d17, (float)y + f2, z + 1, d, d3);
                    d = (float)n2 / 256.0f;
                    d2 = ((float)n2 + 15.99f) / 256.0f;
                    d3 = (float)(n3 + 16) / 256.0f;
                    d4 = ((float)n3 + 15.99f + 16.0f) / 256.0f;
                    tessellator.addVertexWithUV(d18, (float)y + f2, z + 1, d2, d3);
                    tessellator.addVertexWithUV(d14, y + 0, z + 1, d2, d4);
                    tessellator.addVertexWithUV(d14, y + 0, z + 0, d, d4);
                    tessellator.addVertexWithUV(d18, (float)y + f2, z + 0, d, d3);
                } else {
                    tessellator.addVertexWithUV(x + 0, (float)y + f2, d20, d2, d3);
                    tessellator.addVertexWithUV(x + 0, y + 0, d16, d2, d4);
                    tessellator.addVertexWithUV(x + 1, y + 0, d16, d, d4);
                    tessellator.addVertexWithUV(x + 1, (float)y + f2, d20, d, d3);
                    d = (float)n2 / 256.0f;
                    d2 = ((float)n2 + 15.99f) / 256.0f;
                    d3 = (float)(n3 + 16) / 256.0f;
                    d4 = ((float)n3 + 15.99f + 16.0f) / 256.0f;
                    tessellator.addVertexWithUV(x + 1, (float)y + f2, d19, d2, d3);
                    tessellator.addVertexWithUV(x + 1, y + 0, d15, d2, d4);
                    tessellator.addVertexWithUV(x + 0, y + 0, d15, d, d4);
                    tessellator.addVertexWithUV(x + 0, (float)y + f2, d19, d, d3);
                }
            }
        }
        return true;
    }

    public boolean renderBlockRedstoneWire(Block block, int x, int y, int z) {
        Tessellator tessellator = Tessellator.instance;
        int i = this.blockAccess.getBlockMetadata(x, y, z);
        int j = block.getBlockTextureFromSideAndMetadata(1, i);
        if (this.overrideBlockTexture >= 0) {
            j = this.overrideBlockTexture;
        }
        float f = block.getBlockBrightness(this.blockAccess, x, y, z);
        float f1 = i / 15.0F;
        float f2 = f1 * 0.6F + 0.4F;
        if (i == 0) {
            f2 = 0.3F;
        }
        float f3 = f1 * f1 * 0.7F - 0.5F;
        float f4 = f1 * f1 * 0.6F - 0.7F;
        if (f3 < 0.0F) {
            f3 = 0.0F;
        }
        if (f4 < 0.0F) {
            f4 = 0.0F;
        }
        tessellator.setColorOpaque_F(f * f2, f * f3, f * f4);
        int k = (j & 15) << 4;
        int l = j & 240;
        double d0 = k / 256.0F;
        double d1 = (k + 15.99F) / 256.0F;
        double d2 = l / 256.0F;
        double d3 = (l + 15.99F) / 256.0F;
        boolean flag = BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, x - 1, y, z, 1) || !this.blockAccess.isBlockNormalCube(x - 1, y, z) && BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, x - 1, y - 1, z, -1);
        boolean flag1 = BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, x + 1, y, z, 3) || !this.blockAccess.isBlockNormalCube(x + 1, y, z) && BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, x + 1, y - 1, z, -1);
        boolean flag2 = BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, x, y, z - 1, 2) || !this.blockAccess.isBlockNormalCube(x, y, z - 1) && BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, x, y - 1, z - 1, -1);
        boolean flag3 = BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, x, y, z + 1, 0) || !this.blockAccess.isBlockNormalCube(x, y, z + 1) && BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, x, y - 1, z + 1, -1);
        if (!this.blockAccess.isBlockNormalCube(x, y + 1, z)) {
            if (this.blockAccess.isBlockNormalCube(x - 1, y, z) && BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, x - 1, y + 1, z, -1)) {
                flag = true;
            }
            if (this.blockAccess.isBlockNormalCube(x + 1, y, z) && BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, x + 1, y + 1, z, -1)) {
                flag1 = true;
            }
            if (this.blockAccess.isBlockNormalCube(x, y, z - 1) && BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, x, y + 1, z - 1, -1)) {
                flag2 = true;
            }
            if (this.blockAccess.isBlockNormalCube(x, y, z + 1) && BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, x, y + 1, z + 1, -1)) {
                flag3 = true;
            }
        }
        float f5 = x + 0;
        float f6 = x + 1;
        float f7 = z + 0;
        float f8 = z + 1;
        byte b0 = 0;
        if ((flag || flag1) && !flag2 && !flag3) {
            b0 = 1;
        }
        if ((flag2 || flag3) && !flag1 && !flag) {
            b0 = 2;
        }
        if (b0 != 0) {
            d0 = (k + 16) / 256.0F;
            d1 = (k + 16 + 15.99F) / 256.0F;
            d2 = l / 256.0F;
            d3 = (l + 15.99F) / 256.0F;
        }
        if (b0 == 0) {
            if (flag1 || flag2 || flag3 || flag) {
                if (!flag) {
                    f5 += 0.3125F;
                }
                if (!flag) {
                    d0 += 0.01953125;
                }
                if (!flag1) {
                    f6 -= 0.3125F;
                }
                if (!flag1) {
                    d1 -= 0.01953125;
                }
                if (!flag2) {
                    f7 += 0.3125F;
                }
                if (!flag2) {
                    d2 += 0.01953125;
                }
                if (!flag3) {
                    f8 -= 0.3125F;
                }
                if (!flag3) {
                    d3 -= 0.01953125;
                }
            }
            tessellator.addVertexWithUV(f6, y + 0.015625F, f8, d1, d3);
            tessellator.addVertexWithUV(f6, y + 0.015625F, f7, d1, d2);
            tessellator.addVertexWithUV(f5, y + 0.015625F, f7, d0, d2);
            tessellator.addVertexWithUV(f5, y + 0.015625F, f8, d0, d3);
            tessellator.setColorOpaque_F(f, f, f);
            tessellator.addVertexWithUV(f6, y + 0.015625F, f8, d1, d3 + 0.0625);
            tessellator.addVertexWithUV(f6, y + 0.015625F, f7, d1, d2 + 0.0625);
            tessellator.addVertexWithUV(f5, y + 0.015625F, f7, d0, d2 + 0.0625);
            tessellator.addVertexWithUV(f5, y + 0.015625F, f8, d0, d3 + 0.0625);
        } else if (b0 == 1) {
            tessellator.addVertexWithUV(f6, y + 0.015625F, f8, d1, d3);
            tessellator.addVertexWithUV(f6, y + 0.015625F, f7, d1, d2);
            tessellator.addVertexWithUV(f5, y + 0.015625F, f7, d0, d2);
            tessellator.addVertexWithUV(f5, y + 0.015625F, f8, d0, d3);
            tessellator.setColorOpaque_F(f, f, f);
            tessellator.addVertexWithUV(f6, y + 0.015625F, f8, d1, d3 + 0.0625);
            tessellator.addVertexWithUV(f6, y + 0.015625F, f7, d1, d2 + 0.0625);
            tessellator.addVertexWithUV(f5, y + 0.015625F, f7, d0, d2 + 0.0625);
            tessellator.addVertexWithUV(f5, y + 0.015625F, f8, d0, d3 + 0.0625);
        } else if (b0 == 2) {
            tessellator.addVertexWithUV(f6, y + 0.015625F, f8, d1, d3);
            tessellator.addVertexWithUV(f6, y + 0.015625F, f7, d0, d3);
            tessellator.addVertexWithUV(f5, y + 0.015625F, f7, d0, d2);
            tessellator.addVertexWithUV(f5, y + 0.015625F, f8, d1, d2);
            tessellator.setColorOpaque_F(f, f, f);
            tessellator.addVertexWithUV(f6, y + 0.015625F, f8, d1, d3 + 0.0625);
            tessellator.addVertexWithUV(f6, y + 0.015625F, f7, d0, d3 + 0.0625);
            tessellator.addVertexWithUV(f5, y + 0.015625F, f7, d0, d2 + 0.0625);
            tessellator.addVertexWithUV(f5, y + 0.015625F, f8, d1, d2 + 0.0625);
        }
        if (!this.blockAccess.isBlockNormalCube(x, y + 1, z)) {
            d0 = (k + 16) / 256.0F;
            d1 = (k + 16 + 15.99F) / 256.0F;
            d2 = l / 256.0F;
            d3 = (l + 15.99F) / 256.0F;
            if (this.blockAccess.isBlockNormalCube(x - 1, y, z) && this.blockAccess.getBlockId(x - 1, y + 1, z) == Block.redstoneWire.blockID) {
                tessellator.setColorOpaque_F(f * f2, f * f3, f * f4);
                tessellator.addVertexWithUV(x + 0.015625F, y + 1 + 0.021875F, z + 1, d1, d2);
                tessellator.addVertexWithUV(x + 0.015625F, y + 0, z + 1, d0, d2);
                tessellator.addVertexWithUV(x + 0.015625F, y + 0, z + 0, d0, d3);
                tessellator.addVertexWithUV(x + 0.015625F, y + 1 + 0.021875F, z + 0, d1, d3);
                tessellator.setColorOpaque_F(f, f, f);
                tessellator.addVertexWithUV(x + 0.015625F, y + 1 + 0.021875F, z + 1, d1, d2 + 0.0625);
                tessellator.addVertexWithUV(x + 0.015625F, y + 0, z + 1, d0, d2 + 0.0625);
                tessellator.addVertexWithUV(x + 0.015625F, y + 0, z + 0, d0, d3 + 0.0625);
                tessellator.addVertexWithUV(x + 0.015625F, y + 1 + 0.021875F, z + 0, d1, d3 + 0.0625);
            }
            if (this.blockAccess.isBlockNormalCube(x + 1, y, z) && this.blockAccess.getBlockId(x + 1, y + 1, z) == Block.redstoneWire.blockID) {
                tessellator.setColorOpaque_F(f * f2, f * f3, f * f4);
                tessellator.addVertexWithUV(x + 1 - 0.015625F, y + 0, z + 1, d0, d3);
                tessellator.addVertexWithUV(x + 1 - 0.015625F, y + 1 + 0.021875F, z + 1, d1, d3);
                tessellator.addVertexWithUV(x + 1 - 0.015625F, y + 1 + 0.021875F, z + 0, d1, d2);
                tessellator.addVertexWithUV(x + 1 - 0.015625F, y + 0, z + 0, d0, d2);
                tessellator.setColorOpaque_F(f, f, f);
                tessellator.addVertexWithUV(x + 1 - 0.015625F, y + 0, z + 1, d0, d3 + 0.0625);
                tessellator.addVertexWithUV(x + 1 - 0.015625F, y + 1 + 0.021875F, z + 1, d1, d3 + 0.0625);
                tessellator.addVertexWithUV(x + 1 - 0.015625F, y + 1 + 0.021875F, z + 0, d1, d2 + 0.0625);
                tessellator.addVertexWithUV(x + 1 - 0.015625F, y + 0, z + 0, d0, d2 + 0.0625);
            }
            if (this.blockAccess.isBlockNormalCube(x, y, z - 1) && this.blockAccess.getBlockId(x, y + 1, z - 1) == Block.redstoneWire.blockID) {
                tessellator.setColorOpaque_F(f * f2, f * f3, f * f4);
                tessellator.addVertexWithUV(x + 1, y + 0, z + 0.015625F, d0, d3);
                tessellator.addVertexWithUV(x + 1, y + 1 + 0.021875F, z + 0.015625F, d1, d3);
                tessellator.addVertexWithUV(x + 0, y + 1 + 0.021875F, z + 0.015625F, d1, d2);
                tessellator.addVertexWithUV(x + 0, y + 0, z + 0.015625F, d0, d2);
                tessellator.setColorOpaque_F(f, f, f);
                tessellator.addVertexWithUV(x + 1, y + 0, z + 0.015625F, d0, d3 + 0.0625);
                tessellator.addVertexWithUV(x + 1, y + 1 + 0.021875F, z + 0.015625F, d1, d3 + 0.0625);
                tessellator.addVertexWithUV(x + 0, y + 1 + 0.021875F, z + 0.015625F, d1, d2 + 0.0625);
                tessellator.addVertexWithUV(x + 0, y + 0, z + 0.015625F, d0, d2 + 0.0625);
            }
            if (this.blockAccess.isBlockNormalCube(x, y, z + 1) && this.blockAccess.getBlockId(x, y + 1, z + 1) == Block.redstoneWire.blockID) {
                tessellator.setColorOpaque_F(f * f2, f * f3, f * f4);
                tessellator.addVertexWithUV(x + 1, y + 1 + 0.021875F, z + 1 - 0.015625F, d1, d2);
                tessellator.addVertexWithUV(x + 1, y + 0, z + 1 - 0.015625F, d0, d2);
                tessellator.addVertexWithUV(x + 0, y + 0, z + 1 - 0.015625F, d0, d3);
                tessellator.addVertexWithUV(x + 0, y + 1 + 0.021875F, z + 1 - 0.015625F, d1, d3);
                tessellator.setColorOpaque_F(f, f, f);
                tessellator.addVertexWithUV(x + 1, y + 1 + 0.021875F, z + 1 - 0.015625F, d1, d2 + 0.0625);
                tessellator.addVertexWithUV(x + 1, y + 0, z + 1 - 0.015625F, d0, d2 + 0.0625);
                tessellator.addVertexWithUV(x + 0, y + 0, z + 1 - 0.015625F, d0, d3 + 0.0625);
                tessellator.addVertexWithUV(x + 0, y + 1 + 0.021875F, z + 1 - 0.015625F, d1, d3 + 0.0625);
            }
        }
        return true;
    }

    public boolean renderBlockMinecartTrack(BlockRail block, int x, int y, int z) {
        Tessellator tessellator = Tessellator.instance;
        int n = this.blockAccess.getBlockMetadata(x, y, z);
        int n2 = block.getBlockTextureFromSideAndMetadata(0, n);
        if (this.overrideBlockTexture >= 0) {
            n2 = this.overrideBlockTexture;
        }
        if (block.getIsPowered()) {
            n &= 7;
        }
        float f = block.getBlockBrightness(this.blockAccess, x, y, z);
        tessellator.setColorOpaque_F(f, f, f);
        int n3 = (n2 & 0xF) << 4;
        int n4 = n2 & 0xF0;
        double d = (float)n3 / 256.0f;
        double d2 = ((float)n3 + 15.99f) / 256.0f;
        double d3 = (float)n4 / 256.0f;
        double d4 = ((float)n4 + 15.99f) / 256.0f;
        float f2 = 0.0625f;
        float f3 = x + 1;
        float f4 = x + 1;
        float f5 = x + 0;
        float f6 = x + 0;
        float f7 = z + 0;
        float f8 = z + 1;
        float f9 = z + 1;
        float f10 = z + 0;
        float f11 = (float)y + f2;
        float f12 = (float)y + f2;
        float f13 = (float)y + f2;
        float f14 = (float)y + f2;
        if (n == 1 || n == 2 || n == 3 || n == 7) {
            f3 = f6 = (float)(x + 1);
            f4 = f5 = (float)(x + 0);
            f7 = f8 = (float)(z + 1);
            f9 = f10 = (float)(z + 0);
        } else if (n == 8) {
            f3 = f4 = (float)(x + 0);
            f5 = f6 = (float)(x + 1);
            f7 = f10 = (float)(z + 1);
            f8 = f9 = (float)(z + 0);
        } else if (n == 9) {
            f3 = f6 = (float)(x + 0);
            f4 = f5 = (float)(x + 1);
            f7 = f8 = (float)(z + 0);
            f9 = f10 = (float)(z + 1);
        }
        if (n == 2 || n == 4) {
            f11 += 1.0f;
            f14 += 1.0f;
        } else if (n == 3 || n == 5) {
            f12 += 1.0f;
            f13 += 1.0f;
        }
        tessellator.addVertexWithUV(f3, f11, f7, d2, d3);
        tessellator.addVertexWithUV(f4, f12, f8, d2, d4);
        tessellator.addVertexWithUV(f5, f13, f9, d, d4);
        tessellator.addVertexWithUV(f6, f14, f10, d, d3);
        tessellator.addVertexWithUV(f6, f14, f10, d, d3);
        tessellator.addVertexWithUV(f5, f13, f9, d, d4);
        tessellator.addVertexWithUV(f4, f12, f8, d2, d4);
        tessellator.addVertexWithUV(f3, f11, f7, d2, d3);
        return true;
    }

    public boolean renderBlockLadder(Block block, int x, int y, int z) {
        Tessellator tessellator = Tessellator.instance;
        int n = block.getBlockTextureFromSide(0);
        if (this.overrideBlockTexture >= 0) {
            n = this.overrideBlockTexture;
        }
        float f = block.getBlockBrightness(this.blockAccess, x, y, z);
        tessellator.setColorOpaque_F(f, f, f);
        int n2 = (n & 0xF) << 4;
        int n3 = n & 0xF0;
        double d = (float)n2 / 256.0f;
        double d2 = ((float)n2 + 15.99f) / 256.0f;
        double d3 = (float)n3 / 256.0f;
        double d4 = ((float)n3 + 15.99f) / 256.0f;
        int n4 = this.blockAccess.getBlockMetadata(x, y, z);
        float f2 = 0.0f;
        float f3 = 0.05f;
        if (n4 == 5) {
            tessellator.addVertexWithUV((float)x + f3, (float)(y + 1) + f2, (float)(z + 1) + f2, d, d3);
            tessellator.addVertexWithUV((float)x + f3, (float)(y + 0) - f2, (float)(z + 1) + f2, d, d4);
            tessellator.addVertexWithUV((float)x + f3, (float)(y + 0) - f2, (float)(z + 0) - f2, d2, d4);
            tessellator.addVertexWithUV((float)x + f3, (float)(y + 1) + f2, (float)(z + 0) - f2, d2, d3);
        }
        if (n4 == 4) {
            tessellator.addVertexWithUV((float)(x + 1) - f3, (float)(y + 0) - f2, (float)(z + 1) + f2, d2, d4);
            tessellator.addVertexWithUV((float)(x + 1) - f3, (float)(y + 1) + f2, (float)(z + 1) + f2, d2, d3);
            tessellator.addVertexWithUV((float)(x + 1) - f3, (float)(y + 1) + f2, (float)(z + 0) - f2, d, d3);
            tessellator.addVertexWithUV((float)(x + 1) - f3, (float)(y + 0) - f2, (float)(z + 0) - f2, d, d4);
        }
        if (n4 == 3) {
            tessellator.addVertexWithUV((float)(x + 1) + f2, (float)(y + 0) - f2, (float)z + f3, d2, d4);
            tessellator.addVertexWithUV((float)(x + 1) + f2, (float)(y + 1) + f2, (float)z + f3, d2, d3);
            tessellator.addVertexWithUV((float)(x + 0) - f2, (float)(y + 1) + f2, (float)z + f3, d, d3);
            tessellator.addVertexWithUV((float)(x + 0) - f2, (float)(y + 0) - f2, (float)z + f3, d, d4);
        }
        if (n4 == 2) {
            tessellator.addVertexWithUV((float)(x + 1) + f2, (float)(y + 1) + f2, (float)(z + 1) - f3, d, d3);
            tessellator.addVertexWithUV((float)(x + 1) + f2, (float)(y + 0) - f2, (float)(z + 1) - f3, d, d4);
            tessellator.addVertexWithUV((float)(x + 0) - f2, (float)(y + 0) - f2, (float)(z + 1) - f3, d2, d4);
            tessellator.addVertexWithUV((float)(x + 0) - f2, (float)(y + 1) + f2, (float)(z + 1) - f3, d2, d3);
        }
        return true;
    }

    public boolean renderBlockReed(Block block, int x, int y, int z) {
        Tessellator tessellator = Tessellator.instance;
        float f = block.getBlockBrightness(this.blockAccess, x, y, z);
        int n = block.colorMultiplier(this.blockAccess, x, y, z);
        float f2 = (float)(n >> 16 & 0xFF) / 255.0f;
        float f3 = (float)(n >> 8 & 0xFF) / 255.0f;
        float f4 = (float)(n & 0xFF) / 255.0f;
        if (EntityRenderer.anaglyphEnabled) {
            float f5 = (f2 * 30.0f + f3 * 59.0f + f4 * 11.0f) / 100.0f;
            float f6 = (f2 * 30.0f + f3 * 70.0f) / 100.0f;
            float f7 = (f2 * 30.0f + f4 * 70.0f) / 100.0f;
            f2 = f5;
            f3 = f6;
            f4 = f7;
        }
        tessellator.setColorOpaque_F(f * f2, f * f3, f * f4);
        double d = x;
        double d2 = y;
        double d3 = z;
        if (block == Block.tallGrass) {
            long l = (long)(x * 3129871) ^ (long)z * 116129781L ^ (long)y;
            l = l * l * 42317861L + l * 11L;
            d += ((double)((float)(l >> 16 & 0xFL) / 15.0f) - 0.5) * 0.5;
            d2 += ((double)((float)(l >> 20 & 0xFL) / 15.0f) - 1.0) * 0.2;
            d3 += ((double)((float)(l >> 24 & 0xFL) / 15.0f) - 0.5) * 0.5;
        }
        this.renderCrossedSquares(block, this.blockAccess.getBlockMetadata(x, y, z), d, d2, d3);
        return true;
    }

    public boolean renderBlockCrops(Block block, int x, int y, int z) {
        Tessellator tessellator = Tessellator.instance;
        float f = block.getBlockBrightness(this.blockAccess, x, y, z);
        tessellator.setColorOpaque_F(f, f, f);
        this.tesselatePlant(block, this.blockAccess.getBlockMetadata(x, y, z), x, (float)y - 0.0625f, z);
        return true;
    }

    public void renderTorchAtAngle(Block block, double x, double y, double z, double dx, double dz) {
        Tessellator tessellator = Tessellator.instance;
        int i = block.getBlockTextureFromSide(0);
        if (this.overrideBlockTexture >= 0) {
            i = this.overrideBlockTexture;
        }
        int j = (i & 15) << 4;
        int k = i & 240;
        float f = j / 256.0F;
        float f1 = (j + 15.99F) / 256.0F;
        float f2 = k / 256.0F;
        float f3 = (k + 15.99F) / 256.0F;
        double d0 = f + 0.02734375;
        double d1 = f2 + 0.0234375;
        double d2 = f + 0.03515625;
        double d3 = f2 + 0.03125;
        x += 0.5;
        z += 0.5;
        double d4 = x - 0.5;
        double d5 = x + 0.5;
        double d6 = z - 0.5;
        double d7 = z + 0.5;
        double d8 = 0.0625;
        double d9 = 0.625;
        tessellator.addVertexWithUV(x + dx * (1.0 - d9) - d8, y + d9, z + dz * (1.0 - d9) - d8, d0, d1);
        tessellator.addVertexWithUV(x + dx * (1.0 - d9) - d8, y + d9, z + dz * (1.0 - d9) + d8, d0, d3);
        tessellator.addVertexWithUV(x + dx * (1.0 - d9) + d8, y + d9, z + dz * (1.0 - d9) + d8, d2, d3);
        tessellator.addVertexWithUV(x + dx * (1.0 - d9) + d8, y + d9, z + dz * (1.0 - d9) - d8, d2, d1);
        tessellator.addVertexWithUV(x - d8, y + 1.0, d6, f, f2);
        tessellator.addVertexWithUV(x - d8 + dx, y + 0.0, d6 + dz, f, f3);
        tessellator.addVertexWithUV(x - d8 + dx, y + 0.0, d7 + dz, f1, f3);
        tessellator.addVertexWithUV(x - d8, y + 1.0, d7, f1, f2);
        tessellator.addVertexWithUV(x + d8, y + 1.0, d7, f, f2);
        tessellator.addVertexWithUV(x + dx + d8, y + 0.0, d7 + dz, f, f3);
        tessellator.addVertexWithUV(x + dx + d8, y + 0.0, d6 + dz, f1, f3);
        tessellator.addVertexWithUV(x + d8, y + 1.0, d6, f1, f2);
        tessellator.addVertexWithUV(d4, y + 1.0, z + d8, f, f2);
        tessellator.addVertexWithUV(d4 + dx, y + 0.0, z + d8 + dz, f, f3);
        tessellator.addVertexWithUV(d5 + dx, y + 0.0, z + d8 + dz, f1, f3);
        tessellator.addVertexWithUV(d5, y + 1.0, z + d8, f1, f2);
        tessellator.addVertexWithUV(d5, y + 1.0, z - d8, f, f2);
        tessellator.addVertexWithUV(d5 + dx, y + 0.0, z - d8 + dz, f, f3);
        tessellator.addVertexWithUV(d4 + dx, y + 0.0, z - d8 + dz, f1, f3);
        tessellator.addVertexWithUV(d4, y + 1.0, z - d8, f1, f2);
    }

    public void renderCrossedSquares(Block block, int metadata, double x, double y, double z) {
        Tessellator tessellator = Tessellator.instance;
        int n = block.getBlockTextureFromSideAndMetadata(0, metadata);
        if (this.overrideBlockTexture >= 0) {
            n = this.overrideBlockTexture;
        }
        int n2 = (n & 0xF) << 4;
        int n3 = n & 0xF0;
        double d = (float)n2 / 256.0f;
        double d2 = ((float)n2 + 15.99f) / 256.0f;
        double d3 = (float)n3 / 256.0f;
        double d4 = ((float)n3 + 15.99f) / 256.0f;
        double d5 = x + 0.5 - (double)0.45f;
        double d6 = x + 0.5 + (double)0.45f;
        double d7 = z + 0.5 - (double)0.45f;
        double d8 = z + 0.5 + (double)0.45f;
        tessellator.addVertexWithUV(d5, y + 1.0, d7, d, d3);
        tessellator.addVertexWithUV(d5, y + 0.0, d7, d, d4);
        tessellator.addVertexWithUV(d6, y + 0.0, d8, d2, d4);
        tessellator.addVertexWithUV(d6, y + 1.0, d8, d2, d3);
        tessellator.addVertexWithUV(d6, y + 1.0, d8, d, d3);
        tessellator.addVertexWithUV(d6, y + 0.0, d8, d, d4);
        tessellator.addVertexWithUV(d5, y + 0.0, d7, d2, d4);
        tessellator.addVertexWithUV(d5, y + 1.0, d7, d2, d3);
        tessellator.addVertexWithUV(d5, y + 1.0, d8, d, d3);
        tessellator.addVertexWithUV(d5, y + 0.0, d8, d, d4);
        tessellator.addVertexWithUV(d6, y + 0.0, d7, d2, d4);
        tessellator.addVertexWithUV(d6, y + 1.0, d7, d2, d3);
        tessellator.addVertexWithUV(d6, y + 1.0, d7, d, d3);
        tessellator.addVertexWithUV(d6, y + 0.0, d7, d, d4);
        tessellator.addVertexWithUV(d5, y + 0.0, d8, d2, d4);
        tessellator.addVertexWithUV(d5, y + 1.0, d8, d2, d3);
    }

    public void tesselatePlant(Block block, int metadata, double x, double y, double z) {
        Tessellator tessellator = Tessellator.instance;
        int n = block.getBlockTextureFromSideAndMetadata(0, metadata);
        if (this.overrideBlockTexture >= 0) {
            n = this.overrideBlockTexture;
        }
        int n2 = (n & 0xF) << 4;
        int n3 = n & 0xF0;
        double d = (float)n2 / 256.0f;
        double d2 = ((float)n2 + 15.99f) / 256.0f;
        double d3 = (float)n3 / 256.0f;
        double d4 = ((float)n3 + 15.99f) / 256.0f;
        double d5 = x + 0.5 - 0.25;
        double d6 = x + 0.5 + 0.25;
        double d7 = z + 0.5 - 0.5;
        double d8 = z + 0.5 + 0.5;
        tessellator.addVertexWithUV(d5, y + 1.0, d7, d, d3);
        tessellator.addVertexWithUV(d5, y + 0.0, d7, d, d4);
        tessellator.addVertexWithUV(d5, y + 0.0, d8, d2, d4);
        tessellator.addVertexWithUV(d5, y + 1.0, d8, d2, d3);
        tessellator.addVertexWithUV(d5, y + 1.0, d8, d, d3);
        tessellator.addVertexWithUV(d5, y + 0.0, d8, d, d4);
        tessellator.addVertexWithUV(d5, y + 0.0, d7, d2, d4);
        tessellator.addVertexWithUV(d5, y + 1.0, d7, d2, d3);
        tessellator.addVertexWithUV(d6, y + 1.0, d8, d, d3);
        tessellator.addVertexWithUV(d6, y + 0.0, d8, d, d4);
        tessellator.addVertexWithUV(d6, y + 0.0, d7, d2, d4);
        tessellator.addVertexWithUV(d6, y + 1.0, d7, d2, d3);
        tessellator.addVertexWithUV(d6, y + 1.0, d7, d, d3);
        tessellator.addVertexWithUV(d6, y + 0.0, d7, d, d4);
        tessellator.addVertexWithUV(d6, y + 0.0, d8, d2, d4);
        tessellator.addVertexWithUV(d6, y + 1.0, d8, d2, d3);
        d5 = x + 0.5 - 0.5;
        d6 = x + 0.5 + 0.5;
        d7 = z + 0.5 - 0.25;
        d8 = z + 0.5 + 0.25;
        tessellator.addVertexWithUV(d5, y + 1.0, d7, d, d3);
        tessellator.addVertexWithUV(d5, y + 0.0, d7, d, d4);
        tessellator.addVertexWithUV(d6, y + 0.0, d7, d2, d4);
        tessellator.addVertexWithUV(d6, y + 1.0, d7, d2, d3);
        tessellator.addVertexWithUV(d6, y + 1.0, d7, d, d3);
        tessellator.addVertexWithUV(d6, y + 0.0, d7, d, d4);
        tessellator.addVertexWithUV(d5, y + 0.0, d7, d2, d4);
        tessellator.addVertexWithUV(d5, y + 1.0, d7, d2, d3);
        tessellator.addVertexWithUV(d6, y + 1.0, d8, d, d3);
        tessellator.addVertexWithUV(d6, y + 0.0, d8, d, d4);
        tessellator.addVertexWithUV(d5, y + 0.0, d8, d2, d4);
        tessellator.addVertexWithUV(d5, y + 1.0, d8, d2, d3);
        tessellator.addVertexWithUV(d5, y + 1.0, d8, d, d3);
        tessellator.addVertexWithUV(d5, y + 0.0, d8, d, d4);
        tessellator.addVertexWithUV(d6, y + 0.0, d8, d2, d4);
        tessellator.addVertexWithUV(d6, y + 1.0, d8, d2, d3);
    }

    public boolean renderBlockFluids(Block block, int x, int y, int z) {
        float f;
        float f2;
        float f3;
        int n;
        int n2;
        Tessellator tessellator = Tessellator.instance;
        int n3 = block.colorMultiplier(this.blockAccess, x, y, z);
        float f4 = (float)(n3 >> 16 & 0xFF) / 255.0f;
        float f5 = (float)(n3 >> 8 & 0xFF) / 255.0f;
        float f6 = (float)(n3 & 0xFF) / 255.0f;
        boolean bl = block.shouldSideBeRendered(this.blockAccess, x, y + 1, z, 1);
        boolean bl2 = block.shouldSideBeRendered(this.blockAccess, x, y - 1, z, 0);
        boolean[] blArray = new boolean[]{block.shouldSideBeRendered(this.blockAccess, x, y, z - 1, 2), block.shouldSideBeRendered(this.blockAccess, x, y, z + 1, 3), block.shouldSideBeRendered(this.blockAccess, x - 1, y, z, 4), block.shouldSideBeRendered(this.blockAccess, x + 1, y, z, 5)};
        if (!(bl || bl2 || blArray[0] || blArray[1] || blArray[2] || blArray[3])) {
            return false;
        }
        boolean bl3 = false;
        float f7 = 0.5f;
        float f8 = 1.0f;
        float f9 = 0.8f;
        float f10 = 0.6f;
        double d = 0.0;
        double d2 = 1.0;
        Material material = block.blockMaterial;
        int n4 = this.blockAccess.getBlockMetadata(x, y, z);
        float f11 = this.getLiquidHeight(x, y, z, material);
        float f12 = this.getLiquidHeight(x, y, z + 1, material);
        float f13 = this.getLiquidHeight(x + 1, y, z + 1, material);
        float f14 = this.getLiquidHeight(x + 1, y, z, material);
        if (this.renderAllFaces || bl) {
            bl3 = true;
            int n5 = block.getBlockTextureFromSideAndMetadata(1, n4);
            float f15 = (float)BlockFluid.getFlowAngle(this.blockAccess, x, y, z, material);
            if (f15 > -999.0f) {
                n5 = block.getBlockTextureFromSideAndMetadata(2, n4);
            }
            n2 = (n5 & 0xF) << 4;
            n = n5 & 0xF0;
            double d3 = ((double)n2 + 8.0) / 256.0;
            double d4 = ((double)n + 8.0) / 256.0;
            if (f15 < -999.0f) {
                f15 = 0.0f;
            } else {
                d3 = (float)(n2 + 16) / 256.0f;
                d4 = (float)(n + 16) / 256.0f;
            }
            f3 = MathHelper.sin(f15) * 8.0f / 256.0f;
            f2 = MathHelper.cos(f15) * 8.0f / 256.0f;
            f = block.getBlockBrightness(this.blockAccess, x, y, z);
            tessellator.setColorOpaque_F(f8 * f * f4, f8 * f * f5, f8 * f * f6);
            tessellator.addVertexWithUV(x + 0, (float)y + f11, z + 0, d3 - (double)f2 - (double)f3, d4 - (double)f2 + (double)f3);
            tessellator.addVertexWithUV(x + 0, (float)y + f12, z + 1, d3 - (double)f2 + (double)f3, d4 + (double)f2 + (double)f3);
            tessellator.addVertexWithUV(x + 1, (float)y + f13, z + 1, d3 + (double)f2 + (double)f3, d4 + (double)f2 - (double)f3);
            tessellator.addVertexWithUV(x + 1, (float)y + f14, z + 0, d3 + (double)f2 - (double)f3, d4 - (double)f2 - (double)f3);
        }
        if (this.renderAllFaces || bl2) {
            float f16 = block.getBlockBrightness(this.blockAccess, x, y - 1, z);
            tessellator.setColorOpaque_F(f7 * f16, f7 * f16, f7 * f16);
            this.renderBottomFace(block, x, y, z, block.getBlockTextureFromSide(0));
            bl3 = true;
        }
        for (int i = 0; i < 4; ++i) {
            float f17;
            float f18;
            float f19;
            int n6 = x;
            n2 = y;
            n = z;
            if (i == 0) {
                --n;
            }
            if (i == 1) {
                ++n;
            }
            if (i == 2) {
                --n6;
            }
            if (i == 3) {
                ++n6;
            }
            int n7 = block.getBlockTextureFromSideAndMetadata(i + 2, n4);
            int n8 = (n7 & 0xF) << 4;
            int n9 = n7 & 0xF0;
            if (!this.renderAllFaces && !blArray[i]) continue;
            if (i == 0) {
                f19 = f11;
                f3 = f14;
                f2 = x;
                f18 = x + 1;
                f = z;
                f17 = z;
            } else if (i == 1) {
                f19 = f13;
                f3 = f12;
                f2 = x + 1;
                f18 = x;
                f = z + 1;
                f17 = z + 1;
            } else if (i == 2) {
                f19 = f12;
                f3 = f11;
                f2 = x;
                f18 = x;
                f = z + 1;
                f17 = z;
            } else {
                f19 = f14;
                f3 = f13;
                f2 = x + 1;
                f18 = x + 1;
                f = z;
                f17 = z + 1;
            }
            bl3 = true;
            double d5 = (float)(n8 + 0) / 256.0f;
            double d6 = ((double)(n8 + 16) - 0.01) / 256.0;
            double d7 = ((float)n9 + (1.0f - f19) * 16.0f) / 256.0f;
            double d8 = ((float)n9 + (1.0f - f3) * 16.0f) / 256.0f;
            double d9 = ((double)(n9 + 16) - 0.01) / 256.0;
            float f20 = block.getBlockBrightness(this.blockAccess, n6, n2, n);
            f20 = i < 2 ? (f20 *= f9) : (f20 *= f10);
            tessellator.setColorOpaque_F(f8 * f20 * f4, f8 * f20 * f5, f8 * f20 * f6);
            tessellator.addVertexWithUV(f2, (float)y + f19, f, d5, d7);
            tessellator.addVertexWithUV(f18, (float)y + f3, f17, d6, d8);
            tessellator.addVertexWithUV(f18, y + 0, f17, d6, d9);
            tessellator.addVertexWithUV(f2, y + 0, f, d5, d9);
        }
        block.minY = d;
        block.maxY = d2;
        return bl3;
    }

    private float getLiquidHeight(int x, int y, int z, Material liquid) {
        int i = 0;
        float f = 0.0F;
        for (int j = 0; j < 4; j++) {
            int k = x - (j & 1);
            int l = y;
            int i1 = z - (j >> 1 & 1);
            if (this.blockAccess.getBlockMaterial(k, l + 1, i1) == liquid) {
                return 1.0F;
            }
            Material material = this.blockAccess.getBlockMaterial(k, l, i1);
            if (material == liquid) {
                int j1 = this.blockAccess.getBlockMetadata(k, l, i1);
                if (j1 >= 8 || j1 == 0) {
                    f += BlockFluid.getPercentAir(j1) * 10.0F;
                    i += 10;
                }
                f += BlockFluid.getPercentAir(j1);
                i++;
            } else if (!material.isSolid()) {
                f++;
                i++;
            }
        }
        return 1.0F - f / i;
    }

    public void renderBlockFallingSand(Block block, World world, int x, int y, int z) {
        float f = 0.5f;
        float f2 = 1.0f;
        float f3 = 0.8f;
        float f4 = 0.6f;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        float f5 = block.getBlockBrightness(world, x, y, z);
        float f6 = block.getBlockBrightness(world, x, y - 1, z);
        if (f6 < f5) {
            f6 = f5;
        }
        tessellator.setColorOpaque_F(f * f6, f * f6, f * f6);
        this.renderBottomFace(block, -0.5, -0.5, -0.5, block.getBlockTextureFromSide(0));
        f6 = block.getBlockBrightness(world, x, y + 1, z);
        if (f6 < f5) {
            f6 = f5;
        }
        tessellator.setColorOpaque_F(f2 * f6, f2 * f6, f2 * f6);
        this.renderTopFace(block, -0.5, -0.5, -0.5, block.getBlockTextureFromSide(1));
        f6 = block.getBlockBrightness(world, x, y, z - 1);
        if (f6 < f5) {
            f6 = f5;
        }
        tessellator.setColorOpaque_F(f3 * f6, f3 * f6, f3 * f6);
        this.renderEastFace(block, -0.5, -0.5, -0.5, block.getBlockTextureFromSide(2));
        f6 = block.getBlockBrightness(world, x, y, z + 1);
        if (f6 < f5) {
            f6 = f5;
        }
        tessellator.setColorOpaque_F(f3 * f6, f3 * f6, f3 * f6);
        this.renderWestFace(block, -0.5, -0.5, -0.5, block.getBlockTextureFromSide(3));
        f6 = block.getBlockBrightness(world, x - 1, y, z);
        if (f6 < f5) {
            f6 = f5;
        }
        tessellator.setColorOpaque_F(f4 * f6, f4 * f6, f4 * f6);
        this.renderNorthFace(block, -0.5, -0.5, -0.5, block.getBlockTextureFromSide(4));
        f6 = block.getBlockBrightness(world, x + 1, y, z);
        if (f6 < f5) {
            f6 = f5;
        }
        tessellator.setColorOpaque_F(f4 * f6, f4 * f6, f4 * f6);
        this.renderSouthFace(block, -0.5, -0.5, -0.5, block.getBlockTextureFromSide(5));
        tessellator.draw();
    }

    public boolean renderStandardBlock(Block block, int x, int y, int z) {
        int n = block.colorMultiplier(this.blockAccess, x, y, z);
        float f = (float)(n >> 16 & 0xFF) / 255.0f;
        float f2 = (float)(n >> 8 & 0xFF) / 255.0f;
        float f3 = (float)(n & 0xFF) / 255.0f;
        if (EntityRenderer.anaglyphEnabled) {
            float f4 = (f * 30.0f + f2 * 59.0f + f3 * 11.0f) / 100.0f;
            float f5 = (f * 30.0f + f2 * 70.0f) / 100.0f;
            float f6 = (f * 30.0f + f3 * 70.0f) / 100.0f;
            f = f4;
            f2 = f5;
            f3 = f6;
        }
        if (Minecraft.isAmbientOcclusionEnabled()) {
            return this.renderStandardBlockWithAmbientOcclusion(block, x, y, z, f, f2, f3);
        }
        return this.renderStandardBlockWithColorMultiplier(block, x, y, z, f, f2, f3);
    }

    public boolean renderStandardBlockWithAmbientOcclusion(Block block, int x, int y, int z, float r, float g, float b) {
        int n;
        this.enableAO = true;
        boolean bl = false;
        float f = this.lightValueOwn;
        float f2 = this.lightValueOwn;
        float f3 = this.lightValueOwn;
        float f4 = this.lightValueOwn;
        boolean bl2 = true;
        boolean bl3 = true;
        boolean bl4 = true;
        boolean bl5 = true;
        boolean bl6 = true;
        boolean bl7 = true;
        this.lightValueOwn = block.getBlockBrightness(this.blockAccess, x, y, z);
        this.aoLightValueXNeg = block.getBlockBrightness(this.blockAccess, x - 1, y, z);
        this.aoLightValueYNeg = block.getBlockBrightness(this.blockAccess, x, y - 1, z);
        this.aoLightValueZNeg = block.getBlockBrightness(this.blockAccess, x, y, z - 1);
        this.aoLightValueXPos = block.getBlockBrightness(this.blockAccess, x + 1, y, z);
        this.aoLightValueYPos = block.getBlockBrightness(this.blockAccess, x, y + 1, z);
        this.aoLightValueZPos = block.getBlockBrightness(this.blockAccess, x, y, z + 1);
        this.topEastEdgeTranslucent = Block.canBlockGrass[this.blockAccess.getBlockId(x + 1, y + 1, z)];
        this.bottomEastEdgeTranslucent = Block.canBlockGrass[this.blockAccess.getBlockId(x + 1, y - 1, z)];
        this.southEastEdgeTranslucent = Block.canBlockGrass[this.blockAccess.getBlockId(x + 1, y, z + 1)];
        this.northEastEdgeTranslucent = Block.canBlockGrass[this.blockAccess.getBlockId(x + 1, y, z - 1)];
        this.topWestEdgeTranslucent = Block.canBlockGrass[this.blockAccess.getBlockId(x - 1, y + 1, z)];
        this.bottomWestEdgeTranslucent = Block.canBlockGrass[this.blockAccess.getBlockId(x - 1, y - 1, z)];
        this.northWestEdgeTranslucent = Block.canBlockGrass[this.blockAccess.getBlockId(x - 1, y, z - 1)];
        this.southWestEdgeTranslucent = Block.canBlockGrass[this.blockAccess.getBlockId(x - 1, y, z + 1)];
        this.topSouthEdgeTranslucent = Block.canBlockGrass[this.blockAccess.getBlockId(x, y + 1, z + 1)];
        this.topNorthEdgeTranslucent = Block.canBlockGrass[this.blockAccess.getBlockId(x, y + 1, z - 1)];
        this.bottomSouthEdgeTranslucent = Block.canBlockGrass[this.blockAccess.getBlockId(x, y - 1, z + 1)];
        this.bottomNorthEdgeTranslucent = Block.canBlockGrass[this.blockAccess.getBlockId(x, y - 1, z - 1)];
        if (block.blockIndexInTexture == 3) {
            bl2 = bl4 = bl5 = bl6 = bl7 = false;
        }
        if (this.overrideBlockTexture >= 0) {
            bl2 = bl4 = bl5 = bl6 = bl7 = false;
        }
        if (this.renderAllFaces || block.shouldSideBeRendered(this.blockAccess, x, y - 1, z, 0)) {
            if (this.ambientOcclusionLevel > 0) {
                --y;
                this.bottomWestEdgeAmbientOcclusionLight = block.getBlockBrightness(this.blockAccess, x - 1, y, z);
                this.bottomNorthEdgeAmbientOcclusionLight = block.getBlockBrightness(this.blockAccess, x, y, z - 1);
                this.bottomSouthEdgeAmbientOcclusionLight = block.getBlockBrightness(this.blockAccess, x, y, z + 1);
                this.bottomEastEdgeAmbientOcclusionLight = block.getBlockBrightness(this.blockAccess, x + 1, y, z);
                if (this.bottomNorthEdgeTranslucent || this.bottomWestEdgeTranslucent) {
                    this.bottomNorthWestVertexAmbientOcclusionLight = block.getBlockBrightness(this.blockAccess, x - 1, y, z - 1);
                }
                else {
                    this.bottomNorthWestVertexAmbientOcclusionLight = this.bottomWestEdgeAmbientOcclusionLight;
                }
                if (this.bottomSouthEdgeTranslucent || this.bottomWestEdgeTranslucent) {
                    this.bottomSouthWestVertexAmbientOcclusionLight = block.getBlockBrightness(this.blockAccess, x - 1, y, z + 1);
                }
                else {
                    this.bottomSouthWestVertexAmbientOcclusionLight = this.bottomWestEdgeAmbientOcclusionLight;
                }
                if (this.bottomNorthEdgeTranslucent || this.bottomEastEdgeTranslucent) {
                    this.bottomNorthEastVertexAmbientOcclusionLight = block.getBlockBrightness(this.blockAccess, x + 1, y, z - 1);
                }
                else {
                    this.bottomNorthEastVertexAmbientOcclusionLight = this.bottomEastEdgeAmbientOcclusionLight;
                }
                if (this.bottomSouthEdgeTranslucent || this.bottomEastEdgeTranslucent) {
                    this.bottomSouthEastVertexAmbientOcclusionLight = block.getBlockBrightness(this.blockAccess, x + 1, y, z + 1);
                }
                else {
                    this.bottomSouthEastVertexAmbientOcclusionLight = this.bottomEastEdgeAmbientOcclusionLight;
                }
                ++y;
                f = (this.bottomSouthWestVertexAmbientOcclusionLight + this.bottomWestEdgeAmbientOcclusionLight + this.bottomSouthEdgeAmbientOcclusionLight + this.aoLightValueYNeg) / 4.0f;
                f4 = (this.bottomSouthEdgeAmbientOcclusionLight + this.aoLightValueYNeg + this.bottomSouthEastVertexAmbientOcclusionLight + this.bottomEastEdgeAmbientOcclusionLight) / 4.0f;
                f3 = (this.aoLightValueYNeg + this.bottomNorthEdgeAmbientOcclusionLight + this.bottomEastEdgeAmbientOcclusionLight + this.bottomNorthEastVertexAmbientOcclusionLight) / 4.0f;
                f2 = (this.bottomWestEdgeAmbientOcclusionLight + this.bottomNorthWestVertexAmbientOcclusionLight + this.aoLightValueYNeg + this.bottomNorthEdgeAmbientOcclusionLight) / 4.0f;
            } else {
                f = f2 = f3 = f4 = this.aoLightValueYNeg;
            }
            this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = (bl2 ? r : 1.0f) * 0.5f;
            this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = (bl2 ? g : 1.0f) * 0.5f;
            this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = (bl2 ? b : 1.0f) * 0.5f;
            this.colorRedTopLeft *= f;
            this.colorGreenTopLeft *= f;
            this.colorBlueTopLeft *= f;
            this.colorRedBottomLeft *= f2;
            this.colorGreenBottomLeft *= f2;
            this.colorBlueBottomLeft *= f2;
            this.colorRedBottomRight *= f3;
            this.colorGreenBottomRight *= f3;
            this.colorBlueBottomRight *= f3;
            this.colorRedTopRight *= f4;
            this.colorGreenTopRight *= f4;
            this.colorBlueTopRight *= f4;
            this.renderBottomFace(block, x, y, z, block.getBlockTexture(this.blockAccess, x, y, z, 0));
            bl = true;
        }
        if (this.renderAllFaces || block.shouldSideBeRendered(this.blockAccess, x, y + 1, z, 1)) {
            if (this.ambientOcclusionLevel > 0) {
                ++y;
                this.topWestEdgeAmbientOcclusionLight = block.getBlockBrightness(this.blockAccess, x - 1, y, z);
                this.topEastEdgeAmbientOcclusionLight = block.getBlockBrightness(this.blockAccess, x + 1, y, z);
                this.topNorthEdgeAmbientOcclusionLight = block.getBlockBrightness(this.blockAccess, x, y, z - 1);
                this.topSouthEdgeAmbientOcclusionLight = block.getBlockBrightness(this.blockAccess, x, y, z + 1);
                if (this.topNorthEdgeTranslucent || this.topWestEdgeTranslucent) {
                    this.topNorthWestVertexAmbientOcclusionLight = block.getBlockBrightness(this.blockAccess, x - 1, y, z - 1);
                }
                else {
                    this.topNorthWestVertexAmbientOcclusionLight = this.topWestEdgeAmbientOcclusionLight;
                }
                if (this.topNorthEdgeTranslucent || this.topEastEdgeTranslucent) {
                    this.topNorthEastVertexAmbientOcclusionLight = block.getBlockBrightness(this.blockAccess, x + 1, y, z - 1);
                }
                else {
                    this.topNorthEastVertexAmbientOcclusionLight = this.topEastEdgeAmbientOcclusionLight;
                }
                if (this.topSouthEdgeTranslucent || this.topWestEdgeTranslucent) {
                    this.topSouthWestVertexAmbientOcclusionLight = block.getBlockBrightness(this.blockAccess, x - 1, y, z + 1);
                }
                else {
                    this.topSouthWestVertexAmbientOcclusionLight = this.topWestEdgeAmbientOcclusionLight;
                }
                if (this.topSouthEdgeTranslucent || this.topEastEdgeTranslucent) {
                    this.topSouthEastVertexAmbientOcclusionLight = block.getBlockBrightness(this.blockAccess, x + 1, y, z + 1);
                }
                else {
                    this.topSouthEastVertexAmbientOcclusionLight = this.topEastEdgeAmbientOcclusionLight;
                }
                --y;
                f4 = (this.topSouthWestVertexAmbientOcclusionLight + this.topWestEdgeAmbientOcclusionLight + this.topSouthEdgeAmbientOcclusionLight + this.aoLightValueYPos) / 4.0f;
                f = (this.topSouthEdgeAmbientOcclusionLight + this.aoLightValueYPos + this.topSouthEastVertexAmbientOcclusionLight + this.topEastEdgeAmbientOcclusionLight) / 4.0f;
                f2 = (this.aoLightValueYPos + this.topNorthEdgeAmbientOcclusionLight + this.topEastEdgeAmbientOcclusionLight + this.topNorthEastVertexAmbientOcclusionLight) / 4.0f;
                f3 = (this.topWestEdgeAmbientOcclusionLight + this.topNorthWestVertexAmbientOcclusionLight + this.aoLightValueYPos + this.topNorthEdgeAmbientOcclusionLight) / 4.0f;
            } else {
                f = f2 = f3 = f4 = this.aoLightValueYPos;
            }
            this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = bl3 ? r : 1.0f;
            this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = bl3 ? g : 1.0f;
            this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = bl3 ? b : 1.0f;
            this.colorRedTopLeft *= f;
            this.colorGreenTopLeft *= f;
            this.colorBlueTopLeft *= f;
            this.colorRedBottomLeft *= f2;
            this.colorGreenBottomLeft *= f2;
            this.colorBlueBottomLeft *= f2;
            this.colorRedBottomRight *= f3;
            this.colorGreenBottomRight *= f3;
            this.colorBlueBottomRight *= f3;
            this.colorRedTopRight *= f4;
            this.colorGreenTopRight *= f4;
            this.colorBlueTopRight *= f4;
            this.renderTopFace(block, x, y, z, block.getBlockTexture(this.blockAccess, x, y, z, 1));
            bl = true;
        }
        if (this.renderAllFaces || block.shouldSideBeRendered(this.blockAccess, x, y, z - 1, 2)) {
            if (this.ambientOcclusionLevel > 0) {
                --z;
                this.northWestEdgeAmbientOcclusionLight = block.getBlockBrightness(this.blockAccess, x - 1, y, z);
                this.bottomNorthEdgeAmbientOcclusionLight = block.getBlockBrightness(this.blockAccess, x, y - 1, z);
                this.topNorthEdgeAmbientOcclusionLight = block.getBlockBrightness(this.blockAccess, x, y + 1, z);
                this.northEastEdgeAmbientOcclusionLight = block.getBlockBrightness(this.blockAccess, x + 1, y, z);
                if (this.northWestEdgeTranslucent || this.bottomNorthEdgeTranslucent) {
                    this.bottomNorthWestVertexAmbientOcclusionLight = block.getBlockBrightness(this.blockAccess, x - 1, y - 1, z);
                }
                else {
                    this.bottomNorthWestVertexAmbientOcclusionLight = this.northWestEdgeAmbientOcclusionLight;
                }
                if (this.northWestEdgeTranslucent || this.topNorthEdgeTranslucent) {
                    this.topNorthWestVertexAmbientOcclusionLight = block.getBlockBrightness(this.blockAccess, x - 1, y + 1, z);
                }
                else {
                    this.topNorthWestVertexAmbientOcclusionLight = this.northWestEdgeAmbientOcclusionLight;
                }
                if (this.northEastEdgeTranslucent || this.bottomNorthEdgeTranslucent) {
                    this.bottomNorthEastVertexAmbientOcclusionLight = block.getBlockBrightness(this.blockAccess, x + 1, y - 1, z);
                }
                else {
                    this.bottomNorthEastVertexAmbientOcclusionLight = this.northEastEdgeAmbientOcclusionLight;
                }
                if (this.northEastEdgeTranslucent || this.topNorthEdgeTranslucent) {
                    this.topNorthEastVertexAmbientOcclusionLight = block.getBlockBrightness(this.blockAccess, x + 1, y + 1, z);
                }
                else {
                    this.topNorthEastVertexAmbientOcclusionLight = this.northEastEdgeAmbientOcclusionLight;
                }
                ++z;
                f = (this.northWestEdgeAmbientOcclusionLight + this.topNorthWestVertexAmbientOcclusionLight + this.aoLightValueZNeg + this.topNorthEdgeAmbientOcclusionLight) / 4.0f;
                f2 = (this.aoLightValueZNeg + this.topNorthEdgeAmbientOcclusionLight + this.northEastEdgeAmbientOcclusionLight + this.topNorthEastVertexAmbientOcclusionLight) / 4.0f;
                f3 = (this.bottomNorthEdgeAmbientOcclusionLight + this.aoLightValueZNeg + this.bottomNorthEastVertexAmbientOcclusionLight + this.northEastEdgeAmbientOcclusionLight) / 4.0f;
                f4 = (this.bottomNorthWestVertexAmbientOcclusionLight + this.northWestEdgeAmbientOcclusionLight + this.bottomNorthEdgeAmbientOcclusionLight + this.aoLightValueZNeg) / 4.0f;
            } else {
                f = f2 = f3 = f4 = this.aoLightValueZNeg;
            }
            this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = (bl4 ? r : 1.0f) * 0.8f;
            this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = (bl4 ? g : 1.0f) * 0.8f;
            this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = (bl4 ? b : 1.0f) * 0.8f;
            this.colorRedTopLeft *= f;
            this.colorGreenTopLeft *= f;
            this.colorBlueTopLeft *= f;
            this.colorRedBottomLeft *= f2;
            this.colorGreenBottomLeft *= f2;
            this.colorBlueBottomLeft *= f2;
            this.colorRedBottomRight *= f3;
            this.colorGreenBottomRight *= f3;
            this.colorBlueBottomRight *= f3;
            this.colorRedTopRight *= f4;
            this.colorGreenTopRight *= f4;
            this.colorBlueTopRight *= f4;
            n = block.getBlockTexture(this.blockAccess, x, y, z, 2);
            this.renderEastFace(block, x, y, z, n);
            if (fancyGrass && n == 3 && this.overrideBlockTexture < 0) {
                this.colorRedTopLeft *= r;
                this.colorRedBottomLeft *= r;
                this.colorRedBottomRight *= r;
                this.colorRedTopRight *= r;
                this.colorGreenTopLeft *= g;
                this.colorGreenBottomLeft *= g;
                this.colorGreenBottomRight *= g;
                this.colorGreenTopRight *= g;
                this.colorBlueTopLeft *= b;
                this.colorBlueBottomLeft *= b;
                this.colorBlueBottomRight *= b;
                this.colorBlueTopRight *= b;
                this.renderEastFace(block, x, y, z, 38);
            }
            bl = true;
        }
        if (this.renderAllFaces || block.shouldSideBeRendered(this.blockAccess, x, y, z + 1, 3)) {
            if (this.ambientOcclusionLevel > 0) {
                ++z;
                this.southWestEdgeAmbientOcclusionLight = block.getBlockBrightness(this.blockAccess, x - 1, y, z);
                this.southEastEdgeAmbientOcclusionLight = block.getBlockBrightness(this.blockAccess, x + 1, y, z);
                this.bottomSouthEdgeAmbientOcclusionLight = block.getBlockBrightness(this.blockAccess, x, y - 1, z);
                this.topSouthEdgeAmbientOcclusionLight = block.getBlockBrightness(this.blockAccess, x, y + 1, z);
                if (this.southWestEdgeTranslucent || this.bottomSouthEdgeTranslucent) {
                    this.bottomSouthWestVertexAmbientOcclusionLight = block.getBlockBrightness(this.blockAccess, x - 1, y - 1, z);
                }
                else {
                    this.bottomSouthWestVertexAmbientOcclusionLight = this.southWestEdgeAmbientOcclusionLight;
                }
                if (this.southWestEdgeTranslucent || this.topSouthEdgeTranslucent) {
                    this.topSouthWestVertexAmbientOcclusionLight = block.getBlockBrightness(this.blockAccess, x - 1, y + 1, z);
                }
                else {
                    this.topSouthWestVertexAmbientOcclusionLight = this.southWestEdgeAmbientOcclusionLight;
                }
                if (this.southEastEdgeTranslucent || this.bottomSouthEdgeTranslucent) {
                    this.bottomSouthEastVertexAmbientOcclusionLight = block.getBlockBrightness(this.blockAccess, x + 1, y - 1, z);
                }
                else {
                    this.bottomSouthEastVertexAmbientOcclusionLight = this.southEastEdgeAmbientOcclusionLight;
                }
                if (this.southEastEdgeTranslucent || this.topSouthEdgeTranslucent) {
                    this.topSouthEastVertexAmbientOcclusionLight = block.getBlockBrightness(this.blockAccess, x + 1, y + 1, z);
                }
                else {
                    this.topSouthEastVertexAmbientOcclusionLight = this.southEastEdgeAmbientOcclusionLight;
                }
                --z;
                f = (this.southWestEdgeAmbientOcclusionLight + this.topSouthWestVertexAmbientOcclusionLight + this.aoLightValueZPos + this.topSouthEdgeAmbientOcclusionLight) / 4.0f;
                f4 = (this.aoLightValueZPos + this.topSouthEdgeAmbientOcclusionLight + this.southEastEdgeAmbientOcclusionLight + this.topSouthEastVertexAmbientOcclusionLight) / 4.0f;
                f3 = (this.bottomSouthEdgeAmbientOcclusionLight + this.aoLightValueZPos + this.bottomSouthEastVertexAmbientOcclusionLight + this.southEastEdgeAmbientOcclusionLight) / 4.0f;
                f2 = (this.bottomSouthWestVertexAmbientOcclusionLight + this.southWestEdgeAmbientOcclusionLight + this.bottomSouthEdgeAmbientOcclusionLight + this.aoLightValueZPos) / 4.0f;
            } else {
                f = f2 = f3 = f4 = this.aoLightValueZPos;
            }
            this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = (bl5 ? r : 1.0f) * 0.8f;
            this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = (bl5 ? g : 1.0f) * 0.8f;
            this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = (bl5 ? b : 1.0f) * 0.8f;
            this.colorRedTopLeft *= f;
            this.colorGreenTopLeft *= f;
            this.colorBlueTopLeft *= f;
            this.colorRedBottomLeft *= f2;
            this.colorGreenBottomLeft *= f2;
            this.colorBlueBottomLeft *= f2;
            this.colorRedBottomRight *= f3;
            this.colorGreenBottomRight *= f3;
            this.colorBlueBottomRight *= f3;
            this.colorRedTopRight *= f4;
            this.colorGreenTopRight *= f4;
            this.colorBlueTopRight *= f4;
            n = block.getBlockTexture(this.blockAccess, x, y, z, 3);
            this.renderWestFace(block, x, y, z, block.getBlockTexture(this.blockAccess, x, y, z, 3));
            if (fancyGrass && n == 3 && this.overrideBlockTexture < 0) {
                this.colorRedTopLeft *= r;
                this.colorRedBottomLeft *= r;
                this.colorRedBottomRight *= r;
                this.colorRedTopRight *= r;
                this.colorGreenTopLeft *= g;
                this.colorGreenBottomLeft *= g;
                this.colorGreenBottomRight *= g;
                this.colorGreenTopRight *= g;
                this.colorBlueTopLeft *= b;
                this.colorBlueBottomLeft *= b;
                this.colorBlueBottomRight *= b;
                this.colorBlueTopRight *= b;
                this.renderWestFace(block, x, y, z, 38);
            }
            bl = true;
        }
        if (this.renderAllFaces || block.shouldSideBeRendered(this.blockAccess, x - 1, y, z, 4)) {
            if (this.ambientOcclusionLevel > 0) {
                --x;
                this.bottomWestEdgeAmbientOcclusionLight = block.getBlockBrightness(this.blockAccess, x, y - 1, z);
                this.northWestEdgeAmbientOcclusionLight = block.getBlockBrightness(this.blockAccess, x, y, z - 1);
                this.southWestEdgeAmbientOcclusionLight = block.getBlockBrightness(this.blockAccess, x, y, z + 1);
                this.topWestEdgeAmbientOcclusionLight = block.getBlockBrightness(this.blockAccess, x, y + 1, z);
                if (this.northWestEdgeTranslucent || this.bottomWestEdgeTranslucent) {
                    this.bottomNorthWestVertexAmbientOcclusionLight = block.getBlockBrightness(this.blockAccess, x, y - 1, z - 1);
                }
                else {
                    this.bottomNorthWestVertexAmbientOcclusionLight = this.northWestEdgeAmbientOcclusionLight;
                }
                if (this.southWestEdgeTranslucent || this.bottomWestEdgeTranslucent) {
                    this.bottomSouthWestVertexAmbientOcclusionLight = block.getBlockBrightness(this.blockAccess, x, y - 1, z + 1);
                }
                else {
                    this.bottomSouthWestVertexAmbientOcclusionLight = this.southWestEdgeAmbientOcclusionLight;
                }
                if (this.northWestEdgeTranslucent || this.topWestEdgeTranslucent) {
                    this.topNorthWestVertexAmbientOcclusionLight = block.getBlockBrightness(this.blockAccess, x, y + 1, z - 1);
                }
                else {
                    this.topNorthWestVertexAmbientOcclusionLight = this.northWestEdgeAmbientOcclusionLight;
                }
                if (this.southWestEdgeTranslucent || this.topWestEdgeTranslucent) {
                    this.topSouthWestVertexAmbientOcclusionLight = block.getBlockBrightness(this.blockAccess, x, y + 1, z + 1);
                }
                else {
                    this.topSouthWestVertexAmbientOcclusionLight = this.southWestEdgeAmbientOcclusionLight;
                }
                ++x;
                f4 = (this.bottomWestEdgeAmbientOcclusionLight + this.bottomSouthWestVertexAmbientOcclusionLight + this.aoLightValueXNeg + this.southWestEdgeAmbientOcclusionLight) / 4.0f;
                f = (this.aoLightValueXNeg + this.southWestEdgeAmbientOcclusionLight + this.topWestEdgeAmbientOcclusionLight + this.topSouthWestVertexAmbientOcclusionLight) / 4.0f;
                f2 = (this.northWestEdgeAmbientOcclusionLight + this.aoLightValueXNeg + this.topNorthWestVertexAmbientOcclusionLight + this.topWestEdgeAmbientOcclusionLight) / 4.0f;
                f3 = (this.bottomNorthWestVertexAmbientOcclusionLight + this.bottomWestEdgeAmbientOcclusionLight + this.northWestEdgeAmbientOcclusionLight + this.aoLightValueXNeg) / 4.0f;
            } else {
                f = f2 = f3 = f4 = this.aoLightValueXNeg;
            }
            this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = (bl6 ? r : 1.0f) * 0.6f;
            this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = (bl6 ? g : 1.0f) * 0.6f;
            this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = (bl6 ? b : 1.0f) * 0.6f;
            this.colorRedTopLeft *= f;
            this.colorGreenTopLeft *= f;
            this.colorBlueTopLeft *= f;
            this.colorRedBottomLeft *= f2;
            this.colorGreenBottomLeft *= f2;
            this.colorBlueBottomLeft *= f2;
            this.colorRedBottomRight *= f3;
            this.colorGreenBottomRight *= f3;
            this.colorBlueBottomRight *= f3;
            this.colorRedTopRight *= f4;
            this.colorGreenTopRight *= f4;
            this.colorBlueTopRight *= f4;
            n = block.getBlockTexture(this.blockAccess, x, y, z, 4);
            this.renderNorthFace(block, x, y, z, n);
            if (fancyGrass && n == 3 && this.overrideBlockTexture < 0) {
                this.colorRedTopLeft *= r;
                this.colorRedBottomLeft *= r;
                this.colorRedBottomRight *= r;
                this.colorRedTopRight *= r;
                this.colorGreenTopLeft *= g;
                this.colorGreenBottomLeft *= g;
                this.colorGreenBottomRight *= g;
                this.colorGreenTopRight *= g;
                this.colorBlueTopLeft *= b;
                this.colorBlueBottomLeft *= b;
                this.colorBlueBottomRight *= b;
                this.colorBlueTopRight *= b;
                this.renderNorthFace(block, x, y, z, 38);
            }
            bl = true;
        }
        if (this.renderAllFaces || block.shouldSideBeRendered(this.blockAccess, x + 1, y, z, 5)) {
            if (this.ambientOcclusionLevel > 0) {
                ++x;
                this.bottomEastEdgeAmbientOcclusionLight = block.getBlockBrightness(this.blockAccess, x, y - 1, z);
                this.northEastEdgeAmbientOcclusionLight = block.getBlockBrightness(this.blockAccess, x, y, z - 1);
                this.southEastEdgeAmbientOcclusionLight = block.getBlockBrightness(this.blockAccess, x, y, z + 1);
                this.topEastEdgeAmbientOcclusionLight = block.getBlockBrightness(this.blockAccess, x, y + 1, z);
                if (this.bottomEastEdgeTranslucent || this.northEastEdgeTranslucent) {
                    this.bottomNorthEastVertexAmbientOcclusionLight = block.getBlockBrightness(this.blockAccess, x, y - 1, z - 1);
                }
                else {
                    this.bottomNorthEastVertexAmbientOcclusionLight = this.northEastEdgeAmbientOcclusionLight;
                }
                if (this.bottomEastEdgeTranslucent || this.southEastEdgeTranslucent) {
                    this.bottomSouthEastVertexAmbientOcclusionLight = block.getBlockBrightness(this.blockAccess, x, y - 1, z + 1);
                }
                else {
                    this.bottomSouthEastVertexAmbientOcclusionLight = this.southEastEdgeAmbientOcclusionLight;
                }
                if (this.topEastEdgeTranslucent || this.northEastEdgeTranslucent) {
                    this.topNorthEastVertexAmbientOcclusionLight = block.getBlockBrightness(this.blockAccess, x, y + 1, z - 1);
                }
                else {
                    this.topNorthEastVertexAmbientOcclusionLight = this.northEastEdgeAmbientOcclusionLight;
                }
                if (this.topEastEdgeTranslucent || this.southEastEdgeTranslucent) {
                    this.topSouthEastVertexAmbientOcclusionLight = block.getBlockBrightness(this.blockAccess, x, y + 1, z + 1);
                }
                else {
                    this.topSouthEastVertexAmbientOcclusionLight = this.southEastEdgeAmbientOcclusionLight;
                }
                --x;
                f = (this.bottomEastEdgeAmbientOcclusionLight + this.bottomSouthEastVertexAmbientOcclusionLight + this.aoLightValueXPos + this.southEastEdgeAmbientOcclusionLight) / 4.0f;
                f4 = (this.aoLightValueXPos + this.southEastEdgeAmbientOcclusionLight + this.topEastEdgeAmbientOcclusionLight + this.topSouthEastVertexAmbientOcclusionLight) / 4.0f;
                f3 = (this.northEastEdgeAmbientOcclusionLight + this.aoLightValueXPos + this.topNorthEastVertexAmbientOcclusionLight + this.topEastEdgeAmbientOcclusionLight) / 4.0f;
                f2 = (this.bottomNorthEastVertexAmbientOcclusionLight + this.bottomEastEdgeAmbientOcclusionLight + this.northEastEdgeAmbientOcclusionLight + this.aoLightValueXPos) / 4.0f;
            } else {
                f = f2 = f3 = f4 = this.aoLightValueXPos;
            }
            this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = (bl7 ? r : 1.0f) * 0.6f;
            this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = (bl7 ? g : 1.0f) * 0.6f;
            this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = (bl7 ? b : 1.0f) * 0.6f;
            this.colorRedTopLeft *= f;
            this.colorGreenTopLeft *= f;
            this.colorBlueTopLeft *= f;
            this.colorRedBottomLeft *= f2;
            this.colorGreenBottomLeft *= f2;
            this.colorBlueBottomLeft *= f2;
            this.colorRedBottomRight *= f3;
            this.colorGreenBottomRight *= f3;
            this.colorBlueBottomRight *= f3;
            this.colorRedTopRight *= f4;
            this.colorGreenTopRight *= f4;
            this.colorBlueTopRight *= f4;
            n = block.getBlockTexture(this.blockAccess, x, y, z, 5);
            this.renderSouthFace(block, x, y, z, n);
            if (fancyGrass && n == 3 && this.overrideBlockTexture < 0) {
                this.colorRedTopLeft *= r;
                this.colorRedBottomLeft *= r;
                this.colorRedBottomRight *= r;
                this.colorRedTopRight *= r;
                this.colorGreenTopLeft *= g;
                this.colorGreenBottomLeft *= g;
                this.colorGreenBottomRight *= g;
                this.colorGreenTopRight *= g;
                this.colorBlueTopLeft *= b;
                this.colorBlueBottomLeft *= b;
                this.colorBlueBottomRight *= b;
                this.colorBlueTopRight *= b;
                this.renderSouthFace(block, x, y, z, 38);
            }
            bl = true;
        }
        this.enableAO = false;
        return bl;
    }

    public boolean renderStandardBlockWithColorMultiplier(Block block, int x, int y, int z, float r, float g, float b) {
        int n;
        float f;
        this.enableAO = false;
        Tessellator tessellator = Tessellator.instance;
        boolean bl = false;
        float f2 = 0.5f;
        float f3 = 1.0f;
        float f4 = 0.8f;
        float f5 = 0.6f;
        float f6 = f3 * r;
        float f7 = f3 * g;
        float f8 = f3 * b;
        float f9 = f2;
        float f10 = f4;
        float f11 = f5;
        float f12 = f2;
        float f13 = f4;
        float f14 = f5;
        float f15 = f2;
        float f16 = f4;
        float f17 = f5;
        if (block != Block.grass) {
            f9 *= r;
            f10 *= r;
            f11 *= r;
            f12 *= g;
            f13 *= g;
            f14 *= g;
            f15 *= b;
            f16 *= b;
            f17 *= b;
        }
        float f18 = block.getBlockBrightness(this.blockAccess, x, y, z);
        if (this.renderAllFaces || block.shouldSideBeRendered(this.blockAccess, x, y - 1, z, 0)) {
            f = block.getBlockBrightness(this.blockAccess, x, y - 1, z);
            tessellator.setColorOpaque_F(f9 * f, f12 * f, f15 * f);
            this.renderBottomFace(block, x, y, z, block.getBlockTexture(this.blockAccess, x, y, z, 0));
            bl = true;
        }
        if (this.renderAllFaces || block.shouldSideBeRendered(this.blockAccess, x, y + 1, z, 1)) {
            f = block.getBlockBrightness(this.blockAccess, x, y + 1, z);
            if (block.maxY != 1.0 && !block.blockMaterial.getIsLiquid()) {
                f = f18;
            }
            tessellator.setColorOpaque_F(f6 * f, f7 * f, f8 * f);
            this.renderTopFace(block, x, y, z, block.getBlockTexture(this.blockAccess, x, y, z, 1));
            bl = true;
        }
        if (this.renderAllFaces || block.shouldSideBeRendered(this.blockAccess, x, y, z - 1, 2)) {
            f = block.getBlockBrightness(this.blockAccess, x, y, z - 1);
            if (block.minZ > 0.0) {
                f = f18;
            }
            tessellator.setColorOpaque_F(f10 * f, f13 * f, f16 * f);
            n = block.getBlockTexture(this.blockAccess, x, y, z, 2);
            this.renderEastFace(block, x, y, z, n);
            if (fancyGrass && n == 3 && this.overrideBlockTexture < 0) {
                tessellator.setColorOpaque_F(f10 * f * r, f13 * f * g, f16 * f * b);
                this.renderEastFace(block, x, y, z, 38);
            }
            bl = true;
        }
        if (this.renderAllFaces || block.shouldSideBeRendered(this.blockAccess, x, y, z + 1, 3)) {
            f = block.getBlockBrightness(this.blockAccess, x, y, z + 1);
            if (block.maxZ < 1.0) {
                f = f18;
            }
            tessellator.setColorOpaque_F(f10 * f, f13 * f, f16 * f);
            n = block.getBlockTexture(this.blockAccess, x, y, z, 3);
            this.renderWestFace(block, x, y, z, n);
            if (fancyGrass && n == 3 && this.overrideBlockTexture < 0) {
                tessellator.setColorOpaque_F(f10 * f * r, f13 * f * g, f16 * f * b);
                this.renderWestFace(block, x, y, z, 38);
            }
            bl = true;
        }
        if (this.renderAllFaces || block.shouldSideBeRendered(this.blockAccess, x - 1, y, z, 4)) {
            f = block.getBlockBrightness(this.blockAccess, x - 1, y, z);
            if (block.minX > 0.0) {
                f = f18;
            }
            tessellator.setColorOpaque_F(f11 * f, f14 * f, f17 * f);
            n = block.getBlockTexture(this.blockAccess, x, y, z, 4);
            this.renderNorthFace(block, x, y, z, n);
            if (fancyGrass && n == 3 && this.overrideBlockTexture < 0) {
                tessellator.setColorOpaque_F(f11 * f * r, f14 * f * g, f17 * f * b);
                this.renderNorthFace(block, x, y, z, 38);
            }
            bl = true;
        }
        if (this.renderAllFaces || block.shouldSideBeRendered(this.blockAccess, x + 1, y, z, 5)) {
            f = block.getBlockBrightness(this.blockAccess, x + 1, y, z);
            if (block.maxX < 1.0) {
                f = f18;
            }
            tessellator.setColorOpaque_F(f11 * f, f14 * f, f17 * f);
            n = block.getBlockTexture(this.blockAccess, x, y, z, 5);
            this.renderSouthFace(block, x, y, z, n);
            if (fancyGrass && n == 3 && this.overrideBlockTexture < 0) {
                tessellator.setColorOpaque_F(f11 * f * r, f14 * f * g, f17 * f * b);
                this.renderSouthFace(block, x, y, z, 38);
            }
            bl = true;
        }
        return bl;
    }

    public boolean renderBlockCactus(Block block, int x, int y, int z) {
        int n = block.colorMultiplier(this.blockAccess, x, y, z);
        float f = (float)(n >> 16 & 0xFF) / 255.0f;
        float f2 = (float)(n >> 8 & 0xFF) / 255.0f;
        float f3 = (float)(n & 0xFF) / 255.0f;
        if (EntityRenderer.anaglyphEnabled) {
            float f4 = (f * 30.0f + f2 * 59.0f + f3 * 11.0f) / 100.0f;
            float f5 = (f * 30.0f + f2 * 70.0f) / 100.0f;
            float f6 = (f * 30.0f + f3 * 70.0f) / 100.0f;
            f = f4;
            f2 = f5;
            f3 = f6;
        }
        return this.tesselateCactus(block, x, y, z, f, f2, f3);
    }

    public boolean tesselateCactus(Block block, int x, int y, int z, float r, float g, float b) {
        float f;
        Tessellator tessellator = Tessellator.instance;
        boolean bl = false;
        float f2 = 0.5f;
        float f3 = 1.0f;
        float f4 = 0.8f;
        float f5 = 0.6f;
        float f6 = f2 * r;
        float f7 = f3 * r;
        float f8 = f4 * r;
        float f9 = f5 * r;
        float f10 = f2 * g;
        float f11 = f3 * g;
        float f12 = f4 * g;
        float f13 = f5 * g;
        float f14 = f2 * b;
        float f15 = f3 * b;
        float f16 = f4 * b;
        float f17 = f5 * b;
        float f18 = 0.0625f;
        float f19 = block.getBlockBrightness(this.blockAccess, x, y, z);
        if (this.renderAllFaces || block.shouldSideBeRendered(this.blockAccess, x, y - 1, z, 0)) {
            f = block.getBlockBrightness(this.blockAccess, x, y - 1, z);
            tessellator.setColorOpaque_F(f6 * f, f10 * f, f14 * f);
            this.renderBottomFace(block, x, y, z, block.getBlockTexture(this.blockAccess, x, y, z, 0));
            bl = true;
        }
        if (this.renderAllFaces || block.shouldSideBeRendered(this.blockAccess, x, y + 1, z, 1)) {
            f = block.getBlockBrightness(this.blockAccess, x, y + 1, z);
            if (block.maxY != 1.0 && !block.blockMaterial.getIsLiquid()) {
                f = f19;
            }
            tessellator.setColorOpaque_F(f7 * f, f11 * f, f15 * f);
            this.renderTopFace(block, x, y, z, block.getBlockTexture(this.blockAccess, x, y, z, 1));
            bl = true;
        }
        if (this.renderAllFaces || block.shouldSideBeRendered(this.blockAccess, x, y, z - 1, 2)) {
            f = block.getBlockBrightness(this.blockAccess, x, y, z - 1);
            if (block.minZ > 0.0) {
                f = f19;
            }
            tessellator.setColorOpaque_F(f8 * f, f12 * f, f16 * f);
            tessellator.setTranslationF(0.0f, 0.0f, f18);
            this.renderEastFace(block, x, y, z, block.getBlockTexture(this.blockAccess, x, y, z, 2));
            tessellator.setTranslationF(0.0f, 0.0f, -f18);
            bl = true;
        }
        if (this.renderAllFaces || block.shouldSideBeRendered(this.blockAccess, x, y, z + 1, 3)) {
            f = block.getBlockBrightness(this.blockAccess, x, y, z + 1);
            if (block.maxZ < 1.0) {
                f = f19;
            }
            tessellator.setColorOpaque_F(f8 * f, f12 * f, f16 * f);
            tessellator.setTranslationF(0.0f, 0.0f, -f18);
            this.renderWestFace(block, x, y, z, block.getBlockTexture(this.blockAccess, x, y, z, 3));
            tessellator.setTranslationF(0.0f, 0.0f, f18);
            bl = true;
        }
        if (this.renderAllFaces || block.shouldSideBeRendered(this.blockAccess, x - 1, y, z, 4)) {
            f = block.getBlockBrightness(this.blockAccess, x - 1, y, z);
            if (block.minX > 0.0) {
                f = f19;
            }
            tessellator.setColorOpaque_F(f9 * f, f13 * f, f17 * f);
            tessellator.setTranslationF(f18, 0.0f, 0.0f);
            this.renderNorthFace(block, x, y, z, block.getBlockTexture(this.blockAccess, x, y, z, 4));
            tessellator.setTranslationF(-f18, 0.0f, 0.0f);
            bl = true;
        }
        if (this.renderAllFaces || block.shouldSideBeRendered(this.blockAccess, x + 1, y, z, 5)) {
            f = block.getBlockBrightness(this.blockAccess, x + 1, y, z);
            if (block.maxX < 1.0) {
                f = f19;
            }
            tessellator.setColorOpaque_F(f9 * f, f13 * f, f17 * f);
            tessellator.setTranslationF(-f18, 0.0f, 0.0f);
            this.renderSouthFace(block, x, y, z, block.getBlockTexture(this.blockAccess, x, y, z, 5));
            tessellator.setTranslationF(f18, 0.0f, 0.0f);
            bl = true;
        }
        return bl;
    }

    public boolean renderBlockFence(Block block, int x, int y, int z) {
        boolean flag = false;
        float f = 0.375F;
        float f1 = 0.625F;
        block.setBlockBounds(f, 0.0F, f, f1, 1.0F, f1);
        this.renderStandardBlock(block, x, y, z);
        flag = true;
        boolean flag1 = false;
        boolean flag2 = false;
        if (this.blockAccess.getBlockId(x - 1, y, z) == block.blockID || this.blockAccess.getBlockId(x + 1, y, z) == block.blockID) {
            flag1 = true;
        }
        if (this.blockAccess.getBlockId(x, y, z - 1) == block.blockID || this.blockAccess.getBlockId(x, y, z + 1) == block.blockID) {
            flag2 = true;
        }
        boolean flag3 = this.blockAccess.getBlockId(x - 1, y, z) == block.blockID;
        boolean flag4 = this.blockAccess.getBlockId(x + 1, y, z) == block.blockID;
        boolean flag5 = this.blockAccess.getBlockId(x, y, z - 1) == block.blockID;
        boolean flag6 = this.blockAccess.getBlockId(x, y, z + 1) == block.blockID;
        if (!flag1 && !flag2) {
            flag1 = true;
        }
        f = 0.4375F;
        f1 = 0.5625F;
        float f2 = 0.75F;
        float f3 = 0.9375F;
        float f4 = flag3 ? 0.0F : f;
        float f5 = flag4 ? 1.0F : f1;
        float f6 = flag5 ? 0.0F : f;
        float f7 = flag6 ? 1.0F : f1;
        if (flag1) {
            block.setBlockBounds(f4, f2, f, f5, f3, f1);
            this.renderStandardBlock(block, x, y, z);
            flag = true;
        }
        if (flag2) {
            block.setBlockBounds(f, f2, f6, f1, f3, f7);
            this.renderStandardBlock(block, x, y, z);
            flag = true;
        }
        f2 = 0.375F;
        f3 = 0.5625F;
        if (flag1) {
            block.setBlockBounds(f4, f2, f, f5, f3, f1);
            this.renderStandardBlock(block, x, y, z);
            flag = true;
        }
        if (flag2) {
            block.setBlockBounds(f, f2, f6, f1, f3, f7);
            this.renderStandardBlock(block, x, y, z);
            flag = true;
        }
        block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        return flag;
    }

    public boolean renderBlockStairs(Block block, int x, int y, int z) {
        boolean bl = false;
        int n = this.blockAccess.getBlockMetadata(x, y, z);
        if (n == 0) {
            block.setBlockBounds(0.0f, 0.0f, 0.0f, 0.5f, 0.5f, 1.0f);
            this.renderStandardBlock(block, x, y, z);
            block.setBlockBounds(0.5f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
            this.renderStandardBlock(block, x, y, z);
            bl = true;
        } else if (n == 1) {
            block.setBlockBounds(0.0f, 0.0f, 0.0f, 0.5f, 1.0f, 1.0f);
            this.renderStandardBlock(block, x, y, z);
            block.setBlockBounds(0.5f, 0.0f, 0.0f, 1.0f, 0.5f, 1.0f);
            this.renderStandardBlock(block, x, y, z);
            bl = true;
        } else if (n == 2) {
            block.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.5f, 0.5f);
            this.renderStandardBlock(block, x, y, z);
            block.setBlockBounds(0.0f, 0.0f, 0.5f, 1.0f, 1.0f, 1.0f);
            this.renderStandardBlock(block, x, y, z);
            bl = true;
        } else if (n == 3) {
            block.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.5f);
            this.renderStandardBlock(block, x, y, z);
            block.setBlockBounds(0.0f, 0.0f, 0.5f, 1.0f, 0.5f, 1.0f);
            this.renderStandardBlock(block, x, y, z);
            bl = true;
        }
        block.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
        return bl;
    }

    public boolean renderBlockDoor(Block block, int x, int y, int z) {
        Tessellator tessellator = Tessellator.instance;
        BlockDoor blockDoor = (BlockDoor)block;
        boolean bl = false;
        float f = 0.5f;
        float f2 = 1.0f;
        float f3 = 0.8f;
        float f4 = 0.6f;
        float f5 = block.getBlockBrightness(this.blockAccess, x, y, z);
        float f6 = block.getBlockBrightness(this.blockAccess, x, y - 1, z);
        if (blockDoor.minY > 0.0) {
            f6 = f5;
        }
        if (Block.lightValue[block.blockID] > 0) {
            f6 = 1.0f;
        }
        tessellator.setColorOpaque_F(f * f6, f * f6, f * f6);
        this.renderBottomFace(block, x, y, z, block.getBlockTexture(this.blockAccess, x, y, z, 0));
        bl = true;
        f6 = block.getBlockBrightness(this.blockAccess, x, y + 1, z);
        if (blockDoor.maxY < 1.0) {
            f6 = f5;
        }
        if (Block.lightValue[block.blockID] > 0) {
            f6 = 1.0f;
        }
        tessellator.setColorOpaque_F(f2 * f6, f2 * f6, f2 * f6);
        this.renderTopFace(block, x, y, z, block.getBlockTexture(this.blockAccess, x, y, z, 1));
        bl = true;
        f6 = block.getBlockBrightness(this.blockAccess, x, y, z - 1);
        if (blockDoor.minZ > 0.0) {
            f6 = f5;
        }
        if (Block.lightValue[block.blockID] > 0) {
            f6 = 1.0f;
        }
        tessellator.setColorOpaque_F(f3 * f6, f3 * f6, f3 * f6);
        int n = block.getBlockTexture(this.blockAccess, x, y, z, 2);
        if (n < 0) {
            this.flipTexture = true;
            n = -n;
        }
        this.renderEastFace(block, x, y, z, n);
        bl = true;
        this.flipTexture = false;
        f6 = block.getBlockBrightness(this.blockAccess, x, y, z + 1);
        if (blockDoor.maxZ < 1.0) {
            f6 = f5;
        }
        if (Block.lightValue[block.blockID] > 0) {
            f6 = 1.0f;
        }
        tessellator.setColorOpaque_F(f3 * f6, f3 * f6, f3 * f6);
        n = block.getBlockTexture(this.blockAccess, x, y, z, 3);
        if (n < 0) {
            this.flipTexture = true;
            n = -n;
        }
        this.renderWestFace(block, x, y, z, n);
        bl = true;
        this.flipTexture = false;
        f6 = block.getBlockBrightness(this.blockAccess, x - 1, y, z);
        if (blockDoor.minX > 0.0) {
            f6 = f5;
        }
        if (Block.lightValue[block.blockID] > 0) {
            f6 = 1.0f;
        }
        tessellator.setColorOpaque_F(f4 * f6, f4 * f6, f4 * f6);
        n = block.getBlockTexture(this.blockAccess, x, y, z, 4);
        if (n < 0) {
            this.flipTexture = true;
            n = -n;
        }
        this.renderNorthFace(block, x, y, z, n);
        bl = true;
        this.flipTexture = false;
        f6 = block.getBlockBrightness(this.blockAccess, x + 1, y, z);
        if (blockDoor.maxX < 1.0) {
            f6 = f5;
        }
        if (Block.lightValue[block.blockID] > 0) {
            f6 = 1.0f;
        }
        tessellator.setColorOpaque_F(f4 * f6, f4 * f6, f4 * f6);
        n = block.getBlockTexture(this.blockAccess, x, y, z, 5);
        if (n < 0) {
            this.flipTexture = true;
            n = -n;
        }
        this.renderSouthFace(block, x, y, z, n);
        bl = true;
        this.flipTexture = false;
        return bl;
    }

    public void renderBottomFace(Block block, double x, double y, double z, int sprite) {
        Tessellator tessellator = Tessellator.instance;
        if (this.overrideBlockTexture >= 0) {
            sprite = this.overrideBlockTexture;
        }
        int n = (sprite & 0xF) << 4;
        int n2 = sprite & 0xF0;
        double d = ((double)n + block.minX * 16.0) / 256.0;
        double d2 = ((double)n + block.maxX * 16.0 - 0.01) / 256.0;
        double d3 = ((double)n2 + block.minZ * 16.0) / 256.0;
        double d4 = ((double)n2 + block.maxZ * 16.0 - 0.01) / 256.0;
        if (block.minX < 0.0 || block.maxX > 1.0) {
            d = ((float)n + 0.0f) / 256.0f;
            d2 = ((float)n + 15.99f) / 256.0f;
        }
        if (block.minZ < 0.0 || block.maxZ > 1.0) {
            d3 = ((float)n2 + 0.0f) / 256.0f;
            d4 = ((float)n2 + 15.99f) / 256.0f;
        }
        double d5 = d2;
        double d6 = d;
        double d7 = d3;
        double d8 = d4;
        if (this.bottomFaceRotation == 2) {
            d = ((double)n + block.minZ * 16.0) / 256.0;
            d3 = ((double)(n2 + 16) - block.maxX * 16.0) / 256.0;
            d2 = ((double)n + block.maxZ * 16.0) / 256.0;
            d4 = ((double)(n2 + 16) - block.minX * 16.0) / 256.0;
            d5 = d2;
            d6 = d;
            d7 = d3;
            d8 = d4;
            d5 = d;
            d6 = d2;
            d3 = d4;
            d4 = d7;
        } else if (this.bottomFaceRotation == 1) {
            d = ((double)(n + 16) - block.maxZ * 16.0) / 256.0;
            d3 = ((double)n2 + block.minX * 16.0) / 256.0;
            d2 = ((double)(n + 16) - block.minZ * 16.0) / 256.0;
            d4 = ((double)n2 + block.maxX * 16.0) / 256.0;
            d5 = d2;
            d6 = d;
            d7 = d3;
            d8 = d4;
            d = d5;
            d2 = d6;
            d7 = d4;
            d8 = d3;
        } else if (this.bottomFaceRotation == 3) {
            d = ((double)(n + 16) - block.minX * 16.0) / 256.0;
            d2 = ((double)(n + 16) - block.maxX * 16.0 - 0.01) / 256.0;
            d3 = ((double)(n2 + 16) - block.minZ * 16.0) / 256.0;
            d4 = ((double)(n2 + 16) - block.maxZ * 16.0 - 0.01) / 256.0;
            d5 = d2;
            d6 = d;
            d7 = d3;
            d8 = d4;
        }
        double d9 = x + block.minX;
        double d10 = x + block.maxX;
        double d11 = y + block.minY;
        double d12 = z + block.minZ;
        double d13 = z + block.maxZ;
        if (this.enableAO) {
            tessellator.setColorOpaque_F(this.colorRedTopLeft, this.colorGreenTopLeft, this.colorBlueTopLeft);
            tessellator.addVertexWithUV(d9, d11, d13, d6, d8);
            tessellator.setColorOpaque_F(this.colorRedBottomLeft, this.colorGreenBottomLeft, this.colorBlueBottomLeft);
            tessellator.addVertexWithUV(d9, d11, d12, d, d3);
            tessellator.setColorOpaque_F(this.colorRedBottomRight, this.colorGreenBottomRight, this.colorBlueBottomRight);
            tessellator.addVertexWithUV(d10, d11, d12, d5, d7);
            tessellator.setColorOpaque_F(this.colorRedTopRight, this.colorGreenTopRight, this.colorBlueTopRight);
            tessellator.addVertexWithUV(d10, d11, d13, d2, d4);
        } else {
            tessellator.addVertexWithUV(d9, d11, d13, d6, d8);
            tessellator.addVertexWithUV(d9, d11, d12, d, d3);
            tessellator.addVertexWithUV(d10, d11, d12, d5, d7);
            tessellator.addVertexWithUV(d10, d11, d13, d2, d4);
        }
    }

    public void renderTopFace(Block block, double x, double y, double z, int sprite) {
        Tessellator tessellator = Tessellator.instance;
        if (this.overrideBlockTexture >= 0) {
            sprite = this.overrideBlockTexture;
        }
        int n = (sprite & 0xF) << 4;
        int n2 = sprite & 0xF0;
        double d = ((double)n + block.minX * 16.0) / 256.0;
        double d2 = ((double)n + block.maxX * 16.0 - 0.01) / 256.0;
        double d3 = ((double)n2 + block.minZ * 16.0) / 256.0;
        double d4 = ((double)n2 + block.maxZ * 16.0 - 0.01) / 256.0;
        if (block.minX < 0.0 || block.maxX > 1.0) {
            d = ((float)n + 0.0f) / 256.0f;
            d2 = ((float)n + 15.99f) / 256.0f;
        }
        if (block.minZ < 0.0 || block.maxZ > 1.0) {
            d3 = ((float)n2 + 0.0f) / 256.0f;
            d4 = ((float)n2 + 15.99f) / 256.0f;
        }
        double d5 = d2;
        double d6 = d;
        double d7 = d3;
        double d8 = d4;
        if (this.topFaceRotation == 1) {
            d = ((double)n + block.minZ * 16.0) / 256.0;
            d3 = ((double)(n2 + 16) - block.maxX * 16.0) / 256.0;
            d2 = ((double)n + block.maxZ * 16.0) / 256.0;
            d4 = ((double)(n2 + 16) - block.minX * 16.0) / 256.0;
            d5 = d2;
            d6 = d;
            d7 = d3;
            d8 = d4;
            d5 = d;
            d6 = d2;
            d3 = d4;
            d4 = d7;
        } else if (this.topFaceRotation == 2) {
            d = ((double)(n + 16) - block.maxZ * 16.0) / 256.0;
            d3 = ((double)n2 + block.minX * 16.0) / 256.0;
            d2 = ((double)(n + 16) - block.minZ * 16.0) / 256.0;
            d4 = ((double)n2 + block.maxX * 16.0) / 256.0;
            d5 = d2;
            d6 = d;
            d7 = d3;
            d8 = d4;
            d = d5;
            d2 = d6;
            d7 = d4;
            d8 = d3;
        } else if (this.topFaceRotation == 3) {
            d = ((double)(n + 16) - block.minX * 16.0) / 256.0;
            d2 = ((double)(n + 16) - block.maxX * 16.0 - 0.01) / 256.0;
            d3 = ((double)(n2 + 16) - block.minZ * 16.0) / 256.0;
            d4 = ((double)(n2 + 16) - block.maxZ * 16.0 - 0.01) / 256.0;
            d5 = d2;
            d6 = d;
            d7 = d3;
            d8 = d4;
        }
        double d9 = x + block.minX;
        double d10 = x + block.maxX;
        double d11 = y + block.maxY;
        double d12 = z + block.minZ;
        double d13 = z + block.maxZ;
        if (this.enableAO) {
            tessellator.setColorOpaque_F(this.colorRedTopLeft, this.colorGreenTopLeft, this.colorBlueTopLeft);
            tessellator.addVertexWithUV(d10, d11, d13, d2, d4);
            tessellator.setColorOpaque_F(this.colorRedBottomLeft, this.colorGreenBottomLeft, this.colorBlueBottomLeft);
            tessellator.addVertexWithUV(d10, d11, d12, d5, d7);
            tessellator.setColorOpaque_F(this.colorRedBottomRight, this.colorGreenBottomRight, this.colorBlueBottomRight);
            tessellator.addVertexWithUV(d9, d11, d12, d, d3);
            tessellator.setColorOpaque_F(this.colorRedTopRight, this.colorGreenTopRight, this.colorBlueTopRight);
            tessellator.addVertexWithUV(d9, d11, d13, d6, d8);
        } else {
            tessellator.addVertexWithUV(d10, d11, d13, d2, d4);
            tessellator.addVertexWithUV(d10, d11, d12, d5, d7);
            tessellator.addVertexWithUV(d9, d11, d12, d, d3);
            tessellator.addVertexWithUV(d9, d11, d13, d6, d8);
        }
    }

    public void renderEastFace(Block block, double x, double y, double z, int sprite) {
        double d;
        Tessellator tessellator = Tessellator.instance;
        if (this.overrideBlockTexture >= 0) {
            sprite = this.overrideBlockTexture;
        }
        int n = (sprite & 0xF) << 4;
        int n2 = sprite & 0xF0;
        double d2 = ((double)n + block.minX * 16.0) / 256.0;
        double d3 = ((double)n + block.maxX * 16.0 - 0.01) / 256.0;
        double d4 = ((double)(n2 + 16) - block.maxY * 16.0) / 256.0;
        double d5 = ((double)(n2 + 16) - block.minY * 16.0 - 0.01) / 256.0;
        if (this.flipTexture) {
            d = d2;
            d2 = d3;
            d3 = d;
        }
        if (block.minX < 0.0 || block.maxX > 1.0) {
            d2 = ((float)n + 0.0f) / 256.0f;
            d3 = ((float)n + 15.99f) / 256.0f;
        }
        if (block.minY < 0.0 || block.maxY > 1.0) {
            d4 = ((float)n2 + 0.0f) / 256.0f;
            d5 = ((float)n2 + 15.99f) / 256.0f;
        }
        d = d3;
        double d6 = d2;
        double d7 = d4;
        double d8 = d5;
        if (this.northFaceRotation == 2) {
            d2 = ((double)n + block.minY * 16.0) / 256.0;
            d4 = ((double)(n2 + 16) - block.minX * 16.0) / 256.0;
            d3 = ((double)n + block.maxY * 16.0) / 256.0;
            d5 = ((double)(n2 + 16) - block.maxX * 16.0) / 256.0;
            d = d3;
            d6 = d2;
            d7 = d4;
            d8 = d5;
            d = d2;
            d6 = d3;
            d4 = d5;
            d5 = d7;
        } else if (this.northFaceRotation == 1) {
            d2 = ((double)(n + 16) - block.maxY * 16.0) / 256.0;
            d4 = ((double)n2 + block.maxX * 16.0) / 256.0;
            d3 = ((double)(n + 16) - block.minY * 16.0) / 256.0;
            d5 = ((double)n2 + block.minX * 16.0) / 256.0;
            d = d3;
            d6 = d2;
            d7 = d4;
            d8 = d5;
            d2 = d;
            d3 = d6;
            d7 = d5;
            d8 = d4;
        } else if (this.northFaceRotation == 3) {
            d2 = ((double)(n + 16) - block.minX * 16.0) / 256.0;
            d3 = ((double)(n + 16) - block.maxX * 16.0 - 0.01) / 256.0;
            d4 = ((double)n2 + block.maxY * 16.0) / 256.0;
            d5 = ((double)n2 + block.minY * 16.0 - 0.01) / 256.0;
            d = d3;
            d6 = d2;
            d7 = d4;
            d8 = d5;
        }
        double d9 = x + block.minX;
        double d10 = x + block.maxX;
        double d11 = y + block.minY;
        double d12 = y + block.maxY;
        double d13 = z + block.minZ;
        if (this.enableAO) {
            tessellator.setColorOpaque_F(this.colorRedTopLeft, this.colorGreenTopLeft, this.colorBlueTopLeft);
            tessellator.addVertexWithUV(d9, d12, d13, d, d7);
            tessellator.setColorOpaque_F(this.colorRedBottomLeft, this.colorGreenBottomLeft, this.colorBlueBottomLeft);
            tessellator.addVertexWithUV(d10, d12, d13, d2, d4);
            tessellator.setColorOpaque_F(this.colorRedBottomRight, this.colorGreenBottomRight, this.colorBlueBottomRight);
            tessellator.addVertexWithUV(d10, d11, d13, d6, d8);
            tessellator.setColorOpaque_F(this.colorRedTopRight, this.colorGreenTopRight, this.colorBlueTopRight);
            tessellator.addVertexWithUV(d9, d11, d13, d3, d5);
        } else {
            tessellator.addVertexWithUV(d9, d12, d13, d, d7);
            tessellator.addVertexWithUV(d10, d12, d13, d2, d4);
            tessellator.addVertexWithUV(d10, d11, d13, d6, d8);
            tessellator.addVertexWithUV(d9, d11, d13, d3, d5);
        }
    }

    public void renderWestFace(Block block, double x, double y, double z, int sprite) {
        double d;
        Tessellator tessellator = Tessellator.instance;
        if (this.overrideBlockTexture >= 0) {
            sprite = this.overrideBlockTexture;
        }
        int n = (sprite & 0xF) << 4;
        int n2 = sprite & 0xF0;
        double d2 = ((double)n + block.minX * 16.0) / 256.0;
        double d3 = ((double)n + block.maxX * 16.0 - 0.01) / 256.0;
        double d4 = ((double)(n2 + 16) - block.maxY * 16.0) / 256.0;
        double d5 = ((double)(n2 + 16) - block.minY * 16.0 - 0.01) / 256.0;
        if (this.flipTexture) {
            d = d2;
            d2 = d3;
            d3 = d;
        }
        if (block.minX < 0.0 || block.maxX > 1.0) {
            d2 = ((float)n + 0.0f) / 256.0f;
            d3 = ((float)n + 15.99f) / 256.0f;
        }
        if (block.minY < 0.0 || block.maxY > 1.0) {
            d4 = ((float)n2 + 0.0f) / 256.0f;
            d5 = ((float)n2 + 15.99f) / 256.0f;
        }
        d = d3;
        double d6 = d2;
        double d7 = d4;
        double d8 = d5;
        if (this.southFaceRotation == 1) {
            d2 = ((double)n + block.minY * 16.0) / 256.0;
            d5 = ((double)(n2 + 16) - block.minX * 16.0) / 256.0;
            d3 = ((double)n + block.maxY * 16.0) / 256.0;
            d4 = ((double)(n2 + 16) - block.maxX * 16.0) / 256.0;
            d = d3;
            d6 = d2;
            d7 = d4;
            d8 = d5;
            d = d2;
            d6 = d3;
            d4 = d5;
            d5 = d7;
        } else if (this.southFaceRotation == 2) {
            d2 = ((double)(n + 16) - block.maxY * 16.0) / 256.0;
            d4 = ((double)n2 + block.minX * 16.0) / 256.0;
            d3 = ((double)(n + 16) - block.minY * 16.0) / 256.0;
            d5 = ((double)n2 + block.maxX * 16.0) / 256.0;
            d = d3;
            d6 = d2;
            d7 = d4;
            d8 = d5;
            d2 = d;
            d3 = d6;
            d7 = d5;
            d8 = d4;
        } else if (this.southFaceRotation == 3) {
            d2 = ((double)(n + 16) - block.minX * 16.0) / 256.0;
            d3 = ((double)(n + 16) - block.maxX * 16.0 - 0.01) / 256.0;
            d4 = ((double)n2 + block.maxY * 16.0) / 256.0;
            d5 = ((double)n2 + block.minY * 16.0 - 0.01) / 256.0;
            d = d3;
            d6 = d2;
            d7 = d4;
            d8 = d5;
        }
        double d9 = x + block.minX;
        double d10 = x + block.maxX;
        double d11 = y + block.minY;
        double d12 = y + block.maxY;
        double d13 = z + block.maxZ;
        if (this.enableAO) {
            tessellator.setColorOpaque_F(this.colorRedTopLeft, this.colorGreenTopLeft, this.colorBlueTopLeft);
            tessellator.addVertexWithUV(d9, d12, d13, d2, d4);
            tessellator.setColorOpaque_F(this.colorRedBottomLeft, this.colorGreenBottomLeft, this.colorBlueBottomLeft);
            tessellator.addVertexWithUV(d9, d11, d13, d6, d8);
            tessellator.setColorOpaque_F(this.colorRedBottomRight, this.colorGreenBottomRight, this.colorBlueBottomRight);
            tessellator.addVertexWithUV(d10, d11, d13, d3, d5);
            tessellator.setColorOpaque_F(this.colorRedTopRight, this.colorGreenTopRight, this.colorBlueTopRight);
            tessellator.addVertexWithUV(d10, d12, d13, d, d7);
        } else {
            tessellator.addVertexWithUV(d9, d12, d13, d2, d4);
            tessellator.addVertexWithUV(d9, d11, d13, d6, d8);
            tessellator.addVertexWithUV(d10, d11, d13, d3, d5);
            tessellator.addVertexWithUV(d10, d12, d13, d, d7);
        }
    }

    public void renderNorthFace(Block block, double x, double y, double z, int sprite) {
        double d;
        Tessellator tessellator = Tessellator.instance;
        if (this.overrideBlockTexture >= 0) {
            sprite = this.overrideBlockTexture;
        }
        int n = (sprite & 0xF) << 4;
        int n2 = sprite & 0xF0;
        double d2 = ((double)n + block.minZ * 16.0) / 256.0;
        double d3 = ((double)n + block.maxZ * 16.0 - 0.01) / 256.0;
        double d4 = ((double)(n2 + 16) - block.maxY * 16.0) / 256.0;
        double d5 = ((double)(n2 + 16) - block.minY * 16.0 - 0.01) / 256.0;
        if (this.flipTexture) {
            d = d2;
            d2 = d3;
            d3 = d;
        }
        if (block.minZ < 0.0 || block.maxZ > 1.0) {
            d2 = ((float)n + 0.0f) / 256.0f;
            d3 = ((float)n + 15.99f) / 256.0f;
        }
        if (block.minY < 0.0 || block.maxY > 1.0) {
            d4 = ((float)n2 + 0.0f) / 256.0f;
            d5 = ((float)n2 + 15.99f) / 256.0f;
        }
        d = d3;
        double d6 = d2;
        double d7 = d4;
        double d8 = d5;
        if (this.eastFaceRotation == 1) {
            d2 = ((double)n + block.minY * 16.0) / 256.0;
            d4 = ((double)(n2 + 16) - block.maxZ * 16.0) / 256.0;
            d3 = ((double)n + block.maxY * 16.0) / 256.0;
            d5 = ((double)(n2 + 16) - block.minZ * 16.0) / 256.0;
            d = d3;
            d6 = d2;
            d7 = d4;
            d8 = d5;
            d = d2;
            d6 = d3;
            d4 = d5;
            d5 = d7;
        } else if (this.eastFaceRotation == 2) {
            d2 = ((double)(n + 16) - block.maxY * 16.0) / 256.0;
            d4 = ((double)n2 + block.minZ * 16.0) / 256.0;
            d3 = ((double)(n + 16) - block.minY * 16.0) / 256.0;
            d5 = ((double)n2 + block.maxZ * 16.0) / 256.0;
            d = d3;
            d6 = d2;
            d7 = d4;
            d8 = d5;
            d2 = d;
            d3 = d6;
            d7 = d5;
            d8 = d4;
        } else if (this.eastFaceRotation == 3) {
            d2 = ((double)(n + 16) - block.minZ * 16.0) / 256.0;
            d3 = ((double)(n + 16) - block.maxZ * 16.0 - 0.01) / 256.0;
            d4 = ((double)n2 + block.maxY * 16.0) / 256.0;
            d5 = ((double)n2 + block.minY * 16.0 - 0.01) / 256.0;
            d = d3;
            d6 = d2;
            d7 = d4;
            d8 = d5;
        }
        double d9 = x + block.minX;
        double d10 = y + block.minY;
        double d11 = y + block.maxY;
        double d12 = z + block.minZ;
        double d13 = z + block.maxZ;
        if (this.enableAO) {
            tessellator.setColorOpaque_F(this.colorRedTopLeft, this.colorGreenTopLeft, this.colorBlueTopLeft);
            tessellator.addVertexWithUV(d9, d11, d13, d, d7);
            tessellator.setColorOpaque_F(this.colorRedBottomLeft, this.colorGreenBottomLeft, this.colorBlueBottomLeft);
            tessellator.addVertexWithUV(d9, d11, d12, d2, d4);
            tessellator.setColorOpaque_F(this.colorRedBottomRight, this.colorGreenBottomRight, this.colorBlueBottomRight);
            tessellator.addVertexWithUV(d9, d10, d12, d6, d8);
            tessellator.setColorOpaque_F(this.colorRedTopRight, this.colorGreenTopRight, this.colorBlueTopRight);
            tessellator.addVertexWithUV(d9, d10, d13, d3, d5);
        } else {
            tessellator.addVertexWithUV(d9, d11, d13, d, d7);
            tessellator.addVertexWithUV(d9, d11, d12, d2, d4);
            tessellator.addVertexWithUV(d9, d10, d12, d6, d8);
            tessellator.addVertexWithUV(d9, d10, d13, d3, d5);
        }
    }

    public void renderSouthFace(Block block, double x, double y, double z, int sprite) {
        double d;
        Tessellator tessellator = Tessellator.instance;
        if (this.overrideBlockTexture >= 0) {
            sprite = this.overrideBlockTexture;
        }
        int n = (sprite & 0xF) << 4;
        int n2 = sprite & 0xF0;
        double d2 = ((double)n + block.minZ * 16.0) / 256.0;
        double d3 = ((double)n + block.maxZ * 16.0 - 0.01) / 256.0;
        double d4 = ((double)(n2 + 16) - block.maxY * 16.0) / 256.0;
        double d5 = ((double)(n2 + 16) - block.minY * 16.0 - 0.01) / 256.0;
        if (this.flipTexture) {
            d = d2;
            d2 = d3;
            d3 = d;
        }
        if (block.minZ < 0.0 || block.maxZ > 1.0) {
            d2 = ((float)n + 0.0f) / 256.0f;
            d3 = ((float)n + 15.99f) / 256.0f;
        }
        if (block.minY < 0.0 || block.maxY > 1.0) {
            d4 = ((float)n2 + 0.0f) / 256.0f;
            d5 = ((float)n2 + 15.99f) / 256.0f;
        }
        d = d3;
        double d6 = d2;
        double d7 = d4;
        double d8 = d5;
        if (this.westFaceRotation == 2) {
            d2 = ((double)n + block.minY * 16.0) / 256.0;
            d4 = ((double)(n2 + 16) - block.minZ * 16.0) / 256.0;
            d3 = ((double)n + block.maxY * 16.0) / 256.0;
            d5 = ((double)(n2 + 16) - block.maxZ * 16.0) / 256.0;
            d = d3;
            d6 = d2;
            d7 = d4;
            d8 = d5;
            d = d2;
            d6 = d3;
            d4 = d5;
            d5 = d7;
        } else if (this.westFaceRotation == 1) {
            d2 = ((double)(n + 16) - block.maxY * 16.0) / 256.0;
            d4 = ((double)n2 + block.maxZ * 16.0) / 256.0;
            d3 = ((double)(n + 16) - block.minY * 16.0) / 256.0;
            d5 = ((double)n2 + block.minZ * 16.0) / 256.0;
            d = d3;
            d6 = d2;
            d7 = d4;
            d8 = d5;
            d2 = d;
            d3 = d6;
            d7 = d5;
            d8 = d4;
        } else if (this.westFaceRotation == 3) {
            d2 = ((double)(n + 16) - block.minZ * 16.0) / 256.0;
            d3 = ((double)(n + 16) - block.maxZ * 16.0 - 0.01) / 256.0;
            d4 = ((double)n2 + block.maxY * 16.0) / 256.0;
            d5 = ((double)n2 + block.minY * 16.0 - 0.01) / 256.0;
            d = d3;
            d6 = d2;
            d7 = d4;
            d8 = d5;
        }
        double d9 = x + block.maxX;
        double d10 = y + block.minY;
        double d11 = y + block.maxY;
        double d12 = z + block.minZ;
        double d13 = z + block.maxZ;
        if (this.enableAO) {
            tessellator.setColorOpaque_F(this.colorRedTopLeft, this.colorGreenTopLeft, this.colorBlueTopLeft);
            tessellator.addVertexWithUV(d9, d10, d13, d6, d8);
            tessellator.setColorOpaque_F(this.colorRedBottomLeft, this.colorGreenBottomLeft, this.colorBlueBottomLeft);
            tessellator.addVertexWithUV(d9, d10, d12, d3, d5);
            tessellator.setColorOpaque_F(this.colorRedBottomRight, this.colorGreenBottomRight, this.colorBlueBottomRight);
            tessellator.addVertexWithUV(d9, d11, d12, d, d7);
            tessellator.setColorOpaque_F(this.colorRedTopRight, this.colorGreenTopRight, this.colorBlueTopRight);
            tessellator.addVertexWithUV(d9, d11, d13, d2, d4);
        } else {
            tessellator.addVertexWithUV(d9, d10, d13, d6, d8);
            tessellator.addVertexWithUV(d9, d10, d12, d3, d5);
            tessellator.addVertexWithUV(d9, d11, d12, d, d7);
            tessellator.addVertexWithUV(d9, d11, d13, d2, d4);
        }
    }

    public void renderBlockOnInventory(Block block, int metadata, float brightness) {
        float f;
        float f2;
        int n;
        Tessellator tessellator = Tessellator.instance;
        if (this.updateColor) {
            n = block.getRenderColor(metadata);
            f2 = (float)(n >> 16 & 0xFF) / 255.0f;
            f = (float)(n >> 8 & 0xFF) / 255.0f;
            float f3 = (float)(n & 0xFF) / 255.0f;
            GL11.glColor4f(f2 * brightness, f * brightness, f3 * brightness, 1.0f);
        }
        if ((n = block.getRenderType()) == 0 || n == 16) {
            if (n == 16) {
                metadata = 1;
            }
            block.setBlockBoundsForItemRender();
            GL11.glTranslatef(-0.5f, -0.5f, -0.5f);
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0f, -1.0f, 0.0f);
            this.renderBottomFace(block, 0.0, 0.0, 0.0, block.getBlockTextureFromSideAndMetadata(0, metadata));
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0f, 1.0f, 0.0f);
            this.renderTopFace(block, 0.0, 0.0, 0.0, block.getBlockTextureFromSideAndMetadata(1, metadata));
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0f, 0.0f, -1.0f);
            this.renderEastFace(block, 0.0, 0.0, 0.0, block.getBlockTextureFromSideAndMetadata(2, metadata));
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0f, 0.0f, 1.0f);
            this.renderWestFace(block, 0.0, 0.0, 0.0, block.getBlockTextureFromSideAndMetadata(3, metadata));
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(-1.0f, 0.0f, 0.0f);
            this.renderNorthFace(block, 0.0, 0.0, 0.0, block.getBlockTextureFromSideAndMetadata(4, metadata));
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(1.0f, 0.0f, 0.0f);
            this.renderSouthFace(block, 0.0, 0.0, 0.0, block.getBlockTextureFromSideAndMetadata(5, metadata));
            tessellator.draw();
            GL11.glTranslatef(0.5f, 0.5f, 0.5f);
        } else if (n == 1) {
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0f, -1.0f, 0.0f);
            this.renderCrossedSquares(block, metadata, -0.5, -0.5, -0.5);
            tessellator.draw();
        } else if (n == 13) {
            block.setBlockBoundsForItemRender();
            GL11.glTranslatef(-0.5f, -0.5f, -0.5f);
            f2 = 0.0625f;
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0f, -1.0f, 0.0f);
            this.renderBottomFace(block, 0.0, 0.0, 0.0, block.getBlockTextureFromSide(0));
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0f, 1.0f, 0.0f);
            this.renderTopFace(block, 0.0, 0.0, 0.0, block.getBlockTextureFromSide(1));
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0f, 0.0f, -1.0f);
            tessellator.setTranslationF(0.0f, 0.0f, f2);
            this.renderEastFace(block, 0.0, 0.0, 0.0, block.getBlockTextureFromSide(2));
            tessellator.setTranslationF(0.0f, 0.0f, -f2);
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0f, 0.0f, 1.0f);
            tessellator.setTranslationF(0.0f, 0.0f, -f2);
            this.renderWestFace(block, 0.0, 0.0, 0.0, block.getBlockTextureFromSide(3));
            tessellator.setTranslationF(0.0f, 0.0f, f2);
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(-1.0f, 0.0f, 0.0f);
            tessellator.setTranslationF(f2, 0.0f, 0.0f);
            this.renderNorthFace(block, 0.0, 0.0, 0.0, block.getBlockTextureFromSide(4));
            tessellator.setTranslationF(-f2, 0.0f, 0.0f);
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(1.0f, 0.0f, 0.0f);
            tessellator.setTranslationF(-f2, 0.0f, 0.0f);
            this.renderSouthFace(block, 0.0, 0.0, 0.0, block.getBlockTextureFromSide(5));
            tessellator.setTranslationF(f2, 0.0f, 0.0f);
            tessellator.draw();
            GL11.glTranslatef(0.5f, 0.5f, 0.5f);
        } else if (n == 6) {
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0f, -1.0f, 0.0f);
            this.tesselatePlant(block, metadata, -0.5, -0.5, -0.5);
            tessellator.draw();
        } else if (n == 2) {
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0f, -1.0f, 0.0f);
            this.renderTorchAtAngle(block, -0.5, -0.5, -0.5, 0.0, 0.0);
            tessellator.draw();
        } else if (n == 10) {
            for (int i = 0; i < 2; ++i) {
                if (i == 0) {
                    block.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.5f);
                }
                if (i == 1) {
                    block.setBlockBounds(0.0f, 0.0f, 0.5f, 1.0f, 0.5f, 1.0f);
                }
                GL11.glTranslatef(-0.5f, -0.5f, -0.5f);
                tessellator.startDrawingQuads();
                tessellator.setNormal(0.0f, -1.0f, 0.0f);
                this.renderBottomFace(block, 0.0, 0.0, 0.0, block.getBlockTextureFromSide(0));
                tessellator.draw();
                tessellator.startDrawingQuads();
                tessellator.setNormal(0.0f, 1.0f, 0.0f);
                this.renderTopFace(block, 0.0, 0.0, 0.0, block.getBlockTextureFromSide(1));
                tessellator.draw();
                tessellator.startDrawingQuads();
                tessellator.setNormal(0.0f, 0.0f, -1.0f);
                this.renderEastFace(block, 0.0, 0.0, 0.0, block.getBlockTextureFromSide(2));
                tessellator.draw();
                tessellator.startDrawingQuads();
                tessellator.setNormal(0.0f, 0.0f, 1.0f);
                this.renderWestFace(block, 0.0, 0.0, 0.0, block.getBlockTextureFromSide(3));
                tessellator.draw();
                tessellator.startDrawingQuads();
                tessellator.setNormal(-1.0f, 0.0f, 0.0f);
                this.renderNorthFace(block, 0.0, 0.0, 0.0, block.getBlockTextureFromSide(4));
                tessellator.draw();
                tessellator.startDrawingQuads();
                tessellator.setNormal(1.0f, 0.0f, 0.0f);
                this.renderSouthFace(block, 0.0, 0.0, 0.0, block.getBlockTextureFromSide(5));
                tessellator.draw();
                GL11.glTranslatef(0.5f, 0.5f, 0.5f);
            }
        } else if (n == 11) {
            for (int i = 0; i < 4; ++i) {
                f = 0.125f;
                if (i == 0) {
                    block.setBlockBounds(0.5f - f, 0.0f, 0.0f, 0.5f + f, 1.0f, f * 2.0f);
                }
                if (i == 1) {
                    block.setBlockBounds(0.5f - f, 0.0f, 1.0f - f * 2.0f, 0.5f + f, 1.0f, 1.0f);
                }
                f = 0.0625f;
                if (i == 2) {
                    block.setBlockBounds(0.5f - f, 1.0f - f * 3.0f, -f * 2.0f, 0.5f + f, 1.0f - f, 1.0f + f * 2.0f);
                }
                if (i == 3) {
                    block.setBlockBounds(0.5f - f, 0.5f - f * 3.0f, -f * 2.0f, 0.5f + f, 0.5f - f, 1.0f + f * 2.0f);
                }
                GL11.glTranslatef(-0.5f, -0.5f, -0.5f);
                tessellator.startDrawingQuads();
                tessellator.setNormal(0.0f, -1.0f, 0.0f);
                this.renderBottomFace(block, 0.0, 0.0, 0.0, block.getBlockTextureFromSide(0));
                tessellator.draw();
                tessellator.startDrawingQuads();
                tessellator.setNormal(0.0f, 1.0f, 0.0f);
                this.renderTopFace(block, 0.0, 0.0, 0.0, block.getBlockTextureFromSide(1));
                tessellator.draw();
                tessellator.startDrawingQuads();
                tessellator.setNormal(0.0f, 0.0f, -1.0f);
                this.renderEastFace(block, 0.0, 0.0, 0.0, block.getBlockTextureFromSide(2));
                tessellator.draw();
                tessellator.startDrawingQuads();
                tessellator.setNormal(0.0f, 0.0f, 1.0f);
                this.renderWestFace(block, 0.0, 0.0, 0.0, block.getBlockTextureFromSide(3));
                tessellator.draw();
                tessellator.startDrawingQuads();
                tessellator.setNormal(-1.0f, 0.0f, 0.0f);
                this.renderNorthFace(block, 0.0, 0.0, 0.0, block.getBlockTextureFromSide(4));
                tessellator.draw();
                tessellator.startDrawingQuads();
                tessellator.setNormal(1.0f, 0.0f, 0.0f);
                this.renderSouthFace(block, 0.0, 0.0, 0.0, block.getBlockTextureFromSide(5));
                tessellator.draw();
                GL11.glTranslatef(0.5f, 0.5f, 0.5f);
            }
            block.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
        }
    }

    public static boolean renderItemIn3d(int type) {
        if (type == 0) {
            return true;
        }
        if (type == 13) {
            return true;
        }
        if (type == 10) {
            return true;
        }
        if (type == 11) {
            return true;
        }
        return type == 16;
    }
}

