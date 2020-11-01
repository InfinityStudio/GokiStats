package net.infstudio.goki.api.stat;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.infstudio.goki.common.config.Configurable;
import net.infstudio.goki.common.config.GokiConfig;
import net.infstudio.goki.common.config.stats.StatConfig;
import net.infstudio.goki.common.utils.DataHelper;
import net.infstudio.goki.common.utils.Reference;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Objects;

public abstract class StatBase<T extends StatConfig> extends ForgeRegistryEntry<Stat> implements Stat, Configurable<T> {
    public static final IForgeRegistry<Stat> REGISTRY = new RegistryBuilder<Stat>()
            .setName(new ResourceLocation(Reference.MODID, "stats"))
            .setType(Stat.class)
            .create();
    public static final ObjectList<StatBase> stats = new ObjectArrayList<>(16);
    public static LazyOptional<Integer> totalStats = LazyOptional.of(() -> REGISTRY.getValues().size());
    public int imageID;
    public String key;

    public float costMultiplier = 1.0F;
    public float limitMultiplier = 1.0F;
    public float bonusMultiplier = 1.0F;
    private boolean enabled = true;
    private final int limit;

    public final T config;
    public final ForgeConfigSpec configSpec;

    public StatBase(int imgId, String key, int limit) {
        this.imageID = imgId;
        this.limit = limit;
        this.key = key;
        stats.add(this);

        final Pair<T, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder()
                .configure(this::createConfig);
        configSpec = specPair.getRight();
        config = specPair.getLeft();

        setRegistryName(Reference.MODID, key.toLowerCase());
        REGISTRY.register(this);
    }

    protected static float getFinalBonus(float currentBonus) {
        return (float) (currentBonus * GokiConfig.SERVER.globalBonusMultiplier.get());
    }

    @Override
    public T createConfig(ForgeConfigSpec.Builder builder) {
        builder.comment("Server configuration settings")
                .push("stats." + key);
        return (T) new StatConfig(builder);
    }
/*
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
 */
    @Override
    public float getBonus(PlayerEntity player) {
        return getBonus(DataHelper.getPlayerStatLevel(player, this)) * bonusMultiplier;
    }

    @Override
    public float[] getDescriptionFormatArguments(PlayerEntity player) {
        return new float[]
                {DataHelper.trimDecimals(getBonus(player) * 100, 1)};
    }

    @Override
    public int getCost(int level) {
        return (int) ((Math.pow(level, 1.6D) + 6.0D + level) * GokiConfig.SERVER.globalCostMultiplier.get());
    }

    @Override
    public boolean isEffectiveOn(Object... obj) {
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
        if (GokiConfig.SERVER.globalLimitMultiplier.get() <= 0) {
            return 127;
        }
        return (int) (this.limit * GokiConfig.SERVER.globalLimitMultiplier.get());
    }

    @Override
    public float getAppliedBonus(PlayerEntity player, Object object) {
        if (isEffectiveOn(object))
            return getBonus(player);
        else
            return 0;
    }

    protected final int getPlayerStatLevel(PlayerEntity player) {
        return DataHelper.getPlayerStatLevel(player, this);
    }

    public final boolean isEffectiveOn(ItemStack stack, BlockPos pos, World world) {
        if (ForgeHooks.isToolEffective(world, pos, stack))
            return true;
        else return isEffectiveOn(stack);
    }

    @OnlyIn(Dist.CLIENT)
    public String getLocalizedName() {
        return I18n.format("skill.gokistats." + this.key);
    }

    @OnlyIn(Dist.CLIENT)
    public String getLocalizedDescription(PlayerEntity player) {
        if (isEnabled())
            return I18n.format("skill.gokistats." + this.key + ".text",
                this.getDescriptionFormatArguments(player)[0]);
        else
            return I18n.format("skill.gokistats." + this.key + ".disabled",
                this.getDescriptionFormatArguments(player)[0]);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StatBase)) return false;
        StatBase<?> statBase = (StatBase<?>) o;
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
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
