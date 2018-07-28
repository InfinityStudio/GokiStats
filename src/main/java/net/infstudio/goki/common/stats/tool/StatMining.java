package net.infstudio.goki.common.stats.tool;

import net.minecraft.init.Items;
import net.minecraft.item.Item;

public class StatMining extends ToolSpecificStat {
    public StatMining(int id, String key, int limit) {
        super(id, key, limit);
    }

    @Override
    public String getConfigurationKey() {
        return "Mining Tools";
    }

    @Override
    public float getBonus(int level) {
        return getFinalBonus((float) Math.pow(level, 1.3D) * 0.01523F);
    }

    @Override
    public String[] getDefaultSupportedItems() {
        return new String[]
                {Item.getIdFromItem(Items.WOODEN_PICKAXE) + ":0", Item.getIdFromItem(Items.STONE_PICKAXE) + ":0", Item.getIdFromItem(Items.IRON_PICKAXE) + ":0", Item.getIdFromItem(Items.GOLDEN_PICKAXE) + ":0", Item.getIdFromItem(Items.DIAMOND_PICKAXE) + ":0"};
    }
}