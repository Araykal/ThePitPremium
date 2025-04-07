package cn.charlotte.pit.item.type;

import cn.charlotte.pit.util.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * @Author: Misoryan
 * @Created_In: 2021/3/28 15:44
 */

public class FourInARowGadget {

    public static ItemStack toItemStack() {
        return new ItemBuilder(Material.COMPASS)
                .name("&6四子棋小道具")
                .lore(
                        "&7死亡后保留",
                        "",
                        "&7手持右键一名玩家以发起一局",
                        "&7四子棋游戏!",
                        "",
                        "&6阵营活动奖励"
                )
                .internalName("4_in_a_row_gadget")
                .canSaveToEnderChest(true)
                .canTrade(true)
                .shiny()
                .build();
    }
}
