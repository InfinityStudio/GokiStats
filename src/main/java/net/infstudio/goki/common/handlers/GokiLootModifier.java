package net.infstudio.goki.common.handlers;

import com.google.gson.JsonObject;
import net.infstudio.goki.api.stat.Stats;
import net.infstudio.goki.common.init.GokiSounds;
import net.infstudio.goki.common.stat.tool.StatMiningMagician;
import net.infstudio.goki.common.utils.DataHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;

public class GokiLootModifier extends LootModifier {
    /**
     * Constructs a LootModifier.
     *
     * @param conditionsIn the ILootConditions that need to be matched before the loot is modified.
     */
    protected GokiLootModifier(LootItemCondition[] conditionsIn) {
        super(conditionsIn);
    }

    private static ResourceLocation temp = new ResourceLocation("minecraft:null");

    @Nonnull
    @Override
    protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
        if (!context.hasParam(LootContextParams.BLOCK_STATE) || !context.hasParam(LootContextParams.THIS_ENTITY) || !context.hasParam(LootContextParams.ORIGIN)) return generatedLoot;
        var entity = context.getParamOrNull(LootContextParams.THIS_ENTITY);
        if (!(entity instanceof Player player)) {
            return generatedLoot;
        }
        var block = context.getParamOrNull(LootContextParams.BLOCK_STATE).getBlock();
        if (DataHelper.getPlayerStatLevel(player, Stats.TREASURE_FINDER) > 0) { // Player has treasure finder
            var id = new ResourceLocation(block.getRegistryName().getNamespace(), "treasure_finder/" + block.getRegistryName().getPath());

            // Prevent StackOverflow
            if (context.getQueriedLootTableId().equals(temp)) return generatedLoot;
            else {
                temp = context.getQueriedLootTableId();
            }

            var table = context.getLootTable(id);

            var items = table.getRandomItems(context);
            temp = new ResourceLocation("minecraft:null");
            generatedLoot.addAll(items);
            if (!items.isEmpty()) {
                player.level.playSound(player, new BlockPos(context.getParamOrNull(LootContextParams.ORIGIN)), GokiSounds.TREASURE, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
        }

        if (DataHelper.getPlayerStatLevel(player, Stats.MINING_MAGICIAN) > 0) { // Player has mining magician
            var magicHappened = false;

            if (Stats.MINING_MAGICIAN.isEffectiveOn(block)) { // This block can be affected by magic
                for (var i = 0; i < generatedLoot.size(); i++) {
                    if (player.getRandom().nextDouble() * 100.0D <= Stats.MINING_MAGICIAN.getBonus(player)) {
                        var item = generatedLoot.get(i);
                        // If this block drops itself, give player additional block drops
                        if (item.getItem() instanceof BlockItem && Objects.equals(item.getItem().getRegistryName(), block.getRegistryName())) {
                            var stack = new ItemStack(StatMiningMagician.MAGICIAN_ORE.getRandomElement(context.getRandom()), 1);
                            stack.setCount(generatedLoot.get(i).getCount());
                            generatedLoot.add(stack);
                            magicHappened = true;
                        } else { // Give additional item drops
                            generatedLoot.add(new ItemStack(StatMiningMagician.MAGICIAN_ITEM.getRandomElement(context.getRandom()), generatedLoot.get(i).getCount()));

                            magicHappened = true;
                            break;
                        }
                    }
                }
                if (magicHappened) {
                    player.level.playSound(player, new BlockPos(context.getParamOrNull(LootContextParams.ORIGIN)), GokiSounds.MAGICIAN, SoundSource.BLOCKS, 0.3f, 1.0f);
                }
            }
        }
        return generatedLoot;
    }

    public static class Serializer extends GlobalLootModifierSerializer<GokiLootModifier> {

        @Override
        public GokiLootModifier read(ResourceLocation name, JsonObject object, LootItemCondition[] conditionsIn) {
            return new GokiLootModifier(conditionsIn);
        }

        @Override
        public JsonObject write(GokiLootModifier instance) {
            return new JsonObject();
        }
    }
}
