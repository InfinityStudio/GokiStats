package net.infstudio.goki.lib;

import net.infstudio.goki.stats.IConfigeratable;
import net.infstudio.goki.stats.Stat;
import net.minecraftforge.common.config.Configuration;

import java.util.ArrayList;
import java.util.List;

public class StatHelper {
    public static List<IConfigeratable> iConfigeratStat = new ArrayList<>();

    static {
        for (Stat stat : Stat.stats) {
            if (stat instanceof IConfigeratable) {
                iConfigeratStat.add((IConfigeratable) stat);
            }
        }
    }

    public static final void loadAllStatsFromConfiguration(Configuration config) {
        for (IConfigeratable stat : iConfigeratStat) {
            stat.loadFromConfigurationFile(config);
        }
    }

    public static final void saveAllStatsToConfiguration(Configuration config) {
        for (IConfigeratable stat : iConfigeratStat) {
            stat.saveToConfigurationFile(config);
        }
    }

}
