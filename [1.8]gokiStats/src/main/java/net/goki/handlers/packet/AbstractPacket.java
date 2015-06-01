package net.goki.handlers.packet;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;

public abstract class AbstractPacket
{
  public abstract void encodeInto(ChannelHandlerContext paramChannelHandlerContext, ByteBuf paramByteBuf);

  public abstract void decodeInto(ChannelHandlerContext paramChannelHandlerContext, ByteBuf paramByteBuf);

  public abstract void handleClientSide(EntityPlayer paramEntityPlayer);

  public abstract void handleServerSide(EntityPlayer paramEntityPlayer);
}