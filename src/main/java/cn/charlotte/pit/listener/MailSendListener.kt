package cn.charlotte.pit.listener

import cn.charlotte.pit.ThePit
import cn.charlotte.pit.data.FixedRewardData
import cn.charlotte.pit.parm.AutoRegister
import cn.charlotte.pit.util.chat.CC
import cn.charlotte.pit.util.time.TimeUtil
import com.mongodb.client.model.Filters
import com.mongodb.client.model.ReplaceOptions
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.scheduler.BukkitRunnable
import java.util.*

@AutoRegister
class MailSendListener : Listener {
    companion object {
        @JvmStatic
        val editing: HashMap<UUID, FixedRewardData> = HashMap()
    }

    @EventHandler(priority = EventPriority.LOW)
    fun onChat(event: AsyncPlayerChatEvent) {
        val player = event.player

        val data = editing[player.uniqueId] ?: return
        if (event.message.equals("cancel", ignoreCase = true)) {
            editing.remove(player.uniqueId)
            return
        }
        event.isCancelled = true

        if (data.title == "-1") {
            data.title = CC.translate(event.message)

            player.sendMessage(CC.translate("&a现在，你可以输入邮件内容了，输入 cancel 以取消"))
            return
        }

        if (data.content == "-1") {
            data.content = CC.translate(event.message)

            player.sendMessage(CC.translate("&a现在，你可以输入发送时间了，输入的时间将在从现在开始计算起之后的时间生效，输入 cancel 以取消"))
            return
        }

        if (data.startTime == -1L) {
            val parseTime = TimeUtil.parseTime(event.message)
            if (parseTime == -1L) {
                player.sendMessage(CC.translate("&c你输入的时间不合法"))
                return
            }

            data.startTime = parseTime
            player.sendMessage(CC.translate("&a现在，你可以输入结束时间了，输入的时间将在从现在开始计算起之后的时间生效，输入 cancel 以取消"))
            return
        }

        if (data.endTime == -1L) {
            val parseTime = TimeUtil.parseTime(event.message)
            if (parseTime == -1L) {
                player.sendMessage(CC.translate("&c你输入的时间不合法"))
                return
            }

            data.endTime = parseTime
            data.mailId = UUID.randomUUID().toString()
            data.sendTime = System.currentTimeMillis()

            player.sendMessage(data.toString())
            player.sendMessage(CC.translate("&a现在，输入 confirm 以发送，输入 cancel 取消发送"))
            return
        }

        if (event.message.equals("confirm", true)) {
            object : BukkitRunnable() {
                override fun run() {
                    ThePit.getInstance().mongoDB.rewardCollection.replaceOne(
                        Filters.eq("mailId", data.mailId), data, ReplaceOptions().upsert(true)
                    )

                    FixedRewardData.refreshAll()
                    player.sendMessage(CC.translate("&aOK!"))

                    editing.remove(player.uniqueId)
                }
            }.runTaskAsynchronously(ThePit.getInstance())
        }

    }

}