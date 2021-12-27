package net.infstudio.goki.api.capability;

import net.infstudio.goki.api.stat.Stat;
import net.infstudio.goki.api.stat.StatBase;
import net.infstudio.goki.api.stat.StatState;
import net.infstudio.goki.api.stat.StatStorage;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CapabilityStat {
    @CapabilityInject(StatStorage.class)
    public static Capability<StatStorage> STAT;

    private static CompoundNBT serializeNBT(StatStorage statStorage) {
        CompoundNBT compound = new CompoundNBT();
        statStorage.stateMap.forEach((stat, state) -> {
            CompoundNBT stateTag = new CompoundNBT();
            stateTag.putInt("level", state.level);
            stateTag.putInt("revertedLevel", state.revertedLevel);
            compound.put(stat.getRegistryName().toString(), stateTag);
        });
        return compound;
    }

    private static void deserializeNBT(@Nonnull StatStorage statStorage, INBT nbt) {
        if (nbt instanceof CompoundNBT) {
            CompoundNBT compound = (CompoundNBT) nbt;
            for (String stat : compound.getAllKeys()) {
                ResourceLocation statLocation = new ResourceLocation(stat);
                if (!StatBase.REGISTRY.containsKey(statLocation)) continue;
                CompoundNBT stateTag = compound.getCompound(stat);
                Stat statObject = StatBase.REGISTRY.getValue(statLocation);
                statStorage.stateMap.put(statObject, new StatState(statObject, stateTag.getInt("level"), stateTag.getInt("revertedLevel")));
            }
        }
    }

    public static class Provider implements ICapabilitySerializable<CompoundNBT> {
        public StatStorage statStorage = new StatStorage();
        public final LazyOptional<StatStorage> storageProperty = LazyOptional.of(() -> statStorage);

        @Override
        @Nonnull
        public <T> LazyOptional<T> getCapability(@Nonnull final Capability<T> cap, final @Nullable Direction side) {
            if (cap == STAT) return (LazyOptional<T>) storageProperty;
            else return LazyOptional.empty();
        }

        @Override
        public CompoundNBT serializeNBT() {
            return CapabilityStat.serializeNBT(statStorage);
        }


        @Override
        public void deserializeNBT(CompoundNBT nbt) {
            CapabilityStat.deserializeNBT(statStorage, nbt);
        }
    }

    public static void register() {
        CapabilityManager.INSTANCE.register(StatStorage.class, new Capability.IStorage<StatStorage>() {
            @Override
            public INBT writeNBT(Capability<StatStorage> capability, StatStorage instance, Direction side) {
                return serializeNBT(instance);
            }

            @Override
            public void readNBT(Capability<StatStorage> capability, StatStorage instance, Direction side, INBT nbt) {
                CapabilityStat.deserializeNBT(instance, nbt);
            }
        }, StatStorage::new);
    }
}
