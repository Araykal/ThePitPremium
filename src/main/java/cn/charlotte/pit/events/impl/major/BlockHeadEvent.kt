package cn.charlotte.pit.events.impl.major

import cn.charlotte.pit.ThePit
import cn.charlotte.pit.config.NewConfiguration.eventOnlineRequired
import cn.charlotte.pit.data.PlayerProfile
import cn.charlotte.pit.data.sub.PlacedBlockData
import cn.charlotte.pit.enchantment.type.rare.PaparazziEnchant
import cn.charlotte.pit.event.PitKillEvent
import cn.charlotte.pit.event.PitProfileLoadedEvent
import cn.charlotte.pit.events.IEpicEvent
import cn.charlotte.pit.events.IEvent
import cn.charlotte.pit.events.IScoreBoardInsert
import cn.charlotte.pit.item.type.mythic.MythicLeggingsItem
import cn.charlotte.pit.runnable.ClearRunnable
import cn.charlotte.pit.util.PlayerUtil
import cn.charlotte.pit.util.chat.CC
import cn.charlotte.pit.util.chat.MessageType
import cn.charlotte.pit.util.cooldown.Cooldown
import cn.charlotte.pit.util.hologram.reflection.MathUtil.floor
import cn.charlotte.pit.util.item.ItemBuilder
import cn.charlotte.pit.util.time.TimeUtil
import eu.decentsoftware.holograms.api.DHAPI
import eu.decentsoftware.holograms.api.holograms.Hologram
import eu.decentsoftware.holograms.api.holograms.HologramLine
import net.minecraft.server.v1_8_R3.*
import org.bukkit.*
import org.bukkit.Material
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable
import java.text.DecimalFormat
import java.util.*
import java.util.concurrent.TimeUnit


/*
 * @ Created with IntelliJ IDEA
 * @ Author EmptyIrony
 * @ Date 2021/8/6
 * @ Time 21:49
 */

class BlockHeadEvent : IEvent, IEpicEvent, IScoreBoardInsert, Listener {
    companion object {
        @JvmStatic
        private val format = DecimalFormat("##.##")

        @JvmStatic
        fun getType(uuid: UUID): Material? {
            val event = ThePit.getInstance().eventFactory.activeEpicEvent
            if (event is BlockHeadEvent) {
                val data = event.cache[uuid] ?: return null
                return data.block
            }
            return null
        }

        @JvmStatic
        fun getData(uuid: UUID): Short? {
            val event = ThePit.getInstance().eventFactory.activeEpicEvent
            if (event is BlockHeadEvent) {
                val data = event.cache[uuid] ?: return null
                return data.data.toShort()
            }
            return null
        }
    }

    lateinit var timer: Cooldown
    val globalCache = HashMap<Location, BlockCache>()
    val locationToPlayer = HashMap<Location, UUID>()
    val cache = HashMap<UUID, BlockHeadData>()
    val buffCache = ArrayList<BuffData>()
    val runnable: BukkitRunnable by lazy {
        val runnable = object : BukkitRunnable() {
            override fun run() {
                if (timer.hasExpired()) {
                    ThePit.getInstance()
                        .eventFactory
                        .inactiveEvent(this@BlockHeadEvent)
                    cancel()
                    return
                }

                for (player in Bukkit.getOnlinePlayers()) {
                    val cooldown = quickTrail[player.uniqueId] ?: continue
                    if (cooldown.hasExpired()) {
                        quickTrail.remove(player.uniqueId)
                    }
                }
            }
        }
        runnable
    }
    val woolColorNotUsed: HashSet<Int> by lazy {
        val set = HashSet<Int>()
        for (index in 0..15) {
            set.add(index)
        }
        set
    }
    val glassColorNotUsed: HashSet<Int> by lazy {
        val set = HashSet<Int>()
        for (index in 0..15) {
            set.add(index)
        }
        set
    }
    private val blockList = Material.values()
        .filter {
            it.isSolid && it != Material.SAND && it != Material.GRAVEL
                    && it != Material.LEAVES && it != Material.BED_BLOCK
                    && (it.id !in 33..34) && !it.name.contains("SIGN")
                    && !it.name.contains("DOOR") && it != Material.WORKBENCH
                    && !it.name.contains("CHEST") && !it.name.contains("STAIRS")
                    && it != Material.MOB_SPAWNER && !it.name.contains("ICE")
                    && it != Material.GLOWING_REDSTONE_ORE && !it.name.contains("FURNACE")
                    && !it.name.contains("STEP") && !it.name.contains("PISTON")
                    && !it.name.contains("WALL") && it != Material.ANVIL && !it.name.contains("FENCE")
                    && it != Material.DISPENSER && it != Material.ENCHANTMENT_TABLE && !it.name.contains("PLATE")
                    && it != Material.STAINED_GLASS_PANE && it != Material.DRAGON_EGG && !it.name.contains("BANNER")
                    && it != Material.SLIME_BLOCK && it != Material.BEACON && it != Material.CACTUS
                    && !it.name.contains("SLAB") && !it.name.contains("LEAVES")
                    && !it.name.contains("DETECTOR") && it != Material.TNT
                    && it != Material.THIN_GLASS && it != Material.BREWING_STAND
                    && it != Material.HOPPER && it != Material.HOPPER_MINECART
                    && it != Material.SNOW_BLOCK && it != Material.SNOW
                    && it.id != 158
        }
        .toMutableList()
    val quickTrail = HashMap<UUID, Cooldown>()

    override fun getEventInternalName(): String {
        return "block_head"
    }

    override fun getEventName(): String {
        return "&9方块划地战"
    }

    override fun requireOnline(): Int {
        return eventOnlineRequired[eventInternalName]!!
    }

    fun onMove(player: Player, from: Location, to: Location) {
        for (data in buffCache) {
            if (!data.cooldown.hasExpired()) continue

            if (to.distanceSquared(data.location) <= 2 * 2) {
                if (data.type == BuffType.QUICK_TRAIL) {
                    this.quickTrail[player.uniqueId] = Cooldown(30, TimeUnit.SECONDS)
                    player.addPotionEffect(PotionEffect(PotionEffectType.SPEED, 20 * 30, 1, true, true))
                    CC.send(MessageType.EVENT, player, "&9&l捡起! &7你获得了30秒的 &f速度 II&7, 以及你经过的地方将被你染色!")
                }
                if (data.type == BuffType.SUPER_HEAL) {
                    player.health = player.maxHealth
                    val entityPlayer = (player as CraftPlayer).handle
                    entityPlayer.absorptionHearts += 6.0F
                    CC.send(MessageType.EVENT, player, "&9&l捡起! &7你获得了3颗心的额外生命!并且你的血量已恢复最大值")
                }
                if (data.type == BuffType.DIAMOND_ARMOR) {
                    val inv = player.inventory
                    if (inv.helmet == null || inv.helmet.type != Material.DIAMOND_HELMET) {
                        inv.helmet = ItemBuilder(Material.DIAMOND_HELMET)
                            .removeOnJoin(true)
                            .deathDrop(true)
                            .canTrade(false)
                            .canDrop(false)
                            .buildWithUnbreakable()
                    }
                    if (inv.chestplate == null || inv.chestplate.type != Material.DIAMOND_CHESTPLATE) {
                        inv.chestplate = ItemBuilder(Material.DIAMOND_CHESTPLATE)
                            .removeOnJoin(true)
                            .deathDrop(true)
                            .canTrade(false)
                            .canDrop(false)
                            .buildWithUnbreakable()
                    }
                    if (inv.boots == null || inv.boots.type != Material.DIAMOND_BOOTS) {
                        inv.boots = ItemBuilder(Material.DIAMOND_BOOTS)
                            .removeOnJoin(true)
                            .deathDrop(true)
                            .canTrade(false)
                            .canDrop(false)
                            .buildWithUnbreakable()
                    }

                    if (inv.leggings == null || (inv.leggings.type != Material.LEATHER_LEGGINGS && inv.leggings.type != Material.DIAMOND_LEGGINGS)) {
                        inv.leggings = ItemBuilder(Material.DIAMOND_LEGGINGS)
                            .removeOnJoin(true)
                            .deathDrop(true)
                            .canTrade(false)
                            .canDrop(false)
                            .buildWithUnbreakable()
                    }
                    player.playSound(player.location, Sound.HORSE_ARMOR, 1F, 1F)
                    CC.send(MessageType.EVENT, player, "&9&l捡起! &7你已装备钻石套装!")
                }
                player.playSound(player.location, Sound.LEVEL_UP, 4F, 2F)
                data.cooldown = Cooldown(30, TimeUnit.SECONDS)
                for (target in Bukkit.getOnlinePlayers()) {
                    data.changeItem(false)
                }

                Bukkit.getScheduler().runTaskLaterAsynchronously(ThePit.getInstance(), {
                    for (target in Bukkit.getOnlinePlayers()) {
                        data.changeItem(true)
                    }
                }, 20 * 30L)
                break
            }
        }


        val cooldown = quickTrail[player.uniqueId] ?: return
        if (cooldown.hasExpired()) {
            quickTrail.remove(player.uniqueId)
            return
        }
        val profile = PlayerProfile.getPlayerProfileByUuid(player.uniqueId)
        if (!profile.isInArena) {
            return
        }

        if (player.isOnGround) {
            val location = player.location.block.location.clone().add(0.0, -1.0, 0.0)
            for (index in 1..5) {
                if (location.clone().add(0.0, -index.toDouble(), 0.0).block.type == Material.AIR) {
                    return
                }
            }
            if (!globalCache.containsKey(location)) {
                globalCache[location] = BlockCache(location.block.type, location.block.data)
            }
            val uuid = locationToPlayer[location]
            if (uuid != null) {
                val data = cache[uuid]
                data?.gotBlocks?.remove(location)
            }

            val data = cache[player.uniqueId] ?: return
            locationToPlayer[location] = data.uuid
            data.gotBlocks.add(location)
            data.block.let {
                location.block.type = it
                location.block.data = data.data.toByte()
            }
        }
    }

    @EventHandler
    fun whenLoaded(event: PitProfileLoadedEvent) {
        val profile = event.playerProfile
        val index = kotlin.random.Random.nextInt(blockList.size)
        val material = blockList[index]
        blockList.removeAt(index)

        val uuid = profile.playerUuid

        cache[uuid] = BlockHeadData(uuid, material)

        val player = Bukkit.getPlayer(uuid) ?: return
        if (!player.isOnline) {
            return
        }

        for (data in buffCache) {
            if (!data.cooldown.hasExpired()) {
                data.changeItem(false)
            }
        }
    }

    @EventHandler
    fun whenKill(event: PitKillEvent) {
        val killData = cache[event.killer.uniqueId] ?: return
        val targetData = cache[event.target.uniqueId] ?: return



        for (block in targetData.gotBlocks) {
            block.block.type = killData.block
            block.block.data = killData.data.toByte()
            killData.gotBlocks.add(block)
        }
        targetData.gotBlocks.clear()

        val profile = PlayerProfile.getPlayerProfileByUuid(killData.uuid)
        if (!profile.isInArena) {
            return
        }

        val killer = event.killer
        val location = killer.location
        val now = location.block
        val down1 = location.clone().add(0.0, -1.0, 0.0).block
        val down2 = location.clone().add(0.0, -2.0, 0.0).block
        val down3 = location.clone().add(0.0, -3.0, 0.0).block

        val down2IsAir = isAir(down2.location)
        val down1IsAir = isAir(down1.location)

        //trash code, but I don't know how to resolve it
        if (isAir(now.location) || down1IsAir) {
            if (down1IsAir || down2IsAir) {
                if (down2IsAir || isAir(down3.location)) {
                    return
                } else {
                    generateBlock(killData, down2.location)
                }
            } else {
                generateBlock(killData, down1.location)
            }
        } else {
            generateBlock(killData, now.location)
        }

        location.world.playSound(location, Sound.EXPLODE, 4F, 1F)
        location.world.playEffect(location, Effect.EXPLOSION_HUGE, 1)

        val dropped = ArrayList<EntityFallingBlock>()
        for (index in 0 until 10) {
            dropped.add(this.generateFallingBlocks(location, killData.block, killData.data.toByte()))
        }
        Bukkit.getScheduler().runTaskLaterAsynchronously(ThePit.getInstance(), {
            for (block in dropped) {
                for (player in Bukkit.getOnlinePlayers()) {
                    (player as CraftPlayer).handle.playerConnection.sendPacket(PacketPlayOutEntityDestroy(block.id))
                }
            }
        }, 10)

    }

    private fun isAir(location: Location): Boolean {
        return location.block.type == Material.AIR || ClearRunnable.getClearRunnable().placedBlock.contains(
            PlacedBlockData(location, Cooldown(0L))
        )
    }

    private fun generateBlock(data: BlockHeadData, location: Location) {
        for (xOffset in 5 downTo -5) {
            for (zOffset in 5 downTo -5) {
                val offsetLoc = location.clone().add(xOffset.toDouble(), 0.0, zOffset.toDouble()).block.location
                val material = globalCache[offsetLoc]
                if (material != null) {
                    val uuid = locationToPlayer[offsetLoc]
                    if (uuid != null) {
                        val targetData = cache[uuid]
                        targetData?.gotBlocks?.remove(offsetLoc)
                    }
                } else {
                    globalCache[offsetLoc] = BlockCache(offsetLoc.block.type, offsetLoc.block.data)
                }

                locationToPlayer[offsetLoc] = data.uuid
                offsetLoc.block.type = data.block
                offsetLoc.block.data = data.data.toByte()
                data.gotBlocks.add(offsetLoc)
            }
        }
    }

    private fun generateFallingBlocks(location: Location, type: Material, byte: Byte): EntityFallingBlock {
        val x = kotlin.random.Random.nextDouble() - 0.5

        val y = -3.0 + Math.random() * 2.0

        val z = kotlin.random.Random.nextDouble() - 0.5

        val world = (location.world as CraftWorld).handle
        val blockX = location.blockX
        val blockY = location.blockY
        val blockZ = location.blockZ

        val fallingBlock = EntityFallingBlock(
            world,
            blockX.toDouble(),
            blockY.toDouble(), blockZ.toDouble(), Block.getById(type.id).fromLegacyData(0)
        )
        fallingBlock.motX = x
        fallingBlock.motY = y
        fallingBlock.motZ = z
        fallingBlock.velocityChanged = true

        val spawnPacket = PacketPlayOutSpawnEntity(fallingBlock, 70, Block.getCombinedId(fallingBlock.block))
        val vectorPacket = PacketPlayOutEntityVelocity(fallingBlock)


        for (player in PlayerUtil.getNearbyPlayers(location, 30.0)) {
            val entityPlayer = (player as CraftPlayer).handle
            entityPlayer.playerConnection.sendPacket(spawnPacket)
            entityPlayer.playerConnection.sendPacket(vectorPacket)
        }

        return fallingBlock
    }

    override fun onActive() {
        timer = Cooldown(5, TimeUnit.MINUTES)

        val config = ThePit.getInstance().pitConfig
        for (location in config.blockHeadLocations) {
            buffCache.add(
                BuffData()
                    .also {
                        it.cooldown = Cooldown(0)
                        it.location = location
                        it.type = BuffType.values()[kotlin.random.Random.nextInt(BuffType.values().size)]
                    }
            )
        }
        for (data in buffCache) {
            data.spawn()
        }

        for (player in Bukkit.getOnlinePlayers()) {
            val index = kotlin.random.Random.nextInt(blockList.size)
            val material = blockList[index]
            if (material == Material.WOOL) {
                if (woolColorNotUsed.size == 0) {
                    continue
                }
                val get = woolColorNotUsed.toList()[kotlin.random.Random.nextInt(woolColorNotUsed.size)]
                woolColorNotUsed.remove(get)
                cache[player.uniqueId] = BlockHeadData(player.uniqueId, material)
                    .also {
                        it.data = get
                    }
                if (woolColorNotUsed.size == 0) {
                    blockList.remove(Material.WOOL)
                }
            } else {
                blockList.removeAt(index)
                cache[player.uniqueId] = BlockHeadData(player.uniqueId, material)
            }


        }

        for (data in ClearRunnable.getClearRunnable().placedBlock) {
            data.location.block.type = Material.AIR
        }

        for (self in Bukkit.getOnlinePlayers()) {
            for (target in Bukkit.getOnlinePlayers()) {
                val data = cache[target.uniqueId] ?: continue
                (self as CraftPlayer).handle.playerConnection.sendPacket(
                    PacketPlayOutEntityEquipment(
                        target.entityId,
                        4,
                        CraftItemStack.asNMSCopy(
                            ItemStack(data.block, 1, data.data.toShort())
                        )
                    )
                )
            }
        }

        Bukkit.getPluginManager().registerEvents(this, ThePit.getInstance())

        runnable.runTaskTimer(ThePit.getInstance(), 20L, 20L)
    }

    override fun onInactive() {
        HandlerList.unregisterAll(this)
        runnable.cancel()

        for (data in buffCache) {
            data.despawn()
        }

        for (self in Bukkit.getOnlinePlayers()) {
            for (target in Bukkit.getOnlinePlayers()) {
                (self as CraftPlayer).handle.playerConnection.sendPacket(
                    PacketPlayOutEntityEquipment(
                        target.entityId,
                        4,
                        CraftItemStack.asNMSCopy(target.inventory.helmet))
                    )
            }


            val profile = PlayerProfile.getPlayerProfileByUuid(self.uniqueId)
            val data = cache[self.uniqueId] ?: continue
        }

        val list = this.cache.values.sortedByDescending {
            it.gotBlocks.size
        }

        val rank = HashMap<UUID, Int>()

        for ((rankNumber, data) in list.withIndex()) {
            rank[data.uuid] = rankNumber
        }

        val success = globalCache.size >= 100000

        for (player in Bukkit.getOnlinePlayers()) {
            val i = rank[player.uniqueId] ?: continue
            val data = cache[player.uniqueId] ?: continue

            val profile = PlayerProfile.getPlayerProfileByUuid(player.uniqueId) ?: continue

            var rewardCoins = 0
            var rewardRenown = 0
            val integer: Int = (rank[data.uuid] ?: continue) + 1
            if (integer <= 3) {
                rewardCoins += 2500
                rewardRenown += 2
            } else if (integer <= 20) {
                rewardCoins += 1500
                rewardRenown += 1
            } else {
                rewardCoins += 200
            }
            if (ThePit.getInstance().pitConfig.isGenesisEnable && profile.genesisData.tier >= 5 && rewardRenown > 0) {
                rewardRenown++
            }
            var enchantBoostLevel = PaparazziEnchant().getItemEnchantLevel(player.inventory.leggings)
            if (PlayerUtil.shouldIgnoreEnchant(player)) {
                enchantBoostLevel = 0
            }
            if (enchantBoostLevel > 0) {
                rewardCoins += (0.5 * enchantBoostLevel * rewardCoins).toInt()
                rewardRenown += floor(0.5 * enchantBoostLevel * rewardRenown).toInt()
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
                if (integer <= 5) {
                    rewardCoins += 5000
                } else if (integer <= 10) {
                    rewardCoins += 2500
                } else if (integer <= 15) {
                    rewardCoins += 1000
                }
            }

            if (success) {
                rewardCoins *= 2
            }
            profile.grindCoins(rewardCoins.toDouble())
            profile.coins = profile.coins + rewardCoins
            profile.renown = profile.renown + rewardRenown


            player.sendMessage(CC.GOLD + CC.CHAT_BAR)
            player.sendMessage(CC.translate("&6你的奖励: &6+${rewardCoins}硬币 &e+${rewardRenown}声望"))
            if (globalCache.size >= 100000) {
                player.sendMessage(CC.translate("&6&l全局奖励: &a&l成功! &7所有人获得的金币翻倍!"))
            } else {
                player.sendMessage(CC.translate("&6&l全局奖励: &c&l失败! &7全服只占领了 &c${globalCache.size} &7个方块"))
            }
            player.sendMessage(CC.translate("&6你的战绩: &7共占领了 ${data.gotBlocks.size} 个方块! 排名 &8[#${i + 1}]"))
            player.sendMessage(CC.translate("&6&l顶级玩家: "))
            if (list.size > 3) {
                for (index in 0..2) {
                    val headData = list[index]
                    val profile = PlayerProfile.getPlayerProfileByUuid(headData.uuid)
                    player.sendMessage(CC.translate("&6${index + 1} - ${profile.formattedNameWithRoman} &9共占领了&e ${headData.gotBlocks.size} &9个方块"))
                }
            }
            player.sendMessage(CC.GOLD + CC.CHAT_BAR)
        }

        for (entry in this.globalCache.entries) {
            entry.key.block.type = entry.value.material
            entry.key.block.data = entry.value.data
        }
    }

    override fun insert(player: Player): MutableList<String> {
        val data = cache[player.uniqueId] ?: return ArrayList()
        val size = data.gotBlocks.size

        return arrayListOf(
            "&f剩余时间: &a${TimeUtil.millisToRoundedTime(timer.remaining)}",
            "&f已占领方块: &a${size} &7(${if (globalCache.size == 0) "0" else format.format(size.toDouble() / globalCache.size.toDouble())}%)"
        )
    }

    class BlockHeadData(
        val uuid: UUID,
        val block: Material
    ) {
        var data = 0
        val gotBlocks = HashSet<Location>()
    }

    class BlockCache(val material: Material, val data: Byte)

    class BuffData {
        lateinit var uuid: UUID
        lateinit var hologram: Hologram
        lateinit var type: BuffType
        lateinit var location: Location
        var cooldown = Cooldown(0)

        fun spawn() {
            val add = location.clone().add(0.0, 1.0, 0.0)

            hologram = DHAPI.createHologram(uuid.toString(), add, false)
            val page = hologram.addPage()
            page.addLine(HologramLine(page, location, CC.translate(type.display)))
            page.addLine(HologramLine(page, location, "#ICON:${type.name}"))

            hologram.showAll()
        }

        fun changeItem(boolean: Boolean) {
            val page = hologram.getPage(0)


            if (boolean) {
                page.getLine(0).setContent(CC.translate(type.display))
                page.getLine(1).setContent("#ICON:${type.name}")
            } else {
                page.getLine(0).setContent(CC.translate("${CC.translate(type.display)} &7-&c 冷却中"))
                page.getLine(1).setContent("#ICON:${Material.BARRIER.name}")
            }
        }

        fun despawn() {
            DHAPI.removeHologram(uuid.toString())
        }
    }

    enum class BuffType(val display: String, val material: Material) {
        QUICK_TRAIL("&a&l快速染径", Material.INK_SACK),
        SUPER_HEAL("&e&l超级回复", Material.GOLDEN_APPLE),
        DIAMOND_ARMOR("&b&l钻石护甲", Material.DIAMOND_HELMET)
    }


}