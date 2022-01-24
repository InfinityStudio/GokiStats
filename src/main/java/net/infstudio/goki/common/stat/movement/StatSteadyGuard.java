package net.infstudio.goki.common.stat.movement;

import net.infstudio.goki.common.config.stats.StatConfig;
import net.infstudio.goki.common.utils.DataHelper;
import net.infstudio.goki.api.stat.StatBase;
import net.minecraft.world.entity.player.Player;

public class StatSteadyGuard extends StatBase<StatConfig> {
    public StatSteadyGuard(int id, String key, int limit) {
        super(id, key, limit);
    }

    @Override
    public double getBonus(int level) {
        return Math.min(getFinalBonus((float) Math.pow(level, 1.3615D)), 100.0F);
    }

    @Override
    public double[] getDescriptionFormatArguments(Player player) {
        // TODO special
        return new double[]
                {DataHelper.trimDecimals(getBonus(player), 1)};
    }
}
