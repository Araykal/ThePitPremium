package cn.charlotte.pit.perk.type.prestige

import cn.charlotte.pit.data.PlayerProfile
import cn.charlotte.pit.parm.AutoRegister
import cn.charlotte.pit.parm.listener.IAttackEntity
import cn.charlotte.pit.perk.AbstractPerk
import cn.charlotte.pit.perk.PerkType
import cn.charlotte.pit.util.PlayerUtil
import com.google.common.util.concurrent.AtomicDouble
import org.bukkit.Material
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.util.concurrent.atomic.AtomicBoolean

@AutoRegister
class KungFuKnowledgePerk : AbstractPerk(), Listener, IAttackEntity {

    override fun getInternalPerkName(): String {
        return "kung_fu_knowledge"
    }

    override fun getDisplayName(): String {
        return "功夫知识"
    }

    override fun getIcon(): Material {
        return Material.RAW_BEEF
    }

    override fun requireCoins(): Double {
        return 10000.0
    }

    override fun requireRenown(level: Int): Double {
        return 40.0
    }

    override fun requirePrestige(): Int {
        return 9
    }

    override fun requireLevel(): Int {
        return 100
    }

    override fun getPerkType(): PerkType {
        return PerkType.PERK
    }

    override fun getDescription(player: Player?): List<String> {
        return listOf(
            "&7近战将不会造成任何伤害除非不持有任何物品",
            "&7拳头将会高额伤害",
            "&7每4次攻击将会给予你 &b速度 II&7(0:05)"
        )
    }

    override fun getMaxLevel(): Int {
        return 1
    }

    override fun onPerkActive(player: Player?) {}

    override fun onPerkInactive(player: Player?) {}

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    fun onDamage(event: EntityDamageByEntityEvent) {
        val damager = event.damager
        if (damager !is Player) return
        if (!PlayerUtil.isPlayerChosePerk(damager, "kung_fu_knowledge")) {
            return
        }

        val hand = damager.itemInHand
        if (hand != null && hand.type != Material.AIR) {
            event.damage = 0.0
            return
        }

        event.damage =
            if (PlayerUtil.isCritical(damager)) {
                8.0
            } else {
                7.3
            }
    }

    override fun handleAttackEntity(
        enchantLevel: Int,
        attacker: Player,
        target: Entity,
        damage: Double,
        finalDamage: AtomicDouble,
        boostDamage: AtomicDouble,
        cancel: AtomicBoolean?
    ) {
        val profile = PlayerProfile.getPlayerProfileByUuid(attacker.uniqueId)
        if (profile.meleeHit % 4 == 0) {
            attacker.addPotionEffect(PotionEffect(PotionEffectType.SPEED, 20 * 5, 1), false)
        }
    }

}