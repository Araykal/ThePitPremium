package cn.charlotte.pit.enchantment.type.aqua

import cn.charlotte.pit.enchantment.AbstractEnchantment
import cn.charlotte.pit.enchantment.param.event.PlayerOnly
import cn.charlotte.pit.enchantment.param.item.ArmorOnly
import cn.charlotte.pit.enchantment.rarity.EnchantmentRarity
import cn.charlotte.pit.item.MythicColor
import cn.charlotte.pit.parm.listener.IAttackEntity
import cn.charlotte.pit.parm.listener.IPlayerDamaged
import cn.charlotte.pit.util.cooldown.Cooldown
import cn.charlotte.pit.util.toMythicItem
import com.google.common.util.concurrent.AtomicDouble
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import java.util.concurrent.atomic.AtomicBoolean

@ArmorOnly
class RogueEnchant: AbstractEnchantment(), IAttackEntity, IPlayerDamaged{
    override fun getEnchantName(): String {
        return "无赖"
    }

    override fun getMaxEnchantLevel(): Int {
        return 1
    }

    override fun getNbtName(): String {
        return "rogue"
    }

    override fun getRarity(): EnchantmentRarity {
        return EnchantmentRarity.FISH_NORMAL
    }

    override fun getCooldown(): Cooldown? {
        return null
    }

    override fun getUsefulnessLore(enchantLevel: Int): String {
        return "&7攻击穿着青色神话之甲的玩家时造成的伤害 &c+25% &7且自身受到其的伤害 &9-10%"
    }

    @PlayerOnly
    override fun handleAttackEntity(
        enchantLevel: Int,
        attacker: Player?,
        target: Entity,
        damage: Double,
        finalDamage: AtomicDouble?,
        boostDamage: AtomicDouble,
        cancel: AtomicBoolean?
    ) {
        if (target is Player) {
            if (target.inventory.leggings?.toMythicItem()?.color == MythicColor.AQUA) {
                boostDamage.addAndGet(0.25)
            }
        }
    }

    @PlayerOnly
    override fun handlePlayerDamaged(
        enchantLevel: Int,
        myself: Player,
        attacker: Entity?,
        damage: Double,
        finalDamage: AtomicDouble?,
        boostDamage: AtomicDouble,
        cancel: AtomicBoolean?
    ) {
        if (attacker is Player) {
            if (attacker.inventory.leggings?.toMythicItem()?.color == MythicColor.AQUA) {
                boostDamage.set(boostDamage.get() - 0.1)
            }
        }
    }
}