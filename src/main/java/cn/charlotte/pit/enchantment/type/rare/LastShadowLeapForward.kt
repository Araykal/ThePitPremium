package cn.charlotte.pit.enchantment.type.rare

import cn.charlotte.pit.ThePit
import cn.charlotte.pit.enchantment.AbstractEnchantment
import cn.charlotte.pit.enchantment.IActionDisplayEnchant
import cn.charlotte.pit.enchantment.param.item.WeaponOnly
import cn.charlotte.pit.enchantment.rarity.EnchantmentRarity
import cn.charlotte.pit.parm.AutoRegister
import cn.charlotte.pit.util.PlayerUtil
import cn.charlotte.pit.util.cooldown.Cooldown
import cn.charlotte.pit.util.time.TimeUtil
import cn.charlotte.pit.util.toMythicItem
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.util.*

@AutoRegister
@WeaponOnly
class LastShadowLeapForward: AbstractEnchantment(), Listener, IActionDisplayEnchant {
    private val cooldownMap = HashMap<UUID, Cooldown>()

    override fun getEnchantName(): String {
        return "末影跃进"
    }

    override fun getMaxEnchantLevel(): Int {
        return 3
    }

    override fun getNbtName(): String {
        return "last_shadow_leap_forward"
    }

    override fun getRarity(): EnchantmentRarity {
        return EnchantmentRarity.RARE
    }

    override fun getCooldown(): Cooldown? {
        return null
    }

    override fun getUsefulnessLore(enchantLevel: Int): String {
        return "右键发动技能，向前突进一小段距离，之后自身获得速度${enchantLevel}（持续3秒）(${getDuration(enchantLevel)}秒冷却)"
    }

    private fun getDuration(enchantLevel: Int): Int {
        return when(enchantLevel) {
            1 -> 30
            2 -> 28
            else -> 20
        }
    }

    @EventHandler
    fun onInteract(event: PlayerInteractEvent) {
              val action = event.action
        if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
            val player = event.player

            val ignore = PlayerUtil.shouldIgnoreEnchant(player)
            if (ignore) return

            val item = player.itemInHand
            val mythicItem = item.toMythicItem() ?: return
            val level = mythicItem.enchantments[this] ?: return

            cooldownMap[player.uniqueId]?.also {
                if (!it.hasExpired()) {
                    return
                }
            }

            cooldownMap[player.uniqueId] = Cooldown(getDuration(level) * 1000L)

            val vector = player.location.direction.clone().multiply(1.6).apply {
                y = 0.5
            }

            Bukkit.getScheduler().runTask(ThePit.getInstance()) {
                player.velocity = vector
            }
            player.addPotionEffect(
                PotionEffect(
                    PotionEffectType.SPEED,
                    3 * 20,
                    level - 1,
                    true
                )
            )
        }
    }

    override fun getText(level: Int, player: Player): String {
        return (if (cooldownMap.getOrDefault(player.uniqueId, Cooldown(0))
                .hasExpired()
        ) "&a&l✔" else "&c&l" + TimeUtil.millisToRoundedTime(
            cooldownMap.getOrDefault(
                player.uniqueId,
                Cooldown(0)
            ).remaining
        ).replace(" ", ""))
    }

}