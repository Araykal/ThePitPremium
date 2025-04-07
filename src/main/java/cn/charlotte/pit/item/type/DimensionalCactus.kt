package cn.charlotte.pit.item.type

import cn.charlotte.pit.util.item.ItemBuilder
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

/**
 * @author Araykal
 * @since 2025/1/17
 */
class DimensionalCactus {
    fun toItemStack(): ItemStack {
        return ItemBuilder(Material.CACTUS).name("§a次元仙人掌").lore(
            "&e特殊物品",
            "&7手持并右键可以从九件未附魔的",
            "&7随机 &a神&c话&e之&6甲 &7选择其一.",
            " ",
            "&6(所有颜色可选择)"
        ).internalName("dimensional_cactus")
            .canTrade(true).canSaveToEnderChest(true).shiny().build();
    }
}