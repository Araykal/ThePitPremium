package cn.charlotte.pit.enchantment.menu.button;

import cn.charlotte.pit.enchantment.menu.MythicWellMenu;
import cn.charlotte.pit.util.menu.Button;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * @Author: EmptyIrony
 * @Date: 2021/1/17 14:46
 */
@Getter
@Setter
@AllArgsConstructor
public class ItemSlotButton extends Button {
    private MythicWellMenu enchantMenu;
    private ItemStack item;

    @Override
    public ItemStack getButtonItem(Player player) {
        return item;
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton, ItemStack currentItem) {
        if (slot == 13) {
            if (currentItem != null) {
                this.item = currentItem;
            }
        }
    }

    @Override
    public boolean shouldCancel(Player player, int slot, ClickType clickType) {
        return clickType == ClickType.SHIFT_LEFT || clickType == ClickType.SHIFT_RIGHT;
    }
}
