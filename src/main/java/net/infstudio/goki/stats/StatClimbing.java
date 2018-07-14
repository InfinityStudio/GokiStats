package net.infstudio.goki.stats;

import net.infstudio.goki.lib.Reference;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.translation.I18n;

public class StatClimbing extends Stat {
    public StatClimbing(int id, String key, int limit) {
        super(id, key, limit);
    }

    public float getBonus(int level) {
        return getFinalBonus((float) Math.pow(level, 1.1D) * 0.029F);
    }

    @Override
    public String getLocalizedDes(EntityPlayer player) {
        if (Reference.isPlayerAPILoaded) {
            return I18n.translateToLocal(this.key + ".des1");
        }
        return I18n.translateToLocalFormatted(this.key + ".des0",
                this.getAppliedDescriptionVar(player)[0]);
    }
}