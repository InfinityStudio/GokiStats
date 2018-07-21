package net.infstudio.goki.handlers;

import net.infstudio.goki.GokiStats;
import net.infstudio.goki.handlers.packet.PacketStatAlter;
import net.infstudio.goki.handlers.packet.PacketSyncStatConfig;
import net.infstudio.goki.lib.DataHelper;
import net.infstudio.goki.lib.IDMDTuple;
import net.infstudio.goki.stats.IStatSpecial;
import net.infstudio.goki.stats.StatBase;
import net.infstudio.goki.stats.StatMiningMagician;
import net.infstudio.goki.stats.Stats;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.List;
import java.util.Random;

@GameRegistry.ObjectHolder("gokistats")
public class CommonHandler {
    public static final SoundEvent TREASURE = null;
    public static final SoundEvent MAGICIAN = null;
    public static final SoundEvent REAPER = null;

    @SubscribeEvent
    public void registerSounds(RegistryEvent.Register<SoundEvent> event) {
        event.getRegistry().registerAll(
                new SoundEvent(new ResourceLocation("gokistats:treasure")),
                new SoundEvent(new ResourceLocation("gokistats:magician")),
                new SoundEvent(new ResourceLocation("gokistats:reaper"))
        );
    }

    @SubscribeEvent
    public void harvestBlock(BlockEvent.HarvestDropsEvent event) {
        EntityPlayer player = event.getHarvester();
        Block block = event.getState().getBlock();
        if (player != null) {
            if (DataHelper.getPlayerStatLevel(player, Stats.TREASURE_FINDER) > 0) {
                boolean treasureFound = false;
                Random random = player.getRNG();
                List<ItemStack> items = Stats.TREASURE_FINDER.getApplicableItemStackList(block,
                        block.getMetaFromState(event.getState()),
                        DataHelper.getPlayerStatLevel(player,
                                Stats.TREASURE_FINDER));
                List<Integer> chances = Stats.TREASURE_FINDER.getApplicableChanceList(block,
                        block.getMetaFromState(event.getState()),
                        DataHelper.getPlayerStatLevel(player,
                                Stats.TREASURE_FINDER));
                for (int i = 0; i < items.size(); i++) {
                    Integer roll = random.nextInt(10000);
                    if (roll <= chances.get(i)) {
                        if (items.get(i) != null) {
                            event.getDrops().add(items.get(i));
                            treasureFound = true;
                        } else {
                            System.out.println("Tried to add an item from Treasure Finder, but it failed!");
                        }
                    }
                }
                if (treasureFound) {
                    player.world.playSound(player, event.getPos(), TREASURE, SoundCategory.MASTER, 1.0F, 1.0F);
                }
            }

            if (DataHelper.getPlayerStatLevel(player, Stats.MINING_MAGICIAN) > 0) {
                boolean magicHappened = false;
                IDMDTuple mme = new IDMDTuple(block, block.getMetaFromState(event.getState()));
                if (Stats.MINING_MAGICIAN.needAffectedByStat(mme)) {
                    for (int i = 0; i < event.getDrops().size(); i++) {
                        if (player.getRNG().nextDouble() * 100.0D <= Stats.MINING_MAGICIAN.getBonus(player)) {
                            ItemStack item = event.getDrops().get(i);
                            if (((item.getItem() instanceof ItemBlock)) && (ItemBlock.getIdFromItem(item.getItem()) == Block.getIdFromBlock(block))) {
                                if (item.getItemDamage() == block.getMetaFromState(event.getState())) {
                                    int randomEntry = player.getRNG().nextInt(StatMiningMagician.blockEntries.size());
                                    IDMDTuple entry = StatMiningMagician.blockEntries.get(randomEntry);
                                    ItemStack stack = new ItemStack(Item.getItemById(entry.id), 1, entry.md);
                                    stack.setCount(event.getDrops().get(i).getCount());
                                    event.getDrops().set(i, stack);
                                    magicHappened = true;
                                }
                            } else {
                                for (int j = 0; j < StatMiningMagician.itemEntries.size(); j++) {
                                    IDMDTuple entry = StatMiningMagician.itemEntries.get(j);
                                    if ((Item.getIdFromItem(item.getItem()) == entry.id) && (item.getItemDamage() == entry.md)) {
                                        int randomEntry = player.getRNG().nextInt(StatMiningMagician.itemEntries.size());
                                        IDMDTuple chosenEntry = StatMiningMagician.itemEntries.get(randomEntry);
                                        ItemStack stack = new ItemStack(Item.getItemById(chosenEntry.id), 1, chosenEntry.md);
                                        stack.setCount(event.getDrops().get(i).getCount());
                                        event.getDrops().set(i, stack);
                                        magicHappened = true;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    if (magicHappened) {
                        player.world.playSound(player, event.getPos(), MAGICIAN, SoundCategory.MASTER, 0.3f, 1.0f);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void playerJoinWorld(EntityJoinWorldEvent event) {
        if ((event.getEntity() instanceof EntityPlayer)) {
            EntityPlayer player = (EntityPlayer) event.getEntity();
            if (!player.world.isRemote) {
                GokiStats.packetPipeline.sendTo(new PacketSyncStatConfig(StatBase.loseStatsOnDeath, StatBase.globalBonusMultiplier, StatBase.globalCostMultiplier, StatBase.globalLimitMultiplier),
                        (EntityPlayerMP) player);
            } else {
                GokiStats.packetPipeline.sendToServer(new PacketStatAlter(0, 0));
            }
        }
    }

    @SubscribeEvent
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        EntityPlayer player = event.player;
        if (!player.world.isRemote) {
            GokiStats.packetPipeline.sendTo(new PacketSyncStatConfig(StatBase.loseStatsOnDeath, StatBase.globalBonusMultiplier, StatBase.globalCostMultiplier, StatBase.globalLimitMultiplier),
                    (EntityPlayerMP) player);
        } else {
            GokiStats.packetPipeline.sendToServer(new PacketStatAlter(0, 0));
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
    public void playerDead(LivingDeathEvent event) {
        if ((event.getEntityLiving() instanceof EntityPlayer)) {
            EntityPlayer player = (EntityPlayer) event.getEntityLiving();
            if (StatBase.loseStatsOnDeath) {
                for (int stat = 0; stat < StatBase.totalStats; stat++) {
                    DataHelper.setPlayerStatLevel(player,
                            StatBase.stats.get(stat),
                            0);
                }
            }
        }
    }

    @SubscribeEvent
    public void playerBreakSpeed(PlayerEvent.BreakSpeed event) {
        ItemStack heldItem = event.getEntityPlayer().getHeldItemMainhand();
        EntityPlayer player = event.getEntityPlayer();

        float i = 0, j = 0, k = 0, l = 0;

        if (Stats.MINING.needAffectedByStat(heldItem,
                event.getPos(),
                player.world)) {
            i = Stats.MINING.getBonus(player);
        }
        if (Stats.DIGGING.needAffectedByStat(heldItem,
                event.getPos(),
                player.world)) {
            j = Stats.DIGGING.getBonus(player);
        }
        if (Stats.CHOPPING.needAffectedByStat(heldItem,
                event.getPos(),
                player.world)) {
            k = Stats.CHOPPING.getBonus(player);
        }
        if (Stats.TRIMMING.needAffectedByStat(heldItem,
                event.getPos(),
                player.world)) {
            l = Stats.TRIMMING.getBonus(player);
        }

        float multiplier = 1.0F + i + j + k + l;

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
        if (event.getOriginalAttacker().getTags().contains("knockback")) {
            event.getOriginalAttacker().removeTag("knockback");
            event.setStrength(event.getStrength() * 2f);
            event.getOriginalAttacker().sendMessage(new TextComponentTranslation("grpg_Roll.knockback"));
        }
    }

    @SubscribeEvent
    public void entityHurt(LivingHurtEvent event) {
        DamageSource source = event.getSource();

        EntityLivingBase victim = event.getEntityLiving();

        if ((victim instanceof EntityPlayer)) {
            EntityPlayer player = (EntityPlayer) victim;

            if (player.getEntityWorld().rand.nextFloat() <= Stats.ROLL.getBonus(player)) {
                // Avoid damage
                event.setCanceled(true);

                player.addPotionEffect(
                        new PotionEffect(Potion.getPotionFromResourceLocation("minecraft:strength"), 20, 2)
                );

                victim.addTag("knockback");
                player.sendMessage(new TextComponentTranslation("grpg_Roll.message"));

                return;
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

            if (Stats.REAPER.needAffectedByStat((Entity) victim)) {
                float reap = Stats.REAPER.getBonus(player);
                float reapBonus = 0;
                if (Stats.STEALTH.needAffectedByStat(player))
                    reapBonus = reap * ((IStatSpecial) Stats.STEALTH).getSecondaryBonus(player) / 100.0F;
                float reapChance = reap + reapBonus;
                if (player.getRNG().nextFloat() <= reapChance) {
                    player.onEnchantmentCritical(victim);
                    player.world.playSound(player, event.getEntity().getPosition(), REAPER, SoundCategory.MASTER, 1.0f, 1.0f);
                    event.setAmount(100000.0F);
                }
            }
        }
    }
}