package cn.charlotte.pit.api

import org.bukkit.entity.Player

interface PointsAPI {

    fun hasPoints(player: Player, points: Int): Boolean

    fun getPoints(player: Player): Int

    fun costPoints(player: Player, points: Int)

}