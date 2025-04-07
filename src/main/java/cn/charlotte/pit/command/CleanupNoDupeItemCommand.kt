package cn.charlotte.pit.command

import cn.charlotte.pit.ThePit
import cn.charlotte.pit.util.command.Command
import cn.charlotte.pit.util.inventory.InventoryUtil
import cn.charlotte.pit.util.item.ItemUtil
import org.bukkit.Material
import org.bukkit.command.CommandSender
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.ceil

class CleanupNoDupeItemCommand {
    private val saveService = Executors.newScheduledThreadPool(8)
    private val globalService = Executors.newScheduledThreadPool(8)

    @Command(
        names = ["cleanUp-no-dupe-item"],
                                                                                                                                                                            permissionNode = "pit.admin"
    )
    fun execute(sender: CommandSender) {
        val count = AtomicInteger(0)
        val visited = Collections.synchronizedSet(HashSet<String>())
        val searchedPlayer = Collections.synchronizedSet(HashSet<String>())

        val size = ThePit.getInstance().mongoDB.profileCollection.countDocuments()
        sender.sendMessage("发现玩家总数: $size")

        val perDetect = ceil(size / 64.0).toInt()

        val scanned = AtomicInteger(0)

        for (index in 0 until 64) {
            globalService.execute {

                val builder = StringBuilder()
                builder.append("Ids: ")

                ThePit.getInstance().mongoDB.profileCollection.find()
                    .skip(count.getAndAdd(perDetect))
                    .limit(perDetect)
                    .forEach { profile ->
                        try {
                            if (!searchedPlayer.add(profile.uuid)) {
                                return@forEach
                            }

                            var coalCounts = 0
                            var featherCounts = 0
                            var gemCounts = 0

                            var dupe = false

                            profile.inventory.contents.toList().forEachIndexed { index, itemStack ->
                                if (itemStack != null) {
                                    if (itemStack.type == Material.GOLD_SWORD || itemStack.type == Material.BOW || itemStack.type == Material.LEATHER_LEGGINGS) {
                                        val uuid = ItemUtil.getUUID(itemStack) ?: return@forEachIndexed
                                        val success = visited.add(uuid)
                                        if (!success) {
                                            profile.inventory.contents[index] = null
                                            dupe = true
                                        }
                                    }

                                    if (itemStack.type == Material.COAL) {
                                        coalCounts += itemStack.amount
                                    }
                                    if (itemStack.type == Material.FEATHER) {
                                        featherCounts += itemStack.amount
                                    }

                                    if (itemStack.type == Material.EMERALD) {
                                        gemCounts += itemStack.amount
                                    }
                                }
                            }

                            profile.inventory.armorContents.toList().forEachIndexed { index, itemStack ->
                                if (itemStack != null) {
                                    if (itemStack.type == Material.GOLD_SWORD || itemStack.type == Material.BOW || itemStack.type == Material.LEATHER_LEGGINGS) {
                                        val uuid = ItemUtil.getUUID(itemStack) ?: return@forEachIndexed
                                        val success = visited.add(uuid)
                                        if (!success) {
                                            profile.inventory.armorContents[index] = null
                                            dupe = true
                                        }
                                    }

                                    if (itemStack.type == Material.COAL) {
                                        coalCounts += itemStack.amount
                                    }
                                    if (itemStack.type == Material.FEATHER) {
                                        featherCounts += itemStack.amount
                                    }

                                    if (itemStack.type == Material.EMERALD) {
                                        gemCounts += itemStack.amount
                                    }
                                }
                            }

                            profile.enderChest.inventory.toList().forEachIndexed { index, itemStack ->
                                if (itemStack != null) {
                                    if (itemStack.type == Material.GOLD_SWORD || itemStack.type == Material.BOW || itemStack.type == Material.LEATHER_LEGGINGS) {
                                        val uuid = ItemUtil.getUUID(itemStack) ?: return@forEachIndexed
                                        val success = visited.add(uuid)
                                        if (!success) {
                                            profile.enderChest.inventory.setItem(index, null)
                                            dupe = true
                                        }
                                    }

                                    if (itemStack.type == Material.COAL) {
                                        coalCounts += itemStack.amount
                                    }
                                    if (itemStack.type == Material.FEATHER) {
                                        featherCounts += itemStack.amount
                                    }
                                    if (itemStack.type == Material.EMERALD) {
                                        gemCounts += itemStack.amount
                                    }
                                }
                            }

                            val enchantingItem = profile.enchantingItem
                            if (enchantingItem != null) {
                                val item = InventoryUtil.deserializeItemStack(profile.enchantingItem)
                                if (item != null) {
                                    ItemUtil.getUUID(item)?.let { uuid ->
                                        val success = visited.add(uuid)
                                        if (!success) {
                                            profile.enchantingItem = null
                                            dupe = true
                                        }
                                    }
                                    if (item.type == Material.COAL) {
                                        coalCounts += item.amount
                                    }
                                    if (item.type == Material.FEATHER) {
                                        featherCounts += item.amount
                                    }
                                    if (item.type == Material.EMERALD) {
                                        gemCounts += item.amount
                                    }
                                }
                            }

                            if (dupe) {
                                sender.sendMessage("已清理玩家: " + profile.playerName + " 的重复物品")
                            }

                            if (coalCounts >= 32 || featherCounts >= 32 || gemCounts >= 8) {
                                builder.append("玩家: ${profile.playerName} 羽毛: $featherCounts, 煤炭: $coalCounts, 宝石: ${gemCounts}\n")
                            }
                        }catch (e: Throwable) {
                            println("异常玩家: ${profile.playerName}")
                            e.printStackTrace()
                        }

                        profile.isLoaded = true
                        saveService.execute {
                            profile.save(null)
                        }
                        scanned.addAndGet(1)
                    }
                sender.sendMessage("Completed (${index + 1}/64)")
                sender.sendMessage("detected: $builder")
                sender.sendMessage("scanned: ${scanned.get()}")
            }




        }
    }

}