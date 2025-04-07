package cn.charlotte.pit.menu.perk.normal.buy.button;

import cn.charlotte.pit.menu.perk.normal.choose.PerkChooseMenu;
import cn.charlotte.pit.util.item.ItemBuilder;
import cn.charlotte.pit.util.menu.Button;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * @Author: Misoryan
 * @Created_In: 2021/1/7 17:47
 */
public class BackToPerkChooseButton extends Button {
    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(Material.ARROW)
                .name("&a返回")
                .lore("&7点击回到升级主界面.")
                .build();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton, ItemStack currentItem) {
        new PerkChooseMenu().openMenu(player);
    }


}
