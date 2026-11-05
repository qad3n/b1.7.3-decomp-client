package net.minecraft.src;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class GuiSelectWorld
extends GuiScreen {
    private final DateFormat dateFormatter = new SimpleDateFormat();
    protected GuiScreen parentScreen;
    protected String screenTitle = "Select world";
    private boolean selected = false;
    private int selectedWorld;
    private List<SaveFormatComparator> saveList;
    private GuiWorldSlot worldSlotContainer;
    private String worldText;
    private String conversionText;
    private boolean deleting;
    private GuiButton buttonRename;
    private GuiButton buttonSelect;
    private GuiButton buttonDelete;

    public GuiSelectWorld(GuiScreen parent) {
        this.parentScreen = parent;
    }

    public void initGui() {
        StringTranslate stringTranslate = StringTranslate.getInstance();
        this.screenTitle = stringTranslate.translateKey("selectWorld.title");
        this.worldText = stringTranslate.translateKey("selectWorld.world");
        this.conversionText = stringTranslate.translateKey("selectWorld.conversion");
        this.loadSaves();
        this.worldSlotContainer = new GuiWorldSlot();
        this.worldSlotContainer.registerScrollButtons(this.controlList, 4, 5);
        this.initButtons();
    }

    private void loadSaves() {
        ISaveFormat iSaveFormat = this.mc.getSaveLoader();
        this.saveList = iSaveFormat.getAll();
        Collections.sort(this.saveList);
        this.selectedWorld = -1;
    }

    protected String getSaveFileName(int index) {
        return this.saveList.get(index).getFileName();
    }

    protected String getSaveName(int index) {
        String string = this.saveList.get(index).getDisplayName();
        if (string == null || MathHelper.stringNullOrLengthZero(string)) {
            StringTranslate stringTranslate = StringTranslate.getInstance();
            string = stringTranslate.translateKey("selectWorld.world") + " " + (index + 1);
        }
        return string;
    }

    public void initButtons() {
        StringTranslate stringtranslate = StringTranslate.getInstance();
        this.controlList.add(this.buttonSelect = new GuiButton(1, this.width / 2 - 154, this.height - 52, 150, 20, stringtranslate.translateKey("selectWorld.select")));
        this.controlList.add(this.buttonRename = new GuiButton(6, this.width / 2 - 154, this.height - 28, 70, 20, stringtranslate.translateKey("selectWorld.rename")));
        this.controlList.add(this.buttonDelete = new GuiButton(2, this.width / 2 - 74, this.height - 28, 70, 20, stringtranslate.translateKey("selectWorld.delete")));
        this.controlList.add(new GuiButton(3, this.width / 2 + 4, this.height - 52, 150, 20, stringtranslate.translateKey("selectWorld.create")));
        this.controlList.add(new GuiButton(0, this.width / 2 + 4, this.height - 28, 150, 20, stringtranslate.translateKey("gui.cancel")));
        this.buttonSelect.enabled = false;
        this.buttonRename.enabled = false;
        this.buttonDelete.enabled = false;
    }

    protected void actionPerformed(GuiButton button) {
        if (!button.enabled) {
            return;
        }
        if (button.id == 2) {
            String string = this.getSaveName(this.selectedWorld);
            if (string != null) {
                this.deleting = true;
                StringTranslate stringTranslate = StringTranslate.getInstance();
                String string2 = stringTranslate.translateKey("selectWorld.deleteQuestion");
                String string3 = "'" + string + "' " + stringTranslate.translateKey("selectWorld.deleteWarning");
                String string4 = stringTranslate.translateKey("selectWorld.deleteButton");
                String string5 = stringTranslate.translateKey("gui.cancel");
                GuiYesNo guiYesNo = new GuiYesNo(this, string2, string3, string4, string5, this.selectedWorld);
                this.mc.displayGuiScreen(guiYesNo);
            }
        } else if (button.id == 1) {
            this.selectWorld(this.selectedWorld);
        } else if (button.id == 3) {
            this.mc.displayGuiScreen(new GuiCreateWorld(this));
        } else if (button.id == 6) {
            this.mc.displayGuiScreen(new GuiRenameWorld(this, this.getSaveFileName(this.selectedWorld)));
        } else if (button.id == 0) {
            this.mc.displayGuiScreen(this.parentScreen);
        } else {
            this.worldSlotContainer.actionPerformed(button);
        }
    }

    public void selectWorld(int id) {
        this.mc.displayGuiScreen(null);
        if (this.selected) {
            return;
        }
        this.selected = true;
        this.mc.playerController = new PlayerControllerSP(this.mc);
        String string = this.getSaveFileName(id);
        if (string == null) {
            string = "World" + id;
        }
        this.mc.startWorld(string, this.getSaveName(id), 0L);
        this.mc.displayGuiScreen(null);
    }

    public void deleteWorld(boolean result, int id) {
        if (this.deleting) {
            this.deleting = false;
            if (result) {
                ISaveFormat iSaveFormat = this.mc.getSaveLoader();
                iSaveFormat.flushCache();
                iSaveFormat.delete(this.getSaveFileName(id));
                this.loadSaves();
            }
            this.mc.displayGuiScreen(this);
        }
    }

    public void drawScreen(int mouseX, int mouseY, float tickDelta) {
        this.worldSlotContainer.drawScreen(mouseX, mouseY, tickDelta);
        this.drawCenteredString(this.fontRenderer, this.screenTitle, this.width / 2, 20, 0xFFFFFF);
        super.drawScreen(mouseX, mouseY, tickDelta);
    }

    class GuiWorldSlot
    extends GuiSlot {
        public GuiWorldSlot() {
            super(GuiSelectWorld.this.mc, GuiSelectWorld.this.width, GuiSelectWorld.this.height, 32, GuiSelectWorld.this.height - 64, 36);
        }

        protected int getSize() {
            return GuiSelectWorld.this.saveList.size();
        }

        @Override
protected void elementClicked(int index, boolean doubleClick) {
    GuiSelectWorld.this.selectedWorld = index;
    boolean flag = GuiSelectWorld.this.selectedWorld >= 0 && GuiSelectWorld.this.selectedWorld < this.getSize();
    GuiSelectWorld.this.buttonSelect.enabled = flag;
    GuiSelectWorld.this.buttonRename.enabled = flag;
    GuiSelectWorld.this.buttonDelete.enabled = flag;
    if (doubleClick && flag) {
        GuiSelectWorld.this.selectWorld(index);
    }
}

        protected boolean isSelected(int index) {
            return index == GuiSelectWorld.this.selectedWorld;
        }

        protected int getContentHeight() {
            return GuiSelectWorld.this.saveList.size() * 36;
        }

        protected void drawBackground() {
            GuiSelectWorld.this.drawDefaultBackground();
        }

        protected void drawSlot(int index, int x, int y, int height, Tessellator tesselator) {
            SaveFormatComparator saveFormatComparator = (SaveFormatComparator)GuiSelectWorld.this.saveList.get(index);
            String string = saveFormatComparator.getDisplayName();
            if (string == null || MathHelper.stringNullOrLengthZero(string)) {
                string = GuiSelectWorld.this.worldText + " " + (index + 1);
            }
            String string2 = saveFormatComparator.getFileName();
            string2 = string2 + " (" + GuiSelectWorld.this.dateFormatter.format(new Date(saveFormatComparator.getLastPlayed()));
            long l = saveFormatComparator.getSize();
            string2 = string2 + ", " + (float)(l / 1024L * 100L / 1024L) / 100.0f + " MB)";
            String string3 = "";
            if (saveFormatComparator.isSameVersion()) {
                string3 = GuiSelectWorld.this.conversionText + " " + string3;
            }
            GuiSelectWorld.this.drawString(GuiSelectWorld.this.fontRenderer, string, x + 2, y + 1, 0xFFFFFF);
            GuiSelectWorld.this.drawString(GuiSelectWorld.this.fontRenderer, string2, x + 2, y + 12, 0x808080);
            GuiSelectWorld.this.drawString(GuiSelectWorld.this.fontRenderer, string3, x + 2, y + 12 + 10, 0x808080);
        }
    }
}

