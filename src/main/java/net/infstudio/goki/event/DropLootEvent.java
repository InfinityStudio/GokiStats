package net.infstudio.goki.event;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.Cancelable;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Fired just before a block's loot is dropped into the world.
 * Used to modify a block's drops after they're already generated.
 * <p>
 * Cancelling this event will prevent the block from spawning in any drops.
 */
@Cancelable
public class DropLootEvent extends BlockEvent {
    private final PlayerEntity player;
    private final LootContext context;
    private final List<ItemStack> drops;

    public DropLootEvent(World world, BlockPos pos, BlockState state, @Nullable LootContext context, @Nullable PlayerEntity player, List<ItemStack> drops) {
        super(world, pos, state);
        this.context = context;
        this.player = player;
        this.drops = drops;
    }

    /**
     * Get the loot context that was used to generate the dropped loot.
     *
     * @return The context that was used when generating the drops, or null if none was used.
     */
    @Nullable
    public LootContext getContext() {
        return context;
    }

    /**
     * Get the player that caused the loot to drop.
     *
     * @return The player that caused the loot to drop, or null if no player was directly involved.
     */
    @Nullable
    public PlayerEntity getPlayer() {
        return player;
    }

    /**
     * Get the list of the drops that will be added to the world after the event has processed.
     *
     * @return The list of drops to spawn, or an empty list if the event was cancelled.
     */
    public List<ItemStack> getDrops() {
        return isCanceled() ? new ArrayList<>() : drops;
    }
}
