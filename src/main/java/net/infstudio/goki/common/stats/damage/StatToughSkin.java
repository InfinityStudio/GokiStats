package net.infstudio.goki.common.stats.damage;

public class StatToughSkin extends DamageSourceProtectionStat {
    public StatToughSkin(int id, String key, int limit) {
        super(id, key, limit);
    }

    @Override
    public float getBonus(int level) {
        return getFinalBonus(level * 0.026F);
    }

    @Override
    public String[] getDefaultDamageSources() {
        return new String[]
                {"explosion", "explosion.player"};
    }
}