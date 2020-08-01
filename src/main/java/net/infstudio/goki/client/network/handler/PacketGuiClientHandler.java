package net.infstudio.goki.client.network.handler;

import net.infstudio.goki.client.gui.GuiStats;
import net.infstudio.goki.common.network.message.S2COpenGui;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketGuiClientHandler {
    public static void acceptOpenGui(S2COpenGui message, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> Minecraft.getInstance().displayGuiScreen(new GuiStats()));
    }
}
