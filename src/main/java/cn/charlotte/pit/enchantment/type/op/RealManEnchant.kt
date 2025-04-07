package cn.charlotte.pit.enchantment.type.op

import cn.charlotte.pit.ThePit
import cn.charlotte.pit.enchantment.AbstractEnchantment
import cn.charlotte.pit.enchantment.rarity.EnchantmentRarity
import cn.charlotte.pit.parm.listener.ITickTask
import cn.charlotte.pit.util.cooldown.Cooldown
import org.bukkit.entity.Player
import org.bukkit.metadata.FixedMetadataValue

class RealManEnchant: AbstractEnchantment() , ITickTask {
    companion object {
        @JvmStatic
        fun hasRealMan(player: Player): Boolean {
            return (player.getMetadata("real_man").firstOrNull()?.asLong() ?: Long.MAX_VALUE) >= System.currentTimeMillis()
        }
    }

    override fun getEnchantName(): String {
        return "真男人"
    }

    override fun getMaxEnchantLevel(): Int {
        return 1
    }

    override fun getNbtName(): String {
        return "real_man"
    }

    override fun getRarity(): EnchantmentRarity {
        return EnchantmentRarity.OP
    }

    override fun getCooldown(): Cooldown? {
        return null
    }

    override fun getUsefulnessLore(enchantLevel: Int): String {
        return "&7当你的7格范围内全部人 &cbuff天赋连杀失效/s" +
                "           &7出范围外3秒效果消失"
    }

    override fun handle(enchantLevel: Int, player: Player) {
        player.world.getNearbyEntities(player.location, 7.0, 7.0, 7.0)
            .filterIsInstance<Player>()
            .forEach {
                it.setMetadata("real_man", FixedMetadataValue(ThePit.getInstance(), System.currentTimeMillis() + 3 * 1000L))
            }
    }

    override fun loopTick(enchantLevel: Int): Int {
        return 20
    }
}