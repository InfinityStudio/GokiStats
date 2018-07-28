package net.infstudio.goki.common.stats.tool;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

import java.util.Objects;

public class IDMDTuple {
    public String id;
    public int metadata;

    public IDMDTuple(Block block, int bMD) {
        this.id = block.getRegistryName().toString();
        this.metadata = bMD;
    }

    public IDMDTuple(Item item, int bMD) {
        this.id = item.getRegistryName().toString();
        this.metadata = bMD;
    }

    public boolean equals(Object object) {
        if ((object instanceof IDMDTuple)) {
            IDMDTuple entry = (IDMDTuple) object;
            if ((Objects.equals(entry.id, this.id)) && (entry.metadata == this.metadata)) {
                return true;
            }
        }
        return super.equals(object);
    }
}