package net.goki.stats;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.StatCollector;

public class StatFurnaceFinesse extends Stat
{
	public StatFurnaceFinesse(int id, String key, int limit)
	{
		super(id, key, limit);
	}

	@Override
	public float getBonus(int level)
	{
		return Math.min(getFinalBonus(level / 5.0F), 199.0F) + 1.0F;
	}

	@Override
	public float getSecondaryBonus(int level)
	{
		return Math.min(getFinalBonus(level), 100.0F);
	}

	@Override
	public float[] getAppliedDescriptionVar(EntityPlayer player)
	{
		// TODO special
		int level = getPlayerStatLevel(player);
		return new float[]
		{ getBonus(level), getSecondaryBonus(level) };
		// return "Smelt " + getBonus(level) + " ticks faster " +
		// getSecondaryBonus(level) + "% of the time while using a furnace.";
	}

	@Override
	public String getLocalizedDes(EntityPlayer player)
	{
		return StatCollector.translateToLocalFormatted(	this.key + ".des",
														this.getAppliedDescriptionVar(player)[0],
														this.getAppliedDescriptionVar(player)[1]);
	}
}