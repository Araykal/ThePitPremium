package cn.charlotte.pit.menu.admin.backpack.button;

import cn.charlotte.pit.util.menu.buttons.DisplayButton;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * @Author: EmptyIrony
 * @Date: 2021/2/4 20:49
 */
public class ItemShowButton extends DisplayButton {

    public ItemShowButton(ItemStack itemStack, boolean cancel) {
        super(itemStack, cancel);
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton, ItemStack currentItem) {
        if (this.getItemStack() != null) {
            player.getInventory().addItem(this.getItemStack());
        }
    }
}
