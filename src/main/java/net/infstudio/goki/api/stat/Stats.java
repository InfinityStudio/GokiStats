package net.infstudio.goki.api.stat;

import net.infstudio.goki.common.stat.StatMaxHealth;
import net.infstudio.goki.common.stat.StatReaper;
import net.infstudio.goki.common.stat.StatTreasureFinder;
import net.infstudio.goki.common.stat.damage.*;
import net.infstudio.goki.common.stat.movement.StatClimbing;
import net.infstudio.goki.common.stat.movement.StatSteadyGuard;
import net.infstudio.goki.common.stat.movement.StatSwimming;
import net.infstudio.goki.common.stat.special.StatFurnaceFinesse;
import net.infstudio.goki.common.stat.special.leaper.StatLeaperH;
import net.infstudio.goki.common.stat.special.leaper.StatLeaperV;
import net.infstudio.goki.common.stat.special.leaper.StatStealth;
import net.infstudio.goki.common.stat.tool.*;

public interface Stats {
    ToolSpecificStat MINING = new StatMining(0, "mining", 10);
    ToolSpecificStat DIGGING = new StatDigging(1, "digging", 10);
    ToolSpecificStat CHOPPING = new StatChopping(2, "chopping", 10);
    ToolSpecificStat TRIMMING = new StatTrimming(3, "trimming", 10);
    DamageSourceProtectionStat PROTECTION = new StatProtection(4, "protection", 10);
    DamageSourceProtectionStat TEMPERING = new StatTempering(5, "tempering", 10);
    DamageSourceProtectionStat TOUGH_SKIN = new StatToughSkin(6, "tough_skin", 10);
    DamageSourceProtectionStat STAT_FEATHER_FALL = new StatFeatherFall(7, "feather_fall", 10);
    StatBase LEAPER_H = new StatLeaperH(8, "leaper_h", 10);
    StatBase LEAPER_V = new StatLeaperV(9, "leaper_v", 10);
    StatBase SWIMMING = new StatSwimming(10, "swimming", 10);
    StatBase CLIMBING = new StatClimbing(11, "climbing", 10);
    StatBase PUGILISM = new StatPugilism(12, "pugilism", 10);
    ToolSpecificStat SWORDSMANSHIP = new StatSwordsmanship(13, "swordsmanship", 10);
    ToolSpecificStat BOWMANSHIP = new StatBowmanship(14, "bowmanship", 10);
    StatBase REAPER = new StatReaper(15, "reaper", 10);
    StatBase FURNACE_FINESSE = new StatFurnaceFinesse(17, "furnace_finesse", 10);
    StatTreasureFinder TREASURE_FINDER = new StatTreasureFinder(16, "treasure_finder", 3);
    StatBase STEALTH = new StatStealth(19, "stealth", 10);
    StatBase STEADY_GUARD = new StatSteadyGuard(18, "steady_guard", 10);
    StatMiningMagician MINING_MAGICIAN = new StatMiningMagician(20, "mining_magician", 10);
    StatMaxHealth MAX_HEALTH = new StatMaxHealth(21, "health", 16);
    StatBase ROLL = new StatRoll(22, "roll", 10);

}
