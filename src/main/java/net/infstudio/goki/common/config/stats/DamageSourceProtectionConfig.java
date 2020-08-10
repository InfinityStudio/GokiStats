package net.infstudio.goki.common.config.stats;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.ArrayList;
import java.util.List;

public class DamageSourceProtectionConfig extends StatConfig {
    public List<String> damageSources = new ArrayList<>();

    public DamageSourceProtectionConfig(ForgeConfigSpec.Builder builder) {
        super(builder);
    }
}
