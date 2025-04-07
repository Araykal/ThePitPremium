package cn.charlotte.pit.item.type

import cn.charlotte.pit.item.AbstractPitItem
import cn.charlotte.pit.util.item.ItemBuilder
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

/**
 * @Author: Misoryan
 * @Created_In: 2021/3/22 17:14
 */
class SpireArmor(private val material: Material) : AbstractPitItem() {
    override fun getInternalName(): String {
        return "spire_armor"
    }

    override fun getItemDisplayName(): String {
        return material.name
    }

    override fun getItemDisplayMaterial(): Material {
        return material
    }

    override fun toItemStack(): ItemStack {
        val lore = this.enchantLore
        lore.add(0, "")
        lore.add(0, "&7事件物品")

        return ItemBuilder(this.itemDisplayMaterial)
            .name(this.itemDisplayName)
            .internalName(this.internalName)
            .removeOnJoin(true)
            .deathDrop(true)
            .lore(lore)
            .buildWithUnbreakable()
    }

    override fun loadFromItemStack(item: ItemStack) {
    }
}
