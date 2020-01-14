package net.infstudio.goki.common.network.handler;

import net.infstudio.goki.common.network.GokiPacketHandler;
import net.infstudio.goki.common.network.message.C2SStatSync;
import net.infstudio.goki.common.network.message.S2CSyncAll;
import net.infstudio.goki.common.network.message.MessageXPSync;
import net.infstudio.goki.common.network.message.S2CStatSync;
import net.infstudio.goki.common.stats.StatBase;
import net.infstudio.goki.common.stats.StatMaxHealth;
import net.infstudio.goki.common.utils.DataHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketSyncHandler {

    public static class Stat implements IMessageHandler<C2SStatSync, IMessage> {
        @Override
        public IMessage onMessage(C2SStatSync message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().player;
            StatBase stat = StatBase.stats.get(message.stat);
            if (!stat.enabled)
                return null;
            int level = DataHelper.getPlayerStatLevel(player, stat);

            if (level + message.amount > stat.getLimit()) {
                message.amount = stat.getLimit() - level;
            }

            int cost = stat.getCost(level + message.amount - 1);
            int currentXP = DataHelper.getXPTotal(player.experienceLevel, player.experience);
            player.getServerWorld().addScheduledTask(() -> {

                if (currentXP >= cost) {
                    int reverted = DataHelper.getPlayerRevertStatLevel(player, stat);
                    reverted = Math.max(reverted - message.amount, 0);
                    DataHelper.setPlayerRevertStatLevel(player, stat, reverted);

                    DataHelper.setPlayerStatLevel(player, stat, level + message.amount);
                    GokiPacketHandler.CHANNEL.sendTo(new S2CStatSync(StatBase.stats.indexOf(stat), level + message.amount, reverted), player);
                    if (stat instanceof StatMaxHealth) {
                        player.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH)
                                .setBaseValue(20 + level + message.amount);
                    }

                    DataHelper.setPlayersExpTo(player, currentXP - cost);
                }
            });
            return null;
        }
    }

    public static class XP implements IMessageHandler<MessageXPSync, IMessage> {
        @Override
        public IMessage onMessage(MessageXPSync message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().player;
            player.getServerWorld().addScheduledTask(() -> {
                DataHelper.setPlayersExpTo(player, message.experience);
            });
            return null;
        }
    }
}
