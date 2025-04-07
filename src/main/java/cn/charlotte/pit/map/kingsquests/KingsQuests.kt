package cn.charlotte.pit.map.kingsquests

import cn.charlotte.pit.ThePit
import cn.charlotte.pit.item.AbstractPitItem
import cn.charlotte.pit.map.kingsquests.item.*
import org.bukkit.Bukkit
import org.bukkit.event.Listener

object KingsQuests {

    fun enable() {
        Cherry.register()
        HighGradeEggs.register()
        MiniCake.register()
        PackagedBale.register()
        Sugar.register()
        Wheat.register()
        YummyBread.register()
    }

    private fun Any.register() {
        if (this is Listener) {
            Bukkit.getPluginManager().registerEvents(this, ThePit.getInstance())
        }

        if (this is AbstractPitItem) {
            ThePit.getInstance()
                .itemFactor.registerItem(this)
        }
    }

}