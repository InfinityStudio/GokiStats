package net.infstudio.goki.common.network;

import net.infstudio.goki.client.network.handler.PacketGuiClientHandler;
import net.infstudio.goki.client.network.handler.PacketSyncClientHandler;
import net.infstudio.goki.common.network.handler.PacketSyncHandler;
import net.infstudio.goki.common.network.message.*;
import net.infstudio.goki.common.utils.Reference;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class GokiPacketHandler {
    public static final SimpleNetworkWrapper CHANNEL = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.MODID);

    static {
        CHANNEL.registerMessage(PacketSyncHandler.Stat.class, C2SStatSync.class, 0, Side.SERVER);
        CHANNEL.registerMessage(PacketSyncClientHandler.StatAll.class, S2CSyncAll.class, 1, Side.CLIENT);
        CHANNEL.registerMessage(PacketSyncClientHandler.Stat.class, S2CStatSync.class, 2, Side.CLIENT);
        CHANNEL.registerMessage(PacketGuiClientHandler.class, S2COpenGui.class, 3, Side.CLIENT);
        CHANNEL.registerMessage(PacketSyncHandler.RequestSync.class, C2SRequestStatSync.class, 4, Side.SERVER);
//        CHANNEL.registerMessage(new PacketConfigHandler(), MessageConfig.class, 0, Side.CLIENT);

//        CHANNEL.registerMessage(new PacketSyncHandler.XP(), MessageXPSync.class, 0, Side.SERVER);
    }
}
