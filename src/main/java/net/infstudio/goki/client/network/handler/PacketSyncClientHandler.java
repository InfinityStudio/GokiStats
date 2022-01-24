package net.infstudio.goki.client.network.handler;

import net.infstudio.goki.GokiStats;
import net.infstudio.goki.api.stat.Stats;
import net.infstudio.goki.common.network.message.S2CStatSync;
import net.infstudio.goki.common.network.message.S2CSyncAll;
import net.infstudio.goki.api.stat.StatBase;
import net.infstudio.goki.common.utils.DataHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PacketSyncClientHandler {
    public static class Stat implements IMessageHandler<S2CStatSync, IMessage> {
        @Override
        @SideOnly(Side.CLIENT)
        public IMessage onMessage(S2CStatSync message, MessageContext ctx) {
            EntityPlayer player = Minecraft.getMinecraft().player;
            if (player == null)
                return null;
            StatBase stat = StatBase.stats.get(message.stat);
            Minecraft.getMinecraft().addScheduledTask(() -> {
                if (stat == Stats.MAX_HEALTH)
                    DataHelper.addMaxHealth(player, message.amount);
                DataHelper.setPlayerRevertStatLevel(player, stat, message.reverted);
                DataHelper.setPlayerStatLevel(player, stat, message.amount);
                GokiStats.log.debug("Loaded stat from server.");
            });
            return null;
        }
    }

    public static class StatAll implements IMessageHandler<S2CSyncAll, IMessage> {
        @Override
        @SideOnly(Side.CLIENT)
        public IMessage onMessage(S2CSyncAll message, MessageContext ctx) {
            EntityPlayer player = Minecraft.getMinecraft().player;
            if (player == null)
                return null;
            Minecraft.getMinecraft().addScheduledTask(() -> {
                for (int i = 0; i < message.statLevels.length; i++) {
                    DataHelper.setPlayerStatLevel(player,
                            StatBase.stats.get(i),
                            message.statLevels[i]);
                }
                for (int i = 0; i < message.revertedStatLevels.length; i++) {
                    DataHelper.setPlayerRevertStatLevel(player, StatBase.stats.get(i), message.revertedStatLevels[i]);
                }
                DataHelper.resetMaxHealth(player);
                GokiStats.log.debug("Loaded stats from server.");
            });
            return null;
        }
    }
}
