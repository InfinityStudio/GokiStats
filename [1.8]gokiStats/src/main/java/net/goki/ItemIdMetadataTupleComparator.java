package net.goki;

import java.util.Comparator;

public class ItemIdMetadataTupleComparator implements Comparator<ItemIdMetadataTuple>
{
	@Override
	public int compare(ItemIdMetadataTuple o1, ItemIdMetadataTuple o2)
	{
		if ((o1.id == o2.id) && (o1.id == o2.id))
		{
			return 1;
		}
		return 0;
	}
}