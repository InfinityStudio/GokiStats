package net.infstudio.goki

import net.minecraft.util.ResourceLocation

object Reference {
    final val MODID: String = "gokistats"
    final val MOD_NAME = "GokiStats"
    final val RPG_ICON_TEXTURE_LOCATION = new ResourceLocation(MODID, "textures/rpg_icons.png")
    final val RPG_ICON_2_TEXTURE_LOCATION = new ResourceLocation(MODID, "textures/rpg_icons_2.png")
    final val PARTICLES_TEXTURE = new ResourceLocation(MODID, "textures/particles.png")
    final val STAT_TAG = s"${MODID}_Stats"
    var isPlayerAPILoaded = false
}
