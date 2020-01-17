package net.infstudio.goki.api.stat

import net.minecraft.entity.player.EntityPlayer

trait Stat {

    def needAffectedByStat(obj: Any*): Boolean

    def getAppliedDescriptionVar(player: EntityPlayer): Array[Float]

    def getBonus(level: Int): Float

    def getBonus(player: EntityPlayer): Float

    def getAppliedBonus(player: EntityPlayer, paramObject: Any): Float

    def getCost(level: Int): Int

    def getLimit: Int

    def getKey: String
}
