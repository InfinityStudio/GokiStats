package net.infstudio.goki.common.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.PlayerEntity;

public interface GokiPacket {
    void encodeInto(ChannelHandlerContext paramChannelHandlerContext, ByteBuf paramByteBuf);

    void decodeInto(ChannelHandlerContext paramChannelHandlerContext, ByteBuf paramByteBuf);

    void handleClientSide(PlayerEntity paramPlayerEntity);

    void handleServerSide(PlayerEntity paramPlayerEntity);
}
