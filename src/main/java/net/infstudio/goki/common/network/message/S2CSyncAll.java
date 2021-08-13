package net.infstudio.goki.common.network.message;

import net.infstudio.goki.api.stat.StatBase;
import net.infstudio.goki.common.utils.DataHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;

public class S2CSyncAll implements IMessage {
    public int[] statLevels = new int[0];
    public int[] revertedStatLevels = new int[0];

    public S2CSyncAll() {
    }

    public S2CSyncAll(Player player) {
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
    public void fromBytes(FriendlyByteBuf buf) {
        this.statLevels = new int[buf.readInt()];
        this.revertedStatLevels = new int[buf.readInt()];
        for (int i = 0; i < this.statLevels.length; i++) {
            this.statLevels[i] = buf.readInt();
        }
        for (int i = 0; i < this.revertedStatLevels.length; i++) {
            this.revertedStatLevels[i] = buf.readInt();
        }
    }

    @Override
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(statLevels.length);
        buf.writeInt(revertedStatLevels.length);
        for (int statLevel : this.statLevels) {
            buf.writeInt(statLevel);
        }
        for (int revertedStatLevel: this.revertedStatLevels) {
            buf.writeInt(revertedStatLevel);
        }
    }
}
