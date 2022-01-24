package net.infstudio.goki.common.handlers;

import net.infstudio.goki.common.network.GokiPacketHandler;
import net.infstudio.goki.common.network.message.S2CSyncAll;
import net.infstudio.goki.common.utils.DataHelper;
import net.infstudio.goki.common.utils.Reference;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.SERVER)
@Mod.EventBusSubscriber(value = Side.SERVER, modid = Reference.MODID)
public class SyncEventHandler {
    private static void syncPlayerData(EntityPlayer player) {
        DataHelper.resetMaxHealth(player);
        GokiPacketHandler.CHANNEL.sendTo(new S2CSyncAll(player), (EntityPlayerMP) player);
    }

    @SubscribeEvent
    public static void playerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        syncPlayerData(event.player);
    }

    @SubscribeEvent
    public static void playerChangedWorld(PlayerEvent.PlayerChangedDimensionEvent event) {
        syncPlayerData(event.player);
    }

    @SubscribeEvent
    public static void playerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        syncPlayerData(event.player);
    }
}
