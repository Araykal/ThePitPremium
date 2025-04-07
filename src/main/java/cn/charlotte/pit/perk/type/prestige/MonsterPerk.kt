package cn.charlotte.pit.perk.type.prestige

import cn.charlotte.pit.event.PitProfileLoadedEvent
import cn.charlotte.pit.event.PitStreakKillChangeEvent
import cn.charlotte.pit.getPitProfile
import cn.charlotte.pit.parm.AutoRegister
import cn.charlotte.pit.perk.AbstractPerk
import cn.charlotte.pit.perk.PerkType
import cn.charlotte.pit.util.PlayerUtil
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener

@AutoRegister
class MonsterPerk : AbstractPerk(), Listener {
    override fun getInternalPerkName(): String {
        return "monster"
    }

    override fun getDisplayName(): String {
        return "怪物"
    }

    override fun getIcon(): Material {
        return Material.LEASH
    }

    override fun requireCoins(): Double {
        return 10000.0
    }

    override fun requireRenown(level: Int): Double {
        return 10.0
    }

    override fun requirePrestige(): Int {
        return 3
    }

    override fun requireLevel(): Int {
        return 40
    }

    override fun getDescription(player: Player): MutableList<String> {
        return mutableListOf(
            "&7此天赋每&c25&7连杀触发一次",
            "",
            "&7触发时: ",
            "&7  提升血量上限&c1❤&7(最高提升2次)"
        )
    }

    override fun getMaxLevel(): Int {
        return 1
    }

    override fun getPerkType(): PerkType {
        return PerkType.KILL_STREAK
    }

    override fun onPerkActive(player: Player) {

    }

    override fun onPerkInactive(player: Player) {
        val profile = player.getPitProfile()
        profile.extraMaxHealth.remove("monster")
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun onSteak(event: PitStreakKillChangeEvent) {
        val profile = event.playerProfile
        val player = Bukkit.getPlayer(profile.playerUuid) ?: return

        if (!PlayerUtil.isPlayerChosePerk(player, internalPerkName)) {
            return
        }

        if (event.from.toInt() != 25 && event.to.toInt() == 25) {
            profile.extraMaxHealth["monster"] = 2.0
        } else if (event.from.toInt() != 50 && event.to.toInt() == 50) {
            profile.extraMaxHealth["monster"] = 4.0
        }

        if (event.to.toInt() < 25) {
            profile.extraMaxHealth.remove("monster")
        }

        player.maxHealth = profile.maxHealth
    }

    @EventHandler
    fun onProfileLoaded(event: PitProfileLoadedEvent) {
        val profile = event.playerProfile
        val player = Bukkit.getPlayer(profile.playerUuid) ?: return
        player.maxHealth = 20.0
    }
}