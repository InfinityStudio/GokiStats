package net.goki.handlers.packet;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.goki.GokiStats;
import net.goki.lib.DataHelper;
import net.goki.stats.Stat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public class PacketStatAlter extends AbstractPacket
{
	int stat;
	int amount;

	public PacketStatAlter()
	{
	}

	public PacketStatAlter(int stat, int amount)
	{
		this.stat = stat;
		this.amount = amount;
	}

	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
	{
		buffer.writeInt(this.stat);
		buffer.writeInt(this.amount);
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
	{
		this.stat = buffer.readInt();
		this.amount = buffer.readInt();
	}

	@Override
	public void handleClientSide(EntityPlayer player)
	{
	}

	@Override
	public void handleServerSide(EntityPlayer player)
	{
		if (player != null)
		{
			if (this.amount > 0)
			{
				Stat stat = (Stat) Stat.stats.get(this.stat);
				int level = DataHelper.getPlayerStatLevel(player, stat);
				int cost = stat.getCost(level + this.amount - 1);
				int currentXP = DataHelper.getXPTotal(	player.experienceLevel,
														player.experience);

				if (stat.enabled)
				{
					if (level + this.amount > stat.getLimit())
					{
						this.amount = 0;
					}

					if ((currentXP >= cost) && (this.amount != 0))
					{
						DataHelper.setPlayerStatLevel(	player,
														stat,
														level + this.amount);
						if (this.amount > 0)
						{
							DataHelper.setPlayersExpTo(player, currentXP - cost);
						}
					}
				}
			}
			else if (this.amount >= 0)
				;
			GokiStats.packetPipeline.sendTo(new PacketStatSync(player),
											(EntityPlayerMP) player);
			GokiStats.packetPipeline.sendTo(new PacketSyncXP(player),
											(EntityPlayerMP) player);
		}
	}
}