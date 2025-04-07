package cn.charlotte.pit.addon.impl

import cn.charlotte.pit.addon.Addon
import cn.charlotte.pit.runnable.AntiCollateralRunnable

/**
 * @author Araykal
 * @since 2025/2/8
 */
//外置附属插件
object Extend : Addon {
    override fun name(): String {
        return "extend"
    }


    override fun enable() {
        AntiCollateralRunnable.checkAddon = true
    }
}