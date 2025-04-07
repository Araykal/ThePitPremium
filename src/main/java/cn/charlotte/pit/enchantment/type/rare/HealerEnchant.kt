package cn.charlotte.pit.enchantment.type.rare


import cn.charlotte.pit.enchantment.AbstractEnchantment
import cn.charlotte.pit.enchantment.IActionDisplayEnchant
import cn.charlotte.pit.enchantment.param.item.WeaponOnly
import cn.charlotte.pit.enchantment.rarity.EnchantmentRarity
import cn.charlotte.pit.parm.listener.IAttackEntity
import cn.charlotte.pit.util.PlayerUtil
import cn.charlotte.pit.util.cooldown.Cooldown
import com.google.common.util.concurrent.AtomicDouble
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean


@WeaponOnly
class HealerEnchant : AbstractEnchantment(), IAttackEntity, IActionDisplayEnchant {
    override fun getEnchantName(): String {
        return "医师"
    }

    override fun getMaxEnchantLevel(): Int {
        return 3
    }

    override fun getNbtName(): String {
        return "healer_enchant"
    }

    override fun getRarity(): EnchantmentRarity {
        return EnchantmentRarity.RARE
    }

    override fun getCooldown(): Cooldown? {
        return null
    }

    fun getHealAmount(enchantLevel: Int): Int {
        return when (enchantLevel) {
            1 -> 2
            2 -> 4
            3 -> 5
            else -> 0
        }
    }

    override fun getUsefulnessLore(enchantLevel: Int): String {
        return "&7攻击命中玩家时恢复自身 &c" + (0.25 + 0.25 * enchantLevel) + "❤ &7并恢复对方 &c" + getHealAmount(
            enchantLevel
        ) + "❤ &7(1秒冷却)"
    }

    override fun getText(level: Int, player: Player): String {
        return getCooldownActionText(Companion.cooldown.getOrDefault(player.uniqueId, Cooldown(0)))
    }

    override fun handleAttackEntity(
        enchantLevel: Int,
        attacker: Player,
        target: Entity,
        damage: Double,
        finalDamage: AtomicDouble,
        boostDamage: AtomicDouble,
        cancel: AtomicBoolean
    ) {
        if (target is Player && Companion.cooldown.getOrDefault(attacker.uniqueId, Cooldown(0)).hasExpired()) {
            Companion.cooldown[attacker.uniqueId] = Cooldown(1, TimeUnit.SECONDS)
            PlayerUtil.heal(attacker, 0.5 + 0.5 * enchantLevel)
            PlayerUtil.heal(target, (2 * getHealAmount(enchantLevel)).toDouble())
        }
    }

    companion object {
        val cooldown = HashMap<UUID, Cooldown>()
    }
}
