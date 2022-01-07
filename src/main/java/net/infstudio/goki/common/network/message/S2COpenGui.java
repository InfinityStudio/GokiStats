package net.infstudio.goki.common.network.message;

import net.minecraft.network.FriendlyByteBuf;

public class S2COpenGui implements IMessage {
    public int ID;

    public S2COpenGui() {
    }

    public S2COpenGui(int ID) {
        this.ID = ID;
    }

    @Override
    public void fromBytes(FriendlyByteBuf buf) {
        ID = buf.readInt();
    }

    @Override
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(ID);
    }
}
