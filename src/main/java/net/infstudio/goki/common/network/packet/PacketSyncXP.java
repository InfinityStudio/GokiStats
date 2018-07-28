package net.infstudio.goki.common.network.packet;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;

public class PacketSyncXP implements GokiPacket {
    private float experience;
    private int experienceLevel;
    private int experienceTotal;

    public PacketSyncXP() {
    }

    PacketSyncXP(EntityPlayer player) {
        this.experience = player.experience;
        this.experienceLevel = player.experienceLevel;
        this.experienceTotal = player.experienceTotal;
    }

    public PacketSyncXP(float experience, int experienceLevel, int experienceTotal) {
        this.experience = experience;
        this.experienceLevel = experienceLevel;
        this.experienceTotal = experienceTotal;
    }

    public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
        buffer.writeFloat(this.experience);
        buffer.writeInt(this.experienceLevel);
        buffer.writeInt(this.experienceTotal);
    }

    public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
        this.experience = buffer.readFloat();
        this.experienceLevel = buffer.readInt();
        this.experienceTotal = buffer.readInt();
    }

    public void handleClientSide(EntityPlayer player) {
        player.experience = this.experience;
        player.experienceLevel = this.experienceLevel;
        player.experienceTotal = this.experienceTotal;
    }

    public void handleServerSide(EntityPlayer player) {
        player.experience = this.experience;
        player.experienceLevel = this.experienceLevel;
        player.experienceTotal = this.experienceTotal;
    }
}