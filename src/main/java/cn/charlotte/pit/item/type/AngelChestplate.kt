package cn.charlotte.pit.item.type

import cn.charlotte.pit.item.IMythicItem
import cn.charlotte.pit.reflectGetNmsItem
import cn.charlotte.pit.util.item.ItemBuilder
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class AngelChestplate : IMythicItem() {
    init {
        this.maxLive = 100
        this.live = 100
    }

    override fun getInternalName(): String {
        return "angel_chestplate"
    }

    override fun getItemDisplayName(): String {
        return "&f天使之甲"
    }

    override fun getItemDisplayMaterial(): Material {
        return Material.DIAMOND_CHESTPLATE
    }

    override fun isEnchanted(): Boolean {
        return true
    }

    override fun loadFromItemStack(item: ItemStack?) {
        item ?: return

        val nmsItem = item.reflectGetNmsItem()
        val tag = nmsItem?.tag ?: return
        val extra = tag.getCompound("extra") ?: return

        this.maxLive = extra.getInt("maxLive")
        this.live = extra.getInt("live")
    }

    override fun toItemStack(): ItemStack {
        return ItemBuilder(itemDisplayMaterial)
            .name(itemDisplayName)
            .lore(
                "&7生命: " + (if (live / (maxLive * 1.0) <= 0.6) if (live / (maxLive * 1.0) <= 0.3) "&c" else "&e" else "&a") + live + "&7/" + maxLive,
                "",
                "&7装备时,自身受到的伤害 &9-10% &7.",
                ""
            )
            .canTrade(true)
            .canSaveToEnderChest(true)
            .deathDrop(false)
            .removeOnJoin(false)
            .internalName(internalName)
            .maxLive(this.maxLive)
            .live(this.live)
            .deathDrop(false)
            .canSaveToEnderChest(true)
            .removeOnJoin(false)
            .canDrop(false)
            .canTrade(true)
            .shiny()
            .build()

    }
}