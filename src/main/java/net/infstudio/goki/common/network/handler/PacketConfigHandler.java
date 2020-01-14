package net.infstudio.goki.common.network.handler;

import net.infstudio.goki.common.config.GokiConfig;
import net.infstudio.goki.common.network.message.MessageConfig;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketConfigHandler implements IMessageHandler<MessageConfig, IMessage> {
    @Override
    public IMessage onMessage(MessageConfig message, MessageContext ctx) {
        GokiConfig.globalModifiers = message.globalModifiers;
        return null;
    }
}
