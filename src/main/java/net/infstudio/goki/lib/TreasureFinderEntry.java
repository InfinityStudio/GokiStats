package net.infstudio.goki.lib;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class TreasureFinderEntry {
    public String block;
    public String item;
    public int blockMetadata = 0;
    public int itemMetadata = 0;
    public int minimumLevel = 0;
    public int chance = 0;

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