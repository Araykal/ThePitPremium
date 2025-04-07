package cn.charlotte.pit.perk.type.prestige

import cn.charlotte.pit.perk.AbstractPerk
import cn.charlotte.pit.perk.PerkType
import org.bukkit.Material
import org.bukkit.entity.Player

class ToTheMoonBundle : AbstractPerk() {
    override fun getInternalPerkName(): String {
        return "to_the_moon_bundle"
    }

    override fun getDisplayName(): String {
        return "连杀天赋组合包: 月球之旅"
    }

    override fun getIcon(): Material {
        return Material.ENDER_STONE
    }

    override fun requireCoins(): Double {
        return 0.0
    }

    override fun requireRenown(level: Int): Double {
        return 150.0
    }

    override fun requirePrestige(): Int {
        return 14
    }

    override fun requireLevel(): Int {
        return 0
    }

    override fun getDescription(player: Player?): List<String> {
        val lines: MutableList<String> = ArrayList()
        lines.add("&7包括以下连杀天赋:")
        lines.add("&8- &9超级连杀者")
        lines.add("&8- &9黄金助推器")
        lines.add("&8- &9经验助推器")
        lines.add(" ")
        lines.add("&7超级连杀天赋: &b月球之旅")
        return lines
    }

    override fun getMaxLevel(): Int {
        return 0
    }

    override fun getPerkType(): PerkType {
        return PerkType.PERK
    }

    override fun onPerkActive(player: Player?) {}

    override fun onPerkInactive(player: Player?) {}
}