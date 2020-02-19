package net.infstudio.goki.api.stat;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.infstudio.goki.common.config.Configurable;
import net.infstudio.goki.common.config.GokiConfig;
import net.infstudio.goki.common.config.stats.StatConfig;
import net.infstudio.goki.common.utils.DataHelper;
import net.infstudio.goki.common.utils.Reference;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public abstract class StatBase<T extends StatConfig> extends IForgeRegistryEntry.Impl<Stat> implements Stat, Configurable<T> {
    public static final Map<String, StatBase> statKeyMap = new HashMap<>(16);
    public static final ObjectList<StatBase> stats = new ObjectArrayList<>(16);
    public static int totalStats = 0;
    //	 public static final StatBase STAT_FOCUS = new StatFocus(14, "grpg_Focus",
//	 "Focus", 25);
    public int imageID;
    public String key;
    public float costMultiplier = 1.0F;
    public float limitMultiplier = 1.0F;
    public float bonusMultiplier = 1.0F;
    public boolean enabled = true;
    private int limit;

    public StatBase(int imgId, String key, int limit) {
        this.imageID = imgId;
        this.limit = limit;
        this.key = key;
        stats.add(this);
        totalStats++;
        statKeyMap.put(key, this);
        setRegistryName(Reference.MODID, getKey().toLowerCase());
    }

    protected static float getFinalBonus(float currentBonus) {
        return currentBonus * GokiConfig.globalModifiers.globalBonusMultiplier;
    }

    @Override
    public T createConfig() {
        return (T) new StatConfig();
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public void save() {
        getConfig().bonusMultiplier = bonusMultiplier;
    }

    @Override
    public void reload() {
        bonusMultiplier = getConfig().bonusMultiplier;
    }

    @Override
    public float getBonus(EntityPlayer player) {
        return getBonus(DataHelper.getPlayerStatLevel(player, this)) * bonusMultiplier;
    }

    @Override
    public float[] getAppliedDescriptionVar(EntityPlayer player) {
        return new float[]
                {DataHelper.trimDecimals(getBonus(player) * 100, 1)};
    }

    @Override
    public int getCost(int level) {
        return (int) ((Math.pow(level, 1.6D) + 6.0D + level) * GokiConfig.globalModifiers.globalCostMultiplier);
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
        if (GokiConfig.globalModifiers.globalLimitMultiplier <= 0.0F) {
            return 127;
        }
        return (int) (this.limit * GokiConfig.globalModifiers.globalLimitMultiplier);
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

    @SideOnly(Side.CLIENT)
    public String getLocalizedName() {
        return I18n.format(this.key + ".name");
    }

    @SideOnly(Side.CLIENT)
    public String getLocalizedDescription(EntityPlayer player) {
        return I18n.format(this.key + ".des",
                this.getAppliedDescriptionVar(player)[0]);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StatBase)) return false;
        StatBase<?> statBase = (StatBase<?>) o;
        return Objects.equals(getKey(), statBase.getKey());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKey());
    }

    @Override
    public String toString() {
        return getKey();
    }
//	protected float getSecondaryBonus(int amount)
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
