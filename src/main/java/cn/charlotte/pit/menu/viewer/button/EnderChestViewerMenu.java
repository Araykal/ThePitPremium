package cn.charlotte.pit.menu.viewer.button;

import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.util.item.ItemBuilder;
import cn.charlotte.pit.util.menu.Button;
import cn.charlotte.pit.util.menu.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Misoryan
 * @Created_In: 2021/1/13 14:57
 */
public class EnderChestViewerMenu extends Menu {
    private final PlayerProfile profile;

    public EnderChestViewerMenu(PlayerProfile profile) {
        this.profile = profile;
    }

    @Override
    public String getTitle(Player player) {
        return "末影箱";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Inventory enderChest = profile.getEnderChest().getInventory();
        Map<Integer, Button> button = new HashMap<>();
        for (int i = 0; i < profile.getEnderChestRow() * 9; i++) {
            int finalI = i;
            button.put(i, new Button() {
                @Override
                public ItemStack getButtonItem(Player player) {
                    return enderChest.getContents()[finalI] == null ? new ItemBuilder(Material.AIR).build() : enderChest.getContents()[finalI];
                }

                @Override
                public void clicked(Player player, int slot, ClickType clickType, int hotbarButton, ItemStack currentItem) {

                }
            });
        }
        return button;
    }

    @Override
    public int getSize() {
        return profile.getEnderChestRow() * 9;
    }
}
