package cn.charlotte.pit.item.type

import cn.charlotte.pit.ThePit
import cn.charlotte.pit.data.sub.EnchantmentRecord
import cn.charlotte.pit.enchantment.param.item.WeaponOnly
import cn.charlotte.pit.enchantment.rarity.EnchantmentRarity
import cn.charlotte.pit.event.PitKillEvent
import cn.charlotte.pit.event.PitPlayerSpawnEvent
import cn.charlotte.pit.event.PitProfileLoadedEvent
import cn.charlotte.pit.getPitProfile
import cn.charlotte.pit.item.IMythicItem
import cn.charlotte.pit.item.type.mythic.MythicSwordItem
import cn.charlotte.pit.parm.AutoRegister
import cn.charlotte.pit.reflectGetNmsItem
import cn.charlotte.pit.util.chat.CC
import cn.charlotte.pit.util.chat.ChatComponentBuilder
import cn.charlotte.pit.util.item.ItemBuilder
import cn.charlotte.pit.util.item.ItemUtil
import cn.charlotte.pit.util.random.RandomUtil
import net.md_5.bungee.api.chat.BaseComponent
import net.md_5.bungee.api.chat.HoverEvent
import net.md_5.bungee.api.chat.TextComponent
import net.minecraft.server.v1_8_R3.NBTTagCompound
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.inventory.ItemStack
import kotlin.math.min
import kotlin.random.Random

@AutoRegister
class JewelSword : IMythicItem(), Listener {
    companion object {
        @JvmStatic
        private val KILL_TARGET = 117
    }

    var killed = 0

    override fun getInternalName(): String {
        return "jewel_sword"
    }

    override fun getItemDisplayName(): String {
        return "&d宝石之剑"
    }

    override fun getItemDisplayMaterial(): Material {
        return Material.GOLD_SWORD
    }

    override fun toItemStack(): ItemStack {
        val itemStack = super.toItemStack()

        if (killed >= KILL_TARGET) {
            val nmsCopy = itemStack.reflectGetNmsItem()
            nmsCopy?.tag?.getCompound("extra")?.setString("internal", "mythic_sword")

            return CraftItemStack.asCraftMirror(nmsCopy)
        }

        val lore = arrayListOf(
            "",
            "&9隐藏的宝石",
            "&7当您使用本武器连续击杀&a$KILL_TARGET&7名玩家后,",
            "&7本武器将会自动附魔至 T1,",
            "&7它将会必定包含一个满级附魔",
            "&7当前已完成: &c${killed}&7/&a$KILL_TARGET"
        )

        return ItemBuilder(itemStack)
            .lore(lore)
            .jewelSwordKills(killed)
            .itemDamage(6.5)
            .build()
    }

    override fun loadFromItemStack(item: ItemStack) {
        item.reflectGetNmsItem()?.tag?.getCompound("extra")?.getInt("killed")?.let {
            killed = it
        }
        super.loadFromItemStack(item)
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onDeath(event: PitProfileLoadedEvent) {
        val player = Bukkit.getPlayer(event.playerProfile.playerUuid) ?: return
        clearKill(player)
    }

    @EventHandler
    fun onSpawn(event: PitPlayerSpawnEvent) {
        val player = event.player
        clearKill(player)
    }

    @EventHandler(priority = EventPriority.HIGH)
    fun onDeath(event: PlayerDeathEvent) {
        val player = event.entity
        clearKill(player)
    }

    private fun clearKill(player: Player) {
        player.inventory.forEachIndexed { index, item ->
            item ?: return@forEachIndexed
            val name = ItemUtil.getInternalName(item) ?: return@forEachIndexed
            if (name == "jewel_sword") {
                val sword = JewelSword().also {
                    it.loadFromItemStack(item)
                }

                sword.killed = 0
                player.inventory.setItem(index, sword.toItemStack())
            }
        }

        if (player.itemOnCursor != null) {
            val itemOnCursor = player.itemOnCursor
            if ("jewel_sword" == ItemUtil.getInternalName(itemOnCursor)) {
                val jewelSword = JewelSword().also {
                    it.loadFromItemStack(itemOnCursor)
                }
                jewelSword.killed = 0
                player.itemOnCursor = jewelSword.toItemStack()
            }
        }
    }

    @EventHandler
    fun onKill(event: PitKillEvent) {
        val killer = event.killer
        val lastDamageCause = event.target.lastDamageCause
        if (lastDamageCause is EntityDamageByEntityEvent) {
            if (lastDamageCause.damager !is Player) {
                return
            }
        }

        val profile = killer.getPitProfile()
        val item = killer.itemInHand ?: return

        val name = ItemUtil.getInternalName(item)
        if ("jewel_sword" != name) {
            return
        }

        val sword = JewelSword()
        sword.loadFromItemStack(item)

        sword.killed += 1

        if (sword.killed >= KILL_TARGET) {
            val rare = RandomUtil.hasSuccessfullyByChance(0.05)
            val list = ThePit.getInstance()
                .enchantmentFactor
                .enchantments
                .filter {
                    it.javaClass.getAnnotation(WeaponOnly::class.java) != null
                }
                .filter {
                    if (rare) it.rarity == EnchantmentRarity.RARE else it.rarity == EnchantmentRarity.NORMAL
                }

            val enchant = list[RandomUtil.random.nextInt(list.size)]

            val enchantment = enchant.javaClass.newInstance()

            val newSword = MythicSwordItem().also {
                it.tier = 1
                it.maxLive = Random.nextInt(5) + 5
                it.live = it.maxLive
                it.color = color
                it.enchantments[enchantment] = min(3, enchant.maxEnchantLevel)

                it.enchantmentRecords += EnchantmentRecord(
                    killer.name,
                    "Jewel Sword",
                    System.currentTimeMillis()
                )
            }

            val itemStack = newSword.toItemStack()
            killer.itemInHand = itemStack

            if (enchantment.rarity == EnchantmentRarity.RARE) {
                val nms = item.reflectGetNmsItem()
                val tag = NBTTagCompound()
                nms?.save(tag)
                val hoverEventComponents = arrayOf<BaseComponent>(
                    TextComponent(tag.toString())
                )

                for (p in Bukkit.getOnlinePlayers()) {
                    p.spigot().sendMessage(
                        *ChatComponentBuilder(
                            CC.translate(
                                "&d&l稀有附魔! &7" + profile.formattedName + " &7通过 &9隐藏的宝石 &7附魔晋升出物品: " + itemStack
                                    .itemMeta.displayName + " &e[查看]"
                            )
                        )
                            .setCurrentHoverEvent(HoverEvent(HoverEvent.Action.SHOW_ITEM, hoverEventComponents))
                            .create()
                    )
                }
            }

            ThePit.getInstance().soundFactory.playSound("successfully", killer)
            killer.sendMessage(CC.translate("&d晋升! &7经过坚持与努力, 您的 &d宝石之剑 &7获得了附魔!"))
        } else {
            killer.itemInHand = sword.toItemStack()
        }
    }
}