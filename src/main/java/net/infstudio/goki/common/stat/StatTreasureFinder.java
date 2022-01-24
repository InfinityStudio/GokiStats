package net.infstudio.goki.common.stat;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.infstudio.goki.api.stat.StatBase;
import net.infstudio.goki.common.config.stats.TreasureFinderConfig;
import net.infstudio.goki.common.stat.tool.TreasureFinderEntry;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StatTreasureFinder extends StatBase<TreasureFinderConfig> {
    public static List<TreasureFinderEntry> entries = new ArrayList<>();

    public static TreasureFinderEntry[] defaultEntries =
            {new TreasureFinderEntry(Blocks.SAND, Items.GOLD_NUGGET, 1, 10),
                    new TreasureFinderEntry(Blocks.DIRT, Items.ROTTEN_FLESH, 1, 40),
                    new TreasureFinderEntry(Blocks.DIRT, Items.BONE, 1, 20),
                    new TreasureFinderEntry(Blocks.DIRT, Items.SPIDER_EYE, 1, 10),
                    new TreasureFinderEntry(Blocks.DIRT, Items.GUNPOWDER, 1, 10),
                    new TreasureFinderEntry(Blocks.DIRT, Items.SLIME_BALL, 1, 10),
                    new TreasureFinderEntry(Blocks.SAND, Items.GLASS_BOTTLE, 1, 10),
                    new TreasureFinderEntry(Blocks.SAND, Items.CLOCK, 1, 5),
                    new TreasureFinderEntry(Blocks.SAND, Items.COMPASS, 1, 5),
                    new TreasureFinderEntry(Blocks.SAND, Items.CLAY_BALL, 1, 20),
                    new TreasureFinderEntry(Blocks.DIRT, Items.BOWL,1, 6),
                    new TreasureFinderEntry(Blocks.DIRT, Items.POTATO,1, 10),
                    new TreasureFinderEntry(Blocks.DIRT, Items.CARROT,1, 10),
                    new TreasureFinderEntry(Blocks.DIRT, Items.APPLE,1, 10),
                    new TreasureFinderEntry(Blocks.OAK_LEAVES, Items.APPLE, 1, 10),
                    new TreasureFinderEntry(Blocks.DARK_OAK_LEAVES, Items.APPLE, 1, 10),
                    new TreasureFinderEntry(Blocks.DIRT, Blocks.RED_MUSHROOM.asItem(), 1, 10),
                    new TreasureFinderEntry(Blocks.DIRT, Blocks.BROWN_MUSHROOM.asItem(), 1, 10),
                    new TreasureFinderEntry(Blocks.DIRT, Blocks.POPPY.asItem(), 1, 10),
                    new TreasureFinderEntry(Blocks.DIRT, Blocks.DANDELION.asItem(), 1, 10),
                    new TreasureFinderEntry(Blocks.DIRT, Items.STICK, 1, 100),
                    new TreasureFinderEntry(Blocks.TALL_GRASS, Items.PUMPKIN_SEEDS, 1, 30),
                    new TreasureFinderEntry(Blocks.TALL_GRASS, Items.MELON_SEEDS, 1, 30),

                    new TreasureFinderEntry(Blocks.DIRT, Items.BEEF, 2, 50),
                    new TreasureFinderEntry(Blocks.DIRT, Items.PORKCHOP, 2, 50),
                    new TreasureFinderEntry(Blocks.DIRT, Items.CHICKEN, 2, 50),
                    new TreasureFinderEntry(Blocks.SAND, Items.IRON_INGOT, 2, 10),
                    new TreasureFinderEntry(Blocks.SAND, Items.GOLD_INGOT, 2, 10),
                    new TreasureFinderEntry(Blocks.DIRT, Items.LEATHER, 2, 10),
                    new TreasureFinderEntry(Blocks.DIRT, Items.FEATHER, 2, 10),
                    new TreasureFinderEntry(Blocks.DIRT, Blocks.WHITE_WOOL.asItem(), 2, 10),
                    new TreasureFinderEntry(Blocks.SAND, Items.REDSTONE, 2, 50),
                    new TreasureFinderEntry(Blocks.DIRT, Items.BUCKET, 3, 20),
                    new TreasureFinderEntry(Blocks.DIRT, Items.MUSIC_DISC_11, 3, 2),
                    new TreasureFinderEntry(Blocks.DIRT, Items.MUSIC_DISC_13, 3, 2),
                    new TreasureFinderEntry(Blocks.DIRT, Items.MUSIC_DISC_BLOCKS, 3, 2),
                    new TreasureFinderEntry(Blocks.DIRT, Items.MUSIC_DISC_CAT, 3, 2),
                    new TreasureFinderEntry(Blocks.DIRT, Items.MUSIC_DISC_CHIRP, 3, 2),
                    new TreasureFinderEntry(Blocks.DIRT, Items.MUSIC_DISC_FAR, 3, 2),
                    new TreasureFinderEntry(Blocks.DIRT, Items.MUSIC_DISC_MALL, 3, 2),
                    new TreasureFinderEntry(Blocks.DIRT, Items.MUSIC_DISC_MELLOHI, 3, 2),
                    new TreasureFinderEntry(Blocks.DIRT, Items.MUSIC_DISC_STAL, 3, 2),
                    new TreasureFinderEntry(Blocks.DIRT, Items.MUSIC_DISC_STRAD, 3, 2),
                    new TreasureFinderEntry(Blocks.DIRT, Items.MUSIC_DISC_WAIT, 3, 2),
                    new TreasureFinderEntry(Blocks.DIRT, Items.MUSIC_DISC_WARD, 3, 2)};

    public StatTreasureFinder(int id, String key, int limit) {
        super(id, key, limit);
        Collections.addAll(entries, defaultEntries);
    }

    @Override
    public String getLocalizedDescription(Player player) {
        if (getPlayerStatLevel(player) == 0) {
            return I18n.get("skill.gokistats." + this.key + ".text");
        }
        return I18n.get("skill.gokistats." + this.key + ".upgrade");
    }

    @Override
    public TreasureFinderConfig createConfig(ForgeConfigSpec.Builder builder) {
        return new TreasureFinderConfig(builder);
    }

    public List<ItemStack> getApplicableItemStackList(Block block, int level) {
        List<ItemStack> items = new ArrayList<>();
        for (TreasureFinderEntry tfe : entries) {
            if (tfe.minimumLevel <= level) {
                if (tfe.getBlock() == null && (block == Blocks.DIRT || block == Blocks.GRASS) || tfe.getBlock() == block) {
                    items.add(new ItemStack(tfe.getItem()));
                }
            }
        }
        return items;
    }

    public IntList getApplicableChanceList(Block block, int level) {
        IntList chance = new IntArrayList();
        for (TreasureFinderEntry tfe : entries) {
            if (tfe.minimumLevel <= level) {
                if (tfe.getBlock() == null && (block == Blocks.DIRT || block == Blocks.GRASS) || tfe.getBlock() == block) {
                    chance.add(tfe.chance);
                }
            }
        }
        return chance;
    }

    @Override
    public int getCost(int level) {
        int cost = 170;
        if (level == 1) {
            cost = 370;
        } else if (level == 2) {
            cost = 820;
        }
        return cost;
    }

    public int getLimit() {
        if (config.maxLevel.get() > 3 || config.maxLevel.get() < 0) {
            return 3;
        } else {
            return config.maxLevel.get();
        }
    }

    @Override
    public double getBonus(int level) {
        return 0;
    }
}
