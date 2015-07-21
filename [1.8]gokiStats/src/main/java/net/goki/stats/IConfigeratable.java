package net.goki.stats;

import net.minecraftforge.common.config.Configuration;

public interface IConfigeratable
{
	public void loadFromConfigurationFile(Configuration config);

	public void fromConfigurationString(String configString);

	public void saveToConfigurationFile(Configuration config);

	public String toConfigurationString();
}
