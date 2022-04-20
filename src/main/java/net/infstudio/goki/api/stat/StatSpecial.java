package net.infstudio.goki.api.stat;

import net.minecraft.world.entity.player.Player;

/**
 * Special stat with a secondary bonus slot
 */
public interface StatSpecial extends Stat {
    double getSecondaryBonus(Player player);

    double getSecondaryBonus(int level);
}
