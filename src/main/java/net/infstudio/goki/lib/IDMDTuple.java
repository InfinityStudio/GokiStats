package net.infstudio.goki.lib;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

public class IDMDTuple {
    public int id;
    public int md;

    public IDMDTuple(int bID, int bMD) {
        this.id = bID;
        this.md = bMD;
    }

    public IDMDTuple(Block block, int bMD) {
        this.id = Block.getIdFromBlock(block);
        this.md = bMD;
    }

    public IDMDTuple(Item item, int bMD) {
        this.id = Item.getIdFromItem(item);
        this.md = bMD;
    }

    public String toConfigurationString() {
        return this.id + "_" + this.md;
    }

    public boolean fromConfigurationString(String configString) {
        boolean successful = false;
        try {
            String[] values = configString.split("_");
            this.id = Integer.parseInt(values[0]);
            this.md = Integer.parseInt(values[1]);
        } catch (Exception e) {
            successful = false;
        }
        return successful;
    }

    public boolean equals(Object object) {
        if ((object instanceof IDMDTuple)) {
            IDMDTuple entry = (IDMDTuple) object;
            if ((entry.id == this.id) && (entry.md == this.md)) {
                return true;
            }
        }
        return super.equals(object);
    }
}