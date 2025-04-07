package cn.charlotte.pit.map.kingsquests.item

import cn.charlotte.pit.item.AbstractPitItem
import cn.charlotte.pit.util.item.ItemBuilder
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

object Cherry: AbstractPitItem() {
    override fun getInternalName(): String {
        return "cherry"
    }

    override fun getItemDisplayName(): String {
        return "&d樱桃"
    }

    override fun getItemDisplayMaterial(): Material {
        return Material.APPLE
    }

    override fun toItemStack(): ItemStack {
        return ItemBuilder(itemDisplayMaterial)
            .internalName(internalName)
            .canDrop(true)
            .canTrade(true)
            .deathDrop(false)
            .canSaveToEnderChest(true)
            .name(itemDisplayName)
            .lore(
                "&7死亡时保留"
            )
            .build()
    }

    override fun loadFromItemStack(item: ItemStack?) {

    }
}