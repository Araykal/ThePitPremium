package cn.charlotte.pit.events.impl.major

import cn.charlotte.pit.ThePit
import cn.charlotte.pit.config.NewConfiguration.eventOnlineRequired
import cn.charlotte.pit.event.ItemLiveDropEvent
import cn.charlotte.pit.events.IEpicEvent
import cn.charlotte.pit.events.IEvent
import cn.charlotte.pit.events.IScoreBoardInsert
import cn.charlotte.pit.util.chat.CC
import cn.charlotte.pit.util.chat.MessageType
import cn.charlotte.pit.util.cooldown.Cooldown
import cn.charlotte.pit.util.time.TimeUtil
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.scheduler.BukkitRunnable
import java.util.concurrent.TimeUnit

/**
 * @author Araykal
 * @since 2025/2/26
 */
class PatronageEvent : IEvent, IEpicEvent, Listener, IScoreBoardInsert {
    private lateinit var timer: Cooldown
    private lateinit var runnable: BukkitRunnable

    override fun getEventInternalName(): String {
        return "patronage"
    }

    override fun getEventName(): String {
        return "庇护"
    }

    override fun requireOnline(): Int = eventOnlineRequired[eventInternalName]!!


    @EventHandler
    fun onItemLiveDrop(e: ItemLiveDropEvent) {
        e.isCancelled = true
    }

    override fun onActive() {
        println("debug")
        timer = Cooldown(5, TimeUnit.MINUTES)
        runnable = object : BukkitRunnable() {
            override fun run() {
                if (timer.hasExpired()) {
                    cancel()
                    if (this@PatronageEvent == ThePit.getInstance().eventFactory.activeEpicEvent) {
                        Bukkit.getScheduler().runTask(ThePit.getInstance()) {
                            ThePit.getInstance()
                                .eventFactory
                                .inactiveEvent(this@PatronageEvent)
                        }
                    }
                }
            }
        }.apply {
            runTaskTimer(ThePit.getInstance(), 20, 10)
        }

        CC.boardCast(MessageType.EVENT, "&a&l大型事件! &e&l庇护 &7事件开始!")
        Bukkit.getPluginManager().registerEvents(this, ThePit.getInstance())
    }

    override fun onInactive() {
        HandlerList.unregisterAll(this)
    }

    override fun insert(player: Player): List<String> {
        return listOf(
            "&f剩余时间: &a" + TimeUtil.millisToTimer(timer.remaining),
            "&a不掉生命",
            "&3不掉羽毛"
        )
    }
}
