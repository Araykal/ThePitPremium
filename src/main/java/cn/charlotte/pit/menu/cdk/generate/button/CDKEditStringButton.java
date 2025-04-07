package cn.charlotte.pit.menu.cdk.generate.button;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.util.callback.Callback;
import cn.charlotte.pit.util.menu.Button;
import cn.charlotte.pit.util.menu.Menu;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * @Author: EmptyIrony
 * @Date: 2021/3/26 17:33
 */
public class CDKEditStringButton extends Button {
    private final ItemStack material;
    private final Menu menu;
    private final Callback<String> doubleCallback;

    public CDKEditStringButton(ItemStack material, Menu menu, Callback<String> doubleCallback) {
        this.material = material;
        this.menu = menu;
        this.doubleCallback = doubleCallback;
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        return material;
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton, ItemStack currentItem) {
        ThePit.getInstance()
                .getSignGui()
                .open(player, new String[]{"", "~~~~~~~~~~~~~", "请在此输入", "内容"}, (player1, lines) -> {
                    player.closeInventory();
                    menu.setClosedByMenu(true);
                    Bukkit.getScheduler()
                            .runTaskLater(ThePit.getInstance(), () -> {
                                menu.openMenu(player);
                                doubleCallback.call(lines[0]);
                            }, 1L);
                });
    }
}
