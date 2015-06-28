package net.goki.stats;

import net.goki.lib.DataHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.StatCollector;

public class StatStealth extends StatLeaper
{
	public StatStealth(int id, String key, int limit)
	{
		super(id, key, limit);
	}

	@Override
	public float getBonus(int level)
	{
		return getFinalBonus((float) Math.pow(level, 1.3416D));
	}

	@Override
	public boolean needAffectedByStat(Object... obj)
	{
		if (((obj[0] instanceof EntityPlayer)) && (((EntityPlayer) obj[0]).isSneaking()))
		{
			return true;
		}
		return false;
	}

	@Override
	public float getSecondaryBonus(int level)
	{
		return getFinalBonus((float) Math.pow(level, 1.4307D));
	}

	@Override
	public float[] getAppliedDescriptionVar(EntityPlayer player)
	{
		// TODO special
		float speed = DataHelper.trimDecimals(getBonus(player), 1);
		float reapBonus = DataHelper.trimDecimals(getSecondaryBonus(player), 1);
		float reap = Stat.STAT_REAPER.getBonus(player) * 100.0F;
		float newReap = DataHelper.trimDecimals(reap + reap * reapBonus / 100.0F,
												1);
		return new float[]
		{ speed, reapBonus, newReap };
		// return "Move " + speed + "% faster and reap " + reapBonus +
		// "% more often (" + newReap + "%) when sneaking.";
	}

	@Override
	public String getLocalizedDes(EntityPlayer player)
	{
		return StatCollector.translateToLocalFormatted(	this.key + ".des",
														this.getAppliedDescriptionVar(player)[0],
														this.getAppliedDescriptionVar(player)[1],
														this.getAppliedDescriptionVar(player)[2]);
	}
}