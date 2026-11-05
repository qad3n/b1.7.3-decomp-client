package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;

public class AchievementList {
    public static int minDisplayColumn;
    public static int minDisplayRow;
    public static int maxDisplayColumn;
    public static int maxDisplayRow;
    public static List<Achievement> achievementList;
    public static Achievement openInventory;
    public static Achievement mineWood;
    public static Achievement buildWorkBench;
    public static Achievement buildPickaxe;
    public static Achievement buildFurnace;
    public static Achievement acquireIron;
    public static Achievement buildHoe;
    public static Achievement makeBread;
    public static Achievement bakeCake;
    public static Achievement buildBetterPickaxe;
    public static Achievement cookFish;
    public static Achievement onARail;
    public static Achievement buildSword;
    public static Achievement killEnemy;
    public static Achievement killCow;
    public static Achievement flyPig;

    public static void init() {
    }

    static {
        achievementList = new ArrayList<Achievement>();
        openInventory = new Achievement(0, "openInventory", 0, 0, Item.book, null).setLocal().registerStat();
        mineWood = new Achievement(1, "mineWood", 2, 1, Block.wood, openInventory).registerStat();
        buildWorkBench = new Achievement(2, "buildWorkBench", 4, -1, Block.workbench, mineWood).registerStat();
        buildPickaxe = new Achievement(3, "buildPickaxe", 4, 2, Item.pickaxeWood, buildWorkBench).registerStat();
        buildFurnace = new Achievement(4, "buildFurnace", 3, 4, Block.stoneOvenActive, buildPickaxe).registerStat();
        acquireIron = new Achievement(5, "acquireIron", 1, 4, Item.ingotIron, buildFurnace).registerStat();
        buildHoe = new Achievement(6, "buildHoe", 2, -3, Item.hoeWood, buildWorkBench).registerStat();
        makeBread = new Achievement(7, "makeBread", -1, -3, Item.bread, buildHoe).registerStat();
        bakeCake = new Achievement(8, "bakeCake", 0, -5, Item.cake, buildHoe).registerStat();
        buildBetterPickaxe = new Achievement(9, "buildBetterPickaxe", 6, 2, Item.pickaxeStone, buildPickaxe).registerStat();
        cookFish = new Achievement(10, "cookFish", 2, 6, Item.fishCooked, buildFurnace).registerStat();
        onARail = new Achievement(11, "onARail", 2, 3, Block.rail, acquireIron).setSpecial().registerStat();
        buildSword = new Achievement(12, "buildSword", 6, -1, Item.swordWood, buildWorkBench).registerStat();
        killEnemy = new Achievement(13, "killEnemy", 8, -1, Item.bone, buildSword).registerStat();
        killCow = new Achievement(14, "killCow", 7, -3, Item.leather, buildSword).registerStat();
        flyPig = new Achievement(15, "flyPig", 8, -4, Item.saddle, killCow).setSpecial().registerStat();
        System.out.println(achievementList.size() + " achievements");
    }
}

