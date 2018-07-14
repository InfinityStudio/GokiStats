package net.infstudio.goki.stats;

import net.infstudio.goki.lib.DataHelper;
import net.minecraft.entity.player.EntityPlayer;

public abstract class StatSpecial extends Stat implements IStatSpecial {
    public StatSpecial(int id, String key, int limit) {
        super(id, key, limit);
    }

    @Override
    public final float getSecondaryBonus(EntityPlayer player) {
        return getSecondaryBonus(DataHelper.getPlayerStatLevel(player, this));

    }

    public abstract float getSecondaryBonus(int paramInt);

    @Override
    protected abstract float getBonus(int level);
}
