package net.infstudio.goki.client.network.handler;

import net.infstudio.goki.GokiStats;
import net.infstudio.goki.api.stat.StatBase;
import net.infstudio.goki.api.stat.Stats;
import net.infstudio.goki.common.network.message.S2CStatSync;
import net.infstudio.goki.common.network.message.S2CSyncAll;
import net.infstudio.goki.common.utils.DataHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketSyncClientHandler {
    public static void acceptSync(S2CStatSync message, Supplier<NetworkEvent.Context> context) {
        NetworkEvent.Context ctx = context.get();
        PlayerEntity player = Minecraft.getInstance().player;
        if (player == null)
            return;
        StatBase stat = StatBase.stats.get(message.stat);
        ctx.enqueueWork(() -> {
            if (stat == Stats.MAX_HEALTH)
                player.getAttribute(SharedMonsterAttributes.MAX_HEALTH)
                        .setBaseValue(20 + message.amount);
            DataHelper.setPlayerRevertStatLevel(player, stat, message.reverted);
            DataHelper.setPlayerStatLevel(player, stat, message.amount);
            GokiStats.log.debug("Loaded stat from server.");
        });
    }

    public static void acceptSyncAll(S2CSyncAll message, Supplier<NetworkEvent.Context> context) {
        NetworkEvent.Context ctx = context.get();
        PlayerEntity player = Minecraft.getInstance().player;
        if (player == null)
            return;
        ctx.enqueueWork(() -> {
            for (int i = 0; i < message.statLevels.length; i++) {
                DataHelper.setPlayerStatLevel(player,
                        StatBase.stats.get(i),
                        message.statLevels[i]);
            }
            for (int i = 0; i < message.revertedStatLevels.length; i++) {
                DataHelper.setPlayerRevertStatLevel(player, StatBase.stats.get(i), message.revertedStatLevels[i]);
            }
            player.getAttribute(SharedMonsterAttributes.MAX_HEALTH)
                    .setBaseValue(20 + DataHelper.getPlayerStatLevel(player, Stats.MAX_HEALTH));
            GokiStats.log.debug("Loaded stats from server.");
        });
    }
}
