package net.infstudio.goki.common.loot.conditions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.infstudio.goki.api.stat.Stat;
import net.infstudio.goki.api.stat.StatBase;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.LootCondition;

import javax.annotation.Nonnull;
import java.util.Random;

public class LevelCondition implements LootCondition {
    public int level;
    public Stat stat;

    public LevelCondition(int level, String stat) {
        this.level = level;
        this.stat = StatBase.statKeyMap.get(stat);
    }

    @Override
    public boolean testCondition(@Nonnull Random rand, @Nonnull LootContext context) {
        return false;
    }

    public static class Serializer extends LootCondition.Serializer<LevelCondition> {
        public Serializer(ResourceLocation location, Class<LevelCondition> clazz) {
            super(location, clazz);
        }

        @Override
        public void serialize(JsonObject json, LevelCondition value, JsonSerializationContext context) {
            json.addProperty("minLevel", value.level);
            json.addProperty("stat", value.stat.getKey());
        }

        @Nonnull
        @Override
        public LevelCondition deserialize(JsonObject json, JsonDeserializationContext context) {
            return new LevelCondition(JsonUtils.getInt(json, "minLevel"), JsonUtils.getString(json, "stat"));
        }
    }
}
