package cn.charlotte.pit.menu.gem.button


import cn.charlotte.pit.data.sub.EnchantmentRecord
import cn.charlotte.pit.enchantment.AbstractEnchantment
import cn.charlotte.pit.enchantment.rarity.EnchantmentRarity
import cn.charlotte.pit.util.Utils
import cn.charlotte.pit.util.chat.CC
import cn.charlotte.pit.util.item.ItemBuilder
import cn.charlotte.pit.util.item.ItemUtil
import cn.charlotte.pit.util.menu.Button
import cn.charlotte.pit.util.toMythicItem
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.inventory.ItemStack


/*
 * @ Created with IntelliJ IDEA
 * @ Author EmptyIrony
 * @ Date 2022/6/5
 * @ Time 0:29
 */
class EnchantChoseButton(
    private val item: ItemStack,
    private val index: Int,
    private val slot: Int,
    private val enchantment: AbstractEnchantment,
    private val enchantLevel: Int
) : Button() {
    override fun getButtonItem(player: Player): ItemStack {
        return ItemBuilder(item.type)
            .name("&e升级第${slot + 1}号附魔")
            .lore(
                "",
                "&7附魔属性: &9${enchantment.enchantName}",
                "&7等级: &e${enchantLevel}",
                "",
                "&7消耗: &a一个遵纪守法的宝石",
                "",
                if (Utils.canUseGen(item,false)) {
                    if (enchantLevel >= 3) "&c已达最大等级" else
                        if (enchantment.rarity.parentType == EnchantmentRarity.RarityType.RARE) "&c稀有附魔" else
                            "&a点击使用!"
                } else "&c无法作用于此附魔上"
            )
            .build()
    }

    override fun clicked(
        player: Player,
        slot: Int,
        clickType: ClickType,
        hotbarButton: Int,
        currentItem: ItemStack?
    ) {
        if (!Utils.canUseGen(item,false)) {
            player.closeInventory()
            return
        }

        val mythicItem = item.toMythicItem() ?: return

        val indexedItem = player.inventory.getItem(index)
        val indexedMythicItem = indexedItem.toMythicItem()
        if (indexedMythicItem != null && isSameEnchant(mythicItem, indexedMythicItem)) {
            val enchantLevel = indexedMythicItem.enchantments[enchantment] ?: return
            if (enchantLevel >= 3) {
                return
            }

            if (enchantment.rarity.parentType == EnchantmentRarity.RarityType.RARE) {
                return
            }

            var maxedLevel = 0
            indexedMythicItem.enchantments.forEach { (_, level) ->
                if (level >= 3) {
                    maxedLevel++
                }
            }

            //不能从332 => 333
            if (maxedLevel >= 2 && enchantLevel >= 2) {
                player.sendMessage(CC.translate("&c不合法"))
                player.closeInventory()
                return
            }

            indexedMythicItem.enchantments[enchantment] = enchantLevel + 1

            indexedMythicItem.enchantmentRecords += EnchantmentRecord(
                player.name,
                "Gem",
                System.currentTimeMillis()
            )

            indexedMythicItem.isBoostedByGem = true

            player.inventory.setItem(index, indexedMythicItem.toItemStack())
        } else {
            return
        }

        player.closeInventory()
        player.playSound(player.location, Sound.ANVIL_USE, 1.5f, 1.5f)
        player.sendMessage(CC.translate("&a使用成功!"))
        player.inventory.forEachIndexed { index, itemStack ->
            val internalName = ItemUtil.getInternalName(itemStack)
            if ("totally_legit_gem" == internalName) {
                if (itemStack.amount == 1) {
                    player.inventory.setItem(index, null)
                } else {
                    itemStack.amount--
                    player.inventory.setItem(index, itemStack)
                }
                return
            }
        }
    }

    private fun isSameEnchant(itemA: cn.charlotte.pit.item.IMythicItem, itemB: cn.charlotte.pit.item.IMythicItem): Boolean {
        loop@ for (enchantment in itemA.enchantments) {
            for (enchantmentB in itemB.enchantments) {
                if (enchantment.key.nbtName == enchantmentB.key.nbtName && enchantment.value == enchantmentB.value) {
                    continue@loop
                }
            }

            return false
        }

        return true
    }
}