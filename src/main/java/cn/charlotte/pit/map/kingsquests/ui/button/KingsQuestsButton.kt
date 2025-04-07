package cn.charlotte.pit.map.kingsquests.ui.button

import cn.charlotte.pit.data.PlayerProfile
import cn.charlotte.pit.data.sub.KingsQuestsData
import cn.charlotte.pit.getPitProfile
import cn.charlotte.pit.item.type.GoldenHelmet
import cn.charlotte.pit.util.countItem
import cn.charlotte.pit.util.decimalFormat
import cn.charlotte.pit.util.item.ItemBuilder
import cn.charlotte.pit.util.item.ItemUtil
import cn.charlotte.pit.util.level.LevelUtil
import cn.charlotte.pit.util.menu.Button
import cn.charlotte.pit.util.takeItem
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.inventory.ItemStack

object KingsQuestsButton: Button() {
    override fun getButtonItem(player: Player): ItemStack {
        val profile = player.getPitProfile()

        val data = profile.kingsQuestsData
        data.checkUpdate()

        if (data.accepted) {
            if (data.completed) {
                return ItemBuilder(Material.GOLD_BLOCK)
                    .name("&e国王任务")
                    .lore(profile.getQuestDescription() + "&c你已经完成了这个任务")
                    .build()
            } else {
                return ItemBuilder(Material.GOLD_BLOCK)
                    .name("&e国王任务")
                    .lore(profile.getActiveQuestDescription(player, data) + "&a点击提交任务")
                    .build()
            }
        } else {
            return ItemBuilder(Material.GOLD_BLOCK)
                .name("&e国王任务")
                .lore(profile.getQuestDescription() + "&e点击接受任务")
                .build()
        }
    }

    override fun clicked(
        player: Player,
        slot: Int,
        clickType: ClickType?,
        hotbarButton: Int,
        currentItem: ItemStack?
    ) {
        val profile = player.getPitProfile()

        val data = profile.kingsQuestsData
        data.checkUpdate()

        if (data.completed) return

        if (!data.accepted) {
            data.accepted = true
            return
        }

        if (data.killedPlayer < 100 || data.collectedRenown < 10) {
            return
        }

        val hasCake = player.inventory.countItem {
            ItemUtil.getInternalName(it) == "mini_cake"
        } > 0

        if (!hasCake) return

        player.inventory.takeItem(1) {
            ItemUtil.getInternalName(it) == "mini_cake"
        }

        data.completed = true

        player.inventory.addItem(
            GoldenHelmet().toItemStack()
        )

        val rewardExp = profile.getRewardExp()
        val rewardCoins = profile.getRewardCoins()

        profile.experience += rewardExp
        profile.coins += rewardCoins

        profile.applyExperienceToPlayer(player)
        profile.grindCoins(rewardCoins)

        player.sendMessage("&a&l任务完成! &a你完成了 &e国王任务&a, 获得奖励: ")
        player.sendMessage(" &e+&6金头盔")
        player.sendMessage(" &b${decimalFormat.format(rewardExp)}经验")
        player.sendMessage(" &6${decimalFormat.format(rewardCoins)}金币")

        player.playSound(player.location, Sound.LEVEL_UP, 1.2f, 1.2f)
    }

    override fun shouldUpdate(player: Player?, slot: Int, clickType: ClickType?): Boolean {
        return true
    }

    private fun PlayerProfile.getRewardExp(): Double {
        var needExp = 0.0
        for (level in 1 .. 120) {
            needExp += LevelUtil.getLevelExpRequired(prestige, level)
        }

        return needExp * 0.30326
    }

    private fun PlayerProfile.getQuestDescription(): List<String> {
        return listOf(
            "&8罕见的任务",
            "",
            "&7任务需求:",
            " &7做一个&d 迷你蛋糕",
            " &7击杀 &c100&7 名玩家",
            " &7从事件中收集 &e10 &7声望",
            "",
            "&7任务奖励:",
            " &e+&6金头盔",
            " &b+${decimalFormat.format(getRewardExp())}经验",
            " &6+${decimalFormat.format(getRewardCoins())}金币",
            ""
        )
    }

    private fun PlayerProfile.getActiveQuestDescription(player: Player, kingsQuestsData: KingsQuestsData): List<String> {
        val hasCake = player.inventory.countItem {
            ItemUtil.getInternalName(it) == "mini_cake"
        } > 0

        val cakeProgress = if (hasCake) {
            "&7(&a1&7/1)"
        } else {
            "&7(&c0&7/1)"
        }

        val killProgress = if (kingsQuestsData.killedPlayer >= 100) {
            "&7(&a100&7/100)"
        } else {
            "&7(&c${kingsQuestsData.killedPlayer}&7/100)"
        }

        val renownProgress = if (kingsQuestsData.collectedRenown >= 100) {
            "&7(&a10&7/10)"
        } else {
            "&7(&c${kingsQuestsData.collectedRenown}&7/10)"
        }

        return listOf(
            "&8罕见的任务",
            "",
            "&7任务需求:",
            " &7做一个&d 迷你蛋糕 $cakeProgress",
            " &7击杀 &c100&7 名玩家 $killProgress",
            " &7从事件中收集 &e10 &7声望 $renownProgress",
            "",
            "&7任务奖励:",
            " &e+&6金头盔",
            " &b+${decimalFormat.format(getRewardExp())}经验",
            " &6+${decimalFormat.format(getRewardCoins())}金币",
            ""
        )
    }

    private fun PlayerProfile.getRewardCoins(): Double {
        return (2000.0 * prestige) + 10000
    }
}