package net.infstudio.goki.common.handlers;

import com.tfar.additionalevents.event.DropLootEvent;
import it.unimi.dsi.fastutil.ints.IntList;
import net.infstudio.goki.api.stat.Stats;
import net.infstudio.goki.common.init.GokiSounds;
import net.infstudio.goki.common.stat.tool.StatMiningMagician;
import net.infstudio.goki.common.utils.DataHelper;
import net.infstudio.goki.common.utils.Reference;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

import java.util.List;
import java.util.Random;

@ObjectHolder(Reference.MODID)
@Mod.EventBusSubscriber(modid = Reference.MODID)
public class AdditionalEventHandler {
    @SubscribeEvent
    public static void harvestBlock(DropLootEvent event) {
        PlayerEntity player = event.getPlayer();
        Block block = event.getState().getBlock();
        if (player == null) {
            return;
        }
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
                        event.getDrops().add(items.get(i)); // Add treasure to player
                        treasureFound = true;
                    } else {
                        System.out.println("Tried to add an item from Treasure Finder, but it failed!");
                    }
                }
            }
            if (treasureFound) {
                player.world.playSound(player, event.getPos(), GokiSounds.TREASURE, SoundCategory.BLOCKS, 1.0F, 1.0F);
            }
        }

        if (DataHelper.getPlayerStatLevel(player, Stats.MINING_MAGICIAN) > 0) { // Player has mining magician
            boolean magicHappened = false;
            // TODO Rewrite to NBT in 1.13
//            LootConfigDeserializer.MINING_MAGICIAN.getLocationForBlock(event.getState()).map(event.getWorld().getLootTableManager()::getLootTableFromLocation).ifPresent(lootTable ->
//                    lootTable.generateLootForPools(event.getWorld().rand, new LootContext(1f, (WorldServer) event.getWorld(), event.getWorld().getLootTableManager(), null, null, null)));

            if (Stats.MINING_MAGICIAN.isEffectiveOn(block)) { // This block can be affected by magic
                for (int i = 0; i < event.getDrops().size(); i++) {
                    if (player.getRNG().nextDouble() * 100.0D <= Stats.MINING_MAGICIAN.getBonus(player)) {
                        ItemStack item = event.getDrops().get(i);
                        // If this block drops itself, give player additional block drops
                        if (item.getItem() instanceof BlockItem && item.getItem().getRegistryName().equals(block.getRegistryName())) {
                            int randomEntry = player.getRNG().nextInt(StatMiningMagician.blockEntries.size());
                            ItemStack stack = new ItemStack(StatMiningMagician.blockEntries.get(randomEntry), 1);
                            stack.setCount(event.getDrops().get(i).getCount());
                            event.getDrops().add(stack);
                            magicHappened = true;
                        } else { // Give additional item drops
                            for (int j = 0; j < StatMiningMagician.itemEntries.size(); j++) {
                                if (item.getItem() == StatMiningMagician.itemEntries.get(j)) {
                                    int randomEntry = player.getRNG().nextInt(StatMiningMagician.itemEntries.size());
                                    ItemStack stack = new ItemStack(StatMiningMagician.itemEntries.get(randomEntry), 1);
                                    stack.setCount(event.getDrops().get(i).getCount());
                                    event.getDrops().add(stack);
                                    magicHappened = true;
                                    break;
                                }
                            }
                        }
                    }
                }
                if (magicHappened) {
                    player.world.playSound(player, event.getPos(), GokiSounds.MAGICIAN, SoundCategory.BLOCKS, 0.3f, 1.0f);
                }
            }
        }
    }
}
