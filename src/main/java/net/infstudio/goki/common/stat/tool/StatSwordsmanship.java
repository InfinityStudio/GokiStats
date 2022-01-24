package net.infstudio.goki.common.stat.tool;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SwordItem;

public class StatSwordsmanship extends ToolSpecificStat {
    public StatSwordsmanship(int id, String key, int limit) {
        super(id, key, limit);
    }

    @Override
    public String getConfigurationKey() {
        return "Swordsmanship Tools";
    }

    @Override
    public boolean isItemSupported(ItemStack item) {
        return super.isItemSupported(item) || item.getItem() instanceof SwordItem;
    }

    @Override
    public double getBonus(int level) {
        return getFinalBonus((float) Math.pow(level, 1.0895D) * 0.03F);
    }

    @Override
    public Item[] getDefaultSupportedItems() {
        return new Item[]{Items.WOODEN_SWORD, Items.STONE_SWORD, Items.IRON_SWORD, Items.GOLDEN_SWORD, Items.DIAMOND_SWORD};
    }
}
