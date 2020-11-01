package net.infstudio.goki.common.handlers;

import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.ints.IntList;
import net.infstudio.goki.api.stat.Stats;
import net.infstudio.goki.common.init.GokiSounds;
import net.infstudio.goki.common.stat.tool.StatMiningMagician;
import net.infstudio.goki.common.utils.DataHelper;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootParameters;
import net.minecraft.world.storage.loot.conditions.ILootCondition;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Random;

public class GokiLootModifier extends LootModifier {
    /**
     * Constructs a LootModifier.
     *
     * @param conditionsIn the ILootConditions that need to be matched before the loot is modified.
     */
    protected GokiLootModifier(ILootCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Nonnull
    @Override
    protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
        if (!context.has(LootParameters.BLOCK_STATE) || !context.has(LootParameters.THIS_ENTITY) || !context.has(LootParameters.POSITION)) return generatedLoot;
        Entity entity = context.get(LootParameters.THIS_ENTITY);
        if (!(entity instanceof PlayerEntity)) {
            return generatedLoot;
        }
        Block block = context.get(LootParameters.BLOCK_STATE).getBlock();
        PlayerEntity player = (PlayerEntity) entity;
        if (DataHelper.getPlayerStatLevel(player, Stats.TREASURE_FINDER) > 0) { // Player has treasure finder
            boolean treasureFound = false; // Make a temp variable here to play sound
            Random random = player.getRNG();
            // Note: Items and chances are in pairs
            List<ItemStack> items = Stats.TREASURE_FINDER.getApplicableItemStackList(block,
                    DataHelper.getPlayerStatLevel(player,
                            Stats.TREASURE_FINDER));
            IntList chances = Stats.TREASURE_FINDER.getApplicableChanceList(block,
                    DataHelper.getPlayerStatLevel(player,
                            Stats.TREASURE_FINDER));

            for (int i = 0; i < items.size(); i++) {
                int roll = random.nextInt(10000);
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
                player.world.playSound(player, context.get(LootParameters.POSITION), GokiSounds.TREASURE, SoundCategory.BLOCKS, 1.0F, 1.0F);
            }
        }

        if (DataHelper.getPlayerStatLevel(player, Stats.MINING_MAGICIAN) > 0) { // Player has mining magician
            boolean magicHappened = false;
            // TODO Rewrite to NBT in 1.13
//            LootConfigDeserializer.MINING_MAGICIAN.getLocationForBlock(event.getState()).map(event.getWorld().getLootTableManager()::getLootTableFromLocation).ifPresent(lootTable ->
//                    lootTable.generateLootForPools(event.getWorld().rand, new LootContext(1f, (WorldServer) event.getWorld(), event.getWorld().getLootTableManager(), null, null, null)));

            if (Stats.MINING_MAGICIAN.isEffectiveOn(block)) { // This block can be affected by magic
                for (int i = 0; i < generatedLoot.size(); i++) {
                    if (player.getRNG().nextDouble() * 100.0D <= Stats.MINING_MAGICIAN.getBonus(player)) {
                        ItemStack item = generatedLoot.get(i);
                        // If this block drops itself, give player additional block drops
                        if (item.getItem() instanceof BlockItem && item.getItem().getRegistryName().equals(block.getRegistryName())) {
                            int randomEntry = player.getRNG().nextInt(StatMiningMagician.blockEntries.size());
                            ItemStack stack = new ItemStack(StatMiningMagician.blockEntries.get(randomEntry), 1);
                            stack.setCount(generatedLoot.get(i).getCount());
                            generatedLoot.add(stack);
                            magicHappened = true;
                        } else { // Give additional item drops
                            for (int j = 0; j < StatMiningMagician.itemEntries.size(); j++) {
                                if (item.getItem() == StatMiningMagician.itemEntries.get(j)) {
                                    int randomEntry = player.getRNG().nextInt(StatMiningMagician.itemEntries.size());
                                    ItemStack stack = new ItemStack(StatMiningMagician.itemEntries.get(randomEntry), 1);
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
                    player.world.playSound(player, context.get(LootParameters.POSITION), GokiSounds.MAGICIAN, SoundCategory.BLOCKS, 0.3f, 1.0f);
                }
            }
        }
        return generatedLoot;
    }

    public static class Serializer extends GlobalLootModifierSerializer<GokiLootModifier> {

        @Override
        public GokiLootModifier read(ResourceLocation name, JsonObject object, ILootCondition[] conditionsIn) {
            return new GokiLootModifier(conditionsIn);
        }
    }
}
