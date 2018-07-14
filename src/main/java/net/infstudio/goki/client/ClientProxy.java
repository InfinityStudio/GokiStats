package net.infstudio.goki.client;

import net.infstudio.goki.CommonProxy;
import net.infstudio.goki.handlers.GokiKeyHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class ClientProxy extends CommonProxy {
    public void registerKeybinding() {
        GokiKeyHandler keyHandler = new GokiKeyHandler();
        FMLCommonHandler.instance().bus().register(keyHandler);
    }

    public void registerSounds() {

    }
}