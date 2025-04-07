package cn.charlotte.pit.menu.admin.item.button;

import cn.charlotte.pit.util.menu.Button;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * @Author: Misoryan
 * @Created_In: 2021/3/28 18:56
 */
public class PitItemButton extends Button {
    private final ItemStack itemStack;

    public PitItemButton(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        return itemStack;
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton, ItemStack currentItem) {
        player.getInventory().addItem(getButtonItem(player));
    }
}
