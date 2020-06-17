package net.infstudio.goki.common.handlers;

import it.unimi.dsi.fastutil.ints.IntList;
import net.infstudio.goki.api.stat.StatBase;
import net.infstudio.goki.api.stat.StatSpecial;
import net.infstudio.goki.api.stat.Stats;
import net.infstudio.goki.common.config.GokiConfig;
import net.infstudio.goki.common.init.GokiSounds;
import net.infstudio.goki.common.init.MinecraftEffects;
import net.infstudio.goki.common.loot.LootConfigDeserializer;
import net.infstudio.goki.common.network.GokiPacketHandler;
import net.infstudio.goki.common.network.message.S2CSyncAll;
import net.infstudio.goki.common.stat.tool.IDMDTuple;
import net.infstudio.goki.common.stat.tool.StatMiningMagician;
import net.infstudio.goki.common.utils.DataHelper;
import net.infstudio.goki.common.utils.Reference;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Random;

@GameRegistry.ObjectHolder("gokistats")
public class CommonHandler {
    @SubscribeEvent
    @SideOnly(Side.SERVER)
    public void harvestBlock(BlockEvent.HarvestDropsEvent event) {
        EntityPlayer player = event.getHarvester();
        Block block = event.getState().getBlock();
        if (player == null) {
            return;
        }
        if (DataHelper.getPlayerStatLevel(player, Stats.TREASURE_FINDER) > 0) { // Player has treasure finder
            LootConfigDeserializer.TREASURE_FINDER.getLocationForBlock(event.getState()).map(event.getWorld().getLootTableManager()::getLootTableFromLocation).ifPresent(lootTable -> lootTable.generateLootForPools(event.getWorld().rand, new LootContext(1f, (WorldServer) event.getWorld(), event.getWorld().getLootTableManager(), null, null, null)));

            boolean treasureFound = false; // Make a temp variable here to play sound
            Random random = player.getRNG();
            // Note: Items and chances are in pairs
            List<ItemStack> items = Stats.TREASURE_FINDER.getApplicableItemStackList(block,
                    block.getMetaFromState(event.getState()),
                    DataHelper.getPlayerStatLevel(player,
                            Stats.TREASURE_FINDER));
            IntList chances = Stats.TREASURE_FINDER.getApplicableChanceList(block,
                    block.getMetaFromState(event.getState()),
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
            LootConfigDeserializer.MINING_MAGICIAN.getLocationForBlock(event.getState()).map(event.getWorld().getLootTableManager()::getLootTableFromLocation).ifPresent(lootTable ->
                    lootTable.generateLootForPools(event.getWorld().rand, new LootContext(1f, (WorldServer) event.getWorld(), event.getWorld().getLootTableManager(), null, null, null)));

            IDMDTuple mme = new IDMDTuple(block, block.getMetaFromState(event.getState()));
            if (Stats.MINING_MAGICIAN.isEffectiveOn(mme)) { // This block can be affected by magic
                for (int i = 0; i < event.getDrops().size(); i++) {
                    if (player.getRNG().nextDouble() * 100.0D <= Stats.MINING_MAGICIAN.getBonus(player)) {
                        ItemStack item = event.getDrops().get(i);
                        if (((item.getItem() instanceof ItemBlock)) && (ItemBlock.getIdFromItem(item.getItem()) == Block.getIdFromBlock(block))) {
                            if (item.getItemDamage() == block.getMetaFromState(event.getState())) {
                                int randomEntry = player.getRNG().nextInt(StatMiningMagician.blockEntries.size());
                                IDMDTuple entry = StatMiningMagician.blockEntries.get(randomEntry);
                                ItemStack stack = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(entry.id)), 1, entry.metadata);
                                stack.setCount(event.getDrops().get(i).getCount());
                                event.getDrops().add(stack);
                                magicHappened = true;
                            }
                        } else {
                            for (int j = 0; j < StatMiningMagician.itemEntries.size(); j++) {
                                IDMDTuple entry = StatMiningMagician.itemEntries.get(j);
                                if ((item.getItem().getRegistryName().toString().equals(entry.id)) && (item.getItemDamage() == entry.metadata)) {
                                    int randomEntry = player.getRNG().nextInt(StatMiningMagician.itemEntries.size());
                                    IDMDTuple chosenEntry = StatMiningMagician.itemEntries.get(randomEntry);
                                    ItemStack stack = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(chosenEntry.id)), 1, chosenEntry.metadata);
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

    @SubscribeEvent
    public void playerFall(LivingFallEvent event) {
        if ((event.getEntity() instanceof EntityPlayer)) {
            EntityPlayer player = (EntityPlayer) event.getEntity();
            int featherFallLevel = DataHelper.getPlayerStatLevel(player,
                    Stats.STAT_FEATHER_FALL);
            if (event.getDistance() < 3.0D + featherFallLevel * 0.1D) {
                event.setDistance(0.0F);
            }
        }
    }

    @SubscribeEvent
    public void playerRespawn(PlayerRespawnEvent event) {
        EntityPlayer player = event.player;
        if (!player.world.isRemote) {
            if (GokiConfig.globalModifiers.loseStatsOnDeath) {
                for (int stat = 0; stat < StatBase.totalStats; stat++) {
                    DataHelper.multiplyPlayerStatLevel(player,
                            StatBase.stats.get(stat),
                            level -> level - (int) (GokiConfig.globalModifiers.loseStatsMultiplier * level));
                }
            }
            GokiPacketHandler.CHANNEL.sendTo(new S2CSyncAll(player), (EntityPlayerMP) player);
        }
    }

    @SubscribeEvent
    public void playerBreakSpeed(PlayerEvent.BreakSpeed event) {
        ItemStack heldItem = event.getEntityPlayer().getHeldItemMainhand();
        EntityPlayer player = event.getEntityPlayer();

        float multiplier = 1.0F;

        if (Stats.MINING.isEffectiveOn(heldItem,
                event.getPos(),
                player.world)) {
            multiplier += Stats.MINING.getBonus(player);
        }
        if (Stats.DIGGING.isEffectiveOn(heldItem,
                event.getPos(),
                player.world)) {
            multiplier += Stats.DIGGING.getBonus(player);
        }
        if (Stats.CHOPPING.isEffectiveOn(heldItem,
                event.getPos(),
                player.world)) {
            multiplier += Stats.CHOPPING.getBonus(player);
        }
        if (Stats.TRIMMING.isEffectiveOn(heldItem,
                event.getPos(),
                player.world)) {
            multiplier += Stats.TRIMMING.getBonus(player);
        }

        event.setNewSpeed(event.getOriginalSpeed() * multiplier);
    }

    @SubscribeEvent
    public void playerJump(LivingEvent.LivingJumpEvent event) {
        if ((event.getEntityLiving() instanceof EntityPlayer)) {
            EntityPlayer player = (EntityPlayer) event.getEntityLiving();
            if (player.isSprinting()) {
                player.motionY *= 1.0F + Stats.LEAPER_V.getBonus(player);
                player.motionX *= 1.0F + Stats.LEAPER_H.getBonus(player);
                player.motionZ *= 1.0F + Stats.LEAPER_H.getBonus(player);
            }
        }
    }

    @SubscribeEvent
    public void entityKnockback(LivingKnockBackEvent event) {
        if (event.getOriginalAttacker() == null && event.getAttacker() == null) return;
        Entity attacker = event.getOriginalAttacker() != null ? event.getOriginalAttacker() : event.getAttacker();
        if (attacker.getTags().contains("knockback")) {
            attacker.removeTag("knockback");
            event.setStrength(event.getStrength() * 2f);
            attacker.sendMessage(new TextComponentTranslation("grpg_Roll.knockback"));
        }
    }

    @SubscribeEvent
    public void entityHurt(LivingHurtEvent event) {
        DamageSource source = event.getSource();

        EntityLivingBase victim = event.getEntityLiving();

        if (victim instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) victim;

            if (!source.isFireDamage() && !source.isDamageAbsolute()) {
                if (player.getEntityWorld().rand.nextFloat() >= 1.0f - Stats.ROLL.getBonus(player)) {
                    // Avoid damage
                    event.setCanceled(true);

                    player.addPotionEffect(
                            new PotionEffect(MinecraftEffects.STRENGTH, 20, 2)
                    );

                    victim.addTag("knockback");
                    player.sendMessage(new TextComponentTranslation("grpg_Roll.message"));

                    return;
                }
            }

            float damageMultiplier = 1.0F - (Stats.PROTECTION.getAppliedBonus(player,
                    source) + Stats.TOUGH_SKIN.getAppliedBonus(player,
                    source) + Stats.STAT_FEATHER_FALL.getAppliedBonus(player,
                    source) + Stats.TEMPERING.getAppliedBonus(player,
                    source));

            event.setAmount(event.getAmount() * damageMultiplier);
        }

        Entity src = source.getTrueSource();

        if (src instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) src;
            ItemStack heldItem = player.getHeldItemMainhand();
            float damage = event.getAmount();
            float bonus = 0f;

            if (!heldItem.isEmpty()) {
                if (Stats.SWORDSMANSHIP.isItemSupported(heldItem)) {
                    bonus = Math.round(damage * Stats.SWORDSMANSHIP.getAppliedBonus(player, heldItem));
                } else if (Stats.BOWMANSHIP.isItemSupported(heldItem)) {
                    bonus = Math.round(damage * Stats.BOWMANSHIP.getAppliedBonus(player, heldItem));
                }
            } else {
//				bonus = Math.round(damage + (StatBase.PUGILISM.getBonus(DataHelper.getPlayerStatLevel(	player, StatBase.PUGILISM))));
                bonus = Math.round(damage + Stats.PUGILISM.getBonus(player));
            }
            event.setAmount(bonus + damage);

            if (Stats.REAPER.isEffectiveOn(victim)) {
                float reap = Stats.REAPER.getBonus(player);
                float reapBonus = 0;
                if (Stats.STEALTH.isEffectiveOn(player))
                    reapBonus = reap * ((StatSpecial) Stats.STEALTH).getSecondaryBonus(player) / 100.0F;
                float reapChance = reap + reapBonus;
                if (player.getRNG().nextFloat() <= reapChance) {
                    player.onEnchantmentCritical(victim);
                    player.world.playSound(player, event.getEntity().getPosition(), GokiSounds.REAPER, SoundCategory.MASTER, 1.0f, 1.0f);
                    event.setAmount(100000.0F);
                }
            }
        }
    }

    @SubscribeEvent
    public void configChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(Reference.MODID)) {
            ConfigManager.sync(Reference.MODID, Config.Type.INSTANCE);
            LootConfigDeserializer.reloadAll();
        }
    }
}
