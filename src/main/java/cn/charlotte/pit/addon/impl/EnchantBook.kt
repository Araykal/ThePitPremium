package cn.charlotte.pit.addon.impl

import cn.charlotte.pit.addon.Addon
import cn.charlotte.pit.util.command.Command
import cn.charlotte.pit.util.command.CommandHandler
import cn.charlotte.pit.util.command.param.Parameter
import cn.charlotte.pit.util.item.ItemBuilder
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.command.CommandSender
import sinc.Native
import java.util.*
@Native

object EnchantBook : Addon {
    var enchantBook = false
    override fun name(): String {
       return "enchant_book";
    }

    override fun enable() {
        enchantBook = true
        CommandHandler.registerClass(MythicBookCommand::class.java)
    }

    class MythicBookCommand {
        @Command(names = ["giveBook"], permissionNode = "pit.admin")
        fun give(sender: CommandSender, @Parameter(name = "playerName") name: String) {
            val player = Bukkit.getPlayerExact(name) ?: return
            player.inventory.addItem(
                ItemBuilder(Material.PAPER)
                    .name("&d附魔卷轴")
                    .deathDrop(false)
                    .canDrop(true)
                    .canSaveToEnderChest(true)
                    .internalName("mythic_reel")
                    .uuid(UUID.randomUUID())
                    .lore(
                        "",
                        "&7将&6神话物品&7和&d附魔卷轴&7放入神话之井",
                        "&7将会为该&6神话物品&7带来一个随机的三级 &d&l稀有! &7附魔",
                        "",
                        "&7在神话之井使用"
                    )
                    .shiny()
                    .dontStack()
                    .build()
            )
        }
    }


}