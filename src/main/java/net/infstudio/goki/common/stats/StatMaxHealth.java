package net.infstudio.goki.common.stats;

import net.infstudio.goki.common.config.GokiConfig;
import net.infstudio.goki.common.utils.DataHelper;
import net.minecraft.entity.player.EntityPlayer;

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
        if (GokiConfig.globalModifiers.globalLimitMultiplier <= 0.0F)
            return 40;
        else
            return (int) (40 * this.limitMultiplier);
    }

    @Override
    public int getCost(int level) {
        return (int) ((Math.pow(level, 2D) + 12.0D + level) * GokiConfig.globalModifiers.globalCostMultiplier);
    }

    @Override
    public float[] getAppliedDescriptionVar(EntityPlayer player) {
        return new float[]
                {DataHelper.trimDecimals(getBonus(getPlayerStatLevel(player)), 0)};
    }

}
