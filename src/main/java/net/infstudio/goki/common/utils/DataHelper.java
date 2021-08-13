package net.infstudio.goki.common.utils;

import net.infstudio.goki.api.capability.CapabilityStat;
import net.infstudio.goki.api.stat.StatBase;
import net.infstudio.goki.api.stat.StatStorage;
import net.infstudio.goki.common.config.GokiConfig;
import net.minecraft.client.renderer.EffectInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

import java.util.Collection;
import java.util.function.IntFunction;

public class DataHelper {
    public static boolean canPlayerRevertStat(Player player, StatBase stat) {
        return GokiConfig.SERVER.globalMaxRevertLevel.get() == -1 ||
                GokiConfig.SERVER.globalMaxRevertLevel.get() >= 0
                        && getPlayerRevertStatLevel(player, stat) < GokiConfig.SERVER.globalMaxRevertLevel.get()
                        && getPlayerStatLevel(player, stat) > 0;
    }

    public static int getPlayerRevertStatLevel(Player player, StatBase stat) {
        if (player.getCapability(CapabilityStat.STAT).isPresent()) {
            return player.getCapability(CapabilityStat.STAT).orElse(new StatStorage()).stateMap.get(stat).revertedLevel;
        } else {
            throw new RuntimeException(new IllegalAccessException("Player " + player.getDisplayName().getString() + " is missing stat capability!"));
        }
    }

    public static void setPlayerRevertStatLevel(Player player, StatBase stat, int level) {
        if (player.getCapability(CapabilityStat.STAT).isPresent()) {
            player.getCapability(CapabilityStat.STAT).orElse(new StatStorage()).stateMap.get(stat).revertedLevel = level;
        } else {
            throw new RuntimeException(new IllegalAccessException("Player " + player.getDisplayName().getString() + " is missing stat capability!"));
        }
    }

    public static int getPlayerStatLevel(Player player, StatBase stat) {
        if (player.getCapability(CapabilityStat.STAT).isPresent()) {
            return player.getCapability(CapabilityStat.STAT).orElse(new StatStorage()).stateMap.get(stat).level;
        } else {
            throw new RuntimeException(new IllegalAccessException("Player " + player.getDisplayName().getString() + " is missing stat capability!"));
        }
    }

    public static void setPlayerStatLevel(Player player, StatBase stat, int level) {
        if (player.getCapability(CapabilityStat.STAT).isPresent()) {
            player.getCapability(CapabilityStat.STAT).orElse(new StatStorage()).stateMap.get(stat).level = level;
        } else {
            throw new RuntimeException(new IllegalAccessException("Player " + player.getDisplayName().getString() + " is missing stat capability!"));
        }
    }

    public static void multiplyPlayerStatLevel(Player player, StatBase stat, IntFunction<Integer> multiplier) {
        setPlayerStatLevel(player, stat, multiplier.apply(getPlayerStatLevel(player, stat)));
    }

    public static float trimDecimals(float in, int decimals) {
        float f = (float) (in * Math.pow(10.0D, decimals));
        int i = (int) f;
        return i / (float) Math.pow(10.0D, decimals);
    }

    public static void setPlayersExpTo(Player player, int total) {
        player.totalExperience = 0;
        player.experienceProgress = 0;
        player.experienceLevel = 0;
        player.giveExperiencePoints(total);
    }

    public static int getXPTotal(int xpLevel, float current) {
        return (int) (getXPValueFromLevel(xpLevel) + getXPValueToNextLevel(xpLevel) * current);
    }

    public static int getXPTotal(Player player) {
        return player.totalExperience;
    }

    public static boolean hasDamageModifier(ItemStack stack) {
        Collection<AttributeModifier> modifiers = stack.getItem().getAttributeModifiers(EquipmentSlot.MAINHAND, stack).get(Attributes.ATTACK_DAMAGE);
        return modifiers != null && !modifiers.isEmpty();
    }

    public static int getXPValueFromLevel(int xpLevel) {
        int val;
        if (xpLevel > 31) {
            val = (int) (4.5d * Math.pow(xpLevel, 2d) - 162.5d * xpLevel + 2220d);
        } else if (xpLevel > 16) {
            val = (int) (2.5d * Math.pow(xpLevel, 2d) - 40.5d * xpLevel + 360d);
        } else {
            val = (int) (Math.pow(xpLevel, 2d) + 6d * xpLevel);
        }
        return val;
    }

    public static int getXPValueToNextLevel(int xpLevel) {
        int val;
        if (xpLevel > 30) {
            val = 9 * xpLevel - 158;
        } else if (xpLevel > 15) {
            val = 5 * xpLevel - 38;
        } else {
            val = 2 * xpLevel + 7;
        }

        return val;
    }

    public static float getDamageDealt(Player player, Entity target, DamageSource source) {
        float damage = (float) player.getAttribute(Attributes.ATTACK_DAMAGE).getValue();
        float bonusDamage = 0.0F;
        boolean targetIsLiving = target instanceof LivingEntity;
        boolean critical;
        ItemStack stack = player.getMainHandItem();
        if (targetIsLiving) {
            EnchantmentHelper.getDamageBonus(stack, ((LivingEntity) target).getMobType());
        }
        if (damage > 0.0F || bonusDamage > 0.0F) {
            critical = player.fallDistance > 0.0F && !player.isOnGround() && !player.onClimbable() && !player.isInWater() && !player.hasEffect(MobEffects.BLINDNESS) && !player.isPassenger() && targetIsLiving;
            if (critical && damage > 0.0F) {
                damage *= 1.5F;
            }
            damage += bonusDamage;
        }
        return damage;
    }

    public static float getFallResistance(LivingEntity entity) {
        float resistance = 3.0F;
        MobEffectInstance potionEffect = entity.getActiveEffectsMap().get(MobEffects.JUMP);
        float bonus = potionEffect != null ? potionEffect.getAmplifier() + 1 : 0.0F;
        // TODO check if this work as float...

        return resistance + bonus;
    }
}
