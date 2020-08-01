package net.infstudio.goki.common.stat.tool;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ShearsItem;

public class StatTrimming extends ToolSpecificStat {
    public StatTrimming(int id, String key, int limit) {
        super(id, key, limit);
    }

    @Override
    public String getConfigurationKey() {
        return "Trimming Tools";
    }

    @Override
    public boolean isItemSupported(ItemStack item) {
        return super.isItemSupported(item) || item.getItem() instanceof ShearsItem;
    }

    @Override
    public float getBonus(int level) {
        return getFinalBonus(level * 0.1F);
    }

    @Override
    public Item[] getDefaultSupportedItems() {
        return new Item[]{Items.SHEARS};
    }
}
