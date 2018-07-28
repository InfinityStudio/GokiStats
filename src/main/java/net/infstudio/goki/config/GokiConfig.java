package net.infstudio.goki.config;

import net.infstudio.goki.lib.Reference;
import net.minecraftforge.common.config.Config;

@Config(modid = Reference.MODID, name = "gokistats_v2")
public class GokiConfig {
    @Config.Name("Configuration Version")
    public static String version = "v3";

    @Config.Name("Global Modifiers")
    public static GlobalModifiers globalModifiers = new GlobalModifiers();

    @Config.Name("Support")
    public static Support support = new Support();

    public static class Support {
        @Config.Name("Reaper Limit")
        public float reaperLimit = 20;
    }

    public static class GlobalModifiers {
        @Config.Name("Cost Multiplier")
        @Config.Comment("A flat multiplier on the cost to upgrade all stats.")
        public float globalCostMultiplier = 1;

        @Config.Name("Limit Multiplier")
        @Config.Comment("A flat multiplier on the level limit of all stats.")
        public float globalLimitMultiplier = 2.5f;

        @Config.Name("Bonus Multiplier")
        @Config.Comment("A flat multiplier on the bonus all stats gives.")
        public float globalBonusMultiplier = 1;

        @Config.Name("Death Loss")
        @Config.Comment("Lose stats on death?")
        public boolean loseStatsOnDeath = false;
    }
}
