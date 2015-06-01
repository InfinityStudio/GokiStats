package net.goki.handlers.packet;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.goki.lib.DataHelper;
import net.goki.stats.Stat;
import net.minecraft.entity.player.EntityPlayer;

public class PacketStatSync extends AbstractPacket
{
	int[] statLevels;

	public PacketStatSync()
	{
	}

	public PacketStatSync(EntityPlayer player)
	{
		this.statLevels = new int[Stat.stats.size()];
		for (int i = 0; i < this.statLevels.length; i++)
		{
			if (Stat.stats.get(i) != null)
			{
				this.statLevels[i] = DataHelper.getPlayerStatLevel(	player,
																	(Stat) Stat.stats.get(i));
			}
		}
	}

	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
	{
		for (int i = 0; i < this.statLevels.length; i++)
		{
			buffer.writeInt(this.statLevels[i]);
		}
	}

	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
	{
		this.statLevels = new int[Stat.stats.size()];
		for (int i = 0; i < this.statLevels.length; i++)
		{
			this.statLevels[i] = buffer.readInt();
		}
	}

	public void handleClientSide(EntityPlayer player)
	{
		for (int i = 0; i < this.statLevels.length; i++)
		{
			DataHelper.setPlayerStatLevel(	player,
											(Stat) Stat.stats.get(i),
											this.statLevels[i]);
		}
	}

	public void handleServerSide(EntityPlayer player)
	{
	}
}