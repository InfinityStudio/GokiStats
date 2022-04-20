package net.infstudio.goki.common.stat.tool;

import net.infstudio.goki.api.stat.StatBase;
import net.infstudio.goki.common.config.stats.StatConfig;
import net.infstudio.goki.common.utils.DataHelper;
import net.infstudio.goki.common.utils.Reference;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class StatMiningMagician extends StatBase<StatConfig> {
    public static final Tag<Block> MAGICIAN_ORE = BlockTags.createOptional(new ResourceLocation(Reference.MODID, "magician_ore"));
    public static final Tag<Item> MAGICIAN_ITEM = ItemTags.createOptional(new ResourceLocation(Reference.MODID, "magician_item"));

    public StatMiningMagician(int id, String key, int limit) {
        super(id, key, limit);
    }

    @Override
    public double getBonus(int level) {
        return getFinalBonus(level * 0.3F);
    }

    @Override
    public double[] getDescriptionFormatArguments(Player player) {
        return new double[]
                {DataHelper.trimDecimals(getBonus(player), 1)};
    }

    @Override
    public boolean isEffectiveOn(Object... obj) {
        if (obj[0] instanceof Block block) return MAGICIAN_ORE.contains(block);
        return false;
        // return super.needAffectedByStat(obj[0]);
    }
}
