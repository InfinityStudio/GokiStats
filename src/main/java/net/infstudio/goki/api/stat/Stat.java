package net.infstudio.goki.api.stat;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.registries.IForgeRegistryEntry;

public interface Stat extends IForgeRegistryEntry<Stat> {
    boolean needAffectedByStat(Object... obj);

    float[] getAppliedDescriptionVar(EntityPlayer player);

    /**
     * Bonus to be used for this stat
     * @param level stat level
     * @return bonus
     */
    float getBonus(int level);

    float getBonus(EntityPlayer player);

//	abstract float getBonus(int paramInt);

    float getAppliedBonus(EntityPlayer player, Object paramObject);

    /**
     * XP Cost for each level
     * @param level level
     * @return cost
     */
    int getCost(int level);

    /**
     * Stat limit
     * @return limit
     */
    int getLimit();

    String getKey();

//	public String getSimpleDescriptionString();
}
