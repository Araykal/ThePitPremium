package cn.charlotte.pit.bot

import org.bukkit.Location

interface BotHandler {

    fun spawnBot(location: Location, name: String)

}