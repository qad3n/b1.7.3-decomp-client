package net.minecraft.src;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class StatBase {
    public final int statId;
    public final String statName;
    public boolean local = false;
    public String statGuid;
    private final IStatType formatter;
    private static NumberFormat NUMBER_FORMAT = NumberFormat.getIntegerInstance(Locale.US);
    public static IStatType NUMBER_FORMATTER = new IStatType(){

        public String format(int value) {
            return NUMBER_FORMAT.format(value);
        }
    };
    private static DecimalFormat DECIMAL_FORMAT = new DecimalFormat("########0.00");
    public static IStatType TIME_FORMATTER = new IStatType(){

        public String format(int value) {
            double d = (double)value / 20.0;
            double d2 = d / 60.0;
            double d3 = d2 / 60.0;
            double d4 = d3 / 24.0;
            double d5 = d4 / 365.0;
            if (d5 > 0.5) {
                return DECIMAL_FORMAT.format(d5) + " y";
            }
            if (d4 > 0.5) {
                return DECIMAL_FORMAT.format(d4) + " d";
            }
            if (d3 > 0.5) {
                return DECIMAL_FORMAT.format(d3) + " h";
            }
            if (d2 > 0.5) {
                return DECIMAL_FORMAT.format(d2) + " m";
            }
            return d + " s";
        }
    };
    public static IStatType DISTANCE_FORMATTER = new IStatType(){

        public String format(int value) {
            int n = value;
            double d = (double)n / 100.0;
            double d2 = d / 1000.0;
            if (d2 > 0.5) {
                return DECIMAL_FORMAT.format(d2) + " km";
            }
            if (d > 0.5) {
                return DECIMAL_FORMAT.format(d) + " m";
            }
            return value + " cm";
        }
    };

    public StatBase(int id, String key, IStatType formatter) {
        this.statId = id;
        this.statName = key;
        this.formatter = formatter;
    }

    public StatBase(int id, String key) {
        this(id, key, NUMBER_FORMATTER);
    }

    public StatBase setLocal() {
        this.local = true;
        return this;
    }

    public StatBase registerStat() {
        if (StatList.BY_ID.containsKey(this.statId)) {
            throw new RuntimeException("Duplicate stat id: \"" + StatList.BY_ID.get((Object)Integer.valueOf((int)this.statId)).statName + "\" and \"" + this.statName + "\" at id " + this.statId);
        }
        StatList.ALL.add(this);
        StatList.BY_ID.put(this.statId, this);
        this.statGuid = AchievementMap.getGuid(this.statId);
        return this;
    }

    public boolean isAchievement() {
        return false;
    }

    public String format(int value) {
        return this.formatter.format(value);
    }

    public String toString() {
        return this.statName;
    }
}

