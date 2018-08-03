package net.infstudio.goki.common.init;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@GameRegistry.ObjectHolder("gokistats")
public class GokiSounds {
    public static final SoundEvent TREASURE = null;
    public static final SoundEvent MAGICIAN = null;
    public static final SoundEvent REAPER = null;

    @SubscribeEvent
    public void registerSounds(RegistryEvent.Register<SoundEvent> event) {
        event.getRegistry().registerAll(
                new SoundEvent(new ResourceLocation("gokistats:treasure")),
                new SoundEvent(new ResourceLocation("gokistats:magician")),
                new SoundEvent(new ResourceLocation("gokistats:reaper"))
        );
    }
}
