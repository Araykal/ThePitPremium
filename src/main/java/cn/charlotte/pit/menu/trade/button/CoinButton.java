package cn.charlotte.pit.menu.trade.button;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.menu.trade.TradeManager;
import cn.charlotte.pit.util.chat.CC;
import cn.charlotte.pit.util.cooldown.Cooldown;
import cn.charlotte.pit.util.item.ItemBuilder;
import cn.charlotte.pit.util.menu.Button;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.TimeUnit;

/**
 * @Author: EmptyIrony
 * @Date: 2021/1/19 21:10
 */
public class CoinButton extends Button {
    private final TradeManager tradeManager;

    public CoinButton(TradeManager tradeManager) {
        this.tradeManager = tradeManager;
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
        return new ItemBuilder(Material.GOLD_INGOT)
                .name("&6硬币交易")
                .lore(" ", "&7每日交易限制: " + (profile.getTradeLimit().getAmount() >= 50000 ? "&c" : "&a") + profile.getTradeLimit().getAmount() + "g&7/&650000g &7(" + profile.getTradeLimit().getTimes() + "/25次)", " ", "&e点击设置转账金额!")
                .build();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton, ItemStack currentItem) {
        if (tradeManager.getPlayerA().equals(player)) {
            tradeManager.setAEditingCoins(true);
        } else {
            tradeManager.setBEditingCoins(true);
        }

        ThePit.getInstance()
                .getSignGui()
                .open(player, new String[]{"", "~~~~~~~~~~~~~~", "在上方", "输入交易金额"}, (player1, lines) -> {

                    player1.closeInventory();
                    Bukkit.getScheduler()
                            .runTaskLater(
                                    ThePit.getInstance(),
                                    () -> tradeManager.openMenu(player1),
                                    2L
                            );

                    if (tradeManager.getPlayerA().equals(player)) {
                        tradeManager.setAEditingCoins(false);
                    } else {
                        tradeManager.setBEditingCoins(false);
                    }


                    if (lines.length == 0) {
                        return;
                    }
                    long coins;
                    try {
                        coins = Long.parseLong(lines[0].replaceAll("\"", ""));
                    } catch (Exception ignore) {
                        player.sendMessage(CC.translate("&c请输入一个整数金额!"));
                        return;
                    }

                    if (coins < 0) {
                        player.sendMessage(CC.translate("&c请输入一个正数金额!"));
                        return;
                    }
                    if (TradeManager.trades.get(player.getUniqueId()) == null) {
                        player.sendMessage(CC.translate("&c当前交易已被取消/结算!"));
                        return;
                    }

                    PlayerProfile profileA = PlayerProfile.getPlayerProfileByUuid(tradeManager.getPlayerA().getUniqueId());
                    PlayerProfile profileB = PlayerProfile.getPlayerProfileByUuid(tradeManager.getPlayerB().getUniqueId());
                    if (tradeManager.getPlayerA().equals(player)) {
                        if (tradeManager.getAItems().size() >= 8) {
                            player.sendMessage(CC.translate("&c你无法再放入更多物品了!"));
                            return;
                        }
                        if (coins > profileA.getCoins()) {
                            player.sendMessage(CC.translate("&c你的硬币不足!"));
                            return;
                        }
                        //A放置的硬币 + B放置的硬币 + A已交易的硬币 > 50000?
                        if (coins + tradeManager.getBCoins() + profileA.getTradeLimit().getAmount() > 50000 || coins + tradeManager.getBCoins() + profileB.getTradeLimit().getAmount() > 50000) {
                            player.sendMessage(CC.translate("&c双方其中一人的今日交易硬币量上限不允许你放置此数量的硬币!"));
                            return;
                        }
                        profileA.setCoins(profileA.getCoins() + tradeManager.getACoins() - coins);
                        tradeManager.setACoins(coins);
                    } else {
                        if (tradeManager.getBItems().size() >= 8) {
                            player.sendMessage(CC.translate("&c你无法再放入更多物品了!"));
                            return;
                        }
                        if (coins > profileB.getCoins()) {
                            player.sendMessage(CC.translate("&c你的硬币不足!"));
                            return;
                        }
                        if (coins + tradeManager.getACoins() + profileA.getTradeLimit().getAmount() > 50000 || coins + tradeManager.getACoins() + profileB.getTradeLimit().getAmount() > 50000) {
                            player.sendMessage(CC.translate("&c双方其中一人的今日交易硬币量上限不允许你放置此数量的硬币!"));
                            return;
                        }
                        profileB.setCoins(profileB.getCoins() + tradeManager.getBCoins() - coins);
                        tradeManager.setBCoins(coins);
                    }

                    tradeManager.getPlayerA().playSound(tradeManager.getPlayerA().getLocation(), Sound.VILLAGER_HAGGLE, 1, 1);
                    tradeManager.getPlayerB().playSound(tradeManager.getPlayerB().getLocation(), Sound.VILLAGER_HAGGLE, 1, 1);
                    tradeManager.setBConfirm(false);
                    tradeManager.setAConfirm(false);
                    tradeManager.setAPutCooldown(new Cooldown(5, TimeUnit.SECONDS));
                    tradeManager.setBPutCooldown(new Cooldown(5, TimeUnit.SECONDS));
                });
    }
}
