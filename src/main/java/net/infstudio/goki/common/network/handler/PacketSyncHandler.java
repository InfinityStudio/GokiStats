package net.infstudio.goki.common.network.handler;

import net.infstudio.goki.api.stat.StatBase;
import net.infstudio.goki.common.config.GokiConfig;
import net.infstudio.goki.common.network.GokiPacketHandler;
import net.infstudio.goki.common.network.message.*;
import net.infstudio.goki.common.stat.StatMaxHealth;
import net.infstudio.goki.common.utils.DataHelper;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PacketSyncHandler {

    public static class Stat implements IMessageHandler<C2SStatSync, IMessage> {
        @Override
        public IMessage onMessage(C2SStatSync message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().player;
            StatBase stat = StatBase.stats.get(message.stat);
            if (!stat.enabled)
                return null;
            int level = DataHelper.getPlayerStatLevel(player, stat);

            if (message.amount < 0 && level == 0) return null;

            if (level + message.amount > stat.getLimit()) return null;

            int cost = stat.getCost(level + message.amount - 1);
            int currentXP = DataHelper.getXPTotal(player.experienceLevel, player.experience);
            player.getServerWorld().addScheduledTask(() -> {
                int reverted = DataHelper.getPlayerRevertStatLevel(player, stat);
                reverted = Math.max(reverted - message.amount, 0);
                if (GokiConfig.globalModifiers.globalMaxRevertLevel < reverted && GokiConfig.globalModifiers.globalMaxRevertLevel != -1) return;
                DataHelper.setPlayerRevertStatLevel(player, stat, reverted);

                if (currentXP >= cost) {
                    DataHelper.setPlayerStatLevel(player, stat, level + message.amount);

                    // Sync to client player
                    GokiPacketHandler.CHANNEL.sendTo(new S2CStatSync(StatBase.stats.indexOf(stat), level + message.amount, reverted), player);

                    // Deal with health limit
                    if (stat instanceof StatMaxHealth) {
                        player.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH)
                                .setBaseValue(20 + level + message.amount);
                    }

                    if (message.amount <= 0) {
                        DataHelper.setPlayersExpTo(player, currentXP + (int) (stat.getCost(level + message.amount + 1) * GokiConfig.globalModifiers.globalRevertFactor));
                    } else
                        DataHelper.setPlayersExpTo(player, currentXP - cost);
                } else {
                    // Sync to client player
                    GokiPacketHandler.CHANNEL.sendTo(new S2CStatSync(StatBase.stats.indexOf(stat), level, reverted), player);
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

    public static class RequestSync implements IMessageHandler<C2SRequestStatSync, S2CSyncAll> {
        @Override
        @SideOnly(Side.SERVER)
        public S2CSyncAll onMessage(C2SRequestStatSync message, MessageContext ctx) {
            return new S2CSyncAll(ctx.getServerHandler().player);
        }
    }
}
