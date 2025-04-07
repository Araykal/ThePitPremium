package cn.charlotte.pit.enchantment.type.aqua

import cn.charlotte.pit.enchantment.AbstractEnchantment
import cn.charlotte.pit.enchantment.param.item.ArmorOnly
import cn.charlotte.pit.enchantment.rarity.EnchantmentRarity
import cn.charlotte.pit.parm.AutoRegister
import cn.charlotte.pit.util.cooldown.Cooldown
import cn.charlotte.pit.util.toMythicItem
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerFishEvent
import kotlin.math.min

@ArmorOnly
@AutoRegister
class LuckOfPondEnchant: AbstractEnchantment(), Listener{

    @EventHandler
    private fun onFish(event: PlayerFishEvent) {
        val leggings = event.player.inventory.leggings
        if (leggings?.toMythicItem()?.enchantments?.keys?.any { enchant ->
                enchant.nbtName == "luck_of_pond"
            } == true) {
            val chance = event.hook.biteChance
            event.hook.biteChance = min(chance, 1.0)
        }
    }

    override fun getEnchantName(): String {
        return "幸运之池"
    }

    override fun getMaxEnchantLevel(): Int {
        return 1
    }

    override fun getNbtName(): String {
        return "luck_of_pond"
    }

    override fun getRarity(): EnchantmentRarity {
        return EnchantmentRarity.FISH_NORMAL
    }

    override fun getCooldown(): Cooldown? {
        return null
    }

    override fun getUsefulnessLore(enchantLevel: Int): String {
        return "&7钓鱼时鱼咬钩的速度提升 &a10%"
    }


}