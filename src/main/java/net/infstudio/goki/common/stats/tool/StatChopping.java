package net.infstudio.goki.common.stats.tool;

import net.infstudio.goki.common.stats.StatBase;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

public class StatChopping extends ToolSpecificStat {
    public StatChopping(int id, String key, int limit) {
        super(id, key, limit);
    }

    @Override
    public String getConfigurationKey() {
        return "Chopping Tools";
    }

    @Override
    public float getBonus(int level) {
        return StatBase.getFinalBonus((float) Math.pow(level, 1.3D) * 0.01523F);
    }

    @Override
    public String[] getDefaultSupportedItems() {
        return new String[]
                {Item.getIdFromItem(Items.WOODEN_AXE) + ":0", Item.getIdFromItem(Items.STONE_AXE) + ":0", Item.getIdFromItem(Items.IRON_AXE) + ":0", Item.getIdFromItem(Items.GOLDEN_AXE) + ":0", Item.getIdFromItem(Items.DIAMOND_AXE) + ":0"};
    }
}