package net.infstudio.goki.stats.tool;

import net.infstudio.goki.ItemIdMetadataTuple;
import net.infstudio.goki.ItemIdMetadataTupleComparator;
import net.infstudio.goki.lib.Reference;
import net.infstudio.goki.stats.IConfigeratable;
import net.infstudio.goki.stats.StatBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;

import java.util.ArrayList;
import java.util.List;

public abstract class ToolSpecificStat extends StatBase implements IConfigeratable {
    public List<ItemIdMetadataTuple> supportedItems = new ArrayList<>();

    public ToolSpecificStat(int id, String key, int limit) {
        super(id, key, limit);
    }

    public abstract String getConfigurationKey();

    public abstract String[] getDefaultSupportedItems();

    public void addSupportForItem(ItemStack item) {
        loadFromConfigurationFile(Reference.configuration);
        if (item == null) {
            return;
        }
        boolean hasSubtypes = item.getHasSubtypes();
        int id = Item.getIdFromItem(item.getItem());
        int meta = 0;

        if (hasSubtypes) {
            meta = item.getItemDamage();
        }
        ItemIdMetadataTuple iimt = new ItemIdMetadataTuple(id, meta);
        if (!this.supportedItems.contains(iimt)) {
            this.supportedItems.add(iimt);
        }
        saveToConfigurationFile(Reference.configuration);
    }

    public void removeSupportForItem(ItemStack item) {
        if (item != null) {
            ItemIdMetadataTupleComparator iimtc = new ItemIdMetadataTupleComparator();
            loadFromConfigurationFile(Reference.configuration);

            ItemIdMetadataTuple iimt = new ItemIdMetadataTuple(Item.getIdFromItem(item.getItem()), 0);
            if (item.getHasSubtypes()) {
                iimt.metadata = item.getItemDamage();
            }
            for (int i = 0; i < this.supportedItems.size(); i++) {
                ItemIdMetadataTuple ii = this.supportedItems.get(i);
                if (iimtc.compare(iimt, ii) == 1) {
                    this.supportedItems.remove(ii);
                    i--;
                }
            }

            saveToConfigurationFile(Reference.configuration);
        }
    }

    @Override
    public void loadFromConfigurationFile(Configuration config) {
        this.supportedItems.clear();
        String[] configStrings = Reference.configuration.get("Support",
                getConfigurationKey(),
                getDefaultSupportedItems()).getStringList();
        for (String configString : configStrings) {
            this.supportedItems.add(new ItemIdMetadataTuple(configString));
        }
    }

    @Override
    public String toConfigurationString() {
        String configString = "";
        for (ItemIdMetadataTuple supportedItem : this.supportedItems) {
            configString = configString + "," + supportedItem.toConfigString();
        }
        return configString.substring(1);
    }

    @Override
    public void saveToConfigurationFile(Configuration config) {
        String[] toolIDs = new String[this.supportedItems.size()];
        for (int i = 0; i < toolIDs.length; i++) {
            toolIDs[i] = this.supportedItems.get(i).toConfigString();
        }
        Reference.configuration.get("Support",
                getConfigurationKey(),
                getDefaultSupportedItems()).set(toolIDs);
    }

    @Override
    public void fromConfigurationString(String configString) {
        this.supportedItems.clear();
        String[] configStringSplit = configString.split(",");
        for (String aConfigStringSplit : configStringSplit) {
            this.supportedItems.add(new ItemIdMetadataTuple(aConfigStringSplit));
        }
    }

    public boolean isItemSupported(ItemStack item) {
        for (ItemIdMetadataTuple iimt : this.supportedItems) {
            if (Item.getIdFromItem(item.getItem()) == iimt.id) {
                if (item.getHasSubtypes() && item.getItemDamage() == iimt.metadata) {
                    return true;
                } else if (!item.getHasSubtypes()) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean needAffectedByStat(Object... obj) {
        if (obj[0] != null && obj[0] instanceof ItemStack) {
            ItemStack item = (ItemStack) obj[0];
            return isItemSupported(item);
        }
        return false;
    }

    @Override
    public float getAppliedBonus(EntityPlayer player, Object object) {
        if (needAffectedByStat(object))
            return getBonus(player);
        else
            return 0;
    }
}