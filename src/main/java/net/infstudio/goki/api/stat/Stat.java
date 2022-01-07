package net.infstudio.goki.api.stat;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.IForgeRegistryEntry;

public interface Stat extends IForgeRegistryEntry<Stat> {
    /**
     * Return if this stat is effective on the specified objects
     * @param obj in-world objects, like ItemStack, Entity, etc
     * @return if objects meets the stat requirements
     */
    boolean isEffectiveOn(Object... obj);

    boolean isEffectiveOn(ItemStack stack, BlockPos pos, Level world);

    /**
     * Get arguments to format the description
     * @param player player instance
     * @return format arguments, most commonly the bonus and the limit of the stat
     */
    float[] getDescriptionFormatArguments(Player player);

    /**
     * Bonus to be used for this stat
     * @param level stat level
     * @return bonus
     */
    float getBonus(int level);

    /**
     * Get final bonus for a player to process the stat modifier
     * Game mechanic handler calls this
     * @param player player instance
     * @return final bonus
     */
    float getBonus(Player player);

//	abstract float getBonus(int paramInt);

    /**
     * Get final bonus applied on a game object
     * @param player player instance
     * @param paramObject game object such as ItemStack or Entity
     * @return final bonus
     */
    float getAppliedBonus(Player player, Object paramObject);

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

    boolean isEnabled();

//	public String getSimpleDescriptionString();
}
