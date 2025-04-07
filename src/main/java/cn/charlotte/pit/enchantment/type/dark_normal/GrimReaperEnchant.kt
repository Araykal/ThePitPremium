package cn.charlotte.pit.enchantment.type.dark_normal

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
import java.util.stream.Collectors

@ArmorOnly
class GrimReaperEnchant : AbstractEnchantment(), IPlayerKilledEntity {
    override fun getEnchantName(): String {
        return "死神"
    }

    override fun getMaxEnchantLevel(): Int {
        return 1
    }

    override fun getNbtName(): String {
        return "grim_reaper_enchant"
    }

    override fun getRarity(): EnchantmentRarity {
        return EnchantmentRarity.DARK_NORMAL
    }

    override fun getCooldown(): Cooldown? {
        return null
    }

    override fun getUsefulnessLore(enchantLevel: Int): String {
        return ("&7击杀玩家时释放冲击波,对周围8格内所有玩家造成 &c2❤ &7普通伤害"
                + "/s&c击杀获得的奖励 -80%")
    }

    @PlayerOnly
    override fun handlePlayerKilled(
        enchantLevel: Int,
        myself: Player,
        target: Entity,
        coins: AtomicDouble,
        experience: AtomicDouble
    ) {
        Bukkit.getScheduler().runTaskAsynchronously(ThePit.getInstance()) {
            coins.set(-0.8 * coins.get())
            experience.set(-0.8 * experience.get())
            val nearbyPlayers = myself.world.getNearbyEntities(myself.location, 8.0, 8.0, 8.0).stream()
                .filter { entity: Entity? -> entity is Player }
                .map { entity: Entity -> entity as Player }
                .collect(Collectors.toList())
            for (player in nearbyPlayers) {
                if (player == myself) {
                    continue
                }
                Bukkit.getScheduler().runTask(ThePit.getInstance()) { player.damage(4.0, myself) }
            }
        }
    }
}
