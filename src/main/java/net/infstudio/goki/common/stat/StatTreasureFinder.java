package net.infstudio.goki.common.stat;

import net.infstudio.goki.api.stat.StatBase;
import net.infstudio.goki.common.config.stats.StatConfig;
import net.infstudio.goki.common.config.stats.TreasureFinderConfig;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.ForgeConfigSpec;

public class StatTreasureFinder extends StatBase<StatConfig> {
    public StatTreasureFinder(int id, String key, int limit) {
        super(id, key, limit);
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
    public float getBonus(int level) {
        return 0;
    }
}
