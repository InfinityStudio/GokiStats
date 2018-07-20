package net.infstudio.goki;

import net.infstudio.goki.handlers.packet.*;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(modid = "gokistats", useMetadata = true)
public class GokiStats {
    public static final PacketPipeline packetPipeline = new PacketPipeline();

    @Mod.Instance("gokistats")
    public static GokiStats instance;

    @SidedProxy(clientSide = "net.infstudio.goki.client.ClientProxy", serverSide = "net.infstudio.goki.CommonProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        instance = this;
        proxy.initConfig(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        packetPipeline.initialise();
        packetPipeline.registerPacket(PacketStatSync.class);
        packetPipeline.registerPacket(PacketStatAlter.class);
        packetPipeline.registerPacket(PacketSyncXP.class);
        packetPipeline.registerPacket(PacketSyncStatConfig.class);

        proxy.registerHandlers();
        proxy.registerKeybinding();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        packetPipeline.postInitialise();
        proxy.saveConfig();
    }

    @Mod.EventHandler
    public void serverStart(FMLServerStartingEvent event) {
        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        ICommandManager command = server.getCommandManager();
        ServerCommandManager serverCommand = (ServerCommandManager) command;
        serverCommand.registerCommand(new StatsCommand());
        // TODO notice it's a reversion
    }
}