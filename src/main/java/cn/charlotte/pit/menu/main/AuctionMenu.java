package cn.charlotte.pit.menu.main;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.events.EventFactory;
import cn.charlotte.pit.events.impl.AuctionEvent;
import cn.charlotte.pit.menu.main.button.AuctionBidButton;
import cn.charlotte.pit.menu.main.button.BidHistoryButton;
import cn.charlotte.pit.util.item.ItemBuilder;
import cn.charlotte.pit.util.menu.Button;
import cn.charlotte.pit.util.menu.Menu;
import cn.charlotte.pit.util.menu.buttons.DisplayButton;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Creator Misoryan
 * @Date 2021/5/25 16:46
 */
public class AuctionMenu extends Menu {
    @Override
    public String getTitle(Player player) {
        return "拍卖";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        EventFactory eventFactory = ThePit.getInstance().getEventFactory();
        //check if event is available
        if (eventFactory.getActiveNormalEvent() != null && eventFactory.getActiveNormalEventName().equals("auction")) {
            AuctionEvent event = (AuctionEvent) eventFactory.getActiveNormalEvent();
            buttons.put(11, new DisplayButton(convertAuctionIcon(event.getLots().getIcon().clone()), true));
            buttons.put(13, new BidHistoryButton());
            buttons.put(15, new AuctionBidButton());
        }
        return buttons;
    }

    @Override
    public boolean isAutoUpdate() {
        return true;
    }

    @Override
    public int getSize() {
        return 3 * 9;
    }

    @Override
    public void openMenu(Player player) {
        EventFactory eventFactory = ThePit.getInstance().getEventFactory();
        //check if event is available
        if (eventFactory.getActiveNormalEvent() != null && eventFactory.getActiveNormalEventName().equals("auction")) {
            super.openMenu(player);
        } else {
            player.closeInventory();
        }
    }

    public static ItemStack convertAuctionIcon(ItemStack itemStack) {
        if (itemStack.clone().getItemMeta().getDisplayName() != null || (itemStack.clone().getItemMeta().getLore() != null && itemStack.clone().getItemMeta().getLore().size() > 0)) {
            ItemBuilder builder = new ItemBuilder(itemStack);
            List<String> lines = new ArrayList<>();
            lines.add("&8特殊物品");
            lines.add("");
            lines.add("&e&m*----------------------*");
            final ItemMeta meta = itemStack.getItemMeta();
            if (meta.getDisplayName() != null) {
                lines.add(itemStack.getItemMeta().getDisplayName());
            }
            if (meta.getLore() != null && meta.getLore().size() > 0) {
                lines.addAll(meta.getLore());
            }
            lines.add("&e&m*----------------------*");
            builder.name("&e拍卖物品");
            builder.lore(lines);
            return builder.build();
        } else {
            return new ItemBuilder(itemStack).lore("&8拍卖物品").build();
        }
    }

}
