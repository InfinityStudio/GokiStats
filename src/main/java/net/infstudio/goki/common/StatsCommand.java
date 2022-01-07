package net.infstudio.goki.common;

import net.infstudio.goki.common.network.GokiPacketHandler;
import net.infstudio.goki.common.network.message.S2COpenGui;
import net.infstudio.goki.common.utils.Reference;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

@Mod.EventBusSubscriber(modid = Reference.MODID)
public class StatsCommand {
    @SubscribeEvent
    public static void register(RegisterCommandsEvent event) {
        var dispatcher = event.getDispatcher();
        dispatcher.register(Commands.literal("gokistats").requires(source -> source.hasPermission(2))
                .then(Commands.literal("reload").executes(source -> {
                    source.getSource().sendFailure(new TextComponent("Not Implemented"));
                    return 0;
                }))
                .then(Commands.literal("gui").then(Commands.argument("target", EntityArgument.player())).executes(source -> {
                    var player = source.getSource().getPlayerOrException();
                    GokiPacketHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new S2COpenGui());
                    return 0;
                }))
        );
    }
}
