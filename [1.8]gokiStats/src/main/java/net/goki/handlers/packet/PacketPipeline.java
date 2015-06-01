package net.goki.handlers.packet;

import net.minecraftforge.fml.common.network.FMLEmbeddedChannel;
import net.minecraftforge.fml.common.network.FMLOutboundHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;

import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.PacketBuffer;

@ChannelHandler.Sharable
public class PacketPipeline extends MessageToMessageCodec<FMLProxyPacket, AbstractPacket>
{
	private EnumMap<Side, FMLEmbeddedChannel> channels;
	private LinkedList<Class<? extends AbstractPacket>> packets = new LinkedList<Class<? extends AbstractPacket>>();
	private boolean isPostInitialised = false;
	
	public boolean registerPacket(Class<? extends AbstractPacket> clazz)
  	{
  		if (this.packets.size() > 256)
  		{
  			return false;
  		}
		
  		if (this.packets.contains(clazz))
  		{
  			return false;
  		}
		
  		if (this.isPostInitialised)
  		{
  			return false;
  		}
		
  		this.packets.add(clazz);
  		return true;
  	}
	
	@Override
	protected void encode(ChannelHandlerContext ctx, AbstractPacket msg, List<Object> out) throws Exception
 	{
		ByteBuf buffer = Unpooled.buffer();
		Class<? extends AbstractPacket> clazz = msg.getClass();
		if (!this.packets.contains(msg.getClass())) 
		{
			throw new NullPointerException("No Packet Registered for: " + msg.getClass().getCanonicalName());
		}

		byte discriminator = (byte)this.packets.indexOf(clazz);
		buffer.writeByte(discriminator);
		msg.encodeInto(ctx, buffer);
		FMLProxyPacket proxyPacket = new FMLProxyPacket(new PacketBuffer(buffer.copy()), (String)ctx.channel().attr(NetworkRegistry.FML_CHANNEL).get());
		out.add(proxyPacket);
 	}

	@Override
	protected void decode(ChannelHandlerContext ctx, FMLProxyPacket msg, List<Object> out) throws Exception
	{
		ByteBuf payload = msg.payload();
    	byte discriminator = payload.readByte();
    	Class<? extends AbstractPacket> clazz = (Class<? extends AbstractPacket>)this.packets.get(discriminator);
    	if (clazz == null) {
    		throw new NullPointerException("No packet registered for discriminator: " + discriminator);
    	}

    	AbstractPacket pkt = (AbstractPacket)clazz.newInstance();
    	pkt.decodeInto(ctx, payload.slice());
    	EntityPlayer player;
    	boolean isClient = net.minecraftforge.fml.common.FMLCommonHandler.instance().getEffectiveSide().equals(Side.CLIENT);
    	player = getClientPlayer(isClient,ctx);
    	if(isClient)
    	{
    		pkt.handleClientSide(player);
    	}
    	else if(!isClient)
    	{
			pkt.handleServerSide(player);
    	}

    	out.add(pkt);
	}

	public void initialise()
	{
		this.channels = NetworkRegistry.INSTANCE.newChannel("gokiStats", new ChannelHandler[] { this });
	}

	public void postInitialise()
	{
		
		if (this.isPostInitialised) 
		{
			return;
		}

    this.isPostInitialised = true;
    
    
    Collections.sort(this.packets, new packetComparator());
    
	}
	
	class packetComparator implements Comparator<Class<? extends AbstractPacket>>
	{
		@Override
		public int compare(Class<? extends AbstractPacket> pack1, Class<? extends AbstractPacket> pack2) 
		{
			int com = String.CASE_INSENSITIVE_ORDER.compare(pack1.getCanonicalName(), pack2.getCanonicalName());
			if (com == 0) 
	    	{
	          com = pack1.getCanonicalName().compareTo(pack2.getClass().getCanonicalName());
	        }
			return com;
		}
	}
	
	@SideOnly(Side.CLIENT)
	private EntityPlayer getClientPlayer(boolean isClient, ChannelHandlerContext ctx) 
	{
		return isClient ? Minecraft.getMinecraft().thePlayer : ((NetHandlerPlayServer)(INetHandler)ctx.channel().attr(NetworkRegistry.NET_HANDLER).get()).playerEntity;
	}

	
	public void sendToAll(AbstractPacket message)
	{
		((FMLEmbeddedChannel)this.channels.get(Side.SERVER)).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALL);
		((FMLEmbeddedChannel)this.channels.get(Side.SERVER)).writeAndFlush(message);
	}

	public void sendTo(AbstractPacket message, EntityPlayerMP player)
	{
		((FMLEmbeddedChannel)this.channels.get(Side.SERVER)).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.PLAYER);
		((FMLEmbeddedChannel)this.channels.get(Side.SERVER)).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(player);
		((FMLEmbeddedChannel)this.channels.get(Side.SERVER)).writeAndFlush(message);
	}

	public void sendToAllAround(AbstractPacket message, NetworkRegistry.TargetPoint point)
	{
		((FMLEmbeddedChannel)this.channels.get(Side.SERVER)).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALLAROUNDPOINT);
		((FMLEmbeddedChannel)this.channels.get(Side.SERVER)).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(point);
		((FMLEmbeddedChannel)this.channels.get(Side.SERVER)).writeAndFlush(message);
	}

	public void sendToDimension(AbstractPacket message, int dimensionId)
	{
		((FMLEmbeddedChannel)this.channels.get(Side.SERVER)).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.DIMENSION);
		((FMLEmbeddedChannel)this.channels.get(Side.SERVER)).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(Integer.valueOf(dimensionId));
		((FMLEmbeddedChannel)this.channels.get(Side.SERVER)).writeAndFlush(message);
	}

	public void sendToServer(AbstractPacket message)
	{
		((FMLEmbeddedChannel)this.channels.get(Side.CLIENT)).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.TOSERVER);
		((FMLEmbeddedChannel)this.channels.get(Side.CLIENT)).writeAndFlush(message);
	}
}