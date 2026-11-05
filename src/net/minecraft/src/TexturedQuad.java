package net.minecraft.src;


public class TexturedQuad {
    public PositionTextureVertex[] vertexPositions;
    public int nVertices = 0;
    private boolean invertNormal = false;

    public TexturedQuad(PositionTextureVertex[] vertices) {
        this.vertexPositions = vertices;
        this.nVertices = vertices.length;
    }

    public TexturedQuad(PositionTextureVertex[] vertices, int u1, int v1, int u2, int v2) {
        this(vertices);
        float f = 0.0015625f;
        float f2 = 0.003125f;
        vertices[0] = vertices[0].setTexturePosition((float)u2 / 64.0f - f, (float)v1 / 32.0f + f2);
        vertices[1] = vertices[1].setTexturePosition((float)u1 / 64.0f + f, (float)v1 / 32.0f + f2);
        vertices[2] = vertices[2].setTexturePosition((float)u1 / 64.0f + f, (float)v2 / 32.0f - f2);
        vertices[3] = vertices[3].setTexturePosition((float)u2 / 64.0f - f, (float)v2 / 32.0f - f2);
    }

    public void flipFace() {
        PositionTextureVertex[] positionTextureVertexArray = new PositionTextureVertex[this.vertexPositions.length];
        for (int i = 0; i < this.vertexPositions.length; ++i) {
            positionTextureVertexArray[i] = this.vertexPositions[this.vertexPositions.length - i - 1];
        }
        this.vertexPositions = positionTextureVertexArray;
    }

    public void draw(Tessellator tesselator, float scale) {
        Vec3D vec3D = this.vertexPositions[1].vector3D.subtract(this.vertexPositions[0].vector3D);
        Vec3D vec3D2 = this.vertexPositions[1].vector3D.subtract(this.vertexPositions[2].vector3D);
        Vec3D vec3D3 = vec3D2.crossProduct(vec3D).normalize();
        tesselator.startDrawingQuads();
        if (this.invertNormal) {
            tesselator.setNormal(-((float)vec3D3.xCoord), -((float)vec3D3.yCoord), -((float)vec3D3.zCoord));
        } else {
            tesselator.setNormal((float)vec3D3.xCoord, (float)vec3D3.yCoord, (float)vec3D3.zCoord);
        }
        for (int i = 0; i < 4; ++i) {
            PositionTextureVertex positionTextureVertex = this.vertexPositions[i];
            tesselator.addVertexWithUV((float)positionTextureVertex.vector3D.xCoord * scale, (float)positionTextureVertex.vector3D.yCoord * scale, (float)positionTextureVertex.vector3D.zCoord * scale, positionTextureVertex.texturePositionX, positionTextureVertex.texturePositionY);
        }
        tesselator.draw();
    }
}

