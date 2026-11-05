package net.minecraft.src;

import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

public class GuiAchievement
extends Gui {
    private Minecraft theGame;
    private int achievementWindowWidth;
    private int achievementWindowHeight;
    private String title;
    private String description;
    private Achievement theAchievement;
    private long startTime;
    private RenderItem itemRender;
    private boolean tutorial;

    public GuiAchievement(Minecraft minecraft) {
        this.theGame = minecraft;
        this.itemRender = new RenderItem();
    }

    public void queueTakenAchievement(Achievement achievement) {
        this.title = StatCollector.translateToLocal("achievement.get");
        this.description = achievement.statName;
        this.startTime = System.currentTimeMillis();
        this.theAchievement = achievement;
        this.tutorial = false;
    }

    public void queueAchievementInformation(Achievement achievement) {
        this.title = achievement.statName;
        this.description = achievement.getDescription();
        this.startTime = System.currentTimeMillis() - 2500L;
        this.theAchievement = achievement;
        this.tutorial = true;
    }

    private void updateAchievementWindowScale() {
        GL11.glViewport(0, 0, this.theGame.displayWidth, this.theGame.displayHeight);
        GL11.glMatrixMode(5889);
        GL11.glLoadIdentity();
        GL11.glMatrixMode(5888);
        GL11.glLoadIdentity();
        this.achievementWindowWidth = this.theGame.displayWidth;
        this.achievementWindowHeight = this.theGame.displayHeight;
        ScaledResolution scaledResolution = new ScaledResolution(this.theGame.gameSettings, this.theGame.displayWidth, this.theGame.displayHeight);
        this.achievementWindowWidth = scaledResolution.getScaledWidth();
        this.achievementWindowHeight = scaledResolution.getScaledHeight();
        GL11.glClear(256);
        GL11.glMatrixMode(5889);
        GL11.glLoadIdentity();
        GL11.glOrtho(0.0, this.achievementWindowWidth, this.achievementWindowHeight, 0.0, 1000.0, 3000.0);
        GL11.glMatrixMode(5888);
        GL11.glLoadIdentity();
        GL11.glTranslatef(0.0f, 0.0f, -2000.0f);
    }

    public void updateAchievementWindow() {
        if (Minecraft.hasPaidCheckTime > 0L) {
            GL11.glDisable(2929);
            GL11.glDepthMask(false);
            RenderHelper.disableStandardItemLighting();
            this.updateAchievementWindowScale();
            String string = "Minecraft Beta 1.7.3   Unlicensed Copy :(";
            String string2 = "(Or logged in from another location)";
            String string3 = "Purchase at minecraft.net";
            this.theGame.fontRenderer.drawStringWithShadow(string, 2, 2, 0xFFFFFF);
            this.theGame.fontRenderer.drawStringWithShadow(string2, 2, 11, 0xFFFFFF);
            this.theGame.fontRenderer.drawStringWithShadow(string3, 2, 20, 0xFFFFFF);
            GL11.glDepthMask(true);
            GL11.glEnable(2929);
        }
        if (this.theAchievement == null || this.startTime == 0L) {
            return;
        }
        double d = (double)(System.currentTimeMillis() - this.startTime) / 3000.0;
        if (!this.tutorial && !this.tutorial && (d < 0.0 || d > 1.0)) {
            this.startTime = 0L;
            return;
        }
        this.updateAchievementWindowScale();
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        double d2 = d * 2.0;
        if (d2 > 1.0) {
            d2 = 2.0 - d2;
        }
        d2 *= 4.0;
        if ((d2 = 1.0 - d2) < 0.0) {
            d2 = 0.0;
        }
        d2 *= d2;
        d2 *= d2;
        int n = this.achievementWindowWidth - 160;
        int n2 = 0 - (int)(d2 * 36.0);
        int n3 = this.theGame.renderEngine.getTexture("/achievement/bg.png");
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glEnable(3553);
        GL11.glBindTexture(3553, n3);
        GL11.glDisable(2896);
        this.drawTexturedModalRect(n, n2, 96, 202, 160, 32);
        if (this.tutorial) {
            this.theGame.fontRenderer.splitAndDraw(this.description, n + 30, n2 + 7, 120, -1);
        } else {
            this.theGame.fontRenderer.drawString(this.title, n + 30, n2 + 7, -256);
            this.theGame.fontRenderer.drawString(this.description, n + 30, n2 + 18, -1);
        }
        GL11.glPushMatrix();
        GL11.glRotatef(180.0f, 1.0f, 0.0f, 0.0f);
        RenderHelper.enableStandardItemLighting();
        GL11.glPopMatrix();
        GL11.glDisable(2896);
        GL11.glEnable(32826);
        GL11.glEnable(2903);
        GL11.glEnable(2896);
        this.itemRender.renderItemIntoGUI(this.theGame.fontRenderer, this.theGame.renderEngine, this.theAchievement.theItemStack, n + 8, n2 + 8);
        GL11.glDisable(2896);
        GL11.glDepthMask(true);
        GL11.glEnable(2929);
    }
}

