package net.infstudio.goki.common.stat.special;

import net.infstudio.goki.api.stat.StatSpecial;
import net.infstudio.goki.api.stat.StatSpecialBase;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.client.resources.I18n;

public class StatFurnaceFinesse extends StatSpecialBase implements StatSpecial {
    public StatFurnaceFinesse(int id, String key, int limit) {
        super(id, key, limit);
        this.setEnabled(false); // FIXME Find a better way to implement it
    }

    @Override
    public float getBonus(int level) {
        return Math.min(getFinalBonus(level / 5.0F), 199.0F) + 1.0F;
    }

    @Override
    public float getSecondaryBonus(int level) {
        return Math.min(getFinalBonus(level), 100.0F);
    }

    @Override
    public float[] getDescriptionFormatArguments(PlayerEntity player) {
        // TODO special
        int level = getPlayerStatLevel(player);
        return new float[]
                {getBonus(level), getSecondaryBonus(level)};
        // return "Smelt " + getBonus(amount) + " ticks faster " +
        // getSecondaryBonus(amount) + "% of the time while using a furnace.";
    }

    @Override
    public String getLocalizedDescription(PlayerEntity player) {
        return I18n.format(this.key + ".des",
                this.getDescriptionFormatArguments(player)[0],
                this.getDescriptionFormatArguments(player)[1]);
    }
}
