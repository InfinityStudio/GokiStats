package net.infstudio.goki.api.capability;

import net.infstudio.goki.api.stat.StatBase;
import net.infstudio.goki.api.stat.StatState;
import net.infstudio.goki.api.stat.StatStorage;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CapabilityStat {
    @CapabilityInject(StatStorage.class)
    public static Capability<StatStorage> STAT;

    public static class Provider implements ICapabilityProvider {
        public StatStorage statStorage = new StatStorage();
        public final LazyOptional<StatStorage> storageProperty = LazyOptional.of(() -> statStorage);

        @Override
        @Nonnull
        public <T> LazyOptional<T> getCapability(@Nonnull final Capability<T> cap, final @Nullable Direction side) {
            if (cap == STAT) return (LazyOptional<T>) storageProperty;
            else return LazyOptional.empty();
        }
    }

    public static void register() {
        CapabilityManager.INSTANCE.register(StatStorage.class, new Capability.IStorage<StatStorage>() {
            @Override
            public INBT writeNBT(Capability<StatStorage> capability, StatStorage instance, Direction side) {
                CompoundNBT compound = new CompoundNBT();
                instance.stateMap.forEach((stat, state) -> {
                    CompoundNBT stateTag = new CompoundNBT();
                    stateTag.putInt("level", state.level);
                    stateTag.putInt("revertedLevel", state.revertedLevel);
                    compound.put(stat.getRegistryName().toString(), stateTag);
                });
                return compound;
            }

            @Override
            public void readNBT(Capability<StatStorage> capability, StatStorage instance, Direction side, INBT nbt) {
                if (nbt instanceof CompoundNBT) {
                    CompoundNBT compound = (CompoundNBT) nbt;
                    for (String stat : compound.keySet()) {
                        if (!StatBase.statKeyMap.containsKey(stat)) continue;
                        CompoundNBT stateTag = compound.getCompound(stat);
                        StatBase statBase = StatBase.statKeyMap.get(stat);
                        instance.stateMap.put(statBase, new StatState(statBase, stateTag.getInt("level"), stateTag.getInt("revertedLevel")));
                    }
                }
            }
        }, StatStorage::new);
    }
}
