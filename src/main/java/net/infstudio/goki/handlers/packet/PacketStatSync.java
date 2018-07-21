package net.infstudio.goki.handlers.packet;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.infstudio.goki.lib.DataHelper;
import net.infstudio.goki.stats.StatBase;
import net.minecraft.entity.player.EntityPlayer;

public class PacketStatSync implements GokiPacket {
    private int[] statLevels;

    public PacketStatSync() {
    }

    public PacketStatSync(EntityPlayer player) {
        this.statLevels = new int[StatBase.stats.size()];
        for (int i = 0; i < this.statLevels.length; i++) {
            if (StatBase.stats.get(i) != null) {
                this.statLevels[i] = DataHelper.getPlayerStatLevel(player,
                        StatBase.stats.get(i));
            }
        }
    }

    public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
        for (int statLevel : this.statLevels) {
            buffer.writeInt(statLevel);
        }
    }

    public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
        this.statLevels = new int[StatBase.stats.size()];
        for (int i = 0; i < this.statLevels.length; i++) {
            this.statLevels[i] = buffer.readInt();
        }
    }

    public void handleClientSide(EntityPlayer player) {
        for (int i = 0; i < this.statLevels.length; i++) {
            DataHelper.setPlayerStatLevel(player,
                    StatBase.stats.get(i),
                    this.statLevels[i]);
        }
    }

    public void handleServerSide(EntityPlayer player) {
    }
}