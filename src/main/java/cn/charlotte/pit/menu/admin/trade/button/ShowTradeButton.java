package cn.charlotte.pit.menu.admin.trade.button;

import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.data.TradeData;
import cn.charlotte.pit.menu.admin.trade.TradeInfoMenu;
import cn.charlotte.pit.menu.admin.trade.TradeTrackerMenu;
import cn.charlotte.pit.util.inventory.InventoryUtil;
import cn.charlotte.pit.util.item.ItemBuilder;
import cn.charlotte.pit.util.menu.Button;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.text.SimpleDateFormat;

/**
 * @Author: EmptyIrony
 * @Date: 2021/1/31 23:11
 */
public class ShowTradeButton extends Button {
    private static final SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
    private final PlayerProfile profile;
    private final TradeData data;
    private final TradeTrackerMenu menu;

    public ShowTradeButton(PlayerProfile profile, TradeData data, TradeTrackerMenu menu) {
        this.profile = profile;
        this.data = data;
        this.menu = menu;
    }

    public static boolean isTradeDataGifted(TradeData tradeData) {
        try {
            boolean isAGifted = tradeData.getAPaidCoin() <= 0 && InventoryUtil.getInventoryFilledSlots(tradeData.getAPaidItem().getContents()) <= 0;
            boolean isBGifted = tradeData.getBPaidCoin() <= 0 && InventoryUtil.getInventoryFilledSlots(tradeData.getBPaidItem().getContents()) <= 0;

            return isAGifted || isBGifted;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        ItemBuilder builder = new ItemBuilder(Material.BOOK)
                .name("&a" + format.format(data.getCompleteTime()))
                .lore(
                        "&e" + data.getPlayerAName() + " 与 &e" + data.getPlayerBName() + "&7 的交易记录",
                        //("&eItems: " + (data.getAPaidItem().getContents() == null ? "null" : InventoryUtil.getInventoryFilledSlots(data.getAPaidItem().getContents())) + " / " + (data.getBPaidItem().getContents() == null ? "null" : InventoryUtil.getInventoryFilledSlots(data.getBPaidItem().getContents()))),
                        ("&eCoins: " + data.getAPaidCoin() + " / " + data.getBPaidCoin())
                );
        if (isTradeDataGifted(data)) {
            builder.shiny();
        }
        return builder.build();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton, ItemStack currentItem) {
        new TradeInfoMenu(profile, data, menu).openMenu(player);
    }
}
