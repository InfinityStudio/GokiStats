package net.infstudio.goki.common.network.message;

import net.minecraft.network.PacketBuffer;

public class S2COpenGui implements IMessage {
    public int ID;

    public S2COpenGui() {
    }

    public S2COpenGui(int ID) {
        this.ID = ID;
    }

    @Override
    public void fromBytes(PacketBuffer buf) {
        ID = buf.readInt();
    }

    @Override
    public void toBytes(PacketBuffer buf) {
        buf.writeInt(ID);
    }
}
