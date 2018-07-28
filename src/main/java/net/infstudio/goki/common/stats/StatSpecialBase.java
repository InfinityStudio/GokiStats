package net.infstudio.goki.common.stats;

import net.infstudio.goki.common.utils.DataHelper;
import net.minecraft.entity.player.EntityPlayer;

public abstract class StatSpecialBase extends StatBase implements StatSpecial {
    public StatSpecialBase(int id, String key, int limit) {
        super(id, key, limit);
    }

    @Override
    public final float getSecondaryBonus(EntityPlayer player) {
        return getSecondaryBonus(DataHelper.getPlayerStatLevel(player, this));

    }

    public abstract float getSecondaryBonus(int paramInt);

    @Override
    public abstract float getBonus(int level);
}
