package cn.charlotte.pit.menu.viewer.button.admin;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.data.TradeData;
import cn.charlotte.pit.util.inventory.InventoryUtil;
import cn.charlotte.pit.util.item.ItemBuilder;
import cn.charlotte.pit.util.menu.Button;
import com.mongodb.client.FindIterable;
import com.mongodb.client.model.Filters;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Misoryan
 * @Created_In: 2021/3/4 18:59
 */
public class TradeDataViewerButton extends Button {
    private final PlayerProfile profile;

    public TradeDataViewerButton(PlayerProfile profile) {
        this.profile = profile;
    }

    public static boolean isTradeDataGifted(TradeData tradeData) {
        boolean isAGifted = tradeData.getAPaidCoin() <= 0 && InventoryUtil.getInventoryFilledSlots(tradeData.getAPaidItem().getContents()) <= 0;
        boolean isBGifted = tradeData.getBPaidCoin() <= 0 && InventoryUtil.getInventoryFilledSlots(tradeData.getBPaidItem().getContents()) <= 0;

        return isAGifted || isBGifted;
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        List<String> lores = new ArrayList<>();

        List<TradeData> data = new ArrayList<>();
        FindIterable<TradeData> tradeA = ThePit.getInstance()
                .getMongoDB()
                .getTradeCollection()
                .find(Filters.eq("playerA", profile.getUuid()));

        FindIterable<TradeData> tradeB = ThePit.getInstance()
                .getMongoDB()
                .getTradeCollection()
                .find(Filters.eq("playerB", profile.getUuid()));

        for (TradeData tradeData : tradeA) {
            data.add(tradeData);
        }
        for (TradeData tradeData : tradeB) {
            data.add(tradeData);
        }
        int tradeTimesInSevenDays = 0;
        int itemTransferredInSevenDays = 0;
        double coinsTransferredInSevenDays = 0;
        int tradeTimes = 0;
        int itemTransferred = 0;
        double coinsTransferred = 0;

        int giftTradeAmount = 0;
        int giftTradeInSevenDays = 0;
        for (TradeData tradeData : data) {
            if (System.currentTimeMillis() - tradeData.getCompleteTime() <= 7 * 24 * 60 * 60 * 1000) {
                tradeTimesInSevenDays++;
                for (ItemStack itemStack : tradeData.getAPaidItem().getContents()) {
                    if (itemStack != null && itemStack.getType() != Material.AIR) {
                        itemTransferredInSevenDays++;
                    }
                }
                for (ItemStack itemStack : tradeData.getBPaidItem().getContents()) {
                    if (itemStack != null && itemStack.getType() != Material.AIR) {
                        itemTransferredInSevenDays++;
                    }
                }
                coinsTransferredInSevenDays += tradeData.getAPaidCoin() + tradeData.getBPaidCoin();
            }
            tradeTimes++;
            for (ItemStack itemStack : tradeData.getAPaidItem().getContents()) {
                if (itemStack != null && itemStack.getType() != Material.AIR) {
                    itemTransferred++;
                }
            }
            for (ItemStack itemStack : tradeData.getBPaidItem().getContents()) {
                if (itemStack != null && itemStack.getType() != Material.AIR) {
                    itemTransferred++;
                }
            }
            coinsTransferred += tradeData.getAPaidCoin() + tradeData.getBPaidCoin();
            if (isTradeDataGifted(tradeData)) {
                giftTradeAmount++;
                if (System.currentTimeMillis() - tradeData.getCompleteTime() <= 7 * 24 * 60 * 60 * 1000) {
                    giftTradeInSevenDays++;
                }
            }
        }
        lores.add("&7此玩家在7天内/累计进行了以下交易:");
        lores.add("&7在 &a" + tradeTimesInSevenDays + "&7/&a" + tradeTimes + " &7次此玩家参与的交易中:");
        lores.add("&7累计 &a" + itemTransferredInSevenDays + "&7/&a" + itemTransferred + " &7件物品被转移.");
        lores.add("&7累计 &6" + coinsTransferredInSevenDays + "&7/&6" + coinsTransferred + " &7硬币被转移.");
        if (giftTradeAmount > 0) {
            lores.add(" ");
            lores.add("&7此玩家参与了 &c" + giftTradeInSevenDays + "&7/&c" + giftTradeAmount + " &7次单向交易.");
        }
        lores.add(" ");
        lores.add("&e点击追踪此玩家的交易数据!");

        return new ItemBuilder(Material.BOOK).name("&e交易数据监视器").lore(lores).build();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton, ItemStack currentItem) {
        FindIterable<TradeData> tradeA = ThePit.getInstance()
                .getMongoDB()
                .getTradeCollection()
                .find(Filters.eq("playerA", profile.getUuid()));

        FindIterable<TradeData> tradeB = ThePit.getInstance()
                .getMongoDB()
                .getTradeCollection()
                .find(Filters.eq("playerB", profile.getUuid()));

        List<TradeData> data = new ArrayList<>();
        for (TradeData tradeData : tradeA) {
            data.add(tradeData);
        }
        for (TradeData tradeData : tradeB) {
            data.add(tradeData);
        }

        ThePit.api.openTradeTrackMenu(player, profile, data);
    }
}
