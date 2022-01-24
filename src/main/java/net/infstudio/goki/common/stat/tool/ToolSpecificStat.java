package net.infstudio.goki.common.stat.tool;

import net.infstudio.goki.api.stat.StatBase;
import net.infstudio.goki.common.config.stats.ToolSpecificConfig;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class ToolSpecificStat extends StatBase<ToolSpecificConfig> {
    public List<Item> supports = new ArrayList<>();

    public ToolSpecificStat(int id, String key, int limit) {
        super(id, key, limit);
        Collections.addAll(supports, getDefaultSupportedItems());
    }

    public abstract String getConfigurationKey();

    /**
     * Default supported items, will be
     */
    public abstract Item[] getDefaultSupportedItems();

    @Override
    public ToolSpecificConfig createConfig(ForgeConfigSpec.Builder builder) {
        return new ToolSpecificConfig(builder);
    }

    public void addSupportForItem(ItemStack item) {
        supports.add(item.getItem());
    }

    public void removeSupportForItem(ItemStack item) {
        supports.remove(item.getItem());
    }

    public boolean isItemSupported(ItemStack item) {
        return supports.contains(item.getItem());
    }

    @Override
    public boolean isEffectiveOn(Object... obj) {
        if (obj[0] != null && obj[0] instanceof ItemStack) {
            ItemStack item = (ItemStack) obj[0];
            return isItemSupported(item);
        }
        return false;
    }

    @Override
    public double getAppliedBonus(Player player, Object object) {
        if (isEffectiveOn(object))
            return getBonus(player);
        else
            return 0;
    }
}
