package cn.charlotte.pit.perk.type.streak.tothemoon

import cn.charlotte.pit.ThePit
import cn.charlotte.pit.data.PlayerProfile
import cn.charlotte.pit.event.PitKillEvent
import cn.charlotte.pit.event.PitStreakKillChangeEvent
import cn.charlotte.pit.parm.AutoRegister
import cn.charlotte.pit.parm.listener.IPlayerBeKilledByEntity
import cn.charlotte.pit.parm.listener.IPlayerDamaged
import cn.charlotte.pit.parm.listener.IPlayerKilledEntity
import cn.charlotte.pit.perk.AbstractPerk
import cn.charlotte.pit.perk.MegaStreak
import cn.charlotte.pit.perk.PerkType
import cn.charlotte.pit.util.PlayerUtil
import cn.charlotte.pit.util.chat.CC
import cn.charlotte.pit.util.chat.MessageType
import com.google.common.util.concurrent.AtomicDouble
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent
import java.text.DecimalFormat
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.math.floor
import kotlin.math.min
import kotlin.random.Random

@AutoRegister
class ToTheMoonMegaStreak: AbstractPerk() , IPlayerKilledEntity, IPlayerDamaged, IPlayerBeKilledByEntity, MegaStreak, Listener {
    companion object {
        @JvmStatic
        val cache = HashMap<UUID, Double>()
    }

    override fun getInternalPerkName(): String {
        return "to_the_moon"
    }

    override fun getDisplayName(): String {
        return "&b月球之旅"
    }

    override fun getIcon(): Material {
        return Material.ENDER_STONE
    }

    override fun requireCoins(): Double {
        return 50000.0
    }

    override fun requireRenown(level: Int): Double {
        return 150.0
    }

    override fun requirePrestige(): Int {
        return 14
    }

    override fun requireLevel(): Int {
        return 80
    }

    override fun getDescription(player: Player): MutableList<String> {
        return mutableListOf(
            "&7激活要求连杀数: &c100 连杀",
            "",
            "&7当激活时: ",
            "  &a◼ &7击杀额外获得&b +20%经验&7.",
            "  &a◼ &7击杀额外获得&b 最多100经验&7.",
            "",
            "&7但是: ",
            "  &c◼ &7每20击杀将额外承受&c +10%&7 的伤害.",
            "&7       (从100连杀开始)",
            "  &c◼ &7每20击杀将额外承受&c +0.1❤&7 的&f真实&7伤害.",
            "&7       (从200连杀开始)",
            "",
            "&7连杀期间: ",
            "  &b◼ &7复制并存储你获得的&b经验&7.",
            "",
            "&7当死亡时: ",
            "  &b◼ &7获得你存储的&b经验&7, 并且",
            "&7  额外获得超过100击杀部分的击杀数的&b0.005&7倍 &b经验",
            "&7  , 上限为&b1倍"
        )
    }

    override fun getMaxLevel(): Int {
        return 1
    }

    override fun getPerkType(): PerkType {
        return PerkType.MEGA_STREAK
    }

    override fun onPerkActive(player: Player?) {

    }

    override fun onPerkInactive(player: Player?) {

    }

    @EventHandler
    fun quit(event: PlayerQuitEvent) {
        cache.remove(event.player.uniqueId)
    }

    @EventHandler
    fun onKill(event: PitKillEvent) {
        val killer = event.killer
        val profile = PlayerProfile.getPlayerProfileByUuid(killer.uniqueId) ?: return

        if (!PlayerUtil.isPlayerChosePerk(killer, "to_the_moon")) {
            return
        }

        if (profile.streakKills >= 100) {
            val i = cache.getOrDefault(killer.uniqueId, 0.0) + event.exp
            cache[killer.uniqueId] = i
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun onStreak(event: PitStreakKillChangeEvent) {
        if (!PlayerUtil.isPlayerChosePerk(Bukkit.getPlayer(event.playerProfile.playerUuid), "to_the_moon")) {
            return
        }

        val trigger = 100
        //trigger check (get X streak)
        //trigger check (get X streak)
        if (event.from < trigger && event.to >= trigger) {
            CC.boardCast(
                MessageType.COMBAT,
                "&c&l超级连杀! " + event.playerProfile.formattedNameWithRoman + " &7激活了 &b&l月球之旅 &7!"
            )
            Bukkit.getOnlinePlayers().forEach { player: Player? ->
                player!!.playSound(
                    player.location,
                    Sound.WITHER_SPAWN,
                    0.8f,
                    1.5f
                )
            }
        }
    }

    override fun handlePlayerKilled(
        enchantLevel: Int,
        myself: Player,
        target: Entity,
        coins: AtomicDouble,
        experience: AtomicDouble
    ) {
        val profile = PlayerProfile.getPlayerProfileByUuid(myself.uniqueId) ?: return
        if (profile.streakKills >= 100) {
            experience.addAndGet(experience.get() * 0.2)

            val extra = Random.nextInt(1, 100)
            experience.addAndGet(extra.toDouble())
        }
    }

    override fun handlePlayerDamaged(
        enchantLevel: Int,
        myself: Player,
        attacker: Entity,
        damage: Double,
        finalDamage: AtomicDouble,
        boostDamage: AtomicDouble,
        cancel: AtomicBoolean
    ) {
        val profile = PlayerProfile.getPlayerProfileByUuid(myself.uniqueId) ?: return
        val streakKills = profile.streakKills

        if (streakKills >= 100) {
            val extraBoost = floor((streakKills - 100) / 20.0)
            boostDamage.addAndGet(extraBoost * 0.2)
        }

        if (streakKills >= 200) {
            val extraBoost = floor((streakKills - 200) / 20.0)
            finalDamage.addAndGet(extraBoost * 0.1)
        }
    }

    private val format = DecimalFormat("###,###,###,###")
    private val decimalFormat = DecimalFormat("##.##")

    override fun handlePlayerBeKilledByEntity(
        enchantLevel: Int,
        myself: Player,
        target: Entity,
        coins: AtomicDouble,
        experience: AtomicDouble
    ) {
        val profile = PlayerProfile.getPlayerProfileByUuid(myself.uniqueId) ?: return
        val streakKills = profile.streakKills
        if (streakKills >= 100) {
            if (ThePit.getInstance().eventFactory.activeEpicEvent != null) {
                return
            }

            val stored = cache.remove(myself.uniqueId) ?: return
            if (stored > 0) {
                val give = min(((streakKills - 100) * 0.005) * stored + stored, stored * 2)
                val multiple = (give - stored) / stored
                profile.experience += give
                profile.applyExperienceToPlayer(myself)
                myself.sendMessage(CC.translate("&b&l月球之旅! &7你挣取了 &b+${format.format(give)}经验&7 在这次连杀中. (&b${decimalFormat.format(multiple)}x &7倍率)"))
            }
        }
    }

    override fun getStreakNeed(): Int {
        return 100
    }
}