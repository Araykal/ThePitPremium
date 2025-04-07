package cn.charlotte.pit.addon

import cn.charlotte.pit.addon.impl.*

import sinc.Native

@Native
object AddonHandler {

    private val addons = ArrayList<Addon>()

    fun start() {
        addons.addAll(
            listOf(GiveItemCommand(), GachaPool, EnchantBook, Eccentric, Extend)
        )
        for(addon in addons){
            println("Loading addon ${addon.name()}...")
            addon.enable()
        }
//        for (addon in addons.filter {
//            AddonUtil.check(it.name())
//        }) {
//            println("Loading addon ${addon.name()}...")
//            addon.enable()
//        }
//        if (!WebSocketClient.connect){
//           System.exit(-1)
//        }
    }

}