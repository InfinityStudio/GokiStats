package net.goki;

public class ItemIdMetadataTuple
{
	public int id = 0;
	public int metadata = 0;

	public ItemIdMetadataTuple(int id, int meta)
	{
		this.id = id;
		this.metadata = meta;
	}

	public ItemIdMetadataTuple(String configString)
	{
		fromConfigString(configString);
	}

	public String toConfigString()
	{
		return this.id + ":" + this.metadata;
	}

	public void fromConfigString(String configString)
	{
		String[] configStringSplit = configString.split(":");
		try
		{
			this.id = Integer.parseInt(configStringSplit[0]);
			this.metadata = Integer.parseInt(configStringSplit[1]);
		}
		catch (Exception e)
		{
		}
	}
}