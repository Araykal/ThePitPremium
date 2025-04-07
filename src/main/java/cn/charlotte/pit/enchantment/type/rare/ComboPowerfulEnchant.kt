package cn.charlotte.pit.enchantment.type.rare

import cn.charlotte.pit.data.PlayerProfile
import cn.charlotte.pit.enchantment.AbstractEnchantment
import cn.charlotte.pit.enchantment.param.item.ArmorOnly
import cn.charlotte.pit.enchantment.rarity.EnchantmentRarity
import cn.charlotte.pit.parm.listener.IAttackEntity
import cn.charlotte.pit.util.cooldown.Cooldown
import com.google.common.util.concurrent.AtomicDouble
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import java.util.concurrent.atomic.AtomicBoolean

@ArmorOnly
class ComboPowerfulEnchant: AbstractEnchantment(), IAttackEntity {
    override fun getEnchantName(): String {
        return "强力击: 蓄力"
    }

    override fun getMaxEnchantLevel(): Int {
        return 3
    }

    override fun getNbtName(): String {
        return "combo_powerful"
    }

    override fun getRarity(): EnchantmentRarity {
        return EnchantmentRarity.RARE
    }

    override fun getCooldown(): Cooldown? {
        return null
    }

    override fun getUsefulnessLore(enchantLevel: Int): String {
        return "&7在&a 不在战斗&7 状态中持续15秒未受到伤害/s" +
                "&7则你下次攻击伤害额外增加&c +${
                    when(enchantLevel) {
                        2 -> "95"
                        3 -> "110"
                        else -> "75"
                    }
                }%/s/s" +
                "&7在&c 战斗中&7 状态持续超过25秒未收到伤害, /s" +
                "则你下次攻击变为&f 真实伤害"
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
        val profile = PlayerProfile.getPlayerProfileByUuid(attacker.uniqueId)
        val inFight = profile.combatTimer.hasExpired()

        val now = System.currentTimeMillis()
        if (inFight) {
            val lastDamageAt = profile.lastDamageAt

            if (lastDamageAt == -1L || now - lastDamageAt >= 25 * 1000L) {
                val boost = boostDamage.getAndSet(0.0)
                finalDamage.addAndGet(damage * boost)
            }
        } else {
            val lastDamageAt = profile.lastDamageAt

            if (lastDamageAt == -1L || now - lastDamageAt >= 15 * 1000L) {
                val boost = boostDamage.getAndSet(0.0)
                finalDamage.addAndGet(damage * boost)
            }
        }
    }
}