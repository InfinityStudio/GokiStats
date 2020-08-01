package net.infstudio.goki.common.utils;

import net.infstudio.goki.api.capability.CapabilityStat;
import net.infstudio.goki.api.stat.StatBase;
import net.infstudio.goki.api.stat.StatStorage;
import net.infstudio.goki.common.config.GokiConfig;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;

import java.util.function.IntFunction;

public class DataHelper {
    public static boolean canPlayerRevertStat(PlayerEntity player, StatBase stat) {
        return GokiConfig.globalModifiers.globalMaxRevertLevel == -1 ||
                (GokiConfig.globalModifiers.globalMaxRevertLevel >= 0
                        && getPlayerRevertStatLevel(player, stat) < GokiConfig.globalModifiers.globalMaxRevertLevel
                        && getPlayerStatLevel(player, stat) > 0);
    }

    public static int getPlayerRevertStatLevel(PlayerEntity player, StatBase stat) {
        if (player.getCapability(CapabilityStat.STAT).isPresent()) {
            return player.getCapability(CapabilityStat.STAT).orElse(new StatStorage()).stateMap.get(stat).revertedLevel;
        } else {
            throw new RuntimeException(new IllegalAccessException("Player " + player.getDisplayNameAndUUID().getFormattedText() + " is missing stat capability!"));
        }
    }

    public static void setPlayerRevertStatLevel(PlayerEntity player, StatBase stat, int level) {
        if (player.getCapability(CapabilityStat.STAT).isPresent()) {
            player.getCapability(CapabilityStat.STAT).orElse(new StatStorage()).stateMap.get(stat).revertedLevel = level;
        } else {
            throw new RuntimeException(new IllegalAccessException("Player " + player.getDisplayNameAndUUID().getFormattedText() + " is missing stat capability!"));
        }
    }

    public static int getPlayerStatLevel(PlayerEntity player, StatBase stat) {
        if (player.getCapability(CapabilityStat.STAT).isPresent()) {
            return player.getCapability(CapabilityStat.STAT).orElse(new StatStorage()).stateMap.get(stat).level;
        } else {
            throw new RuntimeException(new IllegalAccessException("Player " + player.getDisplayNameAndUUID().getFormattedText() + " is missing stat capability!"));
        }
    }

    public static void setPlayerStatLevel(PlayerEntity player, StatBase stat, int level) {
        if (player.getCapability(CapabilityStat.STAT).isPresent()) {
            player.getCapability(CapabilityStat.STAT).orElse(new StatStorage()).stateMap.get(stat).level = level;
        } else {
            throw new RuntimeException(new IllegalAccessException("Player " + player.getDisplayNameAndUUID().getFormattedText() + " is missing stat capability!"));
        }
    }

    public static void multiplyPlayerStatLevel(PlayerEntity player, StatBase stat, IntFunction<Integer> multiplier) {
        setPlayerStatLevel(player, stat, multiplier.apply(getPlayerStatLevel(player, stat)));
    }

    public static float trimDecimals(float in, int decimals) {
        float f = (float) (in * Math.pow(10.0D, decimals));
        int i = (int) f;
        return i / (float) Math.pow(10.0D, decimals);
    }

    public static void setPlayersExpTo(PlayerEntity player, int total) {
        player.experience = 0;
        player.experienceLevel = 0;
        player.experienceTotal = 0;
        player.giveExperiencePoints(total);
    }

    public static int getXPTotal(int xpLevel, float current) {
        return (int) (getXPValueFromLevel(xpLevel) + getXPValueToNextLevel(xpLevel) * current);
    }

    public static int getXPTotal(PlayerEntity player) {
        return (int) (getXPValueFromLevel(player.experienceLevel) + getXPValueToNextLevel(player.experienceLevel) * player.experience);
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

    public static float getDamageDealt(PlayerEntity player, Entity target, DamageSource source) {
        float damage = (float) player.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getValue();
        float bonusDamage = 0.0F;
        boolean targetIsLiving = target instanceof LivingEntity;
        boolean critical;
        ItemStack stack = player.getHeldItemMainhand();
        if (targetIsLiving) {
            bonusDamage = EnchantmentHelper.getModifierForCreature(stack, ((LivingEntity) target).getCreatureAttribute());
        }
        if ((damage > 0.0F) || (bonusDamage > 0.0F)) {
            critical = (player.fallDistance > 0.0F) && (!player.onGround) && (!player.isOnLadder()) && (!player.isInWater()) && (!player.isPotionActive(Effects.BLINDNESS)) && (player.getRidingEntity() == null) && (targetIsLiving);
            if ((critical) && (damage > 0.0F)) {
                damage *= 1.5F;
            }
            damage += bonusDamage;
        }
        return damage;
    }

    public static float getFallResistance(LivingEntity entity) {
        float resistance = 3.0F;
        EffectInstance potionEffect = entity.getActivePotionEffect(Effects.JUMP_BOOST);
        float bonus = potionEffect != null ? potionEffect.getAmplifier() + 1 : 0.0F;
        // TODO check if this work as float...

        return resistance + bonus;
    }
}
