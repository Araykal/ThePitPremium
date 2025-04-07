package cn.charlotte.pit.enchantment.type.rage

import cn.charlotte.pit.enchantment.AbstractEnchantment
import cn.charlotte.pit.enchantment.param.item.ArmorOnly
import cn.charlotte.pit.enchantment.rarity.EnchantmentRarity
import cn.charlotte.pit.item.type.mythic.MythicSwordItem
import cn.charlotte.pit.parm.listener.IPlayerDamaged
import cn.charlotte.pit.util.cooldown.Cooldown
import cn.charlotte.pit.util.item.ItemUtil
import com.google.common.util.concurrent.AtomicDouble
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import java.util.concurrent.atomic.AtomicBoolean


/**
 * @Creator Misoryan
 * @Date 2021/5/8 14:06
 */
@ArmorOnly
class NewDealEnchant : AbstractEnchantment(), IPlayerDamaged {
    override fun getEnchantName(): String {
        return "新的交易"
    }

    override fun getMaxEnchantLevel(): Int {
        return 3
    }

    override fun getNbtName(): String {
        return "new_deal"
    }

    override fun getRarity(): EnchantmentRarity {
        return EnchantmentRarity.RAGE
    }

    override fun getCooldown(): Cooldown? {
        return null
    }

    override fun getUsefulnessLore(enchantLevel: Int): String {
        return "&7受到的伤害 &9-" + (enchantLevel * 2 + 2) + "% &7且自身免疫附魔 &6亿万富翁 &7的效果"
    }

    override fun handlePlayerDamaged(
        enchantLevel: Int,
        myself: Player,
        attacker: Entity,
        damage: Double,
        finalDamage: AtomicDouble,
        boostDamage: AtomicDouble,
        cancel: AtomicBoolean
    ) {
        if (attacker is Player) {
            val itemInHand = attacker.inventory.itemInHand
            if ("mythic_sword" == ItemUtil.getInternalName(itemInHand)) {
                val item = MythicSwordItem()
                item.loadFromItemStack(itemInHand)
                item.enchantments.entries
                    .stream()
                    .filter { (key): Map.Entry<AbstractEnchantment, Int?> -> key.nbtName == "billionaire" }
                    .findFirst()
                    .ifPresent { (_, value): Map.Entry<AbstractEnchantment?, Int> ->
                        boostDamage.set(
                            boostDamage.get() / (1 + 35 * value * 0.01)
                        )
                    }
            }
        }
        boostDamage.getAndAdd(enchantLevel * -0.02 - 0.02)
    }
}