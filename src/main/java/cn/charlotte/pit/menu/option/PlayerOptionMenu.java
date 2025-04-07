package cn.charlotte.pit.menu.option;

import cn.charlotte.pit.menu.option.button.*;
import cn.charlotte.pit.util.PlayerUtil;
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
 * @Created_In: 2021/1/13 22:25
 */
public class PlayerOptionMenu extends Menu {

    private boolean adminVersion = false;

    @Override
    public String getTitle(Player player) {
        return "游戏选项";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> button = new HashMap<>();
        if (PlayerUtil.isStaff(player)) {
            button.put(8, new Button() {
                @Override
                public ItemStack getButtonItem(Player player) {
                    return adminVersion ? new ItemBuilder(Material.WATCH)
                            .name("&a返回普通版")
                            .build() : new ItemBuilder(Material.COMPASS)
                            .name("&c切换管理员版")
                            .build();

                }

                @Override
                public void clicked(Player player, int slot, ClickType clickType, int hotbarButton, ItemStack currentItem) {
                    adminVersion = !adminVersion;
                }

                @Override
                public boolean shouldUpdate(Player player, int slot, ClickType clickType) {
                    return true;
                }
            });
        }
        if (!adminVersion) {
            button.put(10, new BountyNotifyButton());
            button.put(11, new StreakNotifyOption());
            button.put(12, new PrestigeNotifyButton());
            button.put(13, new EventNotifyButton());
            button.put(14, new CombatNotifyButton());
            button.put(15, new ChatNotifyOption());
            button.put(16, new OtherNotifyButton());

            button.put(19, new ProfileVisibilityButton());
            button.put(20, new InventoryVisibilityButton());
            button.put(21, new EnderChestVisibilityButton());

            button.put(25, new TradeNotifyButton());

            button.put(28, new BarPriorityOptionButton());
            button.put(29, new BountyHiddenWhenNearOptionButton());
            button.put(34, new OutfitOption());
        } else {
            button.put(11, new DebugDamageMessageOptionButton());
        }
        return button;
    }

    @Override
    public int getSize() {
        return 5 * 9;
    }

    @Override
    public boolean isAutoUpdate() {
        return true;
    }

}
