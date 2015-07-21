package net.goki.stats;

import net.minecraft.entity.player.EntityPlayer;

public interface IStat
{
	public boolean needAffectedByStat(Object... obj);

	public float[] getAppliedDescriptionVar(EntityPlayer player);

	public float getBonus(EntityPlayer player);

//	abstract float getBonus(int paramInt);

	public float getAppliedBonus(EntityPlayer player, Object paramObject);

	public int getCost(int level);

	public int getLimit();
	
//	public String getSimpleDescriptionString();
}