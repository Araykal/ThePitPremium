package cn.charlotte.pit.menu.trade;

import cn.charlotte.pit.menu.GlassPaneButton;
import cn.charlotte.pit.menu.trade.button.*;
import cn.charlotte.pit.util.chat.CC;
import cn.charlotte.pit.util.chat.StringUtil;
import cn.charlotte.pit.util.cooldown.Cooldown;
import cn.charlotte.pit.util.item.ItemBuilder;
import cn.charlotte.pit.util.item.ItemUtil;
import cn.charlotte.pit.util.menu.Button;
import cn.charlotte.pit.util.menu.Menu;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author: EmptyIrony
 * @Date: 2021/1/19 20:52
 */
public class TradeMenu extends Menu {
    private static final int[] myselfSlots = {0, 1, 2, 3, 9, 10, 11, 12};
    private static final int[] targetSlots = {5, 6, 7, 8, 14, 15, 16, 17};
    private static final int[] glassSlots = {4, 13, 22};

    private final TradeManager tradeManager;

    public TradeMenu(TradeManager tradeManager) {
        this.tradeManager = tradeManager;
    }

    @Override
    public String getTitle(Player player) {
        return "你                    " + (player.equals(tradeManager.getPlayerA()) ? tradeManager.getPlayerB().getDisplayName() : tradeManager.getPlayerA().getDisplayName());
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        final Map<Integer, Button> buttonMap = new HashMap<>();

        final List<ItemStack> myselfItems;
        final List<ItemStack> targetItems;
        final double myselfCoins;
        final double targetCoins;

        if (player.equals(tradeManager.getPlayerA())) {
            myselfItems = tradeManager.getAItems();
            targetItems = tradeManager.getBItems();
            myselfCoins = tradeManager.getACoins();
            targetCoins = tradeManager.getBCoins();
        } else {
            targetItems = tradeManager.getAItems();
            myselfItems = tradeManager.getBItems();
            targetCoins = tradeManager.getACoins();
            myselfCoins = tradeManager.getBCoins();
        }
        buttonMap.put(21, new SelfConfirmButton(tradeManager));
        buttonMap.put(23, new TargetConfirmButton(tradeManager));
        buttonMap.put(18, new CoinButton(tradeManager));

        if (myselfCoins > 0) {
            Material material = Material.GOLD_NUGGET;
            int amount;
            if (myselfCoins >= 1000) {
                material = Material.GOLD_INGOT;
                amount = new Double((myselfCoins - (myselfCoins % 1000)) / 1000).intValue();
            } else {
                amount = Math.max(1, new Double((myselfCoins - (myselfCoins % 100)) / 100).intValue());
            }
            buttonMap.put(0, new CoinDisplayButton(new ItemBuilder(material).name("&6" + StringUtil.getFormatLong((long) myselfCoins) + "硬币").amount(amount).shiny().build(), true, tradeManager, true));
        }

        putTradeItem(buttonMap, myselfItems, myselfCoins, myselfSlots, true);

        if (targetCoins > 0) {
            Material material = Material.GOLD_NUGGET;
            int amount;
            if (targetCoins >= 1000) {
                material = Material.GOLD_INGOT;
                amount = new Double((targetCoins - (targetCoins % 1000)) / 1000).intValue();
            } else {
                amount = Math.max(1, new Double((targetCoins - (targetCoins % 100)) / 100).intValue());
            }
            buttonMap.put(5, new CoinDisplayButton(new ItemBuilder(material).name("&6" + StringUtil.getFormatLong((long) targetCoins) + "硬币").amount(amount).shiny().build(), true, tradeManager, false));
        }

        putTradeItem(buttonMap, targetItems, targetCoins, targetSlots, false);

        for (int glassSlot : glassSlots) {
            buttonMap.put(glassSlot, new GlassPaneButton());
        }

        return buttonMap;
    }

    private void putTradeItem(Map<Integer, Button> buttonMap, List<ItemStack> myselfItems, double myselfCoins, int[] myselfSlots, boolean self) {
        for (int i = 0; i < myselfItems.size(); i++) {
            ItemStack itemStack = myselfItems.get(i);
            if (myselfCoins > 0) {
                buttonMap.put(myselfSlots[i + 1], new ItemDisplayButton(itemStack, tradeManager, self));
            } else {
                buttonMap.put(myselfSlots[i], new ItemDisplayButton(itemStack, tradeManager, self));
            }
        }
    }

    @Override
    public boolean isAutoUpdate() {
        return true;
    }

    @Override
    public void onClose(Player player) {
        if (player.equals(tradeManager.getPlayerA())) {
            if (tradeManager.isAEditingCoins()) {
                return;
            }
        }
        if (player.equals(tradeManager.getPlayerB())) {
            if (tradeManager.isBEditingCoins()) {
                return;
            }
        }
        tradeManager.onPlayerCancel(player);
    }

    @Override
    public void onClickEvent(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player && event.getClickedInventory() instanceof PlayerInventory) {
            if (event.getCurrentItem() != null && event.getCurrentItem().getType() != Material.AIR) {
                event.setCancelled(true);
                Player player = (Player) event.getWhoClicked();
                if (!ItemUtil.canTrade(event.getCurrentItem()) && !player.hasPermission("pit.admin")) {
                    player.sendMessage(CC.translate("&c此物品无法用于交易!"));
                    return;
                }
                List<ItemStack> items;
                if (player.equals(tradeManager.getPlayerA())) {
                    items = tradeManager.getAItems();
                    int limit;
                    if (tradeManager.getACoins() > 0) {
                        limit = myselfSlots.length - 1;
                    } else {
                        limit = myselfSlots.length;
                    }
                    if (items.size() >= limit) {
                        player.sendMessage(CC.translate("&c你无法再放入更多物品了!"));
                        return;
                    }
                    tradeManager.setAPutCooldown(new Cooldown(5, TimeUnit.SECONDS));
                } else {
                    items = tradeManager.getBItems();
                    int limit;
                    if (tradeManager.getBCoins() > 0) {
                        limit = myselfSlots.length - 1;
                    } else {
                        limit = myselfSlots.length;
                    }
                    if (items.size() + 1 >= limit) {
                        player.sendMessage(CC.translate("&c你无法再放入更多物品了!"));
                        return;
                    }
                }
                tradeManager.getPlayerA().playSound(tradeManager.getPlayerA().getLocation(), Sound.VILLAGER_HAGGLE, 1, 1);
                tradeManager.getPlayerB().playSound(tradeManager.getPlayerB().getLocation(), Sound.VILLAGER_HAGGLE, 1, 1);
                tradeManager.setBConfirm(false);
                tradeManager.setAConfirm(false);
                tradeManager.setAPutCooldown(new Cooldown(5, TimeUnit.SECONDS));
                tradeManager.setBPutCooldown(new Cooldown(5, TimeUnit.SECONDS));

                items.add(event.getCurrentItem());
                player.getInventory().setItem(event.getSlot(), new ItemStack(Material.AIR));

                tradeManager.openMenu(tradeManager.getPlayerA());
                tradeManager.openMenu(tradeManager.getPlayerB());
            }
        }
    }
}
