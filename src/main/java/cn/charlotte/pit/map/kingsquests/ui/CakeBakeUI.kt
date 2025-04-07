package cn.charlotte.pit.map.kingsquests.ui

import cn.charlotte.pit.map.kingsquests.item.MiniCake
import cn.charlotte.pit.util.chat.CC
import cn.charlotte.pit.util.countItem
import cn.charlotte.pit.util.item.ItemBuilder
import cn.charlotte.pit.util.item.ItemUtil
import cn.charlotte.pit.util.menu.Button
import cn.charlotte.pit.util.menu.Menu
import cn.charlotte.pit.util.takeItem
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.inventory.ItemStack

object CakeBakeUI: Menu() {
    override fun getTitle(player: Player?): String {
        return "烘焙大师"
    }

    override fun getButtons(player: Player): MutableMap<Int, Button> {
        return mutableMapOf(
            13 to object :Button() {
                override fun getButtonItem(player: Player): ItemStack {
                    return ItemBuilder(Material.CAKE)
                        .name("&a制作&d迷你蛋糕")
                        .lore(
                            "&7你需要准备材料:",
                            " &d樱桃 &7x1",
                            " &a糖 &7x12",
                            " &e高级鸡蛋 &7x6",
                            " &e干草块 &7x40",
                            "",
                            "&a点击制作!"
                        )
                        .build()
                }

                override fun clicked(
                    player: Player,
                    slot: Int,
                    clickType: ClickType?,
                    hotbarButton: Int,
                    currentItem: ItemStack?
                ) {
                    val inventory = player.inventory
                    val cherry = inventory.countItem {
                        ItemUtil.getInternalName(it) == "cherry"
                    }
                    val sugar = inventory.countItem {
                        ItemUtil.getInternalName(it) == "sugar"
                    }
                    val highGradeEggs = inventory.countItem {
                        ItemUtil.getInternalName(it) == "high-grade_eggs"
                    }
                    val packagedBale = inventory.countItem {
                        ItemUtil.getInternalName(it) == "packaged_bale"
                    }

                    if (cherry < 1) {
                        player.sendMessage(CC.translate("&c你缺少 &d樱桃 &c!"))
                        return
                    }

                    if (sugar < 12) {
                        player.sendMessage(CC.translate("&c你缺少 &a糖 &c!"))
                        return
                    }

                    if (highGradeEggs < 6) {
                        player.sendMessage(CC.translate("&c你缺少 &e高级鸡蛋 &c!"))
                        return
                    }

                    if (packagedBale < 40) {
                        player.sendMessage(CC.translate("&c你缺少 &e干草块 &c!"))
                        return
                    }

                    inventory.takeItem(1) {
                        ItemUtil.getInternalName(it) == "cherry"
                    }
                    inventory.takeItem(12) {
                        ItemUtil.getInternalName(it) == "sugar"
                    }
                    inventory.takeItem(6) {
                        ItemUtil.getInternalName(it) == "high-grade_eggs"
                    }
                    inventory.takeItem(40) {
                        ItemUtil.getInternalName(it) == "packaged_bale"
                    }

                    inventory.addItem(MiniCake.toItemStack())
                }
            }
        )
    }

    override fun size(buttons: MutableMap<Int, Button>?): Int {
        return 3 * 9
    }
}