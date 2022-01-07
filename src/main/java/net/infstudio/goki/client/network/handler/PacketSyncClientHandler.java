package net.infstudio.goki.client.network.handler;

import net.infstudio.goki.api.stat.StatBase;
import net.infstudio.goki.api.stat.Stats;
import net.infstudio.goki.common.network.message.S2CStatSync;
import net.infstudio.goki.common.network.message.S2CSyncAll;
import net.infstudio.goki.common.utils.DataHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketSyncClientHandler {
    public static void acceptSync(S2CStatSync message, Supplier<NetworkEvent.Context> context) {
        var ctx = context.get();
        ctx.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> acceptSyncInternal(message)));
        ctx.setPacketHandled(true);
    }

    public static void acceptSyncAll(S2CSyncAll message, Supplier<NetworkEvent.Context> context) {
        var ctx = context.get();
        ctx.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> acceptSyncAllInternal(message)));
        ctx.setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    public static void acceptSyncInternal(S2CStatSync message) {
        var player = Minecraft.getInstance().player;
        if (player == null)
            return;
        var stat = StatBase.stats.get(message.stat);
        if (stat == Stats.MAX_HEALTH)
            player.getAttribute(Attributes.MAX_HEALTH)
                    .setBaseValue(20 + message.amount);
        DataHelper.setPlayerRevertStatLevel(player, stat, message.reverted);
        DataHelper.setPlayerStatLevel(player, stat, message.amount);
    }

    @OnlyIn(Dist.CLIENT)
    public static void acceptSyncAllInternal(S2CSyncAll message) {
        var player = Minecraft.getInstance().player;
        if (player == null)
            return;
        for (var i = 0; i < message.statLevels.length; i++) {
            DataHelper.setPlayerStatLevel(player,
                    StatBase.stats.get(i),
                    message.statLevels[i]);
        }
        for (var i = 0; i < message.revertedStatLevels.length; i++) {
            DataHelper.setPlayerRevertStatLevel(player, StatBase.stats.get(i), message.revertedStatLevels[i]);
        }
        player.getAttribute(Attributes.MAX_HEALTH)
                .setBaseValue(20 + DataHelper.getPlayerStatLevel(player, Stats.MAX_HEALTH));
    }
}
