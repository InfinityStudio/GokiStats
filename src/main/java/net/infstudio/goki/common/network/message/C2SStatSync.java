package net.infstudio.goki.common.network.message;

import net.minecraft.network.PacketBuffer;

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
    public void fromBytes(PacketBuffer buf) {
        stat = buf.readInt();
        amount = buf.readInt();
    }

    @Override
    public void toBytes(PacketBuffer buf) {
        buf.writeInt(stat);
        buf.writeInt(amount);
    }
}
