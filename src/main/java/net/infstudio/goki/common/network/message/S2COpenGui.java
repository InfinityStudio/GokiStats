package net.infstudio.goki.common.network.message;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class S2COpenGui implements IMessage {
    public int ID;

    public S2COpenGui() {
    }

    public S2COpenGui(int ID) {
        this.ID = ID;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        ID = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(ID);
    }
}
