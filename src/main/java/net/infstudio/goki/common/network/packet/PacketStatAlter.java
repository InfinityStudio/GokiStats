package net.infstudio.goki.common.network.packet;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.infstudio.goki.GokiStats;
import net.infstudio.goki.common.utils.DataHelper;
import net.infstudio.goki.common.stats.StatBase;
import net.infstudio.goki.common.stats.StatMaxHealth;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public class PacketStatAlter implements GokiPacket {
    private int stat;
    private int amount;

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
        if (player != null) {
            StatBase stat = StatBase.stats.get(this.stat);
            if (stat.enabled) {
                int level = DataHelper.getPlayerStatLevel(player, stat);
                if (this.amount > 0) {
                    int cost = stat.getCost(level + this.amount - 1);
                    int currentXP = DataHelper.getXPTotal(player.experienceLevel,
                            player.experience);

                    if (stat.enabled) {
                        if (level + this.amount > stat.getLimit()) {
                            this.amount = 0;
                        }

                        if ((currentXP >= cost) && (this.amount != 0)) {
                            DataHelper.setPlayerStatLevel(player,
                                    stat,
                                    level + this.amount);
                            if (stat instanceof StatMaxHealth) {
                                player.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20 + level + this.amount);
                            }

                            if (this.amount > 0) {
                                DataHelper.setPlayersExpTo(player, currentXP - cost);
                            }
                        }
                    }
                } else if (DataHelper.getPlayerStatLevel(player, stat) > 0) {
                    player.addExperience((int) (stat.getCost(level + this.amount - 2) * 0.8)); // TODO Configure 0.8
                    DataHelper.setPlayerStatLevel(player,
                            stat,
                            level + this.amount);
                    if (stat instanceof StatMaxHealth) {
                        player.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20 + level + this.amount);
                    }
                }
            }
//			else if (this.amount >= 0)
//				;

            GokiStats.packetPipeline.sendTo(new PacketStatSync(player),
                    (EntityPlayerMP) player);
            GokiStats.packetPipeline.sendTo(new PacketSyncXP(player),
                    (EntityPlayerMP) player);
        }
    }
}