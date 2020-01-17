package net.infstudio.goki.api.stat

import java.util
import java.util.ArrayList

import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.client.resources.I18n
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.world.World
import net.minecraftforge.common.ForgeHooks

class StatBase extends Stat {
    val stats = new util.ArrayList[StatBase](32)
    var totalStats = 0
    //	 public static final StatBase STAT_FOCUS = new StatFocus(14, "grpg_Focus",
    //	 "Focus", 25);
    var imageID = 0
    var key: String = null
    var costMultiplier = 1.0F
    var limitMultiplier = 1.0F
    var bonusMultiplier = 1.0F
    var enabled = true
    var name: String = null
    val des: String = null
    private var limit = 0

    def this() {
        this()
        this.imageID = -1
        this.limit = 0
        this.key = "Dummy"
        this.name = "Dummy StatBase"
        stats.add(this)
    }

    def this(imgId: Int, key: String, limit: Int) {
        this()
        this.imageID = imgId
        this.limit = limit
        this.key = key
        stats.add(this)
        totalStats += 1
    }

    protected def getFinalBonus(currentBonus: Float): Float = currentBonus * GokiConfig.globalModifiers.globalBonusMultiplier

    override def getKey: String = key

    override def getBonus(player: EntityPlayer): Float = getBonus(DataHelper.getPlayerStatLevel(player, this)) * bonusMultiplier

    override def getAppliedDescriptionVar(player: EntityPlayer): Array[Float] = Array[Float](DataHelper.trimDecimals(getBonus(player) * 100, 1))

    override def getCost(level: Int): Int = ((Math.pow(level, 1.6D) + 6.0D + level) * GokiConfig.globalModifiers.globalCostMultiplier).toInt

    override def needAffectedByStat(obj: Any*): Boolean = {
        if (obj(1).isInstanceOf[ItemStack] && obj(2).isInstanceOf[BlockPos] && obj(3).isInstanceOf[World]) {
            val stack = obj(1).asInstanceOf[ItemStack]
            val pos = obj(2).asInstanceOf[BlockPos]
            val world = obj(3).asInstanceOf[World]
            return ForgeHooks.isToolEffective(world, pos, stack)
        }
        false
    }

    override def getLimit: Int = {
        if (GokiConfig.globalModifiers.globalLimitMultiplier <= 0.0F) return 127
        (this.limit * GokiConfig.globalModifiers.globalLimitMultiplier).toInt
    }

    override def getAppliedBonus(player: EntityPlayer, `object`: Any): Float = if (needAffectedByStat(`object`)) getBonus(player)
    else 0

    protected def getPlayerStatLevel(player: EntityPlayer): Int = DataHelper.getPlayerStatLevel(player, this)

    def needAffectedByStat(object1: Any, object2: Any, object3: Any): Boolean = {
        if (object1.isInstanceOf[ItemStack] && object2.isInstanceOf[BlockPos] && object3.isInstanceOf[World]) {
            val stack = object1.asInstanceOf[ItemStack]
            val pos = object2.asInstanceOf[BlockPos]
            val world = object3.asInstanceOf[World]
            if (ForgeHooks.isToolEffective(world, pos, stack)) return true
        }
        needAffectedByStat(object1)
    }

    @SideOnly(Side.CLIENT) def getLocalizedName: String = I18n.format(this.key + ".name")

    @SideOnly(Side.CLIENT) def getLocalizedDes(player: EntityPlayer): String = I18n.format(this.key + ".des", this.getAppliedDescriptionVar(player)(0))
}
