package net.infstudio.goki.common.stat;

import net.infstudio.goki.api.stat.StatBase;
import net.infstudio.goki.common.config.GokiConfig;
import net.infstudio.goki.common.utils.DataHelper;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class StatReaper extends StatBase {
    public StatReaper(int id, String key, int limit) {
        super(id, key, limit);
    }

    @Override
    public float getBonus(int level) {
        return getFinalBonus((float) Math.pow(level, 1.0768D) * 0.0025F);
    }

    @Override
    public float[] getDescriptionFormatArguments(Player player) {// TODO special
        return new float[]
                {DataHelper.trimDecimals(getBonus(getPlayerStatLevel(player)) * 100, 1), GokiConfig.SERVER.reaperLimit.get()};
        // return Helper.trimDecimals(getBonus(getPlayerStatLevel(player)) *
        // 100, 1) + "% chance to instantly kill enemies with less than " +
        // healthLimit + " health.";
    }

    @Override
    public String getLocalizedDescription(Player player) {
        return I18n.get("skill.gokistats." + this.key + ".text",
                this.getDescriptionFormatArguments(player)[0],
                this.getDescriptionFormatArguments(player)[1]);
    }

    @Override
    public boolean isEffectiveOn(Object... obj) {
        if (obj[0] != null) {
            if (!(obj[0] instanceof Player)) {
                if ((obj[0] instanceof LivingEntity)) {
                    LivingEntity target = (LivingEntity) obj[0];

                    final int limit = GokiConfig.SERVER.reaperLimit.get();
                    return target.getMaxHealth() <= limit || limit == -1;
                }
            }
        }
        return false;
    }
}
