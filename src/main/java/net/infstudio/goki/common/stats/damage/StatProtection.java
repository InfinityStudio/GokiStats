package net.infstudio.goki.common.stats.damage;

public class StatProtection extends DamageSourceProtectionStat {
    public StatProtection(int id, String key, int limit) {
        super(id, key, limit);
    }

    @Override
    public float getBonus(int level) {
        return getFinalBonus(level * 0.008F);
    }

    @Override
    public String[] getDefaultDamageSources() {
        return new String[]
                {"mob"};
    }
}