package net.infstudio.goki.common.network.message;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class C2SStatSync implements IMessage {
    public int stat;
    public int amount;

    public C2SStatSync() {
    }

    public C2SStatSync(int stat, int amount) {
        this.stat = stat;
        this.amount = amount;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        stat = buf.readInt();
        amount = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(stat);
        buf.writeInt(amount);
    }
}
