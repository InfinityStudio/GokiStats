package net.infstudio.goki.common.config.stats;

import net.infstudio.goki.common.stat.tool.TreasureFinderEntry;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.ArrayList;
import java.util.List;

public class TreasureFinderConfig extends StatConfig {
    public List<TreasureFinderEntry> entries = new ArrayList<>();

    public TreasureFinderConfig(ForgeConfigSpec.Builder builder) {
        super(builder);
    }
}
