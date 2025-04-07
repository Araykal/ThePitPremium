package cn.charlotte.pit.enchantment.menu.button

import cn.charlotte.pit.data.PlayerProfile
import cn.charlotte.pit.item.MythicColor
import cn.charlotte.pit.util.inventory.InventoryUtil
import cn.charlotte.pit.util.item.ItemBuilder
import cn.charlotte.pit.util.menu.Button
import cn.charlotte.pit.util.menu.Menu
import lombok.RequiredArgsConstructor
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.inventory.ItemStack


@RequiredArgsConstructor
class EnchantBookButton(
    private val menu: Menu, private val color: MythicColor
) : Button() {

    override fun getButtonItem(player: Player): ItemStack {
        val profile = PlayerProfile.getPlayerProfileByUuid(player.uniqueId)
        val bookItem = profile.enchantingBook ?: return noneItem
        val item = InventoryUtil.deserializeItemStack(bookItem)
        return if (item == null || item.type == Material.AIR) {
            noneItem
        } else item
    }

    private val noneItem: ItemStack
        get() = ItemBuilder(Material.STAINED_GLASS_PANE)
            .durability(15)
            .name("&d放入附魔卷轴")
            .lore(
                "&7将&6神话物品&7和&d附魔卷轴&7放入神话之井",
                "&7将会为该&6神话物品&7带来一个随机的三级 &d&l稀有! &7附魔",
            )
            .build()

    override fun clicked(player: Player, slot: Int, clickType: ClickType, hotbarButton: Int, currentItem: ItemStack) {
        val profile = PlayerProfile.getPlayerProfileByUuid(player.uniqueId)
        val bookItem = profile.enchantingBook ?: return
        val item = InventoryUtil.deserializeItemStack(bookItem)
        if (item == null || item.type == Material.AIR) {
            return
        }
        if (InventoryUtil.isInvFull(player)) {
            player.sendMessage("&c你的背包已满.")
            return
        }
        profile.enchantingBook = null
        player.inventory.addItem(item)
        player.playSound(player.location, Sound.CHICKEN_EGG_POP, 1f, 0.65f)
        menu!!.openMenu(player)
    }
}
