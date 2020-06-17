package net.infstudio.goki.api.stat;

import net.minecraft.entity.player.EntityPlayer;

/**
 * Special stat with a secondary bonus slot
 */
public interface StatSpecial extends Stat {
    float getSecondaryBonus(EntityPlayer player);

    float getSecondaryBonus(int level);
}
