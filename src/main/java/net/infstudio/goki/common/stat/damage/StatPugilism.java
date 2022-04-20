package net.infstudio.goki.common.stat.damage;

import net.infstudio.goki.api.stat.StatBase;

public class StatPugilism extends StatBase {
    public StatPugilism(int id, String key, int limit) {
        super(id, key, limit);
    }

    @Override
    public double getBonus(int level) {
        return level == 0 ? 0 : getFinalBonus((float) Math.pow(level, 1.03D) * 0.1816F);
    }
}
