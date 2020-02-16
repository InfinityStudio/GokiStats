package net.infstudio.goki.api.stat;

import java.util.Objects;

public class StatState {
    public Stat stat;
    public int level, revertedLevel;

    public StatState(Stat stat, int level) {
        this.stat = stat;
        this.level = level;
    }

    public StatState(Stat stat, int level, int revertedLevel) {
        this.stat = stat;
        this.level = level;
        this.revertedLevel = revertedLevel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StatState statState = (StatState) o;
        return level == statState.level &&
                revertedLevel == statState.revertedLevel &&
                Objects.equals(stat, statState.stat);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stat, level, revertedLevel);
    }
}
