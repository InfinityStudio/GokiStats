package net.infstudio.goki.common.stat.tool;

import net.infstudio.goki.common.utils.Reference;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;

public class StatSwordsmanship extends ToolSpecificStat {
    private static final Tag<Item> TAG = ItemTags.createOptional(new ResourceLocation(Reference.MODID, "sword"));

    public StatSwordsmanship(int id, String key, int limit) {
        super(id, key, limit);
    }

    @Override
    public Tag<Item> getTag() {
        return TAG;
    }

    @Override
    public float getBonus(int level) {
        return getFinalBonus((float) Math.pow(level, 1.0895D) * 0.03F);
    }
}
