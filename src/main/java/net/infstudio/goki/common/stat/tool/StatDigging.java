package net.infstudio.goki.common.stat.tool;

import net.minecraft.world.item.*;

public class StatDigging extends ToolSpecificStat {
    public StatDigging(int id, String key, int limit) {
        super(id, key, limit);
    }

    @Override
    public String getConfigurationKey() {
        return "Digging Tools";
    }

    @Override
    public boolean isItemSupported(ItemStack item) {
        return super.isItemSupported(item) || item.getItem() instanceof ShovelItem;
    }

    @Override
    public double getBonus(int level) {
        return getFinalBonus((float) Math.pow(level, 1.3D) * 0.01523F);
    }

    @Override
    public Item[] getDefaultSupportedItems() {
        return new Item[]{Items.WOODEN_SHOVEL, Items.STONE_SHOVEL, Items.IRON_SHOVEL, Items.GOLDEN_SHOVEL, Items.DIAMOND_SHOVEL};
    }
}
