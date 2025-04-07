package cn.charlotte.pit.enchantment.type.rare

import cn.charlotte.pit.ThePit
import cn.charlotte.pit.data.PlayerProfile
import cn.charlotte.pit.enchantment.AbstractEnchantment
import cn.charlotte.pit.enchantment.IActionDisplayEnchant
import cn.charlotte.pit.enchantment.param.item.BowOnly
import cn.charlotte.pit.enchantment.rarity.EnchantmentRarity
import cn.charlotte.pit.parm.listener.IPlayerShootEntity
import cn.charlotte.pit.util.cooldown.Cooldown
import com.google.common.util.concurrent.AtomicDouble
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.math.min

@BowOnly
class WitheredAndPiercingThroughTheHeart: AbstractEnchantment(), IPlayerShootEntity, IActionDisplayEnchant {
    override fun getEnchantName(): String {
        return "枯萎穿心"
    }

    override fun getMaxEnchantLevel(): Int {
        return 3
    }

    override fun getNbtName(): String {
        return "withered_and_piercing_through_the_heart"
    }

    override fun getRarity(): EnchantmentRarity {
        return EnchantmentRarity.RARE
    }

    override fun getCooldown(): Cooldown? {
        return null
    }

    override fun getUsefulnessLore(enchantLevel: Int): String {
        return "&7第四次攻击对敌人施加禁锢(持续${enchantLevel * 0.5}秒)（禁锢：让敌人停止在原地)/s并且自身获得${1 + enchantLevel}心额外生命值，${enchantLevel}秒的生命回复二"
    }

    override fun handleShootEntity(
        enchantLevel: Int,
        attacker: Player,
        target: Entity,
        damage: Double,
        finalDamage: AtomicDouble?,
        boostDamage: AtomicDouble?,
        cancel: AtomicBoolean?
    ) {
        val victim = (target as? Player) ?: return

        if (PlayerProfile.getPlayerProfileByUuid(attacker.uniqueId).bowHit % 4 == 0) {
            val metadata = victim.getMetadata("lastThroughTheHeart")
            val lastTimeStamp = metadata.firstOrNull()?.asLong() ?: -1L
            if (System.currentTimeMillis() - lastTimeStamp >= 10 * 1000L) {
                victim.addPotionEffect(
                    PotionEffect(
                        PotionEffectType.SLOW,
                        ((0.5 * enchantLevel) * 20).toInt(),
                        10
                    ),
                    true
                )

                victim.addPotionEffect(
                    PotionEffect(
                        PotionEffectType.JUMP,
                        ((0.5 * enchantLevel) * 20).toInt(),
                        -3
                    ),
                    true
                )
                victim.setMetadata("lastThroughTheHeart", FixedMetadataValue(ThePit.getInstance(), System.currentTimeMillis()))
            }

            attacker.playSound(attacker.location, Sound.ANVIL_LAND, 1f, 1f)
            victim.playSound(attacker.location, Sound.ANVIL_LAND, 1f, 1f)

            val ePlayer = (attacker as CraftPlayer).handle
            ePlayer.absorptionHearts = min(((1f + enchantLevel) * 2) * 2, (1f + enchantLevel) * 2)
            attacker.addPotionEffect(
                PotionEffect(
                    PotionEffectType.REGENERATION,
                    (enchantLevel) * 20,
                    1
                ),
                true
            )
        }
    }

    override fun getText(level: Int, player: Player): String {
        val hit =
            (if (player.itemInHand != null && player.itemInHand.type == Material.BOW) PlayerProfile.getPlayerProfileByUuid(
                player.uniqueId
            ).bowHit else PlayerProfile.getPlayerProfileByUuid(player.uniqueId).meleeHit)
        val require = 4
        return if (hit % require == 0) "&a&l✔" else "&e&l" + (require - hit % require)
    }

}