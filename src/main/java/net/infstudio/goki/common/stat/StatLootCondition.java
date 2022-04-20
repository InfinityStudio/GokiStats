package net.infstudio.goki.common.stat;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.infstudio.goki.api.stat.Stat;
import net.infstudio.goki.api.stat.StatBase;
import net.infstudio.goki.common.utils.DataHelper;
import net.infstudio.goki.common.utils.Reference;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

import javax.annotation.Nonnull;
import java.util.Objects;

public record StatLootCondition(Stat stat, int minLevel) implements LootItemCondition {
    @Nonnull
    public static final LootItemConditionType STAT_LOOT_CONDITION = Registry.register(Registry.LOOT_CONDITION_TYPE, new ResourceLocation(Reference.MODID, "stat_level"), new LootItemConditionType(new StatLootCondition.Serializer()));

    @Nonnull
    @Override
    public LootItemConditionType getType() {
        return STAT_LOOT_CONDITION;
    }

    @Override
    public boolean test(LootContext lootContext) {
        if (lootContext.getParamOrNull(LootContextParams.THIS_ENTITY) instanceof Player player) {
            return DataHelper.getPlayerStatLevel(player, stat) >= minLevel;
        }

        return false;
    }

    public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<StatLootCondition> {
        public void serialize(@Nonnull JsonObject object, @Nonnull StatLootCondition condition, @Nonnull JsonSerializationContext context) {
            object.addProperty("stat", Objects.requireNonNull(condition.stat().getRegistryName()).toString());
            object.addProperty("minLevel", condition.minLevel());
        }

        @Nonnull
        public StatLootCondition deserialize(@Nonnull JsonObject object, @Nonnull JsonDeserializationContext context) {
            var stat = StatBase.REGISTRY.getValue(new ResourceLocation(object.get("stat").getAsString()));
            var minLevel = object.get("minLevel").getAsInt();
            return new StatLootCondition(stat, minLevel);
        }
    }
}
