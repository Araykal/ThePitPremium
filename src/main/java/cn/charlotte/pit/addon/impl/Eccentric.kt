package cn.charlotte.pit.addon.impl

import cn.charlotte.pit.ThePit
import cn.charlotte.pit.addon.Addon
import cn.charlotte.pit.mode.Mode

/**
 * @author Araykal
 * @since 2025/2/7
 */
//RPG 模式
object Eccentric : Addon {
    override fun name(): String {
        return "eccentric"
    }

    override fun enable() {
        ThePit.mode = Mode.Normal
    }
}