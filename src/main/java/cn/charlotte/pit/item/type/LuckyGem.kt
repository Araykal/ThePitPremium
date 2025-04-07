package cn.charlotte.pit.item.type

import cn.charlotte.pit.ThePit
import cn.charlotte.pit.item.AbstractPitItem
import cn.charlotte.pit.parm.AutoRegister
import cn.charlotte.pit.util.ParticleBuilder
import cn.charlotte.pit.util.PlayerUtil
import cn.charlotte.pit.util.chat.CC
import cn.charlotte.pit.util.item.ItemBuilder
import cn.charlotte.pit.util.item.ItemUtil
import net.minecraft.server.v1_8_R3.EnumParticle
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.event.Event
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.scheduler.BukkitRunnable


/**
 * @author Araykal
 * @since 2025/2/6
 */
@AutoRegister
class LuckyGem : AbstractPitItem(), Listener {
    override fun getInternalName(): String {
        return "lucky_gem"
    }

    override fun getItemDisplayName(): String {
        return "§e幸运宝石"
    }

    override fun getItemDisplayMaterial(): Material {
        return Material.NETHER_STAR
    }

    override fun toItemStack(): ItemStack {
        return ItemBuilder(itemDisplayMaterial).name(itemDisplayName).lore(
            mutableListOf(
                "&7死亡时保留",
                "",
                "&7为自身添加&e幸运,",
                "&7持续60秒,",
                "&e&l幸运！&7大幅度提升附魔稀有概率",
                "",
                "&e右键使用"
            )
        ).internalName(
            internalName
        ).canSaveToEnderChest(true).canTrade(false).deathDrop(false).shiny().build()
    }

    @EventHandler
    fun onInteract(event: PlayerInteractEvent) {
        if ("lucky_gem" == ItemUtil.getInternalName(event.item)) {
            event.isCancelled = true
            event.setUseInteractedBlock(Event.Result.DENY)
            event.setUseItemInHand(Event.Result.DENY)
            event.player.sendMessage(CC.translate("&e&l幸运! &7自身运气已大幅度提升。"))
            PlayerUtil.takeOneItemInHand(event.player)
            event.player.setMetadata("lucky", FixedMetadataValue(ThePit.getInstance(), true))
            Bukkit.getScheduler().runTaskTimer(ThePit.getInstance(), object : BukkitRunnable() {
                private var tick = 0

                override fun run() {
                    if (tick >= 1200) {
                        this.cancel()
                        event.player.sendMessage(CC.translate("&e&l幸运! &7自身运气已降低。"))
                        event.player.removeMetadata("lucky", ThePit.getInstance())
                    } else {
                        ParticleBuilder(
                            event.player.location.add(0.0, 2.0, 0.0),
                            EnumParticle.VILLAGER_HAPPY
                        ).setVelocity(
                            0.5f
                        )
                            .setCount(5).play()
                        tick++
                    }
                }
            }, 0L, 1L)

        }
    }

    override fun loadFromItemStack(item: ItemStack) {
    }
}
