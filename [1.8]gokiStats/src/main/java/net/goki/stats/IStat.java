package net.goki.stats;

import net.minecraft.entity.player.EntityPlayer;

public abstract interface IStat
{
	public abstract boolean needAffectedByStat(Object... obj);

	public abstract String getSimpleDescriptionString();

	public abstract float[] getAppliedDescriptionVar(EntityPlayer paramEntityPlayer);

	public abstract float getBonus(EntityPlayer paramEntityPlayer);

	public abstract float getBonus(int paramInt);

	public abstract float getSecondaryBonus(int paramInt);

	public abstract float getSecondaryBonus(EntityPlayer paramEntityPlayer);

	public abstract float getAppliedBonus(EntityPlayer paramEntityPlayer, Object paramObject);

	public abstract int getCost(int paramInt);

	public abstract int getLimit();
}