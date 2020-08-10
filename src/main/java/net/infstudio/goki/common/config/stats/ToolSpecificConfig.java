package net.infstudio.goki.common.config.stats;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Collections;
import java.util.List;

public class ToolSpecificConfig extends StatConfig {
    public final ForgeConfigSpec.ConfigValue<List<?>> supports;

    public ToolSpecificConfig(ForgeConfigSpec.Builder builder) {
        super(builder);
        supports = builder.comment("Support item in resource location format, like minecraft:iron_axe")
                .defineList("supports", Collections.emptyList(), str -> ForgeRegistries.ITEMS.containsKey(new ResourceLocation(str.toString())));
    }
}
