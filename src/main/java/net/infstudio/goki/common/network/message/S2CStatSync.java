package net.infstudio.goki.common.network.message;

import net.minecraft.network.PacketBuffer;

public class S2CStatSync implements IMessage {
    public int stat, amount, reverted;

    public S2CStatSync() {
    }

    public S2CStatSync(int stat, int amount, int reverted) {
        this.stat = stat;
        this.amount = amount;
        this.reverted = reverted;
    }

    @Override
    public void fromBytes(PacketBuffer buf) {
        stat = buf.readInt();
        amount = buf.readInt();
        reverted = buf.readInt();
    }

    @Override
    public void toBytes(PacketBuffer buf) {
        buf.writeInt(stat);
        buf.writeInt(amount);
        buf.writeInt(reverted);
    }
}
