package net.infstudio.goki.common.stats.tool;

import net.minecraft.init.Items;
import net.minecraft.item.Item;

public class StatBowmanship extends ToolSpecificStat {
    public StatBowmanship(int id, String key, int limit) {
        super(id, key, limit);
    }

    public String getConfigurationKey() {
        return "Bowmanship Tools";
    }

    public float getBonus(int level) {
        return getFinalBonus((float) Math.pow(level, 1.0895D) * 0.03F);
    }

    public String[] getDefaultSupportedItems() {
        return new String[]
                {Item.getIdFromItem(Items.BOW) + ":0"};
    }
}