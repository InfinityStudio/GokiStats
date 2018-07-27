package net.infstudio.goki;

import net.infstudio.goki.config.ConfigManager;
import net.infstudio.goki.config.ConfigurableV2;
import net.infstudio.goki.handlers.packet.*;
import net.infstudio.goki.lib.Reference;
import net.infstudio.goki.stats.StatBase;
import net.infstudio.goki.stats.Stats;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;

@Mod(modid = Reference.MODID, useMetadata = true)
public class GokiStats {
    public static final PacketPipeline packetPipeline = new PacketPipeline();

    @Mod.Instance(Reference.MODID)
    public static GokiStats instance;

    @SidedProxy(clientSide = "net.infstudio.goki.client.ClientProxy", serverSide = "net.infstudio.goki.CommonProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        // Initialize stats
        try {
            Class.forName(Stats.class.getName(), true, getClass().getClassLoader());
        } catch (ClassNotFoundException ignored) {
        }
        instance = this;
        proxy.initConfig(event);
        new ConfigManager(event.getModConfigurationDirectory().toPath().resolve(Reference.MODID));
        System.out.println(StatBase.totalStats);
        StatBase.stats.forEach(ConfigurableV2::reloadConfig);
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
    public void loadComplete(FMLLoadCompleteEvent event) {
        StatBase.stats.forEach(StatBase::reloadConfig);
        ConfigManager.INSTANCE.reloadConfig();
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