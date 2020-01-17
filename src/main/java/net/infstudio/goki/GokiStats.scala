package net.infstudio.goki

import cpw.mods.fml.common.Mod.EventHandler
import cpw.mods.fml.common.event.{FMLConstructionEvent, FMLPreInitializationEvent}
import cpw.mods.fml.common.{Mod, SidedProxy}
import net.infstudio.goki.common.CommonProxy
import net.infstudio.goki.common.config.ConfigManager
import net.minecraftforge.common.config.Configuration

@Mod(modid = Reference.MODID, modLanguage = "scala")
object GokiStats {
    @SidedProxy(clientSide = "net.infstudio.goki.client.ClientProxy", serverSide = "net.infstudio.goki.common.CommonProxy")
    var proxy: CommonProxy = _

    var config: Configuration = _

    @EventHandler
    def construct(event: FMLConstructionEvent) : Unit = {
        ConfigManager.loadData(event.getASMHarvestedData)
    }

    @EventHandler
    def preInit(event: FMLPreInitializationEvent): Unit = {
        config = new Configuration(event.getSuggestedConfigurationFile)
        config.load()
        config.get("general", "configuration version", "")
        config.save()
    }
}
