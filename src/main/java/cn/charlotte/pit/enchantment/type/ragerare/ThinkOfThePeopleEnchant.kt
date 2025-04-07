package cn.charlotte.pit.enchantment.type.ragerare

import cn.charlotte.pit.enchantment.AbstractEnchantment
import cn.charlotte.pit.enchantment.param.item.ArmorOnly
import cn.charlotte.pit.enchantment.rarity.EnchantmentRarity
import cn.charlotte.pit.util.cooldown.Cooldown


/**
 * @Creator Misoryan
 * @Date 2021/5/8 14:12
 */
@ArmorOnly
class ThinkOfThePeopleEnchant : AbstractEnchantment() {
    override fun getEnchantName(): String {
        return "\"为人着想\""
    }

    override fun getMaxEnchantLevel(): Int {
        return 3
    }

    override fun getNbtName(): String {
        return "think_of_the_people"
    }

    override fun getRarity(): EnchantmentRarity {
        return EnchantmentRarity.RAGE_RARE
    }

    override fun getCooldown(): Cooldown? {
        return null
    }

    override fun getUsefulnessLore(enchantLevel: Int): String {
        return ("&7为周围 &e10 &7格内的所有玩家(包括自身)添加以下效果:"
                + "/s&7免疫附魔 &c处决 &7与 &b强力击:闪电 &7的效果"
                + if (enchantLevel > 1) ";/s&7受到来自以上附魔使用者的伤害 &9-" + (enchantLevel * 10 - 10) + "%" else "")
    }
}