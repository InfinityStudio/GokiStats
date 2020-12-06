package net.infstudio.goki.common.stat.tool;

import net.infstudio.goki.api.stat.StatBase;
import net.infstudio.goki.common.config.stats.MiningMagicianConfig;
import net.infstudio.goki.common.utils.DataHelper;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StatMiningMagician extends StatBase<MiningMagicianConfig> {
    public static List<Block> blockEntries = new ArrayList<>();
    public static List<Item> itemEntries = new ArrayList<>();
    private static final List<Block> defaultBlockEntries =
            Arrays.asList(Blocks.COAL_ORE, Blocks.DIAMOND_ORE, Blocks.EMERALD_ORE, Blocks.GOLD_ORE, Blocks.IRON_ORE, Blocks.LAPIS_ORE, Blocks.NETHER_QUARTZ_ORE, Blocks.REDSTONE_ORE);
    private static final List<Item> defaultItemEntries =
            Arrays.asList(Items.COAL, Items.DIAMOND, Items.EMERALD, Items.GOLD_INGOT, Items.IRON_INGOT, Items.WHITE_DYE, Items.QUARTZ, Items.REDSTONE);

    public StatMiningMagician(int id, String key, int limit) {
        super(id, key, limit);
        blockEntries.addAll(defaultBlockEntries);
        itemEntries.addAll(defaultItemEntries);
    }

    @Override
    public MiningMagicianConfig createConfig(ForgeConfigSpec.Builder builder) {
        return new MiningMagicianConfig(builder);
    }

    @Override
    public float getBonus(int level) {
        return getFinalBonus(level * 0.3F);
    }

    @Override
    public float[] getDescriptionFormatArguments(PlayerEntity player) {
        return new float[]
                {DataHelper.trimDecimals(getBonus(player), 1)};
    }

    @Override
    public boolean isEffectiveOn(Object... obj) {
        if (obj[0] instanceof Item) return itemEntries.contains(obj[0]);
        else if (obj[0] instanceof Block) return blockEntries.contains(obj[0]);
        return false;
        // return super.needAffectedByStat(obj[0]);
    }
}
