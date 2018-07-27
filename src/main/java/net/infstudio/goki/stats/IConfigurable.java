package net.infstudio.goki.stats;

import net.minecraftforge.common.config.Configuration;

public interface IConfigurable {
    void loadFromConfigurationFile(Configuration config);

    void fromConfigurationString(String configString);

    void saveToConfigurationFile(Configuration config);

    String toConfigurationString();
}
