package net.minecraft.src;


public class Achievement
extends StatBase {
    public final int displayColumn;
    public final int displayRow;
    public final Achievement parentAchievement;
    private final String achievementDescription;
    private IStatStringFormat statStringFormatter;
    public final ItemStack theItemStack;
    private boolean isSpecial;

    public Achievement(int id, String name, int column, int row, Item icon, Achievement parent) {
        this(id, name, column, row, new ItemStack(icon), parent);
    }

    public Achievement(int id, String name, int column, int row, Block icon, Achievement parent) {
        this(id, name, column, row, new ItemStack(icon), parent);
    }

    public Achievement(int id, String name, int column, int row, ItemStack icon, Achievement parent) {
        super(0x500000 + id, StatCollector.translateToLocal("achievement." + name));
        this.theItemStack = icon;
        this.achievementDescription = StatCollector.translateToLocal("achievement." + name + ".desc");
        this.displayColumn = column;
        this.displayRow = row;
        if (column < AchievementList.minDisplayColumn) {
            AchievementList.minDisplayColumn = column;
        }
        if (row < AchievementList.minDisplayRow) {
            AchievementList.minDisplayRow = row;
        }
        if (column > AchievementList.maxDisplayColumn) {
            AchievementList.maxDisplayColumn = column;
        }
        if (row > AchievementList.maxDisplayRow) {
            AchievementList.maxDisplayRow = row;
        }
        this.parentAchievement = parent;
    }

    public Achievement setLocal() {
        this.local = true;
        return this;
    }

    public Achievement setSpecial() {
        this.isSpecial = true;
        return this;
    }

    public Achievement registerStat() {
        super.registerStat();
        AchievementList.achievementList.add(this);
        return this;
    }

    public boolean isAchievement() {
        return true;
    }

    public String getDescription() {
        if (this.statStringFormatter != null) {
            return this.statStringFormatter.formatString(this.achievementDescription);
        }
        return this.achievementDescription;
    }

    public Achievement setStatStringFormatter(IStatStringFormat formatter) {
        this.statStringFormatter = formatter;
        return this;
    }

    public boolean getSpecial() {
        return this.isSpecial;
    }
}

