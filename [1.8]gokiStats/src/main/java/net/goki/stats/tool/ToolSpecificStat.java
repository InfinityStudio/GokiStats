package net.goki.stats.tool;

import java.util.ArrayList;
import java.util.List;

import net.goki.ItemIdMetadataTuple;
import net.goki.ItemIdMetadataTupleComparator;
import net.goki.lib.Reference;
import net.goki.stats.IConfigeratable;
import net.goki.stats.Stat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;

public abstract class ToolSpecificStat extends Stat implements IConfigeratable
{
	public List<ItemIdMetadataTuple> supportedItems = new ArrayList<ItemIdMetadataTuple>();

	public ToolSpecificStat(int id, String key, int limit)
	{
		super(id, key, limit);
	}

	public abstract String getConfigurationKey();

	public abstract String[] getDefaultSupportedItems();

	public void addSupportForItem(ItemStack item)
	{
		loadFromConfigurationFile(Reference.configuration);
		if (item == null)
		{
			return;
		}
		boolean hasSubtypes = item.getHasSubtypes();
		int id = Item.getIdFromItem(item.getItem());
		int meta = 0;

		if (hasSubtypes)
		{
			meta = item.getItemDamage();
		}
		ItemIdMetadataTuple iimt = new ItemIdMetadataTuple(id, meta);
		if (!this.supportedItems.contains(iimt))
		{
			this.supportedItems.add(iimt);
		}
		saveToConfigurationFile(Reference.configuration);
	}

	public void removeSupportForItem(ItemStack item)
	{
		if (item != null)
		{
			ItemIdMetadataTupleComparator iimtc = new ItemIdMetadataTupleComparator();
			loadFromConfigurationFile(Reference.configuration);

			ItemIdMetadataTuple iimt = new ItemIdMetadataTuple(Item.getIdFromItem(item.getItem()), 0);
			if (item.getHasSubtypes())
			{
				iimt.metadata = item.getItemDamage();
			}
			for (int i = 0; i < this.supportedItems.size(); i++)
			{
				ItemIdMetadataTuple ii = (ItemIdMetadataTuple) this.supportedItems.get(i);
				if (iimtc.compare(iimt, ii) == 1)
				{
					this.supportedItems.remove(ii);
					i--;
				}
			}

			saveToConfigurationFile(Reference.configuration);
		}
	}

	@Override
	public void loadFromConfigurationFile(Configuration config)
	{
		this.supportedItems.clear();
		String[] configStrings = Reference.configuration.get(	"Support",
																getConfigurationKey(),
																getDefaultSupportedItems()).getStringList();
		for (int i = 0; i < configStrings.length; i++)
		{
			this.supportedItems.add(new ItemIdMetadataTuple(configStrings[i]));
		}
	}

	@Override
	public String toConfigurationString()
	{
		String configString = "";
		for (int i = 0; i < this.supportedItems.size(); i++)
		{
			configString = configString + "," + ((ItemIdMetadataTuple) this.supportedItems.get(i)).toConfigString();
		}
		return configString.substring(1);
	}

	@Override
	public void saveToConfigurationFile(Configuration config)
	{
		String[] toolIDs = new String[this.supportedItems.size()];
		for (int i = 0; i < toolIDs.length; i++)
		{
			toolIDs[i] = ((ItemIdMetadataTuple) this.supportedItems.get(i)).toConfigString();
		}
		Reference.configuration.get("Support",
									getConfigurationKey(),
									getDefaultSupportedItems()).set(toolIDs);
	}

	@Override
	public void fromConfigurationString(String configString)
	{
		this.supportedItems.clear();
		String[] configStringSplit = configString.split(",");
		for (int i = 0; i < configStringSplit.length; i++)
		{
			this.supportedItems.add(new ItemIdMetadataTuple(configStringSplit[i]));
		}
	}

	public boolean isItemSupported(ItemStack item)
	{
		for (int i = 0; i < this.supportedItems.size(); i++)
		{
			ItemIdMetadataTuple iimt = (ItemIdMetadataTuple) this.supportedItems.get(i);
			if (Item.getIdFromItem(item.getItem()) == iimt.id)
			{
				if ((item.getHasSubtypes()) && (item.getItemDamage() == iimt.metadata))
				{
					return true;
				}
				if (!item.getHasSubtypes())
				{
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean needAffectedByStat(Object... obj)
	{
		if ((obj[0] != null) && ((obj[0] instanceof ItemStack)))
		{
			ItemStack item = (ItemStack) obj[0];
			return isItemSupported(item);
		}
		return false;
	}

	@Override
	public float getAppliedBonus(EntityPlayer player, Object object)
	{
		if (needAffectedByStat(object))
			return getBonus(player);
		else
			return 0;
	}
}