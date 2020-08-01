package net.infstudio.goki.common.stat.tool;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class TreasureFinderEntry {
    public String block;
    public String item;
    public int minimumLevel;
    public int chance;

    public TreasureFinderEntry(Block block, Item item, int minimumLevel, int chance) {
        this.block = block.getRegistryName().toString();
        this.item = item.getRegistryName().toString();
        this.minimumLevel = minimumLevel;
        this.chance = chance;
    }

    public Block getBlock() {
        return ForgeRegistries.BLOCKS.getValue(new ResourceLocation(block));
    }

    public Item getItem() {
        return ForgeRegistries.ITEMS.getValue(new ResourceLocation(item));
    }
}
