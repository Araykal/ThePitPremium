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

/*
    override fun enableList(): Set<String> {
        return setOf(
            //cloverpixel
            "3ae69d93c-6820-6261-9324-c1cd00f6d0a",
            //Remiaft
            "c95d33ac4-8667-2259-1380-ab51bf2e84b",

            //normal
            "a2311a94-ca4c-4423-b387-2535d37378c7",
            "a41a5f6e-fcdc-443c-aaa2-8eb6567863d8",
            "6b1a01fc-909f-ac6b-acb4-036afad128ef",
            "59a47104-69b5-470e-a046-6d69cbc3374d",
            "4e1a9b47-74d9-4be4-ad5c-8674cd9df763",
            "1bc9f706-719e-6a07-a986-522791d7188b",
            "683b94be-538b-731f-0035-097a158b1ec5",
            "0dca19ee-40af-4444-a0a8-0624c429a48e",
            "5330151b-e2ff-42dc-04c8-4eb3e20c4c95",
            "074e006a-7d34-4605-968d-263606b85dd0",
            "35feae69-4d8a-43cf-9478-7436cb069023",
            "0eba2181-01d6-48b9-9a27-fa8a1934e975",
            "876736d9-9026-46ee-a5cf-97440e5c5e48",
            "4847a648-bd9f-463e-ab18-3006b0fabd3b",
            "5476536b-866b-261f-f015-07af3d658646",
            "b2379c09-a604-4618-bf91-309800e2b224",
            "679e6c94-905b-4517-a957-c82ba714c5ef",
            "62c4db0d-3741-4999-b5de-6e97beb6106d",
            "e4ba766a-9b84-4195-b61c-df1c4777cbba"
        )
    }
*/

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