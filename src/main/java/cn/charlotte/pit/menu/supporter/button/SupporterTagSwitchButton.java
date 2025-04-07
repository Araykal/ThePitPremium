package cn.charlotte.pit.menu.supporter.button;

import cn.charlotte.pit.util.menu.Button;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * @Creator Misoryan
 * @Date 2021/5/4 10:17
 */
public class SupporterTagSwitchButton extends Button {

    @Override
    public ItemStack getButtonItem(Player player) {
        return null;
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton, ItemStack currentItem) {

    }
}
