package cn.charlotte.pit.menu.admin.item;

import cn.charlotte.pit.item.type.GrimReaper;
import cn.charlotte.pit.item.type.LuckyGem;
import cn.charlotte.pit.menu.admin.item.button.PitItemButton;
import cn.charlotte.pit.menu.shop.button.type.BowBundleShopButton;
import cn.charlotte.pit.menu.shop.button.type.PantsBundleShopButton;
import cn.charlotte.pit.menu.shop.button.type.SwordBundleShopButton;
import cn.charlotte.pit.util.menu.Button;
import cn.charlotte.pit.util.menu.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Araykal
 * @since 2025/1/18
 */
public class AdminRuneMenu extends Menu {
    @Override
    public String getTitle(Player player) {
        return "Pit特殊物品";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttonMap = new HashMap<>();
        buttonMap.put(1, new PitItemButton(new GrimReaper().toItemStack()));
        buttonMap.put(2, new PantsBundleShopButton(true));
        buttonMap.put(3, new BowBundleShopButton(true));
        buttonMap.put(4, new SwordBundleShopButton(true));
        buttonMap.put(5, new PitItemButton(new LuckyGem().toItemStack()));
        return buttonMap;
    }

    @Override
    public void onClickEvent(InventoryClickEvent event) {
        if (event.getClickedInventory() instanceof PlayerInventory) {
            final PlayerInventory inv = (PlayerInventory) event.getClickedInventory();
            inv.setItem(event.getSlot(), new ItemStack(Material.AIR));
        }
    }
}
