package cn.charlotte.pit.perk.type.streak.tothemoon


import cn.charlotte.pit.data.PlayerProfile
import cn.charlotte.pit.event.PitKillEvent
import cn.charlotte.pit.event.PitStreakKillChangeEvent
import cn.charlotte.pit.parm.AutoRegister
import cn.charlotte.pit.perk.AbstractPerk
import cn.charlotte.pit.perk.PerkType
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import kotlin.math.min

@AutoRegister
class GoldStack : AbstractPerk(), Listener {
    override fun getInternalPerkName(): String {
        return "gold_stack"
    }

    override fun getDisplayName(): String {
        return "黄金助推器"
    }

    override fun getIcon(): Material {
        return Material.GOLD_INGOT
    }

    override fun requireCoins(): Double {
        return 25000.0
    }

    override fun requireRenown(level: Int): Double {
        return .0
    }

    override fun requirePrestige(): Int {
        return 14
    }

    override fun requireLevel(): Int {
        return 90
    }

    override fun getDescription(player: Player?): MutableList<String> {
        return mutableListOf(
            "&7此天赋每 &c10 连杀 &7触发一次.",
            "",
            "&7触发时: ",
            "  &a▶ &7永久增加击杀&6 0.1金币 &7奖励, 直至下一次精通升级,",
            "  &7最高可堆叠至 &60.5金币&7, 每次精通将提升&6 0.1金币&7 上限",
            "  &6金币奖励&7 将会在任何时候生效"
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
    fun assist(event: PitKillEvent) {
        val profile = PlayerProfile.getPlayerProfileByUuid(event.killer.uniqueId) ?: return
        event.coins += profile.goldStackAddon
    }

    @EventHandler
    fun assist(event: PitStreakKillChangeEvent) {
        val profile = event.playerProfile
        if (event.to.toInt() % 10 == 0) {
            profile.goldStackAddon = min(profile.goldStackMax, profile.goldStackAddon + 0.1)
        }
    }

}