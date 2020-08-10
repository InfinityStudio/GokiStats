package net.infstudio.goki.common.config.stats;

import net.minecraftforge.common.ForgeConfigSpec;

public class StatConfig {
    public ForgeConfigSpec.DoubleValue costMultiplier;
    public ForgeConfigSpec.DoubleValue limitMultiplier;
    public ForgeConfigSpec.DoubleValue bonusMultiplier;

    public StatConfig(ForgeConfigSpec.Builder builder) {
        costMultiplier = builder.comment("Cost multiplier for this stat")
                .defineInRange("costMultiplier", 1d, 0d, Double.MAX_VALUE);
        limitMultiplier = builder.comment("Limit multiplier for this stat")
                .defineInRange("costMultiplier", 1d, 0d, Double.MAX_VALUE);
        bonusMultiplier = builder.comment("Bonus multiplier for this stat")
                .defineInRange("costMultiplier", 1d, 0d, Double.MAX_VALUE);
    }
}
