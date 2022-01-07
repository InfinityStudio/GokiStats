package net.infstudio.goki.common.stat.special.leaper;

import net.infstudio.goki.api.stat.Stats;
import net.infstudio.goki.common.utils.DataHelper;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.entity.player.Player;

public class StatStealth extends StatLeaper {
    public StatStealth(int id, String key, int limit) {
        super(id, key, limit);
    }

    @Override
    public float getBonus(int level) {
        return getFinalBonus((float) Math.pow(level, 1.3416D));
    }

    @Override
    public boolean isEffectiveOn(Object... obj) {
        return ((obj[0] instanceof Player)) && (((Player) obj[0]).isShiftKeyDown());
    }

    @Override
    public float getSecondaryBonus(int level) {
        return getFinalBonus((float) Math.pow(level, 1.4307D));
    }

    @Override
    public float[] getDescriptionFormatArguments(Player player) {
        // TODO special
        float speed = DataHelper.trimDecimals(getBonus(player), 1);
        float reapBonus = DataHelper.trimDecimals(getSecondaryBonus(player), 1);
        float reap = Stats.REAPER.getBonus(player) * 100.0F;
        float newReap = DataHelper.trimDecimals(reap + reap * reapBonus / 100.0F,
                1);
        return new float[]
                {speed, reapBonus, newReap};
        // return "Move " + speed + "% faster and reap " + reapBonus +
        // "% more often (" + newReap + "%) when sneaking.";
    }

    @Override
    public String getLocalizedDescription(Player player) {
        return I18n.get("skill.gokistats." + this.key + ".text",
                this.getDescriptionFormatArguments(player)[0],
                this.getDescriptionFormatArguments(player)[1],
                this.getDescriptionFormatArguments(player)[2]);
    }
}
