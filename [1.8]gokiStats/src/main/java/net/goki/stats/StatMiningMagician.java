package net.goki.stats;

import java.util.ArrayList;
import java.util.List;

import net.goki.lib.DataHelper;
import net.goki.lib.IDMDTuple;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;

public class StatMiningMagician extends Stat
{
  public static List<IDMDTuple> blockEntries = new ArrayList<IDMDTuple>();

  private static IDMDTuple[] defaultBlockEntries = { new IDMDTuple(Blocks.coal_ore, 0), new IDMDTuple(Blocks.diamond_ore, 0), new IDMDTuple(Blocks.emerald_ore, 0), new IDMDTuple(Blocks.gold_ore, 0), new IDMDTuple(Blocks.iron_ore, 0), new IDMDTuple(Blocks.lapis_ore, 0), new IDMDTuple(Blocks.quartz_ore, 0), new IDMDTuple(Blocks.redstone_ore, 0) };

  public static List<IDMDTuple> itemEntries = new ArrayList<IDMDTuple>();

  private static IDMDTuple[] defaultItemEntries = { new IDMDTuple(Items.coal, 0), new IDMDTuple(Items.diamond, 0), new IDMDTuple(Items.emerald, 0), new IDMDTuple(Items.gold_ingot, 0), new IDMDTuple(Items.iron_ingot, 0), new IDMDTuple(Items.dye, 4), new IDMDTuple(Items.quartz, 0), new IDMDTuple(Items.redstone, 0) };

  public StatMiningMagician(int id, String key, int limit)
  {
    super(id, key, limit);
    for (IDMDTuple mme : defaultBlockEntries)
    {
      blockEntries.add(mme);
    }
    for (IDMDTuple mme : defaultItemEntries)
    {
      itemEntries.add(mme);
    }
  }
  @Override
  public float getBonus(int level)
  {
    return getFinalBonus(level * 0.3F);
  }
  @Override
  public float[] getAppliedDescriptionVar(EntityPlayer player)
  {
	return new float[]{DataHelper.trimDecimals(getBonus(player), 1)};
  }
  @Override
  public boolean needAffectedByStat(Object ... obj)
  {
    IDMDTuple idmd;
    if ((obj[0] instanceof IDMDTuple))
    {
      idmd = (IDMDTuple)obj[0];
      for (IDMDTuple entry : blockEntries)
      {
        if (idmd.equals(entry))
        {
          return true;
        }
      }
      for (IDMDTuple entry : itemEntries)
      {
        if (idmd.equals(entry))
        {
          return true;
        }
      }
    }
    return false;
    //return super.needAffectedByStat(obj[0]);
  }
}