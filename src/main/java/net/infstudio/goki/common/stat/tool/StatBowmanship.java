package net.infstudio.goki.common.stat.tool;


import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class StatBowmanship extends ToolSpecificStat {
    public StatBowmanship(int id, String key, int limit) {
        super(id, key, limit);
    }

    public String getConfigurationKey() {
        return "Bowmanship Tools";
    }

    @Override
    public boolean isItemSupported(ItemStack item) {
        return super.isItemSupported(item) || item.getItem() instanceof BowItem;
    }

    public double getBonus(int level) {
        return getFinalBonus((float) Math.pow(level, 1.0895D) * 0.03F);
    }

    public Item[] getDefaultSupportedItems() {
        return new Item[]{Items.BOW};
    }
}
