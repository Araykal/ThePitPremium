package cn.charlotte.pit.events.impl.major

import cn.charlotte.pit.ThePit
import cn.charlotte.pit.config.NewConfiguration.eventOnlineRequired
import cn.charlotte.pit.data.PlayerProfile
import cn.charlotte.pit.enchantment.type.rare.PaparazziEnchant
import cn.charlotte.pit.event.PitKillEvent
import cn.charlotte.pit.events.*
import cn.charlotte.pit.item.type.mythic.MythicLeggingsItem
import cn.charlotte.pit.util.BoomFirework
import cn.charlotte.pit.util.PlayerUtil
import cn.charlotte.pit.util.chat.CC
import cn.charlotte.pit.util.cooldown.Cooldown
import cn.charlotte.pit.util.time.TimeUtil
import net.minecraft.server.v1_8_R3.EnumParticle
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles
import org.bukkit.*
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer
import org.bukkit.entity.Firework
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin
import kotlin.random.Random


class SquadsEvent : IEpicEvent, IEvent, IPrepareEvent, Listener, IScoreBoardInsert, ISortedEvent {

    companion object {
        @JvmStatic
        private val startChars = 'A'

        @JvmStatic
        private val colors = ArrayList(ChatColor.values().toList().filter {
            BannerData.chatColorToColor[it] != null
        })
    }

    private val timer by lazy {
        Cooldown(5, TimeUnit.MINUTES)
    }

    private val unusedColor by lazy {
        ArrayList(colors)
    }
    val usedChars = HashSet<Char>()
    val playerMap = HashMap<UUID, PlayerData>()
    val teamMap = HashMap<UUID, TeamData>()

    private val unFullTeams = HashSet<TeamData>()

    private val banners = ArrayList<BannerData>()

    private var state = State.PREPARE

    private val logicRunnable by lazy {
        object : BukkitRunnable() {
            override fun run() {
                for (banner in banners) {
                    banner.refreshBannerLogic(this@SquadsEvent)
                }
            }
        }
    }

    private val effectRunnable by lazy {
        object : BukkitRunnable() {
            override fun run() {
                for (banner in banners) {
                    banner.drawParticle()
                }
                refreshRank()
            }
        }
    }

    override fun getEventInternalName(): String {
        return "squads"
    }

    override fun getEventName(): String {
        return "&b&l旗帜争夺战"
    }

    override fun requireOnline(): Int {
        return eventOnlineRequired[eventInternalName]!!
    }

    override fun onActive() {
        val config = ThePit.getInstance().pitConfig
        for (location in config.squadsLocations) {
            location.block.type = Material.STANDING_BANNER
            val blockState = location.block.state
            val banner = blockState as org.bukkit.block.Banner
            banner.baseColor = DyeColor.WHITE
            banner.update()

            val bannerData = BannerData(location)
            banners.add(bannerData)
        }

        for (player in Bukkit.getOnlinePlayers()) {
            var data = teamMap[player.uniqueId]
            if (data == null) {
                data = createTeam()
                teamMap[player.uniqueId] = data

                data.addPlayer(player)
            }

            if (data.players.size >= 3) {
                continue
            }

            val players = Bukkit.getOnlinePlayers().toList()
            players.sortedBy { it.location.distanceSquared(player.location) }
            for (target in players) {
                val teamData = teamMap[target.uniqueId]
                if (teamData != null) {
                    continue
                }

                teamMap[target.uniqueId] = data
                data.addPlayer(target)
                if (data.players.size >= 3) {
                    unFullTeams.remove(data)
                    break
                }
            }
        }

        Bukkit.getPluginManager().registerEvents(this, ThePit.getInstance())
        logicRunnable.runTaskTimerAsynchronously(ThePit.getInstance(), 20L, 20L)
        effectRunnable.runTaskTimerAsynchronously(ThePit.getInstance(), 20L, 10L)

        object : BukkitRunnable() {
            override fun run() {
                if (timer.hasExpired()) {
                    ThePit.getInstance().eventFactory.inactiveEvent(this@SquadsEvent)
                    cancel()
                }
            }
        }.runTaskTimer(ThePit.getInstance(), 20L, 20L)

        state = State.GAMING
    }

    override fun onInactive() {
        for (banner in banners) {
            banner.location.block.type = Material.AIR

//            if (banner.firstLine.isSpawned) {
//                banner.firstLine.deSpawn()
//            }
//            if (banner.secondLine.isSpawned) {
//                banner.secondLine.deSpawn()
//            }
//            if (banner.thirdLine.isSpawned) {
//                banner.thirdLine.deSpawn()
//            }
        }

        logicRunnable.cancel()
        effectRunnable.cancel()
        HandlerList.unregisterAll(this)

        refreshRank()

        Bukkit.broadcastMessage(CC.CHAT_BAR)
        Bukkit.broadcastMessage("&6&l天坑事件结束: " + this.eventName + "&6&l!")

        for (player in Bukkit.getOnlinePlayers()) {
            val team = teamMap[player.uniqueId] ?: continue

            val rank = team.rank
            val profile = PlayerProfile.getPlayerProfileByUuid(player.uniqueId)
            var rewardCoins = 0.0
            var rewardRenown = 0
            if (rank <= 3) {
                rewardCoins += 2000.0
                rewardRenown += 2
            } else if (rank <= 20) {
                rewardCoins += 500.0
                rewardRenown += 1
            } else {
                rewardCoins += 100.0
            }
//            if (data != null && data.reachedSixFloor) {
//                rewardRenown++
//            }
            if (ThePit.getInstance().pitConfig.isGenesisEnable && profile.genesisData.tier >= 5 && rewardRenown > 0) {
                rewardRenown++
            }
            var enchantBoostLevel = PaparazziEnchant().getItemEnchantLevel(player.inventory.leggings)
            if (PlayerUtil.shouldIgnoreEnchant(player)) {
                enchantBoostLevel = 0
            }
            if (enchantBoostLevel > 0) {
                rewardCoins += 0.5 * enchantBoostLevel * rewardCoins
                rewardRenown += Math.floor(0.5 * enchantBoostLevel * rewardRenown).toInt()
                val mythicLeggings = MythicLeggingsItem()
                mythicLeggings.loadFromItemStack(player.inventory.leggings)
                if (mythicLeggings.isEnchanted) {
                    if (mythicLeggings.maxLive > 0 && mythicLeggings.live <= 2) {
                        player.inventory.leggings = ItemStack(Material.AIR)
                    } else {
                        mythicLeggings.live = mythicLeggings.live - 2
                        player.inventory.leggings = mythicLeggings.toItemStack()
                    }
                }
            }
            if (PlayerUtil.isPlayerUnlockedPerk(player, "self_confidence")) {
                if (rank <= 5) {
                    rewardCoins += 5000.0
                } else if (rank <= 10) {
                    rewardCoins += 2500.0
                } else if (rank <= 15) {
                    rewardCoins += 1000.0
                }
            }
            profile.grindCoins(rewardCoins)
            profile.coins = profile.coins + rewardCoins
            profile.renown = profile.renown + rewardRenown
            player.sendMessage(CC.translate("&6你的奖励: &6+" + rewardCoins + "硬币 &e+" + rewardRenown + "声望"))
            if (rank > 0) {
                player.sendMessage(CC.translate("&6&l你的队伍: &7获得了 &b" + team.score + " &7分数 (排名#" + rank + ")"))
            }
            val list = teamMap.values.toSet().sortedByDescending {
                it.score
            }
            if (list.size >= 3) {
                player.sendMessage(CC.translate("&6顶级玩家: "))
                for (i in 1..3) {
                    val data = list[i - 1]
                    val builder = StringBuilder()
                    for (topProfile in data.players) {
                        builder.append(topProfile.formattedNameWithRoman + "&7, ")
                    }

                    player.sendMessage(CC.translate(" &e&l#" + i + " " + data.chatColor + data.char + " &b${data.score}积分 &7(${builder})"))
                }
            }
        }
        Bukkit.broadcastMessage(CC.CHAT_BAR)
    }

    override fun onPreActive() {
        for (player in Bukkit.getOnlinePlayers()) {
            this.playerMap[player.uniqueId] = PlayerData(player.uniqueId)
        }
    }

    private fun refreshRank() {
        val teams = teamMap.values.toSet()
        val sortedTeam = teams.sortedByDescending {
            it.score
        }

        for (index in sortedTeam.indices) {
            sortedTeam[index].rank = index + 1
        }
    }

    @EventHandler
    fun onKill(event: PitKillEvent) {
        val killer = event.killer
        val data = teamMap[killer.uniqueId] ?: return
        data.score += 25

        killer.sendMessage(CC.translate("&b&l击杀! &7您通过击杀获得了 &b25积分&7 !"))
    }

    @EventHandler
    fun onBlockBreak(event: BlockBreakEvent) {
        if (event.block.type == Material.BANNER) {
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        val player = event.player
        val data = this.playerMap[player.uniqueId]

        if (data == null) {
            val playerData = PlayerData(player.uniqueId)
            this.playerMap[player.uniqueId] = playerData
        }

        if (state == State.GAMING) {
            val teamData =
                if (unFullTeams.isEmpty()) {
                    val team = createTeam()
                    teamMap[player.uniqueId] = team
                    team
                } else {
                    unFullTeams.first()
                }

            teamData.addPlayer(player)
        }
    }

    private fun TeamData.addPlayer(player: Player) {
        val size = this.players.size
        if (size >= 3) {
            return
        }

        this.players.add(PlayerProfile.getPlayerProfileByUuid(player.uniqueId))
        if (this.players.size >= 3) {
            unFullTeams.remove(this)
        }
    }

    private fun createTeam(): TeamData {
        val teamData = TeamData(nextChar())

        if (unusedColor.isEmpty()) {
            unusedColor.addAll(ArrayList(colors))
        }

        teamData.chatColor = unusedColor[Random.nextInt(unusedColor.size)]
        unusedColor.remove(teamData.chatColor)
        unFullTeams.add(teamData)

        return teamData
    }

    private fun nextChar(): Char {
        val used = usedChars.size
        val result = startChars + used

        usedChars.add(result)
        return result
    }

    class BannerData(val location: Location) {
        companion object {
            @JvmStatic
            private val timeFormat = SimpleDateFormat("mm分ss秒")

            @JvmStatic
            private val MAX_CAPTURING_STATE = 5

            @JvmStatic
            val chatColorToColor = mapOf(
                ChatColor.BLACK to Color.BLACK,
                ChatColor.RED to Color.RED,
                ChatColor.DARK_GREEN to Color.GREEN,
                ChatColor.DARK_BLUE to Color.BLUE,
                ChatColor.DARK_PURPLE to Color.MAROON,
                ChatColor.DARK_AQUA to Color.fromRGB(0, 170, 170),
                ChatColor.DARK_GRAY to Color.fromRGB(85, 85, 85),
                ChatColor.LIGHT_PURPLE to Color.fromRGB(255, 85, 255),
                ChatColor.GREEN to Color.LIME,
                ChatColor.YELLOW to Color.YELLOW,
                ChatColor.AQUA to Color.AQUA,
                ChatColor.GOLD to Color.ORANGE
            )

            val chatColorToData = mapOf(
                ChatColor.BLACK to 0,
                ChatColor.RED to 1,
                ChatColor.DARK_GREEN to 2,
                ChatColor.DARK_BLUE to 4,
                ChatColor.DARK_PURPLE to 5,
                ChatColor.DARK_AQUA to 6,
                ChatColor.DARK_GRAY to 8,
                ChatColor.LIGHT_PURPLE to 9,
                ChatColor.GREEN to 10,
                ChatColor.YELLOW to 11,
                ChatColor.AQUA to 12,
                ChatColor.GOLD to 14
            )
        }

        var capturedTeam: TeamData? = null
        var capturingTeam: TeamData? = null
        var capturingState = 0
        var bannerStatus = BannerStatus.FREE

        var capturedStartAt = -1L

//        var firstLine = DHAPI.createHologram(
//            location.clone().add(0.0, 2.5, 0.0), "&f&l站在周围占领旗帜".colored()
//        ).also {
//            it.spawn()
//        }
//
//        var secondLine = HologramAPI.createHologram(
//            location.clone().add(0.0, 2.8, 0.0),
//            "&f无人占领".colored()
//        ).also {
//            it.spawn()
//        }

//        var thirdLine = HologramAPI.createHologram(
//            location.clone().add(0.0, 2.25, 0.0),
//            ""
//        ).also {
//            it.spawn()
//        }

        fun refreshBannerLogic(event: SquadsEvent) {
            val players = location.world.getNearbyEntities(location, 3.0, 3.0, 3.0)
                .filterIsInstance<Player>()

//            if (this.capturedTeam != null) {
            val teams = HashSet<TeamData>()
            var otherTeamContains = false

            for (player in players) {
                val teamData = event.teamMap[player.uniqueId] ?: continue
                teams.add(teamData)

                if (event.teamMap[player.uniqueId] != this.capturedTeam) {
                    otherTeamContains = true
                }
            }

            if (teams.size > 1) {
                this.bannerStatus = BannerStatus.FIGHTING
            } else if (otherTeamContains && teams.size > 0) {
                val capturingTeam = teams.first()
                if (this.capturingTeam != capturingTeam) {
                    this.capturingTeam = capturingTeam
                }

                this.capturingState++
                this.bannerStatus = BannerStatus.CAPTURING

                if (capturingState > 5) {
                    Bukkit.getScheduler().runTask(ThePit.getInstance()) {
                        val state = this.location.block.state
                        val banner = state as org.bukkit.block.Banner
                        banner.baseColor = DyeColor.getByDyeData(chatColorToData[capturingTeam.chatColor]!!.toByte())
                        banner.update(true, false)
                    }

                    val first = players.first()
                    val gotPlayer =
                        PlayerProfile.getPlayerProfileByUuid(first.uniqueId).formattedLevelTag

                    capturedTeam?.let {
                        for (player in it.players) {
                            Bukkit.getPlayer(player.playerUuid)?.sendMessage(
                                CC.translate("&b&l夺旗! &c-&c&l旗帜&a! &7被 $gotPlayer &7偷走了一面旗帜!")
                            )
                        }
                    }

                    for (player in capturingTeam.players) {
                        Bukkit.getPlayer(player.playerUuid)?.sendMessage(
                            CC.translate("&b&l夺旗! &a+&a&l旗帜&a! &7${gotPlayer} &7拿到了一面旗帜!")
                        )
                    }

                    this.capturedTeam?.banners?.remove(this)

                    this.bannerStatus = BannerStatus.CAPTURED
                    this.capturingState = 0
                    this.capturingTeam = null
                    this.capturedTeam = capturingTeam
                    this.capturedStartAt = System.currentTimeMillis()
                    setCapturedHologram()

                    capturingTeam.banners.add(this)
                    Bukkit.getScheduler().runTask(ThePit.getInstance()) {
                        val firework = BoomFirework((location.world as CraftWorld).handle)
                        val meta = (firework.bukkitEntity as Firework).fireworkMeta
                        meta.addEffect(
                            FireworkEffect.builder()
                                .withColor(chatColorToColor[capturingTeam.chatColor]!!)
                                .with(FireworkEffect.Type.BURST)
                                .build()
                        )
                        (firework.bukkitEntity as Firework).fireworkMeta = meta
                        firework.setPosition(location.x, location.y, location.z)
                        if ((location.world as CraftWorld).handle.addEntity(firework)) {
                            firework.isInvisible = true
                        }
                    }
                }
            } else {
                this.capturingState = 0
                this.capturingTeam = null
                this.bannerStatus = BannerStatus.CAPTURED
                this.capturedTeam?.score = this.capturedTeam!!.score + (min(
                    4,
                    max(2, (System.currentTimeMillis() - this.capturedStartAt) / (1000 * 60L)).toInt()
                ))
            }
//            }
            refreshBannerHolograms()
        }

        fun drawParticle() {
            if (bannerStatus == BannerStatus.FIGHTING) {
                drawCircle(EnumParticle.FLAME, null)
            } else if (bannerStatus == BannerStatus.CAPTURING && capturingTeam != null) {
                drawCircle(EnumParticle.REDSTONE, chatColorToColor[capturingTeam!!.chatColor]!!)
            }
        }

        private fun drawCircle(effect: EnumParticle, color: Color?) {
            val clone = location.clone()
            for (degree in 0..359) {
                if (degree % 3 != 0) {
                    continue
                }

                val radians = Math.toRadians(degree.toDouble())
                val x = cos(radians) * 3
                val y = sin(radians) * 3
                clone.add(x, 0.0, y)
                val packet = PacketPlayOutWorldParticles(
                    effect,
                    true,
                    clone.x.toFloat(),
                    clone.y.toFloat(),
                    clone.z.toFloat(),
                    max(1 / 255F, (color?.red ?: 0) / 255F),
                    (color?.green ?: 0) / 255F,
                    (color?.blue ?: 0) / 255F,
                    1f,
                    0,
                    0
                )
                Bukkit.getOnlinePlayers().forEach {
                    (it as CraftPlayer).handle.playerConnection.sendPacket(packet)
                }
                clone.subtract(x, 0.0, y)
            }
        }

        fun refreshBannerHolograms() {
//            if (bannerStatus == BannerStatus.FIGHTING) {
//                if (capturedTeam != null) {
//                    setCapturedHologram()
//                }
//                thirdLine.text = "&c争夺中!!!".colored()
//            } else if (bannerStatus == BannerStatus.CAPTURING) {
//                if (capturedTeam != null) {
//                    setCapturedHologram()
//                }
//                if (capturingTeam != null) {
//                    val color = capturingTeam!!.chatColor
//                    val char = capturingTeam!!.char
//                    val builder = StringBuilder("&7[${color}${char}&7] &7[${color}")
//                    for (index in 0 until capturingState) {
//                        builder.append("●")
//                    }
//                    builder.append("&7")
//                    for (index in 0 until MAX_CAPTURING_STATE - capturingState) {
//                        builder.append("●")
//                    }
//                    builder.append("&7]")
//
//                    thirdLine.text = builder.toString().colored()
//                }
//            } else if (bannerStatus == BannerStatus.CAPTURED) {
//                setCapturedHologram()
//                thirdLine.text = ""
//            }
        }

        fun setCapturedHologram() {
//            val teamData = capturedTeam ?: return
//
//            val color = teamData.chatColor
//            firstLine.text = "${color}由 &7[${color}${teamData.char}&7]${color} 队伍占领".colored()
//            val capturedTime = System.currentTimeMillis() - capturedStartAt
//
//            secondLine.text = "&f${timeFormat.format(capturedTime)} &6| &f+${
//                (max(
//                    2,
//                    min(4, (System.currentTimeMillis() - this.capturedStartAt) / (1000 * 60L)).toInt()
//                ))
//            } &b积分/秒".colored()
        }

        private fun setNoneHologram() {
//            firstLine.text = "&f&l站在周围占领旗帜".colored()
//            secondLine.text = "&f无人占领".colored()
        }
    }

    enum class BannerStatus {
        CAPTURED,
        CAPTURING,
        FIGHTING,
        FREE
    }

    class PlayerData(val uuid: UUID) {

        val invites = HashMap<UUID, Cooldown>()
    }

    class TeamData(var char: Char) {
        var rank = -1
        var score = 0
        lateinit var chatColor: ChatColor
        val players = HashSet<PlayerProfile>()
        var banners = HashSet<BannerData>()

        fun getScorePerSecond(): Int {
            return banners.sumOf {
                (min(4, max(2, (System.currentTimeMillis() - it.capturedStartAt) / (1000 * 60L)).toInt()))
            }.toInt()
        }
    }

    enum class State {
        PREPARE,
        GAMING
    }

    private val format = DecimalFormat("###,###,###,###,###")

    override fun insert(player: Player): MutableList<String> {
        val data = teamMap[player.uniqueId] ?: return arrayListOf()

        return arrayListOf(
            "&f剩余时间: &a${TimeUtil.millisToTimer(timer.getRemaining())}",
            "&7[${data.chatColor}${data.char}&7] &e#${data.rank}, &b${format.format(data.score)} 总积分",
            "&f+&b${data.getScorePerSecond()}积分&f/秒, &a${data.banners.size}&f 旗帜已占领"
        )
    }

    override fun getRank(player: Player): Int {
        val team = teamMap[player.uniqueId] ?: return 100000
        return 'Z'.code - team.char.code
    }
}