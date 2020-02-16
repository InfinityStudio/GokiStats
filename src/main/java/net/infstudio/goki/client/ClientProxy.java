package net.infstudio.goki.client;

import net.infstudio.goki.common.CommonProxy;
import net.infstudio.goki.common.config.GokiConfig;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy {
    public void registerKeybinding() {
        GokiKeyHandler keyHandler = new GokiKeyHandler();
        MinecraftForge.EVENT_BUS.register(keyHandler);
    }

    @Override
    public void registerHandlers() {
        super.registerHandlers();
        if (GokiConfig.keyBindingEnabled)
            registerKeybinding();
    }
}
