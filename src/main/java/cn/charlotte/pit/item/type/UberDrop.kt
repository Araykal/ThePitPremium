package cn.charlotte.pit.item.type

import cn.charlotte.pit.ThePit
import cn.charlotte.pit.item.AbstractPitItem
import cn.charlotte.pit.parm.AutoRegister
import cn.charlotte.pit.util.PlayerUtil
import cn.charlotte.pit.util.chat.CC
import cn.charlotte.pit.util.inventory.InventoryUtil
import cn.charlotte.pit.util.item.ItemBuilder
import cn.charlotte.pit.util.item.ItemUtil
import cn.charlotte.pit.util.random.RandomUtil
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable
import kotlin.random.Random

@AutoRegister
class UberDrop : AbstractPitItem(), Listener {
    var index = -1

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun onInteract(event: BlockPlaceEvent) {
        val itemInHand = event.itemInHand ?: return
        if ("uber_drop" == ItemUtil.getInternalName(itemInHand)) {
            event.isCancelled = true

            val player = event.player
            if (InventoryUtil.isInvFull(player)) {
                player.sendMessage(CC.translate("&c你的背包是满的, 无法使用物品"))
                return
            }
            val hasSuccessfullyByChance = RandomUtil.hasSuccessfullyByChance(0.75);

            val itemStack = RandomUtil.helpMeToChooseOne(
                FunkyFeather.toItemStack().also {
                    it.amount = Random.nextInt(2, 3)
                },
                FunkyFeather.toItemStack().also {
                    it.amount = Random.nextInt(2, 3)
                },
                PitCactus.toItemStack().also {
                    it.amount = Random.nextInt(5, 20)
                },
                JewelSword().toItemStack(),
                if (ThePit.getInstance().pitConfig.betaVersion) {
                    if (hasSuccessfullyByChance) TotallyLegitGem().toItemStack() else GlobalAttentionGem().toItemStack()
                } else {
                    TotallyLegitGem().toItemStack()
                }
            ) as ItemStack

            var ticks = 0.3f
            object : BukkitRunnable() {
                override fun run() {
                    player.playSound(player.location, Sound.CHICKEN_EGG_POP, 1f, ticks)
                    ticks += 0.1f
                    if (ticks >= 2.0) {
                        cancel()
                    }
                }
            }.runTaskTimer(ThePit.getInstance(), 0L, 2L)

            PlayerUtil.takeOneItemInHand(player)
            player.inventory.addItem(itemStack)

            player.sendMessage(CC.translate("&d登峰造极掉落物! &7你获得了 ${itemStack.itemMeta.displayName}"))
        }
    }

    override fun getInternalName(): String {
        return "uber_drop"
    }

    override fun getItemDisplayName(): String {
        return "&d登峰造极掉落物"
    }

    override fun getItemDisplayMaterial(): Material {
        return Material.ENDER_CHEST
    }

    override fun toItemStack(): ItemStack {
        return ItemBuilder(itemDisplayMaterial)
            .internalName("uber_drop")
            .name(itemDisplayName)
            .lore(
                "&7死亡时保留",
                "",
                "&e拿着并右键获得物品!"
            )
            .dontStack()
            .build()
    }

    override fun loadFromItemStack(item: ItemStack?) {

    }
}