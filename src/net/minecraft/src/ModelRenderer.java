package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class ModelRenderer {
    private PositionTextureVertex[] corners;
    private TexturedQuad[] faces;
    private int textureOffsetX;
    private int textureOffsetY;
    public float rotationPointX;
    public float rotationPointY;
    public float rotationPointZ;
    public float rotateAngleX;
    public float rotateAngleY;
    public float rotateAngleZ;
    private boolean compiled = false;
    private int displayList = 0;
    public boolean mirror = false;
    public boolean showModel = true;
    public boolean invisible = false;

    public ModelRenderer(int u, int v) {
        this.textureOffsetX = u;
        this.textureOffsetY = v;
    }

    public void addBox(float x, float y, float z, int sizeX, int sizeY, int sizeZ) {
        this.addBox(x, y, z, sizeX, sizeY, sizeZ, 0.0f);
    }

    public void addBox(float x, float y, float z, int sizeX, int sizeY, int sizeZ, float increase) {
        this.corners = new PositionTextureVertex[8];
        this.faces = new TexturedQuad[6];
        float f = x + (float)sizeX;
        float f2 = y + (float)sizeY;
        float f3 = z + (float)sizeZ;
        x -= increase;
        y -= increase;
        z -= increase;
        f += increase;
        f2 += increase;
        f3 += increase;
        if (this.mirror) {
            float f4 = f;
            f = x;
            x = f4;
        }
        PositionTextureVertex positionTextureVertex = new PositionTextureVertex(x, y, z, 0.0f, 0.0f);
        PositionTextureVertex positionTextureVertex2 = new PositionTextureVertex(f, y, z, 0.0f, 8.0f);
        PositionTextureVertex positionTextureVertex3 = new PositionTextureVertex(f, f2, z, 8.0f, 8.0f);
        PositionTextureVertex positionTextureVertex4 = new PositionTextureVertex(x, f2, z, 8.0f, 0.0f);
        PositionTextureVertex positionTextureVertex5 = new PositionTextureVertex(x, y, f3, 0.0f, 0.0f);
        PositionTextureVertex positionTextureVertex6 = new PositionTextureVertex(f, y, f3, 0.0f, 8.0f);
        PositionTextureVertex positionTextureVertex7 = new PositionTextureVertex(f, f2, f3, 8.0f, 8.0f);
        PositionTextureVertex positionTextureVertex8 = new PositionTextureVertex(x, f2, f3, 8.0f, 0.0f);
        this.corners[0] = positionTextureVertex;
        this.corners[1] = positionTextureVertex2;
        this.corners[2] = positionTextureVertex3;
        this.corners[3] = positionTextureVertex4;
        this.corners[4] = positionTextureVertex5;
        this.corners[5] = positionTextureVertex6;
        this.corners[6] = positionTextureVertex7;
        this.corners[7] = positionTextureVertex8;
        this.faces[0] = new TexturedQuad(new PositionTextureVertex[]{positionTextureVertex6, positionTextureVertex2, positionTextureVertex3, positionTextureVertex7}, this.textureOffsetX + sizeZ + sizeX, this.textureOffsetY + sizeZ, this.textureOffsetX + sizeZ + sizeX + sizeZ, this.textureOffsetY + sizeZ + sizeY);
        this.faces[1] = new TexturedQuad(new PositionTextureVertex[]{positionTextureVertex, positionTextureVertex5, positionTextureVertex8, positionTextureVertex4}, this.textureOffsetX + 0, this.textureOffsetY + sizeZ, this.textureOffsetX + sizeZ, this.textureOffsetY + sizeZ + sizeY);
        this.faces[2] = new TexturedQuad(new PositionTextureVertex[]{positionTextureVertex6, positionTextureVertex5, positionTextureVertex, positionTextureVertex2}, this.textureOffsetX + sizeZ, this.textureOffsetY + 0, this.textureOffsetX + sizeZ + sizeX, this.textureOffsetY + sizeZ);
        this.faces[3] = new TexturedQuad(new PositionTextureVertex[]{positionTextureVertex3, positionTextureVertex4, positionTextureVertex8, positionTextureVertex7}, this.textureOffsetX + sizeZ + sizeX, this.textureOffsetY + 0, this.textureOffsetX + sizeZ + sizeX + sizeX, this.textureOffsetY + sizeZ);
        this.faces[4] = new TexturedQuad(new PositionTextureVertex[]{positionTextureVertex2, positionTextureVertex, positionTextureVertex4, positionTextureVertex3}, this.textureOffsetX + sizeZ, this.textureOffsetY + sizeZ, this.textureOffsetX + sizeZ + sizeX, this.textureOffsetY + sizeZ + sizeY);
        this.faces[5] = new TexturedQuad(new PositionTextureVertex[]{positionTextureVertex5, positionTextureVertex6, positionTextureVertex7, positionTextureVertex8}, this.textureOffsetX + sizeZ + sizeX + sizeZ, this.textureOffsetY + sizeZ, this.textureOffsetX + sizeZ + sizeX + sizeZ + sizeX, this.textureOffsetY + sizeZ + sizeY);
        if (this.mirror) {
            for (int i = 0; i < this.faces.length; ++i) {
                this.faces[i].flipFace();
            }
        }
    }

    public void setRotationPoint(float x, float y, float z) {
        this.rotationPointX = x;
        this.rotationPointY = y;
        this.rotationPointZ = z;
    }

    public void render(float scale) {
        if (this.invisible) {
            return;
        }
        if (!this.showModel) {
            return;
        }
        if (!this.compiled) {
            this.compileDisplayList(scale);
        }
        if (this.rotateAngleX != 0.0f || this.rotateAngleY != 0.0f || this.rotateAngleZ != 0.0f) {
            GL11.glPushMatrix();
            GL11.glTranslatef(this.rotationPointX * scale, this.rotationPointY * scale, this.rotationPointZ * scale);
            if (this.rotateAngleZ != 0.0f) {
                GL11.glRotatef(this.rotateAngleZ * 57.295776f, 0.0f, 0.0f, 1.0f);
            }
            if (this.rotateAngleY != 0.0f) {
                GL11.glRotatef(this.rotateAngleY * 57.295776f, 0.0f, 1.0f, 0.0f);
            }
            if (this.rotateAngleX != 0.0f) {
                GL11.glRotatef(this.rotateAngleX * 57.295776f, 1.0f, 0.0f, 0.0f);
            }
            GL11.glCallList(this.displayList);
            GL11.glPopMatrix();
        } else if (this.rotationPointX != 0.0f || this.rotationPointY != 0.0f || this.rotationPointZ != 0.0f) {
            GL11.glTranslatef(this.rotationPointX * scale, this.rotationPointY * scale, this.rotationPointZ * scale);
            GL11.glCallList(this.displayList);
            GL11.glTranslatef(-this.rotationPointX * scale, -this.rotationPointY * scale, -this.rotationPointZ * scale);
        } else {
            GL11.glCallList(this.displayList);
        }
    }

    public void renderWithRotation(float scale) {
        if (this.invisible) {
            return;
        }
        if (!this.showModel) {
            return;
        }
        if (!this.compiled) {
            this.compileDisplayList(scale);
        }
        GL11.glPushMatrix();
        GL11.glTranslatef(this.rotationPointX * scale, this.rotationPointY * scale, this.rotationPointZ * scale);
        if (this.rotateAngleY != 0.0f) {
            GL11.glRotatef(this.rotateAngleY * 57.295776f, 0.0f, 1.0f, 0.0f);
        }
        if (this.rotateAngleX != 0.0f) {
            GL11.glRotatef(this.rotateAngleX * 57.295776f, 1.0f, 0.0f, 0.0f);
        }
        if (this.rotateAngleZ != 0.0f) {
            GL11.glRotatef(this.rotateAngleZ * 57.295776f, 0.0f, 0.0f, 1.0f);
        }
        GL11.glCallList(this.displayList);
        GL11.glPopMatrix();
    }

    public void postRender(float scale) {
        if (this.invisible) {
            return;
        }
        if (!this.showModel) {
            return;
        }
        if (!this.compiled) {
            this.compileDisplayList(scale);
        }
        if (this.rotateAngleX != 0.0f || this.rotateAngleY != 0.0f || this.rotateAngleZ != 0.0f) {
            GL11.glTranslatef(this.rotationPointX * scale, this.rotationPointY * scale, this.rotationPointZ * scale);
            if (this.rotateAngleZ != 0.0f) {
                GL11.glRotatef(this.rotateAngleZ * 57.295776f, 0.0f, 0.0f, 1.0f);
            }
            if (this.rotateAngleY != 0.0f) {
                GL11.glRotatef(this.rotateAngleY * 57.295776f, 0.0f, 1.0f, 0.0f);
            }
            if (this.rotateAngleX != 0.0f) {
                GL11.glRotatef(this.rotateAngleX * 57.295776f, 1.0f, 0.0f, 0.0f);
            }
        } else if (this.rotationPointX != 0.0f || this.rotationPointY != 0.0f || this.rotationPointZ != 0.0f) {
            GL11.glTranslatef(this.rotationPointX * scale, this.rotationPointY * scale, this.rotationPointZ * scale);
        }
    }

    private void compileDisplayList(float scale) {
        this.displayList = GLAllocation.generateDisplayLists(1);
        GL11.glNewList(this.displayList, 4864);
        Tessellator tessellator = Tessellator.instance;
        for (int i = 0; i < this.faces.length; ++i) {
            this.faces[i].draw(tessellator, scale);
        }
        GL11.glEndList();
        this.compiled = true;
    }
}

