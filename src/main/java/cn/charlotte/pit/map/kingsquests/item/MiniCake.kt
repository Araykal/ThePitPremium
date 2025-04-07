package cn.charlotte.pit.map.kingsquests.item

import cn.charlotte.pit.item.AbstractPitItem
import cn.charlotte.pit.util.item.ItemBuilder
import cn.charlotte.pit.util.item.ItemUtil
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.inventory.ItemStack

object MiniCake: AbstractPitItem(), Listener {

    @EventHandler
    fun onPlace(event: BlockPlaceEvent) {
        val itemStack = event.itemInHand ?: return
        if (itemStack.type == Material.CAKE && ItemUtil.getInternalName(itemStack) == "mini_cake") {
            event.isCancelled = true
        }
    }

    override fun getInternalName(): String {
        return "mini_cake"
    }

    override fun getItemDisplayName(): String {
        return "&d迷你蛋糕"
    }

    override fun getItemDisplayMaterial(): Material {
        return Material.CAKE
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
                "&7死亡时保留",
                "&7国王的最爱"
            )
            .dontStack()
            .build()
    }

    override fun loadFromItemStack(item: ItemStack?) {

    }
}