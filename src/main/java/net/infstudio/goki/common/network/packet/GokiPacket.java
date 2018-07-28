package net.infstudio.goki.common.network.packet;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;

public interface GokiPacket {
    void encodeInto(ChannelHandlerContext paramChannelHandlerContext, ByteBuf paramByteBuf);

    void decodeInto(ChannelHandlerContext paramChannelHandlerContext, ByteBuf paramByteBuf);

    void handleClientSide(EntityPlayer paramEntityPlayer);

    void handleServerSide(EntityPlayer paramEntityPlayer);
}