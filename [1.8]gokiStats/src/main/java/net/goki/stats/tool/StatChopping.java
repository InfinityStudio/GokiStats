package net.goki.stats.tool;

import net.goki.stats.Stat;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

public class StatChopping extends ToolSpecificStat
{
	public StatChopping(int id, String key, int limit)
	{
		super(id, key, limit);
	}

	@Override
	public String getConfigurationKey()
	{
		return "Chopping Tools";
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
		{ Item.getIdFromItem(Items.wooden_axe) + ":0", Item.getIdFromItem(Items.stone_axe) + ":0", Item.getIdFromItem(Items.iron_axe) + ":0", Item.getIdFromItem(Items.golden_axe) + ":0", Item.getIdFromItem(Items.diamond_axe) + ":0" };
	}
}