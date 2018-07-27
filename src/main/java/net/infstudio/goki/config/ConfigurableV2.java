package net.infstudio.goki.config;

import java.lang.reflect.Type;

public interface ConfigurableV2<T> {
    T createConfig();

    default void saveConfig() {
        ConfigManager.INSTANCE.saveConfig(getKey());
    }

    default Type getType() {
        return createConfig().getClass();
    }

    default void reloadConfig() {
        ConfigManager.INSTANCE.registerConfig(getKey(), getType());
        if (!ConfigManager.INSTANCE.hasConfig(getKey()))
            ConfigManager.INSTANCE.createConfig(getKey(), createConfig());
        ConfigManager.INSTANCE.reloadConfig(getKey());
        reload();
    }

    void reload();

    String getKey();

    default T getConfig() {
        return ConfigManager.INSTANCE.getOrCreateConfig(getKey(), createConfig());
    }
}
