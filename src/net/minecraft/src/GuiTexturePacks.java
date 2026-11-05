package net.minecraft.src;

import java.io.File;
import java.util.List;
import net.minecraft.client.Minecraft;
import org.lwjgl.Sys;
import org.lwjgl.opengl.GL11;

public class GuiTexturePacks
extends GuiScreen {
    protected GuiScreen guiScreen;
    private int cooldown = -1;
    private String fileLocation = "";
    private GuiTexturePackSlot guiTexturePackSlot;

    public GuiTexturePacks(GuiScreen parent) {
        this.guiScreen = parent;
    }

    public void initGui() {
        StringTranslate stringTranslate = StringTranslate.getInstance();
        this.controlList.add(new GuiSmallButton(5, this.width / 2 - 154, this.height - 48, stringTranslate.translateKey("texturePack.openFolder")));
        this.controlList.add(new GuiSmallButton(6, this.width / 2 + 4, this.height - 48, stringTranslate.translateKey("gui.done")));
        this.mc.texturePackList.updateAvaliableTexturePacks();
        this.fileLocation = new File(Minecraft.getMinecraftDir(), "texturepacks").getAbsolutePath();
        this.guiTexturePackSlot = new GuiTexturePackSlot();
        this.guiTexturePackSlot.registerScrollButtons(this.controlList, 7, 8);
    }

    protected void actionPerformed(GuiButton button) {
        if (!button.enabled) {
            return;
        }
        if (button.id == 5) {
            Sys.openURL("file://" + this.fileLocation);
        } else if (button.id == 6) {
            this.mc.renderEngine.refreshTextures();
            this.mc.displayGuiScreen(this.guiScreen);
        } else {
            this.guiTexturePackSlot.actionPerformed(button);
        }
    }

    protected void mouseClicked(int mouseX, int mouseY, int button) {
        super.mouseClicked(mouseX, mouseY, button);
    }

    protected void mouseMovedOrUp(int mouseX, int mouseY, int button) {
        super.mouseMovedOrUp(mouseX, mouseY, button);
    }

    public void drawScreen(int mouseX, int mouseY, float tickDelta) {
        this.guiTexturePackSlot.drawScreen(mouseX, mouseY, tickDelta);
        if (this.cooldown <= 0) {
            this.mc.texturePackList.updateAvaliableTexturePacks();
            this.cooldown += 20;
        }
        StringTranslate stringTranslate = StringTranslate.getInstance();
        this.drawCenteredString(this.fontRenderer, stringTranslate.translateKey("texturePack.title"), this.width / 2, 16, 0xFFFFFF);
        this.drawCenteredString(this.fontRenderer, stringTranslate.translateKey("texturePack.folderInfo"), this.width / 2 - 77, this.height - 26, 0x808080);
        super.drawScreen(mouseX, mouseY, tickDelta);
    }

    public void updateScreen() {
        super.updateScreen();
        --this.cooldown;
    }

    class GuiTexturePackSlot
    extends GuiSlot {
        public GuiTexturePackSlot() {
            super(GuiTexturePacks.this.mc, GuiTexturePacks.this.width, GuiTexturePacks.this.height, 32, GuiTexturePacks.this.height - 55 + 4, 36);
        }

        protected int getSize() {
            List<TexturePackBase> list = ((GuiTexturePacks)GuiTexturePacks.this).mc.texturePackList.availableTexturePacks();
            return list.size();
        }

        protected void elementClicked(int index, boolean doubleClick) {
            List<TexturePackBase> list = ((GuiTexturePacks)GuiTexturePacks.this).mc.texturePackList.availableTexturePacks();
            ((GuiTexturePacks)GuiTexturePacks.this).mc.texturePackList.setTexturePack(list.get(index));
            ((GuiTexturePacks)GuiTexturePacks.this).mc.renderEngine.refreshTextures();
        }

        protected boolean isSelected(int index) {
            List<TexturePackBase> list = ((GuiTexturePacks)GuiTexturePacks.this).mc.texturePackList.availableTexturePacks();
            return ((GuiTexturePacks)GuiTexturePacks.this).mc.texturePackList.selectedTexturePack == list.get(index);
        }

        protected int getContentHeight() {
            return this.getSize() * 36;
        }

        protected void drawBackground() {
            GuiTexturePacks.this.drawDefaultBackground();
        }

        protected void drawSlot(int index, int x, int y, int height, Tessellator tesselator) {
            TexturePackBase texturePackBase = ((GuiTexturePacks)GuiTexturePacks.this).mc.texturePackList.availableTexturePacks().get(index);
            texturePackBase.bindThumbnailTexture(GuiTexturePacks.this.mc);
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            tesselator.startDrawingQuads();
            tesselator.setColorOpaque_I(0xFFFFFF);
            tesselator.addVertexWithUV(x, y + height, 0.0, 0.0, 1.0);
            tesselator.addVertexWithUV(x + 32, y + height, 0.0, 1.0, 1.0);
            tesselator.addVertexWithUV(x + 32, y, 0.0, 1.0, 0.0);
            tesselator.addVertexWithUV(x, y, 0.0, 0.0, 0.0);
            tesselator.draw();
            GuiTexturePacks.this.drawString(GuiTexturePacks.this.fontRenderer, texturePackBase.texturePackFileName, x + 32 + 2, y + 1, 0xFFFFFF);
            GuiTexturePacks.this.drawString(GuiTexturePacks.this.fontRenderer, texturePackBase.firstDescriptionLine, x + 32 + 2, y + 12, 0x808080);
            GuiTexturePacks.this.drawString(GuiTexturePacks.this.fontRenderer, texturePackBase.secondDescriptionLine, x + 32 + 2, y + 12 + 10, 0x808080);
        }
    }
}

