package net.infstudio.goki.stats;

import net.infstudio.goki.lib.DataHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

import java.util.ArrayList;

public abstract class StatBase implements Stat {
    public static boolean loseStatsOnDeath = true;
    public static float globalCostMultiplier = 1.0F;
    public static float globalLimitMultiplier = 2.5F;
    public static float globalBonusMultiplier = 1.0F;
    public static ArrayList<StatBase> stats = new ArrayList<>(32);
    public static int totalStats = 0;
    //	 public static final StatBase STAT_FOCUS = new StatFocus(14, "grpg_Focus",
//	 "Focus", 25);
    public int imageID;
    public String key;
    public float costMultiplier = 1.0F;
    public float limitMultiplier = 1.0F;
    public float bonusMultiplier = 1.0F;
    public boolean enabled = true;
    String name, des;
    private int limit;

    public StatBase() {
        this.imageID = -1;
        this.limit = 0;
        this.key = "Dummy";
        this.name = "Dummy StatBase";
    }

    public StatBase(int imgId, String key, int limit) {
        this.imageID = imgId;
        this.limit = limit;
        this.key = key;
        stats.add(this);
        totalStats += 1;
    }

    protected static float getFinalBonus(float currentBonus) {
        return currentBonus * globalBonusMultiplier;
    }

    @Override
    public float getBonus(EntityPlayer player) {
        return getBonus(DataHelper.getPlayerStatLevel(player, this));
    }

    @Override
    public float[] getAppliedDescriptionVar(EntityPlayer player) {
        return new float[]
                {DataHelper.trimDecimals(getBonus(getPlayerStatLevel(player)) * 100, 1)};
    }

    @Override
    public int getCost(int level) {
        return (int) ((Math.pow(level, 1.6D) + 6.0D + level) * globalCostMultiplier);
    }

    @Override
    public boolean needAffectedByStat(Object... obj) {
        if (((obj[1] instanceof ItemStack)) && ((obj[2] instanceof BlockPos)) && ((obj[3] instanceof World))) {
            ItemStack stack = (ItemStack) obj[1];
            BlockPos pos = (BlockPos) obj[2];
            World world = (World) obj[3];

            return ForgeHooks.isToolEffective(world, pos, stack);
        }
        return false;
    }

    @Override
    public int getLimit() {
        if (globalLimitMultiplier <= 0.0F) {
            return 127;
        }
        return (int) (this.limit * globalLimitMultiplier);
    }

    @Override
    public float getAppliedBonus(EntityPlayer player, Object object) {
        if (needAffectedByStat(object))
            return getBonus(player);
        else
            return 0;
    }

    protected final int getPlayerStatLevel(EntityPlayer player) {
        return DataHelper.getPlayerStatLevel(player, this);
    }

    public final boolean needAffectedByStat(Object object1, Object object2, Object object3) {
        if (((object1 instanceof ItemStack)) && ((object2 instanceof BlockPos)) && ((object3 instanceof World))) {
            ItemStack stack = (ItemStack) object1;
            BlockPos pos = (BlockPos) object2;
            World world = (World) object3;

            if (ForgeHooks.isToolEffective(world, pos, stack))
                return true;
        }
        return needAffectedByStat(object1);
    }

    public String getLocalizedName() {
        return I18n.translateToLocal(this.key + ".name");
    }

    public String getLocalizedDes(EntityPlayer player) {
        return I18n.translateToLocalFormatted(this.key + ".des",
                this.getAppliedDescriptionVar(player)[0]);
    }


//	protected float getSecondaryBonus(int level)
//	{
//		return 0;
//	}

//	@Override
//	public float getSecondaryBonus(EntityPlayer player)
//	{
//		return getSecondaryBonus(DataHelper.getPlayerStatLevel(player, this));
//	}


//	public static StatBase getStat(int n)
//	{
//		return stats.get(n);
//	}


//	@Override
//	public abstract String getSimpleDescriptionString();


}