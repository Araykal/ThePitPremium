package cn.charlotte.pit.item.type

import cn.charlotte.pit.item.AbstractPitItem
import cn.charlotte.pit.menu.gem.TotallyLegitGemMenu
import cn.charlotte.pit.util.item.ItemBuilder
import cn.charlotte.pit.util.item.ItemUtil
import org.bukkit.Material
import org.bukkit.event.Event
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack

class TotallyLegitGem : AbstractPitItem(), Listener {

    @EventHandler
    fun interact(event: PlayerInteractEvent) {
        if ("totally_legit_gem" == ItemUtil.getInternalName(event.item)) {
            event.isCancelled = true
            event.setUseInteractedBlock(Event.Result.DENY)
            event.setUseItemInHand(Event.Result.DENY)
            TotallyLegitGemMenu().openMenu(event.player)
            return
        }
    }

    override fun getInternalName(): String {
        return "totally_legit_gem"
    }

    override fun getItemDisplayName(): String {
        return "&a遵纪守法的宝石"
    }

    override fun getItemDisplayMaterial(): Material {
        return Material.EMERALD
    }

    override fun toItemStack(): ItemStack {
        return ItemBuilder(itemDisplayMaterial)
            .name(itemDisplayName)
            .lore(
                "&7死亡时保留",
                "",
                "&7增加附魔物品的一级附魔, 并使神话物品生命后添加 &a♦ &7的字符",
                "&7(稀有及特殊附魔除外, 不可超过上限)",
                "&8一件物品只能使用一次",
                "",
                "&e右键使用"
            )
            .internalName(internalName)
            .shiny()
            .removeOnJoin(false)
            .deathDrop(false)
            .canDrop(false)
            .canTrade(true)
            .canSaveToEnderChest(true)
            .build()
    }

    override fun loadFromItemStack(item: ItemStack) {

    }


}