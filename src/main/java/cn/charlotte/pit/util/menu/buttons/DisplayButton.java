package cn.charlotte.pit.util.menu.buttons;

import cn.charlotte.pit.util.menu.Button;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class DisplayButton extends Button {

    private ItemStack itemStack;
    private boolean cancel;

    public DisplayButton(ItemStack itemStack, boolean cancel) {
        this.itemStack = itemStack;
        this.cancel = cancel;
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        if (this.itemStack == null) {
            return new ItemStack(Material.AIR);
        } else {
            return this.itemStack;
        }
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton, ItemStack currentItem) {

    }

    @Override
    public boolean shouldCancel(Player player, int slot, ClickType clickType) {
        return this.cancel;
    }

    public ItemStack getItemStack() {
        return this.itemStack;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public boolean isCancel() {
        return this.cancel;
    }

    public void setCancel(boolean cancel) {
        this.cancel = cancel;
    }
}
