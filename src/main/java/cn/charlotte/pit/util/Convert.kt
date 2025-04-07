package cn.charlotte.pit.util

import cn.charlotte.pit.ThePit
import cn.charlotte.pit.item.type.*
import cn.charlotte.pit.item.type.mythic.MythicBowItem
import cn.charlotte.pit.item.type.mythic.MythicSwordItem
import cn.charlotte.pit.util.item.ItemBuilder
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

fun String.findItem(amount: Int): ItemStack? {
    if (this.equals("gem", ignoreCase = true)) {
        val gem = TotallyLegitGem()
        return gem.toItemStack()

    }else if (this.equals("ggem", ignoreCase = true)) {
        val gem = GlobalAttentionGem()
        return gem.toItemStack()
    } else if (this.equals("chunkofvile", ignoreCase = true)) {
        return ItemBuilder(ChunkOfVileItem.toItemStack()).amount(amount).build()
    } else if (this.equals("funkyfeather", ignoreCase = true)) {
        val lore: MutableList<String> = ArrayList()
        lore.add("&e特殊物品")
        lore.add("&7放于物品栏时,可以保护")
        lore.add("&7背包内的神话物品不会在死亡后扣除生命.")
        lore.add("&7&o此物品会在死亡后消耗")
        return ItemBuilder(Material.FEATHER).name("&3时髦的羽毛").lore(lore).internalName("funky_feather")
            .canTrade(true).canSaveToEnderChest(true).amount(amount).build()
    } else if (this.equals("mythic_sword", ignoreCase = true)) {
        return MythicSwordItem().toItemStack()
    } else if (this.equals("mythic_bow", ignoreCase = true)) {
        return MythicBowItem().toItemStack()
    } else if (this.equals("cactus", ignoreCase = true)) {
        return ItemBuilder(PitCactus.toItemStack()).amount(amount).build()
    } else if (this.equals("uber_drop", ignoreCase = true)) {
        return UberDrop().toItemStack().apply {
            this.amount = amount
        }
    } else {
        return ThePit.getInstance()
            .itemFactor
            .itemMap[this.lowercase()]?.newInstance()?.toItemStack()?.also {
                it.amount = amount
        }
    }
}