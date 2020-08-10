package net.infstudio.goki;

import net.infstudio.goki.api.capability.CapabilityStat;
import net.infstudio.goki.api.stat.Stats;
import net.infstudio.goki.common.StatsCommand;
import net.infstudio.goki.common.config.GokiConfig;
import net.infstudio.goki.common.handlers.TickHandler;
import net.infstudio.goki.common.network.GokiPacketHandler;
import net.infstudio.goki.common.utils.Reference;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Reference.MODID)
public class GokiStats {
    public static GokiStats instance;

    // Keep this same with FMLPreInitializationEvent.getModLog()
    public static final Logger log = LogManager.getLogger(Reference.MODID);

    private static final Class<?>[] loadClasses = {
            Stats.class
    };

    public GokiStats() {
        instance = this;
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        eventBus.addListener(this::construct);
        eventBus.addListener(this::registerCommands);
        GokiPacketHandler.registerMessages();
    }

    public void construct(FMLCommonSetupEvent event) {
        try {
            for (Class<?> clz : loadClasses)
                Class.forName(clz.getName());
        } catch (ClassNotFoundException e) {
            log.warn("Cannot load classes, this may cause some issues", e);
        }

        CapabilityStat.register();
        MinecraftForge.EVENT_BUS.register(instance);
        MinecraftForge.EVENT_BUS.register(new TickHandler());
        initConfig();

        if (ModList.get().isLoaded("additionalevents")) {
            Stats.TREASURE_FINDER.setEnabled(true);
            Stats.MINING_MAGICIAN.setEnabled(true);
        }
    }

    public void registerCommands(FMLServerStartingEvent event) {
        StatsCommand.register(event.getCommandDispatcher());
    }

    public void initConfig() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, GokiConfig.serverSpec);
    }
/*
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        instance = this;

        if (GokiConfig.version.equals("v2")) { // Skip v2
            try {
                Files.deleteIfExists(event.getModConfigurationDirectory().toPath().resolve(Reference.MODID + "_v2.cfg"));
                Files.list(event.getModConfigurationDirectory().toPath().resolve(Reference.MODID)).forEach(path -> {
                    try {
                        Files.deleteIfExists(path);
                    } catch (IOException e) {
                        log.warn("Unable to remove v2 config files", e);
                    }
                });
                Files.deleteIfExists(event.getModConfigurationDirectory().toPath().resolve(Reference.MODID));
                net.minecraftforge.common.config.ConfigManager.sync(Reference.MODID, Config.Type.INSTANCE);
            } catch (IOException e) {
                log.warn("Unable to remove v2 config files", e);
            }
        }

        LootConditionManager.registerCondition(new LevelCondition.Serializer(new ResourceLocation(Reference.MODID, "min_level"), LevelCondition.class));



        new ConfigManager(event.getModConfigurationDirectory().toPath().resolve(Reference.MODID));
       StatBase.stats.forEach(Configurable::reloadConfig);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.registerHandlers();
        FMLCommonHandler.instance().getDataFixer().registerVanillaWalker(FixTypes.PLAYER, new StatFix());
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
    }*/
}
