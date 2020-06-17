package net.infstudio.goki;

import net.infstudio.goki.api.capability.CapabilityStat;
import net.infstudio.goki.api.stat.StatBase;
import net.infstudio.goki.api.stat.Stats;
import net.infstudio.goki.common.CommonProxy;
import net.infstudio.goki.common.StatsCommand;
import net.infstudio.goki.common.adapters.StatFix;
import net.infstudio.goki.common.config.ConfigManager;
import net.infstudio.goki.common.config.Configurable;
import net.infstudio.goki.common.config.GokiConfig;
import net.infstudio.goki.common.init.MinecraftEffects;
import net.infstudio.goki.common.loot.conditions.LevelCondition;
import net.infstudio.goki.common.utils.Reference;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.world.storage.loot.conditions.LootConditionManager;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;

@Mod(modid = Reference.MODID, useMetadata = true, updateJSON = "https://infinitystudio.github.io/Updates/gokistats.json")
public class GokiStats {
    @Mod.Instance(Reference.MODID)
    public static GokiStats instance;

    @SidedProxy(clientSide = "net.infstudio.goki.client.ClientProxy", serverSide = "net.infstudio.goki.common.CommonProxy")
    public static CommonProxy proxy;

    // Keep this same with FMLPreInitializationEvent.getModLog()
    public static final Logger log = LogManager.getLogger(Reference.MODID);

    private static final Class<?>[] loadClasses = {
            Stats.class, MinecraftEffects.class
    };

    @Mod.EventHandler
    public void construct(FMLConstructionEvent event) {
        try {
            for (Class<?> clz : loadClasses)
                Class.forName(clz.getName());
        } catch (ClassNotFoundException e) {
            log.warn("Cannot load classes, this may cause some issues", e);
        }
    }

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

        CapabilityStat.register();

        new ConfigManager(event.getModConfigurationDirectory().toPath().resolve(Reference.MODID));
        StatBase.stats.forEach(Configurable::reloadConfig);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.registerHandlers();
        FMLCommonHandler.instance().getDataFixer().registerVanillaWalker(FixTypes.PLAYER, new StatFix());
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        MinecraftEffects.STRENGTH = ForgeRegistries.POTIONS.getValue(new ResourceLocation("minecraft:strength"));
        MinecraftEffects.JUMP = ForgeRegistries.POTIONS.getValue(new ResourceLocation("minecraft:jump"));
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
