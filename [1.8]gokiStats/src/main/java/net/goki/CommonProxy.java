package net.goki;

import net.goki.client.gui.GuiHandler;
import net.goki.handlers.CommonHandler;
import net.goki.handlers.TickHandler;
import net.goki.lib.DataHelper;
import net.goki.lib.Reference;
import net.goki.lib.StatHelper;
import net.goki.stats.Stat;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class CommonProxy
{

	public void registerKeybinding()
	{
	}

	public void registerHandlers()
	{
		MinecraftForge.EVENT_BUS.register(new CommonHandler());

		NetworkRegistry.INSTANCE.registerGuiHandler(GokiStats.instance,
													new GuiHandler());

		FMLCommonHandler.instance().bus().register(new TickHandler());
	}

	public void iniConfig(FMLPreInitializationEvent event)
	{
		Reference.configuration = new Configuration(event.getSuggestedConfigurationFile());
		Reference.configuration.load();

		if (!Reference.configuration.get(	"Version",
											"Configuration Version",
											"v1").getString().equals("v1"))
		{
			System.err.println("gokiStats configuration file has changed! May cause errors! Delete the configuration file and relaunch.");
		}

		DataHelper.loadOptions(Reference.configuration);
		StatHelper.loadAllStatsFromConfiguration(Reference.configuration);
	}

	public void saveConfig()
	{
		if (Loader.isModLoaded("PlayerAPI"))
		{
			Reference.isPlayerAPILoaded = true;
			Stat.STAT_SWIMMING.enabled = false;
			Stat.STAT_CLIMBING.enabled = false;
		}
		StatHelper.saveAllStatsToConfiguration(Reference.configuration);
		DataHelper.saveGlobalMultipliers(Reference.configuration);
		Reference.configuration.save();
	}
}