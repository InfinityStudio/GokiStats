package net.infstudio.goki.common.network;

import net.infstudio.goki.client.network.handler.PacketGuiClientHandler;
import net.infstudio.goki.client.network.handler.PacketSyncClientHandler;
import net.infstudio.goki.common.network.handler.PacketSyncHandler;
import net.infstudio.goki.common.network.message.*;
import net.infstudio.goki.common.utils.Reference;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fmllegacy.network.NetworkDirection;
import net.minecraftforge.fmllegacy.network.NetworkRegistry;
import net.minecraftforge.fmllegacy.network.simple.SimpleChannel;

import java.util.Optional;

public class GokiPacketHandler {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(Reference.MODID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void registerMessages() {
        CHANNEL.registerMessage(0, C2SStatSync.class, C2SStatSync::toBytes,
                buffer -> { C2SStatSync message = new C2SStatSync(); message.fromBytes(buffer); return message; },
                PacketSyncHandler::acceptC2S, Optional.of(NetworkDirection.PLAY_TO_SERVER));

        CHANNEL.registerMessage(1, S2CSyncAll.class, S2CSyncAll::toBytes,
                buffer -> { S2CSyncAll message = new S2CSyncAll(); message.fromBytes(buffer); return message; }, PacketSyncClientHandler::acceptSyncAll, Optional.of(NetworkDirection.PLAY_TO_CLIENT));

        CHANNEL.registerMessage(2, S2CStatSync.class, S2CStatSync::toBytes,
                buffer -> { S2CStatSync message = new S2CStatSync(); message.fromBytes(buffer); return message; },
                PacketSyncClientHandler::acceptSync, Optional.of(NetworkDirection.PLAY_TO_CLIENT));

        CHANNEL.registerMessage(3, S2COpenGui.class, (message, buffer) -> {}, buffer -> new S2COpenGui(), PacketGuiClientHandler::acceptOpenGui, Optional.of(NetworkDirection.PLAY_TO_CLIENT));

        CHANNEL.registerMessage(4, C2SRequestStatSync.class, (message, buffer) -> {}, buffer -> new C2SRequestStatSync(), PacketSyncHandler::onRequestSyncAll, Optional.of(NetworkDirection.PLAY_TO_SERVER));
//        CHANNEL.registerMessage(new PacketConfigHandler(), MessageConfig.class, 0, Side.CLIENT);

//        CHANNEL.registerMessage(new PacketSyncHandler.XP(), MessageXPSync.class, 0, Side.SERVER);
    }
}
