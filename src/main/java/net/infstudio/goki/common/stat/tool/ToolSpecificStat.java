package net.infstudio.goki.common.stat.tool;

import net.infstudio.goki.api.stat.StatBase;
import net.infstudio.goki.common.config.stats.StatConfig;
import net.minecraft.tags.Tag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ForgeConfigSpec;

public abstract class ToolSpecificStat extends StatBase<StatConfig> {
    public ToolSpecificStat(int id, String key, int limit) {
        super(id, key, limit);
    }

    public abstract Tag<Item> getTag();

    @Override
    public StatConfig createConfig(ForgeConfigSpec.Builder builder) {
        return new StatConfig(builder);
    }

    public boolean isItemSupported(ItemStack item) {
        return item.is(getTag());
    }

    @Override
    public boolean isEffectiveOn(Object... obj) {
        if (obj[0] != null && obj[0] instanceof ItemStack item) {
            return isItemSupported(item);
        }
        return false;
    }

    @Override
    public float getAppliedBonus(Player player, Object object) {
        if (isEffectiveOn(object))
            return getBonus(player);
        else
            return 0;
    }
}
