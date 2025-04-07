package cn.charlotte.pit.menu.admin.mail

import cn.charlotte.pit.menu.cdk.generate.CDKMenu
import cn.charlotte.pit.util.menu.Button
import org.bukkit.entity.Player

/**
 * @Author: EmptyIrony
 * @Date: 2021/4/30 21:40
 */
class MailGenerator : CDKMenu() {
    override fun getTitle(player: Player?): String {
        return "Mail Generator"
    }

    override fun getButtons(player: Player?): MutableMap<Int, Button> {
        this.buttons.remove(34)
        this.buttons.remove(35)


        return this.buttons
    }
}