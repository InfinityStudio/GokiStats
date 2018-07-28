package net.infstudio.goki.common.stats;

import net.infstudio.goki.common.config.stats.TreasureFinderConfig;
import net.infstudio.goki.common.stats.tool.TreasureFinderEntry;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StatTreasureFinder extends StatBase<TreasureFinderConfig> {
    public static List<TreasureFinderEntry> entries = new ArrayList<>();

    public static TreasureFinderEntry[] defaultEntries =
            {new TreasureFinderEntry(Blocks.SAND, 0, Items.GOLD_NUGGET, 0, 1, 10),
                    new TreasureFinderEntry(Blocks.DIRT, 0, Items.ROTTEN_FLESH, 0, 1, 40),
                    new TreasureFinderEntry(Blocks.DIRT, 0, Items.BONE, 0, 1, 20),
                    new TreasureFinderEntry(Blocks.DIRT, 0, Items.SPIDER_EYE, 0, 1, 10),
                    new TreasureFinderEntry(Blocks.DIRT, 0, Items.GUNPOWDER, 0, 1, 10),
                    new TreasureFinderEntry(Blocks.DIRT, 0, Items.SLIME_BALL, 0, 1, 10),
                    new TreasureFinderEntry(Blocks.SAND, 0, Items.GLASS_BOTTLE, 0, 1, 10),
                    new TreasureFinderEntry(Blocks.SAND, 0, Items.CLOCK, 0, 1, 5),
                    new TreasureFinderEntry(Blocks.SAND, 0, Items.COMPASS, 0, 1, 5),
                    new TreasureFinderEntry(Blocks.SAND, 0, Items.CLAY_BALL, 0, 1, 20),
                    new TreasureFinderEntry(Blocks.DIRT, 0, Items.BOWL, 0, 1, 6),
                    new TreasureFinderEntry(Blocks.DIRT, 0, Items.POTATO, 0, 1, 10),
                    new TreasureFinderEntry(Blocks.DIRT, 0, Items.CARROT, 0, 1, 10),
                    new TreasureFinderEntry(Blocks.DIRT, 0, Items.APPLE, 0, 1, 10),
                    new TreasureFinderEntry(Blocks.LEAVES, 0, Items.APPLE, 0, 1, 10),
                    new TreasureFinderEntry(Blocks.LEAVES2, 0, Items.APPLE, 0, 1, 10),
                    new TreasureFinderEntry(Blocks.DIRT, 0, Item.getItemFromBlock(Blocks.RED_MUSHROOM), 0, 1, 10),
                    new TreasureFinderEntry(Blocks.DIRT, 0, Item.getItemFromBlock(Blocks.BROWN_MUSHROOM), 0, 1, 10),
                    new TreasureFinderEntry(Blocks.DIRT, 0, Item.getItemFromBlock(Blocks.RED_FLOWER), 0, 1, 10),
                    new TreasureFinderEntry(Blocks.DIRT, 0, Item.getItemFromBlock(Blocks.YELLOW_FLOWER), 0, 1, 10),
                    new TreasureFinderEntry(Blocks.DIRT, 0, Items.STICK, 0, 1, 100),
                    new TreasureFinderEntry(Blocks.TALLGRASS, 0, Items.PUMPKIN_SEEDS, 0, 1, 30),
                    new TreasureFinderEntry(Blocks.TALLGRASS, 0, Items.MELON_SEEDS, 0, 1, 30),
                    new TreasureFinderEntry(Blocks.DIRT, 0, Items.BEEF, 0, 2, 50),
                    new TreasureFinderEntry(Blocks.DIRT, 0, Items.PORKCHOP, 0, 2, 50),
                    new TreasureFinderEntry(Blocks.DIRT, 0, Items.CHICKEN, 0, 2, 50),
                    new TreasureFinderEntry(Blocks.SAND, 0, Items.IRON_INGOT, 0, 2, 10),
                    new TreasureFinderEntry(Blocks.SAND, 0, Items.GOLD_INGOT, 0, 2, 10),
                    new TreasureFinderEntry(Blocks.DIRT, 0, Items.LEATHER, 0, 2, 10),
                    new TreasureFinderEntry(Blocks.DIRT, 0, Items.FEATHER, 0, 2, 10),
                    new TreasureFinderEntry(Blocks.DIRT, 0, Item.getItemFromBlock(Blocks.WOOL), 0, 2, 10),
                    new TreasureFinderEntry(Blocks.SAND, 0, Items.REDSTONE, 0, 2, 50),
                    new TreasureFinderEntry(Blocks.DIRT, 0, Items.BUCKET, 0, 3, 20),
                    new TreasureFinderEntry(Blocks.DIRT, 0, Items.RECORD_11, 0, 3, 2),
                    new TreasureFinderEntry(Blocks.DIRT, 0, Items.RECORD_13, 0, 3, 2),
                    new TreasureFinderEntry(Blocks.DIRT, 0, Items.RECORD_BLOCKS, 0, 3, 2),
                    new TreasureFinderEntry(Blocks.DIRT, 0, Items.RECORD_CAT, 0, 3, 2),
                    new TreasureFinderEntry(Blocks.DIRT, 0, Items.RECORD_CHIRP, 0, 3, 2),
                    new TreasureFinderEntry(Blocks.DIRT, 0, Items.RECORD_FAR, 0, 3, 2),
                    new TreasureFinderEntry(Blocks.DIRT, 0, Items.RECORD_MALL, 0, 3, 2),
                    new TreasureFinderEntry(Blocks.DIRT, 0, Items.RECORD_MELLOHI, 0, 3, 2),
                    new TreasureFinderEntry(Blocks.DIRT, 0, Items.RECORD_STAL, 0, 3, 2),
                    new TreasureFinderEntry(Blocks.DIRT, 0, Items.RECORD_STRAD, 0, 3, 2),
                    new TreasureFinderEntry(Blocks.DIRT, 0, Items.RECORD_WAIT, 0, 3, 2),
                    new TreasureFinderEntry(Blocks.DIRT, 0, Items.RECORD_WARD, 0, 3, 2)};

    public StatTreasureFinder(int id, String key, int limit) {
        super(id, key, limit);
        Collections.addAll(entries, defaultEntries);
    }

    @Override
    public String getLocalizedDes(EntityPlayer player) {
        if (getPlayerStatLevel(player) == 0) {
            return I18n.format(this.key + ".des0");
        }
        return I18n.format(this.key + ".des1");
    }

    @Override
    public void save() {
        super.save();
        getConfig().entries.clear();
        getConfig().entries.addAll(entries);
    }

    @Override
    public TreasureFinderConfig createConfig() {
        TreasureFinderConfig config = new TreasureFinderConfig();
        Collections.addAll(config.entries, defaultEntries);
        return config;
    }

    @Override
    public void reload() {
        super.reload();
        entries.clear();
        entries.addAll(getConfig().entries);
    }

    public List<ItemStack> getApplicableItemStackList(Block block, int blockMD, int level) {
        List<ItemStack> items = new ArrayList<>();
        for (TreasureFinderEntry tfe : entries) {
            if (tfe.minimumLevel <= level) {
                if (((tfe.getBlock() == null) && ((block == Blocks.DIRT) || (block == Blocks.GRASS))) || ((tfe.getBlock() == block) && (tfe.blockMetadata == blockMD))) {
                    items.add(new ItemStack(tfe.getItem(), 1, tfe.itemMetadata));
                }
            }
        }
        return items;
    }

    public List<Integer> getApplicableChanceList(Block block, int blockMD, int level) {
        List<Integer> chance = new ArrayList<>();
        for (TreasureFinderEntry tfe : entries) {
            if (tfe.minimumLevel <= level) {
                if (((tfe.getBlock() == null) && ((block == Blocks.DIRT) || (block == Blocks.GRASS))) || ((tfe.getBlock() == block) && (tfe.blockMetadata == blockMD))) {
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
        return 3;
    }

    @Override
    public float getBonus(int level) {
        return 0;
    }
}