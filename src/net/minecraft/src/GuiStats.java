package net.minecraft.src;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class GuiStats extends GuiScreen {
    private static RenderItem itemRenderer = new RenderItem();
    protected GuiScreen parent;
    protected String title = "Select world";
    private GuiStats.GuiSlotStatsGeneral generalStats;
    private GuiStats.GuiSlotStatsItem itemStats;
    private GuiStats.GuiSlotStatsBlock blockStats;
    private StatFileWriter stats;
    private GuiSlot selectedStatsList = null;

    public GuiStats(GuiScreen parent, StatFileWriter stats) {
        this.parent = parent;
        this.stats = stats;
    }

    @Override
    public void initGui() {
        this.title = StatCollector.translateToLocal("gui.stats");
        this.generalStats = new GuiStats.GuiSlotStatsGeneral();
        this.generalStats.registerScrollButtons(this.controlList, 1, 1);
        this.itemStats = new GuiStats.GuiSlotStatsItem();
        this.itemStats.registerScrollButtons(this.controlList, 1, 1);
        this.blockStats = new GuiStats.GuiSlotStatsBlock();
        this.blockStats.registerScrollButtons(this.controlList, 1, 1);
        this.selectedStatsList = this.generalStats;
        this.createButtons();
    }

    public void createButtons() {
        StringTranslate stringtranslate = StringTranslate.getInstance();
        this.controlList.add(new GuiButton(0, this.width / 2 + 4, this.height - 28, 150, 20, stringtranslate.translateKey("gui.done")));
        this.controlList.add(new GuiButton(1, this.width / 2 - 154, this.height - 52, 100, 20, stringtranslate.translateKey("stat.generalButton")));
        GuiButton guibutton;
        this.controlList.add(guibutton = new GuiButton(2, this.width / 2 - 46, this.height - 52, 100, 20, stringtranslate.translateKey("stat.blocksButton")));
        GuiButton guibutton1;
        this.controlList.add(guibutton1 = new GuiButton(3, this.width / 2 + 62, this.height - 52, 100, 20, stringtranslate.translateKey("stat.itemsButton")));
        if (this.blockStats.getSize() == 0) {
            guibutton.enabled = false;
        }

        if (this.itemStats.getSize() == 0) {
            guibutton1.enabled = false;
        }
    }

    protected void actionPerformed(GuiButton button) {
        if (!button.enabled) {
            return;
        }
        if (button.id == 0) {
            this.mc.displayGuiScreen(this.parent);
        } else if (button.id == 1) {
            this.selectedStatsList = this.generalStats;
        } else if (button.id == 3) {
            this.selectedStatsList = this.itemStats;
        } else if (button.id == 2) {
            this.selectedStatsList = this.blockStats;
        } else {
            this.selectedStatsList.actionPerformed(button);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float tickDelta) {
        this.selectedStatsList.drawScreen(mouseX, mouseY, tickDelta);
        this.drawCenteredString(this.fontRenderer, this.title, this.width / 2, 20, 16777215);
        super.drawScreen(mouseX, mouseY, tickDelta);
    }

    private void renderStatIcon(int x, int y, int icon) {
        this.renderSlot(x + 1, y + 1);
        GL11.glEnable(32826);
        GL11.glPushMatrix();
        GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GL11.glPopMatrix();
        itemRenderer.drawItemIntoGui(this.fontRenderer, this.mc.renderEngine, icon, 0, Item.itemsList[icon].getIconFromDamage(0), x + 2, y + 2);
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(32826);
    }

    private void renderSlot(int x, int y) {
        this.renderSlot(x, y, 0, 0);
    }

    private void renderSlot(int x, int y, int u, int v) {
        int i = this.mc.renderEngine.getTexture("/gui/slot.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(i);
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(x + 0, y + 18, this.zLevel, (u + 0) * 0.0078125F, (v + 18) * 0.0078125F);
        tessellator.addVertexWithUV(x + 18, y + 18, this.zLevel, (u + 18) * 0.0078125F, (v + 18) * 0.0078125F);
        tessellator.addVertexWithUV(x + 18, y + 0, this.zLevel, (u + 18) * 0.0078125F, (v + 0) * 0.0078125F);
        tessellator.addVertexWithUV(x + 0, y + 0, this.zLevel, (u + 0) * 0.0078125F, (v + 0) * 0.0078125F);
        tessellator.draw();
    }

    abstract class GuiSlotStats extends GuiSlot {
        protected int clickedIconId = -1;
        protected List<StatCrafting> entries;
        protected Comparator<StatCrafting> statComparator;
        protected int selectedTab = -1;
        protected int statSortOrder = 0;

        protected GuiSlotStats() {
            super(GuiStats.this.mc, GuiStats.this.width, GuiStats.this.height, 32, GuiStats.this.height - 64, 20);
            this.setRenderSelectionHighlight(false);
            this.setHeader(true, 20);
        }

        @Override
        protected void elementClicked(int index, boolean doubleClick) {
        }

        @Override
        protected boolean isSelected(int index) {
            return false;
        }

        @Override
        protected void drawBackground() {
            GuiStats.this.drawDefaultBackground();
        }

        @Override
        protected void renderHeader(int x, int y, Tessellator tesselator) {
            if (!Mouse.isButtonDown(0)) {
                this.clickedIconId = -1;
            }

            if (this.clickedIconId == 0) {
                GuiStats.this.renderSlot(x + 115 - 18, y + 1, 0, 0);
            } else {
                GuiStats.this.renderSlot(x + 115 - 18, y + 1, 0, 18);
            }

            if (this.clickedIconId == 1) {
                GuiStats.this.renderSlot(x + 165 - 18, y + 1, 0, 0);
            } else {
                GuiStats.this.renderSlot(x + 165 - 18, y + 1, 0, 18);
            }

            if (this.clickedIconId == 2) {
                GuiStats.this.renderSlot(x + 215 - 18, y + 1, 0, 0);
            } else {
                GuiStats.this.renderSlot(x + 215 - 18, y + 1, 0, 18);
            }

            if (this.selectedTab != -1) {
                short short1 = 79;
                byte b0 = 18;
                if (this.selectedTab == 1) {
                    short1 = 129;
                } else if (this.selectedTab == 2) {
                    short1 = 179;
                }

                if (this.statSortOrder == 1) {
                    b0 = 36;
                }

                GuiStats.this.renderSlot(x + short1, y + 1, b0, 0);
            }
        }

        @Override
        protected void headerClicked(int x, int y) {
            this.clickedIconId = -1;
            if (x >= 79 && x < 115) {
                this.clickedIconId = 0;
            } else if (x >= 129 && x < 165) {
                this.clickedIconId = 1;
            } else if (x >= 179 && x < 215) {
                this.clickedIconId = 2;
            }

            if (this.clickedIconId >= 0) {
                this.click(this.clickedIconId);
                GuiStats.this.mc.sndManager.playSoundFX("random.click", 1.0F, 1.0F);
            }
        }

        @Override
        protected final int getSize() {
            return this.entries.size();
        }

        protected final StatCrafting getEntry(int index) {
            return this.entries.get(index);
        }

        protected abstract String getColumnHeader(int column);

        protected void renderStat(StatCrafting stat, int x, int y, boolean isRowEven) {
            if (stat != null) {
                String s = stat.format(GuiStats.this.stats.writeStat(stat));
                GuiStats.this.drawString(GuiStats.this.fontRenderer, s, x - GuiStats.this.fontRenderer.getStringWidth(s), y + 5, isRowEven ? 16777215 : 9474192);
            } else {
                String s1 = "-";
                GuiStats.this.drawString(
                    GuiStats.this.fontRenderer, s1, x - GuiStats.this.fontRenderer.getStringWidth(s1), y + 5, isRowEven ? 16777215 : 9474192
                );
            }
        }

        @Override
        protected void renderDecorations(int mouseX, int mouseY) {
            if (mouseY >= this.top && mouseY <= this.bottom) {
                int i = this.getEntryAt(mouseX, mouseY);
                int j = GuiStats.this.width / 2 - 92 - 16;
                if (i >= 0) {
                    if (mouseX < j + 40 || mouseX > j + 40 + 20) {
                        return;
                    }

                    StatCrafting statcrafting = this.getEntry(i);
                    this.renderStat(statcrafting, mouseX, mouseY);
                } else {
                    String s = "";
                    if (mouseX >= j + 115 - 18 && mouseX <= j + 115) {
                        s = this.getColumnHeader(0);
                    } else if (mouseX >= j + 165 - 18 && mouseX <= j + 165) {
                        s = this.getColumnHeader(1);
                    } else {
                        if (mouseX < j + 215 - 18 || mouseX > j + 215) {
                            return;
                        }

                        s = this.getColumnHeader(2);
                    }

                    s = ("" + StringTranslate.getInstance().translateKey(s)).trim();
                    if (s.length() > 0) {
                        int k = mouseX + 12;
                        int l = mouseY - 12;
                        int i1 = GuiStats.this.fontRenderer.getStringWidth(s);
                        GuiStats.this.drawGradientRect(k - 3, l - 3, k + i1 + 3, l + 8 + 3, -1073741824, -1073741824);
                        GuiStats.this.fontRenderer.drawStringWithShadow(s, k, l, -1);
                    }
                }
            }
        }

        protected void renderStat(StatCrafting stat, int x, int z) {
            if (stat != null) {
                Item item = Item.itemsList[stat.getItem()];
                String s = ("" + StringTranslate.getInstance().translateNamedKey(item.getItemName())).trim();
                if (s.length() > 0) {
                    int i = x + 12;
                    int j = z - 12;
                    int k = GuiStats.this.fontRenderer.getStringWidth(s);
                    GuiStats.this.drawGradientRect(i - 3, j - 3, i + k + 3, j + 8 + 3, -1073741824, -1073741824);
                    GuiStats.this.fontRenderer.drawStringWithShadow(s, i, j, -1);
                }
            }
        }

        protected void click(int buttonDownTime) {
            if (buttonDownTime != this.selectedTab) {
                this.selectedTab = buttonDownTime;
                this.statSortOrder = -1;
            } else if (this.statSortOrder == -1) {
                this.statSortOrder = 1;
            } else {
                this.selectedTab = -1;
                this.statSortOrder = 0;
            }

            Collections.sort(this.entries, this.statComparator);
        }
    }

    class GuiSlotStatsBlock extends GuiStats.GuiSlotStats {
        public GuiSlotStatsBlock() {
            this.entries = new ArrayList<>();

            for (StatCrafting statcrafting : StatList.MINED) {
                boolean flag = false;
                int i = statcrafting.getItem();
                if (GuiStats.this.stats.writeStat(statcrafting) > 0) {
                    flag = true;
                } else if (StatList.ITEMS_USED[i] != null && GuiStats.this.stats.writeStat(StatList.ITEMS_USED[i]) > 0) {
                    flag = true;
                } else if (StatList.ITEMS_CRAFTED[i] != null && GuiStats.this.stats.writeStat(StatList.ITEMS_CRAFTED[i]) > 0) {
                    flag = true;
                }

                if (flag) {
                    this.entries.add(statcrafting);
                }
            }

            this.statComparator = new Comparator<StatCrafting>() {
                public int compare(StatCrafting var1, StatCrafting var2) {
                    int j = var1.getItem();
                    int k = var2.getItem();
                    StatBase statbase = null;
                    StatBase statbase1 = null;
                    if (GuiSlotStatsBlock.this.selectedTab == 2) {
                        statbase = StatList.mineBlockStatArray[j];
                        statbase1 = StatList.mineBlockStatArray[k];
                    } else if (GuiSlotStatsBlock.this.selectedTab == 0) {
                        statbase = StatList.ITEMS_CRAFTED[j];
                        statbase1 = StatList.ITEMS_CRAFTED[k];
                    } else if (GuiSlotStatsBlock.this.selectedTab == 1) {
                        statbase = StatList.ITEMS_USED[j];
                        statbase1 = StatList.ITEMS_USED[k];
                    }

                    if (statbase != null || statbase1 != null) {
                        if (statbase == null) {
                            return 1;
                        }

                        if (statbase1 == null) {
                            return -1;
                        }

                        int l = GuiStats.this.stats.writeStat(statbase);
                        int i1 = GuiStats.this.stats.writeStat(statbase1);
                        if (l != i1) {
                            return (l - i1) * GuiSlotStatsBlock.this.statSortOrder;
                        }
                    }

                    return j - k;
                }
            };
        }

        @Override
        protected void renderHeader(int x, int y, Tessellator tesselator) {
            super.renderHeader(x, y, tesselator);
            if (this.clickedIconId == 0) {
                GuiStats.this.renderSlot(x + 115 - 18 + 1, y + 1 + 1, 18, 18);
            } else {
                GuiStats.this.renderSlot(x + 115 - 18, y + 1, 18, 18);
            }

            if (this.clickedIconId == 1) {
                GuiStats.this.renderSlot(x + 165 - 18 + 1, y + 1 + 1, 36, 18);
            } else {
                GuiStats.this.renderSlot(x + 165 - 18, y + 1, 36, 18);
            }

            if (this.clickedIconId == 2) {
                GuiStats.this.renderSlot(x + 215 - 18 + 1, y + 1 + 1, 54, 18);
            } else {
                GuiStats.this.renderSlot(x + 215 - 18, y + 1, 54, 18);
            }
        }

        @Override
        protected void drawSlot(int index, int x, int y, int height, Tessellator tesselator) {
            StatCrafting statcrafting = this.getEntry(index);
            int i = statcrafting.getItem();
            GuiStats.this.renderStatIcon(x + 40, y, i);
            this.renderStat((StatCrafting)StatList.ITEMS_CRAFTED[i], x + 115, y, index % 2 == 0);
            this.renderStat((StatCrafting)StatList.ITEMS_USED[i], x + 165, y, index % 2 == 0);
            this.renderStat(statcrafting, x + 215, y, index % 2 == 0);
        }

        protected String getColumnHeader(int column) {
            if (column == 0) {
                return "stat.crafted";
            }
            if (column == 1) {
                return "stat.used";
            }
            return "stat.mined";
        }
    }

    class GuiSlotStatsGeneral extends GuiSlot {
        public GuiSlotStatsGeneral() {
            super(GuiStats.this.mc, GuiStats.this.width, GuiStats.this.height, 32, GuiStats.this.height - 64, 10);
            this.setRenderSelectionHighlight(false);
        }

        @Override
        protected int getSize() {
            return StatList.GENERAL.size();
        }

        @Override
        protected void elementClicked(int index, boolean doubleClick) {
        }

        @Override
        protected boolean isSelected(int index) {
            return false;
        }

        @Override
        protected int getContentHeight() {
            return this.getSize() * 10;
        }

        @Override
        protected void drawBackground() {
            GuiStats.this.drawDefaultBackground();
        }

        @Override
        protected void drawSlot(int index, int x, int y, int height, Tessellator tesselator) {
            StatBase statbase = StatList.GENERAL.get(index);
            GuiStats.this.drawString(GuiStats.this.fontRenderer, statbase.statName, x + 2, y + 1, index % 2 == 0 ? 16777215 : 9474192);
            String s = statbase.format(GuiStats.this.stats.writeStat(statbase));
            GuiStats.this.drawString(
                GuiStats.this.fontRenderer, s, x + 2 + 213 - GuiStats.this.fontRenderer.getStringWidth(s), y + 1, index % 2 == 0 ? 16777215 : 9474192
            );
        }
    }

    class GuiSlotStatsItem extends GuiStats.GuiSlotStats {
        public GuiSlotStatsItem() {
            this.entries = new ArrayList<>();

            for (StatCrafting statcrafting : StatList.USED) {
                boolean flag = false;
                int i = statcrafting.getItem();
                if (GuiStats.this.stats.writeStat(statcrafting) > 0) {
                    flag = true;
                } else if (StatList.ITEMS_BROKEN[i] != null && GuiStats.this.stats.writeStat(StatList.ITEMS_BROKEN[i]) > 0) {
                    flag = true;
                } else if (StatList.ITEMS_CRAFTED[i] != null && GuiStats.this.stats.writeStat(StatList.ITEMS_CRAFTED[i]) > 0) {
                    flag = true;
                }

                if (flag) {
                    this.entries.add(statcrafting);
                }
            }

            this.statComparator = new Comparator<StatCrafting>() {
                public int compare(StatCrafting var1, StatCrafting var2) {
                    int j = var1.getItem();
                    int k = var2.getItem();
                    StatBase statbase = null;
                    StatBase statbase1 = null;
                    if (GuiSlotStatsItem.this.selectedTab == 0) {
                        statbase = StatList.ITEMS_BROKEN[j];
                        statbase1 = StatList.ITEMS_BROKEN[k];
                    } else if (GuiSlotStatsItem.this.selectedTab == 1) {
                        statbase = StatList.ITEMS_CRAFTED[j];
                        statbase1 = StatList.ITEMS_CRAFTED[k];
                    } else if (GuiSlotStatsItem.this.selectedTab == 2) {
                        statbase = StatList.ITEMS_USED[j];
                        statbase1 = StatList.ITEMS_USED[k];
                    }

                    if (statbase != null || statbase1 != null) {
                        if (statbase == null) {
                            return 1;
                        }

                        if (statbase1 == null) {
                            return -1;
                        }

                        int l = GuiStats.this.stats.writeStat(statbase);
                        int i1 = GuiStats.this.stats.writeStat(statbase1);
                        if (l != i1) {
                            return (l - i1) * GuiSlotStatsItem.this.statSortOrder;
                        }
                    }

                    return j - k;
                }
            };
        }

        @Override
        protected void renderHeader(int x, int y, Tessellator tesselator) {
            super.renderHeader(x, y, tesselator);
            if (this.clickedIconId == 0) {
                GuiStats.this.renderSlot(x + 115 - 18 + 1, y + 1 + 1, 72, 18);
            } else {
                GuiStats.this.renderSlot(x + 115 - 18, y + 1, 72, 18);
            }

            if (this.clickedIconId == 1) {
                GuiStats.this.renderSlot(x + 165 - 18 + 1, y + 1 + 1, 18, 18);
            } else {
                GuiStats.this.renderSlot(x + 165 - 18, y + 1, 18, 18);
            }

            if (this.clickedIconId == 2) {
                GuiStats.this.renderSlot(x + 215 - 18 + 1, y + 1 + 1, 36, 18);
            } else {
                GuiStats.this.renderSlot(x + 215 - 18, y + 1, 36, 18);
            }
        }

        @Override
        protected void drawSlot(int index, int x, int y, int height, Tessellator tesselator) {
            StatCrafting statcrafting = this.getEntry(index);
            int i = statcrafting.getItem();
            GuiStats.this.renderStatIcon(x + 40, y, i);
            this.renderStat((StatCrafting)StatList.ITEMS_BROKEN[i], x + 115, y, index % 2 == 0);
            this.renderStat((StatCrafting)StatList.ITEMS_CRAFTED[i], x + 165, y, index % 2 == 0);
            this.renderStat(statcrafting, x + 215, y, index % 2 == 0);
        }

        protected String getColumnHeader(int column) {
            if (column == 1) {
                return "stat.crafted";
            }
            if (column == 2) {
                return "stat.used";
            }
            return "stat.depleted";
        }
    }
}
