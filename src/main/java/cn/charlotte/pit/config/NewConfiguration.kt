package cn.charlotte.pit.config

import cn.charlotte.pit.ThePit
import cn.charlotte.pit.enchantment.type.rare.ThePunchEnchant
import cn.charlotte.pit.item.MythicColor
import cn.charlotte.pit.listener.CombatListener
import cn.charlotte.pit.menu.prestige.button.PrestigeStatusButton
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import java.io.File
import java.util.*

object NewConfiguration {

    var vipPrice = 500
    var priceName = "点券"
    var lobbyCommand = "spb connect lobby"

    var dateFormat = "MM/dd HH:mm"

    var noobProtect = true
    var noobProtectLevel = 120
    var noobDamageBoost = 1.1
    var noobDamageReduce = 0.9

    var luckGem = 0.30;
    var customChatFormatEnable = false
    var customChatFormat = "%pit_level_tag_roman% %s&f: %s"

    var scoreboardShowtime = true

    var pitSupportPermission = "pit.vip"

    var removeSupportWhenNoPermission = false

    val eventOnlineRequired = HashMap<String, Int>()

    val mythicMobs = HashMap<String, MythicMobsConf>()

    var kingsQuestsMarker: UUID = UUID.randomUUID()

    var waterMark: String = "&eThePitPremium &7| &b2025-Future"

    var botName: String = "&cBot"

    lateinit var config: YamlConfiguration

    private val rareRate = HashMap<MythicColor, MutableList<Rate>>()

    private val mythicChance = ArrayList<Pair<String, Double>>()

    fun save() {
        config.save(File(ThePit.getInstance().dataFolder, "custom.yml"))
    }

    fun loadFile() {
        val file = File(ThePit.getInstance().dataFolder, "custom.yml")
        config = YamlConfiguration.loadConfiguration(file)
    }

    fun load() {
        refreshAndSave()

        vipPrice = config.getInt("vip-price", 500)
        priceName = config.getString("price-name", "点券")
        lobbyCommand = config.getString("lobby-command", "hub")

        dateFormat = config.getString("dateFormat", "MM/dd HH:mm")

        noobProtect = config.getBoolean("noob-protect.enable")
        noobProtectLevel = config.getInt("noob-protect.level")
        noobDamageBoost = config.getDouble("noob-protect.damage_boost")
        noobDamageReduce = config.getDouble("noob-protect.damage_reduce")

        CombatListener.eventBoost = config.getDouble("booster")

        luckGem = config.getDouble("luck-gem", 0.30)
        waterMark = config.getString("water-mark")
        botName = config.getString("bot-name")
        customChatFormatEnable = config.getBoolean("custom-chat-format.enable", false)
        customChatFormat = config.getString("custom-chat-format.format", "%pit_level_tag_roman% %s&f: %s")

        PrestigeStatusButton.limit = config.getInt("highest-prestige")

        config.getConfigurationSection("event-online-required")?.let {
            it.getKeys(false).forEach { eventName ->
                eventOnlineRequired[eventName] = it.getInt(eventName)
            }
        }

        ThePunchEnchant.PUNCH_Y = config.getDouble("punch_y", 4.0)

        pitSupportPermission = config.getString("pitSupportPermission", pitSupportPermission)
        removeSupportWhenNoPermission = config.getBoolean("removeSupportWhenNoPermission", false)

        scoreboardShowtime = config.getBoolean("scoreboard-showtime")

        mythicChance.clear()
        config.getConfigurationSection("mythicDropChance")!!.let {
            for (groupName in it.getKeys(false)) {
                val permission = it.getString("${groupName}.test")!!
                val chance = it.getDouble("${groupName}.value", 0.005)

                mythicChance.add(permission to chance)
            }
        }
        mythicChance.sortByDescending {
            it.second
        }

        if (config.getString("kingsQuestsMarker") != null) {
            kingsQuestsMarker = UUID.fromString(config.getString("kingsQuestsMarker"))
        } else {
            config.set("kingsQuestsMarker", kingsQuestsMarker.toString())
            save()
        }

        config.getConfigurationSection("mm-listener")?.let {
            for (mmid in it.getKeys(false)) {
                val xpRange = it.getString("$mmid.xp-provide")?.let { rangeString ->
                    val split = rangeString.split("-")
                    split.first().toInt()..split.last().toInt()
                } ?: continue
                val coinsRange = it.getString("$mmid.coins-provide")?.let { rangeString ->
                    val split = rangeString.split("-")
                    split.first().toInt()..split.last().toInt()
                } ?: continue
                val killMessage = it.getString("$mmid.kill-message")
                val killBroadcast = it.getBoolean("$mmid.kill-broadcast.enable")
                val killBroadcastMessage = it.getString("$mmid.kill-broadcast.message")
                mythicMobs[mmid] = MythicMobsConf(xpRange, coinsRange, killMessage, killBroadcast, killBroadcastMessage)
            }
        }

        rareRate.clear()

        rareRate[MythicColor.DARK] = ArrayList<Rate>().apply {
            val darkRateSection = config.getConfigurationSection("rate.dark")

            for (key in darkRateSection.getKeys(false)) {
                val permission = darkRateSection.getString("${key}.test", "pit.${key}")
                val chance = darkRateSection.getDouble("${key}.value", 0.02)

                this += Rate(permission, chance)
            }
        }

        rareRate[MythicColor.ORANGE] = ArrayList<Rate>().apply {
            val normalSection = config.getConfigurationSection("rate.normal")

            for (key in normalSection.getKeys(false)) {
                val permission = normalSection.getString("${key}.test")
                val chance = normalSection.getDouble("${key}.value")

                if (permission == null) {
                    continue
                }

                this += Rate(permission, chance)
            }
        }

        rareRate[MythicColor.RAGE] = ArrayList<Rate>().apply {
            val rageSection = config.getConfigurationSection("rate.rage")

            for (key in rageSection.getKeys(false)) {
                val permission = rageSection.getString("${key}.test", "pit.${key}")
                val chance = rageSection.getDouble("${key}.value", 0.02)

                this += Rate(permission, chance)
            }
        }

    }

    fun Player.getMythicDropChance(): Double {
        if (mythicChance.isEmpty()) return 0.005
        for ((permission, chance) in mythicChance) {
            if (hasPermission(permission)) return chance
        }

        return 0.005
    }

    fun getChance(player: Player, color: MythicColor): Double {
        val list = when (color) {
            MythicColor.DARK, MythicColor.RAGE -> {
                rareRate[color]
            }

            else -> {
                rareRate[MythicColor.ORANGE]
            }
        }
        if (player.hasMetadata("lucky")) {
            return luckGem
        }

        if (list.isNullOrEmpty()) {
            return 0.02
        }

        return list.filter {
            player.hasPermission(it.permission)
        }.maxByOrNull {
            it.value
        }?.value ?: 0.005
    }

    private fun refreshAndSave() {
        defaults.forEach {
            if (config.get(it.key) == null) {
                config.set(it.key, it.value)
            }
        }

        config.save(File(ThePit.getInstance().dataFolder, "custom.yml"))
    }

    private data class Rate(
        val permission: String,
        val value: Double
    )

    class MythicMobsConf(
        val expRange: IntRange,
        val coinsRange: IntRange,
        val killMessage: String,
        val killBroadcast: Boolean,
        val killBroadcastMessage: String
    )

    private val defaults = mapOf(
        "water-mark" to "&eThePitPremium &7| &b2025-Future",
        "bot-name" to "&cBot",
        "vip-price" to 500,
        "price-name" to "点券",
        "lobby-command" to "hub",


        "coins-boost" to 2.0,
        "luck-gem" to 0.30,

        "dateFormat" to "MM/dd HH:mm",

        "pitSupportPermission" to pitSupportPermission,
        "removeSupportWhenNoPermission" to false,

        //皮肤相关
        "GenesisAngel-npc-skin" to "ewogICJ0aW1lc3RhbXAiIDogMTYxMTcxMzEzMTMyOCwKICAicHJvZmlsZUlkIiA6ICJiMGQ3MzJmZTAwZjc0MDdlOWU3Zjc0NjMwMWNkOThjYSIsCiAgInByb2ZpbGVOYW1lIiA6ICJPUHBscyIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS8xOTA3MjdjNjNkMmQ3MjUwZTQ1NTA4NTBiMmQ0YTdlMTEwZDFkMzliNjhmYjcwMmRkYjkzYmIwYjJlZjg0ZCIsCiAgICAgICJtZXRhZGF0YSIgOiB7CiAgICAgICAgIm1vZGVsIiA6ICJzbGltIgogICAgICB9CiAgICB9CiAgfQp9",
        "GenesisDemon-npc-skin" to "ewogICJ0aW1lc3RhbXAiIDogMTY1NzU5OTQzODI2MCwKICAicHJvZmlsZUlkIiA6ICJmZTYxY2RiMjUyMTA0ODYzYTljY2E2ODAwZDRiMzgzZSIsCiAgInByb2ZpbGVOYW1lIiA6ICJNeVNoYWRvd3MiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDAyMWRiYjc3MzdiZDM1MjM0NDRkNTc3NjBlMWY2MzkzOGVlMTI4NjA4MDM4OTU1M2IzYTY4M2VlOGEzYjkwYiIKICAgIH0KICB9Cn0=",
        "keeper-npc-skin" to "eyJ0aW1lc3RhbXAiOjE1ODE5MTIzMjQ4MzQsInByb2ZpbGVJZCI6IjgyYzYwNmM1YzY1MjRiNzk4YjkxYTEyZDNhNjE2OTc3IiwicHJvZmlsZU5hbWUiOiJOb3ROb3RvcmlvdXNOZW1vIiwic2lnbmF0dXJlUmVxdWlyZWQiOnRydWUsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS83NzI4MWEwZDVkOWY3OGU4Y2FlOTlmMGVhNDExNDhkYmQ2YjJkZTAyNmEzYzc5NTgyMzg4NjMyMGJhNWVkMDI0In19fQ==",
        "mail-npc-skin" to "ewogICJ0aW1lc3RhbXAiIDogMTY0MjQ0OTExOTcxNCwKICAicHJvZmlsZUlkIiA6ICJkODAwZDI4MDlmNTE0ZjkxODk4YTU4MWYzODE0Yzc5OSIsCiAgInByb2ZpbGVOYW1lIiA6ICJ0aGVCTFJ4eCIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9kNDkwZjY2OGU4ZGY0YzliZDMyODVjMmJiNWU0NWU0YWZlYWZiYzhkZWQ0Y2VkZWQzMzU0MmNjZTgyODVmMzM1IgogICAgfQogIH0KfQ==",
        "perk-npc-skin" to "ewogICJ0aW1lc3RhbXAiIDogMTY2OTQ2NDc3NDI4MCwKICAicHJvZmlsZUlkIiA6ICJmODFhNzJhZWZjMjY0MjU0YTQ5NzE0OWYzMjJiZjJlNSIsCiAgInByb2ZpbGVOYW1lIiA6ICJEZXJsYW5fODgiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGFmMmQ1ZjgzZjIyNGU0ODA0NjgwZTBjMzNlNGEyZWNjNTk2ZmYyYjBjNzFlMDY2ODgxNmJhNDI5MTJhYzQyZiIKICAgIH0KICB9Cn0=",
        "prestige-npc-skin" to "ewogICJ0aW1lc3RhbXAiIDogMTYxNDE5OTYwMzQwOCwKICAicHJvZmlsZUlkIiA6ICJiNzQ3OWJhZTI5YzQ0YjIzYmE1NjI4MzM3OGYwZTNjNiIsCiAgInByb2ZpbGVOYW1lIiA6ICJTeWxlZXgiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGRlOTgyMDAzNTBkMjM4ZjJjNjBhYWI5MmE0NmM2ZTY1ODc5ZWE1ZWE3OWExMGJiZmU1NjZhNTg5MWUwNDNiOSIKICAgIH0KICB9Cn0=",
        "quest-npc-skin" to "ewogICJ0aW1lc3RhbXAiIDogMTU5OTIxNzI3NjA5NywKICAicHJvZmlsZUlkIiA6ICJkNjBmMzQ3MzZhMTI0N2EyOWI4MmNjNzE1YjAwNDhkYiIsCiAgInByb2ZpbGVOYW1lIiA6ICJCSl9EYW5pZWwiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmM4Yjc5N2M1NjQ4YzQwNDFkNWE0ZTYwYTY1OGMxMjAzMGJiZGQ3OTM4NWRjMzA4NGRlZmVkYzBjZmQ1MmZjNSIKICAgIH0KICB9Cn0=",
        "shop-npc-skin" to "ewogICJ0aW1lc3RhbXAiIDogMTY0NTk4MDY0NjU4NywKICAicHJvZmlsZUlkIiA6ICI1MjhlYzVmMmEzZmM0MDA0YjYwY2IwOTA5Y2JiMjdjYiIsCiAgInByb2ZpbGVOYW1lIiA6ICJQdWxpenppIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2NmNzhlNzVkZjlkMjg4YjM5ZDRlMDFjNzEwYjRlZjNhYmI2NDQwNGVmZDU2YWQ5OTE1ZTU5ZWVhMTI2ZWNlMGUiCiAgICB9CiAgfQp9",
        "status-npc-skin" to "ewogICJ0aW1lc3RhbXAiIDogMTY0ODY3MjI4MTczMiwKICAicHJvZmlsZUlkIiA6ICI1MjhlYzVmMmEzZmM0MDA0YjYwY2IwOTA5Y2JiMjdjYiIsCiAgInByb2ZpbGVOYW1lIiA6ICJQdWxpenppIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2MxNTIxYTAyNTY2ODgxYjIwNTA4Mzg5YjM4MmEzMDIzMWE4NDViYTc0OWEyN2QxNDQ5NTUyNzQ3NDgxY2Y4YjkiLAogICAgICAibWV0YWRhdGEiIDogewogICAgICAgICJtb2RlbCIgOiAic2xpbSIKICAgICAgfQogICAgfQogIH0KfQ==",

        //新手保护
        "noob-protect.enable" to true,
        "noob-protect.level" to 120,
        "noob-protect.damage_boost" to 1.1,
        "noob-protect.damage_reduce" to 0.9,

        //mm兼容
        "mm-listener.mm_mob_a.xp-provide" to "10-100",
        "mm-listener.mm_mob_a.coins-provide" to "10-100",
        "mm-listener.mm_mob_a.kill-message" to "&c&l击杀! &a成功击杀{mobName} 获得 {coin} {exp}",
        "mm-listener.mm_mob_a.kill-broadcast.enable" to true,
        "mm-listener.mm_mob_a.kill-broadcast.message" to "&c&l击杀! &a成功击杀{mobName} 获得 {coin} {exp}",

        //自定义聊天格式
        "custom-chat-format.enable" to false,
        "custom-chat-format.format" to "%pit_genesis_tag%%pit_level_tag_roman% %s&f: %s",

        //自定义活动人数需求
        "event-online-required.hunt" to 3,
        "event-online-required.patronage" to 5,
        "event-online-required.dragon_egg" to 10,
        "event-online-required.koth" to 10,
        "event-online-required.auction" to 10,
        "event-online-required.cake" to 10,
        "event-online-required.care_package" to 20,
        "event-online-required.everyone_bounty_event" to 10,
        "event-online-required.quick_math_event" to 5,
        "event-online-required.ham" to 20,
        "event-online-required.rage_pit" to 20,
        "event-online-required.red_vs_blue" to 20,
        "event-online-required.spire" to 20,
        "event-online-required.block_head" to 999,
        "event-online-required.squads" to 999,

        "highest-prestige" to 35,

        "scoreboard-showtime" to true,

        "rate.dark.vip1.test" to "pit.vip1",
        "rate.dark.vip1.value" to 0.08,
        "rate.dark.vip2.test" to "pit.vip2",
        "rate.dark.vip2.value" to 0.04,
        "rate.dark.default.value" to 0.02,

        "rate.normal.vip1.test" to "pit.vip1",
        "rate.normal.vip1.value" to 0.08,
        "rate.normal.vip2.test" to "pit.vip2",
        "rate.normal.vip2.value" to 0.04,
        "rate.normal.default.value" to 0.02,

        "rate.rage.vip1.test" to "pit.vip1",
        "rate.rage.vip1.value" to 0.08,
        "rate.rage.vip2.test" to "pit.vip2",
        "rate.rage.vip2.value" to 0.04,
        "rate.rage.default.value" to 0.02,

        "mythicDropChance.vip1.test" to "permission.vip1",
        "mythicDropChance.vip1.value" to 0.01,
        "mythicDropChance.vip2.test" to "permission.vip2",
        "mythicDropChance.vip2.value" to 0.02,
    )

}