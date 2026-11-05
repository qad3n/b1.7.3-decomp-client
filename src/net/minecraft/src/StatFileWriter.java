package net.minecraft.src;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class StatFileWriter {
    private Map<StatBase, Integer> stats = new HashMap<StatBase, Integer>();
    private Map<StatBase, Integer> unsentStats = new HashMap<StatBase, Integer>();
    private boolean dirty = false;
    private StatsSyncher statsSyncher;

    public StatFileWriter(Session session, File parentDir) {
        File file1 = new File(parentDir, "stats");
        if (!file1.exists()) {
            file1.mkdir();
        }
        for (File file2 : parentDir.listFiles()) {
            if (file2.getName().startsWith("stats_") && file2.getName().endsWith(".dat")) {
                File file3 = new File(file1, file2.getName());
                if (!file3.exists()) {
                    System.out.println("Relocating " + file2.getName());
                    file2.renameTo(file3);
                }
            }
        }
        this.statsSyncher = new StatsSyncher(session, this, file1);
    }

    public void readStat(StatBase stat, int amount) {
        this.writeStatToMap(this.unsentStats, stat, amount);
        this.writeStatToMap(this.stats, stat, amount);
        this.dirty = true;
    }

    private void writeStatToMap(Map<StatBase, Integer> values, StatBase stat, int amount) {
        Integer n = values.get(stat);
        int n2 = n == null ? 0 : n;
        values.put(stat, n2 + amount);
    }

    public Map<StatBase, Integer> getUnsent() {
        return new HashMap<StatBase, Integer>(this.unsentStats);
    }

    public void increment(Map<StatBase, Integer> amounts) {
        if (amounts == null) {
            return;
        }
        this.dirty = true;
        for (StatBase statBase : amounts.keySet()) {
            this.writeStatToMap(this.unsentStats, statBase, amounts.get(statBase));
            this.writeStatToMap(this.stats, statBase, amounts.get(statBase));
        }
    }

    public void tick(Map<StatBase, Integer> stats) {
        if (stats == null) {
            return;
        }
        for (StatBase statBase : stats.keySet()) {
            Integer n = this.unsentStats.get(statBase);
            int n2 = n == null ? 0 : n;
            this.stats.put(statBase, stats.get(statBase) + n2);
        }
    }

    public void tickUnsent(Map<StatBase, Integer> stats) {
        if (stats == null) {
            return;
        }
        this.dirty = true;
        for (StatBase statBase : stats.keySet()) {
            this.writeStatToMap(this.unsentStats, statBase, stats.get(statBase));
        }
    }

    public static Map<StatBase, Integer> deserialize(String data) {
        HashMap<StatBase, Integer> hashMap = new HashMap<StatBase, Integer>();
        try {
            String string = "local";
            StringBuilder stringBuilder = new StringBuilder();
            J_JsonRootNode j_JsonRootNode = new J_JdomParser().parse(data);
            List<J_JsonNode> list = j_JsonRootNode.getArrayNode("stats-change");
            for (J_JsonNode object2 : list) {
                Map<J_JsonStringNode, J_JsonNode> map = object2.getFields();
                Map.Entry<J_JsonStringNode, J_JsonNode> entry = map.entrySet().iterator().next();
                int n = Integer.parseInt(entry.getKey().getText());
                int n2 = Integer.parseInt(entry.getValue().getText());
                StatBase statBase = StatList.byKey(n);
                if (statBase == null) {
                    System.out.println(n + " is not a valid stat");
                    continue;
                }
                stringBuilder.append(StatList.byKey((int)n).statGuid).append(",");
                stringBuilder.append(n2).append(",");
                hashMap.put(statBase, n2);
            }
            MD5String mD5String = new MD5String(string);
            String string2 = mD5String.digest(stringBuilder.toString());
            if (!string2.equals(j_JsonRootNode.getStringValue("checksum"))) {
                System.out.println("CHECKSUM MISMATCH");
                return null;
            }
        }
        catch (J_InvalidSyntaxException j_InvalidSyntaxException) {
            j_InvalidSyntaxException.printStackTrace();
        }
        return hashMap;
    }

    public static String serialize(String playerName, String sessionId, Map<StatBase, Integer> stats) {
        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder stringBuilder2 = new StringBuilder();
        boolean bl = true;
        stringBuilder.append("{\r\n");
        if (playerName != null && sessionId != null) {
            stringBuilder.append("  \"user\":{\r\n");
            stringBuilder.append("    \"name\":\"").append(playerName).append("\",\r\n");
            stringBuilder.append("    \"sessionid\":\"").append(sessionId).append("\"\r\n");
            stringBuilder.append("  },\r\n");
        }
        stringBuilder.append("  \"stats-change\":[");
        for (StatBase statBase : stats.keySet()) {
            if (!bl) {
                stringBuilder.append("},");
            } else {
                bl = false;
            }
            stringBuilder.append("\r\n    {\"").append(statBase.statId).append("\":").append(stats.get(statBase));
            stringBuilder2.append(statBase.statGuid).append(",");
            stringBuilder2.append(stats.get(statBase)).append(",");
        }
        if (!bl) {
            stringBuilder.append("}");
        }
        MD5String mD5String = new MD5String(sessionId);
        stringBuilder.append("\r\n  ],\r\n");
        stringBuilder.append("  \"checksum\":\"").append(mD5String.digest(stringBuilder2.toString())).append("\"\r\n");
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    public boolean hasAchievementUnlocked(Achievement achievement) {
        return this.stats.containsKey(achievement);
    }

    public boolean hasParentAchievement(Achievement achievement) {
        return achievement.parentAchievement == null || this.hasAchievementUnlocked(achievement.parentAchievement);
    }

    public int writeStat(StatBase stat) {
        Integer n = this.stats.get(stat);
        return n == null ? 0 : n;
    }

    public void func_27175_b() {
    }

    public void syncStats() {
        this.statsSyncher.syncStatsFileWithMap(this.getUnsent());
    }

    public void tick() {
        if (this.dirty && this.statsSyncher.canSave()) {
            this.statsSyncher.save(this.getUnsent());
        }
        this.statsSyncher.tick();
    }
}

