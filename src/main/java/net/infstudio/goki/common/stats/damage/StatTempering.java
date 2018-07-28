package net.infstudio.goki.common.stats.damage;

public class StatTempering extends DamageSourceProtectionStat {
    public StatTempering(int id, String key, int limit) {
        super(id, key, limit);
    }

    @Override
    public float getBonus(int level) {
        return getFinalBonus(level * 0.026F);
    }

    @Override
    public String[] getDefaultDamageSources() {
        return new String[]
                {"lava", "inFire", "onFire"};
    }
}