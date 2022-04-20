package net.infstudio.goki.common.stat.special;

import net.infstudio.goki.api.stat.StatSpecial;
import net.infstudio.goki.api.stat.StatSpecialBase;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.entity.player.Player;

public class StatFurnaceFinesse extends StatSpecialBase implements StatSpecial {
    public StatFurnaceFinesse(int id, String key, int limit) {
        super(id, key, limit);
        this.setEnabled(false); // FIXME Find a better way to implement it
    }

    @Override
    public double getBonus(int level) {
        return Math.min(getFinalBonus(level / 5.0F), 199.0F) + 1.0F;
    }

    @Override
    public double getSecondaryBonus(int level) {
        return Math.min(getFinalBonus(level), 100.0F);
    }

    @Override
    public double[] getDescriptionFormatArguments(Player player) {
        // TODO special
        int level = getPlayerStatLevel(player);
        return new double[]
                {getBonus(level), getSecondaryBonus(level)};
        // return "Smelt " + getBonus(amount) + " ticks faster " +
        // getSecondaryBonus(amount) + "% of the time while using a furnace.";
    }

    @Override
    public String getLocalizedDescription(Player player) {
        if (isEnabled())
            return I18n.get("skill.gokistats." + this.key + ".text",
                this.getDescriptionFormatArguments(player)[0],
                this.getDescriptionFormatArguments(player)[1]);
        else return super.getLocalizedDescription(player);
    }
}
