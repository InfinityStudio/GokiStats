package net.infstudio.goki.common.init;

import net.infstudio.goki.common.handlers.GokiLootModifier;
import net.infstudio.goki.common.utils.Reference;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

import javax.annotation.Nonnull;

import static net.infstudio.goki.common.utils.Reference.MODID;

@ObjectHolder(MODID)
@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class GokiSounds {
    public static final SoundEvent TREASURE = null;
    public static final SoundEvent MAGICIAN = null;
    public static final SoundEvent REAPER = null;

    @SubscribeEvent
    public static void registerSounds(RegistryEvent.Register<SoundEvent> event) {
        event.getRegistry().registerAll(
                new SoundEvent(new ResourceLocation(MODID, "treasure")).setRegistryName(MODID, "treasure"),
                new SoundEvent(new ResourceLocation(MODID, "magician")).setRegistryName(MODID, "magician"),
                new SoundEvent(new ResourceLocation(MODID, "reaper")).setRegistryName(MODID, "reaper")
        );
    }

    @SubscribeEvent
    public static void registerModifierSerializers(@Nonnull final RegistryEvent.Register<GlobalLootModifierSerializer<?>> event) {
        event.getRegistry().register(
                new GokiLootModifier.Serializer().setRegistryName(Reference.MODID, "loot")
        );
    }
}
