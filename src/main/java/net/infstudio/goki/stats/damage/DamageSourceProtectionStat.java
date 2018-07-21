package net.infstudio.goki.stats.damage;

import net.infstudio.goki.lib.Reference;
import net.infstudio.goki.stats.IConfigeratable;
import net.infstudio.goki.stats.StatBase;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.config.Configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class DamageSourceProtectionStat extends StatBase implements IConfigeratable {
    public List<String> damageSources = new ArrayList<>();

    public DamageSourceProtectionStat(int id, String key, int limit) {
        super(id, key, limit);
    }

    @Override
    public boolean needAffectedByStat(Object... obj) {
        if (obj != null) {
            if ((obj[0] instanceof DamageSource)) {
                DamageSource source = (DamageSource) obj[0];
                for (int i = 0; i < this.damageSources.size(); i++) {
                    if (source.damageType.equals(this.damageSources.get(i))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void loadFromConfigurationFile(Configuration config) {
        this.damageSources.clear();
        String[] sources = Reference.configuration.get("Support",
                key + " Sources",
                getDefaultDamageSources()).getStringList();
        for (int i = 0; i < sources.length; i++) {
            this.damageSources.add(sources[i]);
        }
    }

    @Override
    public String toConfigurationString() {
        String configString = "";
        for (String s : this.damageSources) {
            configString = configString + "," + s;
        }
        return configString.substring(1);
    }

    @Override
    public void saveToConfigurationFile(Configuration config) {
        String[] sources = new String[this.damageSources.size()];
        for (int i = 0; i < sources.length; i++) {
            sources[i] = this.damageSources.get(i);
        }
        Reference.configuration.get("Support",
                key + " Sources",
                getDefaultDamageSources()).set(sources);
    }

    @Override
    public void fromConfigurationString(String configString) {
        this.damageSources.clear();
        String[] configStringSplit = configString.split(",");
        this.damageSources.addAll(Arrays.asList(configStringSplit));
    }

    public abstract String[] getDefaultDamageSources();
}