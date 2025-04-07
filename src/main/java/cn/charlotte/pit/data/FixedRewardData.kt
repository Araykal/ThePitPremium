package cn.charlotte.pit.data

import cn.charlotte.pit.ThePit
import cn.charlotte.pit.data.mail.Mail
import cn.charlotte.pit.util.chat.CC
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.bukkit.entity.Player
import java.util.*

@JsonIgnoreProperties(ignoreUnknown = true)
class FixedRewardData() {
    companion object {
        private val cache: HashMap<UUID, FixedRewardData> = HashMap()
        private var loading: Boolean = false

        fun refreshAll() {
            loading = true
            cache.clear()
            for (data in ThePit.getInstance().mongoDB.rewardCollection.find()) {
                cache[UUID.fromString(data.mailId)] = data
            }
            loading = false;
            
        }

        fun sendMail(profile: PlayerProfile, player: Player) {
            if (loading) return

            val now = System.currentTimeMillis()
            for (data in cache.values) {
                if (profile.claimedMail.contains(data.mailId)) continue

                val info = data.data ?: continue

                if (now >= data.startTime + data.sendTime && now <= data.endTime + data.sendTime) {

                    if (info.limitPermission == null || info.limitPermission == "" || player.hasPermission(info.limitPermission)) {
                        if (profile.prestige > 0 || profile.level > data.data?.limitLevel!!) {
                            val mail = Mail()
                            mail.coins = info.coins
                            mail.exp = info.exp
                            mail.renown = info.renown
                            mail.item = info.item
                            mail.title = data.title
                            mail.content = data.content
                            mail.sendTime = now
                            mail.expireTime = now + 30 * 24 * 60 * 60 * 1000L
                            mail.build()

                            profile.claimedMail.add(data.mailId)
                            profile.mailData.sendMail(mail)
                            player.sendMessage(CC.translate("&6&l邮件! &7你收到了一封邮件,请从邮件NPC处查看具体内容."))
                        }
                    }
                }
            }
        }
    }

    var sendTime: Long = -1
    var mailId: String = ""
    var startTime: Long = -1
    var endTime: Long = -1
    var title: String = "-1"
    var content: String = "-1"
    var data: CDKData? = null

}