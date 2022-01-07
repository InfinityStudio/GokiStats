package net.infstudio.goki.common.stat.movement;

import net.infstudio.goki.api.stat.StatBase;
import net.infstudio.goki.common.config.stats.StatConfig;
import net.infstudio.goki.common.utils.Reference;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.entity.player.Player;

public class StatClimbing extends StatBase<StatConfig> {
    public StatClimbing(int id, String key, int limit) {
        super(id, key, limit);
    }

    public float getBonus(int level) {
        return getFinalBonus((float) Math.pow(level, 1.1D) * 0.029F);
    }

    @Override
    public String getLocalizedDescription(Player player) {
        if (Reference.isPlayerAPILoaded) {
            return I18n.get("skill.gokistats." + this.key + ".disabled");
        }
        return I18n.get("skill.gokistats." + this.key + ".text",
                this.getDescriptionFormatArguments(player)[0]);
    }
}
