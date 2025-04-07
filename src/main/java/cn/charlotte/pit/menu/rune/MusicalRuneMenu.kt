package cn.charlotte.pit.menu.rune

import cn.charlotte.pit.menu.rune.button.ItemRuneButton
import cn.charlotte.pit.music
import cn.charlotte.pit.util.Utils.getMythicItem
import cn.charlotte.pit.util.menu.Button
import cn.charlotte.pit.util.menu.Menu
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

/**
 * @author Araykal
 * @since 2025/1/16
 */
class MusicalRuneMenu : Menu() {


    override fun getTitle(player: Player): String {
        return "乐章谱写"
    }

    override fun getButtons(player: Player): MutableMap<Int, Button> {
        val map = HashMap<Int, Button>()
        var index = 0

        for ((slot, itemStack) in player.inventory.withIndex()) {
            if (isItemValid(itemStack)) {
                map[index++] = ItemRuneButton(itemStack, slot)
            }
        }
        return map
    }

    private fun isItemValid(itemStack: ItemStack?): Boolean {

        return itemStack?.type == Material.LEATHER_LEGGINGS &&
                (getMythicItem(itemStack) != null &&
                        !music.any { it.isItemHasEnchant(itemStack) })
    }

    override fun onClickEvent(event: InventoryClickEvent?) {
        event?.isCancelled = true
    }
}

