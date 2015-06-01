package net.goki.stats.tool;

import net.minecraft.init.Items;
import net.minecraft.item.Item;

public class StatSwordsmanship extends ToolSpecificStat
{
  public StatSwordsmanship(int id, String key, int limit)
  {
    super(id, key, limit);
  }
  @Override
  public String getConfigurationKey()
  {
    return "Swordsmanship Tools";
  }
  @Override
  public float getBonus(int level)
  {
    return getFinalBonus((float)Math.pow(level, 1.0895D) * 0.03F);
  }
  @Override
  public String[] getDefaultSupportedItems()
  {
    return new String[] { Item.getIdFromItem(Items.wooden_sword) + ":0", Item.getIdFromItem(Items.stone_sword) + ":0", Item.getIdFromItem(Items.iron_sword) + ":0", Item.getIdFromItem(Items.golden_sword) + ":0", Item.getIdFromItem(Items.diamond_sword) + ":0" };
  }
}