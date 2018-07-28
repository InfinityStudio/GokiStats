package net.infstudio.goki.common;

import net.infstudio.goki.GokiStats;
import net.infstudio.goki.client.gui.GuiHandler;
import net.infstudio.goki.common.handlers.CommonHandler;
import net.infstudio.goki.common.handlers.TickHandler;
import net.minecraftforge.common.MinecraftForge;
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
}