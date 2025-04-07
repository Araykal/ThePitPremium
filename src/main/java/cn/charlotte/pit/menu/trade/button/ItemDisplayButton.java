package cn.charlotte.pit.menu.trade.button;

import cn.charlotte.pit.menu.trade.TradeManager;
import cn.charlotte.pit.util.cooldown.Cooldown;
import cn.charlotte.pit.util.menu.buttons.DisplayButton;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Author: EmptyIrony
 * @Date: 2021/1/28 23:47
 */
public class ItemDisplayButton extends DisplayButton {
    private final TradeManager tradeManager;
    private final boolean self;

    public ItemDisplayButton(ItemStack itemStack, TradeManager tradeManager, boolean self) {
        super(itemStack, true);
        this.self = self;
        this.tradeManager = tradeManager;
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton, ItemStack currentItem) {
        super.clicked(player, slot, clickType, hotbarButton, currentItem);
        if (!self) {
            return;
        }

        List<ItemStack> items;
        if (tradeManager.getPlayerA().equals(player)) {
            items = tradeManager.getAItems();
            tradeManager.openMenu(tradeManager.getPlayerB());
        } else {
            items = tradeManager.getBItems();
            tradeManager.openMenu(tradeManager.getPlayerA());

        }
        int num = -1;
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).equals(this.getItemStack())) {
                num = i;
                break;
            }
        }
        if (num == -1) {
            return;
        }

        if (tradeManager.getPlayerA().equals(player)) {
            tradeManager.getPlayerA().getInventory().addItem(this.getItemStack());
        } else {
            tradeManager.getPlayerB().getInventory().addItem(this.getItemStack());
        }

        items.remove(num);

        tradeManager.getPlayerA().playSound(tradeManager.getPlayerA().getLocation(), Sound.VILLAGER_HAGGLE, 1, 1);
        tradeManager.getPlayerB().playSound(tradeManager.getPlayerB().getLocation(), Sound.VILLAGER_HAGGLE, 1, 1);
        tradeManager.setBConfirm(false);
        tradeManager.setAConfirm(false);
        tradeManager.setAPutCooldown(new Cooldown(5, TimeUnit.SECONDS));
        tradeManager.setBPutCooldown(new Cooldown(5, TimeUnit.SECONDS));
    }

    @Override
    public boolean shouldUpdate(Player player, int slot, ClickType clickType) {
        return true;
    }
}
