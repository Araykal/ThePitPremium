package cn.charlotte.pit.runnable

import cn.charlotte.pit.ThePit
import cn.charlotte.pit.util.chat.ActionBarUtil
import cn.charlotte.pit.util.chat.CC
import cn.charlotte.pit.util.toMythicItem
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

object ActionBarDisplayRunnable {

    private val enchants by lazy {
        ThePit.getInstance().enchantmentFactor.actionDisplayEnchants
    }

    fun start() {
        val scheduler = Executors.newSingleThreadScheduledExecutor()
        scheduler.scheduleAtFixedRate({
            val currentTime = System.currentTimeMillis()

            Bukkit.getOnlinePlayers().forEach { player ->
                val metadata = player.getMetadata("showing_damage_data")
                val lastShownTime = metadata.firstOrNull()?.asLong() ?: 0L

                if (currentTime - lastShownTime > 1000L) {
                    val message = buildString {
                        player.itemInHand?.let { item ->
                            append(player.createActionDisplay(item))
                        }
                        player.inventory.armorContents.forEach { armorItem ->
                            append(player.createActionDisplay(armorItem))
                        }
                    }

                    if (message.isNotBlank() || message.isNotEmpty()) {
                        ActionBarUtil.sendActionBar(player, CC.translate(message))
                    }
                }
            }
        }, 5000L, 50L, TimeUnit.MILLISECONDS)
    }

    private fun Player.createActionDisplay(itemStack: ItemStack?): String {
        val item = itemStack?.toMythicItem() ?: return ""
        return item.enchantments.entries.joinToString(" ") { (key, value) ->
            val enchantmentDisplay = enchants[key.nbtName] ?: return@joinToString ""
            "&b&l${key.enchantName} ${enchantmentDisplay.getText(value, this)}"
        }
    }
}