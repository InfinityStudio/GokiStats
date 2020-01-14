package net.infstudio.goki.common.network.message;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class MessageXPSync implements IMessage {
    public int experience;

    public MessageXPSync() {
    }

    public MessageXPSync(int experience) {
        this.experience = experience;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.experience = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.experience);
    }
}
