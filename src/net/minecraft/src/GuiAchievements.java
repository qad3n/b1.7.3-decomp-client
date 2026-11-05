package net.minecraft.src;

import java.util.Random;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class GuiAchievements extends GuiScreen {
    private static final int MIN_COLUMN = AchievementList.minDisplayColumn * 24 - 112;
    private static final int MIN_ROW = AchievementList.minDisplayRow * 24 - 112;
    private static final int MAX_COLUMN = AchievementList.maxDisplayColumn * 24 - 77;
    private static final int MAX_ROW = AchievementList.maxDisplayRow * 24 - 77;
    protected int iconWidth = 256;
    protected int iconHeight = 202;
    protected int prevMouseX = 0;
    protected int prevMouseY = 0;
    protected double mouseX;
    protected double mouseY;
    protected double scaledMouseDx;
    protected double scaledMouseDy;
    protected double scrollX;
    protected double scrollY;
    private int scroll = 0;
    private StatFileWriter stats;

    public GuiAchievements(StatFileWriter stats) {
        this.stats = stats;
        short short1 = 141;
        short short2 = 141;
        this.mouseX = this.scaledMouseDx = this.scrollX = AchievementList.openInventory.displayColumn * 24 - short1 / 2 - 12;
        this.mouseY = this.scaledMouseDy = this.scrollY = AchievementList.openInventory.displayRow * 24 - short2 / 2;
    }

    @Override
    public void initGui() {
        this.controlList.clear();
        this.controlList.add(new GuiSmallButton(1, this.width / 2 + 24, this.height / 2 + 74, 80, 20, StatCollector.translateToLocal("gui.done")));
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == 1) {
            this.mc.displayGuiScreen(null);
            this.mc.setIngameFocus();
        }

        super.actionPerformed(button);
    }

    @Override
    protected void keyTyped(char chr, int key) {
        if (key == this.mc.gameSettings.keyBindInventory.keyCode) {
            this.mc.displayGuiScreen(null);
            this.mc.setIngameFocus();
        } else {
            super.keyTyped(chr, key);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float tickDelta) {
        if (Mouse.isButtonDown(0)) {
            int i = (this.width - this.iconWidth) / 2;
            int j = (this.height - this.iconHeight) / 2;
            int k = i + 8;
            int l = j + 17;
            if ((this.scroll == 0 || this.scroll == 1) && mouseX >= k && mouseX < k + 224 && mouseY >= l && mouseY < l + 155) {
                if (this.scroll == 0) {
                    this.scroll = 1;
                } else {
                    this.scaledMouseDx = this.scaledMouseDx - (mouseX - this.prevMouseX);
                    this.scaledMouseDy = this.scaledMouseDy - (mouseY - this.prevMouseY);
                    this.scrollX = this.mouseX = this.scaledMouseDx;
                    this.scrollY = this.mouseY = this.scaledMouseDy;
                }

                this.prevMouseX = mouseX;
                this.prevMouseY = mouseY;
            }

            if (this.scrollX < MIN_COLUMN) {
                this.scrollX = MIN_COLUMN;
            }

            if (this.scrollY < MIN_ROW) {
                this.scrollY = MIN_ROW;
            }

            if (this.scrollX >= MAX_COLUMN) {
                this.scrollX = MAX_COLUMN - 1;
            }

            if (this.scrollY >= MAX_ROW) {
                this.scrollY = MAX_ROW - 1;
            }
        } else {
            this.scroll = 0;
        }

        this.drawDefaultBackground();
        this.renderIcons(mouseX, mouseY, tickDelta);
        GL11.glDisable(2896);
        GL11.glDisable(2929);
        this.setTitle();
        GL11.glEnable(2896);
        GL11.glEnable(2929);
    }

    @Override
    public void updateScreen() {
        this.mouseX = this.scaledMouseDx;
        this.mouseY = this.scaledMouseDy;
        double d0 = this.scrollX - this.scaledMouseDx;
        double d1 = this.scrollY - this.scaledMouseDy;
        if (d0 * d0 + d1 * d1 < 4.0) {
            this.scaledMouseDx += d0;
            this.scaledMouseDy += d1;
        } else {
            this.scaledMouseDx += d0 * 0.85;
            this.scaledMouseDy += d1 * 0.85;
        }
    }

    protected void setTitle() {
        int i = (this.width - this.iconWidth) / 2;
        int j = (this.height - this.iconHeight) / 2;
        this.fontRenderer.drawString("Achievements", i + 15, j + 5, 4210752);
    }

    protected void renderIcons(int mouseX, int mouseY, float tickdelta) {
        int i = MathHelper.floor_double(this.mouseX + (this.scaledMouseDx - this.mouseX) * tickdelta);
        int j = MathHelper.floor_double(this.mouseY + (this.scaledMouseDy - this.mouseY) * tickdelta);
        if (i < MIN_COLUMN) {
            i = MIN_COLUMN;
        }

        if (j < MIN_ROW) {
            j = MIN_ROW;
        }

        if (i >= MAX_COLUMN) {
            i = MAX_COLUMN - 1;
        }

        if (j >= MAX_ROW) {
            j = MAX_ROW - 1;
        }

        int k = this.mc.renderEngine.getTexture("/terrain.png");
        int l = this.mc.renderEngine.getTexture("/achievement/bg.png");
        int i1 = (this.width - this.iconWidth) / 2;
        int j1 = (this.height - this.iconHeight) / 2;
        int k1 = i1 + 16;
        int l1 = j1 + 17;
        this.zLevel = 0.0F;
        GL11.glDepthFunc(518);
        GL11.glPushMatrix();
        GL11.glTranslatef(0.0F, 0.0F, -200.0F);
        GL11.glEnable(3553);
        GL11.glDisable(2896);
        GL11.glEnable(32826);
        GL11.glEnable(2903);
        this.mc.renderEngine.bindTexture(k);
        int i2 = i + 288 >> 4;
        int j2 = j + 288 >> 4;
        int k2 = (i + 288) % 16;
        int l2 = (j + 288) % 16;
        Random random = new Random();

        for (int l3 = 0; l3 * 16 - l2 < 155; l3++) {
            float f = 0.6F - (j2 + l3) / 25.0F * 0.3F;
            GL11.glColor4f(f, f, f, 1.0F);

            for (int i4 = 0; i4 * 16 - k2 < 224; i4++) {
                random.setSeed(1234 + i2 + i4);
                random.nextInt();
                int j4 = random.nextInt(1 + j2 + l3) + (j2 + l3) / 2;
                int k4 = Block.sand.blockIndexInTexture;
                if (j4 > 37 || j2 + l3 == 35) {
                    k4 = Block.bedrock.blockIndexInTexture;
                } else if (j4 == 22) {
                    if (random.nextInt(2) == 0) {
                        k4 = Block.oreDiamond.blockIndexInTexture;
                    } else {
                        k4 = Block.oreRedstone.blockIndexInTexture;
                    }
                } else if (j4 == 10) {
                    k4 = Block.oreIron.blockIndexInTexture;
                } else if (j4 == 8) {
                    k4 = Block.oreCoal.blockIndexInTexture;
                } else if (j4 > 4) {
                    k4 = Block.stone.blockIndexInTexture;
                } else if (j4 > 0) {
                    k4 = Block.dirt.blockIndexInTexture;
                }

                this.drawTexturedModalRect(k1 + i4 * 16 - k2, l1 + l3 * 16 - l2, k4 % 16 << 4, k4 >> 4 << 4, 16, 16);
            }
        }

        GL11.glEnable(2929);
        GL11.glDepthFunc(515);
        GL11.glDisable(3553);

        for (int l4 = 0; l4 < AchievementList.achievementList.size(); l4++) {
            Achievement achievement1 = AchievementList.achievementList.get(l4);
            if (achievement1.parentAchievement != null) {
                k2 = achievement1.displayColumn * 24 - i + 11 + k1;
                l2 = achievement1.displayRow * 24 - j + 11 + l1;
                int i3 = achievement1.parentAchievement.displayColumn * 24 - i + 11 + k1;
                int j3 = achievement1.parentAchievement.displayRow * 24 - j + 11 + l1;
                int k3 = 0;
                boolean flag = this.stats.hasAchievementUnlocked(achievement1);
                boolean flag1 = this.stats.hasParentAchievement(achievement1);
                int k7 = Math.sin(System.currentTimeMillis() % 600L / 600.0 * Math.PI * 2.0) > 0.6 ? 255 : 130;
                if (flag) {
                    k3 = -9408400;
                } else if (flag1) {
                    k3 = 65280 + (k7 << 24);
                } else {
                    k3 = -16777216;
                }

                this.drawHorizontalLine(k2, i3, l2, k3);
                this.drawVerticalLine(i3, l2, j3, k3);
            }
        }

        Achievement achievement = null;
        RenderItem renderitem = new RenderItem();
        GL11.glPushMatrix();
        GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GL11.glPopMatrix();
        GL11.glDisable(2896);
        GL11.glEnable(32826);
        GL11.glEnable(2903);

        for (int i5 = 0; i5 < AchievementList.achievementList.size(); i5++) {
            Achievement achievement3 = AchievementList.achievementList.get(i5);
            int j5 = achievement3.displayColumn * 24 - i;
            int k5 = achievement3.displayRow * 24 - j;
            if (j5 >= -24 && k5 >= -24 && j5 <= 224 && k5 <= 155) {
                if (this.stats.hasAchievementUnlocked(achievement3)) {
                    float f1 = 1.0F;
                    GL11.glColor4f(f1, f1, f1, 1.0F);
                } else if (this.stats.hasParentAchievement(achievement3)) {
                    float f2 = Math.sin(System.currentTimeMillis() % 600L / 600.0 * Math.PI * 2.0) < 0.6 ? 0.6F : 0.8F;
                    GL11.glColor4f(f2, f2, f2, 1.0F);
                } else {
                    float f3 = 0.3F;
                    GL11.glColor4f(f3, f3, f3, 1.0F);
                }

                this.mc.renderEngine.bindTexture(l);
                int i6 = k1 + j5;
                int k6 = l1 + k5;
                if (achievement3.getSpecial()) {
                    this.drawTexturedModalRect(i6 - 2, k6 - 2, 26, 202, 26, 26);
                } else {
                    this.drawTexturedModalRect(i6 - 2, k6 - 2, 0, 202, 26, 26);
                }

                if (!this.stats.hasParentAchievement(achievement3)) {
                    float f4 = 0.1F;
                    GL11.glColor4f(f4, f4, f4, 1.0F);
                    renderitem.useCustomDisplayColor = false;
                }

                GL11.glEnable(2896);
                GL11.glEnable(2884);
                renderitem.renderItemIntoGUI(this.mc.fontRenderer, this.mc.renderEngine, achievement3.theItemStack, i6 + 3, k6 + 3);
                GL11.glDisable(2896);
                if (!this.stats.hasParentAchievement(achievement3)) {
                    renderitem.useCustomDisplayColor = true;
                }

                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                if (mouseX >= k1
                    && mouseY >= l1
                    && mouseX < k1 + 224
                    && mouseY < l1 + 155
                    && mouseX >= i6
                    && mouseX <= i6 + 22
                    && mouseY >= k6
                    && mouseY <= k6 + 22) {
                    achievement = achievement3;
                }
            }
        }

        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(l);
        this.drawTexturedModalRect(i1, j1, 0, 0, this.iconWidth, this.iconHeight);
        GL11.glPopMatrix();
        this.zLevel = 0.0F;
        GL11.glDepthFunc(515);
        GL11.glDisable(2929);
        GL11.glEnable(3553);
        super.drawScreen(mouseX, mouseY, tickdelta);
        if (achievement != null) {
            Achievement achievement2 = achievement;
            String s = achievement2.statName;
            String s1 = achievement2.getDescription();
            int l5 = mouseX + 12;
            int j6 = mouseY - 4;
            if (this.stats.hasParentAchievement(achievement2)) {
                int l6 = Math.max(this.fontRenderer.getStringWidth(s), 120);
                int j7 = this.fontRenderer.splitAndGetHeight(s1, l6);
                if (this.stats.hasAchievementUnlocked(achievement2)) {
                    j7 += 12;
                }

                this.drawGradientRect(l5 - 3, j6 - 3, l5 + l6 + 3, j6 + j7 + 3 + 12, -1073741824, -1073741824);
                this.fontRenderer.splitAndDraw(s1, l5, j6 + 12, l6, -6250336);
                if (this.stats.hasAchievementUnlocked(achievement2)) {
                    this.fontRenderer.drawStringWithShadow(StatCollector.translateToLocal("achievement.taken"), l5, j6 + j7 + 4, -7302913);
                }
            } else {
                int i7 = Math.max(this.fontRenderer.getStringWidth(s), 120);
                String s2 = StatCollector.translateToLocalFormatted("achievement.requires", achievement2.parentAchievement.statName);
                int l7 = this.fontRenderer.splitAndGetHeight(s2, i7);
                this.drawGradientRect(l5 - 3, j6 - 3, l5 + i7 + 3, j6 + l7 + 12 + 3, -1073741824, -1073741824);
                this.fontRenderer.splitAndDraw(s2, l5, j6 + 12, i7, -9416624);
            }

            this.fontRenderer
                .drawStringWithShadow(
                    s,
                    l5,
                    j6,
                    this.stats.hasParentAchievement(achievement2) ? (achievement2.getSpecial() ? -128 : -1) : (achievement2.getSpecial() ? -8355776 : -8355712)
                );
        }

        GL11.glEnable(2929);
        GL11.glEnable(2896);
        RenderHelper.disableStandardItemLighting();
    }

    @Override
    public boolean doesGuiPauseGame() {
        return true;
    }
}
