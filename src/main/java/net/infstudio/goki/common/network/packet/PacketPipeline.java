package net.infstudio.goki.common.network.packet;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.FMLEmbeddedChannel;
import net.minecraftforge.fml.common.network.FMLOutboundHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

@ChannelHandler.Sharable
public class PacketPipeline extends MessageToMessageCodec<FMLProxyPacket, GokiPacket> {
    private EnumMap<Side, FMLEmbeddedChannel> channels;
    private LinkedList<Class<? extends GokiPacket>> packets = new LinkedList<>();
    private boolean isPostInitialised = false;

    public boolean registerPacket(Class<? extends GokiPacket> clazz) {
        if (this.packets.size() > 256) {
            return false;
        }

        if (this.packets.contains(clazz)) {
            return false;
        }

        if (this.isPostInitialised) {
            return false;
        }

        this.packets.add(clazz);
        return true;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, GokiPacket msg, List<Object> out) {
        ByteBuf buffer = Unpooled.buffer();
        Class<? extends GokiPacket> clazz = msg.getClass();
        if (!this.packets.contains(msg.getClass())) {
            throw new NullPointerException("No Packet Registered for: " + msg.getClass().getCanonicalName());
        }

        byte discriminator = (byte) this.packets.indexOf(clazz);
        buffer.writeByte(discriminator);
        msg.encodeInto(ctx, buffer);

        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            GZIPOutputStream outputStream = new GZIPOutputStream(byteArrayOutputStream, true);
            outputStream.write(buffer.array());
            outputStream.close();
            byteArrayOutputStream.close();

            FMLProxyPacket proxyPacket = new FMLProxyPacket(new PacketBuffer(Unpooled.wrappedBuffer(byteArrayOutputStream.toByteArray())), ctx.channel().attr(NetworkRegistry.FML_CHANNEL).get());
            out.add(proxyPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, FMLProxyPacket msg, List<Object> out)
            throws Exception {
        ByteBuf payload = msg.payload();

        try {
            GZIPInputStream inputStream = new GZIPInputStream(new ByteBufInputStream(payload));
            ByteBuf tempBuf = Unpooled.buffer();
            ByteBufOutputStream outputStream = new ByteBufOutputStream(tempBuf);
            while (inputStream.available() != 0) {
                outputStream.write(inputStream.read());
            }
            outputStream.close();
            payload = outputStream.buffer();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte discriminator = payload.readByte();
        Class<? extends GokiPacket> clazz = this.packets.get(discriminator);
        if (clazz == null) {
            throw new NullPointerException("No packet registered for discriminator: " + discriminator);
        }

        GokiPacket pkt = clazz.newInstance();
        pkt.decodeInto(ctx, payload.slice());
        EntityPlayer player;
        boolean isClient = net.minecraftforge.fml.common.FMLCommonHandler.instance().getEffectiveSide().equals(Side.CLIENT);
        player = getPlayer(isClient, ctx);
        if (isClient) {
            pkt.handleClientSide(player);
        } else if (!isClient) {
            pkt.handleServerSide(player);
        }

        out.add(pkt);
    }

    public void initialise() {
        this.channels = NetworkRegistry.INSTANCE.newChannel("gokistats",
                this);
    }

    public void postInitialise() {

        if (this.isPostInitialised) {
            return;
        }

        this.isPostInitialised = true;

        this.packets.sort(new PacketComparator());

    }

    @SideOnly(Side.CLIENT)
    private EntityPlayer getPlayer(boolean isClient, ChannelHandlerContext ctx) {
        return isClient ? Minecraft.getMinecraft().player : ((NetHandlerPlayServer) ctx.channel().attr(NetworkRegistry.NET_HANDLER).get()).player;
    }

    public void sendToAll(GokiPacket message) {
        this.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALL);
        this.channels.get(Side.SERVER).writeAndFlush(message);
    }

    public void sendTo(GokiPacket message, EntityPlayerMP player) {
        this.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.PLAYER);
        this.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(player);
        this.channels.get(Side.SERVER).writeAndFlush(message);
    }

    public void sendToAllAround(GokiPacket message, NetworkRegistry.TargetPoint point) {
        this.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALLAROUNDPOINT);
        this.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(point);
        this.channels.get(Side.SERVER).writeAndFlush(message);
    }

    public void sendToDimension(GokiPacket message, int dimensionId) {
        this.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.DIMENSION);
        this.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(dimensionId);
        this.channels.get(Side.SERVER).writeAndFlush(message);
    }

    public void sendToServer(GokiPacket message) {
        this.channels.get(Side.CLIENT).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.TOSERVER);
        this.channels.get(Side.CLIENT).writeAndFlush(message);
    }

    class PacketComparator implements Comparator<Class<? extends GokiPacket>> {
        @Override
        public int compare(Class<? extends GokiPacket> pack1, Class<? extends GokiPacket> pack2) {
            int com = String.CASE_INSENSITIVE_ORDER.compare(pack1.getCanonicalName(),
                    pack2.getCanonicalName());
            if (com == 0) {
                com = pack1.getCanonicalName().compareTo(pack2.getCanonicalName());
            }
            return com;
        }
    }
}