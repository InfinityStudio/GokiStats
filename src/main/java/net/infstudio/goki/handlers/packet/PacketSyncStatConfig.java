package net.infstudio.goki.handlers.packet;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.channel.ChannelHandlerContext;
import net.infstudio.goki.config.GokiConfig;
import net.infstudio.goki.lib.StatHelper;
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
        this.statConfigStrings = new String[StatHelper.configurables.size()];
    }

    public PacketSyncStatConfig(boolean deathLoss, float newBonus, float newCost, float newLimit) {
        this.deathLoss = deathLoss;
        this.newBonus = newBonus;
        this.newCost = newCost;
        this.newLimit = newLimit;
        this.statConfigStrings = new String[StatHelper.configurables.size()];
//		for (int i = 0; i < StatBase.stats.size(); i++)
//		{
//			this.statConfigStrings[i] = ((StatBase) StatBase.stats.get(i)).toConfigurationString();
//		}
        for (int i = 0; i < StatHelper.configurables.size(); i++) {
            this.statConfigStrings[i] = StatHelper.configurables.get(i).toConfigurationString();
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
        GokiConfig.globalModifiers.loseStatsOnDeath = this.deathLoss;
        GokiConfig.globalModifiers.globalBonusMultiplier = this.newBonus;
        GokiConfig.globalModifiers.globalCostMultiplier = this.newCost;
        GokiConfig.globalModifiers.globalLimitMultiplier = this.newLimit;
//		for (int i = 0; i < this.statConfigStrings.length; i++)
//		{
//			((StatBase) StatBase.stats.get(i)).fromConfigurationString(this.statConfigStrings[i]);
//		}
        for (int i = 0; i < StatHelper.configurables.size(); i++) {
            StatHelper.configurables.get(i).fromConfigurationString(this.statConfigStrings[i]);
        }
    }

    @Override
    public void handleServerSide(EntityPlayer player) {

    }
}