package net.infstudio.goki.common;

import net.infstudio.goki.GokiStats;
import net.infstudio.goki.client.gui.GuiHandler;
import net.infstudio.goki.common.handlers.CommonHandler;
import net.infstudio.goki.common.handlers.StatCapabilityHandler;
import net.infstudio.goki.common.handlers.TickHandler;
import net.infstudio.goki.common.init.GokiSounds;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class CommonProxy {
    public void registerHandlers() {
        MinecraftForge.EVENT_BUS.register(new GokiStats());
        MinecraftForge.EVENT_BUS.register(new GokiSounds());
        MinecraftForge.EVENT_BUS.register(new CommonHandler());
        MinecraftForge.EVENT_BUS.register(new TickHandler());
        MinecraftForge.EVENT_BUS.register(new StatCapabilityHandler());

        NetworkRegistry.INSTANCE.registerGuiHandler(GokiStats.instance,
                new GuiHandler());
    }
}
