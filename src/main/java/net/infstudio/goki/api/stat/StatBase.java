package net.infstudio.goki.api.stat;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.infstudio.goki.common.config.Configurable;
import net.infstudio.goki.common.config.GokiConfig;
import net.infstudio.goki.common.config.stats.StatConfig;
import net.infstudio.goki.common.utils.DataHelper;
import net.infstudio.goki.common.utils.Reference;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.Objects;

public abstract class StatBase<T extends StatConfig> extends ForgeRegistryEntry<Stat> implements Stat, Configurable<T> {
    public static final IForgeRegistry<Stat> REGISTRY = new RegistryBuilder<Stat>()
            .setName(new ResourceLocation(Reference.MODID, "stats"))
            .setType(Stat.class)
            .create();
    public static final ObjectList<StatBase<?>> stats = new ObjectArrayList<>(16);
    public static LazyOptional<Integer> totalStats = LazyOptional.of(() -> REGISTRY.getValues().size());
    public int imageID;
    public String key;

    private boolean enabled = true;
    private final int limit;

    public final T config;
    public final ForgeConfigSpec configSpec;

    public StatBase(int imgId, String key, int limit) {
        this.imageID = imgId;
        this.limit = limit;
        this.key = key;
        stats.add(this);

        final var specPair = new ForgeConfigSpec.Builder()
                .configure(this::createConfig);
        configSpec = specPair.getRight();
        config = specPair.getLeft();

        setRegistryName(Reference.MODID, key.toLowerCase());
        REGISTRY.register(this);
    }

    protected float getFinalBonus(float currentBonus) {
        return (float) (currentBonus * config.bonusMultiplier.get() * GokiConfig.SERVER.globalBonusMultiplier.get());
    }

    @Override
    @SuppressWarnings("unchecked")
    public T createConfig(ForgeConfigSpec.Builder builder) {
        builder.comment("Server configuration settings")
                .push("stats." + key);
        return (T) new StatConfig(builder);
    }

    @Override
    public double getBonus(Player player) {
        return getBonus(DataHelper.getPlayerStatLevel(player, this)) * getBonusMultiplier();
    }

    @Override
    public double[] getDescriptionFormatArguments(Player player) {
        return new double[]
                {DataHelper.trimDecimals(getBonus(player) * 100, 1)};
    }

    @Override
    public int getCost(int level) {
        return (int) ((Math.pow(level, 1.6D) + 6.0D + level) * config.costMultiplier.get() * GokiConfig.SERVER.globalCostMultiplier.get());
    }

    @Override
    public boolean isEffectiveOn(Object... obj) {
        if (obj[0] instanceof ItemStack stack && obj[1] instanceof BlockPos pos && obj[2] instanceof Level world) {

            return stack.isCorrectToolForDrops(world.getBlockState(pos));
        }
        return false;
    }

    @Override
    public int getLimit() {
        var limit = this.limit;
        if (config.maxLevel.get() > 0) return config.maxLevel.get();
        if (GokiConfig.SERVER.globalLimitMultiplier.get() <= 0) {
            return limit;
        }
        return (int) (limit * GokiConfig.SERVER.globalLimitMultiplier.get());
    }

    @Override
    public double getAppliedBonus(Player player, Object object) {
        if (isEffectiveOn(object))
            return getBonus(player);
        else
            return 0;
    }

    protected final int getPlayerStatLevel(Player player) {
        return DataHelper.getPlayerStatLevel(player, this);
    }

    public final boolean isEffectiveOn(ItemStack stack, BlockPos pos, Level world) {
        if (stack.isCorrectToolForDrops(world.getBlockState(pos)))
            return true;
        else return isEffectiveOn(stack);
    }

    @OnlyIn(Dist.CLIENT)
    public String getLocalizedName() {
        return I18n.get("skill.gokistats." + this.key);
    }

    @OnlyIn(Dist.CLIENT)
    public String getLocalizedDescription(Player player) {
        return I18n.get("skill.gokistats." + this.key + ".text",
                this.getDescriptionFormatArguments(player)[0]);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StatBase)) return false;
        var statBase = (StatBase<?>) o;
        return Objects.equals(getRegistryName(), statBase.getRegistryName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRegistryName());
    }

    @Override
    public String toString() {
        return getRegistryName().toString();
    }

    @Override
    public boolean isEnabled() {
        return getLimit() != 0 && enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public double getCostMultiplier() {
        return config.costMultiplier.get();
    }

    public void setCostMultiplier(double costMultiplier) {
        this.config.costMultiplier.set(costMultiplier);
    }

    public double getBonusMultiplier() {
        return config.bonusMultiplier.get();
    }

    public void setBonusMultiplier(double bonusMultiplier) {
        this.config.bonusMultiplier.set(bonusMultiplier);
    }
}
