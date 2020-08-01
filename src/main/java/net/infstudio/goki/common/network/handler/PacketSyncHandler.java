package net.infstudio.goki.common.network.handler;

import net.infstudio.goki.api.stat.StatBase;
import net.infstudio.goki.common.config.GokiConfig;
import net.infstudio.goki.common.network.GokiPacketHandler;
import net.infstudio.goki.common.network.message.*;
import net.infstudio.goki.common.stat.StatMaxHealth;
import net.infstudio.goki.common.utils.DataHelper;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.function.Supplier;

public class PacketSyncHandler {

    public static void acceptC2S(C2SStatSync message, Supplier<NetworkEvent.Context> context) {
        NetworkEvent.Context ctx = context.get();
        ServerPlayerEntity player = ctx.getSender();
        StatBase stat = StatBase.stats.get(message.stat);
        if (!stat.isEnabled())
            return;
        int level = DataHelper.getPlayerStatLevel(player, stat);

        if (message.amount < 0 && level == 0) return;

        if (level + message.amount > stat.getLimit()) return;

        int cost = stat.getCost(level + message.amount - 1);
        int currentXP = DataHelper.getXPTotal(player.experienceLevel, player.experience);
        ctx.enqueueWork(() -> {
            int reverted = DataHelper.getPlayerRevertStatLevel(player, stat);
            reverted = Math.max(reverted - message.amount, 0);
            if (GokiConfig.globalModifiers.globalMaxRevertLevel < reverted && GokiConfig.globalModifiers.globalMaxRevertLevel != -1) return;
            DataHelper.setPlayerRevertStatLevel(player, stat, reverted);

            if (currentXP >= cost) {
                DataHelper.setPlayerStatLevel(player, stat, level + message.amount);

                // Sync to client player
                GokiPacketHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new S2CStatSync(StatBase.stats.indexOf(stat), level + message.amount, reverted));

                // Deal with health limit
                if (stat instanceof StatMaxHealth) {
                    player.getAttribute(SharedMonsterAttributes.MAX_HEALTH)
                            .setBaseValue(20 + stat.getBonus(level) + message.amount);
                }

                if (message.amount <= 0) {
                    DataHelper.setPlayersExpTo(player, currentXP + (int) (stat.getCost(level + message.amount + 1) * GokiConfig.globalModifiers.globalRevertFactor));
                } else
                    DataHelper.setPlayersExpTo(player, currentXP - cost);
            } else {
                // Sync to client player
                GokiPacketHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new S2CStatSync(StatBase.stats.indexOf(stat), level, reverted));
            }
        });
        ctx.setPacketHandled(true);
    }

//    public static void onXPSync(MessageXPSync message, Supplier<NetworkEvent.Context> ctx) {
//        ServerPlayerEntity player = ctx.get().getSender();
//        ctx.get().enqueueWork(() -> DataHelper.setPlayersExpTo(player, message.experience));
//        ctx.get().setPacketHandled(true);
//    }

    public static void onRequestSyncAll(C2SRequestStatSync message, Supplier<NetworkEvent.Context> ctx) {
        GokiPacketHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> ctx.get().getSender()), new S2CSyncAll(ctx.get().getSender()));
        ctx.get().setPacketHandled(true);
    }
}
