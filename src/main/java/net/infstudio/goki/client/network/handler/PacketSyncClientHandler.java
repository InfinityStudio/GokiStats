package net.infstudio.goki.client.network.handler;

import net.infstudio.goki.api.stat.StatBase;
import net.infstudio.goki.api.stat.Stats;
import net.infstudio.goki.common.network.message.S2CStatSync;
import net.infstudio.goki.common.network.message.S2CSyncAll;
import net.infstudio.goki.common.utils.DataHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketSyncClientHandler {
    public static void acceptSync(S2CStatSync message, Supplier<NetworkEvent.Context> context) {
        NetworkEvent.Context ctx = context.get();
        ctx.enqueueWork(() -> {
            DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> acceptSyncInternal(message));
        });
        ctx.setPacketHandled(true);
    }

    public static void acceptSyncAll(S2CSyncAll message, Supplier<NetworkEvent.Context> context) {
        NetworkEvent.Context ctx = context.get();
        ctx.enqueueWork(() -> {
            DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> acceptSyncAllInternal(message));
        });
        ctx.setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    public static void acceptSyncInternal(S2CStatSync message) {
        Player player = Minecraft.getInstance().player;
        if (player == null)
            return;
        StatBase stat = StatBase.stats.get(message.stat);
        if (stat == Stats.MAX_HEALTH)
            player.getAttribute(Attributes.MAX_HEALTH)
                    .setBaseValue(20 + message.amount);
        DataHelper.setPlayerRevertStatLevel(player, stat, message.reverted);
        DataHelper.setPlayerStatLevel(player, stat, message.amount);
    }

    @OnlyIn(Dist.CLIENT)
    public static void acceptSyncAllInternal(S2CSyncAll message) {
        Player player = Minecraft.getInstance().player;
        if (player == null)
            return;
        for (int i = 0; i < message.statLevels.length; i++) {
            DataHelper.setPlayerStatLevel(player,
                    StatBase.stats.get(i),
                    message.statLevels[i]);
        }
        for (int i = 0; i < message.revertedStatLevels.length; i++) {
            DataHelper.setPlayerRevertStatLevel(player, StatBase.stats.get(i), message.revertedStatLevels[i]);
        }
        player.getAttribute(Attributes.MAX_HEALTH)
                .setBaseValue(20 + DataHelper.getPlayerStatLevel(player, Stats.MAX_HEALTH));
    }
}
