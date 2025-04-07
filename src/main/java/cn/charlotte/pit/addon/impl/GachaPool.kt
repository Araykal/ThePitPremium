package cn.charlotte.pit.addon.impl

import cn.charlotte.pit.ThePit
import cn.charlotte.pit.addon.Addon
import cn.charlotte.pit.data.PlayerProfile
import cn.charlotte.pit.util.MythicUtil
import cn.charlotte.pit.util.chat.CC
import cn.charlotte.pit.util.findItem
import cn.charlotte.pit.util.item.ItemBuilder
import cn.charlotte.pit.util.menu.Button
import cn.charlotte.pit.util.menu.Menu
import cn.charlotte.pit.util.menu.buttons.DisplayButton
import cn.charlotte.pit.util.random.RandomUtil
import cn.charlotte.pit.util.toMythicItem
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.mongodb.client.model.Filters
import com.mongodb.client.model.ReplaceOptions
import org.bson.UuidRepresentation
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable
import org.mongojack.DBQuery
import org.mongojack.JacksonMongoCollection
import sinc.Native
import java.io.File
import java.util.*
import kotlin.random.Random

/**
 * @Author Misoryan
 * @Date 2023/09/19 22:32
 */
@Native
object GachaPool : Addon {

    val rewards = ArrayList<Reward>()

    val keysCollections by lazy {
        JacksonMongoCollection.builder()
            .build(ThePit.getInstance().mongoDB.database.getCollection("gacha", GachaData::class.java), GachaData::class.java, UuidRepresentation.JAVA_LEGACY);
    }

    var enable = false

    var config = YamlConfiguration().apply {
        set("武器A", YamlConfiguration().also {
            it.set("chance", 10.0)
            it.set("pane_durability", 1)
            it.set("type", "equip")
            it.set("item", "mythic_sword")
            it.set("amount", 1)
            it.set("enchant", "赏金猎人-1;处决-1")
            it.set("tier", "3")
        })

        set("武器B", YamlConfiguration().also {
            it.set("chance", 10.0)
            it.set("pane_durability", 1)
            it.set("amount", 1)
            it.set("type", "equip")
            it.set("item", "mythic_sword")
        })
    }

    val rolling = mutableListOf<UUID>()

    class Preview(): Menu() {
        override fun getTitle(player: Player?): String {
            return "预览"
        }

        override fun getButtons(player: Player?): MutableMap<Int, Button> {
            val map = HashMap<Int, Button>()
            rewards.forEachIndexed { index, reward ->
                map[index] = object :Button() {
                    override fun getButtonItem(player: Player?): ItemStack {
                        return ItemBuilder(reward.item).build()
                    }

                    override fun clicked(                        player: Player?,
                        slot: Int,
                        clickType: ClickType?,
                        hotbarButton: Int,
                        currentItem: ItemStack?
                    ) {

                    }

                }
            }


            return map
        }

    }

    override fun name(): String {
        return "gacha_pool"
    }

/*    override fun enableList(): Set<String> {
        return setOf(
            "d8157845-8e44-4c83-ab53-60bb7abc3a08",
            "a41a5f6e-fcdc-443c-aaa2-8eb6567863d8",
            "c95d33ac4-8667-2259-1380-ab51bf2e84b"
        )
    }*/

    override fun enable() {
        val file = File(ThePit.getInstance().dataFolder, "gacha.yml")
        if (file.exists()) {
            config = YamlConfiguration.loadConfiguration(file)
        } else {
            config.save(file)
        }

        config.getKeys(false).forEach { item ->
            val section = config.getConfigurationSection(item)!!
            val chance = section.getDouble("chance")
            val paneDurability = section.getInt("pane_durability")
            val itemName = section.getString("item")
            val amount = section.getInt("amount")

            var findItem = itemName.findItem(amount)
            if (findItem == null) {
                println("$itemName not found!")
                return@forEach
            }

            val mythicItem = findItem.toMythicItem()
            if (mythicItem != null) {
                section.getString("enchant")?.let { enchantString ->
                    val enchants = enchantString.split(";")
                    enchants.map { enchant ->
                        val split = enchant.split("-")
                        ThePit.getInstance().enchantmentFactor.enchantments.first() {
                            it.enchantName == split[0]
                        } to split[1].toInt()
                    }
                }?.forEach {
                    mythicItem.enchantments[it.first] = it.second
                }

                val tier = section.getInt("tier")
                if (tier != 0) {
                    mythicItem.tier = tier
                }
                if (mythicItem.isEnchanted) {
                    mythicItem.maxLive = RandomUtil.random.nextInt(5) + 11
                    mythicItem.live = mythicItem.maxLive
                }

                findItem = mythicItem.toItemStack()
            }

            rewards += Reward(item, chance, findItem!!, paneDurability.toByte(), section.getBoolean("rare", false))
        }

        enable = true
    }

    fun gacha(player: Player?): Boolean {
        if (!enable) {
            return false
        }

        if (player == null) return false

        val data = keysCollections.findOne(DBQuery.`is`("playerName", player.name))
        if (data == null) {
            player.sendMessage(CC.translate("&c你的钥匙不足"))
            return false
        }
        if (data.keys <= 0) {
            player.sendMessage(CC.translate("&c你的钥匙不足"))
            return false
        }

        if (rolling.contains(player.uniqueId)) return false
        rolling.add(player.uniqueId)

        data.keys--
        keysCollections.replaceOne(Filters.eq("playerName", player.name), data, ReplaceOptions().upsert(true))

        val menu = GachaMenu()
        menu.openMenu(player)
        menu.gacha(player)
        return true
    }

    class Reward(val displayName: String, val chance: Double, val item: ItemStack, val durability: Byte, var rare: Boolean = false)

    class GachaMenu : Menu() {

        private var count = 0

        val total = 108

        val rewardList = selectRewards(rewards, total + 8).toMutableList()

        private fun selectRewards(rewards: List<Reward>, n: Int): List<Reward> {
            val totalWeight = rewards.sumOf { it.chance }
            val selectedRewards = mutableListOf<Reward>()
            repeat(n) {
                var randomValue = Random.nextDouble(0.0, totalWeight)
                var selectedReward: Reward? = null
                for (reward in rewards) {
                    randomValue -= reward.chance
                    if (randomValue <= 0) {
                        selectedReward = reward
                        break
                    }
                }
                selectedReward?.let { selectedRewards.add(it) }
            }
            return selectedRewards
        }

        override fun getTitle(player: Player?): String {
            return "Gacha"
        }

        override fun getButtons(player: Player): MutableMap<Int, Button> {
            val buttonMap = mutableMapOf<Int, Button>()
            (0 .. 8).forEach {
                if (it == 4) {
                    buttonMap[it] = DisplayButton(
                        ItemBuilder(Material.SKULL_ITEM)
                            .durability(3)
                            .setSkullProperty("ewogICJ0aW1lc3RhbXAiIDogMTczNzQ0OTEyMDM1MiwKICAicHJvZmlsZUlkIiA6ICJmMTA0NzMxZjljYTU0NmI0OTkzNjM4NTlkZWY5N2NjNiIsCiAgInByb2ZpbGVOYW1lIiA6ICJ6aWFkODciLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTkyNDA2MjljZWVlMzQ4Yjk4MjI0ZjBiMWMyZjJhNGUzN2NlMjY0Mjc5MzQ5OGRmMTZhMDA2OTk1ZGNmYTU2NSIsCiAgICAgICJtZXRhZGF0YSIgOiB7CiAgICAgICAgIm1vZGVsIiA6ICJzbGltIgogICAgICB9CiAgICB9CiAgfQp9")
                            .build(),
                        true
                    )
                    buttonMap[it + 18] = DisplayButton(
                        ItemBuilder(Material.SKULL_ITEM)
                            .durability(3)
                            .setSkullProperty("ewogICJ0aW1lc3RhbXAiIDogMTYyODMzNjQ0NDkxOCwKICAicHJvZmlsZUlkIiA6ICI5MWZlMTk2ODdjOTA0NjU2YWExZmMwNTk4NmRkM2ZlNyIsCiAgInByb2ZpbGVOYW1lIiA6ICJoaGphYnJpcyIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS8zYmQ2ZDE5NzkyZjAyYTIxNDgzZWFmOGJmNmQ0MzE5OWQxMjkzOGRjNTUwM2RjZTg3OTgzNzZlMWE1MjYwNDZkIgogICAgfQogIH0KfQ==")
                            .build(),
                        true
                    )
                } else {
                    buttonMap[it] = Button.placeholder(Material.STAINED_GLASS_PANE, rewardList[count + it].durability, " ")
                    buttonMap[it + 18] = Button.placeholder(Material.STAINED_GLASS_PANE, rewardList[count + it].durability, " ")
                }

                buttonMap[it + 9] = DisplayButton(rewardList[count + it].item, true)
            }
            return buttonMap
        }

        override fun onClose(player: Player) {
            giveReward(player)
            super.onClose(player)
        }

        fun giveReward(player: Player) {
            if (!rolling.contains(player.uniqueId)) return
            rolling.remove(player.uniqueId)
            val reward = rewardList[104]
            val resultItem = MythicUtil.getMythicItem(reward.item)?.let {
                it.uuid = UUID.randomUUID()
                it.toItemStack()
            } ?: run {
                reward.item
            }

            player.inventory.addItem(resultItem)
            player.sendMessage(CC.translate("&a你抽到了 &e${reward.displayName} &a!"))

            val profile = PlayerProfile.getPlayerProfileByUuid(player.uniqueId)

            if (reward.rare) {
                for (onlinePlayer in Bukkit.getOnlinePlayers()) {

                    onlinePlayer.sendTitle(CC.translate("&d幸运星"), CC.translate("&e${profile.formattedNameWithRoman} &a抽到了 &e${reward.displayName}"))
                }
            }
        }

        fun gacha(player: Player) {
            object : BukkitRunnable() {

                var tick = 0
                override fun run() {
                    tick++
                    when (count) {
                        in 50 .. 80 -> {
                            if (tick % 2 != 0) {
                                return
                            }
                        }
                        in 80 .. 97 -> {
                            if (tick % 4 != 0) {
                                return
                            }
                        }
                        98 -> {
                            if (tick % 10 != 0) {
                                return
                            }
                        }
                        99 -> {
                            if (tick % 15 != 0) {
                                return
                            }
                        }
                        100 -> {
                            if (tick % 20 != 0) {
                                return
                            }
                        }
                    }
                    if (currentlyOpenedMenus.containsValue(this@GachaMenu)) {
                        openMenu(player)
                    } else {
                        cancel()
                        return
                    }
                    if (count + 8 >= total) {
                        this.cancel()
                        giveReward(player)
                        return
                    }
                    count++
                }
            }.runTaskTimer(ThePit.getInstance(), 1, 1)
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    class GachaData {
        lateinit var playerName: String
        var keys = 0
    }
}