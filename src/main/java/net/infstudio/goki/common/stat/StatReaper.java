package net.infstudio.goki.common.stat;

import net.infstudio.goki.api.stat.StatBase;
import net.infstudio.goki.common.config.GokiConfig;
import net.infstudio.goki.common.utils.DataHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

public class StatReaper extends StatBase {
    public StatReaper(int id, String key, int limit) {
        super(id, key, limit);
    }

    @Override
    public float getBonus(int level) {
        return getFinalBonus((float) Math.pow(level, 1.0768D) * 0.0025F);
    }

    @Override
    public float[] getDescriptionFormatArguments(PlayerEntity player) {// TODO special
        return new float[]
                {DataHelper.trimDecimals(getBonus(getPlayerStatLevel(player)) * 100, 1), GokiConfig.support.reaperLimit};
        // return Helper.trimDecimals(getBonus(getPlayerStatLevel(player)) *
        // 100, 1) + "% chance to instantly kill enemies with less than " +
        // healthLimit + " health.";
    }

    @Override
    public String getLocalizedDescription(PlayerEntity player) {
        return I18n.format(this.key + ".des",
                this.getDescriptionFormatArguments(player)[0],
                this.getDescriptionFormatArguments(player)[1]);
    }

    @Override
    public boolean isEffectiveOn(Object... obj) {
        if (obj[0] != null) {
            if (!(obj[0] instanceof PlayerEntity)) {
                if ((obj[0] instanceof LivingEntity)) {
                    LivingEntity target = (LivingEntity) obj[0];

                    return target.getMaxHealth() <= GokiConfig.support.reaperLimit;
                }
            }
        }
        return false;
    }
}
