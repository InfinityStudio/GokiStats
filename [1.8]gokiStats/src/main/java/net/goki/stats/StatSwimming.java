package net.goki.stats;

import net.goki.lib.Reference;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.StatCollector;

public class StatSwimming extends Stat
{
	public StatSwimming(int id, String key, int limit)
	{
		super(id, key, limit);
	}

	public float getBonus(int level)
	{
		return getFinalBonus((float) Math.pow(level, 1.1D) * 0.029F);
	}

	@Override
	public String getLocalizedDes(EntityPlayer player)
	{
		if (Reference.isPlayerAPILoaded)
		{
			return StatCollector.translateToLocal(this.key + ".des1");
		}
		return StatCollector.translateToLocalFormatted(	this.key + ".des0",
														this.getAppliedDescriptionVar(player)[0]);
	}
}