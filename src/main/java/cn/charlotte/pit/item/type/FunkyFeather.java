package cn.charlotte.pit.item.type;

import cn.charlotte.pit.util.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * @Creator Misoryan
 * @Date 2021/5/29 17:37
 */
public class FunkyFeather {

    public static ItemStack toItemStack() {
        List<String> lore = new ArrayList<>();
        lore.add("&e特殊物品");
        lore.add("&7放于物品栏时,可以保护");
        lore.add("&7背包内的神话物品不会在死亡后扣除生命.");
        lore.add("&7&o此物品会在死亡后消耗");
        return new ItemBuilder(Material.FEATHER).name("&3时髦的羽毛").lore(lore).internalName("funky_feather").canTrade(true).canSaveToEnderChest(true).build();
    }
}
