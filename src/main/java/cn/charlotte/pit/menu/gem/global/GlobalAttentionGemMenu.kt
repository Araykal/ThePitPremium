package cn.charlotte.pit.menu.gem.global

import cn.charlotte.pit.menu.gem.global.button.GlobalItemGemButton
import cn.charlotte.pit.util.Utils
import cn.charlotte.pit.util.menu.Button
import cn.charlotte.pit.util.menu.Menu
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent

/**
 * @author Araykal
 * @since 2025/1/17
 */
class GlobalAttentionGemMenu : Menu(){
    override fun getTitle(player: Player): String {
        return "宝石点缀"
    }

    override fun getButtons(player: Player): MutableMap<Int, Button> {
        val map = HashMap<Int, Button>()
        var index = 0
        for ((slot, itemStack) in player.inventory.withIndex()) {
            if (Utils.canUseGen(itemStack,true)) {
                map[index] = GlobalItemGemButton(itemStack, slot)
                index++
            }
        }

        return map
    }

    override fun onClickEvent(event: InventoryClickEvent?) {
        event?.isCancelled = true
    }
}
