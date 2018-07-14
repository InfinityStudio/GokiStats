package net.infstudio.goki.stats;

public class StatPugilism extends Stat {
    public StatPugilism(int id, String key, int limit) {
        super(id, key, limit);
    }

    @Override
    public float getBonus(int level) {
        return getFinalBonus((float) Math.pow(level, 1.03D) * 0.1816F);
    }
}