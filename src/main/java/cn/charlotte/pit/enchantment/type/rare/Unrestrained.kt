package cn.charlotte.pit.enchantment.type.rare

import cn.charlotte.pit.enchantment.AbstractEnchantment
import cn.charlotte.pit.enchantment.param.item.ArmorOnly
import cn.charlotte.pit.enchantment.rarity.EnchantmentRarity
import cn.charlotte.pit.parm.listener.IAttackEntity
import cn.charlotte.pit.parm.listener.IPlayerBeKilledByEntity
import cn.charlotte.pit.util.cooldown.Cooldown
import com.google.common.util.concurrent.AtomicDouble
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.math.min

@ArmorOnly
class Unrestrained: AbstractEnchantment(), IPlayerBeKilledByEntity, IAttackEntity {
    override fun getEnchantName(): String {
        return "无拘无束"
    }

    override fun getMaxEnchantLevel(): Int {
        return 3
    }

    override fun getNbtName(): String {
        return "unrestrained"
    }

    override fun getRarity(): EnchantmentRarity {
        return EnchantmentRarity.RARE
    }

    override fun getCooldown(): Cooldown? {
        return null
    }

    override fun getUsefulnessLore(enchantLevel: Int): String {
        return "&7当你被某名玩家杀死时，你下次复活/s" +
                "时对该玩家伤害额外增加&c24%&7(最高叠加至6层)"
    }

    val map = HashMap<UUID, AttackData>()

    override fun handleAttackEntity(
        enchantLevel: Int,
        attacker: Player,
        target: Entity,
        damage: Double,
        finalDamage: AtomicDouble?,
        boostDamage: AtomicDouble,
        cancel: AtomicBoolean?
    ) {
        val attackData = map[target.uniqueId] ?: return
        boostDamage.addAndGet(attackData.amount * 0.24)
    }

    override fun handlePlayerBeKilledByEntity(
        enchantLevel: Int,
        myself: Player,
        target: Entity,
        coins: AtomicDouble?,
        experience: AtomicDouble?
    ) {
        val data = map[target.uniqueId] ?: kotlin.run {
            AttackData(target.uniqueId).also {
                map[target.uniqueId] = it
            }
        }

        data.amount = min(6, data.amount + 1)
    }

    class AttackData(val uuid: UUID) {
        var amount = 0

    }

}