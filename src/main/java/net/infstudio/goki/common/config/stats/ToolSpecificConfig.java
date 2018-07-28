package net.infstudio.goki.common.config.stats;

import net.infstudio.goki.common.stats.tool.ItemIdMetadataTuple;

import java.util.ArrayList;
import java.util.List;

public class ToolSpecificConfig extends StatConfig {
    public List<ItemIdMetadataTuple> supports = new ArrayList<>();
}
