package cn.charlotte.pit.util

import net.minecraft.server.v1_8_R3.EntityFireworks
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityStatus
import net.minecraft.server.v1_8_R3.World
import org.bukkit.Bukkit
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer

class BoomFirework(world: World) :
    EntityFireworks(world) {

    val players = Bukkit.getOnlinePlayers()
    var gone = false
    override fun t_() {
        if (gone) {
            die()
            return
        }
        if (!world.isClientSide) {
            gone = true
            if (players.isNotEmpty()) {
                for (player in players) {
                    (player as CraftPlayer).handle.playerConnection.sendPacket(
                        PacketPlayOutEntityStatus(this, 17.toByte())
                    )
                }
            } else {
                world.broadcastEntityEffect(this, 17.toByte())
                die()
            }
        }
    }
}