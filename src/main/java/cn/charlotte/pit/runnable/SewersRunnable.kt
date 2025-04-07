package cn.charlotte.pit.runnable

import cn.charlotte.pit.ThePit
import cn.charlotte.pit.getPitProfile
import cn.charlotte.pit.util.RandomList
import cn.charlotte.pit.util.chat.CC
import cn.charlotte.pit.util.item.ItemBuilder
import org.bukkit.Effect
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.scheduler.BukkitRunnable
import kotlin.random.Random

object SewersRunnable: BukkitRunnable(), Listener {

    private var existSewersChest: Location? = null

    private var lastClaimed = -1L

    private val randomList = RandomList(
        "xp" to 100,
        "gold" to 100,
        "diamond_chestplate" to 50,
        "diamond_leggings" to 50,
        "diamond_boots" to 50,
        "rubbish" to 50,
        "milk_buckets" to 10,
    )

    override fun run() {
        val locs = ThePit.getInstance().pitConfig.sewersChestsLocations
        if (locs.isEmpty()) {
            return
        }

        if (existSewersChest != null) {
            val location = existSewersChest ?: return

            repeat(10) {
                val particleLoc = location.clone().add(
                    Random.nextDouble(-0.5, 0.5),
                    Random.nextDouble(-0.5, 0.5),
                    Random.nextDouble(-0.5, 0.5)
                )

                particleLoc.world.playEffect(
                    particleLoc,
                    Effect.HAPPY_VILLAGER,
                    1, 1
                )
            }
            return
        }

        if (System.currentTimeMillis() - lastClaimed < 1000 * 20) {
            return
        }

        val location = locs.random()
        existSewersChest = location
        val block = location.block
        block.type = Material.CHEST
    }

    @EventHandler
    fun e(e: PlayerInteractEvent) {
        if (e.action != Action.RIGHT_CLICK_BLOCK) {
            return
        }

        val player = e.player
        if (e.clickedBlock.location == existSewersChest) {
            claim(player)
        }
    }

    private fun claim(player: Player) {
        existSewersChest?.block?.type = Material.AIR
        existSewersChest = null
        lastClaimed = System.currentTimeMillis()

        val profile = player.getPitProfile()

        val id = randomList.random() ?: return
        player.playSound(player.location, Sound.LEVEL_UP, 1f, 1f)
        player.sendMessage(CC.translate("&9下水道! &7你领取了下水道奖励."))
        when(id) {
            "xp" -> {
                profile.experience += 100
                profile.applyExperienceToPlayer(player)
            }
            "gold" -> {
                profile.coins += 200
                profile.grindCoins(200.0)
            }
            "diamond_chestplate" -> {
                player.inventory.addItem(
                    ItemBuilder(Material.DIAMOND_CHESTPLATE)
                        .deathDrop(true)
                        .canSaveToEnderChest(true)
                        .canTrade(true)
                        .internalName("shopItem")
                        .buildWithUnbreakable()
                )
            }
            "diamond_leggings" -> {
                ItemBuilder(Material.DIAMOND_LEGGINGS)
                    .deathDrop(true)
                    .canSaveToEnderChest(true)
                    .canTrade(true)
                    .internalName("shopItem")
                    .buildWithUnbreakable()
            }
            "diamond_boots" -> {
                ItemBuilder(Material.DIAMOND_BOOTS)
                    .deathDrop(true)
                    .canSaveToEnderChest(true)
                    .canTrade(true)
                    .internalName("shopItem")
                    .buildWithUnbreakable()
            }
            "rubbish" -> {
                player.inventory.addItem(
                    ItemBuilder(Material.INK_SACK)
                        .name("&9下水道废弃物")
                        .durability(15)
                        .deathDrop(false)
                        .canSaveToEnderChest(true)
                        .canTrade(true)
                        .lore(
                            "&7死亡后保留",
                            "&7使用 64 个废弃物可以找 &9下水道鱼&7",
                            "&7换取稀有护甲"
                        )
                        .internalName("rubbish")
                        .build()
                )
            }
            "milk_buckets" -> {
                player.inventory.addItem(
                    ItemBuilder(Material.MILK_BUCKET)
                        .deathDrop(false)
                        .canSaveToEnderChest(true)
                        .canDrop(false)
                        .canTrade(true)
                        .lore(
                            "&7死亡后保留",
                            "",
                            "&a生命恢复 I(2:00)",
                            "&7补钙"
                        )
                        .internalName("milk_bucket")
                        .build()
                )
            }

        }
    }
}