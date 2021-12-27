package net.infstudio.goki.common.stat.special.leaper;

import net.infstudio.goki.common.utils.DataHelper;
import net.infstudio.goki.api.stat.Stats;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.client.resources.I18n;

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
        return ((obj[0] instanceof PlayerEntity)) && (((PlayerEntity) obj[0]).isShiftKeyDown());
    }

    @Override
    public float getSecondaryBonus(int level) {
        return getFinalBonus((float) Math.pow(level, 1.4307D));
    }

    @Override
    public float[] getDescriptionFormatArguments(PlayerEntity player) {
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
    public String getLocalizedDescription(PlayerEntity player) {
        return I18n.get("skill.gokistats." + this.key + ".text",
                this.getDescriptionFormatArguments(player)[0],
                this.getDescriptionFormatArguments(player)[1],
                this.getDescriptionFormatArguments(player)[2]);
    }
}
