package cn.charlotte.pit.menu.trade.button;

import cn.charlotte.pit.menu.trade.TradeManager;
import cn.charlotte.pit.util.cooldown.Cooldown;
import cn.charlotte.pit.util.item.ItemBuilder;
import cn.charlotte.pit.util.menu.Button;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * @Author: EmptyIrony
 * @Date: 2021/1/19 21:10
 */
public class SelfConfirmButton extends Button {
    private final TradeManager tradeManager;

    public SelfConfirmButton(TradeManager tradeManager) {
        this.tradeManager = tradeManager;
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        Cooldown cooldown;
        if (tradeManager.getPlayerA().equals(player)) {
            cooldown = tradeManager.getAPutCooldown();
        } else {
            cooldown = tradeManager.getBPutCooldown();
        }

        if ((tradeManager.getPlayerA().equals(player) && tradeManager.isAConfirm()) || (!tradeManager.getPlayerA().equals(player) && tradeManager.isBConfirm())) {
            return new ItemBuilder(Material.STAINED_CLAY)
                    .durability(5)
                    .name("&a已同意交易!")
                    .lore("&7你已同意了交易.", "&7现在请等待对方同意交易.", "", "&e点击以取消同意交易,再思考一会...")
                    .build();
        }
        if (cooldown.hasExpired() && cooldown.getRemaining() / 1000L < 1) {
            if (tradeManager.getACoins() == 0 && tradeManager.getAItems().size() == 0 && tradeManager.getBCoins() == 0 && tradeManager.getBItems().size() == 0) {
                //如果交易界面上什么也没有
                return new ItemBuilder(Material.STAINED_CLAY)
                        .durability(5)
                        .name("&e交易!")
                        .lore("&7交易现在开始!,", "&7放上你想要交换的金钱,或是物品.")
                        .build();
            } else {
                //如果对方没有放上任何硬币或物品
                if ((tradeManager.getPlayerA().equals(player) && tradeManager.getBItems().size() == 0 && tradeManager.getBCoins() == 0) || (tradeManager.getPlayerB().equals(player) && tradeManager.getAItems().size() == 0 && tradeManager.getACoins() == 0)) {
                    return new ItemBuilder(Material.STAINED_CLAY)
                            .durability(14)
                            .name("&e警告!")
                            .lore("&7你正在对方没有提供任何物品", "&7的情况下进行交易.", " ", "&c这真的是一份礼物?", " ", "&e点击同意此次交易!")
                            .build();
                }
                //如果玩家没有放上任何物品或物品
                if ((tradeManager.getPlayerA().equals(player) && tradeManager.getAItems().size() == 0 && tradeManager.getACoins() == 0) || (tradeManager.getPlayerB().equals(player) && tradeManager.getBItems().size() == 0 && tradeManager.getBCoins() == 0)) {
                    return new ItemBuilder(Material.STAINED_CLAY)
                            .durability(3)
                            .name("&e是的,这是一份礼物!")
                            .lore("&7你正在没有提供任何物品,", "&7的情况下进行交易.", " ", "&3你赚了!他亏了!", " ", "&e点击同意此次交易!")
                            .build();
                }
            }
            return new ItemBuilder(Material.STAINED_CLAY)
                    .durability(5)
                    .name("&e交易!")
                    .lore("&7交易成交后便无法修改,", "&7且无法被撤销.", " ", "&a请确保在确认交易前", "&a仔细核对了所有交易物品.", " ", "&e点击同意此次交易!")
                    .build();
        } else {
            return new ItemBuilder(Material.STAINED_CLAY)
                    .durability(4)
                    .amount((int) (cooldown.getRemaining() / 1000L))
                    .name("&e请三思! &7(&e" + (cooldown.getRemaining() / 1000L) + "&7)")
                    .lore("&7交易内容发生了变动,", "&7在同意交易前请检查交易内容.")
                    .build();
        }
    }

    @Override
    public boolean shouldUpdate(Player player, int slot, ClickType clickType) {
        return true;
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton, ItemStack currentItem) {
        if (tradeManager.getACoins() == 0 && tradeManager.getAItems().size() == 0 && tradeManager.getBCoins() == 0 && tradeManager.getBItems().size() == 0) {
            //还没有任何交易内容时 不允许点击
            return;
        }
        Cooldown cooldown;
        boolean accept;
        if (tradeManager.getPlayerA().equals(player)) {
            cooldown = tradeManager.getAPutCooldown();
        } else {
            cooldown = tradeManager.getBPutCooldown();
        }

        if (cooldown.hasExpired()) {
            if (tradeManager.getPlayerA().equals(player)) {
                tradeManager.setAConfirm(!tradeManager.isAConfirm());
                tradeManager.openMenu(tradeManager.getPlayerB());
                accept = tradeManager.isAConfirm();
            } else {
                tradeManager.setBConfirm(!tradeManager.isBConfirm());
                tradeManager.openMenu(tradeManager.getPlayerA());
                accept = tradeManager.isBConfirm();
            }
            if (accept) {
                tradeManager.getPlayerB().playSound(tradeManager.getPlayerB().getLocation(), Sound.VILLAGER_YES, 1, 1);
                tradeManager.getPlayerA().playSound(tradeManager.getPlayerA().getLocation(), Sound.VILLAGER_YES, 1, 1);
            } else {
                tradeManager.getPlayerA().playSound(tradeManager.getPlayerA().getLocation(), Sound.VILLAGER_HAGGLE, 1, 1);
                tradeManager.getPlayerB().playSound(tradeManager.getPlayerB().getLocation(), Sound.VILLAGER_HAGGLE, 1, 1);
            }
        }
    }
}
