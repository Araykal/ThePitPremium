package cn.charlotte.pit.enchantment.type.normal

import cn.charlotte.pit.enchantment.AbstractEnchantment
import cn.charlotte.pit.enchantment.IActionDisplayEnchant
import cn.charlotte.pit.enchantment.param.item.ArmorOnly
import cn.charlotte.pit.enchantment.rarity.EnchantmentRarity
import cn.charlotte.pit.parm.listener.IPlayerDamaged
import cn.charlotte.pit.util.chat.RomanUtil
import cn.charlotte.pit.util.cooldown.Cooldown
import com.google.common.util.concurrent.AtomicDouble
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean

@ArmorOnly
class HappyNewYearEnchant : AbstractEnchantment(), IPlayerDamaged, IActionDisplayEnchant {
    private val cooldown = HashMap<UUID, Cooldown>()

    override fun getEnchantName(): String {
        return "新年快乐"
    }

    override fun getMaxEnchantLevel(): Int {
        return 3
    }

    override fun getNbtName(): String {
        return "happy_new_year"
    }

    override fun getRarity(): EnchantmentRarity {
        return EnchantmentRarity.NORMAL
    }

    override fun getCooldown(): Cooldown? {
        return null
    }

    override fun getUsefulnessLore(enchantLevel: Int): String {
        return "&7受到攻击时获得 &f生命恢复 ${RomanUtil.convert(getHeal(enchantLevel).first + 1)} &c" + "${getHeal(enchantLevel).second}秒&7 (1.5秒冷却)"
    }

    private fun getHeal(enchantLevel: Int): Pair<Int, Int> {
        return when (enchantLevel) {
            1 -> 0 to 4
            2 -> 0 to 8
            3 -> 1 to 5
            else -> 0 to 0
        }
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
        cooldown.putIfAbsent(myself.uniqueId, Cooldown(0))
        if (cooldown[myself.uniqueId]!!.hasExpired()) {
            cooldown[myself.uniqueId] = Cooldown(1.5.toLong(), TimeUnit.SECONDS)
            val (level, duration) = getHeal(enchantLevel)
            myself.addPotionEffect(
                PotionEffect(
                    PotionEffectType.REGENERATION,
                    duration * 20,
                    level,
                    true
                )
            )
        }
    }

    override fun getText(level: Int, player: Player): String {
        return getCooldownActionText(cooldown.getOrDefault(player.uniqueId, Cooldown(0)))
    }
}