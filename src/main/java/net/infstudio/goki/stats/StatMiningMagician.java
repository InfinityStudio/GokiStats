package net.infstudio.goki.stats;

import net.infstudio.goki.lib.DataHelper;
import net.infstudio.goki.lib.IDMDTuple;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;

import java.util.ArrayList;
import java.util.List;

public class StatMiningMagician extends Stat {
    public static List<IDMDTuple> blockEntries = new ArrayList<>();
    public static List<IDMDTuple> itemEntries = new ArrayList<>();
    private static IDMDTuple[] defaultBlockEntries =
            {new IDMDTuple(Blocks.COAL_ORE, 0), new IDMDTuple(Blocks.DIAMOND_ORE, 0), new IDMDTuple(Blocks.EMERALD_ORE, 0), new IDMDTuple(Blocks.GOLD_ORE, 0), new IDMDTuple(Blocks.IRON_ORE, 0), new IDMDTuple(Blocks.LAPIS_ORE, 0), new IDMDTuple(Blocks.QUARTZ_ORE, 0), new IDMDTuple(Blocks.REDSTONE_ORE, 0)};
    private static IDMDTuple[] defaultItemEntries =
            {new IDMDTuple(Items.COAL, 0), new IDMDTuple(Items.DIAMOND, 0), new IDMDTuple(Items.EMERALD, 0), new IDMDTuple(Items.GOLD_INGOT, 0), new IDMDTuple(Items.IRON_INGOT, 0), new IDMDTuple(Items.DYE, 4), new IDMDTuple(Items.QUARTZ, 0), new IDMDTuple(Items.REDSTONE, 0)};

    public StatMiningMagician(int id, String key, int limit) {
        super(id, key, limit);
        for (IDMDTuple mme : defaultBlockEntries) {
            blockEntries.add(mme);
        }
        for (IDMDTuple mme : defaultItemEntries) {
            itemEntries.add(mme);
        }
    }

    @Override
    public float getBonus(int level) {
        return getFinalBonus(level * 0.3F);
    }

    @Override
    public float[] getAppliedDescriptionVar(EntityPlayer player) {
        return new float[]
                {DataHelper.trimDecimals(getBonus(player), 1)};
    }

    @Override
    public boolean needAffectedByStat(Object... obj) {
        IDMDTuple idmd;
        if ((obj[0] instanceof IDMDTuple)) {
            idmd = (IDMDTuple) obj[0];
            for (IDMDTuple entry : blockEntries) {
                if (idmd.equals(entry)) {
                    return true;
                }
            }
            for (IDMDTuple entry : itemEntries) {
                if (idmd.equals(entry)) {
                    return true;
                }
            }
        }
        return false;
        // return super.needAffectedByStat(obj[0]);
    }
}