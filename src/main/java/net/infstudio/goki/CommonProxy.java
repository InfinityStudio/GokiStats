package net.infstudio.goki;

import net.infstudio.goki.client.gui.GuiHandler;
import net.infstudio.goki.handlers.CommonHandler;
import net.infstudio.goki.handlers.TickHandler;
import net.minecraftforge.common.MinecraftForge;
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
    }
}