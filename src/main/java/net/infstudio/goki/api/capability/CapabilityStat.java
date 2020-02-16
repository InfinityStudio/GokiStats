package net.infstudio.goki.api.capability;

import net.infstudio.goki.api.stat.StatBase;
import net.infstudio.goki.api.stat.StatState;
import net.infstudio.goki.api.stat.StatStorage;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class CapabilityStat {
    @CapabilityInject(StatStorage.class)
    public static Capability<StatStorage> STAT;

    public static void register() {
        CapabilityManager.INSTANCE.register(StatStorage.class, new Capability.IStorage<StatStorage>() {
            @Override
            public NBTBase writeNBT(Capability<StatStorage> capability, StatStorage instance, EnumFacing side) {
                NBTTagCompound compound = new NBTTagCompound();
                instance.stateMap.forEach((stat, state) -> {
                    NBTTagCompound stateTag = new NBTTagCompound();
                    stateTag.setInteger("level", state.level);
                    stateTag.setInteger("revertedLevel", state.revertedLevel);
                    compound.setTag(stat.getKey(), stateTag);
                });
                return compound;
            }

            @Override
            public void readNBT(Capability<StatStorage> capability, StatStorage instance, EnumFacing side, NBTBase nbt) {
                if (nbt instanceof NBTTagCompound) {
                    NBTTagCompound compound = (NBTTagCompound) nbt;
                    for (String stat : compound.getKeySet()) {
                        if (!StatBase.statKeyMap.containsKey(stat)) continue;
                        NBTTagCompound stateTag = compound.getCompoundTag(stat);
                        StatBase statBase = StatBase.statKeyMap.get(stat);
                        instance.stateMap.put(statBase, new StatState(statBase, stateTag.getInteger("level"), stateTag.getInteger("revertedLevel")));
                    }
                }
            }
        }, StatStorage::new);
    }
}
