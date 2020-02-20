package net.infstudio.goki.common.config;

import com.google.common.collect.Lists;
import net.infstudio.goki.common.utils.Reference;
import net.minecraftforge.common.config.Config;

import java.util.List;

@Config(modid = Reference.MODID, name = "gokistats_v2")
public class GokiConfig {
    @Config.Name("Configuration Version")
    public static String version = "v3";

    @Config.Name("Keybinding Enabled")
    @Config.Comment({"Should register gokistats keybinding (default Y)", "If set to false, player can only use /gokistats gui to open gui"})
    @Config.RequiresMcRestart
    public static boolean keyBindingEnabled = true;

    @Config.Name("Initiative stat synchronization")
    @Config.Comment("Enables synchronizing all stat data in a period")
    public static boolean initiativeSync = false;

    @Config.Name("Ticks for initiative sync")
    @Config.Comment("Default 400 Ticks (20s)")
    @Config.RangeInt(min = 20)
    public static int syncTicks = 400;

    public static List<String> treasureFinderLootTables = Lists.newArrayList(
            "ore:dirt|gokistats:treasure_finder/dirt", "ore:treeLeaves|gokistats:treasure_finder/leaves",
            "minecraft:tallgrass|gokistats:treasure_finder/tallgrass");

    public static List<String> miningMagicianLootTables = Lists.newArrayList(
            "ore:dirt|gokistats:treasure_finder/dirt", "ore:treeLeaves|gokistats:treasure_finder/leaves",
            "minecraft:tallgrass|gokistats:treasure_finder/tallgrass");

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
        @Config.Comment("A flat multiplier on the amount limit of all stats.")
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

        @Config.Name("Maximum revertable skill amount")
        @Config.Comment("An integer that constrains the max number of amount of the skill can be reverted. -1 for no limit. 0 to disable reverting.")
        public int globalMaxRevertLevel = -1;

        @Config.Name("Revert Factor")
        @Config.Comment("How much percentage of exp will be given back to player if a player revert a skill.")
        public float globalRevertFactor = 0.8F;
    }
}
