package net.infstudio.goki.api.stat;

import net.minecraft.entity.player.PlayerEntity;

/**
 * Special stat with a secondary bonus slot
 */
public interface StatSpecial extends Stat {
    float getSecondaryBonus(PlayerEntity player);

    float getSecondaryBonus(int level);
}
