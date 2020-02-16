package net.infstudio.goki.common.adapters;

import net.infstudio.goki.api.stat.StatBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;

import javax.annotation.Nonnull;

public class StatFix implements IFixableData {
    @Override
    public int getFixVersion() {
        return 1343;
    }

    @Nonnull
    @Override
    public NBTTagCompound fixTagCompound(@Nonnull NBTTagCompound compound) {
        if (compound.hasKey("PlayerPersisted") && compound.getCompoundTag("PlayerPersisted").hasKey("gokistats_Stats")) {
            NBTTagCompound statsCompound = compound.getCompoundTag("PlayerPersisted").getCompoundTag("gokistats_Stats");
            for (StatBase stat : StatBase.stats) {
                if (statsCompound.hasKey(stat.key));
                if (statsCompound.hasKey(stat.key + ".revert"));
            }
        }
        return null;
    }
}
