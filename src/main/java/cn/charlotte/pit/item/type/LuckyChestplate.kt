package cn.charlotte.pit.item.type

import cn.charlotte.pit.ThePit
import cn.charlotte.pit.item.IMythicItem
import cn.charlotte.pit.reflectGetNmsItem
import cn.charlotte.pit.util.MythicUtil
import cn.charlotte.pit.util.item.ItemBuilder
import cn.charlotte.pit.util.random.RandomUtil
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.metadata.FixedMetadataValue

class LuckyChestplate: IMythicItem(), Listener {

    init {
        this.maxLive = 10
        this.live = 10
    }

    override fun getInternalName(): String {
        return "lucky_chestplate"
    }

    override fun getItemDisplayName(): String {
        return "&b幸运之甲"
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
                "&9幸运之甲",
                "&7装备时,&c20%&7的几率将致命一击抵消 &7.",
                "&7下一次伤害增加 &c20",
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

    @EventHandler(priority = EventPriority.LOW)
    fun e(e: EntityDamageByEntityEvent) {
        val player = e.entity
        if (player !is Player) return

        if (player.health - e.finalDamage >= 0.0) {
            return
        }

        val itemStack = player.inventory.chestplate ?: return
        val mythicItem = MythicUtil.getMythicItem(itemStack)
        if (mythicItem !is LuckyChestplate) {
            return
        }

        if ((player.getMetadata("lucky_chestplate").firstOrNull()?.asLong() ?: Long.MAX_VALUE) <= System.currentTimeMillis()) {
            e.damage += 20.0
        }

        player.removeMetadata("lcuky_chestplate", ThePit.getInstance())

        val success = RandomUtil.hasSuccessfullyByChance(0.2)
        if (success) {
            e.isCancelled = true
            player.setMetadata("lucky_chestplate", FixedMetadataValue(ThePit.getInstance(), System.currentTimeMillis() + 1000 * 30))
            player.playSound(player.location, Sound.ANVIL_USE, 1f, 1f)
            player.sendMessage("您抵御了一次必中伤害")
        }
    }

}