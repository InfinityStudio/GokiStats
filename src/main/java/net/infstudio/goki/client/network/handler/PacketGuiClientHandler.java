package net.infstudio.goki.client.network.handler;

import net.infstudio.goki.client.gui.GuiStats;
import net.infstudio.goki.common.network.message.S2COpenGui;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketGuiClientHandler {
    public static void acceptOpenGui(S2COpenGui message, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> openGuiInternal(context.get())));
        context.get().setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    public static void openGuiInternal(NetworkEvent.Context context) {
        Minecraft.getInstance().displayGuiScreen(new GuiStats());
    }
}
