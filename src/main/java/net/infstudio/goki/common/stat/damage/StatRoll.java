package net.infstudio.goki.common.stat.damage;

import net.infstudio.goki.api.stat.StatBase;

public class StatRoll extends StatBase {
    public StatRoll(int imgId, String key, int limit) {
        super(imgId, key, limit);
    }

    @Override
    public float getBonus(int level) {
        return level == 0 ? 0 : getFinalBonus((float) Math.pow(level, 1.03D) * 0.1816F);
    }
}
