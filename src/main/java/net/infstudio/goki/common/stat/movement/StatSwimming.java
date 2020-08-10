package net.infstudio.goki.common.stat.movement;

import net.infstudio.goki.common.config.stats.StatConfig;
import net.infstudio.goki.common.utils.Reference;
import net.infstudio.goki.api.stat.StatBase;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.client.resources.I18n;

public class StatSwimming extends StatBase<StatConfig> {
    public StatSwimming(int id, String key, int limit) {
        super(id, key, limit);
    }

    public float getBonus(int level) {
        return getFinalBonus((float) Math.pow(level, 1.1D) * 0.029F);
    }

    @Override
    public String getLocalizedDescription(PlayerEntity player) {
        if (Reference.isPlayerAPILoaded) {
            return I18n.format("skill.gokistats." + this.key + ".disabled");
        }
        return I18n.format("skill.gokistats." + this.key + ".text",
                this.getDescriptionFormatArguments(player)[0]);
    }
}
