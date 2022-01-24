package net.infstudio.goki.common.stat.tool;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ShearsItem;

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
    public double getBonus(int level) {
        return getFinalBonus(level * 0.1F);
    }

    @Override
    public Item[] getDefaultSupportedItems() {
        return new Item[]{Items.SHEARS};
    }
}
