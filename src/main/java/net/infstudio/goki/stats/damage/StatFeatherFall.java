package net.infstudio.goki.stats.damage;

import net.infstudio.goki.lib.DataHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.translation.I18n;

public class StatFeatherFall extends DamageSourceProtectionStat {
    public StatFeatherFall(int id, String key, int limit) {
        super(id, key, limit);
    }

    //	@Override
    public float getSecondaryBonus(int level) {
        return getFinalBonus(level * 0.1F);
    }

    @Override
    public float[] getAppliedDescriptionVar(EntityPlayer player) {
        // TODO special
        float height = DataHelper.getFallResistance(player) + DataHelper.trimDecimals(getSecondaryBonus(getPlayerStatLevel(player)),
                1);
        return new float[]
                {DataHelper.trimDecimals(getBonus(getPlayerStatLevel(player)) * 100, 1), height};
        // return "Take " +
        // Helper.trimDecimals(getBonus(getPlayerStatLevel(player)) * 100, 1) +
        // "% less damage from falling more than " + height + " blocks.";

    }

    @Override
    public String getLocalizedDes(EntityPlayer player) {
        return I18n.translateToLocalFormatted(this.key + ".des",
                this.getAppliedDescriptionVar(player)[0],
                this.getAppliedDescriptionVar(player)[1]);
    }

    @Override
    public String[] getDefaultDamageSources() {
        return new String[]
                {"fall"};
    }

    @Override
    protected float getBonus(int level) {
        return 0;
    }


}