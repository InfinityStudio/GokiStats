package net.goki.stats;

import java.util.ArrayList;
import java.util.List;

import net.goki.lib.TreasureFinderEntry;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.config.Configuration;

public class StatTreasureFinder extends Stat implements IConfigeratable
{
	public static List<TreasureFinderEntry> entries = new ArrayList<TreasureFinderEntry>();

	public static String[] defaultEntries =
	{ new TreasureFinderEntry(Blocks.sand, 0, Items.gold_nugget, 0, 1, 10).toConfigurationString(), new TreasureFinderEntry(Blocks.dirt, 0, Items.rotten_flesh, 0, 1, 40).toConfigurationString(), new TreasureFinderEntry(Blocks.dirt, 0, Items.bone, 0, 1, 20).toConfigurationString(), new TreasureFinderEntry(Blocks.dirt, 0, Items.spider_eye, 0, 1, 10).toConfigurationString(), new TreasureFinderEntry(Blocks.dirt, 0, Items.gunpowder, 0, 1, 10).toConfigurationString(), new TreasureFinderEntry(Blocks.dirt, 0, Items.slime_ball, 0, 1, 10).toConfigurationString(), new TreasureFinderEntry(Blocks.sand, 0, Items.glass_bottle, 0, 1, 10).toConfigurationString(), new TreasureFinderEntry(Blocks.sand, 0, Items.clock, 0, 1, 5).toConfigurationString(), new TreasureFinderEntry(Blocks.sand, 0, Items.compass, 0, 1, 5).toConfigurationString(), new TreasureFinderEntry(Blocks.sand, 0, Items.clay_ball, 0, 1, 20).toConfigurationString(), new TreasureFinderEntry(Blocks.dirt, 0, Items.bowl, 0, 1, 6).toConfigurationString(), new TreasureFinderEntry(Blocks.dirt, 0, Items.potato, 0, 1, 10).toConfigurationString(), new TreasureFinderEntry(Blocks.dirt, 0, Items.carrot, 0, 1, 10).toConfigurationString(), new TreasureFinderEntry(Blocks.dirt, 0, Items.apple, 0, 1, 10).toConfigurationString(), new TreasureFinderEntry(Blocks.leaves, 0, Items.apple, 0, 1, 10).toConfigurationString(), new TreasureFinderEntry(Blocks.leaves2, 0, Items.apple, 0, 1, 10).toConfigurationString(), new TreasureFinderEntry(Blocks.dirt, 0, Item.getItemFromBlock(Blocks.red_mushroom), 0, 1, 10).toConfigurationString(), new TreasureFinderEntry(Blocks.dirt, 0, Item.getItemFromBlock(Blocks.brown_mushroom), 0, 1, 10).toConfigurationString(), new TreasureFinderEntry(Blocks.dirt, 0, Item.getItemFromBlock(Blocks.red_flower), 0, 1, 10).toConfigurationString(), new TreasureFinderEntry(Blocks.dirt, 0, Item.getItemFromBlock(Blocks.yellow_flower), 0, 1, 10).toConfigurationString(), new TreasureFinderEntry(Blocks.dirt, 0, Items.stick, 0, 1, 100).toConfigurationString(), new TreasureFinderEntry(Blocks.tallgrass, 0, Items.pumpkin_seeds, 0, 1, 30).toConfigurationString(), new TreasureFinderEntry(Blocks.tallgrass, 0, Items.melon_seeds, 0, 1, 30).toConfigurationString(), new TreasureFinderEntry(Blocks.dirt, 0, Items.beef, 0, 2, 50).toConfigurationString(), new TreasureFinderEntry(Blocks.dirt, 0, Items.porkchop, 0, 2, 50).toConfigurationString(), new TreasureFinderEntry(Blocks.dirt, 0, Items.chicken, 0, 2, 50).toConfigurationString(), new TreasureFinderEntry(Blocks.sand, 0, Items.iron_ingot, 0, 2, 10).toConfigurationString(), new TreasureFinderEntry(Blocks.sand, 0, Items.gold_ingot, 0, 2, 10).toConfigurationString(), new TreasureFinderEntry(Blocks.dirt, 0, Items.leather, 0, 2, 10).toConfigurationString(), new TreasureFinderEntry(Blocks.dirt, 0, Items.feather, 0, 2, 10).toConfigurationString(), new TreasureFinderEntry(Blocks.dirt, 0, Item.getItemFromBlock(Blocks.wool), 0, 2, 10).toConfigurationString(), new TreasureFinderEntry(Blocks.sand, 0, Items.redstone, 0, 2, 50).toConfigurationString(), new TreasureFinderEntry(Blocks.dirt, 0, Items.bucket, 0, 3, 20).toConfigurationString(), new TreasureFinderEntry(Blocks.dirt, 0, Items.record_11, 0, 3, 2).toConfigurationString(), new TreasureFinderEntry(Blocks.dirt, 0, Items.record_13, 0, 3, 2).toConfigurationString(), new TreasureFinderEntry(Blocks.dirt, 0, Items.record_blocks, 0, 3, 2).toConfigurationString(), new TreasureFinderEntry(Blocks.dirt, 0, Items.record_cat, 0, 3, 2).toConfigurationString(), new TreasureFinderEntry(Blocks.dirt, 0, Items.record_chirp, 0, 3, 2).toConfigurationString(), new TreasureFinderEntry(Blocks.dirt, 0, Items.record_far, 0, 3, 2).toConfigurationString(), new TreasureFinderEntry(Blocks.dirt, 0, Items.record_mall, 0, 3, 2).toConfigurationString(), new TreasureFinderEntry(Blocks.dirt, 0, Items.record_mellohi, 0, 3, 2).toConfigurationString(), new TreasureFinderEntry(Blocks.dirt, 0, Items.record_stal, 0, 3, 2).toConfigurationString(), new TreasureFinderEntry(Blocks.dirt, 0, Items.record_strad, 0, 3, 2).toConfigurationString(), new TreasureFinderEntry(Blocks.dirt, 0, Items.record_wait, 0, 3, 2).toConfigurationString(), new TreasureFinderEntry(Blocks.dirt, 0, Items.record_ward, 0, 3, 2).toConfigurationString() };

	public StatTreasureFinder(int id, String key, int limit)
	{
		super(id, key, limit);
	}

	@Override
	public String getLocalizedDes(EntityPlayer player)
	{
		if (getPlayerStatLevel(player) == 0)
		{
			return StatCollector.translateToLocal(this.key + ".des0");
		}
		return StatCollector.translateToLocal(this.key + ".des1");
	}

	@Override
	public void loadFromConfigurationFile(Configuration config)
	{
		entries.clear();
		String[] entryStrings = config.get(	"Treasure Finder",
											"Entries",
											defaultEntries).getStringList();
		for (int i = 0; i < entryStrings.length; i++)
		{
			entries.add(new TreasureFinderEntry(entryStrings[i]));
		}
	}

	@Override
	public void saveToConfigurationFile(Configuration config)
	{
		String[] entryStrings = new String[entries.size()];
		for (int i = 0; i < entryStrings.length; i++)
		{
			entryStrings[i] = ((TreasureFinderEntry) entries.get(i)).toConfigurationString();
		}
		config.get("Treasure Finder", "Entries", defaultEntries).set(entryStrings);
	}

	public List<ItemStack> getApplicableItemStackList(Block block, int blockMD, int level)
	{
		List<ItemStack> items = new ArrayList<ItemStack>();
		for (TreasureFinderEntry tfe : entries)
		{
			if (tfe.miniumLevel <= level)
			{
				if (((tfe.block == null) && ((block == Blocks.dirt) || (block == Blocks.grass))) || ((tfe.block == block) && (tfe.blockMD == blockMD)))
				{
					items.add(new ItemStack(tfe.item, 1, tfe.itemMD));
				}
			}
		}
		return items;
	}

	public List<Integer> getApplicableChanceList(Block block, int blockMD, int level)
	{
		List<Integer> chance = new ArrayList<Integer>();
		for (TreasureFinderEntry tfe : entries)
		{
			if (tfe.miniumLevel <= level)
			{
				if (((tfe.block == null) && ((block == Blocks.dirt) || (block == Blocks.grass))) || ((tfe.block == block) && (tfe.blockMD == blockMD)))
				{
					chance.add(Integer.valueOf(tfe.chance));
				}
			}
		}
		return chance;
	}

	@Override
	public String toConfigurationString()
	{
		String configString = "";
		for (int i = 0; i < entries.size(); i++)
		{
			configString = configString + "," + ((TreasureFinderEntry) entries.get(i)).toConfigurationString();
		}
		return configString;
	}

	@Override
	public void fromConfigurationString(String configString)
	{
		entries.clear();
		String[] configStringSplit = configString.split(",");
		for (int i = 0; i < configStringSplit.length; i++)
		{
			entries.add(new TreasureFinderEntry(configStringSplit[i]));
		}
	}

	@Override
	public int getCost(int level)
	{
		int cost = 170;
		if (level == 1)
		{
			cost = 370;
		}
		else if (level == 2)
		{
			cost = 820;
		}
		return cost;
	}

	public int getLimit()
	{
		return 3;
	}

	@Override
	protected float getBonus(int level)
	{
		// TODO 自动生成的方法存根
		return 0;
	}
}