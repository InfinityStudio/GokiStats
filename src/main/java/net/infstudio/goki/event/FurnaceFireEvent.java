package net.infstudio.goki.event;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;

public class FurnaceFireEvent extends BlockEvent {
    EntityPlayer player;

    public FurnaceFireEvent(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        super(world, pos, state);
    }
}
