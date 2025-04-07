package cn.charlotte.pit.events.impl

import cn.charlotte.pit.ThePit
import cn.charlotte.pit.config.NewConfiguration.eventOnlineRequired
import cn.charlotte.pit.data.PlayerProfile
import cn.charlotte.pit.events.IEvent
import cn.charlotte.pit.events.INormalEvent
import cn.charlotte.pit.util.chat.CC
import cn.charlotte.pit.util.hologram.Hologram
import cn.charlotte.pit.util.hologram.HologramAPI
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.event.EventHandler
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import java.util.*

/**
 * @author Araykal
 * @since 2025/1/17
 */
class DragonEggsEvent : IEvent, INormalEvent, Listener {
    private var eggLocation: Location? = null
    private var clicks: Int = 0
    private var firstHologram: Hologram? = null
    private var secondHologram: Hologram? = null
    private var isActive = false
    private var isClick = false

    companion object {
        private const val MAX_CLICKS = 230
        private const val CLICK_THRESHOLD = 50
        private const val SEARCH_RADIUS = 10
        private const val MAX_ATTEMPTS = 20
    }

    override fun getEventInternalName(): String = "dragon_egg"

    override fun getEventName(): String = "&5龙蛋"

    override fun requireOnline(): Int = eventOnlineRequired[eventInternalName]!!

    private fun registerEvents() {
        Bukkit.getPluginManager().registerEvents(this, ThePit.getInstance())
    }

    private fun unregisterEvents() {
        HandlerList.unregisterAll(this)
    }

    private fun prepareNewLocation() {
        despawnHolograms()
        eggLocation?.block?.type = Material.AIR
    }

    private fun calculateOffset(origin: Location, random: Random): Int {
        return random.nextInt(31) - SEARCH_RADIUS
    }

    override fun onActive() {
        eggLocation = ThePit.getInstance().pitConfig.eggLoc ?: run {
            Bukkit.broadcastMessage(CC.translate("&5&l龙蛋！ &7活动区域未设置，请联系管理员设置！"))
            ThePit.getInstance().getEventFactory().inactiveEvent(this)
            return
        }
        isActive = true
        isClick = true
        registerEvents()
        CC.boardCast(CC.translate("&5&l龙蛋！ &d龙蛋已在中心点位刷新,请前往点击！"))
        setEggLocation(eggLocation!!)
        playSoundToOnlinePlayers(Sound.ENDERDRAGON_GROWL, 1.5f, 1.5f)
        Bukkit.getScheduler().runTaskLater(ThePit.getInstance(), {
            ThePit.getInstance().getEventFactory().inactiveEvent(this)
            isActive = false
        }, 8 * 20 * 60)
    }

    private fun setEggLocation(location: Location) {
        prepareNewLocation()
        eggLocation = location
        eggLocation!!.block.type = Material.DRAGON_EGG
        reCreateHologram(location)
    }

    @EventHandler
    fun onInteract(event: PlayerInteractEvent) {
        if (!isActive || event.clickedBlock?.type != Material.DRAGON_EGG) return
        event.isCancelled = true
        if (!isClick) {
            despawnHolograms()
            return
        }
        val player = event.player
        val p = PlayerProfile.getPlayerProfileByUuid(player.uniqueId)
        val random = Random()
        val randomMultiplier = random.nextInt(3) + 1
        val (coins, exp) = when (clicks) {
            0 -> Pair(3 * randomMultiplier, 3 * (random.nextInt(5) + 1))
            else -> Pair(clicks * 0.5 * randomMultiplier, clicks * 0.5 * (random.nextInt(5) + 1))
        }
        if (clicks <= MAX_CLICKS) {
            p.coins += coins.toDouble()
            p.experience += exp.toDouble()
        }
        player.playSound(player.location, Sound.CLICK, 1.5f, 1.5f)
        player.sendMessage(CC.translate("&5&l龙蛋！ &7点击龙蛋 获得 &e$coins &6金币 &e$exp &b经验&7"))
        addClicks()
        handleClickEvents()
    }

    private fun handleClickEvents() {
        if (clicks >= MAX_CLICKS) {
            ThePit.getInstance().getEventFactory().inactiveEvent(this)
        } else if (clicks % CLICK_THRESHOLD == 0 || clicks + 1 % CLICK_THRESHOLD == 0) {
            isClick = false
            setNewLocation()
        }
    }

    private fun setNewLocation() {
        prepareNewLocation()
        eggLocation?.let { setEggLocation(findRandomLocation(it)) }
        CC.boardCast("&5&l龙蛋！ &7龙蛋已被移动到了新的位置！")
        playSoundToOnlinePlayers(Sound.ENDERDRAGON_HIT, 1.5f, 1.5f)
        isClick = true
    }

    private fun playSoundToOnlinePlayers(sound: Sound, volume: Float, pitch: Float) {
        Bukkit.getOnlinePlayers().forEach {
            it.playSound(it.location, sound, volume, pitch)
        }
    }

    private fun reCreateHologram(location: Location) {
        firstHologram = HologramAPI.createHologram(location.block.location.clone().add(0.5, 2.4, 0.5), "§a$clicks")
        secondHologram = HologramAPI.createHologram(location.block.location.clone().add(0.5, 2.0, 0.5), "§e§l点击")
        firstHologram!!.spawn()
        secondHologram!!.spawn()
    }

    private fun despawnHolograms() {
        firstHologram?.deSpawn()
        secondHologram?.deSpawn()
        firstHologram = null
        secondHologram = null
    }

    private fun addClicks() {
        clicks++
        firstHologram!!.text = "§a$clicks"
    }

    override fun onInactive() {
        unregisterEvents()
        cleanup()
        playSoundToOnlinePlayers(Sound.ENDERDRAGON_DEATH, 1.5f, 1.5f)
        CC.boardCast(CC.translate("&5&l龙蛋！ &7活动已结束！"))
    }

    private fun cleanup() {
        eggLocation?.block?.type = Material.AIR
        despawnHolograms()
        isActive = false
        eggLocation = null
        clicks = 0
    }

    private fun findRandomLocation(origin: Location): Location {
        val random = Random()
        var newLocation: Location
        var attempts = 0
        while (true) {
            val x = origin.x + calculateOffset(origin, random)
            val z = origin.z + calculateOffset(origin, random)
            newLocation = Location(origin.world, x, origin.y, z)
            if (newLocation.block.type == Material.AIR || attempts >= MAX_ATTEMPTS) break
            attempts++
        }
        return newLocation
    }
}