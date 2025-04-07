package cn.charlotte.pit.menu.cactus

import cn.charlotte.pit.ThePit
import cn.charlotte.pit.data.PlayerProfile
import cn.charlotte.pit.data.sub.EnchantmentRecord
import cn.charlotte.pit.enchantment.AbstractEnchantment
import cn.charlotte.pit.enchantment.rarity.EnchantmentRarity
import cn.charlotte.pit.enchantment.type.rage.NewDealEnchant
import cn.charlotte.pit.enchantment.type.ragerare.Regularity
import cn.charlotte.pit.item.IMythicItem
import cn.charlotte.pit.item.MythicColor
import cn.charlotte.pit.item.type.mythic.MythicLeggingsItem
import cn.charlotte.pit.util.PlayerUtil
import cn.charlotte.pit.util.Utils
import cn.charlotte.pit.util.chat.CC
import cn.charlotte.pit.util.chat.ChatComponentBuilder
import cn.charlotte.pit.util.inventory.InventoryUtil
import cn.charlotte.pit.util.menu.Button
import cn.charlotte.pit.util.menu.Menu
import net.md_5.bungee.api.chat.BaseComponent
import net.md_5.bungee.api.chat.HoverEvent
import net.md_5.bungee.api.chat.TextComponent
import net.minecraft.server.v1_8_R3.NBTTagCompound
import org.bukkit.Bukkit
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.inventory.ItemStack
import java.util.concurrent.ThreadLocalRandom

/**
 * @author Araykal
 * @since 2025/1/16
 */
class SacrosanctCactusMenu : Menu() {

    override fun getTitle(player: Player): String {
        return "神圣仙人掌 (选择其一)"
    }

    override fun getButtons(player: Player): Map<Int, Button> {
        PlayerUtil.takeOneItemInHand(player)
        player.sendMessage(CC.translate("&a成功使用，请不要关闭领取界面，请选择您的奖励！"))
        val button: MutableMap<Int, Button> = HashMap()
        for (i in 0 until 9) {
            button[i] = pantsButton
        }
        return button
    }

    private val pantsButton: Button
        get() = object : Button() {

            private val colorMaxLiveMap = mapOf(
                MythicColor.DARK to 40,
                MythicColor.RED to 24,
                MythicColor.ORANGE to 24,
                MythicColor.BLUE to 24,
                MythicColor.GREEN to 24,
                MythicColor.YELLOW to 24,
                MythicColor.AQUA to 24,
                MythicColor.RAGE to 24
            )

            private val colorTierMap = mapOf(
                MythicColor.DARK to 2,
                MythicColor.RED to 1,
                MythicColor.ORANGE to 1,
                MythicColor.BLUE to 1,
                MythicColor.GREEN to 1,
                MythicColor.YELLOW to 1,
                MythicColor.AQUA to 1,
                MythicColor.RAGE to 1
            )

            override fun getButtonItem(player: Player): ItemStack {
                val enchantments: MutableList<Map<AbstractEnchantment, Int>> = ArrayList()
                val possibleEnchantments: MutableList<AbstractEnchantment> = ThePit.getApi().armorEnchantments()
                val random = ThreadLocalRandom.current()
                val colors = arrayOf(
                    MythicColor.RED,
                    MythicColor.DARK,
                    MythicColor.ORANGE,
                    MythicColor.BLUE,
                    MythicColor.GREEN,
                    MythicColor.YELLOW,
                    MythicColor.AQUA,
                    MythicColor.RAGE
                )

                fun populateEnchantments() {
                    enchantments.clear()
                    for (possibleEnchantment in possibleEnchantments) {
                        val enchantment: MutableMap<AbstractEnchantment, Int> = HashMap()
                        val probability = when {
                            possibleEnchantment.rarity == EnchantmentRarity.RAGE_RARE -> 0.01
                            possibleEnchantment.rarity == EnchantmentRarity.RAGE -> 0.1
                            possibleEnchantment.rarity == EnchantmentRarity.DARK_RARE -> 0.02
                            possibleEnchantment.rarity == EnchantmentRarity.DARK_NORMAL -> 0.1
                            possibleEnchantment.rarity == EnchantmentRarity.RARE -> 0.1
                            possibleEnchantment.rarity == EnchantmentRarity.NORMAL -> 0.5
                            possibleEnchantment is Regularity || possibleEnchantment is NewDealEnchant -> 0.01
                            else -> 0.0
                        }
                        if (random.nextDouble() < probability) {
                            enchantment[possibleEnchantment] = 1
                            enchantments.add(enchantment)
                        }
                    }
                }

                do {
                    populateEnchantments()
                } while (enchantments.isEmpty())

                val color = colors[random.nextInt(colors.size)]
                val item: IMythicItem = MythicLeggingsItem()
                item.color = color
                item.apply {
                    val randomEnchantmentMap = enchantments[random.nextInt(enchantments.size)]
                    item.enchantments = randomEnchantmentMap
                    item.tier = colorTierMap[color] ?: 1
                    item.maxLive = colorMaxLiveMap[color] ?: 24
                    item.live = colorMaxLiveMap[color] ?: 0
                    item.enchantmentRecords += EnchantmentRecord(
                        player.name,
                        "神圣仙人掌",
                        System.currentTimeMillis()
                    )
                    if (item.enchantments.any { it.key.rarity == EnchantmentRarity.DARK_RARE || it.key.rarity == EnchantmentRarity.DARK_NORMAL } && item.color != MythicColor.DARK) {
                        item.color = MythicColor.DARK
                    }
                    if (item.enchantments.any { it.key.rarity == EnchantmentRarity.RAGE_RARE || it.key.rarity == EnchantmentRarity.RAGE } && item.color != MythicColor.RAGE) {
                        item.color = MythicColor.RAGE
                    }
                }
                if (item.color == MythicColor.RAGE && item.enchantments.any { it.key.rarity != EnchantmentRarity.RAGE && it.key.rarity != EnchantmentRarity.RAGE_RARE }) {
                    item.color = MythicColor.RED
                    item.live = 24
                    item.maxLive = 24
                    item.tier = 1
                }
                if (item.color == MythicColor.DARK && item.enchantments.any { it.key.rarity != EnchantmentRarity.DARK_RARE && it.key.rarity != EnchantmentRarity.DARK_NORMAL }) {
                    item.color = MythicColor.BLUE
                    item.live = 24
                    item.maxLive = 24
                    item.tier = 1
                }
                if (item.color == MythicColor.DARK && (item.tier == 1 || item.live == 24 || item.maxLive == 24)) {
                    item.live = 40
                    item.maxLive = 40
                    item.tier = 2
                }
                return item.toItemStack()
            }


            override fun clicked(
                player: Player,
                slot: Int,
                clickType: ClickType,
                hotbarButton: Int,
                currentItem: ItemStack
            ) {
                if (InventoryUtil.isInvFull(player)) {
                    player.sendMessage(CC.translate("&c你的背包已满！"))
                    return
                }
                if (Utils.getMythicItem(currentItem).enchantments.any { it.key.rarity == EnchantmentRarity.DARK_RARE || it.key.rarity == EnchantmentRarity.RAGE_RARE || it.key.rarity == EnchantmentRarity.RARE }) {
                    val profile = PlayerProfile.getPlayerProfileByUuid(player.uniqueId)

                    val nms = CraftItemStack.asNMSCopy(currentItem)
                    val tag = NBTTagCompound()
                    nms.save(tag)
                    val hoverEventComponents = arrayOf<BaseComponent>(
                        TextComponent(tag.toString())
                    )
                    for (p in Bukkit.getOnlinePlayers()) {
                        p.spigot().sendMessage(
                            *ChatComponentBuilder(
                                CC.translate(
                                    "&d&l稀有附魔! &7" + profile.formattedNameWithRoman + " &7在神圣仙人掌中抽到了稀有物品: " + currentItem
                                        .itemMeta.displayName + " &e[查看]"
                                )
                            )
                                .setCurrentHoverEvent(HoverEvent(HoverEvent.Action.SHOW_ITEM, hoverEventComponents))
                                .create()
                        )
                    }
                }

                player.inventory.addItem(currentItem)
                player.closeInventory()
                player.sendMessage(CC.translate("&a&l奖励已领取！ " + currentItem.itemMeta.displayName))
            }
        }
}
