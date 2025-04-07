package cn.charlotte.pit.addon.impl

import cn.charlotte.pit.ThePit
import cn.charlotte.pit.addon.Addon
import cn.charlotte.pit.data.PlayerProfile
import cn.charlotte.pit.item.AbstractPitItem
import cn.charlotte.pit.util.command.Command
import cn.charlotte.pit.util.command.CommandHandler
import cn.charlotte.pit.util.command.param.Parameter
import cn.charlotte.pit.util.findItem
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender


class GiveItemCommand : Addon {
    override fun name(): String {
        return "give_item_command"
    }

 /*   override fun enableList(): Set<String> {
        return setOf(
            "59a47104-69b5-470e-a046-6d69cbc3374d")
    }*/

    override fun enable() {
        CommandHandler.registerClass(GiveItemCommandExecutor::class.java)
    }


    class GiveItemCommandExecutor {
        @Command(names = ["giveItems"], permissionNode = "thepit.admin")
        fun execute(
            sender: CommandSender,
            @Parameter(name = "playerName") playerName: String,
            @Parameter(name = "itemName") itemString: String,
            @Parameter(name = "amount") amountString: String
        ) {
            val player = Bukkit.getPlayerExact(playerName) ?: return
            val amount = amountString.toIntOrNull() ?: return

            val findItem = itemString.findItem(amount)
            if (findItem != null) {
                player.inventory.addItem(findItem)
                return
            } else {
                if (itemString == "exp") {
                    PlayerProfile.getPlayerProfileByUuid(player.uniqueId)?.apply {
                        experience += amount
                    }
                } else if (itemString == "coin") {
                    PlayerProfile.getPlayerProfileByUuid(player.uniqueId)?.apply {
                        grindedCoins += amount
                        coins += amount
                    }
                }
            }


            try {
                val itemClass: Class<out AbstractPitItem> =
                    ThePit.getInstance().itemFactor.itemMap[itemString] ?: return
                val item = itemClass.newInstance()
                val itemStack = item.toItemStack()
                itemStack.amount = amount
                player.inventory.addItem(itemStack)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}