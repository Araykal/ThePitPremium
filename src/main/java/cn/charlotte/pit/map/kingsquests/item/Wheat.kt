package cn.charlotte.pit.map.kingsquests.item

import cn.charlotte.pit.item.AbstractPitItem
import cn.charlotte.pit.util.countItem
import cn.charlotte.pit.util.item.ItemBuilder
import cn.charlotte.pit.util.takeItem
import org.bukkit.CropState
import org.bukkit.Effect
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.material.Crops
import kotlin.random.Random

object Wheat: AbstractPitItem(), Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
    fun onBreak(event: BlockBreakEvent) {
        val player = event.player
        val block = event.block

        if (block.type == Material.CROPS) {
            event.isCancelled = true
            val state = block.state

            val crops = state.data as Crops
            if (crops.state != CropState.RIPE) return

            crops.state = CropState.SEEDED
            state.update()

            player.inventory.addItem(
                toItemStack()
            )
        }
    }

    @EventHandler
    fun onInteract(event: PlayerInteractEvent) {
        val block = event.clickedBlock ?: return
        val player = event.player

        if (event.action != Action.RIGHT_CLICK_BLOCK) {
            return
        }

        if (block.type != Material.WORKBENCH) {
            return
        }

        val count = player.inventory.countItem {
            it.type == Material.WHEAT
        }

        if (count >= 64) {
            val result = count / 64
            val cost = result * 64
            player.inventory.takeItem(amount = cost) {
                it.type == Material.WHEAT
            }

            repeat(10) {
                val particlePlayLoc = block.location.clone()
                    .add(Random.nextDouble(-0.5, 0.5), Random.nextDouble(-0.5, 0.5), Random.nextDouble(-0.5, 0.5))
                particlePlayLoc.world.playEffect(particlePlayLoc, Effect.HAPPY_VILLAGER, 1, 1)
            }

            player.inventory.addItem(PackagedBale.toItemStack().apply {
                amount = result
            })

            player.playSound(block.location, Sound.ORB_PICKUP, 1f, 1f)
        }

    }

    override fun getInternalName(): String {
        return "wheat"
    }

    override fun getItemDisplayName(): String {
        return "&a小麦"
    }

    override fun getItemDisplayMaterial(): Material {
        return Material.WHEAT
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