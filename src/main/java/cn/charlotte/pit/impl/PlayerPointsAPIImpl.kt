package cn.charlotte.pit.impl

import cn.charlotte.pit.api.PointsAPI
import org.black_ixx.playerpoints.PlayerPoints
import org.bukkit.Bukkit
import org.bukkit.entity.Player

object PlayerPointsAPIImpl: PointsAPI {
    private val pointsAPI by lazy {
        (Bukkit.getPluginManager().getPlugin("PlayerPoints") as PlayerPoints).api
    }

    override fun hasPoints(player: Player, points: Int): Boolean {
        return pointsAPI.look(player.uniqueId) >= points
    }

    override fun getPoints(player: Player): Int {
        return pointsAPI.look(player.uniqueId)
    }

    override fun costPoints(player: Player, points: Int) {
        pointsAPI.take(player.uniqueId, points)
    }
}