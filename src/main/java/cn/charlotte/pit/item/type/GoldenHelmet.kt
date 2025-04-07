package cn.charlotte.pit.item.type

import cn.charlotte.pit.event.PitKillEvent
import cn.charlotte.pit.item.IMythicItem
import cn.charlotte.pit.parm.AutoRegister
import cn.charlotte.pit.reflectGetNmsItem
import cn.charlotte.pit.util.item.ItemBuilder
import cn.charlotte.pit.util.item.ItemUtil
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.inventory.ItemStack

@AutoRegister
class GoldenHelmet : IMythicItem(), Listener {
    init {
        this.maxLive = 50
        this.live = 50
    }

    override fun getInternalName(): String {
        return "kings_helmet"
    }

    override fun getItemDisplayName(): String {
        return "&6国王的王冠"
    }

    override fun getItemDisplayMaterial(): Material {
        return Material.GOLD_HELMET
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

    @EventHandler
    fun onKill(event: PitKillEvent) {
        val killer = event.killer
        if (internalName == ItemUtil.getInternalName(killer.inventory.helmet)) {
            event.exp *= 1.1
        }
    }

    override fun toItemStack(): ItemStack {
        return ItemBuilder(itemDisplayMaterial)
            .name(itemDisplayName)
            .lore(
                "&7生命: " + (if (live / (maxLive * 1.0) <= 0.6) if (live / (maxLive * 1.0) <= 0.3) "&c" else "&e" else "&a") + live + "&7/" + maxLive,
                "",
                "&9皇室",
                "&7击杀经验值&b +10%&7.",
                "",
                "&6穿着时提供钻石头盔相同的伤害减免效果"
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
            .buildWithUnbreakable()

    }
}