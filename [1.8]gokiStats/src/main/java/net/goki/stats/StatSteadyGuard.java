package net.goki.stats;

import net.goki.lib.DataHelper;
import net.minecraft.entity.player.EntityPlayer;

public class StatSteadyGuard extends Stat
{
  public StatSteadyGuard(int id, String key, int limit)
  {
    super(id, key, limit);
  }
  @Override
  public float getBonus(int level)
  {
    return Math.min(getFinalBonus((float)Math.pow(level, 1.3615D)), 100.0F);
  }
  @Override
  public float[] getAppliedDescriptionVar(EntityPlayer player)
  {
	  //TODO special
	  return new float[]{DataHelper.trimDecimals(getBonus(player), 1)};
  }
}