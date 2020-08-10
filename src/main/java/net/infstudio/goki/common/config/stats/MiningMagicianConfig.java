package net.infstudio.goki.common.config.stats;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.ArrayList;
import java.util.List;

public class MiningMagicianConfig extends StatConfig {
    public List<String> blockEntries = new ArrayList<>();
    public List<String> itemEntries = new ArrayList<>();


    public MiningMagicianConfig(ForgeConfigSpec.Builder builder) {
        super(builder);
    }
}
