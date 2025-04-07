package cn.charlotte.pit.menu.trade.button;

import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.menu.trade.TradeManager;
import cn.charlotte.pit.util.cooldown.Cooldown;
import cn.charlotte.pit.util.menu.buttons.DisplayButton;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.TimeUnit;

/**
 * @Author: EmptyIrony
 * @Date: 2021/1/28 12:31
 */
public class CoinDisplayButton extends DisplayButton {
    private final TradeManager tradeManager;
    private final boolean self;

    public CoinDisplayButton(ItemStack itemStack, boolean cancel, TradeManager tradeManager, boolean self) {
        super(itemStack, cancel);
        this.tradeManager = tradeManager;
        this.self = self;
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton, ItemStack currentItem) {
        super.clicked(player, slot, clickType, hotbarButton, currentItem);
        if (!self) {
            return;
        }

        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
        double coins;

        if (tradeManager.getPlayerA().equals(player)) {
            coins = tradeManager.getACoins();
            tradeManager.setACoins(0);
            tradeManager.openMenu(tradeManager.getPlayerB());
        } else {
            coins = tradeManager.getBCoins();
            tradeManager.setBCoins(0);
            tradeManager.openMenu(tradeManager.getPlayerA());
        }

        profile.setCoins(profile.getCoins() + coins);

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
