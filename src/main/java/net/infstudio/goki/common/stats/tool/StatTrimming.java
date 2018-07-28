package net.infstudio.goki.common.stats.tool;

import net.minecraft.init.Items;
import net.minecraft.item.Item;

public class StatTrimming extends ToolSpecificStat {
    public StatTrimming(int id, String key, int limit) {
        super(id, key, limit);
    }

    @Override
    public String getConfigurationKey() {
        return "Trimming Tools";
    }

    @Override
    public float getBonus(int level) {
        return getFinalBonus(level * 0.1F);
    }

    @Override
    public String[] getDefaultSupportedItems() {
        return new String[]
                {Item.getIdFromItem(Items.SHEARS) + ":0"};
    }
}