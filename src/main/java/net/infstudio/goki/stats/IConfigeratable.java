package net.infstudio.goki.stats;

import net.minecraftforge.common.config.Configuration;

public interface IConfigeratable {
    void loadFromConfigurationFile(Configuration config);

    void fromConfigurationString(String configString);

    void saveToConfigurationFile(Configuration config);

    String toConfigurationString();
}
