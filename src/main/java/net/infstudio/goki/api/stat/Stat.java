package net.infstudio.goki.api.stat;

import net.minecraft.entity.player.EntityPlayer;

public interface Stat {
    boolean needAffectedByStat(Object... obj);

    float[] getAppliedDescriptionVar(EntityPlayer player);

    float getBonus(int level);

    float getBonus(EntityPlayer player);

//	abstract float getBonus(int paramInt);

    float getAppliedBonus(EntityPlayer player, Object paramObject);

    int getCost(int level);

    int getLimit();

    String getKey();

//	public String getSimpleDescriptionString();
}