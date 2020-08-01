package net.infstudio.goki.common.network.message;

import net.minecraft.network.PacketBuffer;

public interface IMessage {
    void fromBytes(PacketBuffer buf);
    void toBytes(PacketBuffer buf);
}
