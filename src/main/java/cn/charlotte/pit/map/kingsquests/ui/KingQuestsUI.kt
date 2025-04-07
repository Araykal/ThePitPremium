package cn.charlotte.pit.map.kingsquests.ui

import cn.charlotte.pit.map.kingsquests.ui.button.KingsQuestsButton
import cn.charlotte.pit.util.menu.Button
import cn.charlotte.pit.util.menu.Menu
import org.bukkit.entity.Player

object KingQuestsUI: Menu() {
    override fun getTitle(player: Player?): String {
        return "国王任务"
    }

    override fun getButtons(player: Player): MutableMap<Int, Button> {
        val map = HashMap<Int, Button>()

        map[13] = KingsQuestsButton

        return map
    }

    override fun size(buttons: MutableMap<Int, Button>?): Int {
        return 3 * 9
    }
}