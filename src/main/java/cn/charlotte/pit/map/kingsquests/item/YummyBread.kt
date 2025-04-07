package cn.charlotte.pit.map.kingsquests.item

import cn.charlotte.pit.item.AbstractPitItem
import cn.charlotte.pit.util.PlayerUtil
import cn.charlotte.pit.util.chat.CC
import cn.charlotte.pit.util.countItem
import cn.charlotte.pit.util.hologram.HologramAPI
import cn.charlotte.pit.util.item.ItemBuilder
import cn.charlotte.pit.util.item.ItemUtil
import cn.charlotte.pit.util.submit
import cn.charlotte.pit.util.takeItem
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.block.Furnace
import org.bukkit.entity.Chicken
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack

object YummyBread: AbstractPitItem(), Listener {

    @EventHandler
    fun onInteractBlock(event: PlayerInteractEvent) {
        val player = event.player
        val block = event.clickedBlock ?: return

        if (event.action != Action.RIGHT_CLICK_BLOCK) {
            return
        }

        if (block.type == Material.FURNACE || block.type == Material.BURNING_FURNACE) {
            event.isCancelled = true
        }

        if (block.type != Material.FURNACE) {
            return
        }

        val count = player.inventory.countItem {
            it.type == Material.WHEAT
        }

        if (count < 64) return

        player.inventory.takeItem(64) {
            it.type == Material.WHEAT
        }

        val furnace = block.state as Furnace
        furnace.burnTime = 9999
        furnace.update()

//        block.type = Material.BURNING_FURNACE

        val vector = player.location.direction.clone().multiply(-1).apply {
            y = 0.0
        }

        val hologramLoc = block.location.add(vector)

        val hologram = HologramAPI.createHologram(
            hologramLoc, CC.translate("&a10")
        )

        hologram.spawn()

        var countdown = 10

        submit(period = 20L) {
            if (!player.isOnline) {
                hologram.deSpawn()
                cancel()
                block.type = Material.FURNACE
                furnace.burnTime = 0
                furnace.cookTime = 0

                furnace.update()
                return@submit
            }

            countdown--
            if (countdown <= 0) {
                player.inventory.addItem(toItemStack())
                player.playSound(block.location, Sound.ORB_PICKUP, 1f, 1f)
                hologram.deSpawn()
                block.type = Material.FURNACE
                furnace.burnTime = 0
                furnace.cookTime = 0

                furnace.update()
                cancel()
                return@submit
            }

            hologram.text = CC.translate(
                when(countdown) {
                    in 0 .. 3 -> {
                        "&c${countdown}"
                    }
                    in 4 .. 7 -> {
                        "&e${countdown}"
                    }
                    else -> "&a${countdown}"
                }
            )
        }
    }

    @EventHandler
    fun onInteract(event: PlayerInteractEntityEvent) {
        val player = event.player
        val entity = event.rightClicked ?: return
        val itemInHand = player.itemInHand

        if (entity !is Chicken) return

        if (ItemUtil.getInternalName(itemInHand) == "yummy_bread") {
            // 美味面包右键鸡
            PlayerUtil.takeOneItemInHand(player)
            player.inventory.addItem(HighGradeEggs.toItemStack())
        }
    }

    @EventHandler
    fun onInteract(event: PlayerInteractEvent) {
        val player = event.player
        val item = event.item ?: return

        if (event.action == Action.RIGHT_CLICK_BLOCK || event.action == Action.RIGHT_CLICK_AIR) {
            if (ItemUtil.getInternalName(item) == "yummy_bread") {
                PlayerUtil.takeOneItemInHand(player)
                PlayerUtil.heal(player, 1.0)
                player.playSound(player.location, Sound.EAT, 1f, 1f)
            }
        }
    }

    override fun getInternalName(): String {
        return "yummy_bread"
    }

    override fun getItemDisplayName(): String {
        return "&e美味的面包"
    }

    override fun getItemDisplayMaterial(): Material {
        return Material.BREAD
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