package net.infstudio.goki.common.network.packet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.channel.ChannelHandlerContext;
import net.infstudio.goki.common.config.GokiConfig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

public class PacketSyncStatConfig implements GokiPacket {
    private static final Gson GSON = new GsonBuilder().registerTypeAdapter(ResourceLocation.class, new ResourceLocation.Serializer()).create();

    @Override
    public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
        try (ByteBufOutputStream outputStream = new ByteBufOutputStream(buffer)) {
            outputStream.writeUTF(GSON.toJson(GokiConfig.globalModifiers));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
        try (ByteBufInputStream inputStream = new ByteBufInputStream(buffer)) {
            GokiConfig.globalModifiers = GSON.fromJson(inputStream.readUTF(), GokiConfig.GlobalModifiers.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleClientSide(EntityPlayer player) {
    }

    @Override
    public void handleServerSide(EntityPlayer player) {
    }
}