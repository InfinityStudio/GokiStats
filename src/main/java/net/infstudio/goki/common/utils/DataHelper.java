package net.infstudio.goki.common.utils;

import net.infstudio.goki.api.capability.CapabilityStat;
import net.infstudio.goki.api.stat.Stat;
import net.infstudio.goki.api.stat.StatBase;
import net.infstudio.goki.api.stat.StatStorage;
import net.infstudio.goki.common.config.GokiConfig;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;

import java.util.Collection;
import java.util.function.IntFunction;

public class DataHelper {
    public static boolean canPlayerRevertStat(PlayerEntity player, Stat stat) {
        return GokiConfig.SERVER.globalMaxRevertLevel.get() == -1 ||
                (GokiConfig.SERVER.globalMaxRevertLevel.get() >= 0
                        && getPlayerRevertStatLevel(player, stat) < GokiConfig.SERVER.globalMaxRevertLevel.get()
                        && getPlayerStatLevel(player, stat) > 0);
    }

    public static int getPlayerRevertStatLevel(PlayerEntity player, Stat stat) {
        return player.getCapability(CapabilityStat.STAT).map(storage -> storage.stateMap.get(stat).revertedLevel).orElse(0);
    }

    public static void setPlayerRevertStatLevel(PlayerEntity player, Stat stat, int level) {
        player.getCapability(CapabilityStat.STAT).ifPresent(storage -> storage.stateMap.get(stat).revertedLevel = level);
    }

    public static int getPlayerStatLevel(PlayerEntity player, Stat stat) {
        return player.getCapability(CapabilityStat.STAT).map(storage -> storage.stateMap.get(stat).level).orElse(0);
    }

    public static void setPlayerStatLevel(PlayerEntity player, Stat stat, int level) {
        player.getCapability(CapabilityStat.STAT).ifPresent(storage -> storage.stateMap.get(stat).level = level);
    }

    public static void multiplyPlayerStatLevel(PlayerEntity player, Stat stat, IntFunction<Integer> multiplier) {
        setPlayerStatLevel(player, stat, multiplier.apply(getPlayerStatLevel(player, stat)));
    }

    public static float trimDecimals(float in, int decimals) {
        float f = (float) (in * Math.pow(10.0D, decimals));
        int i = (int) f;
        return i / (float) Math.pow(10.0D, decimals);
    }

    public static int getXPTotal(PlayerEntity player) {
        int level = player.experienceLevel;
        int offset = player.totalExperience;
        if (level <= 16)
            return level * level + 6 * level + offset;
        else if (level <= 31)
            return (int) (2.5 * level * level - 40.5 * level) + 360 + offset;
        else
            return (int) (4.5 * level * level - 162.5 * level) + 2220 + offset;
    }

    public static boolean hasDamageModifier(ItemStack stack) {
        Collection<AttributeModifier> modifiers = stack.getItem().getAttributeModifiers(EquipmentSlotType.MAINHAND, stack).get(Attributes.ATTACK_DAMAGE);
        return modifiers != null && !modifiers.isEmpty();
    }

    public static float getDamageDealt(PlayerEntity player, Entity target, DamageSource source) {
        float damage = (float) player.getAttribute(Attributes.ATTACK_DAMAGE).getValue();
        float bonusDamage = 0.0F;
        boolean targetIsLiving = target instanceof LivingEntity;
        boolean critical;
        ItemStack stack = player.getMainHandItem();
        if (targetIsLiving) {
            bonusDamage += EnchantmentHelper.getDamageBonus(stack, ((LivingEntity) target).getMobType());
        }
        if ((damage > 0.0F) || (bonusDamage > 0.0F)) {
            critical = (player.fallDistance > 0.0F) && (!player.isOnGround()) && (!player.onClimbable()) && (!player.isInWater()) && (!player.hasEffect(Effects.BLINDNESS)) && (player.getVehicle() == null) && (targetIsLiving);
            if ((critical) && (damage > 0.0F)) {
                damage *= 1.5F;
            }
            damage += bonusDamage;
        }
        return damage;
    }

    public static float getFallResistance(LivingEntity entity) {
        float resistance = 3.0F;
        EffectInstance potionEffect = entity.getEffect(Effects.JUMP);
        float bonus = potionEffect != null ? potionEffect.getAmplifier() + 1 : 0.0F;
        // TODO check if this work as float...

        return resistance + bonus;
    }
}
