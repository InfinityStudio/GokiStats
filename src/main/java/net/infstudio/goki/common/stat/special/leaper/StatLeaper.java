package net.infstudio.goki.common.stat.special.leaper;

import net.infstudio.goki.api.stat.StatSpecial;
import net.infstudio.goki.api.stat.StatSpecialBase;
import net.infstudio.goki.common.utils.DataHelper;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.entity.player.Player;

public abstract class StatLeaper extends StatSpecialBase implements StatSpecial {
    public StatLeaper(int id, String key, int limit) {
        super(id, key, limit);
    }

    @Override
    public double getBonus(int level) {
        return getFinalBonus((float) Math.pow(level, 1.065D) * 0.0195F);
    }

    @Override
    public double getSecondaryBonus(int level) {
        return getFinalBonus((float) Math.pow(level, 1.1D) * 0.0203F);
    }

    @Override
    public double[] getDescriptionFormatArguments(Player player) {
        // TODO speical
        return new double[]
                {DataHelper.trimDecimals(getBonus(getPlayerStatLevel(player)) * 100, 1), DataHelper.trimDecimals(getSecondaryBonus(getPlayerStatLevel(player)) * 100,
                        1)};
        // return "Jump " +
        // Helper.trimDecimals(getBonus(getPlayerStatLevel(player)) * 100, 1) +
        // "% higher and " +
        // Helper.trimDecimals(getSecondaryBonus(getPlayerStatLevel(player)) *
        // 100, 1) + "% farther when sprinting.";
    }

    @Override
    public String getLocalizedDescription(Player player) {
        return I18n.get("skill.gokistats." + this.key + ".text",
                this.getDescriptionFormatArguments(player)[0],
                this.getDescriptionFormatArguments(player)[1]);
    }
}
