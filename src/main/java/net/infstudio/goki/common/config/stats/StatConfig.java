package net.infstudio.goki.common.config.stats;

import net.minecraftforge.common.ForgeConfigSpec;

public class StatConfig {
    public ForgeConfigSpec.DoubleValue costMultiplier;
    public ForgeConfigSpec.IntValue maxLevel;
    public ForgeConfigSpec.DoubleValue bonusMultiplier;

    public StatConfig(ForgeConfigSpec.Builder builder) {
        costMultiplier = builder.comment("Cost multiplier for this stat")
                .defineInRange("costMultiplier", 1d, 0d, Double.MAX_VALUE);
        maxLevel = builder.comment("Max Level Limit for this stat, -1 for leave it as-is, 0 for disable this stat.")
                .defineInRange("maxLevel", -1, -1, Integer.MAX_VALUE);
        bonusMultiplier = builder.comment("Bonus multiplier for this stat")
                .defineInRange("bonusMultiplier", 1d, 0d, Double.MAX_VALUE);
    }
}
