package net.infstudio.goki.common.stat.special.leaper;

import net.infstudio.goki.common.utils.DataHelper;
import net.infstudio.goki.api.stat.StatSpecial;
import net.infstudio.goki.api.stat.StatSpecialBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.resources.I18n;

public abstract class StatLeaper extends StatSpecialBase implements StatSpecial {
    public StatLeaper(int id, String key, int limit) {
        super(id, key, limit);
    }

    @Override
    public float getBonus(int level) {
        return getFinalBonus((float) Math.pow(level, 1.065D) * 0.0195F);
    }

    @Override
    public float getSecondaryBonus(int level) {
        return getFinalBonus((float) Math.pow(level, 1.1D) * 0.0203F);
    }

    @Override
    public float[] getDescriptionFormatArguments(EntityPlayer player) {
        // TODO speical
        return new float[]
                {DataHelper.trimDecimals(getBonus(getPlayerStatLevel(player)) * 100, 1), DataHelper.trimDecimals(getSecondaryBonus(getPlayerStatLevel(player)) * 100,
                        1)};
        // return "Jump " +
        // Helper.trimDecimals(getBonus(getPlayerStatLevel(player)) * 100, 1) +
        // "% higher and " +
        // Helper.trimDecimals(getSecondaryBonus(getPlayerStatLevel(player)) *
        // 100, 1) + "% farther when sprinting.";
    }

    @Override
    public String getLocalizedDescription(EntityPlayer player) {
        return I18n.format(this.key + ".des",
                this.getDescriptionFormatArguments(player)[0],
                this.getDescriptionFormatArguments(player)[1]);
    }
}
