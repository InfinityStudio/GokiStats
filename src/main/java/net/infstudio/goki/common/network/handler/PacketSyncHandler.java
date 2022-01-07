package net.infstudio.goki.common.network.handler;

import net.infstudio.goki.api.stat.StatBase;
import net.infstudio.goki.common.config.GokiConfig;
import net.infstudio.goki.common.network.GokiPacketHandler;
import net.infstudio.goki.common.network.message.C2SRequestStatSync;
import net.infstudio.goki.common.network.message.C2SStatSync;
import net.infstudio.goki.common.network.message.S2CStatSync;
import net.infstudio.goki.common.network.message.S2CSyncAll;
import net.infstudio.goki.common.stat.StatMaxHealth;
import net.infstudio.goki.common.utils.DataHelper;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.function.Supplier;

public class PacketSyncHandler {

    public static void acceptC2S(C2SStatSync message, Supplier<NetworkEvent.Context> context) {
        var ctx = context.get();
        var player = ctx.getSender();
        StatBase stat = StatBase.stats.get(message.stat);
        if (!stat.isEnabled())
            return;

        final int reverted = DataHelper.getPlayerRevertStatLevel(player, stat);
        if (GokiConfig.SERVER.globalMaxRevertLevel.get() < reverted - message.amount && GokiConfig.SERVER.globalMaxRevertLevel.get() != -1) return;

        final int level = DataHelper.getPlayerStatLevel(player, stat);

        if (message.amount < 0 && level == 0) return;

        if (level + message.amount > stat.getLimit()) return;

        int cost = stat.getCost(level + message.amount - 1);
        int currentXP = DataHelper.getXPTotal(player);
        ctx.enqueueWork(() -> {
            if (message.amount <= 0) { // Downgrade
                DataHelper.setPlayerRevertStatLevel(player, stat, reverted);

                player.giveExperiencePoints((int) (stat.getCost(level + message.amount) * GokiConfig.SERVER.globalRevertFactor.get()));
                // Deal with health limit
                if (stat instanceof StatMaxHealth) {
                    player.getAttribute(Attributes.MAX_HEALTH)
                            .setBaseValue(20 + stat.getBonus(level) + message.amount);
                }
                DataHelper.setPlayerStatLevel(player, stat, level + message.amount);
                GokiPacketHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new S2CStatSync(StatBase.stats.indexOf(stat), level + message.amount, reverted));
            } else if (currentXP >= cost) {
                // Deal with health limit
                if (stat instanceof StatMaxHealth) {
                    player.getAttribute(Attributes.MAX_HEALTH)
                            .setBaseValue(20 + stat.getBonus(level) + message.amount);
                }

                if (message.amount <= 0) {
//                    DataHelper.setPlayersExpTo(player, currentXP + (int) (stat.getCost(level + message.amount + 1) * GokiConfig.globalModifiers.globalRevertFactor));
                    player.giveExperiencePoints((int) (stat.getCost(level + message.amount + 1) * GokiConfig.SERVER.globalRevertFactor.get()));
                } else {
//                    DataHelper.setPlayersExpTo(player, currentXP - cost);
                    player.giveExperiencePoints(-cost);
                    DataHelper.setPlayerStatLevel(player, stat, level + message.amount);
                }
                // Sync
                GokiPacketHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new S2CStatSync(StatBase.stats.indexOf(stat), level + message.amount, reverted));
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
