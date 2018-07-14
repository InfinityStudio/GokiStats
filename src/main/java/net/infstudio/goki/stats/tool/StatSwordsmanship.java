package net.infstudio.goki.stats.tool;

import net.minecraft.init.Items;
import net.minecraft.item.Item;

public class StatSwordsmanship extends ToolSpecificStat {
    public StatSwordsmanship(int id, String key, int limit) {
        super(id, key, limit);
    }

    @Override
    public String getConfigurationKey() {
        return "Swordsmanship Tools";
    }

    @Override
    public float getBonus(int level) {
        return getFinalBonus((float) Math.pow(level, 1.0895D) * 0.03F);
    }

    @Override
    public String[] getDefaultSupportedItems() {
        return new String[]
                {Item.getIdFromItem(Items.WOODEN_SWORD) + ":0", Item.getIdFromItem(Items.STONE_SWORD) + ":0", Item.getIdFromItem(Items.IRON_SWORD) + ":0", Item.getIdFromItem(Items.GOLDEN_SWORD) + ":0", Item.getIdFromItem(Items.DIAMOND_SWORD) + ":0"};
    }
}