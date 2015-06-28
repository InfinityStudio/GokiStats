package net.goki.stats;

import net.goki.lib.DataHelper;
import net.goki.lib.Reference;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.config.Configuration;

public class StatReaper extends Stat
{
	public static float healthLimit = 20.0F;

	public StatReaper(int id, String key, int limit)
	{
		super(id, key, limit);
	}

	@Override
	public float getBonus(int level)
	{
		return getFinalBonus((float) Math.pow(level, 1.0768D) * 0.0025F);
	}

	@Override
	public float[] getAppliedDescriptionVar(EntityPlayer player)
	{// TODO special
		return new float[]
		{ DataHelper.trimDecimals(getBonus(getPlayerStatLevel(player)) * 100, 1), healthLimit };
		// return Helper.trimDecimals(getBonus(getPlayerStatLevel(player)) *
		// 100, 1) + "% chance to instantly kill enemies with less than " +
		// healthLimit + " health.";
	}

	@Override
	public String getLocalizedDes(EntityPlayer player)
	{
		return StatCollector.translateToLocalFormatted(	this.key + ".des",
														this.getAppliedDescriptionVar(player)[0],
														this.getAppliedDescriptionVar(player)[1]);
	}

	@Override
	public boolean needAffectedByStat(Object... obj)
	{
		if (obj[0] != null)
		{
			if (!(obj[0] instanceof EntityPlayer))
			{
				if ((obj[0] instanceof EntityLivingBase))
				{
					EntityLivingBase target = (EntityLivingBase) obj[0];

					return target.getMaxHealth() <= healthLimit;
				}
			}
		}
		return false;
	}

	@Override
	public void loadFromConfigurationFile(Configuration config)
	{
		healthLimit = (float) Reference.configuration.get(	"Support",
															"Reaper Limit",
															healthLimit).getDouble(20.0D);
	}

	@Override
	public void saveToConfigurationFile(Configuration config)
	{
		Reference.configuration.get("Support", "Reaper Limit", healthLimit).set(healthLimit);
	}

	@Override
	public String toConfigurationString()
	{
		return "" + healthLimit;
	}

	@Override
	public void fromConfigurationString(String configString)
	{
		try
		{
			healthLimit = Float.parseFloat(configString);
		}
		catch (Exception e)
		{
		}
	}
}