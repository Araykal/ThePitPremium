package cn.charlotte.pit.listener

import cn.charlotte.pit.config.NewConfiguration
import cn.charlotte.pit.getPitProfile
import cn.charlotte.pit.util.chat.CC
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobDeathEvent
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

object MythicMobListener : Listener {

    @EventHandler
    fun onMMDead(event: MythicMobDeathEvent) {
        val mob = event.mob
        val beKilled = mob.entity.bukkitEntity

        if (beKilled !is LivingEntity) return

        NewConfiguration.mythicMobs[mob.type.internalName] ?: return

        val killer = event.killer
        if (killer !is Player) return
/*
        CombatListener.INSTANCE.handleKill(
            killer,
            killer.getPitProfile(),
            beKilled,
            PlayerProfile.NONE_PROFILE
        )*/
        val coinsRange = NewConfiguration.mythicMobs[mob.type.internalName]?.coinsRange ?: IntRange(0, 0)

        val coinsToAdd = coinsRange.random().toDouble()


        val expRange = NewConfiguration.mythicMobs[mob.type.internalName]?.expRange ?: IntRange(0, 0)

        val expToAdd = expRange.random().toDouble()

        val killMessage = NewConfiguration.mythicMobs[mob.type.internalName]?.killMessage
        killer.getPitProfile().coins += coinsToAdd
        killer.getPitProfile().experience += expToAdd
        if (killMessage != null) {
            killer.sendMessage(CC.translate(killMessage.replace("{mobName}", mob.type.displayName).replace("{exp}",expToAdd.toString()).replace("{coin}",coinsToAdd.toString())))
        }
    }

}