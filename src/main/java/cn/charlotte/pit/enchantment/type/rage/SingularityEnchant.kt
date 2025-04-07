package cn.charlotte.pit.enchantment.type.rage

import cn.charlotte.pit.ThePit
import cn.charlotte.pit.enchantment.AbstractEnchantment
import cn.charlotte.pit.enchantment.rarity.EnchantmentRarity
import cn.charlotte.pit.parm.AutoRegister
import cn.charlotte.pit.util.cooldown.Cooldown
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import kotlin.math.min

@AutoRegister
class SingularityEnchant: AbstractEnchantment(), Listener {
    override fun getEnchantName(): String {
        return "奇异"
    }

    override fun getMaxEnchantLevel(): Int {
        return 3
    }

    override fun getNbtName(): String {
        return "singularity"
    }

    override fun getRarity(): EnchantmentRarity {
        return EnchantmentRarity.RARE
    }

    override fun getCooldown(): Cooldown? {
        return null
    }

    override fun getUsefulnessLore(enchantLevel: Int): String {
        return "&7单次最多受到&c ${
            when(enchantLevel) {
                1 -> 2.5
                2 -> 2
                else -> 1.5
            }
        }❤ &7伤害"
    }

    @EventHandler
    fun onDamage(event: EntityDamageByEntityEvent) {
        val victim = event.entity

        if (victim !is Player) return


        val level = ThePit.api.getItemEnchantLevel(victim.inventory.leggings, "singularity")
        if (level > 0) {
            val reduce = min(
                when (level) {
                    1 -> 2.5
                    2 -> 2.0
                    else -> 1.5
                } * 2.0,
                event.finalDamage
            ) / event.finalDamage

            event.damage = reduce * event.damage
        }
    }
}