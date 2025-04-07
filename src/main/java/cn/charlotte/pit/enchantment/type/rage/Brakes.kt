package cn.charlotte.pit.enchantment.type.rage

import cn.charlotte.pit.enchantment.AbstractEnchantment
import cn.charlotte.pit.enchantment.rarity.EnchantmentRarity
import cn.charlotte.pit.parm.listener.IAttackEntity
import cn.charlotte.pit.parm.listener.IPlayerDamaged
import cn.charlotte.pit.util.cooldown.Cooldown
import com.google.common.util.concurrent.AtomicDouble
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.util.concurrent.atomic.AtomicBoolean

class Brakes: AbstractEnchantment(), IAttackEntity, IPlayerDamaged {
    override fun getEnchantName(): String {
        return "停下!"
    }

    override fun getMaxEnchantLevel(): Int {
        return 3
    }

    override fun getNbtName(): String {
        return "brakes"
    }

    override fun getRarity(): EnchantmentRarity {
        return EnchantmentRarity.RAGE
    }

    override fun getCooldown(): Cooldown? {
        return null
    }

    override fun getUsefulnessLore(enchantLevel: Int): String {
        return "&7攻击目标的玩家如果拥有 &f速度&7 且效果为 &fI&7 级及以上, /s&7将其效果等级降至&f I&7 级." +
                if (enchantLevel > 1) "/s&7高处玩家攻击你时减少 &9${if (enchantLevel == 2) "5" else "10"}%  &7" else ""
    }

    override fun handleAttackEntity(
        enchantLevel: Int,
        attacker: Player,
        target: Entity,
        damage: Double,
        finalDamage: AtomicDouble?,
        boostDamage: AtomicDouble?,
        cancel: AtomicBoolean?
    ) {
        if (target is Player) {
            target.activePotionEffects.toList().forEach {
                if (it.type == PotionEffectType.SPEED && it.amplifier > 0) {
                    target.addPotionEffect(PotionEffect(PotionEffectType.SPEED, it.duration, 0), true)
                }
            }
        }
    }

    override fun handlePlayerDamaged(
        enchantLevel: Int,
        myself: Player,
        attacker: Entity,
        damage: Double,
        finalDamage: AtomicDouble?,
        boostDamage: AtomicDouble,
        cancel: AtomicBoolean?
    ) {
        if (attacker is Player) {
            if (attacker.location.y > myself.location.y) {
                boostDamage.set(boostDamage.get() * (1 - if (enchantLevel == 2) 0.05 else 0.1))
            }
        }
    }
}