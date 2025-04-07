package cn.charlotte.pit.enchantment.type.normal

import cn.charlotte.pit.ThePit
import cn.charlotte.pit.enchantment.AbstractEnchantment
import cn.charlotte.pit.enchantment.param.event.PlayerOnly
import cn.charlotte.pit.enchantment.param.item.ArmorOnly
import cn.charlotte.pit.enchantment.rarity.EnchantmentRarity
import cn.charlotte.pit.parm.listener.IPlayerKilledEntity
import cn.charlotte.pit.util.cooldown.Cooldown
import com.google.common.util.concurrent.AtomicDouble
import org.bukkit.Bukkit
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

/**
 * @Author: Misoryan
 * @Created_In: 2021/2/25 15:11
 */
@ArmorOnly
class ElectrolytesEnchant : AbstractEnchantment(), IPlayerKilledEntity {
    override fun getEnchantName(): String {
        return "电解质"
    }

    override fun getMaxEnchantLevel(): Int {
        return 3
    }

    override fun getNbtName(): String {
        return "electrolytes_enchant"
    }

    override fun getRarity(): EnchantmentRarity {
        return EnchantmentRarity.NORMAL
    }

    override fun getCooldown(): Cooldown? {
        return null
    }

    override fun getUsefulnessLore(enchantLevel: Int): String {
        return ("&7击杀时如自身存在 &b速度 &7效果,延长效果时间 &e" + (enchantLevel * 2) + " 秒"
                + "/s&7(如效果等级大于II则延长时间减半,上限" + ((enchantLevel + 2) * 6) + "秒)")
    }

    @PlayerOnly
    override fun handlePlayerKilled(
        enchantLevel: Int,
        myself: Player,
        target: Entity?,
        coins: AtomicDouble?,
        experience: AtomicDouble?
    ) {
        myself.activePotionEffects
            .firstOrNull { effect: PotionEffect -> effect.type === PotionEffectType.SPEED }?.let { potion ->
                val duration = if (potion.amplifier >= 1) {
                    potion.duration + enchantLevel * 2 * 20
                } else {
                    potion.duration + enchantLevel * 20
                }
                Bukkit.getScheduler().runTaskLater(
                    ThePit.getInstance(),
                    {
                        myself.addPotionEffect(
                            PotionEffect(
                                PotionEffectType.SPEED,
                                duration,
                                potion.amplifier
                            ), true
                        )
                    },
                    5L
                )
            }
    }
}