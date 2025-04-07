package cn.charlotte.pit.menu.gem.global.button

import cn.charlotte.pit.menu.gem.global.GlobalSecondChoseGemMenu
import cn.charlotte.pit.util.menu.Button
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.inventory.ItemStack

/**
 * @author Araykal
 * @since 2025/1/17
 */
class GlobalItemGemButton(private val item: ItemStack, private val index: Int) : Button() {

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
        GlobalSecondChoseGemMenu(item, index).openMenu(player)
    }
}