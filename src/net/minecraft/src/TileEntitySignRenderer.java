package net.minecraft.src;

import org.lwjgl.opengl.GL11;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class TileEntitySignRenderer
extends TileEntitySpecialRenderer<TileEntitySign> {
    private SignModel signModel = new SignModel();

    @Override
    public void renderTileEntityAt(TileEntitySign tileEntitySign, double d, double d2, double d3, float f) {
        float f2;
        Block block = tileEntitySign.getBlockType();
        GL11.glPushMatrix();
        float f3 = 0.6666667f;
        if (block == Block.signPost) {
            GL11.glTranslatef((float)d + 0.5f, (float)d2 + 0.75f * f3, (float)d3 + 0.5f);
            float f4 = (float)(tileEntitySign.getBlockMetadata() * 360) / 16.0f;
            GL11.glRotatef(-f4, 0.0f, 1.0f, 0.0f);
            this.signModel.signStick.showModel = true;
        } else {
            int n = tileEntitySign.getBlockMetadata();
            f2 = 0.0f;
            if (n == 2) {
                f2 = 180.0f;
            }
            if (n == 4) {
                f2 = 90.0f;
            }
            if (n == 5) {
                f2 = -90.0f;
            }
            GL11.glTranslatef((float)d + 0.5f, (float)d2 + 0.75f * f3, (float)d3 + 0.5f);
            GL11.glRotatef(-f2, 0.0f, 1.0f, 0.0f);
            GL11.glTranslatef(0.0f, -0.3125f, -0.4375f);
            this.signModel.signStick.showModel = false;
        }
        this.bindTextureByName("/item/sign.png");
        GL11.glPushMatrix();
        GL11.glScalef(f3, -f3, -f3);
        this.signModel.render();
        GL11.glPopMatrix();
        FontRenderer fontRenderer = this.getFontRenderer();
        f2 = 0.016666668f * f3;
        GL11.glTranslatef(0.0f, 0.5f * f3, 0.07f * f3);
        GL11.glScalef(f2, -f2, f2);
        GL11.glNormal3f(0.0f, 0.0f, -1.0f * f2);
        GL11.glDepthMask(false);
        int n = 0;
        for (int i = 0; i < tileEntitySign.signText.length; ++i) {
            String string = tileEntitySign.signText[i];
            if (i == tileEntitySign.lineBeingEdited) {
                string = "> " + string + " <";
                fontRenderer.drawString(string, -fontRenderer.getStringWidth(string) / 2, i * 10 - tileEntitySign.signText.length * 5, n);
                continue;
            }
            fontRenderer.drawString(string, -fontRenderer.getStringWidth(string) / 2, i * 10 - tileEntitySign.signText.length * 5, n);
        }
        GL11.glDepthMask(true);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glPopMatrix();
    }
}

