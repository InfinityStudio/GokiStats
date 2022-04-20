package net.infstudio.goki.common.handlers;

import net.infstudio.goki.api.capability.CapabilityStat;
import net.infstudio.goki.api.stat.StatBase;
import net.infstudio.goki.api.stat.StatSpecial;
import net.infstudio.goki.api.stat.StatStorage;
import net.infstudio.goki.api.stat.Stats;
import net.infstudio.goki.common.config.GokiConfig;
import net.infstudio.goki.common.init.GokiSounds;
import net.infstudio.goki.common.network.GokiPacketHandler;
import net.infstudio.goki.common.network.message.S2CSyncAll;
import net.infstudio.goki.common.utils.DataHelper;
import net.infstudio.goki.common.utils.Reference;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(Reference.MODID)
@Mod.EventBusSubscriber(modid = Reference.MODID)
public class CommonHandler {
    @SubscribeEvent
    public static void injectCapability(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player)
            event.addCapability(new ResourceLocation(Reference.MODID, "stat_storage"), new CapabilityStat.Provider());
    }

    @SubscribeEvent
    public static void playerFall(LivingFallEvent event) {
        if (event.getEntity() instanceof Player) {
            var player = (Player) event.getEntity();
            var featherFallLevel = DataHelper.getPlayerStatLevel(player,
                    Stats.STAT_FEATHER_FALL);
            if (event.getDistance() < 3.0D + featherFallLevel * 0.1D) {
                event.setDistance(0.0F);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        if (event.getOriginal().getCapability(CapabilityStat.STAT).isPresent() && event.getEntity().getCapability(CapabilityStat.STAT).isPresent()) {
            event.getEntity().getCapability(CapabilityStat.STAT).orElse(new StatStorage()).stateMap = event.getOriginal().getCapability(CapabilityStat.STAT).orElse(new StatStorage()).stateMap;
        }
    }

    @SubscribeEvent
    public static void playerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        if (!player.level.isClientSide()) {
            if (GokiConfig.SERVER.loseStatsOnDeath.get()) {
                for (var stat = 0; stat < StatBase.totalStats.orElse(0); stat++) {
                    DataHelper.multiplyPlayerStatLevel(player,
                            StatBase.stats.get(stat),
                            level -> level - (int) (GokiConfig.SERVER.loseStatsMultiplier.get() * level));
                }
            }
            GokiPacketHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player), new S2CSyncAll(player));
        }
    }

    @SubscribeEvent
    public static void playerBreakSpeed(PlayerEvent.BreakSpeed event) {
        var heldItem = event.getPlayer().getMainHandItem();
        var player = event.getPlayer();

        var multiplier = 1.0F;

        if (Stats.MINING.isEffectiveOn(heldItem,
                event.getPos(),
                player.level)) {
            multiplier += Stats.MINING.getBonus(player);
        }
        if (Stats.DIGGING.isEffectiveOn(heldItem,
                event.getPos(),
                player.level)) {
            multiplier += Stats.DIGGING.getBonus(player);
        }
        if (Stats.CHOPPING.isEffectiveOn(heldItem,
                event.getPos(),
                player.level)) {
            multiplier += Stats.CHOPPING.getBonus(player);
        }
        if (Stats.TRIMMING.isEffectiveOn(heldItem,
                event.getPos(),
                player.level)) {
            multiplier += Stats.TRIMMING.getBonus(player);
        }

        event.setNewSpeed(event.getOriginalSpeed() * multiplier);
    }

    @SubscribeEvent
    public static void playerJump(LivingEvent.LivingJumpEvent event) {
        if (event.getEntityLiving() instanceof Player player) {
            if (player.isSprinting()) {
                player.setDeltaMovement(player.getDeltaMovement().multiply(1.0 + Stats.LEAPER_V.getBonus(player), 1.0 + Stats.LEAPER_H.getBonus(player), 1.0 + Stats.LEAPER_H.getBonus(player)));
            }
        }
    }

    @SubscribeEvent
    public static void entityHurt(LivingHurtEvent event) {
        var source = event.getSource();

        var victim = event.getEntityLiving();

        if (victim instanceof Player player) {
            if (!source.isFire() && !source.isBypassMagic()) {
                if (player.level.random.nextFloat() >= 1.0f - Stats.ROLL.getBonus(player)) {
                    // Avoid damage
                    event.setCanceled(true);

                    player.addEffect(
                            new MobEffectInstance(MobEffects.DAMAGE_BOOST, 20, 2)
                    );

                    victim.addTag("knockback");
//                    player.sendMessage(new TranslationTextComponent("skill.gokistats.roll.message"));

                    return;
                }
            }

            var damageMultiplier = 1.0F - (Stats.PROTECTION.getAppliedBonus(player,
                    source) + Stats.TOUGH_SKIN.getAppliedBonus(player,
                    source) + Stats.STAT_FEATHER_FALL.getAppliedBonus(player,
                    source) + Stats.TEMPERING.getAppliedBonus(player,
                    source));

            event.setAmount(event.getAmount() * damageMultiplier);
        }

        Entity src = source.getEntity();

        if (src instanceof Player player) {
            var heldItem = player.getMainHandItem();
            var damage = event.getAmount();
            var bonus = 0f;

            if (!heldItem.isEmpty()) {
                if (Stats.SWORDSMANSHIP.isItemSupported(heldItem)) {
                    bonus = Math.round(damage * Stats.SWORDSMANSHIP.getAppliedBonus(player, heldItem));
                } else if (Stats.BOWMANSHIP.isItemSupported(heldItem)) {
                    bonus = Math.round(damage * Stats.BOWMANSHIP.getAppliedBonus(player, heldItem));
                } else {
                    if (!DataHelper.hasDamageModifier(heldItem))
                        // This is not a item providing damage, apply pugilism
                        bonus = Math.round(damage + Stats.PUGILISM.getBonus(player));
                }
            } else {
//				bonus = Math.round(damage + (StatBase.PUGILISM.getBonus(DataHelper.getPlayerStatLevel(	player, StatBase.PUGILISM))));
                bonus = Math.round(damage + Stats.PUGILISM.getBonus(player));
            }
            event.setAmount(bonus + damage);

            if (Stats.REAPER.isEffectiveOn(victim)) {
                var reap = Stats.REAPER.getBonus(player);
                float reapBonus = 0;
                if (Stats.STEALTH.isEffectiveOn(player))
                    reapBonus = reap * ((StatSpecial) Stats.STEALTH).getSecondaryBonus(player) / 100.0F;
                var reapChance = reap + reapBonus;
                if (player.getRandom().nextFloat() <= reapChance) {
                    player.crit(victim);
                    player.level.playSound(player, event.getEntity().blockPosition(), GokiSounds.REAPER, SoundSource.MASTER, 1.0f, 1.0f);
                    event.setAmount(100000.0F);
                }
            }
        }
    }

    /*
    @SubscribeEvent
    public void configChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(Reference.MODID)) {
            ConfigManager.sync(Reference.MODID, Config.Type.INSTANCE);
            LootConfigDeserializer.reloadAll();
        }
    }*/
}
