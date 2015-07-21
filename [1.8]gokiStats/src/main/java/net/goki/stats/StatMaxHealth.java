package net.goki.stats;

import net.goki.lib.DataHelper;
import net.minecraft.entity.player.EntityPlayer;

public class StatMaxHealth extends Stat
{

	public StatMaxHealth(int id, String key, int limit)
	{
		super(id, key, limit);
	}

	@Override
	protected float getBonus(int level)
	{
		return getFinalBonus(level);
	}

	@Override
	public int getLimit()
	{
		if (globalLimitMultiplier <= 0.0F)
			return 40;
		else
			return (int) (40 * this.limitMultiplier);
	}

	@Override
	public int getCost(int level)
	{
		return (int) ((Math.pow(level, 2D) + 12.0D + level) * globalCostMultiplier);
	}
	
	@Override
	public float[] getAppliedDescriptionVar(EntityPlayer player)
	{
		return new float[]
		{ DataHelper.trimDecimals(getBonus(getPlayerStatLevel(player)), 0) };
	}
	
}
