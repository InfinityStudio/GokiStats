package net.goki.lib;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

public class TreasureFinderEntry
{
	public Block block;
	public Item item;
	public int blockMD = 0;
	public int itemMD = 0;
	public int miniumLevel = 0;
	public int chance = 0;

	public TreasureFinderEntry(Block block, int bMD, Item item, int iMD, int mL, int c)
	{
		this.block = block;
		this.item = item;
		this.blockMD = bMD;
		this.itemMD = iMD;
		this.miniumLevel = mL;
		this.chance = c;
	}

	public TreasureFinderEntry(String configString)
	{
		fromConfigurationString(configString);
	}

	public String toConfigurationString()
	{
		return Block.getIdFromBlock(this.block) + "_" + this.blockMD + "_" + Item.getIdFromItem(this.item) + "_" + this.itemMD + "_" + this.miniumLevel + "_" + this.chance;
	}

	public boolean fromConfigurationString(String configString)
	{
		boolean successful = false;
		try
		{
			String[] values = configString.split("_");
			int blockID = Integer.parseInt(values[0]);
			this.block = Block.getBlockById(blockID);
			this.blockMD = Integer.parseInt(values[1]);
			int itemID = Integer.parseInt(values[2]);
			this.item = Item.getItemById(itemID);
			this.itemMD = Integer.parseInt(values[3]);
			this.miniumLevel = Integer.parseInt(values[4]);
			this.chance = Integer.parseInt(values[5]);
		}
		catch (Exception e)
		{
			successful = false;
		}
		return successful;
	}
}