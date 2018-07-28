package net.infstudio.goki.common.config.stats;

import net.infstudio.goki.common.stats.tool.IDMDTuple;

import java.util.ArrayList;
import java.util.List;

public class MiningMagicianConfig extends StatConfig {
    public List<IDMDTuple> blockEntries = new ArrayList<>();
    public List<IDMDTuple> itemEntries = new ArrayList<>();
}
