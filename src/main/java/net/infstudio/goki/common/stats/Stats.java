package net.infstudio.goki.common.stats;

import net.infstudio.goki.common.stats.damage.*;
import net.infstudio.goki.common.stats.movement.StatClimbing;
import net.infstudio.goki.common.stats.movement.StatSteadyGuard;
import net.infstudio.goki.common.stats.movement.StatSwimming;
import net.infstudio.goki.common.stats.special.StatFurnaceFinesse;
import net.infstudio.goki.common.stats.special.leaper.StatLeaperH;
import net.infstudio.goki.common.stats.special.leaper.StatLeaperV;
import net.infstudio.goki.common.stats.special.leaper.StatStealth;
import net.infstudio.goki.common.stats.tool.*;

public interface Stats {
    ToolSpecificStat MINING = new StatMining(0, "grpg_Mining", 10);
    ToolSpecificStat DIGGING = new StatDigging(1, "grpg_Digging", 10);
    ToolSpecificStat CHOPPING = new StatChopping(2, "grpg_Chopping", 10);
    ToolSpecificStat TRIMMING = new StatTrimming(3, "grpg_Trimming", 10);
    DamageSourceProtectionStat PROTECTION = new StatProtection(4, "grpg_Protection", 10);
    DamageSourceProtectionStat TEMPERING = new StatTempering(5, "grpg_Tempering", 10);
    DamageSourceProtectionStat TOUGH_SKIN = new StatToughSkin(6, "grpg_ToughSkin", 10);
    StatBase MAX_HEALTH = new StatMaxHealth(21, "grpg_Health", 10);
    DamageSourceProtectionStat STAT_FEATHER_FALL = new StatFeatherFall(7, "grpg_FeatherFall", 10);
    StatBase LEAPER_H = new StatLeaperH(8, "grpg_LeaperH", 10);
    StatBase LEAPER_V = new StatLeaperV(9, "grpg_LeaperV", 10);
    StatBase SWIMMING = new StatSwimming(10, "grpg_Swimming", 10);
    StatBase CLIMBING = new StatClimbing(11, "grpg_Climbing", 10);
    StatBase PUGILISM = new StatPugilism(12, "grpg_Pugilism", 10);
    ToolSpecificStat SWORDSMANSHIP = new StatSwordsmanship(13, "grpg_Swordsmanship", 10);
    ToolSpecificStat BOWMANSHIP = new StatBowmanship(14, "grpg_Bowmanship", 10);
    StatBase REAPER = new StatReaper(15, "grpg_Reaper", 10);
    StatBase FURNACE_FINESSE = new StatFurnaceFinesse(17, "grpg_Furnace_Finesse", 10);
    StatTreasureFinder TREASURE_FINDER = new StatTreasureFinder(16, "grpg_Treasure_Finder", 3);
    StatBase STEALTH = new StatStealth(19, "grpg_Stealth", 10);
    StatBase STEADY_GUARD = new StatSteadyGuard(18, "grpg_Steady_Guard", 10);
    StatMiningMagician MINING_MAGICIAN = new StatMiningMagician(20, "grpg_Mining_Magician", 10);
    StatBase ROLL = new StatRoll(21, "grpg_Roll", 10);

}
