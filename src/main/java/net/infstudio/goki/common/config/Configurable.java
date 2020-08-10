package net.infstudio.goki.common.config;

import net.minecraftforge.common.ForgeConfigSpec;

public interface Configurable<T> {
    T createConfig(ForgeConfigSpec.Builder builder);
}
