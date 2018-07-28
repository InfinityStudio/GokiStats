package net.infstudio.goki.common.stats.tool;

import java.util.Comparator;
import java.util.Objects;

public class ItemIdMetadataTupleComparator implements Comparator<ItemIdMetadataTuple> {
    @Override
    public int compare(ItemIdMetadataTuple o1, ItemIdMetadataTuple o2) {
        if (Objects.equals(o1.id, o2.id) && (Objects.equals(o1.id, o2.id))) {
            return 1;
        }
        return 0;
    }
}