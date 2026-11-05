package net.minecraft.src;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.lwjgl.opengl.ARBVertexBufferObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import java.nio.Buffer;

public class Tessellator {
    private static boolean convertQuadsToTriangles = true;
    private static boolean tryVBO = false;
    private ByteBuffer byteBuffer;
    private IntBuffer intBuffer;
    private FloatBuffer floatBuffer;
    private int[] rawBuffer;
    private int vertexCount = 0;
    private double textureU;
    private double textureV;
    private int color;
    private boolean hasColor = false;
    private boolean hasTexture = false;
    private boolean hasNormals = false;
    private int rawBufferIndex = 0;
    private int addedVertices = 0;
    private boolean isColorDisabled = false;
    private int drawMode;
    private double xOffset;
    private double yOffset;
    private double zOffset;
    private int normal;
    public static final Tessellator instance = new Tessellator(0x200000);
    private boolean isDrawing = false;
    private boolean useVBO = false;
    private IntBuffer vertexBuffers;
    private int vboIndex = 0;
    private int vboCount = 10;
    private int bufferSize;

    private Tessellator(int size) {
        this.bufferSize = size;
        this.byteBuffer = GLAllocation.createDirectByteBuffer(size * 4);
        this.intBuffer = this.byteBuffer.asIntBuffer();
        this.floatBuffer = this.byteBuffer.asFloatBuffer();
        this.rawBuffer = new int[size];
        this.useVBO = tryVBO && GLContext.getCapabilities().GL_ARB_vertex_buffer_object;
        if (this.useVBO) {
            this.vertexBuffers = GLAllocation.createDirectIntBuffer(this.vboCount);
            ARBVertexBufferObject.glGenBuffersARB(this.vertexBuffers);
        }
    }

    public void draw() {
        if (!this.isDrawing) {
            throw new IllegalStateException("Not tesselating!");
        }
        this.isDrawing = false;
        if (this.vertexCount > 0) {
            this.intBuffer.clear();
            this.intBuffer.put(this.rawBuffer, 0, this.rawBufferIndex);
            this.byteBuffer.position(0);
            this.byteBuffer.limit(this.rawBufferIndex * 4);
            if (this.useVBO) {
                this.vboIndex = (this.vboIndex + 1) % this.vboCount;
                ARBVertexBufferObject.glBindBufferARB(34962, this.vertexBuffers.get(this.vboIndex));
                ARBVertexBufferObject.glBufferDataARB(34962, this.byteBuffer, 35040);
            }
            if (this.hasTexture) {
                if (this.useVBO) {
                    GL11.glTexCoordPointer(2, 5126, 32, 12L);
                } else {
                    this.floatBuffer.position(3);
                    GL11.glTexCoordPointer(2, 32, this.floatBuffer);
                }
                GL11.glEnableClientState(32888);
            }
            if (this.hasColor) {
                if (this.useVBO) {
                    GL11.glColorPointer(4, 5121, 32, 20L);
                } else {
                    this.byteBuffer.position(20);
                    GL11.glColorPointer(4, true, 32, this.byteBuffer);
                }
                GL11.glEnableClientState(32886);
            }
            if (this.hasNormals) {
                if (this.useVBO) {
                    GL11.glNormalPointer(5120, 32, 24L);
                } else {
                    this.byteBuffer.position(24);
                    GL11.glNormalPointer(32, this.byteBuffer);
                }
                GL11.glEnableClientState(32885);
            }
            if (this.useVBO) {
                GL11.glVertexPointer(3, 5126, 32, 0L);
            } else {
                this.floatBuffer.position(0);
                GL11.glVertexPointer(3, 32, this.floatBuffer);
            }
            GL11.glEnableClientState(32884);
            if (this.drawMode == 7 && convertQuadsToTriangles) {
                GL11.glDrawArrays(4, 0, this.vertexCount);
            } else {
                GL11.glDrawArrays(this.drawMode, 0, this.vertexCount);
            }
            GL11.glDisableClientState(32884);
            if (this.hasTexture) {
                GL11.glDisableClientState(32888);
            }
            if (this.hasColor) {
                GL11.glDisableClientState(32886);
            }
            if (this.hasNormals) {
                GL11.glDisableClientState(32885);
            }
        }
        this.reset();
    }

    private void reset() {
        this.vertexCount = 0;
        this.byteBuffer.clear();
        this.rawBufferIndex = 0;
        this.addedVertices = 0;
    }

    public void startDrawingQuads() {
        this.startDrawing(7);
    }

    public void startDrawing(int drawMode) {
        if (this.isDrawing) {
            throw new IllegalStateException("Already tesselating!");
        }
        this.isDrawing = true;
        this.reset();
        this.drawMode = drawMode;
        this.hasNormals = false;
        this.hasColor = false;
        this.hasTexture = false;
        this.isColorDisabled = false;
    }

    public void setTextureUV(double u, double v) {
        this.hasTexture = true;
        this.textureU = u;
        this.textureV = v;
    }

    public void setColorOpaque_F(float r, float g, float b) {
        this.setColorOpaque((int)(r * 255.0f), (int)(g * 255.0f), (int)(b * 255.0f));
    }

    public void setColorRGBA_F(float r, float g, float b, float a) {
        this.setColorRGBA((int)(r * 255.0f), (int)(g * 255.0f), (int)(b * 255.0f), (int)(a * 255.0f));
    }

    public void setColorOpaque(int r, int g, int b) {
        this.setColorRGBA(r, g, b, 255);
    }

    public void setColorRGBA(int r, int g, int b, int a) {
        if (this.isColorDisabled) {
            return;
        }
        if (r > 255) {
            r = 255;
        }
        if (g > 255) {
            g = 255;
        }
        if (b > 255) {
            b = 255;
        }
        if (a > 255) {
            a = 255;
        }
        if (r < 0) {
            r = 0;
        }
        if (g < 0) {
            g = 0;
        }
        if (b < 0) {
            b = 0;
        }
        if (a < 0) {
            a = 0;
        }
        this.hasColor = true;
        if (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN) {
            this.color = (a << 24 | b << 16 | g << 8 | r);
        } else {
            this.color = (r << 24 | g << 16 | b << 8 | a);
        }
    }

    public void addVertexWithUV(double x, double y, double z, double u, double v) {
        this.setTextureUV(u, v);
        this.addVertex(x, y, z);
    }

    public void addVertex(double x, double y, double z) {
        ++this.addedVertices;
        if (this.drawMode == 7 && convertQuadsToTriangles && this.addedVertices % 4 == 0) {
            for (int i = 0; i < 2; ++i) {
                int n = 8 * (3 - i);
                if (this.hasTexture) {
                    this.rawBuffer[this.rawBufferIndex + 3] = this.rawBuffer[this.rawBufferIndex - n + 3];
                    this.rawBuffer[this.rawBufferIndex + 4] = this.rawBuffer[this.rawBufferIndex - n + 4];
                }
                if (this.hasColor) {
                    this.rawBuffer[this.rawBufferIndex + 5] = this.rawBuffer[this.rawBufferIndex - n + 5];
                }
                this.rawBuffer[this.rawBufferIndex + 0] = this.rawBuffer[this.rawBufferIndex - n + 0];
                this.rawBuffer[this.rawBufferIndex + 1] = this.rawBuffer[this.rawBufferIndex - n + 1];
                this.rawBuffer[this.rawBufferIndex + 2] = this.rawBuffer[this.rawBufferIndex - n + 2];
                ++this.vertexCount;
                this.rawBufferIndex += 8;
            }
        }
        if (this.hasTexture) {
            this.rawBuffer[this.rawBufferIndex + 3] = Float.floatToRawIntBits((float)this.textureU);
            this.rawBuffer[this.rawBufferIndex + 4] = Float.floatToRawIntBits((float)this.textureV);
        }
        if (this.hasColor) {
            this.rawBuffer[this.rawBufferIndex + 5] = this.color;
        }
        if (this.hasNormals) {
            this.rawBuffer[this.rawBufferIndex + 6] = this.normal;
        }
        this.rawBuffer[this.rawBufferIndex + 0] = Float.floatToRawIntBits((float)(x + this.xOffset));
        this.rawBuffer[this.rawBufferIndex + 1] = Float.floatToRawIntBits((float)(y + this.yOffset));
        this.rawBuffer[this.rawBufferIndex + 2] = Float.floatToRawIntBits((float)(z + this.zOffset));
        this.rawBufferIndex += 8;
        ++this.vertexCount;
        if (this.vertexCount % 4 == 0 && this.rawBufferIndex >= this.bufferSize - 32) {
            this.draw();
            this.isDrawing = true;
        }
    }

    public void setColorOpaque_I(int rgb) {
        int n = rgb >> 16 & 0xFF;
        int n2 = rgb >> 8 & 0xFF;
        int n3 = rgb & 0xFF;
        this.setColorOpaque(n, n2, n3);
    }

    public void setColorRGBA_I(int rgb, int a) {
        int n = rgb >> 16 & 0xFF;
        int n2 = rgb >> 8 & 0xFF;
        int n3 = rgb & 0xFF;
        this.setColorRGBA(n, n2, n3, a);
    }

    public void disableColor() {
        this.isColorDisabled = true;
    }

    public void setNormal(float x, float y, float z) {
        if (!this.isDrawing) {
            System.out.println("But..");
        }
        this.hasNormals = true;
        byte by = (byte)(x * 128.0f);
        byte by2 = (byte)(y * 127.0f);
        byte by3 = (byte)(z * 127.0f);
        this.normal = by | by2 << 8 | by3 << 16;
    }

    public void setTranslationD(double offsetX, double offsetY, double offsetZ) {
        this.xOffset = offsetX;
        this.yOffset = offsetY;
        this.zOffset = offsetZ;
    }

    public void setTranslationF(float offsetX, float offsetY, float offsetZ) {
        this.xOffset += (double)offsetX;
        this.yOffset += (double)offsetY;
        this.zOffset += (double)offsetZ;
    }
}

