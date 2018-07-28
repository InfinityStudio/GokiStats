package net.infstudio.goki.common.stats.tool;

import net.infstudio.goki.common.stats.StatBase;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

public class StatDigging extends ToolSpecificStat {
    public StatDigging(int id, String key, int limit) {
        super(id, key, limit);
    }

    @Override
    public String getConfigurationKey() {
        return "Digging Tools";
    }

    @Override
    public float getBonus(int level) {
        return StatBase.getFinalBonus((float) Math.pow(level, 1.3D) * 0.01523F);
    }

    @Override
    public String[] getDefaultSupportedItems() {
        return new String[]
                {Item.getIdFromItem(Items.WOODEN_SHOVEL) + ":0", Item.getIdFromItem(Items.STONE_SHOVEL) + ":0", Item.getIdFromItem(Items.IRON_SHOVEL) + ":0", Item.getIdFromItem(Items.GOLDEN_SHOVEL) + ":0", Item.getIdFromItem(Items.DIAMOND_SHOVEL) + ":0"};
    }
}