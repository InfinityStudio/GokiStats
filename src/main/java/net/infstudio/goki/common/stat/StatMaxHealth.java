package net.infstudio.goki.common.stat;

import net.infstudio.goki.api.stat.StatBase;
import net.infstudio.goki.common.config.GokiConfig;
import net.infstudio.goki.common.utils.DataHelper;
import net.minecraft.entity.player.PlayerEntity;

public class StatMaxHealth extends StatBase {

    public StatMaxHealth(int id, String key, int limit) {
        super(id, key, limit);
    }

    @Override
    public float getBonus(int level) {
        return getFinalBonus(level);
    }

    @Override
    public int getLimit() {
        int limit = 16;
        if (config.maxLevel.get() > 0) return config.maxLevel.get();
        if (GokiConfig.SERVER.globalLimitMultiplier.get() <= 0) {
            return limit;
        }
        return (int) (limit * this.limitMultiplier * GokiConfig.SERVER.globalLimitMultiplier.get());
    }

    @Override
    public int getCost(int level) {
        return (int) getFinalBonus((float) ((Math.pow(level, 2D) + 12.0D + level) * GokiConfig.SERVER.globalCostMultiplier.get()));
    }

    @Override
    public float[] getDescriptionFormatArguments(PlayerEntity player) {
        return new float[]
                {DataHelper.trimDecimals(getBonus(getPlayerStatLevel(player)), 0)};
    }

}
