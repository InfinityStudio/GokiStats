package net.infstudio.goki.lib;

import net.infstudio.goki.stats.IConfigurable;
import net.infstudio.goki.stats.StatBase;
import net.minecraftforge.common.config.Configuration;

import java.util.ArrayList;
import java.util.List;

public class StatHelper {
    public static List<IConfigurable> configurables = new ArrayList<>();

    static {
        for (StatBase stat : StatBase.stats) {
            if (stat instanceof IConfigurable) {
                configurables.add((IConfigurable) stat);
            }
        }
    }

    public static void loadAllStatsFromConfiguration(Configuration config) {
        for (IConfigurable stat : configurables) {
            stat.loadFromConfigurationFile(config);
        }
    }

    public static void saveAllStatsToConfiguration(Configuration config) {
        for (IConfigurable stat : configurables) {
            stat.saveToConfigurationFile(config);
        }
    }

}
