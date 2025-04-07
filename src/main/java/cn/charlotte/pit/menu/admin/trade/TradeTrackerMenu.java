package cn.charlotte.pit.menu.admin.trade;

import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.data.TradeData;
import cn.charlotte.pit.menu.admin.trade.button.ShowTradeButton;
import cn.charlotte.pit.util.inventory.InventoryUtil;
import cn.charlotte.pit.util.item.ItemBuilder;
import cn.charlotte.pit.util.menu.Button;
import cn.charlotte.pit.util.menu.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: EmptyIrony
 * @Date: 2021/1/31 23:07
 */
public class TradeTrackerMenu extends Menu {
    private final PlayerProfile profile;
    private final List<TradeData> data;

    private int page = 0;

    private boolean recentOnly = false;
    private boolean giftOnly = false;

    public TradeTrackerMenu(PlayerProfile profile, List<TradeData> data) {
        this.profile = profile;
        this.data = data;
    }

    //Gifted: Paid Nothing
    public static boolean isTradeDataGifted(TradeData tradeData) {
        boolean isAGifted = tradeData.getAPaidCoin() <= 0 && InventoryUtil.getInventoryFilledSlots(tradeData.getAPaidItem().getContents()) <= 0;
        boolean isBGifted = tradeData.getBPaidCoin() <= 0 && InventoryUtil.getInventoryFilledSlots(tradeData.getBPaidItem().getContents()) <= 0;

        return isAGifted || isBGifted;
    }

    @Override
    public String getTitle(Player player) {
        return profile.getPlayerName() + " 的交易记录 (" + page + ")";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttonMap = new HashMap<>();
        int num = 45 * page;

        final List<TradeData> tradeDataList = data.stream()
                .filter(tradeData -> !recentOnly || System.currentTimeMillis() - tradeData.getCompleteTime() <= 7 * 24 * 60 * 60 * 1000)
                .filter(tradeData -> !giftOnly || isTradeDataGifted(tradeData))
                .collect(Collectors.toList());

        int slot = 0;
        for (int i = 0; i < 45; i++) {
            if (tradeDataList.size() <= i + num) {
                break;
            }
            buttonMap.put(slot, new ShowTradeButton(profile, tradeDataList.get(i + num), this));
            slot++;
        }
        buttonMap.put(45, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return new ItemBuilder(Material.ARROW)
                        .name("上一页")
                        .build();
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton, ItemStack currentItem) {
                if (page == 0) {
                    return;
                }
                page--;
            }

            @Override
            public boolean shouldUpdate(Player player, int slot, ClickType clickType) {
                return true;
            }
        });

        buttonMap.put(47, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return recentOnly ? new ItemBuilder(Material.WATCH)
                        .name("&a仅显示7天内交易记录")
                        .shiny()
                        .build() : new ItemBuilder(Material.WATCH)
                        .name("&c仅显示7天内交易记录")
                        .build();

            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton, ItemStack currentItem) {
                recentOnly = !recentOnly;
            }

            @Override
            public boolean shouldUpdate(Player player, int slot, ClickType clickType) {
                return true;
            }
        });

        buttonMap.put(48, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return giftOnly ? new ItemBuilder(Material.ENDER_CHEST)
                        .name("&a仅显示单向交易")
                        .build() : new ItemBuilder(Material.CHEST)
                        .name("&c仅显示单向交易")
                        .build();

            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton, ItemStack currentItem) {
                giftOnly = !giftOnly;
            }

            @Override
            public boolean shouldUpdate(Player player, int slot, ClickType clickType) {
                return true;
            }
        });
        buttonMap.put(53, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return new ItemBuilder(Material.ARROW)
                        .name("下一页")
                        .build();
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton, ItemStack currentItem) {
                page++;
            }

            @Override
            public boolean shouldUpdate(Player player, int slot, ClickType clickType) {
                return true;
            }
        });

        return buttonMap;
    }
}
