package net.infstudio.goki.common.stat.damage;

import net.infstudio.goki.api.stat.StatBase;

public class StatRoll extends StatBase {
    public StatRoll(int imgId, String key, int limit) {
        super(imgId, key, limit);
    }

    @Override
    public double getBonus(int level) {
        return getFinalBonus(level * 0.025f);
    }
}
