package net.infstudio.goki.common.network.packet;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.infstudio.goki.GokiStats;
import net.infstudio.goki.common.config.GokiConfig;
import net.infstudio.goki.common.stats.StatBase;
import net.infstudio.goki.common.stats.StatMaxHealth;
import net.infstudio.goki.common.utils.DataHelper;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

import javax.annotation.Nonnegative;

public class PacketStatAlter implements GokiPacket {
    protected int stat;
    protected int amount;

    public PacketStatAlter() {
    }

    public PacketStatAlter(int stat, int amount) {
        this.stat = stat;
        this.amount = amount;
    }

    @Override
    public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
        buffer.writeInt(this.stat);
        buffer.writeInt(this.amount);
    }

    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
        this.stat = buffer.readInt();
        this.amount = buffer.readInt();
    }

    @Override
    public void handleClientSide(EntityPlayer player) {
    }

    @Override
    public void handleServerSide(EntityPlayer player) {
        if (player == null)
            return;
    }

    public static class Up extends PacketStatAlter {
        public Up() {
        }

        public Up(int stat, @Nonnegative int amount) {
            super(stat, amount);
        }

        @Override
        public void handleServerSide(EntityPlayer player) {
            super.handleServerSide(player);
            StatBase stat = StatBase.stats.get(this.stat);
            if (!stat.enabled)
                return;
            int level = DataHelper.getPlayerStatLevel(player, stat);

            if (level + this.amount > stat.getLimit()) {
                this.amount = stat.getLimit() - level;
            }

            int cost = stat.getCost(level + this.amount - 1);
            int currentXP = DataHelper.getXPTotal(player.experienceLevel, player.experience);

            if (currentXP >= cost) {
                int reverted = DataHelper.getPlayerRevertStatLevel(player, stat);
                reverted = Math.max(reverted - this.amount, 0);
                DataHelper.setPlayerRevertStatLevel(player, stat, reverted);

                DataHelper.setPlayerStatLevel(player, stat, level + this.amount);
                if (stat instanceof StatMaxHealth) {
                    player.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH)
                            .setBaseValue(20 + level + this.amount);
                }

                DataHelper.setPlayersExpTo(player, currentXP - cost);
            }

            GokiStats.packetPipeline.sendTo(new PacketStatSync(player), (EntityPlayerMP) player);
            GokiStats.packetPipeline.sendTo(new PacketSyncXP(player), (EntityPlayerMP) player);
        }
    }

    public static class Down extends PacketStatAlter {
        public Down() {
        }

        public Down(int stat, @Nonnegative int amount) {
            super(stat, amount);
        }

        @Override
        public void handleServerSide(EntityPlayer player) {
            super.handleServerSide(player);
            StatBase stat = StatBase.stats.get(this.stat);
            if (!stat.enabled)
                return;
            int level = DataHelper.getPlayerStatLevel(player, stat);
            if (DataHelper.getPlayerStatLevel(player, stat) > 0) {
                int reverted = DataHelper.getPlayerRevertStatLevel(player, stat);
                int maxRevert = GokiConfig.globalModifiers.globalMaxRevertLevel;

                if (maxRevert >= 0 && reverted + this.amount > maxRevert) { // check if we limit the max revert, and if the revert overflow the value
                    this.amount += maxRevert - reverted; // ceil the value for max revert
                }

                reverted += this.amount;
                DataHelper.setPlayerRevertStatLevel(player, stat, reverted);

                player.addExperience((int) (stat.getCost(level - this.amount - 2) * GokiConfig.globalModifiers.globalRevertFactor));
                DataHelper.setPlayerStatLevel(player, stat, level - this.amount);
                if (stat instanceof StatMaxHealth) {
                    player.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20 + level - this.amount);
                }

                GokiStats.packetPipeline.sendTo(new PacketStatSync(player), (EntityPlayerMP) player);
                GokiStats.packetPipeline.sendTo(new PacketSyncXP(player), (EntityPlayerMP) player);
            }
        }
    }
}