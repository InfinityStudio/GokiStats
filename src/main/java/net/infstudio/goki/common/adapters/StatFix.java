package net.infstudio.goki.common.adapters;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IDataFixer;
import net.minecraft.util.datafix.IDataWalker;

import javax.annotation.Nonnull;

public class StatFix implements IDataWalker {

    @Nonnull
    @Override
    public NBTTagCompound process(@Nonnull IDataFixer fixer, @Nonnull NBTTagCompound compound, int versionIn) {
        System.out.println(compound);
        return compound;
    }
}
