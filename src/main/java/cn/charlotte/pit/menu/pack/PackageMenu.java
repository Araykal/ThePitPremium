package cn.charlotte.pit.menu.pack;

import cn.charlotte.pit.menu.pack.button.ItemButton;
import cn.charlotte.pit.util.menu.Button;
import cn.charlotte.pit.util.menu.Menu;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: EmptyIrony
 * @Date: 2021/1/30 17:19
 */
public class PackageMenu extends Menu {
    private static final Map<Integer, ItemStack> items = new ConcurrentHashMap<>();

    public static Map<Integer, ItemStack> getItems() {
        return PackageMenu.items;
    }

    @Override
    public String getTitle(Player player) {
        return "空投";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> map = new HashMap<>();
        for (Map.Entry<Integer, ItemStack> entry : items.entrySet()) {
            map.put(entry.getKey(), new ItemButton(entry.getValue()));
        }

        return map;
    }

    @Override
    public int getSize() {
        return 3 * 9;
    }

}
