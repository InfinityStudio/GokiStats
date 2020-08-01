package net.infstudio.goki.common;

import com.mojang.brigadier.CommandDispatcher;
import net.infstudio.goki.common.network.GokiPacketHandler;
import net.infstudio.goki.common.network.message.S2COpenGui;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.network.PacketDistributor;

public class StatsCommand {
    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("gokistats").requires(source -> source.hasPermissionLevel(2))
                .then(Commands.literal("reload").executes(source -> {
                    source.getSource().sendFeedback(new StringTextComponent("Not Implemented"), false);
                    return 0;
                }))
                .then(Commands.literal("gui").then(Commands.argument("target", EntityArgument.player())).executes(source -> {
                    PlayerEntity player = source.getSource().asPlayer();
                    GokiPacketHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player), new S2COpenGui());
                    return 0;
                }))
        );
    }
}
