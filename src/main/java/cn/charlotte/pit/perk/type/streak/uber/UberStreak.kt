package cn.charlotte.pit.perk.type.streak.uber


//import org.bukkit.event.entity.PotionEffectAddEvent
import cn.charlotte.pit.data.PlayerProfile
import cn.charlotte.pit.event.*
import cn.charlotte.pit.getPitProfile
import cn.charlotte.pit.item.type.UberDrop
import cn.charlotte.pit.parm.AutoRegister
import cn.charlotte.pit.perk.AbstractPerk
import cn.charlotte.pit.perk.MegaStreak
import cn.charlotte.pit.perk.PerkType
import cn.charlotte.pit.util.PlayerUtil
import cn.charlotte.pit.util.chat.CC
import cn.charlotte.pit.util.chat.MessageType
import cn.charlotte.pit.util.inventory.InventoryUtil
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityRegainHealthEvent
import org.bukkit.potion.PotionEffect
import java.util.*


/*
 * @ Created with IntelliJ IDEA
 * @ Author EmptyIrony
 * @ Date 10/6/2021
 * @ Time 1:32 PM
 */
@AutoRegister
class UberStreak : AbstractPerk(), Listener, MegaStreak {

    override fun getInternalPerkName(): String {
        return "uber_streak"
    }

    override fun getDisplayName(): String {
        return "&d登峰造极"
    }

    override fun getIcon(): Material {
        return Material.GOLD_SWORD
    }

    override fun requireCoins(): Double {
        return 50000.0
    }

    override fun requireRenown(level: Int): Double {
        return 50.0
    }

    override fun requirePrestige(): Int {
        return 20
    }

    override fun requireLevel(): Int {
        return 90
    }

    override fun getPerkType(): PerkType {
        return PerkType.MEGA_STREAK
    }

    override fun getDescription(player: Player): List<String> {
        val list: MutableList<String> = ArrayList()
        list.add("&7激活要求连杀数: &c100 连杀")
        list.add(" ")
        list.add("&7激活后:")
        list.add("  &a▶ &7获得神话物品的几率提升至原来的 &d150%")
        list.add("  &c▶ &7每击杀100名玩家,受到的伤害 &c+10%")
        list.add("  &c▶ &7100连杀后,攻击未精通玩家造成的伤害 &c-40%")
        list.add("  &c▶ &7200连杀后,生命上限 &c-2❤")
        list.add("  &c▶ &7300连杀后,所有药水效果持续时间 &c-50%")
        list.add("  &c▶ &7400连杀后,&c无法恢复生命值")
        list.add(" ")
        list.add("&7激活后死亡时:")
        list.add("  &a▶ &7若连杀数大于400,获得一件 &d登峰造极掉落物")
        list.add(" ")
        list.add("&c注意:若在400连杀前死亡,受连杀助理保护的神话物品仍然会掉落生命!")
        return list
    }

    override fun getMaxLevel(): Int {
        return 0
    }

    override fun onPerkActive(player: Player?) {}

    override fun onPerkInactive(player: Player?) {}

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onRegen(event: EntityRegainHealthEvent) {
        val player = event.entity
        if (player is Player) {
            val profile = PlayerProfile.getPlayerProfileByUuid(player.uniqueId)
            if (!profile.isLoaded) {
                return
            }

            if (!PlayerUtil.isPlayerChosePerk(player, "uber_streak")) {
                return
            }

            if (profile.streakKills < 400) {
                return
            }

            event.isCancelled = true
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onRegen(event: PitRegainHealthEvent) {
        val player = event.player
        val profile = PlayerProfile.getPlayerProfileByUuid(player.uniqueId)
        if (!profile.isLoaded) {
            return
        }

        if (!PlayerUtil.isPlayerChosePerk(player, "uber_streak")) {
            return
        }

        if (profile.streakKills < 400) {
            return
        }

        event.isCancelled = true
    }

    /**
     * 100 连杀负面效果 和 激活后每击杀100名玩家的负面效果
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onDamage(event: EntityDamageByEntityEvent) {
        val victim = event.entity
        val damager = event.damager

        if (damager is Player && victim is Player) {
            val hasUber = this.hasUber(damager)
            if (hasUber) {
                val profile = damager.getPitProfile()
                if (profile.streakKills >= 100) {
                    val victimProfile = victim.getPitProfile()
                    if (victimProfile.prestige < 1) {
                        event.damage = event.damage * 0.6
                    }
                }
            }

            val victimHasUber = this.hasUber(victim)
            if (victimHasUber) {
                val victimProfile = victim.getPitProfile()
                event.damage = event.damage * (1 + victimProfile.streakKills / 1000)
            }
        }
    }

    /**
     * 100 连杀播报 以及 200连杀负面效果
     */
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    fun onStreak(event: PitStreakKillChangeEvent) {
        val player = Bukkit.getPlayer(event.playerProfile.playerUuid) ?: return
        if (!hasUber(player)) return

        if (event.from < 100 && event.to >= 100) {
            CC.boardCast(
                MessageType.COMBAT,
                "&c&l超级连杀! " + event.playerProfile.formattedNameWithRoman + " &7激活了 &c&l登峰造极 &7!"
            )
            Bukkit.getOnlinePlayers().forEach { online: Player ->
                online.playSound(
                    online.location,
                    Sound.WITHER_SPAWN,
                    0.8f,
                    1.5f
                )
            }
        }

        if (event.from < 200 && event.to >= 200) {
            player.maxHealth = player.maxHealth - 4
        }
    }

    /**
     * 死亡掉落以及恢复数据
     */
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    fun onPlayerBeKilled(event: PitKillEvent) {
        val victim = event.target
        if (victim !is Player) return

        if (!victim.isOnline) {
            return
        }

        if (!hasUber(victim)) return

        val profile = victim.getPitProfile()
        if (profile.streakKills >= 400) {


            val refreshedTime = profile.todayCompletedUberLastRefreshed
            val todayCalendar = Calendar.getInstance()
            todayCalendar.timeInMillis = System.currentTimeMillis()

            val lastRefreshedCalendar = Calendar.getInstance()
            lastRefreshedCalendar.timeInMillis = refreshedTime

            if (todayCalendar.get(Calendar.YEAR) != lastRefreshedCalendar.get(Calendar.YEAR) ||
                todayCalendar.get(Calendar.DAY_OF_YEAR) != lastRefreshedCalendar.get(Calendar.DAY_OF_YEAR)) {
                profile.todayCompletedUberLastRefreshed = System.currentTimeMillis()
                profile.todayCompletedUber = 0
            }

            profile.todayCompletedUber++

            when(profile.todayCompletedUber) {
                2 -> {
                    if (profile.renown < 5) {
                        victim.sendMessage(CC.translate("&c登峰造极! &c你的声望不足以支付你今日的第二次&d登峰造极&c奖励门槛"))
                        return
                    }
                    profile.renown -= 5
                }
                3 -> {
                    if (profile.renown < 10) {
                        victim.sendMessage(CC.translate("&c登峰造极! &c你的声望不足以支付你今日的第三次&d登峰造极&c奖励门槛"))
                        return
                    }
                    profile.renown -= 10
                }
                4 -> {
                    if (profile.renown < 15) {
                        victim.sendMessage(CC.translate("&c登峰造极! &c你的声望不足以支付你今日的第四次&d登峰造极&c奖励门槛"))
                        return
                    }
                    profile.renown -= 15
                }
                1 -> {

                }
                else -> {
                    victim.sendMessage(CC.translate("&c登峰造极! &c你今日完成次数已达上限!"))
                    return
                }
            }

            val itemStack = UberDrop()

            if (InventoryUtil.isInvFull(victim.inventory)) {
                victim.sendMessage(CC.translate("&c由于你的背包已满, 物品已&4掉落至地面&c."))
                victim.world.dropItemNaturally(victim.location, itemStack.toItemStack())
            } else {
                victim.inventory.addItem(itemStack.toItemStack())
            }

            victim.maxHealth = profile.maxHealth
        }
    }


    /**
     * 300连杀 负面效果
     *     */
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    fun onPotionAdd(event: PotionAddEvent) {
        val player = event.player

        if (player !is Player) return
        if (!hasUber(player)) return
        val profile = player.getPitProfile()
        if (profile.streakKills < 300) return

        val effect = event.effect
        val ambient = effect.isAmbient

        if (!ambient) {
            return
        }

        event.isCancelled = true


        val type = effect.type
        val duration = effect.duration / 2
        val amplifier = effect.amplifier


        player.addPotionEffect(PotionEffect(type, duration, amplifier, false))
    }


    @EventHandler
    private fun onSpawn(event: PitPlayerSpawnEvent) {
        event.player.getPitProfile().streakKills = 0.0
    }

    private fun hasUber(player: Player): Boolean {
        return PlayerUtil.isPlayerChosePerk(player, "uber_streak")
    }

    override fun getStreakNeed(): Int {
        return 100
    }


}