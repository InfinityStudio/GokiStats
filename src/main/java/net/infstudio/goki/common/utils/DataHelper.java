package net.infstudio.goki.common.utils;

import net.infstudio.goki.common.config.GokiConfig;
import net.infstudio.goki.common.stats.StatBase;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;

import java.util.function.IntFunction;

public class DataHelper {
    public static NBTTagCompound getPlayerPersistentNBT(EntityPlayer player) {
        NBTTagCompound nbt = player.getEntityData().getCompoundTag("PlayerPersisted");
        if (!nbt.hasKey("gokistats_Stats")) {
            nbt.setTag("gokistats_Stats", new NBTTagCompound());
            player.getEntityData().setTag("PlayerPersisted", nbt);
        }
        return nbt;
    }

    public static boolean canPlayerRevertStat(EntityPlayer player, StatBase stat) {
        return GokiConfig.globalModifiers.globalMaxRevertLevel >= 0 && getPlayerRevertStatLevel(player, stat) < GokiConfig.globalModifiers.globalMaxRevertLevel && getPlayerStatLevel(player, stat) > 0;
    }

    public static int getPlayerRevertStatLevel(EntityPlayer player, StatBase stat) {
        NBTTagCompound nbt = getPlayerPersistentNBT(player);
        if (nbt.hasKey("gokistats_Stats")) {
            return ((NBTTagCompound) nbt.getTag("gokistats_Stats")).getInteger(stat.key + ".revert");
        }
        return 0;
    }

    public static int setPlayerRevertStatLevel(EntityPlayer player, StatBase stat, int level) {
        NBTTagCompound nbt = getPlayerPersistentNBT(player);
        if (nbt.hasKey("gokistats_Stats")) {
            ((NBTTagCompound) nbt.getTag("gokistats_Stats")).setInteger(stat.key + ".revert",
                    (byte) level);
        }
        return 0;
    }

    public static int getPlayerStatLevel(EntityPlayer player, StatBase stat) {
        NBTTagCompound nbt = getPlayerPersistentNBT(player);
        if (nbt.hasKey("gokistats_Stats")) {
            return ((NBTTagCompound) nbt.getTag("gokistats_Stats")).getInteger(stat.key);
        }
        return 0;
    }

    public static void setPlayerStatLevel(EntityPlayer player, StatBase stat, int level) {
        NBTTagCompound nbt = getPlayerPersistentNBT(player);
        if (nbt.hasKey("gokistats_Stats")) {
            ((NBTTagCompound) nbt.getTag("gokistats_Stats")).setInteger(stat.key,
                    (byte) level);
        }
    }

    public static void multiplyPlayerStatLevel(EntityPlayer player, StatBase stat, IntFunction<Integer> multiplier) {
        setPlayerStatLevel(player, stat, multiplier.apply(getPlayerStatLevel(player, stat)));
    }

    public static float trimDecimals(float in, int decimals) {
        float f = (float) (in * Math.pow(10.0D, decimals));
        int i = (int) f;
        return i / (float) Math.pow(10.0D, decimals);
    }

    public static void setPlayersExpTo(EntityPlayer player, int total) {
        player.experience = 0;
        player.experienceLevel = 0;
        player.experienceTotal = 0;
        player.addExperience(total);
    }

    public static int getXPTotal(int xpLevel, float current) {
        return (int) (getXPValueFromLevel(xpLevel) + getXPValueToNextLevel(xpLevel) * current);
    }

    public static int getXPTotal(EntityPlayer player) {
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

    public static float getDamageDealt(EntityPlayer player, Entity target, DamageSource source) {
        float damage = (float) player.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
        float bonusDamage = 0.0F;
        boolean targetIsLiving = target instanceof EntityLivingBase;
        boolean critical;
        ItemStack stack = player.getHeldItemMainhand();
        if (targetIsLiving) {
            bonusDamage = EnchantmentHelper.getModifierForCreature(stack, ((EntityLivingBase) target).getCreatureAttribute());
        }
        if ((damage > 0.0F) || (bonusDamage > 0.0F)) {
            critical = (player.fallDistance > 0.0F) && (!player.onGround) && (!player.isOnLadder()) && (!player.isInWater()) && (!player.isPotionActive(Potion.getPotionFromResourceLocation("blindness"))) && (player.getRidingEntity() == null) && (targetIsLiving);
            if ((critical) && (damage > 0.0F)) {
                damage *= 1.5F;
            }
            damage += bonusDamage;
        }
        return damage;
    }

    public static float getFallResistance(EntityLivingBase entity) {
        float resistance = 3.0F;
        PotionEffect potioneffect = entity.getActivePotionEffect(Potion.getPotionFromResourceLocation("jump"));
        float bonus = potioneffect != null ? potioneffect.getAmplifier() + 1 : 0.0F;
        // TODO check if this work as float...

        return resistance + bonus;
    }
}