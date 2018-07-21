package net.infstudio.goki.stats.damage;

import net.infstudio.goki.stats.StatBase;

public class StatRoll extends StatBase {
    public StatRoll(int imgId, String key, int limit) {
        super(imgId, key, limit);
    }

    @Override
    public float getBonus(int level) {
        return getFinalBonus(level * 0.025f);
    }
}
