package net.goki.stats.tool;

import net.goki.stats.Stat;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

public class StatDigging extends ToolSpecificStat
{
	public StatDigging(int id, String key, int limit)
	{
		super(id, key, limit);
	}

	@Override
	public String getConfigurationKey()
	{
		return "Digging Tools";
	}

	@Override
	public float getBonus(int level)
	{
		return Stat.getFinalBonus((float) Math.pow(level, 1.3D) * 0.01523F);
	}

	@Override
	public String[] getDefaultSupportedItems()
	{
		return new String[]
		{ Item.getIdFromItem(Items.wooden_shovel) + ":0", Item.getIdFromItem(Items.stone_shovel) + ":0", Item.getIdFromItem(Items.iron_shovel) + ":0", Item.getIdFromItem(Items.golden_shovel) + ":0", Item.getIdFromItem(Items.diamond_shovel) + ":0" };
	}
}