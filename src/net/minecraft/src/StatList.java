package net.minecraft.src;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class StatList {
    protected static Map<Integer, StatBase> BY_ID = new HashMap<>();
    public static List<StatBase> ALL = new ArrayList<>();
    public static List<StatBase> GENERAL = new ArrayList<>();
    public static List<StatCrafting> USED = new ArrayList<>();
    public static List<StatCrafting> MINED = new ArrayList<>();
    public static StatBase startGameStat = new StatBasic(1000, StatCollector.translateToLocal("stat.startGame")).setLocal().registerStat();
    public static StatBase createWorldStat = new StatBasic(1001, StatCollector.translateToLocal("stat.createWorld")).setLocal().registerStat();
    public static StatBase loadWorldStat = new StatBasic(1002, StatCollector.translateToLocal("stat.loadWorld")).setLocal().registerStat();
    public static StatBase joinMultiplayerStat = new StatBasic(1003, StatCollector.translateToLocal("stat.joinMultiplayer")).setLocal().registerStat();
    public static StatBase leaveGameStat = new StatBasic(1004, StatCollector.translateToLocal("stat.leaveGame")).setLocal().registerStat();
    public static StatBase minutesPlayedStat = new StatBasic(1100, StatCollector.translateToLocal("stat.playOneMinute"), StatBase.TIME_FORMATTER)
        .setLocal()
        .registerStat();
    public static StatBase distanceWalkedStat = new StatBasic(2000, StatCollector.translateToLocal("stat.walkOneCm"), StatBase.DISTANCE_FORMATTER)
        .setLocal()
        .registerStat();
    public static StatBase distanceSwumStat = new StatBasic(2001, StatCollector.translateToLocal("stat.swimOneCm"), StatBase.DISTANCE_FORMATTER)
        .setLocal()
        .registerStat();
    public static StatBase distanceFallenStat = new StatBasic(2002, StatCollector.translateToLocal("stat.fallOneCm"), StatBase.DISTANCE_FORMATTER)
        .setLocal()
        .registerStat();
    public static StatBase distanceClimbedStat = new StatBasic(2003, StatCollector.translateToLocal("stat.climbOneCm"), StatBase.DISTANCE_FORMATTER)
        .setLocal()
        .registerStat();
    public static StatBase distanceFlownStat = new StatBasic(2004, StatCollector.translateToLocal("stat.flyOneCm"), StatBase.DISTANCE_FORMATTER)
        .setLocal()
        .registerStat();
    public static StatBase distanceDoveStat = new StatBasic(2005, StatCollector.translateToLocal("stat.diveOneCm"), StatBase.DISTANCE_FORMATTER)
        .setLocal()
        .registerStat();
    public static StatBase distanceByMinecartStat = new StatBasic(2006, StatCollector.translateToLocal("stat.minecartOneCm"), StatBase.DISTANCE_FORMATTER)
        .setLocal()
        .registerStat();
    public static StatBase distanceByBoatStat = new StatBasic(2007, StatCollector.translateToLocal("stat.boatOneCm"), StatBase.DISTANCE_FORMATTER)
        .setLocal()
        .registerStat();
    public static StatBase distanceByPigStat = new StatBasic(2008, StatCollector.translateToLocal("stat.pigOneCm"), StatBase.DISTANCE_FORMATTER)
        .setLocal()
        .registerStat();
    public static StatBase jumpStat = new StatBasic(2010, StatCollector.translateToLocal("stat.jump")).setLocal().registerStat();
    public static StatBase dropStat = new StatBasic(2011, StatCollector.translateToLocal("stat.drop")).setLocal().registerStat();
    public static StatBase damageDealtStat = new StatBasic(2020, StatCollector.translateToLocal("stat.damageDealt")).registerStat();
    public static StatBase damageTakenStat = new StatBasic(2021, StatCollector.translateToLocal("stat.damageTaken")).registerStat();
    public static StatBase deathsStat = new StatBasic(2022, StatCollector.translateToLocal("stat.deaths")).registerStat();
    public static StatBase mobKillsStat = new StatBasic(2023, StatCollector.translateToLocal("stat.mobKills")).registerStat();
    public static StatBase playerKillsStat = new StatBasic(2024, StatCollector.translateToLocal("stat.playerKills")).registerStat();
    public static StatBase fishCaughtStat = new StatBasic(2025, StatCollector.translateToLocal("stat.fishCaught")).registerStat();
    public static StatBase[] mineBlockStatArray = initBlocksMinedStats("stat.mineBlock", 16777216);
    public static StatBase[] ITEMS_CRAFTED;
    public static StatBase[] ITEMS_USED;
    public static StatBase[] ITEMS_BROKEN;

    public static void init() {
    }

    public static void initBlockStats() {
        ITEMS_USED = initItemsUsedStats(ITEMS_USED, "stat.useItem", 16908288, 0, Block.blocksList.length);
        ITEMS_BROKEN = initItemsBrokenStats(ITEMS_BROKEN, "stat.breakItem", 16973824, 0, Block.blocksList.length);
        hasBlockStats = true;
        initItemsCraftedStats();
    }

    public static void initItemStats() {
        ITEMS_USED = initItemsUsedStats(ITEMS_USED, "stat.useItem", 16908288, Block.blocksList.length, 32000);
        ITEMS_BROKEN = initItemsBrokenStats(ITEMS_BROKEN, "stat.breakItem", 16973824, Block.blocksList.length, 32000);
        hasItemStats = true;
        initItemsCraftedStats();
    }

    public static void initItemsCraftedStats() {
        if (hasBlockStats && hasItemStats) {
            HashSet<Integer> hashset = new HashSet<>();

            for (IRecipe irecipe : CraftingManager.getInstance().getRecipeList()) {
                hashset.add(irecipe.getRecipeOutput().itemID);
            }

            for (ItemStack itemstack : FurnaceRecipes.smelting().getSmeltingList().values()) {
                hashset.add(itemstack.itemID);
            }

            ITEMS_CRAFTED = new StatBase[32000];

            for (Integer integer : hashset) {
                if (Item.itemsList[integer] != null) {
                    String s = StatCollector.translateToLocalFormatted("stat.craftItem", Item.itemsList[integer].getStatName());
                    ITEMS_CRAFTED[integer] = new StatCrafting(16842752 + integer, s, integer).registerStat();
                }
            }

            replaceAllSimilarBlocks(ITEMS_CRAFTED);
        }
    }

    private static StatBase[] initBlocksMinedStats(String name, int id) {
        StatBase[] astatbase = new StatBase[256];

        for (int i = 0; i < 256; i++) {
            if (Block.blocksList[i] != null && Block.blocksList[i].getEnableStats()) {
                String s = StatCollector.translateToLocalFormatted(name, Block.blocksList[i].translateBlockName());
                astatbase[i] = new StatCrafting(id + i, s, i).registerStat();
                MINED.add((StatCrafting)astatbase[i]);
            }
        }

        replaceAllSimilarBlocks(astatbase);
        return astatbase;
    }

    private static StatBase[] initItemsUsedStats(StatBase[] stats, String name, int id, int minId, int maxId) {
        if (stats == null) {
            stats = new StatBase[32000];
        }

        for (int i = minId; i < maxId; i++) {
            if (Item.itemsList[i] != null) {
                String s = StatCollector.translateToLocalFormatted(name, Item.itemsList[i].getStatName());
                stats[i] = new StatCrafting(id + i, s, i).registerStat();
                if (i >= Block.blocksList.length) {
                    USED.add((StatCrafting)stats[i]);
                }
            }
        }

        replaceAllSimilarBlocks(stats);
        return stats;
    }

    private static StatBase[] initItemsBrokenStats(StatBase[] stats, String name, int id, int minId, int maxId) {
        if (stats == null) {
            stats = new StatBase[32000];
        }

        for (int i = minId; i < maxId; i++) {
            if (Item.itemsList[i] != null && Item.itemsList[i].isDamagable()) {
                String s = StatCollector.translateToLocalFormatted(name, Item.itemsList[i].getStatName());
                stats[i] = new StatCrafting(id + i, s, i).registerStat();
            }
        }

        replaceAllSimilarBlocks(stats);
        return stats;
    }

    private static void replaceAllSimilarBlocks(StatBase[] stats) {
        replaceSimilarBlocks(stats, Block.waterStill.blockID, Block.waterMoving.blockID);
        replaceSimilarBlocks(stats, Block.lavaStill.blockID, Block.lavaStill.blockID);
        replaceSimilarBlocks(stats, Block.pumpkinLantern.blockID, Block.pumpkin.blockID);
        replaceSimilarBlocks(stats, Block.stoneOvenActive.blockID, Block.stoneOvenIdle.blockID);
        replaceSimilarBlocks(stats, Block.oreRedstoneGlowing.blockID, Block.oreRedstone.blockID);
        replaceSimilarBlocks(stats, Block.redstoneRepeaterActive.blockID, Block.redstoneRepeaterIdle.blockID);
        replaceSimilarBlocks(stats, Block.torchRedstoneActive.blockID, Block.torchRedstoneIdle.blockID);
        replaceSimilarBlocks(stats, Block.mushroomRed.blockID, Block.mushroomBrown.blockID);
        replaceSimilarBlocks(stats, Block.stairDouble.blockID, Block.stairSingle.blockID);
        replaceSimilarBlocks(stats, Block.grass.blockID, Block.dirt.blockID);
        replaceSimilarBlocks(stats, Block.tilledField.blockID, Block.dirt.blockID);
    }

    private static void replaceSimilarBlocks(StatBase[] stats, int block1, int block2) {
        if (stats[block1] != null && stats[block2] == null) {
            stats[block2] = stats[block1];
            return;
        }
        ALL.remove(stats[block1]);
        MINED.remove(stats[block1]);
        GENERAL.remove(stats[block1]);
        stats[block1] = stats[block2];
    }

    public static StatBase byKey(int id) {
        return BY_ID.get(id);
    }

    static {
        AchievementList.init();
    }

    private static boolean hasBlockStats = false;
    private static boolean hasItemStats = false;
}
