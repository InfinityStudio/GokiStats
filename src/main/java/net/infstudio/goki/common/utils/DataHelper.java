package net.infstudio.goki.common.utils;

import net.infstudio.goki.api.capability.CapabilityStat;
import net.infstudio.goki.api.stat.StatBase;
import net.infstudio.goki.api.stat.StatStorage;
import net.infstudio.goki.api.stat.Stats;
import net.infstudio.goki.common.config.GokiConfig;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

import java.util.function.IntFunction;

public class DataHelper {
    public static boolean canPlayerRevertStat(Player player, StatBase stat) {
        return GokiConfig.SERVER.globalMaxRevertLevel.get() == -1 ||
                (GokiConfig.SERVER.globalMaxRevertLevel.get() >= 0
                        && getPlayerRevertStatLevel(player, stat) < GokiConfig.SERVER.globalMaxRevertLevel.get()
                        && getPlayerStatLevel(player, stat) > 0);
    }

    public static int getPlayerRevertStatLevel(Player player, StatBase stat) {
        return player.getCapability(CapabilityStat.STAT).orElse(new StatStorage()).stateMap.get(stat).revertedLevel;
    }

    public static void setPlayerRevertStatLevel(Player player, StatBase stat, int level) {
        player.getCapability(CapabilityStat.STAT).orElse(new StatStorage()).stateMap.get(stat).revertedLevel = level;
    }

    public static int getPlayerStatLevel(Player player, StatBase stat) {
        return player.getCapability(CapabilityStat.STAT).orElse(new StatStorage()).stateMap.get(stat).level;
    }

    public static void setPlayerStatLevel(Player player, StatBase stat, int level) {
        player.getCapability(CapabilityStat.STAT).orElse(new StatStorage()).stateMap.get(stat).level = level;
    }

    public static void multiplyPlayerStatLevel(Player player, StatBase stat, IntFunction<Integer> multiplier) {
        setPlayerStatLevel(player, stat, multiplier.apply(getPlayerStatLevel(player, stat)));
    }

    public static void addMaxHealth(Player player, int amount) {
        var attribute = player.getAttribute(Attributes.MAX_HEALTH);
        attribute.setBaseValue(20 + amount);
        attribute.removeModifiers();
        attribute.addPermanentModifier(new AttributeModifier("MaxHealth", DataHelper.getPlayerStatLevel(player, Stats.MAX_HEALTH), AttributeModifier.Operation.ADDITION));
    }

    public static void resetMaxHealth(Player player) {
        addMaxHealth(player, DataHelper.getPlayerStatLevel(player, Stats.MAX_HEALTH));
    }

    public static double trimDecimals(double in, int decimals) {
        var f = (float) (in * Math.pow(10.0D, decimals));
        var i = (int) f;
        return i / (float) Math.pow(10.0D, decimals);
    }

    public static int getXPTotal(Player player) {
        var level = player.experienceLevel;
        var xp = player.totalExperience;

        if (level >= 0 && level <= 15) {
            xp = (int) Math.round(Math.pow(level, 2) + 6 * level);
        } else if (level > 15 && level <= 30) {
            xp = (int) Math.round((2.5 * Math.pow(level, 2) - 40.5 * level + 360));
        } else if (level > 30) {
            xp = (int) Math.round(((4.5 * Math.pow(level, 2) - 162.5 * level + 2220)));
        }
        return xp;
    }

    public static boolean hasDamageModifier(ItemStack stack) {
        var modifiers = stack.getItem().getAttributeModifiers(EquipmentSlot.MAINHAND, stack).get(Attributes.ATTACK_DAMAGE);
        return !modifiers.isEmpty();
    }

    public static float getDamageDealt(Player player, Entity target, DamageSource source) {
        var damage = (float) player.getAttribute(Attributes.ATTACK_DAMAGE).getValue();
        var bonusDamage = 0.0F;
        var targetIsLiving = target instanceof LivingEntity;
        boolean critical;
        var stack = player.getMainHandItem();
        if (targetIsLiving) {
            bonusDamage += EnchantmentHelper.getDamageBonus(stack, ((LivingEntity) target).getMobType());
        }
        if ((damage > 0.0F) || (bonusDamage > 0.0F)) {
            critical = (player.fallDistance > 0.0F) && (!player.isOnGround()) && (!player.onClimbable()) && (!player.isInWater()) && (!player.hasEffect(MobEffects.BLINDNESS)) && (player.getVehicle() == null) && (targetIsLiving);
            if ((critical) && (damage > 0.0F)) {
                damage *= 1.5F;
            }
            damage += bonusDamage;
        }
        return damage;
    }

    public static float getFallResistance(LivingEntity entity) {
        var resistance = 3.0F;
        var potionEffect = entity.getEffect(MobEffects.JUMP);
        var bonus = potionEffect != null ? potionEffect.getAmplifier() + 1 : 0.0F;
        // TODO check if this work as float...

        return resistance + bonus;
    }
}
