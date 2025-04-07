package cn.charlotte.pit.menu.viewer.button;

import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.data.sub.PlayerInv;
import cn.charlotte.pit.util.item.ItemBuilder;
import cn.charlotte.pit.util.menu.Button;
import cn.charlotte.pit.util.menu.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Misoryan
 * @Created_In: 2021/1/13 14:53
 */
public class InventoryViewerMenu extends Menu {
    private final PlayerProfile profile;

    public InventoryViewerMenu(PlayerProfile profile) {
        this.profile = profile;
    }

    @Override
    public String getTitle(Player player) {
        return "背包";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        PlayerInv inventory = profile.getInventory();
        Map<Integer, Button> button = new HashMap<>();
        for (int i = 0; i < 36; i++) {
            int finalI = i;
            button.put(i, new Button() {
                @Override
                public ItemStack getButtonItem(Player player) {
                    return inventory.getContents()[finalI] == null ? new ItemBuilder(Material.AIR).build() : inventory.getContents()[finalI];
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
        return 4 * 9;
    }
}
