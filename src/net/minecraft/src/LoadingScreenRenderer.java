package net.minecraft.src;

import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

public class LoadingScreenRenderer
implements IProgressUpdate {
    private String stage = "";
    private Minecraft mc;
    private String title = "";
    private long lastTime = System.currentTimeMillis();
    private boolean noAbort = false;

    public LoadingScreenRenderer(Minecraft minecraft) {
        this.mc = minecraft;
    }

    public void printText(String title) {
        this.noAbort = false;
        this.start(title);
    }

    public void progressStartNoAbort(String title) {
        this.noAbort = true;
        this.start(this.title);
    }

    public void start(String title) {
        if (!this.mc.running) {
            if (this.noAbort) {
                return;
            }
            throw new MinecraftError();
        }
        this.title = title;
        ScaledResolution scaledResolution = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
        GL11.glClear(256);
        GL11.glMatrixMode(5889);
        GL11.glLoadIdentity();
        GL11.glOrtho(0.0, scaledResolution.scaledWidthD, scaledResolution.scaledHeightD, 0.0, 100.0, 300.0);
        GL11.glMatrixMode(5888);
        GL11.glLoadIdentity();
        GL11.glTranslatef(0.0f, 0.0f, -200.0f);
    }

    public void displayLoadingString(String stage) {
        if (!this.mc.running) {
            if (this.noAbort) {
                return;
            }
            throw new MinecraftError();
        }
        this.lastTime = 0L;
        this.stage = stage;
        this.setLoadingProgress(-1);
        this.lastTime = 0L;
    }

    public void setLoadingProgress(int percentage) {
        if (!this.mc.running) {
            if (this.noAbort) {
                return;
            }
            throw new MinecraftError();
        }
        long l = System.currentTimeMillis();
        if (l - this.lastTime < 20L) {
            return;
        }
        this.lastTime = l;
        ScaledResolution scaledResolution = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
        int n = scaledResolution.getScaledWidth();
        int n2 = scaledResolution.getScaledHeight();
        GL11.glClear(256);
        GL11.glMatrixMode(5889);
        GL11.glLoadIdentity();
        GL11.glOrtho(0.0, scaledResolution.scaledWidthD, scaledResolution.scaledHeightD, 0.0, 100.0, 300.0);
        GL11.glMatrixMode(5888);
        GL11.glLoadIdentity();
        GL11.glTranslatef(0.0f, 0.0f, -200.0f);
        GL11.glClear(16640);
        Tessellator tessellator = Tessellator.instance;
        int n3 = this.mc.renderEngine.getTexture("/gui/background.png");
        GL11.glBindTexture(3553, n3);
        float f = 32.0f;
        tessellator.startDrawingQuads();
        tessellator.setColorOpaque_I(0x404040);
        tessellator.addVertexWithUV(0.0, n2, 0.0, 0.0, (float)n2 / f);
        tessellator.addVertexWithUV(n, n2, 0.0, (float)n / f, (float)n2 / f);
        tessellator.addVertexWithUV(n, 0.0, 0.0, (float)n / f, 0.0);
        tessellator.addVertexWithUV(0.0, 0.0, 0.0, 0.0, 0.0);
        tessellator.draw();
        if (percentage >= 0) {
            int n4 = 100;
            int n5 = 2;
            int n6 = n / 2 - n4 / 2;
            int n7 = n2 / 2 + 16;
            GL11.glDisable(3553);
            tessellator.startDrawingQuads();
            tessellator.setColorOpaque_I(0x808080);
            tessellator.addVertex(n6, n7, 0.0);
            tessellator.addVertex(n6, n7 + n5, 0.0);
            tessellator.addVertex(n6 + n4, n7 + n5, 0.0);
            tessellator.addVertex(n6 + n4, n7, 0.0);
            tessellator.setColorOpaque_I(0x80FF80);
            tessellator.addVertex(n6, n7, 0.0);
            tessellator.addVertex(n6, n7 + n5, 0.0);
            tessellator.addVertex(n6 + percentage, n7 + n5, 0.0);
            tessellator.addVertex(n6 + percentage, n7, 0.0);
            tessellator.draw();
            GL11.glEnable(3553);
        }
        this.mc.fontRenderer.drawStringWithShadow(this.title, (n - this.mc.fontRenderer.getStringWidth(this.title)) / 2, n2 / 2 - 4 - 16, 0xFFFFFF);
        this.mc.fontRenderer.drawStringWithShadow(this.stage, (n - this.mc.fontRenderer.getStringWidth(this.stage)) / 2, n2 / 2 - 4 + 8, 0xFFFFFF);
        Display.update();
        try {
            Thread.yield();
        }
        catch (Exception exception) {
            // empty catch block
        }
    }
}

