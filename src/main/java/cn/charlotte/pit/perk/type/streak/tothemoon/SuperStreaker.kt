package cn.charlotte.pit.perk.type.streak.tothemoon

import cn.charlotte.pit.data.PlayerProfile
import cn.charlotte.pit.event.PitAssistEvent
import cn.charlotte.pit.parm.AutoRegister
import cn.charlotte.pit.parm.listener.IPlayerKilledEntity
import cn.charlotte.pit.perk.AbstractPerk
import cn.charlotte.pit.perk.PerkType
import cn.charlotte.pit.util.PlayerUtil
import com.google.common.util.concurrent.AtomicDouble
import org.bukkit.Material
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import kotlin.math.min

@AutoRegister
class SuperStreaker : AbstractPerk(), IPlayerKilledEntity, Listener {
    override fun getInternalPerkName(): String {
        return "super_streaker"
    }

    override fun getDisplayName(): String {
        return "超级连杀者"
    }

    override fun getIcon(): Material {
        return Material.FEATHER
    }

    override fun requireCoins(): Double {
        return 20000.0
    }

    override fun requireRenown(level: Int): Double {
        return .0
    }

    override fun requirePrestige(): Int {
        return 14
    }

    override fun requireLevel(): Int {
        return 80
    }

    override fun getDescription(player: Player?): MutableList<String> {
        return mutableListOf(
            "&7此天赋每 &c10 连杀 &7触发一次.",
            "",
            "&7触发时: ",
            " &a▶ &7增加 &b50经验 &7, 且累计增加 &b+5% &7经验(最高增加50%)"
        )
    }

    override fun getMaxLevel(): Int {
        return 1
    }

    override fun getPerkType(): PerkType {
        return PerkType.KILL_STREAK
    }

    override fun onPerkActive(player: Player?) {

    }

    override fun onPerkInactive(player: Player?) {

    }

    @EventHandler
    fun assist(event: PitAssistEvent) {
        val profile = PlayerProfile.getPlayerProfileByUuid(event.assist.uniqueId) ?: return
        if (PlayerUtil.isPlayerChosePerk(event.assist, "super_streaker")) {
            if (profile.streakKills >= 10) {
                val numbers = profile.streakKills / 10
                val add = min(numbers * 0.05, 0.5)
                event.exp = event.exp * (1.0 + add) + 50
            }
        }
    }

    override fun handlePlayerKilled(
        enchantLevel: Int,
        myself: Player,
        target: Entity?,
        coins: AtomicDouble,
        experience: AtomicDouble
    ) {
        val profile = PlayerProfile.getPlayerProfileByUuid(myself.uniqueId) ?: return
        if (profile.streakKills >= 10) {
            val numbers = profile.streakKills / 10
            val add = min(numbers * 0.05, 0.5)
            experience.getAndAdd(experience.get() * (1.0 + add) + 50)
        }
    }
}