package net.infstudio.goki.handlers.packet;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.channel.ChannelHandlerContext;
import net.infstudio.goki.lib.StatHelper;
import net.infstudio.goki.stats.StatBase;
import net.minecraft.entity.player.EntityPlayer;

import java.io.IOException;

public class PacketSyncStatConfig implements GokiPacket {
    private boolean deathLoss;
    private float newBonus;
    private float newCost;
    private float newLimit;
    private String[] statConfigStrings;

    public PacketSyncStatConfig() {
        this.deathLoss = true;
        this.newBonus = 1.0F;
        this.newCost = 1.0F;
        this.newLimit = 1.0F;
        this.statConfigStrings = new String[StatHelper.iConfigeratStat.size()];
    }

    public PacketSyncStatConfig(boolean deathLoss, float newBonus, float newCost, float newLimit) {
        this.deathLoss = deathLoss;
        this.newBonus = newBonus;
        this.newCost = newCost;
        this.newLimit = newLimit;
        this.statConfigStrings = new String[StatHelper.iConfigeratStat.size()];
//		for (int i = 0; i < StatBase.stats.size(); i++)
//		{
//			this.statConfigStrings[i] = ((StatBase) StatBase.stats.get(i)).toConfigurationString();
//		}
        for (int i = 0; i < StatHelper.iConfigeratStat.size(); i++) {
            this.statConfigStrings[i] = StatHelper.iConfigeratStat.get(i).toConfigurationString();
        }
    }

    @Override
    public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
        ByteBufOutputStream bbos = new ByteBufOutputStream(buffer);
        try {
            bbos.writeBoolean(this.deathLoss);
            bbos.writeFloat(this.newBonus);
            bbos.writeFloat(this.newCost);
            bbos.writeFloat(this.newLimit);
            for (String statConfigString : this.statConfigStrings) {
                if (!statConfigString.isEmpty()) {
                    bbos.writeUTF(statConfigString);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
        ByteBufInputStream bbis = new ByteBufInputStream(buffer);
        try {
            this.deathLoss = bbis.readBoolean();
            this.newBonus = bbis.readFloat();
            this.newCost = bbis.readFloat();
            this.newLimit = bbis.readFloat();
            for (int i = 0; i < this.statConfigStrings.length; i++) {
                this.statConfigStrings[i] = bbis.readUTF();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleClientSide(EntityPlayer player) {
        StatBase.loseStatsOnDeath = this.deathLoss;
        StatBase.globalBonusMultiplier = this.newBonus;
        StatBase.globalCostMultiplier = this.newCost;
        StatBase.globalLimitMultiplier = this.newLimit;
//		for (int i = 0; i < this.statConfigStrings.length; i++)
//		{
//			((StatBase) StatBase.stats.get(i)).fromConfigurationString(this.statConfigStrings[i]);
//		}
        for (int i = 0; i < StatHelper.iConfigeratStat.size(); i++) {
            StatHelper.iConfigeratStat.get(i).fromConfigurationString(this.statConfigStrings[i]);
        }
    }

    @Override
    public void handleServerSide(EntityPlayer player) {

    }
}