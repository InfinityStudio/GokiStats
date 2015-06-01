package net.goki.stats.tool;

import net.minecraft.init.Items;
import net.minecraft.item.Item;

public class StatMining extends ToolSpecificStat
{
  public StatMining(int id, String key, int limit)
  {
    super(id, key, limit);
  }
  @Override
  public String getConfigurationKey()
  {
    return "Mining Tools";
  }
  @Override
  public float getBonus(int level)
  {
    return getFinalBonus((float)Math.pow(level, 1.3D) * 0.01523F);
  }
 
  @Override
  public String[] getDefaultSupportedItems()
  {
    return new String[] { Item.getIdFromItem(Items.wooden_pickaxe) + ":0", Item.getIdFromItem(Items.stone_pickaxe) + ":0", Item.getIdFromItem(Items.iron_pickaxe) + ":0", Item.getIdFromItem(Items.golden_pickaxe) + ":0", Item.getIdFromItem(Items.diamond_pickaxe) + ":0" };
  }
}