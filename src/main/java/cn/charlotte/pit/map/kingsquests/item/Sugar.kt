package cn.charlotte.pit.map.kingsquests.item

import cn.charlotte.pit.item.AbstractPitItem
import cn.charlotte.pit.util.item.ItemBuilder
import cn.charlotte.pit.util.submit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.inventory.ItemStack

object Sugar: AbstractPitItem(), Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
    fun onBreak(event: BlockBreakEvent) {
        val block = event.block
        val player = event.player

        if (block.type != Material.MELON_BLOCK) {
            return
        }

        val item = player.itemInHand ?: return
        if (!item.type.name.contains("_AXE")) {
            return
        }

        event.isCancelled = false
        block.type = Material.AIR
        player.inventory.addItem(toItemStack())

        submit(delay = 20 * 60 * 3) {
            block.type = Material.MELON_BLOCK
            block.world.playSound(block.location, Sound.ORB_PICKUP, 1f, 1f)
        }
    }

    override fun getInternalName(): String {
        return "sugar"
    }

    override fun getItemDisplayName(): String {
        return "&a糖"
    }

    override fun getItemDisplayMaterial(): Material {
        return Material.SUGAR
    }

    override fun toItemStack(): ItemStack {
        return ItemBuilder(itemDisplayMaterial)
            .internalName(internalName)
            .canDrop(true)
            .canTrade(true)
            .deathDrop(false)
            .canSaveToEnderChest(true)
            .name(itemDisplayName)
            .lore(
                "&7死亡时保留"
            )
            .build()
    }

    override fun loadFromItemStack(item: ItemStack?) {

    }
}