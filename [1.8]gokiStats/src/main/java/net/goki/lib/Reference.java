package net.goki.lib;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;

public class Reference
{
	public static Configuration configuration;
	public static final String MOD_ID = "gokiStats";
	public static final String MOD_NAME = "gokiStats";
	public static final String VERSION = "1.0.0";
	public static final String CONFIGURATION_VERSION = "v1";
	public static final ResourceLocation RPG_ICON_TEXTURE_LOCATION = new ResourceLocation("gokiStats".toLowerCase(), "textures/rpg_icons.png");
	public static final ResourceLocation RPG_ICON_2_TEXTURE_LOCATION = new ResourceLocation("gokiStats".toLowerCase(), "textures/rpg_icons_2.png");
	public static final ResourceLocation PARTICLES_TEXTURE = new ResourceLocation("gokiStats".toLowerCase(), "textures/particles.png");
	public static final String STAT_TAG = "gokiStats_Stats";
	public static boolean isPlayerAPILoaded = false;
}