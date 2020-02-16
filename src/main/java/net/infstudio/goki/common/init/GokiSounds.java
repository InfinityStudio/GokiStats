package net.infstudio.goki.common.init;

import net.infstudio.goki.common.utils.Reference;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import static net.infstudio.goki.common.utils.Reference.MODID;

@GameRegistry.ObjectHolder(MODID)
public class GokiSounds {
    public static final SoundEvent TREASURE = null;
    public static final SoundEvent MAGICIAN = null;
    public static final SoundEvent REAPER = null;

    @SubscribeEvent
    public void registerSounds(RegistryEvent.Register<SoundEvent> event) {
        event.getRegistry().registerAll(
                new SoundEvent(new ResourceLocation(MODID, "treasure")),
                new SoundEvent(new ResourceLocation(MODID, "magician")),
                new SoundEvent(new ResourceLocation(MODID, "reaper"))
        );
    }
}
