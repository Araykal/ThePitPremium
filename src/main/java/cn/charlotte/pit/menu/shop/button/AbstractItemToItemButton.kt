package cn.charlotte.pit.menu.shop.button

import cn.charlotte.pit.util.chat.CC
import cn.charlotte.pit.util.inventory.InventoryUtil
import cn.charlotte.pit.util.item.ItemBuilder
import cn.charlotte.pit.util.menu.Button
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.inventory.ItemStack

abstract class AbstractItemToItemButton : Button() {

    abstract fun getInternalName(): String?


    override fun getButtonItem(player: Player): ItemStack {
        val itemStack = getDisplayButtonItem(player)
        return try {
            val lines = itemStack.itemMeta.lore
            lines.add(" ")
            lines.addAll(getNeedItem(player))
            ItemBuilder(itemStack).lore(lines).build()
        } catch (e: Exception) {
            getDisplayButtonItem(player)
        }
    }

    abstract fun getDisplayButtonItem(player: Player?): ItemStack

    abstract fun getResultItem(player: Player?): Array<ItemStack?>

    abstract fun getNeedItem(player: Player): List<String>

    abstract fun hasItems(player: Player): Boolean

    abstract fun costItems(player: Player)

    override fun clicked(player: Player, slot: Int, clickType: ClickType, hotbarButton: Int, currentItem: ItemStack?) {
        if (hasItems(player)) {
            val freeSlots = InventoryUtil.getInventoryEmptySlots(player.inventory.contents)
            if (freeSlots < this.getResultItem(player).size) {
                player.sendMessage(CC.translate("&c&l背包已满! &7你的背包已满,暂时无法购买物品."))
                return
            }
            costItems(player)
            player.inventory.addItem(*getResultItem(player))
            player.sendMessage(CC.translate("&a购买成功!"))
            player.playSound(player.location, Sound.NOTE_PLING, 1f, 1f)
        } else {
            player.sendMessage(CC.translate("&c你的材料不足!"))
            player.playSound(player.location, Sound.ENDERMAN_TELEPORT, 1f, 1f)
        }
    }
}