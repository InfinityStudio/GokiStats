package net.infstudio.goki.api.stat;

import net.minecraft.entity.player.EntityPlayer;

public interface StatSpecial extends Stat {
    float getSecondaryBonus(EntityPlayer paramEntityPlayer);
}
