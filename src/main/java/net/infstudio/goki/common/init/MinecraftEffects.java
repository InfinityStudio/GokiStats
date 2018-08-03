package net.infstudio.goki.common.init;

import net.minecraft.potion.Potion;
import net.minecraftforge.fml.common.registry.GameRegistry;

@GameRegistry.ObjectHolder("minecraft")
public interface MinecraftEffects {
    Potion STRENGTH = null;
}
