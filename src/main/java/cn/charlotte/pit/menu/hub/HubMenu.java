package cn.charlotte.pit.menu.hub;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.config.NewConfiguration;
import cn.charlotte.pit.util.item.ItemBuilder;
import cn.charlotte.pit.util.menu.Button;
import cn.charlotte.pit.util.menu.Menu;
import cn.charlotte.pit.util.menu.menus.ConfirmMenu;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Misoryan
 * @since 2021/2/4 10:03
 */
public class HubMenu extends Menu {

    @Override
    public String getTitle(Player player) {
        return "返回大厅";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> button = new HashMap<>();
        button.put(13, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return new ItemBuilder(Material.BIRCH_DOOR_ITEM).name("&a返回大厅").lore("&7左键点累了吗?", " ", "&e点击返回大厅!").build();
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton, ItemStack currentItem) {
                new ConfirmMenu("确认返回大厅?", confirm -> {
                    if (confirm) {
                        Bukkit.getScheduler().runTaskLater(ThePit.getInstance(), () -> {
                            if (NewConfiguration.INSTANCE.getLobbyCommand().equalsIgnoreCase("kick")) {
                                player.kickPlayer("§c遣送回国!");
                                return;
                            }
                            player.chat("/" + NewConfiguration.INSTANCE.getLobbyCommand());
                        }, 20L);
                    }
                }, true, 3).openMenu(player);
            }
        });
        return button;
    }

    @Override
    public int getSize() {
        return 3 * 9;
    }
}
