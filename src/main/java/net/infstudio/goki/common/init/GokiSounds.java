package net.infstudio.goki.common.init;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ObjectHolder;

import static net.infstudio.goki.common.utils.Reference.MODID;

@ObjectHolder(MODID)
public class GokiSounds {
    public static final SoundEvent TREASURE = null;
    public static final SoundEvent MAGICIAN = null;
    public static final SoundEvent REAPER = null;

    @SubscribeEvent
    public static void registerSounds(RegistryEvent.Register<SoundEvent> event) {
        event.getRegistry().registerAll(
                new SoundEvent(new ResourceLocation(MODID, "treasure")),
                new SoundEvent(new ResourceLocation(MODID, "magician")),
                new SoundEvent(new ResourceLocation(MODID, "reaper"))
        );
    }
}
