package net.minecraft.src;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import org.lwjgl.opengl.GL11;

public class GuiMainMenu extends GuiScreen {
    private static final Random rand = new Random();
    private float updateCounter = 0.0F;
    private String splashText = "missingno";
    private GuiButton multiplayerButton;

    public GuiMainMenu() {
        try {
            List arraylist = new ArrayList();
            BufferedReader bufferedreader = new BufferedReader(
                new InputStreamReader(GuiMainMenu.class.getResourceAsStream("/title/splashes.txt"), Charset.forName("UTF-8"))
            );
            String s = "";

            while ((s = bufferedreader.readLine()) != null) {
                s = s.trim();
                if (s.length() > 0) {
                    arraylist.add(s);
                }
            }

            this.splashText = (String)arraylist.get(rand.nextInt(arraylist.size()));
        } catch (Exception exception) {
        }
    }

    @Override
    public void updateScreen() {
        this.updateCounter++;
    }

    @Override
    protected void keyTyped(char chr, int key) {
    }

    @Override
    public void initGui() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        if (calendar.get(2) + 1 == 11 && calendar.get(5) == 9) {
            this.splashText = "Happy birthday, ez!";
        } else if (calendar.get(2) + 1 == 6 && calendar.get(5) == 1) {
            this.splashText = "Happy birthday, Notch!";
        } else if (calendar.get(2) + 1 == 12 && calendar.get(5) == 24) {
            this.splashText = "Merry X-mas!";
        } else if (calendar.get(2) + 1 == 1 && calendar.get(5) == 1) {
            this.splashText = "Happy new year!";
        }

        StringTranslate stringtranslate = StringTranslate.getInstance();
        int i = this.height / 4 + 48;
        this.controlList.add(new GuiButton(1, this.width / 2 - 100, i, stringtranslate.translateKey("menu.singleplayer")));
        this.controlList.add(this.multiplayerButton = new GuiButton(2, this.width / 2 - 100, i + 24, stringtranslate.translateKey("menu.multiplayer")));
        this.controlList.add(new GuiButton(3, this.width / 2 - 100, i + 48, stringtranslate.translateKey("menu.mods")));
        if (this.mc.hideQuitButton) {
            this.controlList.add(new GuiButton(0, this.width / 2 - 100, i + 72, stringtranslate.translateKey("menu.options")));
        } else {
            this.controlList.add(new GuiButton(0, this.width / 2 - 100, i + 72 + 12, 98, 20, stringtranslate.translateKey("menu.options")));
            this.controlList.add(new GuiButton(4, this.width / 2 + 2, i + 72 + 12, 98, 20, stringtranslate.translateKey("menu.quit")));
        }

        if (this.mc.session == null) {
            this.multiplayerButton.enabled = false;
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == 0) {
            this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
        }

        if (button.id == 1) {
            this.mc.displayGuiScreen(new GuiSelectWorld(this));
        }

        if (button.id == 2) {
            this.mc.displayGuiScreen(new GuiMultiplayer(this));
        }

        if (button.id == 3) {
            this.mc.displayGuiScreen(new GuiTexturePacks(this));
        }

        if (button.id == 4) {
            this.mc.shutdown();
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float tickDelta) {
        this.drawDefaultBackground();
        Tessellator tessellator = Tessellator.instance;
        short short1 = 274;
        int i = this.width / 2 - short1 / 2;
        byte b0 = 30;
        GL11.glBindTexture(3553, this.mc.renderEngine.getTexture("/title/mclogo.png"));
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.drawTexturedModalRect(i + 0, b0 + 0, 0, 0, 155, 44);
        this.drawTexturedModalRect(i + 155, b0 + 0, 0, 45, 155, 44);
        tessellator.setColorOpaque_I(16777215);
        GL11.glPushMatrix();
        GL11.glTranslatef(this.width / 2 + 90, 70.0F, 0.0F);
        GL11.glRotatef(-20.0F, 0.0F, 0.0F, 1.0F);
        float f = 1.8F - MathHelper.abs(MathHelper.sin((float)(System.currentTimeMillis() % 1000L) / 1000.0F * (float) Math.PI * 2.0F) * 0.1F);
        f = f * 100.0F / (this.fontRenderer.getStringWidth(this.splashText) + 32);
        GL11.glScalef(f, f, f);
        this.drawCenteredString(this.fontRenderer, this.splashText, 0, -8, 16776960);
        GL11.glPopMatrix();
        this.drawString(this.fontRenderer, "Minecraft Beta 1.7.3", 2, 2, 5263440);
        String s = "Copyright Mojang AB. Do not distribute.";
        this.drawString(this.fontRenderer, s, this.width - this.fontRenderer.getStringWidth(s) - 2, this.height - 10, 16777215);
        super.drawScreen(mouseX, mouseY, tickDelta);
    }
}
