package cn.charlotte.pit.impl

import cn.charlotte.pit.bot.BotHandler
import net.citizensnpcs.api.npc.MemoryNPCDataStore
import net.citizensnpcs.npc.CitizensNPCRegistry
import org.bukkit.Location
import org.bukkit.configuration.Configuration
import org.bukkit.entity.EntityType

object BotHandlerImpl: BotHandler {
    val npcRegistry by lazy {
        CitizensNPCRegistry(MemoryNPCDataStore())
    }

    override fun spawnBot(location: Location, name: String) {
        val npc = npcRegistry.createNPC(EntityType.PLAYER, name)

    }

    class BotConfig {
        companion object {
            @JvmStatic
            fun load(config: Configuration) {
                val botConfig = BotConfig()
                botConfig.enabled = config.getBoolean("enable")
                botConfig.prestigeRange = config.getString("prestige")!!.split("-").let {
                    it[0].toInt() .. it[1].toInt()
                }
                botConfig.levelRange = config.getString("prestige")!!.split("-").let {
                    it[0].toInt() .. it[1].toInt()
                }

                val armors = config.getStringList("armor").map {
                    it.split("/").map { randomString ->
                        val split = randomString.split(":")
                        RandomObj(split[0]).also {
                            it.randomValue = split[1].toDouble()
                        }
                    }
                }

                for (index in 0 until 4) {
                    botConfig.armorRandoms[index] = armors[index]
                }

                botConfig.heldItems.addAll(
                    config.getString("held")!!.let {
                        it.split("/").map { line ->
                            line.split(":").let { split ->
                                RandomObj(split.first()).also {
                                    it.randomValue = split.get(1).toDouble()
                                }
                            }
                        }
                    }
                )

                botConfig.bountyChance = config.getDouble("bounty-chance")
                botConfig.bountyValue = config.getString("bounty-value")!!.split("-").let {
                    it[0].toInt() .. it[1].toInt()
                }
            }
        }

        var enabled = false
        var prestigeRange = 40 .. 120
        var levelRange = 40 .. 120
        val armorRandoms = HashMap<Int, List<RandomObj>>()
        val heldItems = ArrayList<RandomObj>()
        var bountyChance = 0.08
        var bountyValue = 100..500


        class RandomObj(
            val  content: String
        ) {
            var randomValue = 0.0
        }

    }
}