package net.goki.lib;

import java.util.ArrayList;
import java.util.List;

import net.goki.stats.IConfigeratable;
import net.goki.stats.Stat;
import net.minecraftforge.common.config.Configuration;

public class StatHelper
{
	public static List<IConfigeratable> iConfigeratStat = new ArrayList<IConfigeratable>();

	static
	{
		for(Stat stat: Stat.stats)
		{
			if(stat instanceof IConfigeratable)
			{
				iConfigeratStat.add((IConfigeratable)stat);
			}
		}
	}

	public static final void loadAllStatsFromConfiguration(Configuration config)
	{
		for (IConfigeratable stat : iConfigeratStat)
		{
			stat.loadFromConfigurationFile(config);
		}
	}

	public static final void saveAllStatsToConfiguration(Configuration config)
	{
		for (IConfigeratable stat : iConfigeratStat)
		{
			stat.saveToConfigurationFile(config);
		}
	}

}
