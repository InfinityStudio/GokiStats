package net.infstudio.goki.common.stat.tool;

import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class StatChopping extends ToolSpecificStat {
    public StatChopping(int id, String key, int limit) {
        super(id, key, limit);
    }

    @Override
    public String getConfigurationKey() {
        return "Chopping Tools";
    }

    @Override
    public boolean isItemSupported(ItemStack item) {
        return super.isItemSupported(item) || item.getItem() instanceof AxeItem;
    }

    @Override
    public float getBonus(int level) {
        return getFinalBonus((float) Math.pow(level, 1.3D) * 0.01523F);
    }

    @Override
    public Item[] getDefaultSupportedItems() {
        return new Item[]{Items.WOODEN_AXE, Items.STONE_AXE, Items.IRON_AXE, Items.GOLDEN_AXE, Items.DIAMOND_AXE};
    }
}
