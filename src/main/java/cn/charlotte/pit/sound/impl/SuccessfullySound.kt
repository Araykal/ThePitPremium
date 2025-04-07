package cn.charlotte.pit.sound.impl

import cn.charlotte.pit.util.sound.AbstractPitSound
import org.bukkit.Sound
import org.bukkit.entity.Player

object SuccessfullySound : AbstractPitSound() {
    override fun getMusicInternalName(): String {
        return "successfully"
    }

    override fun onSoundTick(player: Player, tick: Int) {
        if (tick == 0 || tick == 2) {
            player.playSound(player.location, Sound.NOTE_PIANO, 1f, 1f)
        } else if (tick == 4 || tick == 6) {
            player.playSound(player.location, Sound.NOTE_PIANO, 1f, 1.1f)
        } else if (tick == 8 || tick == 10) {
            player.playSound(player.location, Sound.NOTE_PIANO, 1f, 1.2f)
        } else if (tick > 10) {
            end(player)
        }
    }
}