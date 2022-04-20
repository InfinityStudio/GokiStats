package net.infstudio.goki.common.stat.damage;

public class StatTempering extends DamageSourceProtectionStat {
    public StatTempering(int id, String key, int limit) {
        super(id, key, limit);
    }

    @Override
    public double getBonus(int level) {
        return getFinalBonus(level * 0.026F);
    }

    @Override
    public String[] getDefaultDamageSources() {
        return new String[]
                {"lava", "inFire", "onFire"};
    }
}
