package cn.charlotte.pit.item.type

import cn.charlotte.pit.item.AbstractPitItem
import cn.charlotte.pit.menu.gem.global.GlobalAttentionGemMenu
import cn.charlotte.pit.util.item.ItemBuilder
import cn.charlotte.pit.util.item.ItemUtil
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.event.Event
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack

/**
 * @author Araykal
 * @since 2025/1/17
 */
class GlobalAttentionGem  : AbstractPitItem(), Listener {
    @EventHandler
    fun interact(event: PlayerInteractEvent) {
        if ("global_attention_gem" == ItemUtil.getInternalName(event.item)) {
            event.isCancelled = true
            event.setUseInteractedBlock(Event.Result.DENY)
            event.setUseItemInHand(Event.Result.DENY)
            GlobalAttentionGemMenu().openMenu(event.player)
            return
        }
    }

    override fun getInternalName(): String {
        return "global_attention_gem"
    }

    /**
     * 举世瞩目的宝石
     * 物品类型为附魔钻石图标，并使神话物品生命后添加 ♦ 的字符（天蓝色）
     * 仅可为稀有附魔增加一级附魔（不可超过III级），使用后消失
     * 与遵纪守法的宝石向相冲突，一件神话物品最多只能镶嵌1个宝石
     * @return
     */
    override fun getItemDisplayName(): String {
        return "&b举世瞩目的宝石"
    }

    override fun getItemDisplayMaterial(): Material {
        return Material.DIAMOND
    }

    override fun toItemStack(): ItemStack {
        return ItemBuilder(itemDisplayMaterial)
            .name(itemDisplayName)
            .lore(
                "&7死亡时保留",
                "",
                "&7增加附魔物品的一级附魔, 并使神话物品生命后添加 &b♦ &7的字符",
                "&7(普通及特殊附魔除外, 不可超过上限)",
                "&8一件物品只能使用一次",
                "",
                "&e右键使用"
            )
            .internalName(internalName)
            .shiny()
            .removeOnJoin(false)
            .deathDrop(false)
            .canDrop(false)
            .canTrade(true)
            .enchant(Enchantment.DURABILITY,1)
            .canSaveToEnderChest(true)
            .build()
    }

    override fun loadFromItemStack(item: ItemStack) {

    }
}