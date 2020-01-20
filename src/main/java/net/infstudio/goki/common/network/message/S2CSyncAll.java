package net.infstudio.goki.common.network.message;

import io.netty.buffer.ByteBuf;
import net.infstudio.goki.api.stat.StatBase;
import net.infstudio.goki.common.utils.DataHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class S2CSyncAll implements IMessage {
    public int[] statLevels;
    public int[] revertedStatLevels;

    public S2CSyncAll() {
    }

    public S2CSyncAll(EntityPlayer player) {
        this.statLevels = new int[StatBase.stats.size()];
        this.revertedStatLevels = new int[StatBase.stats.size()];
        for (int i = 0; i < this.statLevels.length; i++) {
            if (StatBase.stats.get(i) != null) {
                this.statLevels[i] = DataHelper.getPlayerStatLevel(player,
                        StatBase.stats.get(i));
                this.revertedStatLevels[i] = DataHelper.getPlayerRevertStatLevel(player, StatBase.stats.get(i));
            }
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.statLevels = new int[StatBase.stats.size()];
        this.revertedStatLevels = new int[StatBase.stats.size()];
        for (int i = 0; i < this.statLevels.length; i++) {
            this.statLevels[i] = buf.readInt();
        }
        for (int i = 0; i < this.revertedStatLevels.length; i++) {
            this.revertedStatLevels[i] = buf.readInt();
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        for (int statLevel : this.statLevels) {
            buf.writeInt(statLevel);
        }
        for (int revertedStatLevel: this.revertedStatLevels) {
            buf.writeInt(revertedStatLevel);
        }
    }
}
