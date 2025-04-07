package cn.charlotte.pit.menu.gem

import cn.charlotte.pit.menu.gem.button.EnchantChoseButton
import cn.charlotte.pit.util.menu.Button
import cn.charlotte.pit.util.menu.Menu
import cn.charlotte.pit.util.toMythicItem
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import kotlin.math.min


/*
 * @ Created with IntelliJ IDEA
 * @ Author EmptyIrony
 * @ Date 2022/6/5
 * @ Time 0:28
 */
class SecondChoseGemMenu(private val itemStack: ItemStack, var index: Int) : Menu() {
    companion object {
        @JvmStatic
        private val SLOTS = arrayOf(
            11, 13, 15
        )
    }

    override fun getTitle(player: Player?): String {
        return "选择一项附魔"
    }

    override fun getButtons(player: Player): MutableMap<Int, Button> {
        val mythicItem = itemStack.toMythicItem() ?: return mutableMapOf()

        val map = HashMap<Int, Button>()

        var slot = 0
        mythicItem.enchantments.forEach { (enchant, level) ->
            map[SLOTS[min(2, slot)]] = EnchantChoseButton(itemStack, index, slot, enchant, level)
            slot++
        }

        return map
    }

    override fun getSize(): Int {
        return 3 * 9
    }
}