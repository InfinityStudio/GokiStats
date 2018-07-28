package net.infstudio.goki.common.stats.tool;

import net.infstudio.goki.common.config.stats.ToolSpecificConfig;
import net.infstudio.goki.common.stats.StatBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public abstract class ToolSpecificStat extends StatBase<ToolSpecificConfig> {
    public List<ItemIdMetadataTuple> supports = new ArrayList<>();

    public ToolSpecificStat(int id, String key, int limit) {
        super(id, key, limit);
    }

    public abstract String getConfigurationKey();

    public abstract String[] getDefaultSupportedItems();

    public ToolSpecificStat() {
        Arrays.stream(getDefaultSupportedItems()).map(ItemIdMetadataTuple::new).forEach(supports::add);
    }

    @Override
    public ToolSpecificConfig createConfig() {
        ToolSpecificConfig config = new ToolSpecificConfig();
        Arrays.stream(getDefaultSupportedItems()).map(ItemIdMetadataTuple::new).forEach(config.supports::add);
        return config;
    }

    @Override
    public void save() {
        super.save();
        getConfig().supports.clear();
        getConfig().supports.addAll(supports);
    }

    @Override
    public void reload() {
        super.reload();
        supports.clear();
        supports.addAll(getConfig().supports);
    }

    public void addSupportForItem(ItemStack item) {
        reloadConfig();
        if (item == null) {
            return;
        }
        boolean hasSubtypes = item.getHasSubtypes();
        int id = Item.getIdFromItem(item.getItem());
        int meta = 0;

        if (hasSubtypes) {
            meta = item.getItemDamage();
        }
        ItemIdMetadataTuple iimt = new ItemIdMetadataTuple(item.getItem().getRegistryName().toString(), meta);
        if (!this.supports.contains(iimt)) {
            this.supports.add(iimt);
        }
        saveConfig();
    }

    public void removeSupportForItem(ItemStack item) {
        if (item != null) {
            ItemIdMetadataTupleComparator iimtc = new ItemIdMetadataTupleComparator();
            reloadConfig();

            ItemIdMetadataTuple iimt = new ItemIdMetadataTuple(item.getItem().getRegistryName().toString(), 0);
            if (item.getHasSubtypes()) {
                iimt.metadata = item.getItemDamage();
            }
            for (int i = 0; i < this.supports.size(); i++) {
                ItemIdMetadataTuple ii = this.supports.get(i);
                if (iimtc.compare(iimt, ii) == 1) {
                    this.supports.remove(ii);
                    i--;
                }
            }

            saveConfig();
        }
    }

    public boolean isItemSupported(ItemStack item) {
        for (ItemIdMetadataTuple iimt : this.supports) {
            if (Objects.equals(item.getItem().getRegistryName().toString(), iimt.id)) {
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