package net.infstudio.goki.stats;

import net.infstudio.goki.lib.DataHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.resources.I18n;

public abstract class StatLeaper extends StatSpecialBase implements StatSpecial {
    StatLeaper(int id, String key, int limit) {
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
    public float[] getAppliedDescriptionVar(EntityPlayer player) {
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
    public String getLocalizedDes(EntityPlayer player) {
        return I18n.format(this.key + ".des",
                this.getAppliedDescriptionVar(player)[0],
                this.getAppliedDescriptionVar(player)[1]);
    }
}