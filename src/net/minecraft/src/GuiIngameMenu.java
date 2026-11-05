package net.minecraft.src;

public class GuiIngameMenu extends GuiScreen {
    private int updateCounter2 = 0;
    private int updateCounter = 0;

    @Override
    public void initGui() {
        this.updateCounter2 = 0;
        this.controlList.clear();
        byte b0 = -16;
        this.controlList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 120 + b0, "Save and quit to title"));
        if (this.mc.isMultiplayerWorld()) {
            this.controlList.get(0).displayString = "Disconnect";
        }

        this.controlList.add(new GuiButton(4, this.width / 2 - 100, this.height / 4 + 24 + b0, "Back to game"));
        this.controlList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 96 + b0, "Options..."));
        this.controlList.add(new GuiButton(5, this.width / 2 - 100, this.height / 4 + 48 + b0, 98, 20, StatCollector.translateToLocal("gui.achievements")));
        this.controlList.add(new GuiButton(6, this.width / 2 + 2, this.height / 4 + 48 + b0, 98, 20, StatCollector.translateToLocal("gui.stats")));
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == 0) {
            this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
        }

        if (button.id == 1) {
            this.mc.statFileWriter.readStat(StatList.leaveGameStat, 1);
            if (this.mc.isMultiplayerWorld()) {
                this.mc.theWorld.sendQuittingDisconnectingPacket();
            }

            this.mc.changeWorld1(null);
            this.mc.displayGuiScreen(new GuiMainMenu());
        }

        if (button.id == 4) {
            this.mc.displayGuiScreen(null);
            this.mc.setIngameFocus();
        }

        if (button.id == 5) {
            this.mc.displayGuiScreen(new GuiAchievements(this.mc.statFileWriter));
        }

        if (button.id == 6) {
            this.mc.displayGuiScreen(new GuiStats(this, this.mc.statFileWriter));
        }
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        this.updateCounter++;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float tickDelta) {
        this.drawDefaultBackground();
        boolean flag = !this.mc.theWorld.saveWhilePaused(this.updateCounter2++);
        if (flag || this.updateCounter < 20) {
            float f = (this.updateCounter % 10 + tickDelta) / 10.0F;
            f = MathHelper.sin(f * (float) Math.PI * 2.0F) * 0.2F + 0.8F;
            int i = (int)(255.0F * f);
            this.drawString(this.fontRenderer, "Saving level..", 8, this.height - 16, i << 16 | i << 8 | i);
        }

        this.drawCenteredString(this.fontRenderer, "Game menu", this.width / 2, 40, 16777215);
        super.drawScreen(mouseX, mouseY, tickDelta);
    }
}
