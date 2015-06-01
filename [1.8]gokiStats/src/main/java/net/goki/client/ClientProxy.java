package net.goki.client;

import net.goki.CommonProxy;
import net.goki.handlers.GokiKeyHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class ClientProxy extends CommonProxy
{
	public void registerKeybinding()
	{
		GokiKeyHandler keyHandler = new GokiKeyHandler();
		FMLCommonHandler.instance().bus().register(keyHandler);
	}

	public void registerSounds()
	{

	}
}