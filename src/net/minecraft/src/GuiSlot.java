package net.minecraft.src;

import java.util.List;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public abstract class GuiSlot {
    private final Minecraft mc;
    private final int width;
    private final int height;
    protected final int top;
    protected final int bottom;
    private final int right;
    private final int left;
    protected final int posZ;
    private int scrollUpButtonID;
    private int scrollDownButtonID;
    private float initialClickY = -2.0f;
    private float scrollMultiplier;
    private float amountScrolled;
    private int selectedElement = -1;
    private long lastClicked = 0L;
    private boolean renderSelectionHighlight = true;
    private boolean renderHeader;
    private int headerHeight;

    public GuiSlot(Minecraft minecraft, int width, int height, int minY, int maxY, int entryHeight) {
        this.mc = minecraft;
        this.width = width;
        this.height = height;
        this.top = minY;
        this.bottom = maxY;
        this.posZ = entryHeight;
        this.left = 0;
        this.right = width;
    }

    public void setRenderSelectionHighlight(boolean renderSelectionHighlight) {
        this.renderSelectionHighlight = renderSelectionHighlight;
    }

    protected void setHeader(boolean renderHeader, int headerHeight) {
        this.renderHeader = renderHeader;
        this.headerHeight = headerHeight;
        if (!renderHeader) {
            this.headerHeight = 0;
        }
    }

    protected abstract int getSize();

    protected abstract void elementClicked(int var1, boolean var2);

    protected abstract boolean isSelected(int var1);

    protected int getContentHeight() {
        return this.getSize() * this.posZ + this.headerHeight;
    }

    protected abstract void drawBackground();

    protected abstract void drawSlot(int var1, int var2, int var3, int var4, Tessellator var5);

    protected void renderHeader(int x, int y, Tessellator tesselator) {
    }

    protected void headerClicked(int x, int y) {
    }

    protected void renderDecorations(int mouseX, int mouseY) {
    }

    public int getEntryAt(int x, int y) {
        int n = this.width / 2 - 110;
        int n2 = this.width / 2 + 110;
        int n3 = y - this.top - this.headerHeight + (int)this.amountScrolled - 4;
        int n4 = n3 / this.posZ;
        if (x >= n && x <= n2 && n4 >= 0 && n3 >= 0 && n4 < this.getSize()) {
            return n4;
        }
        return -1;
    }

    public void registerScrollButtons(List<GuiButton> buttons, int upButtonId, int downButtonId) {
        this.scrollUpButtonID = upButtonId;
        this.scrollDownButtonID = downButtonId;
    }

    private void bindAmountScrolled() {
        int n = this.getContentHeight() - (this.bottom - this.top - 4);
        if (n < 0) {
            n /= 2;
        }
        if (this.amountScrolled < 0.0f) {
            this.amountScrolled = 0.0f;
        }
        if (this.amountScrolled > (float)n) {
            this.amountScrolled = n;
        }
    }

    public void actionPerformed(GuiButton button) {
        if (!button.enabled) {
            return;
        }
        if (button.id == this.scrollUpButtonID) {
            this.amountScrolled -= (float)(this.posZ * 2 / 3);
            this.initialClickY = -2.0f;
            this.bindAmountScrolled();
        } else if (button.id == this.scrollDownButtonID) {
            this.amountScrolled += (float)(this.posZ * 2 / 3);
            this.initialClickY = -2.0f;
            this.bindAmountScrolled();
        }
    }

    public void drawScreen(int mouseX, int mouseY, float tickDelta) {
        int n;
        int n2;
        int n3;
        int n4;
        int n5;
        int n6;
        this.drawBackground();
        int n7 = this.getSize();
        int n8 = this.width / 2 + 124;
        int n9 = n8 + 6;
        if (Mouse.isButtonDown(0)) {
            if (this.initialClickY == -1.0f) {
                boolean bl = true;
                if (mouseY >= this.top && mouseY <= this.bottom) {
                    int n10 = this.width / 2 - 110;
                    n6 = this.width / 2 + 110;
                    n5 = mouseY - this.top - this.headerHeight + (int)this.amountScrolled - 4;
                    n4 = n5 / this.posZ;
                    if (mouseX >= n10 && mouseX <= n6 && n4 >= 0 && n5 >= 0 && n4 < n7) {
                        n3 = n4 == this.selectedElement && System.currentTimeMillis() - this.lastClicked < 250L ? 1 : 0;
                        this.elementClicked(n4, n3 != 0);
                        this.selectedElement = n4;
                        this.lastClicked = System.currentTimeMillis();
                    } else if (mouseX >= n10 && mouseX <= n6 && n5 < 0) {
                        this.headerClicked(mouseX - n10, mouseY - this.top + (int)this.amountScrolled - 4);
                        bl = false;
                    }
                    if (mouseX >= n8 && mouseX <= n9) {
                        this.scrollMultiplier = -1.0f;
                        n3 = this.getContentHeight() - (this.bottom - this.top - 4);
                        if (n3 < 1) {
                            n3 = 1;
                        }
                        if ((n2 = (int)((float)((this.bottom - this.top) * (this.bottom - this.top)) / (float)this.getContentHeight())) < 32) {
                            n2 = 32;
                        }
                        if (n2 > this.bottom - this.top - 8) {
                            n2 = this.bottom - this.top - 8;
                        }
                        this.scrollMultiplier /= (float)(this.bottom - this.top - n2) / (float)n3;
                    } else {
                        this.scrollMultiplier = 1.0f;
                    }
                    this.initialClickY = bl ? (float)mouseY : -2.0f;
                } else {
                    this.initialClickY = -2.0f;
                }
            } else if (this.initialClickY >= 0.0f) {
                this.amountScrolled -= ((float)mouseY - this.initialClickY) * this.scrollMultiplier;
                this.initialClickY = mouseY;
            }
        } else {
            this.initialClickY = -1.0f;
        }
        this.bindAmountScrolled();
        GL11.glDisable(2896);
        GL11.glDisable(2912);
        Tessellator tessellator = Tessellator.instance;
        GL11.glBindTexture(3553, this.mc.renderEngine.getTexture("/gui/background.png"));
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        float f = 32.0f;
        tessellator.startDrawingQuads();
        tessellator.setColorOpaque_I(0x202020);
        tessellator.addVertexWithUV(this.left, this.bottom, 0.0, (float)this.left / f, (float)(this.bottom + (int)this.amountScrolled) / f);
        tessellator.addVertexWithUV(this.right, this.bottom, 0.0, (float)this.right / f, (float)(this.bottom + (int)this.amountScrolled) / f);
        tessellator.addVertexWithUV(this.right, this.top, 0.0, (float)this.right / f, (float)(this.top + (int)this.amountScrolled) / f);
        tessellator.addVertexWithUV(this.left, this.top, 0.0, (float)this.left / f, (float)(this.top + (int)this.amountScrolled) / f);
        tessellator.draw();
        n6 = this.width / 2 - 92 - 16;
        n5 = this.top + 4 - (int)this.amountScrolled;
        if (this.renderHeader) {
            this.renderHeader(n6, n5, tessellator);
        }
        for (n4 = 0; n4 < n7; ++n4) {
            n3 = n5 + n4 * this.posZ + this.headerHeight;
            n2 = this.posZ - 4;
            if (n3 > this.bottom || n3 + n2 < this.top) continue;
            if (this.renderSelectionHighlight && this.isSelected(n4)) {
                n = this.width / 2 - 110;
                int n11 = this.width / 2 + 110;
                GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                GL11.glDisable(3553);
                tessellator.startDrawingQuads();
                tessellator.setColorOpaque_I(0x808080);
                tessellator.addVertexWithUV(n, n3 + n2 + 2, 0.0, 0.0, 1.0);
                tessellator.addVertexWithUV(n11, n3 + n2 + 2, 0.0, 1.0, 1.0);
                tessellator.addVertexWithUV(n11, n3 - 2, 0.0, 1.0, 0.0);
                tessellator.addVertexWithUV(n, n3 - 2, 0.0, 0.0, 0.0);
                tessellator.setColorOpaque_I(0);
                tessellator.addVertexWithUV(n + 1, n3 + n2 + 1, 0.0, 0.0, 1.0);
                tessellator.addVertexWithUV(n11 - 1, n3 + n2 + 1, 0.0, 1.0, 1.0);
                tessellator.addVertexWithUV(n11 - 1, n3 - 1, 0.0, 1.0, 0.0);
                tessellator.addVertexWithUV(n + 1, n3 - 1, 0.0, 0.0, 0.0);
                tessellator.draw();
                GL11.glEnable(3553);
            }
            this.drawSlot(n4, n6, n3, n2, tessellator);
        }
        GL11.glDisable(2929);
        n4 = 4;
        this.overlayBackground(0, this.top, 255, 255);
        this.overlayBackground(this.bottom, this.height, 255, 255);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3008);
        GL11.glShadeModel(7425);
        GL11.glDisable(3553);
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA_I(0, 0);
        tessellator.addVertexWithUV(this.left, this.top + n4, 0.0, 0.0, 1.0);
        tessellator.addVertexWithUV(this.right, this.top + n4, 0.0, 1.0, 1.0);
        tessellator.setColorRGBA_I(0, 255);
        tessellator.addVertexWithUV(this.right, this.top, 0.0, 1.0, 0.0);
        tessellator.addVertexWithUV(this.left, this.top, 0.0, 0.0, 0.0);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA_I(0, 255);
        tessellator.addVertexWithUV(this.left, this.bottom, 0.0, 0.0, 1.0);
        tessellator.addVertexWithUV(this.right, this.bottom, 0.0, 1.0, 1.0);
        tessellator.setColorRGBA_I(0, 0);
        tessellator.addVertexWithUV(this.right, this.bottom - n4, 0.0, 1.0, 0.0);
        tessellator.addVertexWithUV(this.left, this.bottom - n4, 0.0, 0.0, 0.0);
        tessellator.draw();
        n3 = this.getContentHeight() - (this.bottom - this.top - 4);
        if (n3 > 0) {
            n2 = (this.bottom - this.top) * (this.bottom - this.top) / this.getContentHeight();
            if (n2 < 32) {
                n2 = 32;
            }
            if (n2 > this.bottom - this.top - 8) {
                n2 = this.bottom - this.top - 8;
            }
            if ((n = (int)this.amountScrolled * (this.bottom - this.top - n2) / n3 + this.top) < this.top) {
                n = this.top;
            }
            tessellator.startDrawingQuads();
            tessellator.setColorRGBA_I(0, 255);
            tessellator.addVertexWithUV(n8, this.bottom, 0.0, 0.0, 1.0);
            tessellator.addVertexWithUV(n9, this.bottom, 0.0, 1.0, 1.0);
            tessellator.addVertexWithUV(n9, this.top, 0.0, 1.0, 0.0);
            tessellator.addVertexWithUV(n8, this.top, 0.0, 0.0, 0.0);
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setColorRGBA_I(0x808080, 255);
            tessellator.addVertexWithUV(n8, n + n2, 0.0, 0.0, 1.0);
            tessellator.addVertexWithUV(n9, n + n2, 0.0, 1.0, 1.0);
            tessellator.addVertexWithUV(n9, n, 0.0, 1.0, 0.0);
            tessellator.addVertexWithUV(n8, n, 0.0, 0.0, 0.0);
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setColorRGBA_I(0xC0C0C0, 255);
            tessellator.addVertexWithUV(n8, n + n2 - 1, 0.0, 0.0, 1.0);
            tessellator.addVertexWithUV(n9 - 1, n + n2 - 1, 0.0, 1.0, 1.0);
            tessellator.addVertexWithUV(n9 - 1, n, 0.0, 1.0, 0.0);
            tessellator.addVertexWithUV(n8, n, 0.0, 0.0, 0.0);
            tessellator.draw();
        }
        this.renderDecorations(mouseX, mouseY);
        GL11.glEnable(3553);
        GL11.glShadeModel(7424);
        GL11.glEnable(3008);
        GL11.glDisable(3042);
    }

    private void overlayBackground(int top, int bottom, int topAlpha, int bottomAlpha) {
        Tessellator tessellator = Tessellator.instance;
        GL11.glBindTexture(3553, this.mc.renderEngine.getTexture("/gui/background.png"));
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        float f = 32.0f;
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA_I(0x404040, bottomAlpha);
        tessellator.addVertexWithUV(0.0, bottom, 0.0, 0.0, (float)bottom / f);
        tessellator.addVertexWithUV(this.width, bottom, 0.0, (float)this.width / f, (float)bottom / f);
        tessellator.setColorRGBA_I(0x404040, topAlpha);
        tessellator.addVertexWithUV(this.width, top, 0.0, (float)this.width / f, (float)top / f);
        tessellator.addVertexWithUV(0.0, top, 0.0, 0.0, (float)top / f);
        tessellator.draw();
    }
}

