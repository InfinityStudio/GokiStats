package net.infstudio.goki;

import net.infstudio.goki.client.gui.GuiHandler;
import net.infstudio.goki.handlers.CommonHandler;
import net.infstudio.goki.handlers.TickHandler;
import net.infstudio.goki.lib.Reference;
import net.infstudio.goki.lib.StatHelper;
import net.infstudio.goki.stats.Stats;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class CommonProxy {

    public void registerKeybinding() {
    }

    public void registerHandlers() {
        MinecraftForge.EVENT_BUS.register(new CommonHandler());
        MinecraftForge.EVENT_BUS.register(new TickHandler());

        NetworkRegistry.INSTANCE.registerGuiHandler(GokiStats.instance,
                new GuiHandler());
    }

    public void initConfig(FMLPreInitializationEvent event) {
        Reference.configuration = new Configuration(event.getSuggestedConfigurationFile());
        Reference.configuration.load();

        if (!Reference.configuration.get("Version",
                "Configuration Version",
                "v1").getString().equals("v1")) {
            System.err.println("gokistats configuration file has changed! May cause errors! Delete the configuration file and relaunch.");
        }

//        DataHelper.loadOptions(Reference.configuration);
        StatHelper.loadAllStatsFromConfiguration(Reference.configuration);
    }

    public void saveConfig() {
        if (Loader.isModLoaded("PlayerAPI")) {
            Reference.isPlayerAPILoaded = true;
            Stats.SWIMMING.enabled = false;
            Stats.CLIMBING.enabled = false;
        }
        StatHelper.saveAllStatsToConfiguration(Reference.configuration);
//        DataHelper.saveGlobalMultipliers(Reference.configuration);
        Reference.configuration.save();
    }
}