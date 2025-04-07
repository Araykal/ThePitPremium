package cn.charlotte.pit.map.kingsquests.item

import cn.charlotte.pit.item.AbstractPitItem
import cn.charlotte.pit.util.item.ItemBuilder
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

object HighGradeEggs: AbstractPitItem() {
    override fun getInternalName(): String {
        return "high-grade_eggs"
    }

    override fun getItemDisplayName(): String {
        return "&e高级鸡蛋"
    }

    override fun getItemDisplayMaterial(): Material {
        return Material.EGG
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