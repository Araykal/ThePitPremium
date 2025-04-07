package cn.charlotte.pit.item.type;

import cn.charlotte.pit.util.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * @Creator Misoryan
 * @Date 2021/5/29 17:36
 */
public class PitCactus {

    public static ItemStack toItemStack() {
        List<String> lore = new ArrayList<>();
        lore.add("&e特殊物品");
        lore.add("&7手持并右键可以从九件未附魔的");
        lore.add("&7随机 &a神&c话&e之&6甲 &7选择其一.");
        lore.add(" ");
        lore.add("&7(部分特殊颜色不可选择)");
        return new ItemBuilder(Material.CACTUS).name("&a哲学仙人掌").lore(lore).internalName("cactus").canTrade(true).canSaveToEnderChest(true).build();
    }
}
