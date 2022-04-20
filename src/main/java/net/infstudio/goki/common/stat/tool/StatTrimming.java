package net.infstudio.goki.common.stat.tool;

import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.Tags;

public class StatTrimming extends ToolSpecificStat {
    private static final Tag<Item> TAG = Tags.Items.SHEARS;

    public StatTrimming(int id, String key, int limit) {
        super(id, key, limit);
    }

    @Override
    public Tag<Item> getTag() {
        return TAG;
    }

    @Override
    public float getBonus(int level) {
        return getFinalBonus(level * 0.1F);
    }
}
