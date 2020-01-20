package net.infstudio.goki.common.stat;

import net.infstudio.goki.api.stat.StatBase;

public class StatFocus extends StatBase {
    public StatFocus(int id, String key, int limit) {
        super(id, key, limit);
    }

    @Override
    public float getBonus(int level) {
        return getFinalBonus((float) Math.pow(level, 1.36D) * 0.03767F);
    }
}