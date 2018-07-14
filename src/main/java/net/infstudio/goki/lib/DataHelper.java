package net.infstudio.goki.lib;

import net.infstudio.goki.stats.Stat;
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
import net.minecraftforge.common.config.Configuration;

public class DataHelper {
    public static NBTTagCompound getPlayerPersistentNBT(EntityPlayer player) {
        NBTTagCompound nbt = player.getEntityData().getCompoundTag("PlayerPersisted");
        if (!nbt.hasKey("gokistats_Stats")) {
            nbt.setTag("gokistats_Stats", new NBTTagCompound());
            player.getEntityData().setTag("PlayerPersisted", nbt);
        }
        return nbt;
    }

    public static int getPlayerStatLevel(EntityPlayer player, Stat stat) {
        NBTTagCompound nbt = getPlayerPersistentNBT(player);
        if (nbt.hasKey("gokistats_Stats")) {
            return ((NBTTagCompound) nbt.getTag("gokistats_Stats")).getInteger(stat.key);
        }
        return 0;
    }

    public static void setPlayerStatLevel(EntityPlayer player, Stat stat, int level) {
        NBTTagCompound nbt = getPlayerPersistentNBT(player);
        if (nbt.hasKey("gokistats_Stats")) {
            ((NBTTagCompound) nbt.getTag("gokistats_Stats")).setInteger(stat.key,
                    (byte) level);
        }
    }

    public static float trimDecimals(float in, int decimals) {
        float f = (float) (in * Math.pow(10.0D, decimals));
        int i = (int) f;
        return i / (float) Math.pow(10.0D, decimals);
    }

    public static void setPlayersExpTo(EntityPlayer player, int total) {
        player.experienceLevel = getLevelFromXPValue(total);
        player.experience = getCurrentFromXPValue(total);
    }

    public static int getXPTotal(int xpLevel, float current) {
        return (int) (getXPValueFromLevel(xpLevel) + getXPValueToNextLevel(xpLevel) * current);
    }

    public static int getXPTotal(EntityPlayer player) {
        return (int) (getXPValueFromLevel(player.experienceLevel) + getXPValueToNextLevel(player.experienceLevel) * player.experience);
    }

    public static int getLevelFromXPValue(int value) {
        int level = 0;
        if (value >= getXPValueFromLevel(30)) {
            level = (int) (0.07142857142857143D * (Math.sqrt(56.0D * value - 32511.0D) + 303.0D));
        } else if (value >= getXPValueFromLevel(15)) {
            level = (int) (0.1666666666666667D * (Math.sqrt(24.0D * value - 5159.0D) + 59.0D));
        } else {
            level = (int) (value / 17.0D);
        }
        return level;
    }

    public static float getCurrentFromXPValue(int value) {
        if (value == 0) {
            return 0.0F;
        }
        int level = getLevelFromXPValue(value);
        int needed = getXPValueFromLevel(level);
        int next = getXPValueToNextLevel(level);
        int difference = value - needed;
        float current = difference / next;
        return current;
    }

    public static int getXPValueFromLevel(int xpLevel) {
        int val = 0;
        if (xpLevel >= 30) {
            val = (int) (3.5D * Math.pow(xpLevel, 2.0D) - 151.5D * xpLevel + 2220.0D);
        } else if (xpLevel >= 15) {
            val = (int) (1.5D * Math.pow(xpLevel, 2.0D) - 29.5D * xpLevel + 360.0D);
        } else {
            val = 17 * xpLevel;
        }
        return val;
    }

    public static int getXPValueToNextLevel(int xpLevel) {
        int val = 0;
        if (xpLevel >= 30) {
            val = 7 * xpLevel - 148;
        } else if (xpLevel >= 15) {
            val = 3 * xpLevel - 28;
        } else {
            val = 17;
        }

        return val;
    }

    public static float getDamageDealt(EntityPlayer player, Entity target, DamageSource source) {
        float damage = (float) player.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
        float bonusDamage = 0.0F;
        boolean targetIsLiving = target instanceof EntityLivingBase;
        boolean critical = false;
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

    public static void loadOptions(Configuration config) {
        Stat.globalCostMultiplier = (float) config.get("Global Modifiers",
                "Cost Muliplier",
                1.0D,
                "A flat multiplier on the cost to upgrade all stats.").getDouble(1.0D);
        Stat.globalLimitMultiplier = (float) config.get("Global Modifiers",
                "Limit Muliplier",
                2.5D,
                "A flat multiplier on the level limit of all stats.").getDouble(1.0D);
        Stat.globalBonusMultiplier = (float) config.get("Global Modifiers",
                "Bonus Muliplier",
                1.0D,
                "A flat multiplier on the bonus all stats gives.").getDouble(1.0D);
        Stat.loseStatsOnDeath = config.get("Options",
                "Death Loss",
                false,
                "Lose stats on death?").getBoolean(true);
    }

    public static void saveGlobalMultipliers(Configuration config) {
        config.get("Global Modifiers",
                "Cost Muliplier",
                1.0D,
                "A flat multiplier on the cost to upgrade all stats.").set(Stat.globalCostMultiplier);
        config.get("Global Modifiers",
                "Limit Muliplier",
                2.5D,
                "A flat multiplier on the level limit of all stats.").set(Stat.globalLimitMultiplier);
        config.get("Global Modifiers",
                "Bonus Muliplier",
                1.0D,
                "A flat multiplier on the bonus all stats gives.").set(Stat.globalBonusMultiplier);
        config.get("Options", "Death Loss", true, "Lose stats on death?").set(Stat.loseStatsOnDeath);
    }
}