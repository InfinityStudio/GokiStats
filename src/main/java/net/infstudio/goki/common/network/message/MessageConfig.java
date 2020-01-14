package net.infstudio.goki.common.network.message;

import com.google.gson.Gson;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import net.infstudio.goki.common.config.GokiConfig;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.io.IOException;

public class MessageConfig implements IMessage {
    public GokiConfig.GlobalModifiers globalModifiers = GokiConfig.globalModifiers;
    private static final Gson GSON = new Gson();

    public MessageConfig() {
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        try (ByteBufInputStream inputStream = new ByteBufInputStream(buf)) {
            globalModifiers = GSON.fromJson(inputStream.readUTF(), GokiConfig.GlobalModifiers.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        try (ByteBufOutputStream outputStream = new ByteBufOutputStream(buf)) {
            outputStream.writeUTF(GSON.toJson(globalModifiers));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
