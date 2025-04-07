package cn.charlotte.pit.enchantment.type

import cn.charlotte.pit.enchantment.AbstractEnchantment
import cn.charlotte.pit.enchantment.rarity.EnchantmentRarity
import cn.charlotte.pit.parm.listener.IAttackEntity
import cn.charlotte.pit.parm.listener.IPlayerAssist
import cn.charlotte.pit.parm.listener.IPlayerBeKilledByEntity
import cn.charlotte.pit.parm.listener.IPlayerDamaged
import cn.charlotte.pit.parm.listener.IPlayerKilledEntity
import cn.charlotte.pit.parm.listener.IPlayerShootEntity
import cn.charlotte.pit.util.cooldown.Cooldown
import com.google.common.util.concurrent.AtomicDouble
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import java.util.concurrent.atomic.AtomicBoolean
import javax.script.ScriptEngine

class JSEnchantment(
    val engine: ScriptEngine,
    val enchantName0: String,
    val internalName0: String,
    val rarity0: EnchantmentRarity,
    private val desc: List<String>,
    allowedItems: Set<String>,
    private val attackFunc: ((Int, Player, Entity, Double, AtomicDouble, AtomicDouble, AtomicBoolean) -> Unit)?,
    private val playerKilledFunc: ((Int, Player, Entity, AtomicDouble, AtomicDouble) -> Unit)?,
    private val shootFunc: ((Int, Player, Entity, Double, AtomicDouble, AtomicDouble, AtomicBoolean) -> Unit)?,
    private val playerAssistFunc: ((Int, Player, Entity, Double, Double, AtomicDouble, AtomicDouble) -> Unit)?,
    private val playerBeKilledFunc: ((Int, Player, Entity, AtomicDouble, AtomicDouble) -> Unit)?,
    private val playerDamagedFunc: ((Int, Player, Entity, Double, AtomicDouble, AtomicDouble, AtomicBoolean) -> Unit)?
): AbstractEnchantment(), IAttackEntity, IPlayerDamaged, IPlayerKilledEntity, IPlayerShootEntity, IPlayerAssist, IPlayerBeKilledByEntity  {

    init {
        if (allowedItems.contains("WEAPON")) {
            weaponOnly = true
        }
        if (allowedItems.contains("ARMOR")) {
            armorOnly = true
        }
        if (allowedItems.contains("BOW")) {
            bowOnly = true
        }
        if (allowedItems.contains("ROD")) {
            rodOnly = true
        }
    }

    override fun getEnchantName(): String {
        return enchantName0
    }

    override fun getMaxEnchantLevel(): Int {
        return 3
    }

    override fun getNbtName(): String {
        return internalName0
    }

    override fun getRarity(): EnchantmentRarity {
        return rarity0
    }

    override fun getCooldown(): Cooldown? {
        return null
    }

    override fun getUsefulnessLore(enchantLevel: Int): String {
        return desc.joinToString("/s")
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
        attackFunc?.invoke(enchantLevel, attacker, target, damage, finalDamage, boostDamage, cancel)
    }


    override fun handlePlayerKilled(
        enchantLevel: Int,
        myself: Player,
        target: Entity,
        coins: AtomicDouble,
        experience: AtomicDouble
    ) {
        playerKilledFunc?.invoke(enchantLevel, myself, target, coins, experience)
    }

    override fun handleShootEntity(
        enchantLevel: Int,
        attacker: Player,
        target: Entity,
        damage: Double,
        finalDamage: AtomicDouble,
        boostDamage: AtomicDouble,
        cancel: AtomicBoolean
    ) {
        shootFunc?.invoke(enchantLevel, attacker, target, damage, finalDamage, boostDamage, cancel)
    }

    override fun handlePlayerAssist(
        enchantLevel: Int,
        myself: Player,
        target: Entity,
        damage: Double,
        finalDamage: Double,
        coins: AtomicDouble,
        experience: AtomicDouble
    ) {
        playerAssistFunc?.invoke(enchantLevel, myself, target, damage, finalDamage, coins, experience)
    }

    override fun handlePlayerBeKilledByEntity(
        enchantLevel: Int,
        myself: Player,
        target: Entity,
        coins: AtomicDouble,
        experience: AtomicDouble
    ) {
        playerBeKilledFunc?.invoke(enchantLevel, myself, target, coins, experience)
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
        playerDamagedFunc?.invoke(enchantLevel, myself, attacker, damage, finalDamage, boostDamage, cancel)
    }
}