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
import java.util.Random;

public class GokiLootModifier extends LootModifier {
    /**
     * Constructs a LootModifier.
     *
     * @param conditionsIn the ILootConditions that need to be matched before the loot is modified.
     */
    protected GokiLootModifier(LootItemCondition[] conditionsIn) {
        super(conditionsIn);
    }

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
            var treasureFound = false; // Make a temp variable here to play sound
            Random random = player.getRandom();
            // Note: Items and chances are in pairs
            List<ItemStack> items = Stats.TREASURE_FINDER.getApplicableItemStackList(block,
                    DataHelper.getPlayerStatLevel(player,
                            Stats.TREASURE_FINDER));
            var chances = Stats.TREASURE_FINDER.getApplicableChanceList(block,
                    DataHelper.getPlayerStatLevel(player,
                            Stats.TREASURE_FINDER));

            for (var i = 0; i < items.size(); i++) {
                var roll = random.nextInt(10000);
                if (roll <= chances.getInt(i)) {
                    if (items.get(i) != null) {
                        generatedLoot.add(items.get(i)); // Add treasure to player
                        treasureFound = true;
                    } else {
                        System.out.println("Tried to add an item from Treasure Finder, but it failed!");
                    }
                }
            }
            if (treasureFound) {
                player.level.playSound(player, new BlockPos(context.getParamOrNull(LootContextParams.ORIGIN)), GokiSounds.TREASURE, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
        }

        if (DataHelper.getPlayerStatLevel(player, Stats.MINING_MAGICIAN) > 0) { // Player has mining magician
            var magicHappened = false;
            // TODO Rewrite to NBT in 1.13
//            LootConfigDeserializer.MINING_MAGICIAN.getLocationForBlock(event.getState()).map(event.getWorld().getLootTableManager()::getLootTableFromLocation).ifPresent(lootTable ->
//                    lootTable.generateLootForPools(event.getWorld().rand, new LootContext(1f, (WorldServer) event.getWorld(), event.getWorld().getLootTableManager(), null, null, null)));

            if (Stats.MINING_MAGICIAN.isEffectiveOn(block)) { // This block can be affected by magic
                for (var i = 0; i < generatedLoot.size(); i++) {
                    if (player.getRandom().nextDouble() * 100.0D <= Stats.MINING_MAGICIAN.getBonus(player)) {
                        var item = generatedLoot.get(i);
                        // If this block drops itself, give player additional block drops
                        if (item.getItem() instanceof BlockItem && Objects.equals(item.getItem().getRegistryName(), block.getRegistryName())) {
                            int randomEntry = player.getRandom().nextInt(StatMiningMagician.blockEntries.size());
                            var stack = new ItemStack(StatMiningMagician.blockEntries.get(randomEntry), 1);
                            stack.setCount(generatedLoot.get(i).getCount());
                            generatedLoot.add(stack);
                            magicHappened = true;
                        } else { // Give additional item drops
                            for (var j = 0; j < StatMiningMagician.itemEntries.size(); j++) {
                                if (item.getItem() == StatMiningMagician.itemEntries.get(j)) {
                                    int randomEntry = player.getRandom().nextInt(StatMiningMagician.itemEntries.size());
                                    var stack = new ItemStack(StatMiningMagician.itemEntries.get(randomEntry), 1);
                                    stack.setCount(generatedLoot.get(i).getCount());
                                    generatedLoot.add(stack);
                                    magicHappened = true;
                                    break;
                                }
                            }
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
