package cn.charlotte.pit.sound.impl

import cn.charlotte.pit.util.sound.AbstractPitSound
import org.bukkit.Sound
import org.bukkit.entity.Player

object StreakSound : AbstractPitSound() {
    override fun getMusicInternalName(): String {
        return "streak"
    }

    override fun onSoundTick(player: Player, tick: Int) {
        if (tick == 0) {
            player.playSound(player.location, Sound.ORB_PICKUP, 1f, 1.7f)
        } else if (tick == 2) {
            player.playSound(player.location, Sound.ORB_PICKUP, 1f, 1.8f)
        } else if (tick == 4) {
            player.playSound(player.location, Sound.ORB_PICKUP, 1f, 1.9f)
        } else if (tick == 6) {
            player.playSound(player.location, Sound.ORB_PICKUP, 1f, 2f)
        } else if (tick == 8) {
            player.playSound(player.location, Sound.ORB_PICKUP, 1f, 2.1f)
        } else if (tick > 10) {
            end(player)
        }
    }
}