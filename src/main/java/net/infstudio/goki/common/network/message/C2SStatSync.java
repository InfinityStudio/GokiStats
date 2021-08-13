package net.infstudio.goki.common.network.message;

import net.minecraft.network.FriendlyByteBuf;

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
    public void fromBytes(FriendlyByteBuf buf) {
        stat = buf.readInt();
        amount = buf.readInt();
    }

    @Override
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(stat);
        buf.writeInt(amount);
    }
}
