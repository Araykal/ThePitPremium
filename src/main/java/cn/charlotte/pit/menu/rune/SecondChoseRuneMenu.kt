package cn.charlotte.pit.menu.rune

import cn.charlotte.pit.menu.rune.button.DJEnchantButton
import cn.charlotte.pit.music
import cn.charlotte.pit.musicIndex
import cn.charlotte.pit.util.Utils
import cn.charlotte.pit.util.menu.Button
import cn.charlotte.pit.util.menu.Menu
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

/**
 * @author Araykal
 * @since 2025/1/16
 */
class SecondChoseRuneMenu(private val itemStack: ItemStack, var index: Int) : Menu() {

    override fun getTitle(player: Player?): String {
        return "选择一项附魔"
    }

    override fun getButtons(player: Player): MutableMap<Int, Button> {
        val iMythicItem = Utils.getMythicItem(itemStack) ?: return mutableMapOf()

        val map = HashMap<Int, Button>()
        addEnchantButtonsToMap(map)
        return map
    }

    private fun addEnchantButtonsToMap(map: MutableMap<Int, Button>) {
        val positions = musicIndex
        for ((index, enchantment) in music.withIndex()) {
            map[positions[index]] = DJEnchantButton(enchantment, itemStack, this.index)
        }
    }

    override fun getSize(): Int {
        return 27
    }
}
