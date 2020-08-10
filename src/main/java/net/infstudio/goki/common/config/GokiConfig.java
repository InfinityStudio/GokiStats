package net.infstudio.goki.common.config;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

/**
 * GokiStats mod config, in Forge config specification
 */
public class GokiConfig {
    public static final ForgeConfigSpec serverSpec;
    public static final Server SERVER;
    static {
        final Pair<Server, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Server::new);
        serverSpec = specPair.getRight();
        SERVER = specPair.getLeft();
    }

    public static class Server {
        public static boolean initiativeSync = false;

        public final ForgeConfigSpec.IntValue syncTicks;
        public final ForgeConfigSpec.DoubleValue globalCostMultiplier;
        public final ForgeConfigSpec.DoubleValue globalLimitMultiplier;
        public final ForgeConfigSpec.DoubleValue globalBonusMultiplier;
        public final ForgeConfigSpec.BooleanValue loseStatsOnDeath;
        public final ForgeConfigSpec.DoubleValue loseStatsMultiplier;
        public final ForgeConfigSpec.IntValue globalMaxRevertLevel;
        public final ForgeConfigSpec.DoubleValue globalRevertFactor;
        public final ForgeConfigSpec.IntValue reaperLimit;

        public Server(ForgeConfigSpec.Builder builder) {
            builder.comment("Server configuration settings")
                    .push("server");

            syncTicks = builder.comment("Ticks for initiative sync, default for 400 ticks (20s). Set to 0 to disable this feature")
                    .defineInRange("syncTicks", 400, 0, Integer.MAX_VALUE);
            globalCostMultiplier = builder.comment("A global multiplier on the cost to upgrade all stats")
                    .defineInRange("globalCostMultiplier", 1, 0, Double.MAX_VALUE);
            globalLimitMultiplier = builder.comment("A global multiplier on the max level limit of all stats")
                    .defineInRange("globalLimitMultiplier", 2.5, 0, Double.MAX_VALUE);
            globalBonusMultiplier = builder.comment("A global multiplier on the bonus all stats gives")
                    .defineInRange("globalBonusMultiplier", 1, 0, Double.MAX_VALUE);
            loseStatsOnDeath = builder.comment("Does your stats lose on death")
                    .define("loseStatsOnDeath", false);
            loseStatsMultiplier = builder.comment("Multiplier of levels you will lose, between 0-1")
                    .defineInRange("loseStatsMultiplier", 1, 0d, 1d);
            globalMaxRevertLevel = builder.comment("An integer that constrains the max number of amount of the skill can be reverted. -1 for no limit. 0 to disable reverting.")
                    .defineInRange("globalMaxRevertLevel", -1, -1, Integer.MAX_VALUE);
            globalRevertFactor = builder.comment("How much percentage of exp will be given back to player if a player revert a skill, between 0-1.")
                    .defineInRange("globalRevertFactor", 0.8, 0d, 1d);
            reaperLimit = builder.comment("When using reaper skill, the maximum health of target hostile. -1 for no limit.")
                    .defineInRange("reaperLimit", 20, -1, Integer.MAX_VALUE);
        }
    }
}
