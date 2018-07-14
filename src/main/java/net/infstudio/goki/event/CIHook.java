package net.infstudio.goki.event;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

public class CIHook {
    public static void fireBlockFurnace(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        FurnaceFireEvent event = new FurnaceFireEvent(world, pos, state, player);
        MinecraftForge.EVENT_BUS.post(event);
    }
}
