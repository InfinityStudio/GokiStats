package net.infstudio.goki.common.network.message;

import net.minecraft.network.FriendlyByteBuf;

public interface IMessage {
    void fromBytes(FriendlyByteBuf buf);
    void toBytes(FriendlyByteBuf buf);
}
