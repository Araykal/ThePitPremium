package cn.charlotte.pit.enchantment.type.rage


import cn.charlotte.pit.ThePit
import cn.charlotte.pit.enchantment.AbstractEnchantment
import cn.charlotte.pit.enchantment.param.item.ArmorOnly
import cn.charlotte.pit.enchantment.rarity.EnchantmentRarity
import cn.charlotte.pit.parm.AutoRegister
import cn.charlotte.pit.util.PlayerUtil
import cn.charlotte.pit.util.cooldown.Cooldown
import cn.charlotte.pit.util.item.ItemUtil
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.entity.EntityDamageByEntityEvent

@AutoRegister
@ArmorOnly
class AceOfSpades : AbstractEnchantment() {
    override fun getEnchantName(): String {
        return "战斗铲之王"
    }

    override fun getMaxEnchantLevel(): Int {
        return 3
    }

    override fun getNbtName(): String {
        return "ace_of_spades"
    }

    override fun getRarity(): EnchantmentRarity {
        return EnchantmentRarity.RAGE
    }

    override fun getCooldown(): Cooldown? {
        return null
    }

    override fun getUsefulnessLore(enchantLevel: Int): String {
        return "&7来自 &e战斗之铲 &7伤害减少 &9${
            when (enchantLevel) {
                1 -> 0.5
                2 -> 1.0
                else -> 1.5
            }
        }&c❤"
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    fun onDamage(event: EntityDamageByEntityEvent) {
        val damager = event.damager
        val victim = event.entity
        if (damager is Player && victim is Player) {
            val item = damager.itemInHand
            if ("combat_spade" == ItemUtil.getInternalName(item)) {
                val leggings = victim.inventory.leggings
                val enchantLevel = ThePit.api.getItemEnchantLevel(leggings, nbtName)
                if (enchantLevel > 0) {
                    if (PlayerUtil.shouldIgnoreEnchant(victim, damager)) {
                        return
                    }
                    val reduceValue = when (enchantLevel) {
                        1 -> 0.5
                        2 -> 1.0
                        else -> 1.5
                    } * 2

                    event.damage -= reduceValue
                }
            }
        }
    }
}