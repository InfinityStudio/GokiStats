package net.goki.stats;

import java.util.ArrayList;

import net.goki.lib.DataHelper;
import net.goki.stats.damage.DamageSourceProtectionStat;
import net.goki.stats.damage.StatFeatherFall;
import net.goki.stats.damage.StatProtection;
import net.goki.stats.damage.StatTempering;
import net.goki.stats.damage.StatToughSkin;
import net.goki.stats.tool.StatBowmanship;
import net.goki.stats.tool.StatChopping;
import net.goki.stats.tool.StatDigging;
import net.goki.stats.tool.StatMining;
import net.goki.stats.tool.StatSwordsmanship;
import net.goki.stats.tool.StatTrimming;
import net.goki.stats.tool.ToolSpecificStat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.config.Configuration;

public abstract class Stat implements IStat
{
	public static boolean loseStatsOnDeath = true;
	public static float globalCostMultiplier = 1.0F;
	public static float globalLimitMultiplier = 2.5F;
	public static float globalBonusMultiplier = 1.0F;
	public static ArrayList<Stat> stats = new ArrayList<Stat>(32);
	public static int totalStats = 0;

	public static final ToolSpecificStat STAT_MINING = new StatMining(0, "grpg_Mining", 10);
	public static final ToolSpecificStat STAT_DIGGING = new StatDigging(1, "grpg_Digging", 10);
	public static final ToolSpecificStat STAT_CHOPPING = new StatChopping(2, "grpg_Chopping", 10);
	public static final ToolSpecificStat STAT_TRIMMING = new StatTrimming(3, "grpg_Trimming", 10);
	public static final ToolSpecificStat STAT_SWORDSMANSHIP = new StatSwordsmanship(13, "grpg_Swordsmanship", 10);
	public static final ToolSpecificStat STAT_BOWMANSHIP = new StatBowmanship(14, "grpg_Bowmanship", 10);
	
	public static final DamageSourceProtectionStat STAT_PROTECTION = new StatProtection(4, "grpg_Protection", 10);
	public static final DamageSourceProtectionStat STAT_TEMPERING = new StatTempering(5, "grpg_Tempering", 10);
	public static final DamageSourceProtectionStat STAT_TOUGH_SKIN = new StatToughSkin(6, "grpg_ToughSkin", 10);
	public static final DamageSourceProtectionStat STAT_FEATHER_FALL = new StatFeatherFall(7, "grpg_FeatherFall", 10);
	
	public static final Stat STAT_LEAPERH = new StatLeaperH(8, "grpg_LeaperH", 10);
	public static final Stat STAT_LEAPERV = new StatLeaperV(9, "grpg_LeaperV", 10);
	public static final Stat STAT_SWIMMING = new StatSwimming(10, "grpg_Swimming", 10);
	public static final Stat STAT_CLIMBING = new StatClimbing(11, "grpg_Climbing", 10);
	public static final Stat STAT_PUGILISM = new StatPugilism(12, "grpg_Pugilism", 10);
	
	public static final Stat STAT_REAPER = new StatReaper(16, "grpg_Reaper", 10);
	
	public static final Stat STAT_FURNACE_FINESSE = new StatFurnaceFinesse(17, "grpg_Furnace_Finesse", 10);
	public static final Stat STAT_STEADY_GUARD = new StatSteadyGuard(18, "grpg_Steady_Guard", 10);
	public static final Stat STAT_STEALTH = new StatStealth(19, "grpg_Stealth", 10);
	
	public static final StatTreasureFinder STAT_TREASURE_FINDER = new StatTreasureFinder(19, "grpg_Treasure_Finder", 3);
	public static final StatMiningMagician STAT_MINING_MAGICIAN = new StatMiningMagician(20, "grpg_Mining_Magician", 10);
//	 public static final Stat STAT_FOCUS = new StatFocus(14, "grpg_Focus",
//	 "Focus", 25);
	public int imageID;
	private int limit;
	public String key;
	String name, des;
	public float costMultiplier = 1.0F;
	public float limitMultiplier = 1.0F;
	public float bonusMultiplier = 1.0F;
	public boolean enabled = true;

	public Stat()
	{
		this.imageID = -1;
		this.limit = 0;
		this.key = "Dummy";
		this.name = "Dummy Stat";
	}

	public Stat(int imgId, String key, int limit)
	{
		this.imageID = imgId;
		this.limit = limit;
		this.key = key;
		stats.add(this);
		totalStats += 1;
	}

	@Override
	public float getBonus(int level)
	{
		return 0.0F;
	}

	@Override
	public float getBonus(EntityPlayer player)
	{
		return getBonus(DataHelper.getPlayerStatLevel(player, this));
	}

	@Override
	public String getSimpleDescriptionString()
	{
		return null;
	}

	@Override
	public float[] getAppliedDescriptionVar(EntityPlayer player)
	{
		return new float[]
		{ DataHelper.trimDecimals(getBonus(getPlayerStatLevel(player)) * 100, 1) };
	}

	@Override
	public int getCost(int level)
	{
		return (int) ((Math.pow(level, 1.6D) + 6.0D + level) * globalCostMultiplier);
	}

	@Override
	public float getSecondaryBonus(int level)
	{
		return 0.0F;
	}

	@Override
	public float getSecondaryBonus(EntityPlayer player)
	{
		return getSecondaryBonus(DataHelper.getPlayerStatLevel(player, this));
	}

	@Override
	public boolean needAffectedByStat(Object... obj)
	{
		if (((obj[1] instanceof ItemStack)) && ((obj[2] instanceof BlockPos)) && ((obj[3] instanceof World)))
		{
			ItemStack stack = (ItemStack) obj[1];
			BlockPos pos = (BlockPos) obj[2];
			World world = (World) obj[3];

			if (ForgeHooks.isToolEffective(world, pos, stack))
				return true;
		}
		return false;
	}

	@Override
	public int getLimit()
	{
		if (globalLimitMultiplier <= 0.0F)
		{
			return 127;
		}
		return (int) (this.limit * globalLimitMultiplier);
	}

	@Override
	public float getAppliedBonus(EntityPlayer player, Object object)
	{
		if (needAffectedByStat(object))
			return getBonus(player);
		else
			return 0;
	}

	public static Stat getStat(int n)
	{
		return stats.get(n);
	}

	public static float getFinalBonus(float currentBonus)
	{
		return currentBonus * globalBonusMultiplier;
	}

	protected int getPlayerStatLevel(EntityPlayer player)
	{
		return DataHelper.getPlayerStatLevel(player, this);
	}

	public void loadFromConfigurationFile(Configuration config)
	{
	}

	public void fromConfigurationString(String configString)
	{
	}

	public void saveToConfigurationFile(Configuration config)
	{
	}

	public String toConfigurationString()
	{
		return "!";
	}

	public static void loadAllStatsFromConfiguration(Configuration config)
	{
		for (int i = 0; i < totalStats; i++)
		{
			(stats.get(i)).loadFromConfigurationFile(config);
		}
	}

	public static void saveAllStatsToConfiguration(Configuration config)
	{
		for (int i = 0; i < totalStats; i++)
		{
			(stats.get(i)).saveToConfigurationFile(config);
		}
	}

	public boolean needAffectedByStat(Object object1, Object object2, Object object3)
	{
		if (((object1 instanceof ItemStack)) && ((object2 instanceof BlockPos)) && ((object3 instanceof World)))
		{
			ItemStack stack = (ItemStack) object1;
			BlockPos pos = (BlockPos) object2;
			World world = (World) object3;

			if (ForgeHooks.isToolEffective(world, pos, stack))
				return true;
		}
		return needAffectedByStat(object1);
	}

	public String getLocalizedName()
	{
		return StatCollector.translateToLocal(this.key + ".name");
	}

	public String getLocalizedDes(EntityPlayer player)
	{
		return StatCollector.translateToLocalFormatted(	this.key + ".des",
														this.getAppliedDescriptionVar(player)[0]);
	}

}