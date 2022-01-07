package net.infstudio.goki.api.capability;

import net.infstudio.goki.api.stat.StatBase;
import net.infstudio.goki.api.stat.StatState;
import net.infstudio.goki.api.stat.StatStorage;
import net.infstudio.goki.common.utils.Reference;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Mod.EventBusSubscriber(modid = Reference.MODID)
public class CapabilityStat {
    public static Capability<StatStorage> STAT = CapabilityManager.get(new CapabilityToken<>(){});

    private static CompoundTag serializeNBT(StatStorage statStorage) {
        var compound = new CompoundTag();
        statStorage.stateMap.forEach((stat, state) -> {
            var stateTag = new CompoundTag();
            stateTag.putInt("level", state.level);
            stateTag.putInt("revertedLevel", state.revertedLevel);
            compound.put(stat.getRegistryName().toString(), stateTag);
        });
        return compound;
    }

    private static void deserializeNBT(@Nonnull StatStorage statStorage, Tag nbt) {
        if (nbt instanceof CompoundTag) {
            var compound = (CompoundTag) nbt;
            for (var stat : compound.getAllKeys()) {
                var statLocation = new ResourceLocation(stat);
                if (!StatBase.REGISTRY.containsKey(statLocation)) continue;
                var stateTag = compound.getCompound(stat);
                var statObject = StatBase.REGISTRY.getValue(statLocation);
                statStorage.stateMap.put(statObject, new StatState(statObject, stateTag.getInt("level"), stateTag.getInt("revertedLevel")));
            }
        }
    }

    public static class Provider implements ICapabilitySerializable<CompoundTag> {
        public StatStorage statStorage = new StatStorage();
        public final LazyOptional<StatStorage> storageProperty = LazyOptional.of(() -> statStorage);

        @Override
        @Nonnull
        public <T> LazyOptional<T> getCapability(@Nonnull final Capability<T> cap, final @Nullable Direction side) {
            if (cap == STAT) return (LazyOptional<T>) storageProperty;
            else return LazyOptional.empty();
        }

        @Override
        public CompoundTag serializeNBT() {
            return CapabilityStat.serializeNBT(statStorage);
        }


        @Override
        public void deserializeNBT(CompoundTag nbt) {
            CapabilityStat.deserializeNBT(statStorage, nbt);
        }
    }

    public static void register(RegisterCapabilitiesEvent event) {
        event.register(StatStorage.class);
    }
}
