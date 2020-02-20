package net.infstudio.goki.common.loot;

import net.infstudio.goki.common.config.GokiConfig;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;

import java.util.*;
import java.util.function.Function;

public class LootConfigDeserializer {
    public static final LootConfigDeserializer TREASURE_FINDER = new LootConfigDeserializer(GokiConfig.treasureFinderLootTables);
    public static final LootConfigDeserializer MINING_MAGICIAN = new LootConfigDeserializer(GokiConfig.miningMagicianLootTables);

    public List<String> configValue;
    public List<Function<IBlockState, ResourceLocation>> lootFunctions = new ArrayList<>();

    public LootConfigDeserializer(List<String> configValue) {
        this.configValue = configValue;
    }

    public Optional<ResourceLocation> getLocationForBlock(IBlockState state) {
        return lootFunctions.stream().map(it -> it.apply(state)).filter(Objects::nonNull).findFirst();
    }

    public void reload() {
        for (String s : configValue) {
            if (!s.contains("|")) throw new IllegalArgumentException(s + " does not contain | split character!");
            String[] split = s.split("\\|");
            ResourceLocation blockLocation = new ResourceLocation(split[0]);
            ResourceLocation lootLocation = new ResourceLocation(split[1]);
            lootFunctions.add(state -> {
                // Ore Dictionary lookup
                if (blockLocation.getNamespace().equals("ore")) {
                    if (Arrays.stream(OreDictionary.getOreIDs(new ItemStack(state.getBlock())))
                            .mapToObj(OreDictionary::getOreName)
                            .anyMatch(blockLocation.getPath()::equals))
                        return lootLocation;
                } else {
                    if (state.getBlock().getRegistryName().equals(blockLocation))
                        return lootLocation;
                }
                return null;
            });
        }
    }

    public static void reloadAll() {
        TREASURE_FINDER.reload();
        MINING_MAGICIAN.reload();
    }
}
