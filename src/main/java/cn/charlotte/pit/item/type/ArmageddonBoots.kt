package cn.charlotte.pit.item.type

import cn.charlotte.pit.item.IMythicItem
import cn.charlotte.pit.util.item.ItemBuilder
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack
import org.bukkit.inventory.ItemStack

class ArmageddonBoots : IMythicItem() {

    init {
        maxLive = 66
        live = 66
    }

    override fun getInternalName(): String {
        return "armageddon_boots"
    }

    override fun getItemDisplayName(): String {
        return "&c终末之靴"
    }

    override fun getItemDisplayMaterial(): Material {
        return Material.LEATHER_BOOTS
    }

    override fun isEnchanted(): Boolean {
        return true
    }

    override fun loadFromItemStack(item: ItemStack?) {
        item ?: return

        val nmsItem = CraftItemStack.asNMSCopy(item)
        val tag = nmsItem?.tag ?: return
        val extra = tag.getCompound("extra") ?: return

        this.maxLive = extra.getInt("maxLive")
        this.live = extra.getInt("live")
    }


    override fun toItemStack(): ItemStack? {
        return ItemBuilder(itemDisplayMaterial)
            .name(itemDisplayName)
            .lore(
                "&7生命: " + (if (live / (maxLive * 1.0) <= 0.6) if (live / (maxLive * 1.0) <= 0.3) "&c" else "&e" else "&a") + live + "&7/" + maxLive,
                "",
                "&7攻击其他玩家时免疫 &9无尽黑暗 &7附魔效果.",
                ""
            )
            .internalName(internalName)
            .maxLive(maxLive)
            .live(live)
            .setLetherColor(Color.RED)
            .deathDrop(false)
            .canSaveToEnderChest(true)
            .removeOnJoin(false)
            .canDrop(false)
            .canTrade(true)
            .shiny()
            .build()
    }

}