package cn.charlotte.pit.menu.gem.button


import cn.charlotte.pit.menu.gem.SecondChoseGemMenu
import cn.charlotte.pit.util.menu.Button
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.inventory.ItemStack

class ItemGemButton(private val item: ItemStack, private val index: Int) : Button() {
    override fun getButtonItem(player: Player): ItemStack {
        return item
    }

    override fun clicked(
        player: Player,
        slot: Int,
        clickType: ClickType,
        hotbarButton: Int,
        currentItem: ItemStack
    ) {
        SecondChoseGemMenu(item, index).openMenu(player)
    }
}