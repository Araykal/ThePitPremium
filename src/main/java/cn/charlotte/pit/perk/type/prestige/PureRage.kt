package cn.charlotte.pit.perk.type.prestige

import cn.charlotte.pit.item.MythicColor
import cn.charlotte.pit.perk.AbstractPerk
import cn.charlotte.pit.perk.PerkType
import cn.charlotte.pit.util.item.ItemBuilder
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class PureRage : AbstractPerk() {


    override fun getInternalPerkName(): String {
        return "pure_rage"
    }

    override fun getDisplayName(): String {
        return "极度愤怒"
    }

    override fun getIcon(): Material {
        return Material.LEATHER_LEGGINGS
    }

    override fun getIconWithNameAndLore(
        name: String,
        lore: MutableList<String>,
        durability: Int,
        amount: Int
    ): ItemStack {
        return ItemBuilder(Material.LEATHER_LEGGINGS)
            .name(name)
            .lore(lore)
            .durability(durability)
            .amount(1)
            .setLetherColor(MythicColor.RAGE.leatherColor)
            .build();
    }

    override fun requireCoins(): Double {
        return 0.0
    }

    override fun requireRenown(level: Int): Double {
        return 40.0
    }

    override fun requirePrestige(): Int {
        return 13
    }

    override fun requireLevel(): Int {
        return 0
    }

    override fun getDescription(player: Player?): List<String> {
        val lines: MutableList<String> = ArrayList()
        lines.add("&7解锁合成&4 暴怒之甲&7.")
        lines.add("&7对抗特定附魔的好助手")
        return lines
    }

    override fun getMaxLevel(): Int {
        return 1
    }

    override fun getPerkType(): PerkType? {
        return PerkType.PERK
    }

    override fun onPerkActive(player: Player?) {}

    override fun onPerkInactive(player: Player?) {}

}