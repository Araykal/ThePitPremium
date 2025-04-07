package cn.charlotte.pit.enchantment.type.rage

import cn.charlotte.pit.buff.impl.HealPoisonDeBuff
import cn.charlotte.pit.enchantment.AbstractEnchantment
import cn.charlotte.pit.enchantment.param.event.PlayerOnly
import cn.charlotte.pit.enchantment.param.item.ArmorOnly
import cn.charlotte.pit.enchantment.rarity.EnchantmentRarity
import cn.charlotte.pit.parm.AutoRegister
import cn.charlotte.pit.parm.listener.IAttackEntity
import cn.charlotte.pit.parm.listener.IPlayerShootEntity
import cn.charlotte.pit.util.cooldown.Cooldown
import com.google.common.util.concurrent.AtomicDouble
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import java.util.concurrent.atomic.AtomicBoolean


/**
 * @Creator Misoryan
 * @Date 2021/5/8 19:39
 */
@ArmorOnly
@AutoRegister
class ReallyToxicEnchant : AbstractEnchantment(), IAttackEntity, IPlayerShootEntity, Listener {
    override fun getEnchantName(): String {
        return "不愈"
    }

    override fun getMaxEnchantLevel(): Int {
        return 3
    }

    override fun getNbtName(): String {
        return "really_toxic"
    }

    override fun getRarity(): EnchantmentRarity {
        return EnchantmentRarity.RAGE
    }

    override fun getCooldown(): Cooldown? {
        return null
    }

    override fun getUsefulnessLore(enchantLevel: Int): String {
        return ("&7攻击为目标玩家施加 &e2 &7层 &a不愈之毒 &f(00:30) &7,最高叠加" + (enchantLevel * 10 + 10) + "层."
                + "/s&7效果 &a不愈之毒&7: 降低恢复生命值时的生命恢复量,每层降低 &a1%")
    }

    @PlayerOnly
    override fun handleAttackEntity(
        enchantLevel: Int,
        attacker: Player,
        target: Entity,
        damage: Double,
        finalDamage: AtomicDouble,
        boostDamage: AtomicDouble,
        cancel: AtomicBoolean
    ) {
        val targetPlayer = target as Player
        buff.stackBuff(targetPlayer, 30 * 1000L, 2)
    }

    @PlayerOnly
    override fun handleShootEntity(
        enchantLevel: Int,
        attacker: Player,
        target: Entity,
        damage: Double,
        finalDamage: AtomicDouble,
        boostDamage: AtomicDouble,
        cancel: AtomicBoolean
    ) {
        val targetPlayer = target as Player
        buff.stackBuff(targetPlayer, 30 * 1000L, 2)
    }

    companion object {
        val buff = HealPoisonDeBuff()
    }
}