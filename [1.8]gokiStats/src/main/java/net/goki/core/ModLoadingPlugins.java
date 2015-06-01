package net.goki.core;

import java.util.Map;

import net.goki.core.ModClassTransformer;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

@IFMLLoadingPlugin.MCVersion("1.8")
@IFMLLoadingPlugin.TransformerExclusions({"net.goki.core.ModLoadingPlugins"})
@IFMLLoadingPlugin.Name("GokiStat Core")
public class ModLoadingPlugins implements IFMLLoadingPlugin
{

	@Override
	public String[] getASMTransformerClass() 
	{
		System.out.println("load core mod");
		return new String[] {ModClassTransformer.class.getName()};
	}

	@Override
	public String getModContainerClass()
	{
		return ModContainer.class.getName();
	}

	@Override
	public String getSetupClass() 
	{
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data) 
	{
		
	}

	@Override
	public String getAccessTransformerClass() 
	{

		return null;
	}
	
}
