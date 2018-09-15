package net.infstudio.goki.common.config;

import net.infstudio.goki.common.utils.Reference;
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

        @Config.Name("Death Loss Multiplier")
        @Config.Comment("Multiplier of levels you will lose, between 0~1.")
        public float loseStatsMultiplier = 1;

        @Config.Name("Maximum revertable skill level")
        @Config.Comment("An integer that constrains the max number of level of the skill can be reverted. -1 for no limit. 0 to disable reverting.")
        public int globalMaxRevertLevel = -1;

        @Config.Name("Revert Factor")
        @Config.Comment("How much percentage of exp will be given back to player if a player revert a skill.")
        public float globalRevertFactor = 0.8F;
    }
}
