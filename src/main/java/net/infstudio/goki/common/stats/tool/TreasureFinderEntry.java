package net.infstudio.goki.common.stats.tool;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class TreasureFinderEntry {
    public String block;
    public String item;
    public int blockMetadata;
    public int itemMetadata;
    public int minimumLevel;
    public int chance;

    public TreasureFinderEntry(Block block, int bMD, Item item, int iMD, int mL, int c) {
        this.block = block.getRegistryName().toString();
        this.item = item.getRegistryName().toString();
        this.blockMetadata = bMD;
        this.itemMetadata = iMD;
        this.minimumLevel = mL;
        this.chance = c;
    }

    public Block getBlock() {
        return ForgeRegistries.BLOCKS.getValue(new ResourceLocation(block));
    }

    public Item getItem() {
        return ForgeRegistries.ITEMS.getValue(new ResourceLocation(item));
    }
}